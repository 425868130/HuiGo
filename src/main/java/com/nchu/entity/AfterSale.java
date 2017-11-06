package com.nchu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 售后记录实体表
 */
@Entity
@Table(name = "after_sale")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({"gmtModified"})
public class AfterSale implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    private String title;
    private String description;
    private int serviceStatus;
    /*关联的订单*/
    private Order order;
    /*处理人:管理员对象*/
    private User handlePerson;
    private String remark;

    @ManyToOne
    @JoinColumn(name = "orderid")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
    @Column(name = "title", length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    @Column(name = "service_status")
    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    @ManyToOne
    @JoinColumn(name = "handle_person")
    public User getHandlePerson() {
        return handlePerson;
    }

    public void setHandlePerson(User handlePerson) {
        this.handlePerson = handlePerson;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AfterSale afterSale = (AfterSale) o;
        if (id != afterSale.id) return false;
        if (serviceStatus != afterSale.serviceStatus) return false;
        if (handlePerson != afterSale.handlePerson) return false;
        if (gmtCreate != null ? !gmtCreate.equals(afterSale.gmtCreate) : afterSale.gmtCreate != null) return false;
        if (gmtModified != null ? !gmtModified.equals(afterSale.gmtModified) : afterSale.gmtModified != null)
            return false;
        if (title != null ? !title.equals(afterSale.title) : afterSale.title != null) return false;
        if (description != null ? !description.equals(afterSale.description) : afterSale.description != null)
            return false;
        if (remark != null ? !remark.equals(afterSale.remark) : afterSale.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (gmtCreate != null ? gmtCreate.hashCode() : 0);
        result = 31 * result + (gmtModified != null ? gmtModified.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + serviceStatus;
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
