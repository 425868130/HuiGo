package com.nchu.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户参团表
 */
@Entity
@Table(name = "participate_group")
public class ParticipateGroup implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    /*参与团购的用户*/
    private User user;
    /*参与的团购活动*/
    private GroupPurchase groupPurchase;
    private boolean iseffective;

    @ManyToOne
    @JoinColumn(name = "userid")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @ManyToOne
    @JoinColumn(name = "groupid")
    public GroupPurchase getGroupPurchase() {
        return groupPurchase;
    }

    public void setGroupPurchase(GroupPurchase groupPurchase) {
        this.groupPurchase = groupPurchase;
    }

    @Id
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
    @Column(name = "iseffective", nullable = false)
    public boolean isIseffective() {
        return iseffective;
    }

    public void setIseffective(boolean iseffective) {
        this.iseffective = iseffective;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParticipateGroup that = (ParticipateGroup) o;

        if (id != that.id) return false;
        if (iseffective != that.iseffective) return false;
        if (gmtCreate != null ? !gmtCreate.equals(that.gmtCreate) : that.gmtCreate != null) return false;
        if (gmtModified != null ? !gmtModified.equals(that.gmtModified) : that.gmtModified != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (gmtCreate != null ? gmtCreate.hashCode() : 0);
        result = 31 * result + (gmtModified != null ? gmtModified.hashCode() : 0);
        result = 31 * result + (iseffective ? 1 : 0);
        return result;
    }
}
