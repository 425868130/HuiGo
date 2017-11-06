package com.nchu.service;

import com.nchu.entity.Evaluation;
import com.nchu.entity.Goods;
import com.nchu.entity.User;
import com.nchu.exception.EvaluationException;

import java.util.List;

/**
 * 2017-9-23 18:13:14
 * 商品评论相关业务接口
 */
public interface EvaluationService {
    /**
     * TODO 添加评价
     * (生成一条系统消息通知商家)
     *
     * @param evaluation 评价实体
     * @return 操作结果
     */
    boolean addEvaluation(Evaluation evaluation);

    /**
     * TODO 加载商品评价
     *
     * @param goods    要加载评论的商品
     * @param page     页码
     * @param pageSize 每页大小
     * @return 商品评论列表
     */
    List<Evaluation> listGoodsEvaluation(Goods goods, int page, int pageSize);

    /**
     * TODO 清空商品全部评价
     *
     * @param goods    要清空评价的商品
     * @param operator 操作员,必须是管理员
     * @return 操作结果
     */
    boolean clearAllEvaluation(Goods goods, User operator) throws EvaluationException;

    List<Evaluation> listUserEvaluation(User user, int page, int pageSize);
}
