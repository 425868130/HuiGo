package com.nchu.dao.impl;

import com.nchu.dao.UserDao;
import com.nchu.entity.User;
import com.nchu.util.DateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 2017年9月20日08:10:27
 * 用户DAO层实现类
 */
@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @Autowired
    SessionFactory sessionfactory;

    /**
     * 获取hibernate回话session
     */
    private Session getSession() {
        return sessionfactory.getCurrentSession();
    }

    /**
     * 登录校验,传入use对象(只有账号和密码数据),
     * 查找比对用户账户密码信息,
     * 返回登录成功的用户对象
     *
     * @param user 待验证的用户对象
     * @return 登录成功的用户对象
     */
    @Override
    public User loginCheck(User user) {
        String hql = "from User where account = :account and password = :psd";
        Query query = getSession().createQuery(hql);
        query.setParameter("account", user.getAccount());
        query.setParameter("psd", user.getPassword());
        return (User) query.uniqueResult();
    }

    /**
     * 判断用户账号是否已经存在,用户注册前进行账号重复性检查
     *
     * @param account 要检查的账号
     * @return 若账号已存在返回true, 否则返回false
     */
    @Override
    public boolean accountCheck(String account) {
        String hql = "select count(*) from User where account=:ac";
        Query query = getSession().createQuery(hql);
        query.setParameter("ac", account);
        long count = (long) query.uniqueResult();
        if (count != 0) {
            return true;
        }
        return false;
    }

    /**
     * 通过账号获取用户信息
     *
     * @param account 用户账户
     * @return 返回用户实体
     */
    @Override
    public User getUserByAccount(String account) {
        String hql = "from User where account =:ac";
        Query query = getSession().createQuery(hql);
        query.setParameter("ac", account);
        return (User) query.uniqueResult();
    }

    /**
     * 保存一个对象到数据库
     *
     * @param model 要保存的对象
     * @return 主键
     */
    @Override
    public Long save(User model) {
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        model.setGmtCreate(DateUtil.getCurrentTimestamp());
        getSession().save(model);
        System.out.println("userId" + model.getId());
        return model.getId();
    }

    /**
     * 保存或者更新,当数据库中存在同id对象时执行更新操作,没有则执行插入操作
     *
     * @param model 实体类
     */
    @Override
    public void saveOrUpdate(User model) {
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        getSession().saveOrUpdate(model);
    }

    /**
     * 更新一条记录
     *
     * @param model 要更新的实例对象
     */
    @Override
    public void update(User model) {
        model.setGmtModified(DateUtil.getCurrentTimestamp());
        getSession().update(model);
    }

    /**
     * 混合方法
     *
     * @param model
     */
    @Override
    public void merge(User model) {
        getSession().merge(model);
    }

    /**
     * 根据主键id删除记录
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        User model = (User) getSession().get(User.class, id);
        getSession().delete(model);
    }

    /**
     * 删除所有指定的持久化对象
     *
     * @param users 要删除的列表
     */
    @Override
    public void deleteAll(List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            getSession().delete(users.get(i));
        }
    }

    /**
     * 通过实体对象删除记录
     *
     * @param model 实体对象
     */
    @Override
    public void deleteObject(User model) {
        getSession().delete(model);
    }

    /**
     * 通过主键Id获取对象
     *
     * @param id 主键id
     * @return 实体对象
     */
    @Override
    public User get(Long id) {
        return (User) getSession().get(User.class, id);
    }

    /**
     * 统计整张表的记录数
     *
     * @return 返回记录条数
     */
    @Override
    public Long countAll() {
        String sql = "select count(*) from User";
        long count = (long) getSession().createQuery(sql).uniqueResult();
        return count;
    }

    /**
     * 查询全部数据记录
     *
     * @return 全部记录列表
     */
    @Override
    public List<User> listAll() {
        String hql = "from User";
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
    public List<User> searchPage(Map<String, Object> conditions, int page, int pageSize) {
        return null;
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
    public List<User> searchPageByOrder(Map<String, Object> conditions, String orderBy, String order, int page, int pageSize) {
        return null;
    }

    @Override
    public List<User> searchListDefined(String HQL) {
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
        User model = (User) getSession().get(User.class, id);
        if (model != null)
            return true;
        else
            return false;
    }
}
