package com.nchu.service.impl;

import com.nchu.dao.EvaluationDao;
import com.nchu.dao.GoodsDao;
import com.nchu.entity.Evaluation;
import com.nchu.entity.Goods;
import com.nchu.entity.User;
import com.nchu.enumdef.UserRoleType;
import com.nchu.exception.EvaluationException;
import com.nchu.service.EvaluationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2017-9-23 18:13:14
 * 商品评论相关业务接口实现类
 */
@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    EvaluationDao ed;

    /**
     * TODO 添加评价
     * (生成一条系统消息通知商家)
     *
     * @param evaluation 评价实体
     * @return 操作结果
     */
    @Override
    public boolean addEvaluation(Evaluation evaluation) {
        Long id = ed.save(evaluation);
        //系统消息未实现
        if (id == evaluation.getId()) {
            return true;
        } else
            return false;
    }

    /**
     * TODO 加载商品评价
     *
     * @param goods    要加载评论的商品
     * @param page     页码
     * @param pageSize 每页大小
     * @return 商品评论列表
     */
    @Override
    public List<Evaluation> listGoodsEvaluation(Goods goods, int page, int pageSize) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("goodsid", goods.getId());
        return ed.searchPage(conditions, page, pageSize);
    }

    /**
     * TODO 清空商品全部评价
     *
     * @param goods    要清空评价的商品
     * @param operator 操作员,必须是管理员
     * @return 操作结果
     */
    @Override
    public boolean clearAllEvaluation(Goods goods, User operator) throws EvaluationException {
        if (Integer.valueOf(operator.getRole()) == UserRoleType.ADMIN.getIndex()) {
            try {
                ed.deleteAllByGoods(goods);
                return true;
            } catch (Exception e) {
                throw new EvaluationException("清空评价异常");
            }
        } else
            throw new EvaluationException("清空评价非法操作者");
    }

    /**
     * TODO 加载用户评价
     *
     * @param user     要加载评论的用户
     * @param page     页码
     * @param pageSize 每页大小
     * @return 用户评论列表
     */
    @Override
    public List<Evaluation> listUserEvaluation(User user, int page, int pageSize) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("userid", user.getId());
        return ed.searchPage(conditions, page, pageSize);
    }
}
