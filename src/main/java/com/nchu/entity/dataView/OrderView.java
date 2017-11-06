package com.nchu.entity.dataView;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.nchu.entity.Order;
import com.nchu.enumdef.OrderStatus;

/**
 * 界面返回订单视图类
 */
public class OrderView {

    /*订单编号*/
    private Long id;
    /*用户编号*/
    private Long userId;
    /*订单商品名称*/
    private String name;
    /*商品编号*/
    private Long goodId;
    /*订单商品原价*/
    private BigDecimal originalPrice;
    /*订单商品团购价*/
    private BigDecimal preferentialPrice;
    /*订单实际付款*/
    private BigDecimal payPrice;
    /*订单状态*/
    private String status;
    /*订单创建时间*/
    private Timestamp gmtCreate;
    /*商家联系电话*/
    private String telephone;
    /*图片路径*/
    private String picture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getPreferentialPrice() {
        return preferentialPrice;
    }

    public void setPreferentialPrice(BigDecimal preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static OrderView getOrderViewOne(Order orders) {
        OrderView orderView = new OrderView();
        orderView.setId(orders.getId());
        orderView.setName(orders.getGoods().getName());
        orderView.setOriginalPrice(orders.getGoods().getOriginalprice());
        orderView.setPreferentialPrice(orders.getGoods().getPreferentialprice());
        orderView.setPayPrice(orders.getPrice());
        orderView.setGoodId(orders.getGoods().getId());
        orderView.setGmtCreate(orders.getGmtCreate());
        orderView.setTelephone(orders.getGoods().getShop().getServiceTel());
        orderView.setPicture(orders.getGoods().getPicture());
        orderView.setUserId(orders.getUser().getId());
        if (orders.getOrderStatus().equals(OrderStatus.ORDER_SUCCESS.toString())) {
            orderView.setStatus("交易成功");
        } else if (orders.getOrderStatus().equals(OrderStatus.ORDER_PAY.toString())) {
            orderView.setStatus("等待发货");
        } else if (orders.getOrderStatus().equals(OrderStatus.ORDER_SENDOUT.toString())) {
            orderView.setStatus("等待收货");
        } else {
            orderView.setStatus("其它");
        }
        return orderView;
    }

    public static List<OrderView> getOrderView(List<Order> orders) {
        List<OrderView> orderViews = new ArrayList<OrderView>();
        for (int i = 0; i < orders.size(); i++) {
            OrderView orderView = new OrderView();
            orderView.setId(orders.get(i).getId());
            orderView.setName(orders.get(i).getGoods().getName());
            orderView.setOriginalPrice(orders.get(i).getGoods().getOriginalprice());
            orderView.setPreferentialPrice(orders.get(i).getGoods().getPreferentialprice());
            orderView.setPayPrice(orders.get(i).getPrice());
            orderView.setGoodId(orders.get(i).getGoods().getId());
            orderView.setGmtCreate(orders.get(i).getGmtCreate());
            orderView.setTelephone(orders.get(i).getGoods().getShop().getServiceTel());
            orderView.setPicture(orders.get(i).getGoods().getPicture());
            if (orders.get(i).getOrderStatus().equals(OrderStatus.ORDER_SUCCESS.toString())) {
                orderView.setStatus("交易成功");
            } else if (orders.get(i).getOrderStatus().equals(OrderStatus.ORDER_PAY.toString())) {
                orderView.setStatus("等待发货");
            } else if (orders.get(i).getOrderStatus().equals(OrderStatus.ORDER_SENDOUT.toString())) {
                orderView.setStatus("等待收货");
            } else {
                orderView.setStatus("其它");
            }
            orderViews.add(orderView);
        }
        return orderViews;
    }
}
