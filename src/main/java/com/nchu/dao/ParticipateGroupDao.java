package com.nchu.dao;

import java.util.List;

import com.nchu.entity.GroupPurchase;
import com.nchu.entity.ParticipateGroup;

/**
 * 2017-9-25 16:24:04
 * 用户参团表DAO接口
 */
public interface ParticipateGroupDao extends BaseDao<ParticipateGroup, Long> {

    /**
     * 根据用户id和团购活动id，增加参团记录
     */
    public void createParticipateGroup(Long userid, Long groupid);

    /**
     * 根据用户id和团购活动id，设置参团记录
     */
    public void removeParticipateGroup(Long userid, Long groupid);

    /**
     * 根据团购活动id，查找所有参团记录
     */
    public List<ParticipateGroup> getAllByGroupPurchase(GroupPurchase model);
}
