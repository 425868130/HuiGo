package com.nchu.entity.dataView;

import com.nchu.entity.User;

import java.sql.Date;

/*用户注册表单数据视图*/
public class UserRegDataView {
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRelname() {
        return relname;
    }

    public void setRelname(String relname) {
        this.relname = relname;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getHeadportrait() {
        return headportrait;
    }

    public void setHeadportrait(String headportrait) {
        this.headportrait = headportrait;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*将页面的注册表单数据视图转换为User实体*/
    public static User transToUser(UserRegDataView regDataView) {
        User user = new User();
        user.setAccount(regDataView.getAccount());
        user.setPassword(regDataView.getPassword());
        user.setHeadportrait(regDataView.getHeadportrait());
        user.setTelephone(regDataView.getTelephone());
        user.setRelname(regDataView.getRelname());
        user.setEmail(regDataView.getEmail());
        user.setSex(regDataView.isSex());
        user.setNickName(regDataView.getNickName());
        user.setBirthday(regDataView.getBirthday());
        user.setIdCard(regDataView.getIdCard());
        return user;
    }
}
