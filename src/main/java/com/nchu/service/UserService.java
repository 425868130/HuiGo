package com.nchu.service;

import com.nchu.entity.User;
import com.nchu.enumdef.UserRoleType;
import com.nchu.exception.UserServiceException;

import javax.mail.MessagingException;
import java.util.List;

/**
 * 2017年9月22日08:35:30
 * 用户相关业务接口
 */
public interface UserService {
    /**
     * TODO 用户登录业务
     * 完成用户账号密码校验,同时账户处于未验证状态无法登陆
     * 如果通过则修改用户账户状态为登录,并返回登录成功的用户对象
     *
     * @param user 要登录的用户
     * @return 返回登录成功的用户(登录失败请返回null)
     */
    User login(User user) throws UserServiceException;

    /**
     * TODO 用户注销
     * 修改用户表状态为未登录
     *
     * @param user 要注销的用户
     * @return 返回操作结果
     */
    boolean logout(User user);

    /**
     * TODO 用户注册业务
     * 传入前台生成的用户实体,向数据库中插入一条新的用户数据
     * 在数据插入前要检查用户account属性是否重复,,要动态生成用户校验码
     *
     * @param user 要注册的用户
     * @return 返回插入执行结果
     */
    boolean register(User user) throws UserServiceException, MessagingException;

    /**
     * TODO 获取用户信息
     * 通过账号获取用户信息
     *
     * @param id 用户账号id
     * @return 返回用户实体
     */
    User getUserById(Long id);

    /**
     * TODO 获取用户信息
     * 通过账号获取用户信息
     *
     * @param account 用户账号
     * @return 返回用户实体
     */
    User getUserByAccount(String account) throws UserServiceException;

    /**
     * TODO 用户分页查询
     * 按用户类型对用户表进行分页查询
     *
     * @param userRoleType 用户角色类型
     * @param page         页码
     * @param pageSize     每页大小
     * @return 对应查询的用户列表
     */
    List<User> listByPage(UserRoleType userRoleType, int page, int pageSize);

    /**
     * TODO 用户信息修改
     * 传入用户实体完成个人信息数据修改,
     * 禁止通过该方法修改用户密码
     *
     * @param user 要修改信息的用户
     * @return 返回操作结果
     */
    boolean updateUserInfo(User user) throws UserServiceException;

    /**
     * TODO 账号密码修改
     * 校验新密码的安全合法性,判断是否与旧密码相同,修改用户账户密码
     *
     * @param user        要修改账户密码的用户
     * @param newPassword 新密码
     * @return 返回操作结果
     */
    boolean changePassword(User user, String newPassword) throws UserServiceException;

    /**
     * TODO 用户账户认证
     * 完成用户账户校验,同时比对注册邮箱和校验码,校验通过则修改用户账户为已验证状态
     *
     * @param user      要认证的用户
     * @param email     认证邮箱
     * @param checkCode 校验码
     * @return 返回校验结果
     */
    boolean Authentication(User user, String email, String checkCode) throws UserServiceException;

    /**
     * TODO 用户账户封禁
     * 通过传入的账号id将指定用户账户的状态改为禁用状态,
     * 此处要验证操作人(管理员)的身份
     *
     * @param id       要禁用的用户id
     * @param operator 执行操作的管理员
     * @return 返回数据库操作结果
     */
    boolean userLock(Long id, User operator) throws UserServiceException;

    /**
     * TODO 用户账户解禁
     * 通过传入的账号id将指定用户账户的状态改为正常状态,
     * 此处要验证操作人(管理员)的身份
     *
     * @param id       要禁用的用户id
     * @param operator 执行操作的管理员
     * @return 返回数据库操作结果
     */
    boolean userUnLock(Long id, User operator) throws UserServiceException;

    /**
     * TODO 列出所有被冻结的账号
     *
     * @param page     页码
     * @param pageSize 页面大小
     * @return 被冻结的用户列表
     */
    List<User> listLockUser(int page, int pageSize);

    List<User> listUserByStatus(Boolean isLocked, String userType);
}
