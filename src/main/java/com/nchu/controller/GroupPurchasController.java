package com.nchu.controller;

import com.nchu.entity.*;
import com.nchu.entity.dataView.PageDataView;
import com.nchu.entity.dataView.SimpleGroupView;
import com.nchu.exception.GroupPurchaseException;
import com.nchu.exception.ShopException;
import com.nchu.exception.UserSessionException;
import com.nchu.service.*;
import com.nchu.service.impl.ExpressServiceImpl;
import com.nchu.util.DateUtil;
import com.nchu.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 团购活动页面控制器
 *
 * @author 42586
 */
@RestController
@RequestMapping("/group")
@CrossOrigin
public class GroupPurchasController {
    @Autowired
    GroupPurchaseService groupPurchaseService;
    @Autowired
    UserSessionService sessionService;
    @Autowired
    ShopService shopService;
    @Autowired
    UserService userService;
    @Autowired
    ExpressServiceImpl expressService;
    @Autowired
    GoodsService goodsService;

    /*获取首页轮播图的热门活动*/
    @RequestMapping(value = "/indexHotGroup", method = RequestMethod.GET)
    public List<SimpleGroupView> getIndexHotGroup() {
        return SimpleGroupView.transFromGroupList(groupPurchaseService.topGroup(4));
    }

    /*获取首页团购活动列表*/
    @RequestMapping(value = "/indexGroupList", method = RequestMethod.GET)
    public PageDataView<SimpleGroupView> getIndexGroupList(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("totalRecord") Long totalRecord) {
        /*先进行一次非分页查询获取总记录条数*/
        List<GroupPurchase> groupPurchaseList = groupPurchaseService.listAllGroup(page, -1);
        if (totalRecord == 0) {
            totalRecord = Long.valueOf(groupPurchaseList.size());
        }
        /*转换成简单数据信息*/
        List<SimpleGroupView> simpleGroupView = SimpleGroupView.transFromGroupList(groupPurchaseList.subList((page - 1) * pageSize, page * pageSize > groupPurchaseList.size() ? groupPurchaseList.size() : page * pageSize));
        return new PageDataView<>(page, pageSize, totalRecord, simpleGroupView, "首页团购列表");
    }

    /*获取总页数*/
    @RequestMapping(value = "/groupListPageAccount", method = RequestMethod.GET)
    public Long getPageAccount(@RequestParam("pageSize") int pageSize) {
        return groupPurchaseService.groupPageAccount(pageSize);
    }

    /*获取团购详细信息*/
    @RequestMapping(value = "/getGroupInfo", method = RequestMethod.GET)
    public GroupPurchase getGroupInfo(@RequestParam("groupId") Long groupId_web, HttpServletRequest httpRequest) throws GroupPurchaseException {
        Long groupId;
        /*如果前端没有传递id数据则尝试从Session中获取(前一个页面跳转时保存)*/
        if (groupId_web != -1) {
            groupId = groupId_web;
        } else {
            groupId = (Long) sessionService.getAttr(httpRequest, "groupId");
        }
        if (groupId == null) {
            return null;
        }
        return groupPurchaseService.getById(groupId);
    }

    /*获取服务器时间*/
    @RequestMapping("/serverTime")
    public Timestamp getServerTime() {
        return DateUtil.getCurrentTimestamp();
    }

    /*参与团购活动*/
    @RequestMapping(value = "/joinGroup", method = RequestMethod.GET)
    public Boolean joinGroup(@RequestParam("groupId") Long groupId, HttpServletRequest request) throws UserSessionException, GroupPurchaseException {
        User user = sessionService.getUser(request);
        groupPurchaseService.joinGroup(user, groupPurchaseService.getById(groupId));
        return true;
    }

    /*获取分类团购信息*/
    @RequestMapping(value = "/groupBySaleType/{saleTypeId}", method = RequestMethod.GET)
    public PageDataView<SimpleGroupView> getGroupBySaleType(@PathVariable("saleTypeId") Long saleTypeId,
                                                            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("totalRecord") Long totalRecord, HttpServletRequest request) {

        if (saleTypeId == -1) {
            saleTypeId = (Long) sessionService.getAttr(request, "saleTypeId");
        }
        Saletype saletype = shopService.getSaleTypeById(saleTypeId);
        List<GroupPurchase> groupPurchaseList = groupPurchaseService.searchByType(saletype, page, -1);
        /*如果总记录条数为0则进行一次非分页查询获取总记录条数*/
        if (totalRecord == 0) {
            totalRecord = Long.valueOf(groupPurchaseList.size());
        }
        /*转换成简单数据信息*/
        List<SimpleGroupView> simpleGroupView = SimpleGroupView.transFromGroupList(groupPurchaseList.subList((page - 1) * pageSize, page * pageSize > groupPurchaseList.size() ? groupPurchaseList.size() : page * pageSize));
        return new PageDataView<>(page, pageSize, totalRecord, simpleGroupView, saletype.getName());
    }

    /*获取指定店铺中的全部团购*/
    @RequestMapping(value = "/getGroupByShop")
    public PageDataView<SimpleGroupView> getGroupByShop(@RequestParam("shopInfoId") Long shopInfoId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize) throws ShopException {
        Shop shop = shopService.getById(shopInfoId);
        PageDataView<SimpleGroupView> pageDataView = new PageDataView<>();
        /*获取店铺中全部团购活动*/
        List<GroupPurchase> groupPurchaseList = shop.getGoods().stream().map(goods -> goods.getGroupPurchase()).collect(Collectors.toList());
        /*分页并转换为简单数据视图*/
        List<SimpleGroupView> simpleGroupViewList = SimpleGroupView.transFromGroupList(groupPurchaseList.subList((page - 1) * pageSize, page * pageSize > groupPurchaseList.size() ? groupPurchaseList.size() : page * pageSize));
        pageDataView.setDataList(simpleGroupViewList);
        pageDataView.setPageIndex(page);
        pageDataView.setPageSize(pageSize);
        pageDataView.setTotalRecord((long) groupPurchaseList.size());
        return pageDataView;
    }

    /*获取当前登录的商家的全部团购活动*/
    @RequestMapping(value = "/getBusinessGroup", method = RequestMethod.GET)
    public List<GroupPurchase> getBusinessGroup(HttpServletRequest request, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize) throws ShopException, UserSessionException {
        Shop shop = shopService.getByOwner(sessionService.getUser(request).getId());
        /*获取店铺中全部团购活动*/
        List<GroupPurchase> groupPurchaseList = shop.getGoods().stream().map(goods -> goods.getGroupPurchase()).collect(Collectors.toList());
        if (groupPurchaseList.size() == 0) {
            return null;
        }
        return groupPurchaseList;
    }

    /*添加团购活动*/
    @RequestMapping(value = "/addGroupPurchas", method = RequestMethod.POST)
    public boolean addGroupPurchas(@RequestBody GroupPurchase groupPurchase, HttpServletRequest request) throws UserSessionException, GroupPurchaseException {
        groupPurchase.setUser(userService.getUserById(sessionService.getUser(request).getId()));
        Goods goods = goodsService.getById(groupPurchase.getGoods().getId());
        groupPurchase.setGoods(goods);
        System.out.println(groupPurchase.getStartTime());

        return groupPurchaseService.createGroup(groupPurchase);
    }

    @RequestMapping(value = "/getGroupList", method = RequestMethod.GET)
    public List<GroupPurchase> getGroupList(HttpServletRequest request) throws UserSessionException {
        User user = sessionService.getUser(request);
        return groupPurchaseService.getGroupByUserId(user.getId());
    }

    @RequestMapping(value = "/upGroupPurchase{groupId}", method = RequestMethod.GET)
    public GroupPurchase upGroupPuerchase(@PathVariable("groupId") long groupId, HttpServletRequest request) {
        return groupPurchaseService.get(groupId);
    }

    @RequestMapping(value = "/delGroupPurchase{groupId}", method = RequestMethod.GET)
    public void delGroupPurchase(@PathVariable("groupId") long groupId, HttpServletRequest request) {
        groupPurchaseService.delGroupPurchase(groupId);
    }

    @RequestMapping(value = "/getAllExpress", method = RequestMethod.GET)
    public List<ExpressDelivery> getAllExpress() {
        return expressService.getExpressDelivery();
    }
}
