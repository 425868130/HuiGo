package com.nchu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * 商品表实体类
 */
@Entity
@Table(name = "goods")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties(value = {"shop", "evaluations", "groupPurchase", "gmtCreate", "gmtModified"})
public class Goods implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    private String name;
    private BigDecimal originalprice;
    private BigDecimal preferentialprice;
    /*总库存量*/
    private Long totalInventory;
    /*剩余库存量*/
    private long surplusInventory;
    /*商品所属的店铺*/
    private Shop shop;
    /*商品Logo图片*/
    private String picture;
    /*描述*/
    private String description;
    /*商品类型*/
    private long category;
    /*是否上架*/
    private boolean isonshelves;
    /*商品信息展示图片*/
    private Set<GoodsPicture> goodsPictures = new HashSet<>();
    /*商品评价列表*/
    private Set<Evaluation> evaluations = new HashSet<>();
    /*商品参与的团购活动*/
    private GroupPurchase groupPurchase;

    @OneToMany(mappedBy = "goods")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<GoodsPicture> getGoodsPictures() {
        return goodsPictures;
    }

    public void setGoodsPictures(Set<GoodsPicture> goodsPictures) {
        this.goodsPictures = goodsPictures;
    }

    @OneToMany(mappedBy = "goods")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(Set<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    @ManyToOne
    @JoinColumn(name = "shopid")
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @OneToOne(mappedBy = "goods")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public GroupPurchase getGroupPurchase() {
        return groupPurchase;
    }

    public void setGroupPurchase(GroupPurchase groupPurchase) {
        this.groupPurchase = groupPurchase;
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
    @Column(name = "name", length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "originalprice", precision = 2)
    public BigDecimal getOriginalprice() {
        return originalprice;
    }

    public void setOriginalprice(BigDecimal originalprice) {
        this.originalprice = originalprice;
    }

    @Basic
    @Column(name = "preferentialprice", precision = 2)
    public BigDecimal getPreferentialprice() {
        return preferentialprice;
    }

    public void setPreferentialprice(BigDecimal preferentialprice) {
        this.preferentialprice = preferentialprice;
    }

    @Basic
    @Column(name = "total_inventory", nullable = true)
    public Long getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(Long totalInventory) {
        this.totalInventory = totalInventory;
    }

    @Basic
    @Column(name = "surplus_inventory")
    public long getSurplusInventory() {
        return surplusInventory;
    }

    public void setSurplusInventory(long surplusInventory) {
        this.surplusInventory = surplusInventory;
    }

    @Basic
    @Column(name = "picture", nullable = true, length = 255)
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Basic
    @Column(name = "description", length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "category")
    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    @Basic
    @Column(name = "isonshelves")
    public boolean isIsonshelves() {
        return isonshelves;
    }

    public void setIsonshelves(boolean isonshelves) {
        this.isonshelves = isonshelves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Goods goods = (Goods) o;

        if (id != goods.id) return false;
        if (surplusInventory != goods.surplusInventory) return false;
        if (category != goods.category) return false;
        if (isonshelves != goods.isonshelves) return false;
        if (gmtCreate != null ? !gmtCreate.equals(goods.gmtCreate) : goods.gmtCreate != null) return false;
        if (gmtModified != null ? !gmtModified.equals(goods.gmtModified) : goods.gmtModified != null) return false;
        if (name != null ? !name.equals(goods.name) : goods.name != null) return false;
        if (originalprice != null ? !originalprice.equals(goods.originalprice) : goods.originalprice != null)
            return false;
        if (preferentialprice != null ? !preferentialprice.equals(goods.preferentialprice) : goods.preferentialprice != null)
            return false;
        if (totalInventory != null ? !totalInventory.equals(goods.totalInventory) : goods.totalInventory != null)
            return false;
        if (picture != null ? !picture.equals(goods.picture) : goods.picture != null) return false;
        if (description != null ? !description.equals(goods.description) : goods.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (gmtCreate != null ? gmtCreate.hashCode() : 0);
        result = 31 * result + (gmtModified != null ? gmtModified.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (originalprice != null ? originalprice.hashCode() : 0);
        result = 31 * result + (preferentialprice != null ? preferentialprice.hashCode() : 0);
        result = 31 * result + (totalInventory != null ? totalInventory.hashCode() : 0);
        result = 31 * result + (int) (surplusInventory ^ (surplusInventory >>> 32));
        result = 31 * result + (picture != null ? picture.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (int) (category ^ (category >>> 32));
        result = 31 * result + (isonshelves ? 1 : 0);
        return result;
    }
}
