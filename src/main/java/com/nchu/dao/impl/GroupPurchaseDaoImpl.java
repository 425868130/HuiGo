package com.nchu.dao.impl;

import com.nchu.dao.GroupPurchaseDao;
import com.nchu.entity.AfterSale;
import com.nchu.entity.Goods;
import com.nchu.entity.GoodsPicture;
import com.nchu.entity.GroupPurchase;

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
 * 2017年9月20日10:11:11
 * 参团表Dao实现类
 */
@Repository
@Transactional
public class GroupPurchaseDaoImpl implements GroupPurchaseDao {
    @Autowired
    SessionFactory sessionFactory;

    Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 保存一个对象到数据库
     *
     * @param model 要保存的对象
     * @return 主键
     */
    @Override
    public Long save(GroupPurchase model) {
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
    public void saveOrUpdate(GroupPurchase model) {
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        getSession().saveOrUpdate(model);
    }

    /**
     * 更新一条记录
     *
     * @param model 要更新的实例对象
     */
    @Override
    public void update(GroupPurchase model) {
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        getSession().update(model);
    }

    /**
     * 混合方法
     *
     * @param model
     */
    @Override
    public void merge(GroupPurchase model) {
        getSession().merge(model);
    }

    /**
     * 根据主键id删除记录
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        GroupPurchase model = (GroupPurchase) getSession().get(GroupPurchase.class, id);
        getSession().delete(model);
    }

    /**
     * 删除所有指定的持久化对象
     *
     * @param groupPurchases 要删除的列表
     */
    @Override
    public void deleteAll(List<GroupPurchase> groupPurchases) {
        for (int i = 0; i < groupPurchases.size(); i++) {
            getSession().delete(groupPurchases.get(i));
        }
    }

    /**
     * 通过实体对象删除记录
     *
     * @param model 实体对象
     */
    @Override
    public void deleteObject(GroupPurchase model) {
        getSession().delete(model);
    }

    /**
     * 通过主键Id获取对象
     *
     * @param id 主键id
     * @return 实体对象
     */
    @Override
    public GroupPurchase get(Long id) {
        return (GroupPurchase) getSession().get(GroupPurchase.class, id);
    }

    /**
     * 统计整张表的记录数
     *
     * @return 返回记录条数
     */
    @Override
    public Long countAll() {
        String sql = "select count(*) from GroupPurchase where end_time >:now";
        Query query = getSession().createQuery(sql);
        query.setTimestamp("now", DateUtil.getCurrentTime());
        Long count = (Long) query.uniqueResult();
        return count;
    }

    /**
     * 查询全部数据记录
     *
     * @return 全部记录列表
     */
    @Override
    public List<GroupPurchase> listAll() {
        String hql = "from GroupPurchase ";
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
    public List<GroupPurchase> searchPage(Map<String, Object> conditions, int page, int pageSize) {
        Session session = getSession();
        String hql;
        if (conditions == null) {
            hql = "from GroupPurchase ";
        } else {
            Iterator<String> iterator = conditions.keySet().iterator();
            StringBuilder stringBuilder = new StringBuilder("from group_purchase gp where ");
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
        if (pageSize != -1) {
            int startIndex = (page - 1) * pageSize;
            query.setFirstResult(startIndex);
            query.setMaxResults(pageSize);
        }

        List<GroupPurchase> list = query.list();
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
    public List<GroupPurchase> searchPageByOrder(Map<String, Object> conditions, String orderBy, String order, int page, int pageSize) {
        Session session = getSession();
        String hql;
        if (conditions == null) {
            hql = "from GroupPurchase order by " + orderBy + " " + order;
            System.out.println("test.....");
        } else {
            Iterator<String> iterator = conditions.keySet().iterator();
            StringBuilder stringBuilder = new StringBuilder("from GroupPurchase af where ");
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
        if (pageSize != -1) {
            int startIndex = (page - 1) * pageSize;
            query.setFirstResult(startIndex);
            query.setMaxResults(pageSize);
        }
        List<GroupPurchase> list = query.list();
        return list;
    }

    /**
     * 直接通过HQL语句进行查询
     *
     * @param HQL 语句字符串
     * @return 查询结果列表
     */
    @Override
    public List<GroupPurchase> searchListDefined(String HQL) {
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
        GroupPurchase model = (GroupPurchase) getSession().get(GroupPurchase.class, id);
        if (model != null)
            return true;
        else
            return false;
    }

    /**
     * 查询商品的团购活动记录
     *
     * @param model 商品对象
     * @return 返回活动记录结果
     */
    @Override
    public GroupPurchase getByGoods(Goods model) {
        String hql = "from GroupPurchase gp where gp.goods.id=?";
        Query query = getSession().createQuery(hql);
        query.setBigInteger(0, BigInteger.valueOf(model.getId()));
        return (GroupPurchase) query.uniqueResult();
    }

    /**
     * 根据提供的参数，查询人数排名前N的团购活动记录
     *
     * @param top 排名数
     * @return 返回活动记录结果
     */
    @Override
    public List<GroupPurchase> listAllTop(int top) {
        String hql = "from GroupPurchase where end_time > :now order by numberPart DESC";
        Query query = getSession().createQuery(hql);
        query.setTimestamp("now", DateUtil.getCurrentTime());
        query.setFirstResult(0);
        query.setMaxResults(top);
        return query.list();
    }

    /* 根据用户Id 查询团购信息 */
    @Override
    public List<GroupPurchase> getGroupByUserId(long userId) {
        String hql = "from GroupPurchase gp where gp.userid=:userId";
        Query query = getSession().createQuery(hql);
        return query.list();
    }

}
