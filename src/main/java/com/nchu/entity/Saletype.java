package com.nchu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 销售类型表实体类
 */
@Entity
@Table(name = "saletype")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties(value = {"gmtCreate", "gmtModified"})
public class Saletype implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    private String name;
    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Saletype)) return false;

        Saletype saletype = (Saletype) o;

        if (id != null ? !id.equals(saletype.id) : saletype.id != null) return false;
        if (gmtCreate != null ? !gmtCreate.equals(saletype.gmtCreate) : saletype.gmtCreate != null) return false;
        if (gmtModified != null ? !gmtModified.equals(saletype.gmtModified) : saletype.gmtModified != null)
            return false;
        if (name != null ? !name.equals(saletype.name) : saletype.name != null) return false;
        return description != null ? description.equals(saletype.description) : saletype.description == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (gmtCreate != null ? gmtCreate.hashCode() : 0);
        result = 31 * result + (gmtModified != null ? gmtModified.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
