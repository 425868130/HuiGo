package com.nchu.dao;

import java.util.List;

import com.nchu.entity.Goods;
import com.nchu.entity.GroupPurchase;

/**
 * 2017年9月20日10:09:30
 * 参团表Dao接口
 */
public interface GroupPurchaseDao extends BaseDao<GroupPurchase, Long> {

    /**
     * 根据商品对象model，获得团购活动记录
     */
    GroupPurchase getByGoods(Goods model);

    /**
     * 根据提供的参数，查询人数排名前N的团购活动记录
     */
    List<GroupPurchase> listAllTop(int top);

    /* 根据用户Id 查询团购信息 */
    List<GroupPurchase> getGroupByUserId(long userId);
}
