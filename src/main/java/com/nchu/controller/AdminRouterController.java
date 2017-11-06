package com.nchu.controller;

import com.nchu.entity.Shop;
import com.nchu.entity.User;
import com.nchu.entity.dataView.PageDataView;
import com.nchu.entity.dataView.ShopDateView;
import com.nchu.service.ShopService;
import com.nchu.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 管理员相关页面控制器
 */
@CrossOrigin
@Controller
@RequestMapping("/admin")
public class AdminRouterController {
    @Autowired
    ShopService shopService;

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcomePage() {
        return "admin/welcome";
    }

    @RequestMapping(value = "/businessAudit", method = RequestMethod.GET)
    public String businessAudit() {
        return "admin/businessAudit";
    }

    @RequestMapping(value = "/businessManagement", method = RequestMethod.GET)
    public String businessManagement() {
        return "admin/businessManagement";
    }

    @RequestMapping(value = "/shopManage", method = RequestMethod.GET)
    public String shopManage() {
        return "admin/shopManage";
    }

    @RequestMapping(value = "/userManagement", method = RequestMethod.GET)
    public String userManagement() {
        return "admin/userManagement";
    }

    @RequestMapping(value = "/webNotice", method = RequestMethod.GET)
    public String webNotice() {
        return "admin/webNotice";
    }

}
