package com.nchu.service.impl;

import com.nchu.dao.GoodsDao;
import com.nchu.dao.GroupPurchaseDao;
import com.nchu.dao.ParticipateGroupDao;
import com.nchu.dao.UserDao;
import com.nchu.entity.Goods;
import com.nchu.entity.GroupPurchase;
import com.nchu.entity.ParticipateGroup;
import com.nchu.entity.Saletype;
import com.nchu.entity.User;
import com.nchu.exception.GoodsException;
import com.nchu.exception.GroupPurchaseException;
import com.nchu.service.GroupPurchaseService;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2017-9-23 18:14:55
 * 团购活动相关业务接口实现类
 */
@Service
public class GroupPurchaseServiceImpl implements GroupPurchaseService {

    @Autowired
    GroupPurchaseDao groupPurchaseDao;
    @Autowired
    ParticipateGroupDao participateGroupDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    UserDao userDao;

    /**
     * TODO 创建团购活动
     * 校验团购活动实体的user对象是否为商家
     * 团购活动时间有效性检查
     * 插入数据成功后按团购活动截止定时设置定时任务,
     * 活动截止后自动将活动设置为无效
     * 团购活动开始后自动给所有订阅用户发送提醒邮件
     *
     * @param groupPurchase 团购活动
     * @return 操作结果
     */
    @Override
    public boolean createGroup(GroupPurchase groupPurchase) throws GroupPurchaseException {
        if (groupPurchaseDao.getByGoods(groupPurchase.getGoods()) == null) {
            groupPurchaseDao.save(groupPurchase);
        } else {
            throw new GroupPurchaseException("该商品已经创建了团购活动");
        }
        return true;
    }

    /**
     * TODO 参与团购
     * 用户参与团购,创建参团记录
     *
     * @param user          参团用户
     * @param groupPurchase 团购活动
     * @return 操作结果
     */
    @Override
    public boolean joinGroup(User user, GroupPurchase groupPurchase) throws GroupPurchaseException {
        user = userDao.get(user.getId());
        if (groupPurchase.getNumberPart() + 1 > groupPurchase.getMaxParticipants()) {
            throw new GroupPurchaseException("已满最大参与人数,看看其他商品吧^_^");
        }
        /*判断用户是否已经参与该团购*/
        if (user.getParticipateGroups().stream().filter(participateGroup -> participateGroup.getGroupPurchase().getId() == groupPurchase.getId()).count() > 0) {
            throw new GroupPurchaseException("您已参与该团购,请勿重复操作!");
        }
        try {
            groupPurchase.setNumberPart(groupPurchase.getNumberPart() + 1);
            groupPurchaseDao.save(groupPurchase);
            ParticipateGroup participateGroup = new ParticipateGroup();
            participateGroup.setUser(user);
            participateGroup.setGroupPurchase(groupPurchase);
            participateGroup.setIseffective(true);
            participateGroupDao.save(participateGroup);
            return true;
        } catch (Exception e) {
            throw new GroupPurchaseException("系统异常,参团失败");
        }
    }

    /**
     * TODO 退出团购
     * 用户退出某一团购活动,将参团表中对应用户和团购活动的记录设置为无效
     *
     * @param user          用户
     * @param groupPurchase 团购活动
     * @return 操作结果
     */
    @Override
    public boolean exitGroup(User user, GroupPurchase groupPurchase) throws GroupPurchaseException {
        try {
            participateGroupDao.removeParticipateGroup(user.getId(), groupPurchase.getId());
            return true;
        } catch (Exception e) {
            throw new GroupPurchaseException("退出团购活动异常");
        }
    }

    /**
     * TODO 获取所有参团用户
     * 查询参团表
     *
     * @param groupPurchase 团购活动
     * @return 参团用户表
     */
    @Override
    public List<User> listGroupUsers(GroupPurchase groupPurchase) {
        List<ParticipateGroup> list = participateGroupDao.getAllByGroupPurchase(groupPurchase);
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            userList.add(list.get(i).getUser());
        }
        return userList;
    }

    /**
     * TODO 取消团购活动
     * 将团购活动状态设置为已取消,将所有相关参团记录设置为无效
     * 退回所有已经支付用户的订单金额:获取商品id然后通过OrderService的订单退款方法完成业务
     * 团购活动过期未达到总参与人数时也将取消团购
     *
     * @param groupPurchase 要取消的团购活动
     * @return
     */
    @Override
    public boolean cancelGroup(GroupPurchase groupPurchase) {
        return false;
    }

    /**
     * TODO 通过商品关键词搜索团购
     * 先通过关键词搜索商品,再在团购活动表中搜索含有对应商品id的团购活动
     *
     * @param keywords 关键词
     * @param page     页码
     * @param pageSize 每页大小
     * @return 团购活动列表
     */
    @Override
    public List<GroupPurchase> searchByGoods(String keywords, int page, int pageSize) throws GoodsException {
        List<GroupPurchase> groupPurchaseList = new ArrayList<>();
        List<Goods> goodsList = goodsDao.searchByNamePage(keywords, page, pageSize);
        if (goodsList == null) {
            System.out.println("TIPS:要查找的商品不存在");
            throw new GoodsException("要查找的商品不存在");
        } else {
            for (Goods goods : goodsList) {
                groupPurchaseList.add(goods.getGroupPurchase());
            }
        }
        return groupPurchaseList;
    }

    /**
     * TODO 通过商品类型获取团购
     * 先通过类型搜索已上架商品,再在团购活动表中搜索含有对应商品id的团购活动
     *
     * @param saletype 商品类型
     * @param page     页码
     * @param pageSize 每页大小
     * @return 团购活动列表
     */
    @Override
    public List<GroupPurchase> searchByType(Saletype saletype, int page, int pageSize) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("category", saletype.getId());
        List<Goods> listGoods = goodsDao.searchPage(conditions, page, pageSize);
        List<GroupPurchase> listGP = new ArrayList<>();
        for (int i = 0; i < listGoods.size(); i++) {
            listGP.add(groupPurchaseDao.getByGoods(listGoods.get(i)));
        }
        return listGP;
    }

    /**
     * TODO 获取全部团购活动
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @return 团购活动列表
     */
    @Override
    public List<GroupPurchase> listAllGroup(int page, int pageSize) {
        return groupPurchaseDao.searchPage(null, page, pageSize);
    }

    /**
     * 获取团购活动页码数
     */
    @Override
    public Long groupPageAccount(int pageSize) {
        Long count = groupPurchaseDao.countAll();
        return count / pageSize == 0 ? 1 : count;
    }

    /**
     * TODO 获取参团人数前top名的团购活动
     * 用户首页展示和用户推荐
     *
     * @param top 排名
     * @return 团购活动列表
     */
    @Override
    public List<GroupPurchase> topGroup(int top) {
        return groupPurchaseDao.listAllTop(top);
    }

    /**
     * TODO 通过ID获取团购记录
     *
     * @param groupId 团购ID
     * @return
     */
    @Override
    public GroupPurchase getById(Long groupId) throws GroupPurchaseException {
        GroupPurchase groupPurchase = groupPurchaseDao.get(groupId);
        if (groupPurchase == null) {
            throw new GroupPurchaseException("团购活动不存在!");
        }
        return groupPurchase;
    }

    /**
     * 根据用户ID查询团购列表
     */
    @Override
    public List<GroupPurchase> getGroupByUserId(long userId) {
        return groupPurchaseDao.getGroupByUserId(userId);
    }

    /*
    * 根据主键Id获取团购信息*/
    @Override
    public GroupPurchase get(Long id) {
        return groupPurchaseDao.get(id);
    }

    /*L
    * 根据主键删除团购信息*/
    @Override
    public void delGroupPurchase(Long id) {
        groupPurchaseDao.delete(id);
    }

}
