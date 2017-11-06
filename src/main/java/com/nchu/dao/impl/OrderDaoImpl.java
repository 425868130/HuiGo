package com.nchu.dao.impl;

import com.nchu.dao.OrderDao;
import com.nchu.entity.Order;
import com.nchu.entity.Shop;
import com.nchu.enumdef.OrderStatus;
import com.nchu.util.DateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class OrderDaoImpl implements OrderDao {

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
    public Long save(Order model) {
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
    public void saveOrUpdate(Order model) {
        getSession().saveOrUpdate(model);
    }

    /**
     * 更新一条记录
     *
     * @param model 要更新的实例对象
     */
    @Override
    public void update(Order model) {
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        getSession().update(model);
    }

    /**
     * 混合方法
     *
     * @param model
     */
    @Override
    public void merge(Order model) {
        getSession().merge(model);
    }

    /**
     * 根据主键id删除记录
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Order model = (Order) getSession().get(Order.class, id);
        getSession().delete(model);
    }

    /**
     * 删除所有指定的持久化对象
     *
     * @param order 要删除的列表
     */
    @Override
    public void deleteAll(List<Order> order) {
        for (int i = 0; i < order.size(); i++) {
            getSession().delete(order.get(i));
        }
    }

    /**
     * 通过实体对象删除记录
     *
     * @param model 实体对象
     */
    @Override
    public void deleteObject(Order model) {
        getSession().delete(model);
    }

    /**
     * 通过主键Id获取对象
     *
     * @param id 主键id
     * @return 实体对象
     */
    @Override
    public Order get(Long id) {
        Order model = new Order();
        model = (Order) getSession().get(Order.class, id);
        return model;
    }

    /**
     * 统计整张表的记录数
     *
     * @return 返回记录条数
     */
    @Override
    public Long countAll() {
        String sql = "select count(*) from Order ";
        Long count = (Long) getSession().createQuery(sql).uniqueResult();
        return count;
    }

    /**
     * 查询全部数据记录
     *
     * @return 全部记录列表
     */
    @Override
    public List<Order> listAll() {
        String hql = "from Order ";
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
    public List<Order> searchPage(Map<String, Object> conditions, int page, int pageSize) {
        Session session = getSession();
        String hql;
        if (conditions == null) {
            hql = "from Order ";
        } else {
            Iterator<String> iterator = conditions.keySet().iterator();
            StringBuilder stringBuilder = new StringBuilder("from Order where ");
            while (iterator.hasNext()) {
                String key = iterator.next();
                /*拼接HQL语句*/
                stringBuilder.append(key + " =  " + "'" + conditions.get(key) + "'");
                if (iterator.hasNext()) {
                    stringBuilder.append(" AND ");
                }
            }
            hql = stringBuilder.toString();
        }

        Query query = session.createQuery(hql);
             /*如果pageSize大于0才进行分页*/
        if (pageSize > 0) {
            int startIndex = (page - 1) * pageSize;
            query.setFirstResult(startIndex);
            query.setMaxResults(pageSize);
        }
        return query.list();
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
    public List<Order> searchPageByOrder(Map<String, Object> conditions, String orderBy, String order, int page, int pageSize) {
        Session session = getSession();
        String hql;
        if (conditions == null) {
            hql = "from Order order by " + orderBy + " " + order;
            System.out.println("test.....");
        } else {
            Iterator<String> iterator = conditions.keySet().iterator();
            StringBuilder stringBuilder = new StringBuilder("from Order af where ");
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
        /*如果pageSize大于0才进行分页*/
        if (pageSize > 0) {
            int startIndex = (page - 1) * pageSize;
            query.setFirstResult(startIndex);
            query.setMaxResults(pageSize);
        }
        List<Order> list = query.list();
        return list;
    }

    /**
     * 直接通过HQL语句进行查询
     *
     * @param HQL 语句字符串
     * @return 查询结果列表
     */
    @Override
    public List<Order> searchListDefined(String HQL) {
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
        Order model = (Order) getSession().get(Order.class, id);
        if (model != null)
            return true;
        else
            return false;
    }

    /**
     * 获取一家店铺的全部指定状态订单
     *
     * @param shop        店铺
     * @param orderStatus 订单状态
     * @param page        页码
     * @param pageSize    页面大小,如果小于0则不分页
     * @return 订单列表
     */
    @Override
    public List<Order> getShopOrders(Shop shop, OrderStatus orderStatus, int page, int pageSize) {
        String hql;
        Query query;
        if (orderStatus == null) {
            hql = "from Order where goods.shop.id =:shopid";
            query = getSession().createQuery(hql);
            query.setParameter("shopid", shop.getId());
        } else {
            hql = "from Order where orderStatus=:status and goods.shop.id =:shopid";
            query = getSession().createQuery(hql);
            query.setParameter("status", orderStatus.name());
            query.setParameter("shopid", shop.getId());
        }

        /*如果pageSize大于0才进行分页*/
        if (pageSize > 0) {
            int startIndex = (page - 1) * pageSize;
            query.setFirstResult(startIndex);
            query.setMaxResults(pageSize);
        }
        return query.list();
    }
}
