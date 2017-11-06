package com.nchu.controller;

import com.nchu.entity.Goods;
import com.nchu.entity.Saletype;
import com.nchu.entity.Shop;
import com.nchu.entity.User;
import com.nchu.entity.dataView.PageDataView;
import com.nchu.entity.dataView.ShopDateView;
import com.nchu.exception.ShopException;
import com.nchu.exception.UserServiceException;
import com.nchu.exception.UserSessionException;
import com.nchu.service.GoodsService;
import com.nchu.service.ShopService;
import com.nchu.service.UserService;
import com.nchu.service.UserSessionService;
import com.nchu.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    ShopService shopService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    UserSessionService sessionService;
    @Autowired
    UserService userService;


    /*获取销售类型表*/
    @RequestMapping(value = "/saleTypeList", method = RequestMethod.GET)
    public List<Saletype> getSaleType() {
        return shopService.getAllSaleType();
    }

    /*获取推荐商家*/
    @RequestMapping(value = "/getHotShop", method = RequestMethod.GET)
    public List<ShopDateView> getHotShop(@RequestParam("number") int number) {
        return ShopDateView.transFromList(shopService.listAll().subList(0, number));
    }

    /*通过商品ID获取店铺信息*/
    @RequestMapping(value = "/getShopInfoByGoods/{goodsId}", method = RequestMethod.GET)
    public Shop getShopInfoByGoods(@PathVariable("goodsId") Long goodsId) {
        Goods goods = goodsService.getById(goodsId);
        return goods.getShop();
    }

    /*通过商品ID获取店铺信息*/
    @RequestMapping(value = "/getShopInfo", method = RequestMethod.GET)
    public Shop getShopInfo(HttpServletRequest request) throws UserSessionException, ShopException {
        User user = sessionService.getUser(request);
        return shopService.getByOwner(user.getId());
    }

    /*获取店铺详细信息*/
    @RequestMapping(value = "/getShopInfo/{shopId}", method = RequestMethod.GET)
    public ShopDateView getShopInfo(HttpServletRequest request, @PathVariable("shopId") Long shopId) throws ShopException {
        /*等于-1表示前端页面没有相应的值,需要从session中读取*/
        if (shopId == -1) {
            shopId = (Long) sessionService.getAttr(request, "shopInfoId");
        }
        return ShopDateView.transFrom(shopService.getById(shopId));
    }

    /****业务区*/

    /*获取全部未审核的店铺列表*/
    @RequestMapping(value = "/getUncheckShop", method = RequestMethod.GET)
    public PageDataView<ShopDateView> getUncheckShop(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        List<Shop> shopList = shopService.listAllUncheck();
        PageDataView<ShopDateView> shopPageDataView = new PageDataView<>();
        shopPageDataView.setTotalRecord((long) shopList.size());
        shopPageDataView.setPageIndex(page);
        shopPageDataView.setPageSize(pageSize);
        shopPageDataView.setDataList(ShopDateView.transFromList(PageUtil.getPage(shopList, page, pageSize)));
        return shopPageDataView;
    }

    /*店铺审核*/
    @RequestMapping(value = "/shopCheck", method = RequestMethod.GET)
    public void ShopCheck(@RequestParam("isPass") Boolean isPass, @RequestParam("shopId") Long shopId) throws ShopException {
        Shop shop = shopService.getById(shopId);
        /*2表示拒绝*/
        shop.setIsVerify((byte) (isPass ? 1 : 2));
        shopService.update(shop);
    }

    /*获取冻结或未冻结的店铺列表*/
    @RequestMapping(value = "/getShopListByLock", method = RequestMethod.GET)
    public PageDataView<ShopDateView> getBusiUserList(@RequestParam("status") Boolean status, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        List<Shop> shopList = shopService.listAll().stream().filter(shop -> shop.isIslocked() == status).collect(Collectors.toList());
        PageDataView<ShopDateView> shopDataView = new PageDataView<>();
        shopDataView.setPageIndex(page);
        shopDataView.setPageSize(pageSize);
        shopDataView.setTotalRecord((long) shopList.size());
        shopDataView.setDataList(PageUtil.getPage(shopList, page, pageSize));
        return shopDataView;
    }

    /*修改店铺的状态*/
    @RequestMapping(value = "/shopLock", method = RequestMethod.GET)
    public void changeUserLockStatus(@RequestParam("userId") Long ShopId, @RequestParam("isLock") Boolean isLock) throws UserServiceException, ShopException {
        Shop shop = shopService.getById(ShopId);
        shop.setIslocked(isLock);
        shopService.update(shop);
    }
}
