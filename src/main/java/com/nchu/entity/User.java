package com.nchu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户表对应实体类
 */
@Entity
@Table(name = "user")
/*配置转换为json对象时要忽略的属性,防止hibernate触发懒加载导致递归查询*/
@JsonIgnoreProperties({
        "password", "receivingAddress", "favorites", "orders", "participateGroups", "msgBox", "sendMsgBox", "vouchers", "idCard", "gmtCreate", "gmtModified"})
@DynamicInsert
@DynamicUpdate
public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String account;
    private String password;
    private String relname;
    private String nickName;
    private String email;
    private String telephone;
    private String headportrait;
    private boolean sex;
    private Date birthday;
    private String idCard;
    private String role;
    private Boolean islocked;
    private long integral;
    private BigDecimal balance;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    private String checkcode;
    private boolean isVerification;
    private boolean isLogin;
    /*收货地址*/
    private Set<ReceivingAddress> receivingAddress = new HashSet<>();
    /*收藏夹*/
    private Set<Favorites> favorites = new HashSet<>();
    /*订单*/
    private Set<Order> orders = new HashSet<>();
    /*用户参团记录*/
    private Set<ParticipateGroup> participateGroups = new HashSet<>();

    /*消息收件箱:用户为接收人*/
    private Set<Message> MsgBox = new HashSet<>();
    /*发件箱;发送人为用户*/
    private Set<Message> SendMsgBox = new HashSet<>();

    /*优惠券,一个用户可以持有多个优惠券*/

    private Set<Vouchers> vouchers = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<ReceivingAddress> getReceivingAddress() {
        return receivingAddress;
    }

    public void setReceivingAddress(Set<ReceivingAddress> receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<Favorites> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Favorites> favorites) {
        this.favorites = favorites;
    }

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    /*一个用户可以参与多个团购*/
    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<ParticipateGroup> getParticipateGroups() {
        return participateGroups;
    }

    public void setParticipateGroups(Set<ParticipateGroup> participateGroups) {
        this.participateGroups = participateGroups;
    }

    @OneToMany(mappedBy = "receiver")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<Message> getMsgBox() {
        return MsgBox;
    }

    public void setMsgBox(Set<Message> msgBox) {
        MsgBox = msgBox;
    }

    @OneToMany(mappedBy = "sender")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<Message> getSendMsgBox() {
        return SendMsgBox;
    }

    public void setSendMsgBox(Set<Message> sendMsgBox) {
        SendMsgBox = sendMsgBox;
    }

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<Vouchers> getVouchers() {
        return vouchers;
    }

    public void setVouchers(Set<Vouchers> vouchers) {
        this.vouchers = vouchers;
    }

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
    @Column(name = "account", nullable = false, length = 25)
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Basic
    @Column(name = "password", length = 25)
    /*忽略password字段防止密码泄露到前端,但是不忽略set方法,使前端密码可以传到后端*/
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "relname", length = 20)
    public String getRelname() {
        return relname;
    }

    public void setRelname(String relname) {
        this.relname = relname;
    }

    @Basic
    @Column(name = "nickName", length = 50)
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Basic
    @Column(name = "email", length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "telephone", length = 20)
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Basic
    @Column(name = "headportrait", length = 255)
    public String getHeadportrait() {
        return headportrait;
    }

    public void setHeadportrait(String headportrait) {
        this.headportrait = headportrait;
    }

    @Basic
    @Column(name = "sex")
    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "idCard")
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Basic
    @Column(name = "role", length = 20)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "islocked")
    public Boolean getIslocked() {
        return islocked;
    }

    public void setIslocked(Boolean islocked) {
        this.islocked = islocked;
    }

    @Basic
    @Column(name = "integral")
    public long getIntegral() {
        return integral;
    }

    public void setIntegral(long integral) {
        this.integral = integral;
    }

    @Basic
    @Column(name = "balance", precision = 2)
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
    @Column(name = "checkcode", length = 255)
    public String getCheckcode() {
        return checkcode;
    }

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    @Basic
    @Column(name = "isVerification")
    public boolean isVerification() {
        return isVerification;
    }

    public void setVerification(boolean verification) {
        isVerification = verification;
    }

    @Basic
    @Column(name = "isLogin")
    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (sex != user.sex) return false;
        if (integral != user.integral) return false;
        if (isVerification != user.isVerification) return false;
        if (isLogin != user.isLogin) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (account != null ? !account.equals(user.account) : user.account != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (relname != null ? !relname.equals(user.relname) : user.relname != null) return false;
        if (nickName != null ? !nickName.equals(user.nickName) : user.nickName != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (telephone != null ? !telephone.equals(user.telephone) : user.telephone != null) return false;
        if (headportrait != null ? !headportrait.equals(user.headportrait) : user.headportrait != null) return false;
        if (birthday != null ? !birthday.equals(user.birthday) : user.birthday != null) return false;
        if (idCard != null ? !idCard.equals(user.idCard) : user.idCard != null) return false;
        if (role != null ? !role.equals(user.role) : user.role != null) return false;
        if (islocked != null ? !islocked.equals(user.islocked) : user.islocked != null) return false;
        if (balance != null ? !balance.equals(user.balance) : user.balance != null) return false;
        if (gmtCreate != null ? !gmtCreate.equals(user.gmtCreate) : user.gmtCreate != null) return false;
        if (gmtModified != null ? !gmtModified.equals(user.gmtModified) : user.gmtModified != null) return false;
        if (checkcode != null ? !checkcode.equals(user.checkcode) : user.checkcode != null) return false;
        if (receivingAddress != null ? !receivingAddress.equals(user.receivingAddress) : user.receivingAddress != null)
            return false;
        if (favorites != null ? !favorites.equals(user.favorites) : user.favorites != null) return false;
        if (orders != null ? !orders.equals(user.orders) : user.orders != null) return false;
        if (participateGroups != null ? !participateGroups.equals(user.participateGroups) : user.participateGroups != null)
            return false;
        if (MsgBox != null ? !MsgBox.equals(user.MsgBox) : user.MsgBox != null) return false;
        if (SendMsgBox != null ? !SendMsgBox.equals(user.SendMsgBox) : user.SendMsgBox != null) return false;
        return vouchers != null ? vouchers.equals(user.vouchers) : user.vouchers == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (relname != null ? relname.hashCode() : 0);
        result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (headportrait != null ? headportrait.hashCode() : 0);
        result = 31 * result + (sex ? 1 : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (idCard != null ? idCard.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (islocked != null ? islocked.hashCode() : 0);
        result = 31 * result + (int) (integral ^ (integral >>> 32));
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (gmtCreate != null ? gmtCreate.hashCode() : 0);
        result = 31 * result + (gmtModified != null ? gmtModified.hashCode() : 0);
        result = 31 * result + (checkcode != null ? checkcode.hashCode() : 0);
        result = 31 * result + (isVerification ? 1 : 0);
        result = 31 * result + (isLogin ? 1 : 0);
        return result;
    }
}
