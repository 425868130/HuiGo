package com.nchu.service.impl;

import com.nchu.dao.GoodsDao;
import com.nchu.dao.GroupPurchaseDao;
import com.nchu.dao.ShopDao;
import com.nchu.dao.UserDao;
import com.nchu.entity.Goods;
import com.nchu.entity.GroupPurchase;
import com.nchu.entity.Shop;
import com.nchu.entity.User;
import com.nchu.enumdef.UserRoleType;
import com.nchu.exception.GoodsException;
import com.nchu.service.GoodsService;

import java.util.Map;

import java.util.HashMap;
import java.util.List;

import com.nchu.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2017-9-23 17:22:07
 * 商品相关业务接口实现类
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ShopDao shopDao;
    @Autowired
    UserDao userDao;
    @Autowired
    GroupPurchaseDao groupPurchaseDao;

    /**
     * TODO 添加商品
     * 1.判断操作人身份是否为商家
     * 2.获取并判断商家店铺状态是否封禁
     * 3.补充商品对象属性,并插入数据库,商品默认为下架状态
     *
     * @param goods    要添加的商品
     * @param operator 操作人
     * @return 操作结果
     */
    @Override
    public boolean addGoods(Goods goods, User operator) throws GoodsException {
        /*if (Integer.valueOf(operator.getRole()) == UserRoleType.MERCHANT.getIndex()) {*/
            if (!shopDao.getByUser(operator).isIslocked()) {
                try {
                    goods.setIsonshelves(false);
                    goodsDao.save(goods);
                    return true;
                } catch (Exception e) {
                    throw new GoodsException("添加商品异常");
                }
            } else
                throw new GoodsException("添加商品操作者已被锁定");
        /*} else
            throw new GoodsException("添加商品非法操作者");*/
    }

    /**
     * TODO 通过id获取商品
     *
     * @param id 商品id
     * @return 商品实体
     */
    @Override
    public Goods getById(Long id) {
        return goodsDao.get(id);
    }

    /**
     * TODO 修改商品信息
     * 需要判断操作人身份再执行,禁止通过该方法修改商品上下架状态图
     *
     * @param goods    包含新商品信息的商品对象
     * @param operator 操作人
     * @return 操作结果
     */
    @Override
    public boolean updateGoodsInfo(Goods goods, User operator) throws GoodsException {
        if (Integer.valueOf(operator.getRole()) == UserRoleType.MERCHANT.getIndex()) {
            if (shopDao.getByUser(operator).getId() == goods.getShop().getId()) {
                Long id = goods.getId();
                Goods model = goodsDao.get(id);
                if (goods.isIsonshelves() == model.isIsonshelves()) {
                    try {
                        goods.setGmtModified(DateUtil.getCurrentTimestamp());
                        goodsDao.update(goods);
                        return true;
                    } catch (Exception e) {
                        throw new GoodsException("修改商品信息异常");
                    }
                }
            }
        }
        throw new GoodsException("修改商品信息非法操作者");
    }

    /**
     * TODO 更新商品库存
     *
     * @param goods 新商品信息
     * @return 操作结果
     */
    @Override
    public boolean updateInventory(Goods goods) {
        goodsDao.update(goods);
        return true;
    }

    /**
     * TODO 关键字商品查询
     * 按关键词进行商品模糊查询,若关键词为空则查询全部,商品状态条件为已上架
     *
     * @param keywords 商品关键字
     * @param page     页码
     * @param pageSize 每页大小
     * @return 商品列表
     */
    @Override
    public List<Goods> searchGoods(String keywords, int page, int pageSize) {
        return goodsDao.searchByNamePage(keywords, page, pageSize);
    }

    /**
     * TODO 删除商品
     * 要删除的商品必须是已下架的商品,否则删除失败
     *
     * @param goods    要删除的商品
     * @param operator 操作者
     * @return 操作结果
     */
    @Override
    public boolean deleteGoods(Goods goods, User operator) throws GoodsException {
       /* if (Integer.valueOf(operator.getRole()) == UserRoleType.MERCHANT.getIndex()) {*/
            if (shopDao.getByUser(operator).getId() == goods.getShop().getId()) {
                if (!goods.isIsonshelves()) {
                    try {
                        goodsDao.deleteObject(goods);
                        return true;
                    } catch (Exception e) {
                        throw new GoodsException("删除商品异常");
                    }
                }
            }
       /* }
        throw new GoodsException("删除商品非法操作者");*/
        return true;
    }

    /**
     * TODO 获取店铺中的所有上架/下架商品
     *
     * @param shop        要查询的商品
     * @param isonshelves 是否上架
     * @param page        页码
     * @param pageSize    每页大小
     * @return 返回商品列表
     */
    @Override
    public List<Goods> listGoods(Shop shop, boolean isonshelves, int page, int pageSize) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("shopid", shop.getId());
        conditions.put("isonshelves", isonshelves);
        return goodsDao.searchPage(conditions, page, pageSize);
    }

    /**
     * TODO 下架商品
     * 下架指定店铺的商品,需要鉴定用户为店铺所有者,商品为该店铺商品
     * 商品下架的同时将取消商品的团购活动
     *
     * @param shop
     * @param goods
     * @param operator
     * @return
     */
    @Override
    public boolean shelveGoods(Shop shop, Goods goods, User operator) throws GoodsException {
        if (Integer.valueOf(operator.getRole()) == UserRoleType.MERCHANT.getIndex()) {
            if (operator.getId() == shop.getUser().getId() && goods.getShop().getId() == shop.getId()) {
                try {
                    goods.setIsonshelves(false);
                    goodsDao.update(goods);
                    GroupPurchase gp = groupPurchaseDao.getByGoods(goods);
                    gp.setIseffective(false);
                    groupPurchaseDao.update(gp);
                    return true;
                } catch (Exception e) {
                    throw new GoodsException("下架商品异常");
                }
            }
        }
        throw new GoodsException("下架商品非法操作者");
    }

    /**
     * TODO 下架店铺中的全部商品
     * 此功能仅提供给管理员封禁店铺时同步将店铺商品自动下架,所以要检查操作员身份为管理员
     *
     * @param shop     要下架商品的店铺
     * @param operator 操作人
     * @return 操作结果
     */
    @Override
    public boolean shelveAll(Shop shop, User operator) throws GoodsException {
        if (Integer.valueOf(operator.getRole()) == UserRoleType.ADMIN.getIndex()) {
            try {
                goodsDao.removeGoodsByShop(shop.getId());
                return true;
            } catch (Exception e) {
                throw new GoodsException("店铺下架商品异常");
            }
        } else
            throw new GoodsException("店铺下架商品非法操作者");
    }
}
