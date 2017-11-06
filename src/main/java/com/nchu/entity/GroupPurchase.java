package com.nchu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * 团购活动表实体类,记录团购活动的相关信息
 */
@Entity
@Table(name = "group_purchase")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties(value = {"user", "participateGroup", "gmtCreate", "gmtModified"})
public class GroupPurchase implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    /*开启团购活动的商家*/
    private User user;
    /*默认的快递方式*/
    private ExpressDelivery expressDelivery;
    /*参团商品*/
    private Goods goods;
    /*最低参与人数*/
    private long minParticipants;
    /*最大参与人数*/
    private long maxParticipants;
    private Timestamp startTime;
    private Timestamp endTime;
    /*是否有效*/
    private boolean iseffective;
    private String description;
    /*参与人数*/
    private int numberPart;
    /*参团记录表*/
    private Set<ParticipateGroup> participateGroup = new HashSet<>();

    @OneToMany(mappedBy = "groupPurchase")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<ParticipateGroup> getParticipateGroup() {
        return participateGroup;
    }

    public void setParticipateGroup(Set<ParticipateGroup> participateGroup) {
        this.participateGroup = participateGroup;
    }


    /*一个商家可以开启多个团购*/
    @ManyToOne
    @JoinColumn(name = "userid")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /*多个团购可以使用相同的快递*/
    @ManyToOne
    @JoinColumn(name = "express_deliveryid")
    public ExpressDelivery getExpressDelivery() {
        return expressDelivery;
    }

    public void setExpressDelivery(ExpressDelivery expressDelivery) {
        this.expressDelivery = expressDelivery;
    }

    @OneToOne
    @JoinColumn(name = "goodsid")
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "gmt_create")
    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Basic
    @Column(name = "gmt_modified")
    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Basic
    @Column(name = "min_participants")
    public long getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(long minParticipants) {
        this.minParticipants = minParticipants;
    }

    @Basic
    @Column(name = "max_participants")
    public long getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(long maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    @Basic
    @Column(name = "start_time")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "iseffective")
    public boolean isIseffective() {
        return iseffective;
    }

    public void setIseffective(boolean iseffective) {
        this.iseffective = iseffective;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "number_part")
    public int getNumberPart() {
        return numberPart;
    }

    public void setNumberPart(int numberPart) {
        this.numberPart = numberPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupPurchase that = (GroupPurchase) o;
        if (id != that.id) return false;
        if (minParticipants != that.minParticipants) return false;
        if (maxParticipants != that.maxParticipants) return false;
        if (iseffective != that.iseffective) return false;
        if (gmtCreate != null ? !gmtCreate.equals(that.gmtCreate) : that.gmtCreate != null) return false;
        if (gmtModified != null ? !gmtModified.equals(that.gmtModified) : that.gmtModified != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (gmtCreate != null ? gmtCreate.hashCode() : 0);
        result = 31 * result + (gmtModified != null ? gmtModified.hashCode() : 0);
        result = 31 * result + (int) (minParticipants ^ (minParticipants >>> 32));
        result = 31 * result + (int) (maxParticipants ^ (maxParticipants >>> 32));
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (iseffective ? 1 : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
