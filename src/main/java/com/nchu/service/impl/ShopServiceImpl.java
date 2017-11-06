package com.nchu.service.impl;

import com.nchu.dao.SaleTypeDao;
import com.nchu.dao.ShopDao;
import com.nchu.entity.Saletype;
import com.nchu.entity.Shop;
import com.nchu.entity.User;
import com.nchu.enumdef.UserRoleType;
import com.nchu.exception.ShopException;
import com.nchu.exception.UserServiceException;
import com.nchu.service.ShopService;
import com.nchu.util.DateUtil;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2017-9-24 15:46:29
 * 店铺相关业务逻辑接口实现类
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    ShopDao shopDao;
    @Autowired
    SaleTypeDao saleTypeDao;

    /**
     * TODO 新建店铺
     * 需要判断店铺对象中的所属人身份是否为商家
     *
     * @param shop 要创建的店铺
     * @return 操作结果
     */
    @Transactional
    @Override
    public boolean createShop(Shop shop) throws UserServiceException, ShopException {
        User user = shop.getUser();
        if (!UserRoleType.MERCHANT.toString().equals(user.getRole())) {
            throw new UserServiceException("权限不足,非商家账号无法创建店铺");
        } else if (shopDao.getByUser(shop.getUser()) != null) {
            throw new ShopException("店铺已存在,一个商家只能有一个店铺");
        } else {
            shop.setGmtCreate(DateUtil.getCurrentTimestamp());
            shop.setGmtModified(DateUtil.getCurrentTimestamp());
            try {
                shopDao.save(shop);
            } catch (Exception e) {
                throw new ShopException("店铺创建失败,请重试");
            }
        }
        return true;
    }

    /**
     * TODO 更新店铺信息
     * 判断店铺状态,若已封禁则直接返回,判断user是否为店铺所有人,校验通过修改店铺信息
     *
     * @param newShopInfo 新店铺信息
     * @param user        操作人
     * @return 操作结果
     */
    @Transactional
    @Override
    public boolean updateShopInfo(Shop newShopInfo, User user) throws ShopException {
        Shop shop = shopDao.getByUser(user);
        if (shop == null) {
            throw new ShopException("店铺不存在");
        }
        /*验证店铺是否被禁用*/
        if (shop.isIslocked()) {
            throw new ShopException("店铺已禁用无法更新消息");
        } else if (shop.getId() != newShopInfo.getId()) {
            throw new ShopException("店铺信息异常,操作失败");
        }
        try {
            shopDao.update(shop);
        } catch (Exception e) {
            throw new ShopException("数据更新失败,请重试");
        }
        return true;
    }

    /**
     * TODO 通过所有者id获取店铺信息
     *
     * @param ownerId 店铺所有者id
     * @return 店铺实体
     */
    @Override
    public Shop getByOwner(Long ownerId) throws ShopException {
        User temp = new User();
        temp.setId(ownerId);
        Shop shop = shopDao.getByUser(temp);
        if (shop == null) {
            throw new ShopException("店铺不存在");
        }
        return shop;
    }

    /**
     * TODO 通过id获取店铺信息
     *
     * @param Id 店铺id
     * @return 店铺实体
     */
    @Override
    public Shop getById(Long Id) throws ShopException {
        Shop shop;
        try {
            shop = shopDao.get(Id);
        } catch (Exception e) {
            throw new ShopException("没有找到相关店铺");
        }
        return shop;
    }

    /**
     * TODO 分页查询店铺
     *
     * @param keyword  查询关键词,如果关键词为空执行全部查询
     * @param page     页码
     * @param pageSize 每页大小
     * @return 查询结果列表
     */
    @Override
    public List<Shop> searchShop(String keyword, int page, int pageSize) throws ShopException {
        List<Shop> shopList;
        try {
            shopList = shopDao.searchShop(keyword, page, pageSize);
        } catch (Exception e) {
            throw new ShopException("数据查询失败");
        }
        if (shopList == null) {
            throw new ShopException("没有相关店铺");
        }
        return shopList;
    }

    /**
     * TODO 分页列出所有被禁用店铺
     * 本方法主要供管理员后台管理页面使用
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @return 店铺列表
     */
    @Override
    public List<Shop> listLockShop(int page, int pageSize) throws ShopException {
        List<Shop> shopList;
        try {
            shopList = shopDao.searchPage(null, page, pageSize);
        } catch (Exception e) {
            throw new ShopException("数据查询失败,请重试");
        }
        if (shopList == null) {
            throw new ShopException("没有相关店铺");
        }
        return shopList;
    }

    /**
     * TODO 店铺封禁
     * 判断店铺状态,判断管理员身份,执行店铺封禁操作
     *
     * @param shop     要封禁的店铺
     * @param operator 操作员
     * @return 返回操作执行结果
     */
    @Transactional
    @Override
    public boolean shopLock(Shop shop, User operator) throws UserServiceException, ShopException {
        if (!operator.getRole().equals(UserRoleType.ADMIN)) {
            throw new UserServiceException("权限不足,禁止操作");
        } else if (shop.isIslocked()) {
            throw new ShopException("操作失败,店铺已经是封禁状态");
        } else {
            shop.setIslocked(true);
            shopDao.update(shop);
        }
        return true;
    }

    /**
     * TODO 店铺解封
     * 判断店铺状态,判断管理员身份,执行店铺解封操作
     *
     * @param shop     要解封的店铺
     * @param operator 操作员
     * @return 返回操作执行结果
     */
    @Transactional
    @Override
    public boolean shopUnLock(Shop shop, User operator) throws UserServiceException, ShopException {
        if (!operator.getRole().equals(UserRoleType.ADMIN)) {
            throw new UserServiceException("权限不足,禁止操作");
        } else if (!shop.isIslocked()) {
            throw new ShopException("操作失败,店铺已经是正常状态");
        } else {
            shop.setIslocked(false);
            shopDao.update(shop);
        }
        return true;
    }

    /**
     * TODO 获取全部销售类型列表
     *
     * @return 销售类型列表
     */
    @Override
    public List<Saletype> getAllSaleType() {
        return saleTypeDao.listAll();
    }

    @Override
    public Saletype getSaleTypeById(Long id) {
        return saleTypeDao.get(id);
    }

    /**
     * 查询全部数据记录
     *
     * @return 全部记录列表
     */
    @Override
    public List<Shop> listAll() {
        return shopDao.listAll();
    }

    /**
     * 获取所有未审核的店铺
     */
    @Override
    public List<Shop> listAllUncheck() {
        String hql = "from Shop where isVerify = 0";
        return shopDao.searchListDefined(hql);
    }

    @Override
    public void update(Shop shop) {
        shopDao.update(shop);
    }
}
