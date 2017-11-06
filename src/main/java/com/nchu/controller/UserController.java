package com.nchu.controller;

import com.nchu.entity.*;
import com.nchu.entity.dataView.*;
import com.nchu.enumdef.OrderStatus;
import com.nchu.enumdef.UserRoleType;
import com.nchu.exception.*;
import com.nchu.service.*;
import com.nchu.util.MD5Util;
import com.nchu.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Set;

/**
 * 2017-9-25 15:30:47
 * 用户账户相关控制器
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserSessionService sessionService;
    @Autowired
    ShopService shopService;
    @Autowired
    ReceivingAddressService receivingAddressService;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    EvaluationService evaluationService;
    @Autowired
    FavoriteService favoriteService;
    @Autowired
    GroupPurchaseService groupPurchaseService;
    @Autowired
    @Qualifier("ossFileService")
    FileService fileService;
    @Autowired
    AnnounceService announceService;
    /*
     * 用户登录验证
     */

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Boolean login(@RequestBody UserRegDataView userDataView, HttpServletRequest request) throws ServiceException {
        /*如果用户已经登录则直接通过*/
        User loginUser;
        try {
            loginUser = sessionService.getUser(request);
            /*验证已经登录的用户信息*/
            if (userDataView.getAccount().equals(loginUser.getAccount()) && MD5Util.validate(userDataView.getPassword(), loginUser.getPassword())) {
                return true;
            } else {
                throw new UserServiceException("用户名或密码不正确");
            }
        } catch (UserSessionException e) {
            /*出现异常说明用户未登录,则通过数据库进行验证*/
            /*从请求中的数据视图转换成user对象*/
            loginUser = UserRegDataView.transToUser(userDataView);
            /*从新指向持久化对象*/
            loginUser = userService.login(loginUser);
            sessionService.addUser(loginUser, request);
            return true;
        }
    }
    /*
     * 管理员登录验证
     */

    @RequestMapping(value = "/AdminLogin", method = RequestMethod.POST)
    public Boolean AdminLogin(@RequestBody UserRegDataView userDataView, HttpServletRequest request) throws ServiceException {
        /*如果用户已经登录则直接通过*/
        User loginUser;
        try {
            loginUser = sessionService.getUser(request);
            /*验证已经登录的用户信息*/
            if (!loginUser.getRole().equals(UserRoleType.ADMIN)) {
                throw new UserServiceException("非法登录!");
            } else if (userDataView.getAccount().equals(loginUser.getAccount()) && MD5Util.validate(userDataView.getPassword(), loginUser.getPassword())) {
                return true;
            } else {
                throw new UserServiceException("用户名或密码不正确");
            }
        } catch (UserSessionException e) {
            /*出现异常说明用户未登录,则通过数据库进行验证*/
            /*从请求中的数据视图转换成user对象*/
            loginUser = UserRegDataView.transToUser(userDataView);
            /*从新指向持久化对象*/
            loginUser = userService.login(loginUser);
            sessionService.addUser(loginUser, request);
            return true;
        }
    }



    /*会员注册*/
    @RequestMapping(value = "/vipReg", method = RequestMethod.POST)
    public Boolean register(@RequestBody UserRegDataView userDataView) throws UserServiceException, MessagingException {
        userService.register(UserRegDataView.transToUser(userDataView));
        return true;
    }

    /*商家注册*/
    @RequestMapping(value = "/businessReg", method = RequestMethod.POST)
    public Boolean businessReg(@RequestBody BusinessRegForm form) throws UserServiceException, MessagingException, ShopException {
        /*获取用户注册信息*/
        User user = form.getUser();
        userService.register(user);
        /*获取店铺注册信息*/
        Shop shop = form.getShop();
        shop.setUser(user);
        shopService.createShop(shop);
        System.out.println(form.getAccount());
        return true;
    }


    /*修改用户密码*/
    @RequestMapping(value = "/modify_password/{account}", method = RequestMethod.GET)
    public User modifyUserPasswordByAccount(@PathVariable("account") String account, HttpServletRequest request) throws ServiceException {
        User user = userService.getUserByAccount(account);

        if (MD5Util.validate(request.getParameter("oldPassword"), user.getPassword())) {
            user.setPassword(MD5Util.encode2hex(request.getParameter("newPassword")));
            userService.updateUserInfo(user);
            return user;
        } else
            return null;
    }

    /*绑定用户手机*/
    @RequestMapping(value = "/bind_telephone/{account}", method = RequestMethod.GET)
    public User bindUserTelephoneByAccount(@PathVariable("account") String account, HttpServletRequest request) throws ServiceException {
        User user = userService.getUserByAccount(account);
        user.setTelephone(request.getParameter("newTelephone"));
        userService.updateUserInfo(user);
        return user;
    }

    /*修改用户个人信息*/
    @RequestMapping(value = "/modify_info/{account}", method = RequestMethod.POST)
    public User modifyUserInfoByAccount(@PathVariable("account") String account, HttpServletRequest request) throws ServiceException {
        User user = userService.getUserByAccount(account);
        user.setNickName(request.getParameter("inputNickName"));
        user.setRelname(request.getParameter("inputRelName"));
        user.setSex(Boolean.parseBoolean(request.getParameter("sexRadio")));
        user.setBirthday(Date.valueOf(request.getParameter("inputBirthday")));
        userService.updateUserInfo(user);
        return user;
    }

    /*修改用户头像*/
    @RequestMapping(value = "/modify_HeadPortrait/{account}", method = RequestMethod.POST)
    public User getUserInfoByAccount(@RequestParam("myHeadPortrait") MultipartFile file, @PathVariable("account") String account, HttpServletRequest request) throws ServiceException, IOException {
        System.out.println("进来了");
        User user = userService.getUserByAccount(account);
        String url = fileService.fileUpload("cache", file);
        user.setHeadportrait(url);
        userService.updateUserInfo(user);
        return user;
    }


    /*获取用户个人信息*/
    @RequestMapping(value = "/info/{account}", method = RequestMethod.GET)
    public User getUserInfoByAccount(@PathVariable("account") String account) throws ServiceException {
        User user = userService.getUserByAccount(account);
        return user;
    }

    /*获取当前在线用户的个人信息*/
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public User getLoginUserInfo(HttpServletRequest request) throws ServiceException {
        User user = sessionService.getUser(request);
        return user;
    }

    /*获取当前登录用户的收货地址*/
    @RequestMapping("/receivingAddress")
    public Set<ReceivingAddress> getUserReceivingAddress(HttpServletRequest request) throws UserSessionException {
        User user = userService.getUserById(sessionService.getUser(request).getId());
        return user.getReceivingAddress();
    }

    /*获取用户收货地址*/
    @RequestMapping(value = "/receivingAddress/{userAccount}", method = RequestMethod.GET)
    public Set<ReceivingAddress> getReceivingAddress(@PathVariable("userAccount") String userAccount, HttpServletRequest request) throws UserServiceException {
        User user = userService.getUserByAccount(userAccount);
        return user.getReceivingAddress();
    }

    /*根据地址id获取用户收货地址*/
    @RequestMapping(value = "/getReceivingAddressById/{Id}", method = RequestMethod.GET)
    public ReceivingAddress getReceivingAddressById(@PathVariable("Id") String Id, HttpServletRequest request) {
        return receivingAddressService.getReceivingAddress(Long.valueOf(Id));
    }

    /*增加用户收货地址*/
    @RequestMapping(value = "/add_receivingAddress", method = RequestMethod.POST)
    public void addReceivingAddress(HttpServletRequest request) throws UserServiceException, ReceivingAddressException {
        User user = userService.getUserById(Long.valueOf(request.getParameter("userId")));
        ReceivingAddress receivingAddress = new ReceivingAddress();
        receivingAddress.setName(request.getParameter("name"));
        receivingAddress.setTel(request.getParameter("telephone"));
        receivingAddress.setAddress(request.getParameter("address"));
        receivingAddress.setUser(user);
        receivingAddressService.saveReceivingAddress(receivingAddress);
    }

    /*修改用户收货地址*/
    @RequestMapping(value = "/modify_receivingAddress", method = RequestMethod.POST)
    public void modifyReceivingAddress(HttpServletRequest request) throws UserServiceException, ReceivingAddressException {
        if (!request.getParameter("name").equals("") && !request.getParameter("telephone").equals("") && !request.getParameter("address").equals("")) {
            ReceivingAddress receivingAddress = receivingAddressService.getReceivingAddress(Long.valueOf(request.getParameter("id")));
            receivingAddress.setName(request.getParameter("name"));
            receivingAddress.setTel(request.getParameter("telephone"));
            receivingAddress.setAddress(request.getParameter("address"));
            receivingAddressService.updateReceivingAddress(receivingAddress);
        }
    }

    /*删除用户收货地址*/
    @RequestMapping(value = "/delete_receivingAddress", method = RequestMethod.POST)
    public void deleteReceivingAddress(HttpServletRequest request) throws ReceivingAddressException {
        receivingAddressService.deleteReceivingAddress(Long.valueOf(request.getParameter("Id")));
    }

    /*获取用户订单信息*/
    @RequestMapping(value = "/get_Order", method = RequestMethod.POST)
    public List<OrderView> getOrder(HttpServletRequest request) throws NumberFormatException, OrderException {
        User user = userService.getUserById(Long.valueOf(request.getParameter("userId")));

        if (request.getParameter("status").equals(OrderStatus.ORDER_PAY.toString())) {
            return OrderView.getOrderView(orderService.listUserOrders(user, OrderStatus.ORDER_PAY, Integer.parseInt(request.getParameter("page")), Integer.parseInt(request.getParameter("pageSize"))));
        } else if (request.getParameter("status").equals(OrderStatus.ORDER_SENDOUT.toString())) {
            return OrderView.getOrderView(orderService.listUserOrders(user, OrderStatus.ORDER_SENDOUT, Integer.parseInt(request.getParameter("page")), Integer.parseInt(request.getParameter("pageSize"))));
        } else if (request.getParameter("status").equals("ORDER_ALL")) {
            return OrderView.getOrderView(orderService.listUserOrders(user, null, Integer.parseInt(request.getParameter("page")), Integer.parseInt(request.getParameter("pageSize"))));
        }
        return null;
    }

    /*用户订单评论*/
    @RequestMapping(value = "/add_evaluation", method = RequestMethod.POST)
    public void evaluationOrder(HttpServletRequest request) throws ServiceException {
        Evaluation evaluation = new Evaluation();
        User user = userService.getUserById(Long.valueOf(request.getParameter("userId")));
        Goods goods = goodsService.getById(Long.valueOf(request.getParameter("goodId")));
        evaluation.setContent(request.getParameter("content"));
        evaluation.setScore(Integer.valueOf(request.getParameter("score")));
        evaluation.setGoods(goods);
        evaluation.setUser(user);
        evaluationService.addEvaluation(evaluation);
    }

    /*用户搜索订单*/
    @RequestMapping(value = "/search_orderById/{orderId}", method = RequestMethod.GET)
    public OrderView searchOrderById(@PathVariable("orderId") String orderId, HttpServletRequest request) throws ServiceException {
        User user = userService.getUserById(Long.valueOf(request.getParameter("userId")));
        OrderView orderView = OrderView.getOrderViewOne(orderService.searchOrderById(Long.valueOf(orderId)));
        if (user.getId() == orderView.getUserId()) {
            return orderView;
        } else {
            return null;
        }
    }

    /*获取用户评论*/
    @RequestMapping(value = "/get_Evaluation", method = RequestMethod.POST)
    public List<EvaluationView> getEvaluation(HttpServletRequest request) {
        User user = userService.getUserById(Long.valueOf(request.getParameter("userId")));
        return EvaluationView.getEvaluationView(evaluationService.listUserEvaluation(user, Integer.valueOf(request.getParameter("page")), Integer.valueOf(request.getParameter("pageSize"))));
    }

    /*获取用户收藏*/
    @RequestMapping(value = "/get_Favorites", method = RequestMethod.POST)
    public List<FavoritesView> getFavorites(HttpServletRequest request) {
        User user = userService.getUserById(Long.valueOf(request.getParameter("userId")));
        return FavoritesView.getFavoritesView(favoriteService.getAllFavorite(user, Integer.valueOf(request.getParameter("page")), Integer.valueOf(request.getParameter("pageSize"))));
    }

    /*删除用户收藏*/
    @RequestMapping(value = "/delete_favorites", method = RequestMethod.POST)
    public void deleteFavorites(HttpServletRequest request) throws FavoritesException {
        Favorites favorites = favoriteService.getFavoritesById(Long.valueOf(request.getParameter("favoritesId")));
        User user = userService.getUserById(Long.valueOf(request.getParameter("userId")));
        favoriteService.deleteFavorite(favorites, user);
    }

    /*获取团购编号*/
    @RequestMapping(value = "/get_groupPurchaseId", method = RequestMethod.POST)
    public Long getGroupPurchaseId(HttpServletRequest request) throws GroupPurchaseException {
        GroupPurchase groupPurchase = groupPurchaseService.getById(Long.valueOf(request.getParameter("groupPurchaseId")));
        return groupPurchase.getId();
    }

    /*获取冻结或未冻结的用户列表*/
    @RequestMapping(value = "/getBusiUserList", method = RequestMethod.GET)
    public PageDataView<User> getBusiUserList(@RequestParam("status") Boolean status, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
                                              @RequestParam("userType") String userType) {
        List<User> userList = userService.listUserByStatus(status, userType);
        PageDataView<User> userPageDataView = new PageDataView<>();
        userPageDataView.setPageIndex(page);
        userPageDataView.setPageSize(pageSize);
        userPageDataView.setTotalRecord((long) userList.size());
        userPageDataView.setDataList(PageUtil.getPage(userList, page, pageSize));
        return userPageDataView;
    }

    /*修改用户账户的状态*/
    @RequestMapping(value = "/userLock", method = RequestMethod.GET)
    public void changeUserLockStatus(@RequestParam("userId") Long userId, @RequestParam("isLock") Boolean isLock) throws UserServiceException {
        User user = userService.getUserById(userId);
        user.setIslocked(isLock);
        userService.updateUserInfo(user);
    }

    @RequestMapping(value = "/getAnnouncementList", method = RequestMethod.GET)
    public PageDataView<Announcement> getAnnouncementList(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        List<Announcement> announcementList = announceService.listAll();
        PageDataView<Announcement> announcementPageDataView = new PageDataView<>();
        announcementPageDataView.setTotalRecord((long) announcementList.size());
        announcementPageDataView.setPageIndex(page);
        announcementPageDataView.setPageSize(pageSize);
        announcementPageDataView.setDataList(PageUtil.getPage(announcementList, page, pageSize));
        return announcementPageDataView;
    }

    @RequestMapping(value = "/addAnnouncement", method = RequestMethod.POST)
    public void addAnnouncement(@RequestBody Announcement announcement, HttpServletRequest request) throws UserSessionException {
        User user = sessionService.getUser(request);
        announcement.setPublisher(user.getId());
        announcement.setPublisherNick(user.getNickName());
        announceService.addAnnounceMent(announcement);
    }

    @RequestMapping(value = "/getAnnounce", method = RequestMethod.GET)
    public Announcement getAnnounce() {
        List<Announcement> announcementList = announceService.listAll();
        return announcementList.get(announcementList.size() - 1);
    }
}
