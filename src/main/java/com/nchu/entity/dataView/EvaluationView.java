package com.nchu.entity.dataView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.nchu.entity.Evaluation;
import com.nchu.entity.Order;
import com.nchu.enumdef.OrderStatus;

public class EvaluationView {
    /*评论编号*/
    private Long id;
    /*商品名称*/
    private String name;
    /*商品图片*/
    private String picture;
    /*评论内容*/
    private String content;
    /*评论时间*/
    private Timestamp gmtCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public static List<EvaluationView> getEvaluationView(List<Evaluation> evaluations) {
        List<EvaluationView> evaluationViews = new ArrayList<EvaluationView>();
        for (int i = 0; i < evaluations.size(); i++) {
            EvaluationView evaluationView = new EvaluationView();
            evaluationView.setId(evaluations.get(i).getId());
            evaluationView.setName(evaluations.get(i).getGoods().getName());
            evaluationView.setPicture(evaluations.get(i).getGoods().getPicture());
            evaluationView.setGmtCreate(evaluations.get(i).getGmtCreate());
            evaluationView.setContent(evaluations.get(i).getContent());

            evaluationViews.add(evaluationView);
        }
        return evaluationViews;
    }
}
