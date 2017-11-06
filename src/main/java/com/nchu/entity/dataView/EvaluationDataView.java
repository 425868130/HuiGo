package com.nchu.entity.dataView;

import com.nchu.entity.Evaluation;

import java.util.List;
import java.util.stream.Collectors;

public class EvaluationDataView {
    private Evaluation evaluation;
    /*评论人昵称*/
    private String userNickName;
    private String userHeadImg;

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public EvaluationDataView(Evaluation evaluation, String userNickName, String userHeadImg) {
        this.evaluation = evaluation;
        this.userNickName = userNickName;
        this.userHeadImg = userHeadImg;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    /*从持久化评论列表对象中农装换出评论数据视图*/
    public static List<EvaluationDataView> transFrom(List<Evaluation> evaluationList) {
        return evaluationList.stream().map(evaluation -> new EvaluationDataView(evaluation, evaluation.getUser().getNickName(), evaluation.getUser().getHeadportrait())).collect(Collectors.toList());
    }
}
