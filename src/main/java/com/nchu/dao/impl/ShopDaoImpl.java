package com.nchu.dao.impl;

import com.nchu.dao.ShopDao;
import com.nchu.entity.Shop;
import com.nchu.entity.User;
import com.nchu.util.DateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 2017年9月20日09:19:23
 * 店铺Doa实现类
 */
@Repository
@Transactional
public class ShopDaoImpl implements ShopDao {

    @Autowired
    SessionFactory sessionfactory;

    private Session getSession() {
        return sessionfactory.getCurrentSession();
    }

    /**
     * 保存一个对象到数据库
     *
     * @param model 要保存的对象
     * @return 主键
     */
    @Override
    public Long save(Shop model) {
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        model.setGmtCreate(DateUtil.getCurrentTimestamp());
        getSession().save(model);
        return model.getId();
    }

    /**
     * 保存或者更新,当数据库中 存在同id对象时执行更新操作,没有则执行插入操作
     *
     * @param model 实体类
     */
    @Override
    public void saveOrUpdate(Shop model) {
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        getSession().saveOrUpdate(model);
    }

    /**
     * 更新一条记录
     *
     * @param model 要更新的实例对象
     */
    @Override
    public void update(Shop model) {
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        getSession().update(model);
    }

    /**
     * 混合方法
     *
     * @param model
     */
    @Override
    public void merge(Shop model) {
        getSession().merge(model);
    }

    /**
     * 根据主键id删除记录
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Shop model = (Shop) getSession().get(Shop.class, id);
        getSession().delete(model);
    }

    /**
     * 删除所有指定的持久化对象
     *
     * @param shops 要删除的列表
     */
    @Override
    public void deleteAll(List<Shop> shops) {
        for (int i = 0; i < shops.size(); i++) {
            getSession().delete(shops.get(i));
        }
    }

    /**
     * 通过实体对象删除记录
     *
     * @param model 实体对象
     */
    @Override
    public void deleteObject(Shop model) {
        getSession().delete(model);
    }

    /**
     * 通过主键Id获取对象
     *
     * @param id 主键id
     * @return 实体对象
     */
    @Override
    public Shop get(Long id) {
        Shop model = (Shop) getSession().get(Shop.class, id);
        return model;
    }

    /**
     * 统计整张表的记录数
     *
     * @return 返回记录条数
     */
    @Override
    public Long countAll() {
        String sql = "select count(*) from Shop ";
        Long count = (Long) getSession().createQuery(sql).uniqueResult();
        return count;
    }

    /**
     * 查询全部数据记录
     *
     * @return 全部记录列表
     */
    @Override
    public List<Shop> listAll() {
        String hql = "from Shop where isVerify=1";
        Query query = getSession().createQuery(hql);
        return query.list();
    }

    /**
     * 分页查询
     *
     * @param conditions 条件参数集合
     * @param page       页码
     * @param pageSize   每页大小
     * @return 查询结果集
     */
    @Override
    public List<Shop> searchPage(Map<String, Object> conditions, int page, int pageSize) {
        Session session = getSession();
        String hql;
        if (conditions == null) {
            hql = "from Shop ";
        } else {
            Iterator<String> iterator = conditions.keySet().iterator();
            StringBuilder stringBuilder = new StringBuilder("from shop s where ");
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
        /*-1则不分页*/
        if (pageSize == -1) {
            int startIndex = (page - 1) * pageSize;
            query.setFirstResult(startIndex);
            query.setMaxResults(pageSize);
        }
        List<Shop> list = query.list();
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
    public List<Shop> searchPageByOrder(Map<String, Object> conditions, String orderBy, String order, int page, int pageSize) {
        Session session = getSession();
        String hql;
        if (conditions == null) {
            hql = "from Shop order by " + orderBy + " " + order;
            System.out.println("test.....");
        } else {
            Iterator<String> iterator = conditions.keySet().iterator();
            StringBuilder stringBuilder = new StringBuilder("from Shop af where ");
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
        List<Shop> list = query.list();
        return list;
    }

    /**
     * 直接通过HQL语句进行查询
     *
     * @param HQL 语句字符串
     * @return 查询结果列表
     */
    @Override
    public List<Shop> searchListDefined(String HQL) {
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
        Shop model = (Shop) getSession().get(Shop.class, id);
        if (model != null)
            return true;
        else
            return false;
    }

    /**
     * 通过User查询对象
     *
     * @param model User实体
     * @return 返回查询结果对象
     */
    @Override
    public Shop getByUser(User model) {
        String hql = "from Shop where userId =:uid";
        Query query = getSession().createQuery(hql);
        query.setParameter("uid", model.getId());
        return (Shop) query.uniqueResult();
    }

    /**
     * 分页查询店铺
     *
     * @param keyword  查询关键词,如果关键词为空执行全部查询
     * @param page     页码
     * @param pageSize 每页大小
     * @return 查询结果列表
     */
    @Override
    public List<Shop> searchShop(String keyword, int page, int pageSize) {
        String hql;
        if (keyword.trim().length() != 0) {
            hql = "from Shop ";
        } else {
            hql = "from Shop where Shop.name like '%%" + keyword + "%%'";
        }
        Query query = getSession().createQuery(hql);
        int startIndex = (page - 1) * pageSize;
        query.setFirstResult(startIndex);
        query.setMaxResults(pageSize);
        return query.list();
    }
}
