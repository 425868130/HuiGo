package com.nchu.controller;

import com.nchu.exception.UserSessionException;
import com.nchu.service.GoodsService;
import com.nchu.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求数据暂存及读取控制器,前后端分离时将前一个页面的数据暂存到Session
 */
@RestController
@CrossOrigin
public class ParamsController {
    @Autowired
    UserSessionService sessionService;
    @Autowired
    GoodsService goodsService;

    /*商品搜索控制器*/
    @RequestMapping(value = "/searchGoods/{goodsKeyWords}", method = RequestMethod.GET)
    public String ToGoodsSearch(@PathVariable("goodsKeyWords") String keyWords, HttpServletRequest request, Model model) {
        /*前后端分离时将关键词保存到Session*/
        sessionService.addAttr(request, "goodsKeyWords", keyWords);
        /*使用服务器渲染时传递关键词到页面*/
        model.addAttribute("goodsKeyWords", keyWords);
        return "goods/searchResult";
    }

    /*跳转到商品详情页面*/
    @RequestMapping(value = "/shop/goodsInfo/{goodsId}", method = RequestMethod.GET)
    public String ToGoodsInfo(@PathVariable("goodsId") long goodsId, HttpServletRequest request, Model model) {
        sessionService.addAttr(request, "goodsId", goodsId);
        model.addAttribute("goodsId", goodsId);
        return "shop/goodsInfo";
    }

    /*跳转到团购详情页面*/
    @RequestMapping(value = "/group/groupInfo/{groupId}", method = RequestMethod.GET)
    public String ToGroupInfo(@PathVariable("groupId") Long groupId, HttpServletRequest request, Model model) {
        sessionService.addAttr(request, "groupId", groupId);
        model.addAttribute("groupId");
        return "group/groupInfo";
    }

    /*保存要结算的团购编号*/
    @RequestMapping(value = "/group/paymentSave", method = RequestMethod.GET)
    public void savePaymentGroup(HttpServletRequest request, @RequestParam("groupId") Long groupId) throws UserSessionException {
        /*判断当前用户是否登录*/
        sessionService.getUser(request);
        sessionService.addAttr(request, "paymentGroup", groupId);
    }

    /*获取要结算的团购编号*/
    @RequestMapping(value = "/group/getPaymentSave", method = RequestMethod.GET)
    public Long getPaymentGroup(HttpServletRequest request) {
        return (Long) sessionService.getAttr(request, "paymentGroup");
    }

    @RequestMapping(value = "/group/saveSaleTypeParam", method = RequestMethod.GET)
    public void saveSaleTypeParam(HttpServletRequest request, @RequestParam("saleTypeId") Long saleTypeId) {
        sessionService.addAttr(request, "saleTypeId", saleTypeId);
    }

    /*跳转页面前保存下个页面要查询数据的ID*/
    @RequestMapping(value = "/saveParams", method = RequestMethod.GET)
    public void saveParams(HttpServletRequest request, @RequestParam("paramsName") String name, @RequestParam("value") Long someId) {
        sessionService.addAttr(request, name, someId);
    }

    /*搜索结果页面通过商品id获取团购活动id保存在Session中,以便跳转到团购活动页面*/
    @RequestMapping(value = "/saveGroupIdByGoodsId")
    public void saveGroupIdByGoodsId(HttpServletRequest request, @RequestParam("goodsId") Long goodsId) {
        Long groupId = goodsService.getById(goodsId).getGroupPurchase().getId();
        sessionService.addAttr(request, "groupId", groupId);
    }
}
