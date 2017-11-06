package com.nchu.service;

import com.nchu.entity.Saletype;
import com.nchu.entity.Shop;
import com.nchu.entity.User;
import com.nchu.exception.ShopException;
import com.nchu.exception.UserServiceException;

import java.util.List;

/**
 * 2017-9-23 16:12:25
 * 店铺相关业务逻辑接口
 */
public interface ShopService {
    /**
     * TODO 新建店铺
     * 需要判断店铺对象中的所属人身份是否为商家
     *
     * @param shop 要创建的店铺
     * @return 操作结果
     */
    boolean createShop(Shop shop) throws UserServiceException, ShopException;

    /**
     * TODO 更新店铺信息
     * 判断店铺状态,若已封禁则直接返回,判断user是否为店铺所有人,校验通过修改店铺信息
     *
     * @param newShopInfo 新店铺信息
     * @param user        操作人
     * @return 操作结果
     */
    boolean updateShopInfo(Shop newShopInfo, User user) throws ShopException;

    /**
     * TODO 通过所有者id获取店铺信息
     *
     * @param ownerId 店铺所有者id
     * @return 店铺实体
     */
    Shop getByOwner(Long ownerId) throws ShopException;

    /**
     * TODO 通过id获取店铺信息
     *
     * @param Id 店铺id
     * @return 店铺实体
     */
    Shop getById(Long Id) throws ShopException;

    /**
     * TODO 分页查询店铺
     *
     * @param keyword  查询关键词,如果关键词为空执行全部查询
     * @param page     页码
     * @param pageSize 每页大小
     * @return 查询结果列表
     */
    List<Shop> searchShop(String keyword, int page, int pageSize) throws ShopException;

    /**
     * TODO 分页列出所有被禁用店铺
     * 本方法主要供管理员后台管理页面使用
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @return 店铺列表
     */
    List<Shop> listLockShop(int page, int pageSize) throws ShopException;

    /**
     * TODO 店铺封禁
     * 判断店铺状态,判断管理员身份,执行店铺封禁操作
     *
     * @param shop     要封禁的店铺
     * @param operator 操作员
     * @return 返回操作执行结果
     */
    boolean shopLock(Shop shop, User operator) throws UserServiceException, ShopException;

    /**
     * TODO 店铺解封
     * 判断店铺状态,判断管理员身份,执行店铺解封操作
     *
     * @param shop     要解封的店铺
     * @param operator 操作员
     * @return 返回操作执行结果
     */
    boolean shopUnLock(Shop shop, User operator) throws UserServiceException, ShopException;

    /**
     * TODO 获取全部销售类型列表
     *
     * @return 销售类型列表
     */
    List<Saletype> getAllSaleType();

    Saletype getSaleTypeById(Long id);

    List<Shop> listAll();

    List<Shop> listAllUncheck();

    void update(Shop shop);
}
