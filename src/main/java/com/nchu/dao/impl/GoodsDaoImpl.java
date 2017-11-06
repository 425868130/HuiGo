package com.nchu.dao.impl;

import com.nchu.dao.GoodsDao;
import com.nchu.entity.AfterSale;
import com.nchu.entity.Goods;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nchu.util.DateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 2017年9月20日09:14:39
 * 商品Dao的实现类
 */
@Repository
@Transactional
public class GoodsDaoImpl implements GoodsDao {
    @Autowired
    SessionFactory sessionFactory;

    Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 下架指定店铺中的全部商品,即更改商品状态为已下架
     *
     * @param shopId 要下架商品的店铺id
     */
    @Override
    public void removeGoodsByShop(Long shopId) {
        String hql = "update Goods g set g.isonshelves=? where g.shop.id=?";
        Query query = getSession().createQuery(hql);
        query.setByte(0, (byte) 0);
        query.setBigInteger(1, BigInteger.valueOf(shopId));
        query.executeUpdate();
    }

    /**
     * 根据商品关键字模糊查询商品
     *
     * @param keyword 商品关键字
     */
    @Override
    public List<Goods> searchByName(String keyword) {
        String hql = "from Goods where name like '%%" + keyword + "%%'";
        Query query = getSession().createQuery(hql);
        return query.list();
    }

    /**
     * 根据商品关键字模糊查询商品
     *
     * @param keyword  商品关键字
     * @param page
     * @param pageSize
     */
    @Override
    public List<Goods> searchByNamePage(String keyword, int page, int pageSize) {
        String hql = "from Goods where name like '%%" + keyword + "%%'";
        Query query = getSession().createQuery(hql);
        if (pageSize != -1) {
            query.setFirstResult((1 - page) * pageSize);
            query.setMaxResults(pageSize);
        }
        return query.list();
    }

    /**
     * 保存一个对象到数据库
     *
     * @param model 要保存的对象
     * @return 主键
     */
    @Override
    public Long save(Goods model) {
        model.setGmtCreate(DateUtil.getCurrentTimestamp());
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        getSession().save(model);
        return model.getId();
    }

    /**
     * 保存或者更新,当数据库中 存在同id对象时执行更新操作,没有则执行插入操作
     *
     * @param model 实体类
     */
    @Override
    public void saveOrUpdate(Goods model) {
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        getSession().saveOrUpdate(model);
    }

    /**
     * 更新一条记录
     *
     * @param model 要更新的实例对象
     */
    @Override
    public void update(Goods model) {
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        model.setGmtCreate(DateUtil.getCurrentTimestamp());
        getSession().update(model);
    }

    /**
     * 混合方法
     *
     * @param model
     */
    @Override
    public void merge(Goods model) {
        getSession().merge(model);
    }

    /**
     * 根据主键id删除记录
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Goods model = (Goods) getSession().get(Goods.class, id);
        getSession().delete(model);
    }

    /**
     * 删除所有指定的持久化对象
     *
     * @param goods 要删除的列表
     */
    @Override
    public void deleteAll(List<Goods> goods) {
        for (int i = 0; i < goods.size(); i++) {
            getSession().delete(goods.get(i));
        }
    }

    /**
     * 通过实体对象删除记录
     *
     * @param model 实体对象
     */
    @Override
    public void deleteObject(Goods model) {
        getSession().delete(model);
    }

    /**
     * 通过主键Id获取对象
     *
     * @param id 主键id
     * @return 实体对象
     */
    @Override
    public Goods get(Long id) {
        Goods model = (Goods) getSession().get(Goods.class, id);
        return model;
    }

    /**
     * 统计整张表的记录数
     *
     * @return 返回记录条数
     */
    @Override
    public Long countAll() {
        String sql = "select count(*) from Goods";
        Long count = (Long) getSession().createQuery(sql).uniqueResult();
        return count;
    }

    /**
     * 查询全部数据记录
     *
     * @return 全部记录列表
     */
    @Override
    public List<Goods> listAll() {
        String hql = "from Goods";
        Query query = getSession().createQuery(hql);
        return query.list();
    }

    /**
     * 分页查询
     *
     * @param conditions 条件参数集合
     * @param page       页码
     * @param pageSize   每页大小 如果传入页面大小为-1表示查询全部
     * @return 查询结果集
     */
    @Override
    public List<Goods> searchPage(Map<String, Object> conditions, int page, int pageSize) {
        Session session = getSession();
        String hql;
        if (conditions == null) {
            hql = "from Goods";
        } else {
            Iterator<String> iterator = conditions.keySet().iterator();
            StringBuilder stringBuilder = new StringBuilder("from Goods g where ");
            while (iterator.hasNext()) {
                String key = iterator.next();
                stringBuilder.append(key + " =  " + conditions.get(key));
                if (iterator.hasNext()) {
                    stringBuilder.append(" AND ");
                }
            }
            hql = stringBuilder.toString();
        }

        Query query = session.createQuery(hql);
        /*如果传入页面大小为-1表示查询全部*/
        if (pageSize != -1) {
            int startIndex = (page - 1) * pageSize;
            query.setFirstResult(startIndex);
            query.setMaxResults(pageSize);
        }
        List<Goods> list = query.list();
        return list;
    }

    /**
     * 带排序的分页查询
     *
     * @param conditions 条件参数集合
     * @param orderBy    排序参考列
     * @param order      排序方式
     * @param page       页码
     * @param pageSize   每页大小
     * @return 查询结果集
     */
    @Override
    public List<Goods> searchPageByOrder(Map<String, Object> conditions, String orderBy, String order, int page, int pageSize) {
        Session session = getSession();
        String hql;
        if (conditions == null) {
            hql = "from Goods order by " + orderBy + " " + order;
            System.out.println("test.....");
        } else {
            Iterator<String> iterator = conditions.keySet().iterator();
            StringBuilder stringBuilder = new StringBuilder("from Goods af where ");
            while (iterator.hasNext()) {
                String key = iterator.next();
                stringBuilder.append(key + " =  " + conditions.get(key));
                if (iterator.hasNext()) {
                    stringBuilder.append(" AND ");
                }
            }
            hql = stringBuilder.toString();
        }

        Query query = session.createQuery(hql);
        int startIndex = (page - 1) * pageSize;
        query.setFirstResult(startIndex);
        query.setMaxResults(pageSize);
        List<Goods> list = query.list();
        return list;
    }

    /**
     * 直接通过HQL语句进行查询
     *
     * @param HQL 语句字符串
     * @return 查询结果列表
     */
    @Override
    public List<Goods> searchListDefined(String HQL) {
        Query query = getSession().createQuery(HQL);
        return query.list();
    }

    /**
     * 通过Id判断对象是否存在
     *
     * @param id 要判断的Id
     * @return 返回判断结果
     */
    @Override
    public boolean exists(Long id) {
        Goods model = (Goods) getSession().get(Goods.class, id);
        if (model != null)
            return true;
        else
            return false;
    }
}
