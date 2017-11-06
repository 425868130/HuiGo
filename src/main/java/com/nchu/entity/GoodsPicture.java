package com.nchu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商品图片表
 */
@Entity
@Table(name = "goods_picture")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({"goods", "gmtCreate", "gmtModified"})
public class GoodsPicture implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    /*图片存储URL地址*/
    private String saveUrl;
    /*关联的商品*/
    private Goods goods;
    private boolean isdeleted;

    @ManyToOne
    @JoinColumn(name = "goodsid")
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "gmt_create", nullable = false)
    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Basic
    @Column(name = "gmt_modified", nullable = false)
    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Basic
    @Column(name = "save_url", nullable = false, length = 255)
    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    @Basic
    @Column(name = "isdeleted", nullable = false)
    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoodsPicture that = (GoodsPicture) o;

        if (id != that.id) return false;
        if (isdeleted != that.isdeleted) return false;
        if (gmtCreate != null ? !gmtCreate.equals(that.gmtCreate) : that.gmtCreate != null) return false;
        if (gmtModified != null ? !gmtModified.equals(that.gmtModified) : that.gmtModified != null) return false;
        if (saveUrl != null ? !saveUrl.equals(that.saveUrl) : that.saveUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (gmtCreate != null ? gmtCreate.hashCode() : 0);
        result = 31 * result + (gmtModified != null ? gmtModified.hashCode() : 0);
        result = 31 * result + (saveUrl != null ? saveUrl.hashCode() : 0);
        result = 31 * result + (isdeleted ? 1 : 0);
        return result;
    }
}
