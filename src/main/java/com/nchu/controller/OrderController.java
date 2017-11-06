package com.nchu.controller;

import com.nchu.entity.Order;
import com.nchu.entity.Shop;
import com.nchu.entity.User;
import com.nchu.entity.dataView.PayMethodDataView;
import com.nchu.entity.dataView.PayOrderDataView;
import com.nchu.exception.*;
import com.nchu.service.*;
import com.nchu.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单页面控制器
 *
 * @author 42586
 */
@CrossOrigin
@RequestMapping("/order")
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserSessionService sessionService;
    @Autowired
    UserService userService;
    @Autowired
    GroupPurchaseService groupPurchaseService;
    @Autowired
    GoodsService goodsService;

    /*创建订单*/
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public Long createOrder(HttpServletRequest request, @RequestBody Order order) throws OrderException, UserSessionException, GroupPurchaseException {
        order.setUser(sessionService.getUser(request));
        /*创建订单*/
        return orderService.createOrder(order);
    }

    /*获取所有支付方式列表*/
    @RequestMapping(value = "/getPaymentMethods", method = RequestMethod.GET)
    public PayMethodDataView[] getPaymentMethods() {
        return PayMethodDataView.getAll();
    }

    /*TODO 订单支付*/
    @RequestMapping(value = "/orderPayment", method = RequestMethod.POST)
    public Boolean orderPayment(HttpServletRequest request, @RequestBody PayOrderDataView orderDataView) throws UserSessionException, VouchersException, UserServiceException, OrderException {
        User user = sessionService.getUser(request);
        if (!MD5Util.validate(orderDataView.getPassword(), user.getPassword())) {
            throw new UserServiceException("密码输入错误!");
        }
        Order order = orderService.searchOrderById(orderDataView.getOrderId());
        orderService.payment(order, user, orderDataView.getPaymentMethod(), null);
        return true;
    }

    @RequestMapping(value = "/getOrderByShop")
    public List<Order> getOrderByShop(@RequestParam("shopId") Long shopId) throws OrderException, ShopException {
        Shop shop = new Shop();
        shop.setId(shopId);
        return orderService.getShopOrders(shop, null, 1, -1);
    }
}
