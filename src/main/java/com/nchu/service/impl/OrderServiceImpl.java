package com.nchu.service.impl;

import com.nchu.dao.ExpressDeliveryDao;
import com.nchu.dao.OrderDao;
import com.nchu.entity.*;
import com.nchu.enumdef.OrderStatus;
import com.nchu.enumdef.PaymentMethod;
import com.nchu.enumdef.VoucherPrice;
import com.nchu.exception.*;
import com.nchu.service.*;
import com.nchu.util.DateUtil;
import com.nchu.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2017-9-23 18:12:02
 * 订单相关业务接口实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ExpressDeliveryDao expressDeliveryDao;
    @Autowired
    PaymentService paymentService;
    @Autowired
    VoucherService voucherService;
    @Autowired
    UserService userService;
    @Autowired
    ShopService shopService;
    @Autowired
    GroupPurchaseService groupPurchaseService;

    /**
     * TODO　创建订单
     * 新创建订单默认为未支付订单
     *
     * @param order 　订单实体
     * @return 操作结果
     */
    @Transactional
    @Override
    public Long createOrder(Order order) throws OrderException, GroupPurchaseException {
        if (order.getUser() == null || order.getGoods() == null || order.getExpressDelivery() == null) {
            throw new OrderException("异常订单,请重试");
        }
        if (order.getUser().getIslocked()) {
            throw new OrderException("账号已冻结,无法下单,请联系管理员");
        }
        /*由控制器传入的关联对象中只含有对应实体类的id属性,需要通过id重新获取对象*/
        Long goodId = order.getGoods().getId();
        Goods goods = goodsService.getById(goodId);
        synchronized (goods) {
            if (goods.getSurplusInventory() - 1 < 0) {
                throw new OrderException("库存不足,订单创建失败");
            }
            /*先将用户加入参团表中*/
            groupPurchaseService.joinGroup(order.getUser(), goods.getGroupPurchase());
            /*更新商品库存*/
            goods.setSurplusInventory(goods.getSurplusInventory() - 1);
            try {
                goodsService.updateInventory(goods);
            } catch (Exception e) {
                throw new OrderException("系统异常订单创建失败");
            }
        }
        Long expId = order.getExpressDelivery().getId();
        ExpressDelivery expressDelivery = expressDeliveryDao.get(expId);
        /*计算订单价格*/
        order.setPrice(goods.getPreferentialprice().add(expressDelivery.getPrice()));
        order.setGmtCreate(DateUtil.getCurrentTimestamp());
        order.setGmtModified(DateUtil.getCurrentTimestamp());
        order.setOrderStatus(OrderStatus.ORDER_UNPAY.name());
        try {
            return orderDao.save(order);
        } catch (Exception e) {
            throw new OrderException("订单创建失败,请重试");
        }
    }

    /**
     * TODO 获取用户指定状态的全部订单
     *
     * @param user     用户
     * @param status   订单状态
     * @param page     页码
     * @param pageSize 页面大小
     * @return 订单列表
     */
    @Override
    public List<Order> listUserOrders(User user, OrderStatus status, int page, int pageSize) throws OrderException {
        Map<String, Object> conditions = new HashMap<>();
        if (status != null) {
            conditions.put("userid", user.getId());
            conditions.put("status", status.name());
        } else {
            conditions.put("userid", user.getId());
        }
        List<Order> orderList = orderDao.searchPage(conditions, page, pageSize);
        if (orderList == null) {
            throw new OrderException("暂无相关订单");
        }
        return orderList;
    }

    /**
     * TODO 订单支付
     * 判断用户余额,支付成功修改订单状态为已支付,发送系统消息通知商家
     * 没有使用优惠券的订单达到一定金额可以获取指定价格优惠券,使用了优惠券的订单无法再获取优惠券
     *
     * @param order         待支付的订单
     * @param user          用户
     * @param paymentMethod 支付方式
     * @return 操作结果
     */
    @Transactional
    @Override
    public boolean payment(Order order, User user, PaymentMethod paymentMethod, Vouchers vouchers) throws OrderException, VouchersException, UserServiceException {
        if (order.getUser().getId() != user.getId()) {
            throw new OrderException("订单异常,支付失败");
        }
        /*如果本次支付没有使用优惠券则判断订单价格,发放优惠券*/
        if (vouchers == null) {
            vouchers = new Vouchers();
            vouchers.setUser(user);
            VoucherPrice voucherPrice = VoucherPrice.getVoucherPrice(order.getPrice());
            /*设置优惠金额和限制使用区间*/
            vouchers.setPrice(voucherPrice.getPrice());
            vouchers.setLimituse(voucherPrice.getLimt());
            /*生成随机序列号*/
            vouchers.setSeriacod(UUIDUtils.getUUID());
            voucherService.createVouchers(vouchers);
        } else if (!voucherService.isValid(vouchers)) {
            throw new VouchersException("无效的优惠券");
            /*如果订单价格达到了优惠券的限制使用价格,则可以使用*/
        } else if (order.getPrice().compareTo(vouchers.getLimituse()) >= 0) {
            /*更新订单价格*/
            order.setPrice(order.getPrice().subtract(vouchers.getPrice()));
            orderDao.update(order);
            /*使用优惠券*/
            voucherService.userVouchers(vouchers, user);
        } else {
            throw new VouchersException("未达到优惠券使用区间,请选择其他优惠券");
        }
        /*如果是账号余额支付则直接扣除账户余额*/
        if (paymentMethod == PaymentMethod.ACCOUNT) {
            /*账号余额小于订单金额支付失败*/
            if (user.getBalance().compareTo(order.getPrice()) == -1) {
                throw new OrderException("余额不足,支付失败");
            }
            user.setBalance(user.getBalance().subtract(order.getPrice()));
            userService.updateUserInfo(user);
            /*修改订单状态*/
            order.setPayMethod(paymentMethod.name());
            order.setOrderStatus(OrderStatus.ORDER_PAY.name());
            orderDao.update(order);
            return true;
        } else {
            /*第三方支付*/
            if (!paymentService.payOrder(order, paymentMethod)) {
                throw new OrderException("支付失败,请重试");
            }
            order.setOrderStatus(OrderStatus.ORDER_PAY.name());
            orderDao.update(order);
            return true;
        }
    }

    /**
     * TODO 交易取消
     * 判断订单状态
     * 订单用户需与操作者相同,取消完成后返回用户支付金额
     *
     * @param order    要取消的订单
     * @param operator 操作者
     * @return 操作结果
     */
    @Transactional
    @Override
    public boolean orderCancel(Order order, User operator) throws OrderException {
        if (order.getUser().getId() != operator.getId()) {
            throw new OrderException("用户异常,操作失败");
        }
        if (order.getOrderStatus().equals(OrderStatus.ORDER_UNPAY)) {
            /*订单未支付则直接修改订单状态*/
            order.setOrderStatus(OrderStatus.ORDER_CANCEL.name());
            orderDao.update(order);
            Goods goods = order.getGoods();
            /*返还商品库存*/
            goods.setSurplusInventory(goods.getSurplusInventory() + 1);
            goodsService.updateInventory(goods);
        } else if (order.getOrderStatus().equals(OrderStatus.ORDER_PAY)) {
            /*订单支付成功,还没有发货,执行退款操作*/
            List<Order> orderList = new ArrayList<>();
            orderList.add(order);
            refund(orderList);
        } else {
            throw new OrderException("此订单状态下不允许取消");
        }
        return true;
    }

    /**
     * TODO 更新 订单状态
     * 主要用于商家发货,用户收货等操作
     *
     * @param order       订单
     * @param orderStatus 订单新状态
     * @return 操作结果
     */
    @Transactional
    @Override
    public boolean updateOrderStatus(Order order, OrderStatus orderStatus) throws OrderException {
        order.setOrderStatus(orderStatus.name());
        try {
            orderDao.update(order);
        } catch (Exception e) {
            throw new OrderException("操作失败");
        }
        return true;
    }

    /**
     * TODO 取消所有某一件商品的已支付但未收货订单
     * 通过Order表的goodsId查询,全部购买了某商品且订单状态为已支付状态的用户,
     * 用于团购活动失败或取消后的统一订单处理,订单取消后需要返回所有用户的支付金额
     *
     * @param goods 商品,至少包含商品id
     * @return 操作结果
     */
    @Override
    public boolean cancelAllOrder(Goods goods) throws OrderException {
        Map<String, Object> condtions = new HashMap<>();
        condtions.put("goodsid", goods.getId());
        condtions.put("status", OrderStatus.ORDER_PAY.name());
        List<Order> orderList = orderDao.searchPage(condtions, 1, -1);
        if (orderList == null) {
            throw new OrderException("没有相关订单");
        }
        refund(orderList);
        return true;
    }

    /**
     * TODO 订单退款
     *
     * @param orderList 要退款的订单列表,
     * @return 操作结果
     */
    @Transactional
    @Override
    public synchronized boolean refund(List<Order> orderList) throws OrderException {

        for (Order order : orderList) {
            /*判断订单是否为已支付订单,只有已支付订单可以退款*/
            if (!order.getOrderStatus().equals(OrderStatus.ORDER_PAY)) {
                continue;
            }
            /*判断订单的支付方式*/
            if (order.getPayMethod().equals(PaymentMethod.ACCOUNT)) {
                /*账号余额支付*/
                order.setOrderStatus(OrderStatus.ORDER_CANCEL.name());
                orderDao.update(order);
                User user = order.getUser();
                user.setBalance(user.getBalance().add(order.getPrice()));
                try {
                    userService.updateUserInfo(user);
                } catch (UserServiceException e) {
                    throw new OrderException("退款失败");
                }
                /*还原商品库存*/
                Goods goods = order.getGoods();
                goods.setSurplusInventory(goods.getSurplusInventory() + 1);
                goodsService.updateInventory(goods);
            } else {
                /*第三方支付*/
                if (paymentService.refund(order, PaymentMethod.valueOf(order.getPayMethod()))) {
                    /*修改订单状态*/
                    order.setOrderStatus(OrderStatus.ORDER_CANCEL.name());
                    orderDao.update(order);
                     /*还原商品库存*/
                    Goods goods = order.getGoods();
                    goods.setSurplusInventory(goods.getSurplusInventory() + 1);
                    goodsService.updateInventory(goods);
                } else {
                    throw new OrderException("退款失败");
                }
            }
        }//for
        return true;
    }

    /**
     * TODO 获取一家店铺的全部指定状态订单
     *
     * @param shop        店铺
     * @param orderStatus 订单状态
     * @param page        页码
     * @param pageSize    页面大小,如果小于0则不分页
     * @return 订单列表
     */
    @Override
    public List<Order> getShopOrders(Shop shop, OrderStatus orderStatus, int page, int pageSize) throws ShopException, OrderException {
        shop = shopService.getById(shop.getId());
        List<Order> orderList = null;
        try {
            orderList = orderDao.getShopOrders(shop, orderStatus, page, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            // throw new OrderException("订单加载失败");
        }
        return orderList;
    }

    @Override
    public Order searchOrderById(Long id) {
        return orderDao.get(id);
    }
}
