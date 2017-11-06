package com.nchu.entity.dataView;

import com.nchu.entity.GroupPurchase;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/*简单团购数据视图
首页轮播热门团购数据视图*/
public class SimpleGroupView {
    private long groupId;
    private String groupImg;
    private long goodsId;
    private BigDecimal groupPrice;
    private BigDecimal originPrice;
    private String goodsName;
    private String startDate;
    private String endDate;

    public BigDecimal getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(BigDecimal groupPrice) {
        this.groupPrice = groupPrice;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupImg() {
        return groupImg;
    }

    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getStartDate() {
        return startDate;
    }

    public BigDecimal getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(BigDecimal originPrice) {
        this.originPrice = originPrice;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /*从团购活动详细信息中筛选前端所需信息*/
    public static List<SimpleGroupView> transFromGroupList(List<GroupPurchase> groupPurchaseList) {
        return groupPurchaseList.stream().map(groupPurchase -> {
            SimpleGroupView hotGroupView = new SimpleGroupView();
            hotGroupView.setGroupId(groupPurchase.getId());
            hotGroupView.setGroupPrice(groupPurchase.getGoods().getPreferentialprice());
            hotGroupView.setOriginPrice(groupPurchase.getGoods().getOriginalprice());
            hotGroupView.setGoodsId(groupPurchase.getGoods().getId());
            hotGroupView.setGoodsName(groupPurchase.getGoods().getName());
            hotGroupView.setGroupImg(groupPurchase.getGoods().getPicture());
            hotGroupView.setStartDate(groupPurchase.getStartTime().toString().substring(0, 16));
            hotGroupView.setEndDate(groupPurchase.getEndTime().toString().substring(0, 16));
            return hotGroupView;
        }).collect(Collectors.toList());
    }
}
