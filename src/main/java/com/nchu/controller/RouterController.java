package com.nchu.controller;

import com.nchu.entity.ReceivingAddress;
import com.nchu.entity.User;
import com.nchu.exception.UserServiceException;
import com.nchu.exception.UserSessionException;
import com.nchu.service.UserService;
import com.nchu.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

/**
 * 路由控制器,负责页面的跳转
 */
@CrossOrigin
@Controller
public class RouterController {
    @Autowired
    UserService userService;
    @Autowired
    UserSessionService sessionService;

    /*首页*/
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    /*管理员*/
    @RequestMapping(value = "/admin")
    public String admin() {
        return "admin/manager";
    }

    /*用户登录*/
    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public String login() {
        return "user/login";
    }

    /*用户注销*/
    @RequestMapping("user/logout")
    public String logout(HttpServletRequest request) {
        sessionService.removeUser(request);
        return "user/login";
    }
    /*会员注册*/
    @RequestMapping(value = "/user/vipReg", method = RequestMethod.GET)
    public String vipReg() {
        return "user/vipReg";
    }

    /*商家注册*/
    @RequestMapping(value = "/user/businessReg", method = RequestMethod.GET)
    public String businessReg() {
        return "user/businessReg";
    }

    /*用户个人中心*/
    @RequestMapping(value = "/user/userZone", method = RequestMethod.GET)
    public String ToUserZone() {
        return "user/userZone";
    }

    /*获取页脚元素*/
    @RequestMapping(value = "/ui/footer", method = RequestMethod.GET)
    public String getFooter() {
        return "fragment/footer";
    }


    /*用户邮箱验证*/
    @RequestMapping(value = "/user/userAuthentication", method = RequestMethod.GET)
    public String userAuthentication(Model model, @RequestParam("action") String action, @RequestParam("account") String account,
                                     @RequestParam("email") String email, @RequestParam("checkCode") String checkCode) {

        System.out.println("验证中" + action);
        /*判断是否为激活请求*/
        if ("activate".equals(action)) {
            try {
                userService.Authentication(userService.getUserByAccount(account), email, checkCode);
            } catch (UserServiceException e) {
                model.addAttribute("msg", e.getMessage());
                return "user/userAuthentication";
            }
        }
        model.addAttribute("msg", "验证成功");
        return "user/userAuthentication";
    }

    /*商品搜索控制器*/
    @RequestMapping(value = "/searchGoods", method = RequestMethod.GET)
    public String ToGoodsSearch() {
        return "goods/searchResult";
    }

    /*跳转到商品详情页面*/
    @RequestMapping(value = "/shop/goodsInfo", method = RequestMethod.GET)
    public String ToGoodsInfo() {
        return "shop/goodsInfo";
    }

    /*跳转到团购详情页面*/
    @RequestMapping(value = "/group/groupInfo", method = RequestMethod.GET)
    public String ToGroupInfo() {
        return "group/groupInfo";
    }

    @RequestMapping(value = "/personal_Address", method = RequestMethod.GET)
    public String personal_Address() {
        return "user/personal_Address";
    }

    @RequestMapping(value = "/personal_Information", method = RequestMethod.GET)
    public String personal_Information() {
        return "user/personal_Information";
    }

    /*订单支付页面*/
    @RequestMapping(value = "/order/payment", method = RequestMethod.GET)
    public String ToPayment(HttpServletRequest request, Model model) {
        model.addAttribute("paymentGroup", sessionService.getAttr(request, "paymentGroup"));
        return "order/payMent";
    }


    /*跳转到团购分类页面*/
    @RequestMapping(value = "/group/saleTypePage", method = RequestMethod.GET)
    public String ToSaleTypePage(HttpServletRequest request, Model model) {
        model.addAttribute("saleTypeId", sessionService.getAttr(request, "saleTypeId"));
        return "group/saleType";
    }

    /*获取用户收货地址*/
    @RequestMapping(value = "/express/{userAccount}", method = RequestMethod.GET)
    public String[] getRecAddress(@PathVariable("userAccount") String userAccount, HttpServletRequest request) throws UserServiceException, UserSessionException {
        User user = sessionService.getUser(request);
        System.out.println("aaa" + user.getId());

        Iterator<ReceivingAddress> it = user.getReceivingAddress().iterator();

        String[] str = new String[user.getReceivingAddress().size()];
        int i = 0;
        while (it.hasNext()) {
            str[i] = it.next().getAddress();
            System.out.println(str[i].toString());
            i++;
        }
        return str;
    }

    /*跳转到店铺详情页面*/
    @RequestMapping(value = "/shop/shopInfo", method = RequestMethod.GET)
    public String ToShopInfo(HttpServletRequest request, Model model) {
        model.addAttribute("shopInfoId", sessionService.getAttr(request, "shopInfoId"));
        return "shop/shopInfo";
    }

    /******************************************************/
    @RequestMapping(value = "/user/businessInfoManage", method = RequestMethod.GET)
    public String businessInfoManage() {
        return "user/businessInfoManage";
    }

    @RequestMapping(value = "/user/businessGoodsInfo", method = RequestMethod.GET)
    public String businessGoodsInfo() {
        return "user/businessGoodsInfo";
    }

    @RequestMapping(value = "/user/businessEvaluation", method = RequestMethod.GET)
    public String businessEvaluation() {
        return "user/businessEvaluation";
    }

    @RequestMapping(value = "/user/businessGroupPurchase", method = RequestMethod.GET)
    public String businessGroupPurchase() {
        return "user/businessGroupPurchase";
    }

    @RequestMapping(value = "user/businessOrderManage", method = RequestMethod.GET)
    public String businessOrderManage() {
        return "user/businessOrderManage";
    }
}
