package com.nchu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 消息记录表实体
 */
@Entity
@Table(name = "message")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({"sender", "receiver", "gmtModified"})
public class Message implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    /*发送人*/
    private User sender;
    /*接收人*/
    private User receiver;
    private String senderNicke;
    private String content;
    private int type;
    private boolean isread;
    private boolean isdeleted;

    @ManyToOne
    @JoinColumn(name = "senderid")
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @ManyToOne
    @JoinColumn(name = "receiveid")
    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
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
    @Column(name = "senderNicke", length = 255)
    public String getSenderNicke() {
        return senderNicke;
    }

    public void setSenderNicke(String senderNicke) {
        this.senderNicke = senderNicke;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "isread")
    public boolean isIsread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }

    @Basic
    @Column(name = "isdeleted")
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

        Message message = (Message) o;

        if (id != message.id) return false;
        if (type != message.type) return false;
        if (isread != message.isread) return false;
        if (isdeleted != message.isdeleted) return false;
        if (gmtCreate != null ? !gmtCreate.equals(message.gmtCreate) : message.gmtCreate != null) return false;
        if (gmtModified != null ? !gmtModified.equals(message.gmtModified) : message.gmtModified != null) return false;
        if (sender != null ? !sender.equals(message.sender) : message.sender != null) return false;
        if (receiver != null ? !receiver.equals(message.receiver) : message.receiver != null) return false;
        if (senderNicke != null ? !senderNicke.equals(message.senderNicke) : message.senderNicke != null) return false;
        return content != null ? content.equals(message.content) : message.content == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (gmtCreate != null ? gmtCreate.hashCode() : 0);
        result = 31 * result + (gmtModified != null ? gmtModified.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (senderNicke != null ? senderNicke.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + (isread ? 1 : 0);
        result = 31 * result + (isdeleted ? 1 : 0);
        return result;
    }
}
