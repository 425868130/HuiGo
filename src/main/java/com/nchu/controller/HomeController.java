package com.nchu.controller;

import com.nchu.dao.OrderDao;
import com.nchu.dao.UserDao;
import com.nchu.dao.VouchersDao;
import com.nchu.entity.*;
import com.nchu.enumdef.UserRoleType;
import com.nchu.exception.UserSessionException;
import com.nchu.service.UserSessionService;
import com.nchu.enumdef.OrderStatus;
import com.nchu.util.DateUtil;
import com.nchu.util.MD5Util;
import com.nchu.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    UserDao userDao;
    @Autowired
    VouchersDao vouchersDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    UserSessionService sessionService;

    //跳转到用户个人收货地址界面
    @RequestMapping(value = "/personal_Address")
    public String personal_Address() {
        return "user/personal_Address";
    }

    //跳转到用户个人信息界面
    @RequestMapping(value = "/personal_Information")
    public String personal_Information() {
        return "user/personal_Information";
    }

    //跳转到用户个人账户设置界面
    @RequestMapping(value = "/personal_Set")
    public String personal_Set() {
        return "user/personal_Set";
    }

    //跳转到用户个人订单界面
    @RequestMapping(value = "/personal_Order")
    public String personal_Order() {
        return "user/personal_Order";
    }

    //跳转到用户个人评价界面
    @RequestMapping(value = "/personal_Evaluation")
    public String personal_Evaluation() {
        return "user/personal_Evaluation";
    }

    //跳转到用户个人收藏界面
    @RequestMapping(value = "/personal_Favorites")
    public String personal_Favorites() {
        return "user/personal_Favorites";
    }

    @RequestMapping(value = "/user/test", method = RequestMethod.POST)
    public String submit_register_user(HttpServletRequest request) {
        System.out.println("这里");
        System.out.println(request.getParameter("keyword"));
        return "user/login";
    }

    private void saveuserTest() {
        User user = new User();
        user.setAccount("ADMIN");
        user.setPassword(MD5Util.encode2hex("1320074071"));
        //user.setRelname("ADMIN");
        //user.setNickName("ADMIN");
        user.setEmail("ADMIN@qq.com");
        user.setTelephone("12334564");
        user.setGmtCreate(DateUtil.getCurrentTimestamp());
        user.setGmtModified(DateUtil.getCurrentTimestamp());
        user.setCheckcode(MD5Util.encode2hex(UUIDUtils.getUUID() + DateUtil.getCurrentTime()));
        user.setBalance(BigDecimal.valueOf(12.3));
        user.setRole(UserRoleType.CUSTOMER.name());
        userDao.save(user);
    }

    private void testdao() {
        Vouchers vouchers = new Vouchers();
        vouchers.setGmtCreate(DateUtil.getCurrentTimestamp());
        vouchers.setGmtModified(DateUtil.getCurrentTimestamp());
        User temp = new User();
        temp.setId(1L);
        vouchers.setUser(temp);
        vouchers.setIndate(DateUtil.getCurrentTimestamp());
        vouchers.setPrice(BigDecimal.valueOf(12.3));
        vouchers.setLimituse(BigDecimal.valueOf(130));
        vouchers.setSeriacod(MD5Util.encode2hex(UUIDUtils.getUUID()));
        vouchers.setStatus("1");
        vouchers.setIsused(false);
        vouchersDao.save(vouchers);
    }

   /* void testOrderDao() {
        Order o = new Order();
        User u = new User();
        u.setId(1L);
        o.setUser(u);
        Goods g = new Goods();
        g.setId(1L);
        o.setGoods(g);
        ExpressDelivery ed = new ExpressDelivery();
        ed.setId(1L);
        o.setExpressDelivery(ed);
        o.setOrderStatus(OrderStatus.ORDER_UNPAY.name());
        Timestamp d = new Timestamp(System.currentTimeMillis());
        o.setGmtCreate(d);
        o.setGmtModified(d);

        o.setPrice(BigDecimal.valueOf(10));
        o.setOrderStatus(os);
        o.setRemark("5");
        orderDao.save(o);

    }*/
}
