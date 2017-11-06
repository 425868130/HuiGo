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
 * 店铺表实体类
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "shop")
@JsonIgnoreProperties(value = {"goods", "user", "saletype", "gmtModified"})
public class Shop implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private long id;
    /*店铺所有者*/
    private User user;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    private String name;
    private String addresss;
    private String serviceTel;
    /*销售类型*/
    private Saletype saletype;
    /*是否认证*/
    private byte isVerify;
    private String licenseCode;
    private String licensePic;
    private String logo;
    private String description;
    private boolean islocked;
    /*店铺的商品列表*/
    Set<Goods> goods = new HashSet<>();

    @OneToMany(mappedBy = "shop")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<Goods> getGoods() {
        return goods;
    }

    public void setGoods(Set<Goods> goods) {
        this.goods = goods;
    }

    /*一个商家只能有一个店铺*/
    @OneToOne
    @JoinColumn(name = "userId")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
    @Column(name = "addresss", length = 255)
    public String getAddresss() {
        return addresss;
    }

    public void setAddresss(String addresss) {
        this.addresss = addresss;
    }

    @Basic
    @Column(name = "serviceTel", length = 50)
    public String getServiceTel() {
        return serviceTel;
    }

    public void setServiceTel(String serviceTel) {
        this.serviceTel = serviceTel;
    }

    @ManyToOne
    @JoinColumn(name = "saletype")
    public Saletype getSaletype() {
        return saletype;
    }

    public void setSaletype(Saletype saletype) {
        this.saletype = saletype;
    }

    @Basic
    @Column(name = "isVerify")
    public byte getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(byte isVerify) {
        this.isVerify = isVerify;
    }

    @Basic
    @Column(name = "license_code", length = 255)
    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    @Basic
    @Column(name = "license_pic", length = 255)
    public String getLicensePic() {
        return licensePic;
    }

    public void setLicensePic(String licensePic) {
        this.licensePic = licensePic;
    }

    @Basic
    @Column(name = "logo", length = 255)
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Basic
    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "islocked")
    public boolean isIslocked() {
        return islocked;
    }

    public void setIslocked(boolean islocked) {
        this.islocked = islocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shop shop = (Shop) o;

        if (id != shop.id) return false;
        if (isVerify != shop.isVerify) return false;
        if (islocked != shop.islocked) return false;
        if (gmtCreate != null ? !gmtCreate.equals(shop.gmtCreate) : shop.gmtCreate != null) return false;
        if (gmtModified != null ? !gmtModified.equals(shop.gmtModified) : shop.gmtModified != null) return false;
        if (name != null ? !name.equals(shop.name) : shop.name != null) return false;
        if (addresss != null ? !addresss.equals(shop.addresss) : shop.addresss != null) return false;
        if (serviceTel != null ? !serviceTel.equals(shop.serviceTel) : shop.serviceTel != null) return false;
        if (saletype != null ? !saletype.equals(shop.saletype) : shop.saletype != null) return false;
        if (licenseCode != null ? !licenseCode.equals(shop.licenseCode) : shop.licenseCode != null) return false;
        if (licensePic != null ? !licensePic.equals(shop.licensePic) : shop.licensePic != null) return false;
        if (logo != null ? !logo.equals(shop.logo) : shop.logo != null) return false;
        if (description != null ? !description.equals(shop.description) : shop.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (gmtCreate != null ? gmtCreate.hashCode() : 0);
        result = 31 * result + (gmtModified != null ? gmtModified.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (addresss != null ? addresss.hashCode() : 0);
        result = 31 * result + (serviceTel != null ? serviceTel.hashCode() : 0);
        result = 31 * result + (saletype != null ? saletype.hashCode() : 0);
        result = 31 * result + (int) isVerify;
        result = 31 * result + (licenseCode != null ? licenseCode.hashCode() : 0);
        result = 31 * result + (licensePic != null ? licensePic.hashCode() : 0);
        result = 31 * result + (logo != null ? logo.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (islocked ? 1 : 0);
        return result;
    }
}
