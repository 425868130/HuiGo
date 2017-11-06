package com.nchu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 优惠券表实体类
 */
@Entity
@Table(name = "vouchers")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({"user", "gmtModified"})
public class Vouchers implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    /*持有人*/
    private User user;
    private String seriacod;
    private Timestamp indate;
    /*优惠价格*/
    private BigDecimal price;
    /*限制使用条件金额,不足无法使用*/
    private BigDecimal limituse;
    private String status;
    private boolean isused;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
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

    @ManyToOne
    @JoinColumn(name = "userid")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Basic
    @Column(name = "seriacod", length = 255)
    public String getSeriacod() {
        return seriacod;
    }

    public void setSeriacod(String seriacod) {
        this.seriacod = seriacod;
    }

    @Basic
    @Column(name = "indate")
    public Timestamp getIndate() {
        return indate;
    }

    public void setIndate(Timestamp indate) {
        this.indate = indate;
    }

    @Basic
    @Column(name = "price", precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Basic
    @Column(name = "limituse", precision = 2)
    public BigDecimal getLimituse() {
        return limituse;
    }

    public void setLimituse(BigDecimal limituse) {
        this.limituse = limituse;
    }

    @Basic
    @Column(name = "status", length = 20)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "isused")
    public boolean isIsused() {
        return isused;
    }

    public void setIsused(boolean isused) {
        this.isused = isused;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vouchers vouchers = (Vouchers) o;

        if (id != vouchers.id) return false;
        if (isused != vouchers.isused) return false;
        if (gmtCreate != null ? !gmtCreate.equals(vouchers.gmtCreate) : vouchers.gmtCreate != null) return false;
        if (gmtModified != null ? !gmtModified.equals(vouchers.gmtModified) : vouchers.gmtModified != null)
            return false;
        if (seriacod != null ? !seriacod.equals(vouchers.seriacod) : vouchers.seriacod != null) return false;
        if (indate != null ? !indate.equals(vouchers.indate) : vouchers.indate != null) return false;
        if (price != null ? !price.equals(vouchers.price) : vouchers.price != null) return false;
        if (limituse != null ? !limituse.equals(vouchers.limituse) : vouchers.limituse != null) return false;
        if (status != null ? !status.equals(vouchers.status) : vouchers.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (gmtCreate != null ? gmtCreate.hashCode() : 0);
        result = 31 * result + (gmtModified != null ? gmtModified.hashCode() : 0);
        result = 31 * result + (seriacod != null ? seriacod.hashCode() : 0);
        result = 31 * result + (indate != null ? indate.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (limituse != null ? limituse.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (isused ? 1 : 0);
        return result;
    }
}
