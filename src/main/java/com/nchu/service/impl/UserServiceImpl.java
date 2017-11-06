package com.nchu.service.impl;

import com.nchu.dao.UserDao;
import com.nchu.entity.User;
import com.nchu.enumdef.UserRoleType;
import com.nchu.exception.UserServiceException;
import com.nchu.service.EmailService;
import com.nchu.service.UserService;
import com.nchu.util.DateUtil;
import com.nchu.util.MD5Util;
import com.nchu.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2017-9-24 15:46:13
 * 用户相关业务接口实现类
 */
@Service
public class UserServiceImpl implements UserService {
    /*用户默认头像路径*/
    final static String DEFAULT_HEAD = "http://benefit-go.oss-cn-hangzhou.aliyuncs.com/headPortrait/default/userDefault.png";
    @Autowired
    UserDao userDao;
    @Autowired
    EmailService emailService;

    /**
     * TODO 用户登录业务
     * 完成用户账号密码校验,同时账户处于未验证状态无法登陆
     * 如果通过则修改用户账户状态为登录,并返回登录成功的用户对象
     *
     * @param user 要登录的用户
     * @return 返回登录成功的用户(登录失败请返回null)
     */
    @Override
    public User login(User user) throws UserServiceException {
        User temp = getUserByAccount(user.getAccount());
        /*temp不为空说明账号校验已经通过*/
        if (MD5Util.validate(user.getPassword(), temp.getPassword())) {
            /*密码判断通过后判断账号是否验证且未被锁定*/
            if (!temp.isVerification()) {
                throw new UserServiceException("账号未验证,请通过邮件链接激活!");
            } else if (temp.getIslocked()) {
                throw new UserServiceException("账号已冻结!");
            }
            /*验证全部通过后修改用户的在线状态*/
            temp.setLogin(true);
            userDao.update(temp);
            return temp;
        }
        throw new UserServiceException("密码错误,登录失败");
    }

    /**
     * TODO 用户注销
     * 修改用户表状态为未登录
     *
     * @param user 要注销的用户
     * @return 返回操作结果
     */
    @Override
    public boolean logout(User user) {
        user.setLogin(false);
        userDao.update(user);
        return true;
    }

    /**
     * TODO 用户注册业务
     * 传入前台生成的用户实体,向数据库中插入一条新的用户数据
     * 在数据插入前要检查用户account属性是否重复,,要动态生成用户校验码
     *
     * @param user 要注册的用户
     * @return 返回插入执行结果
     */
    @Transactional
    @Override
    public boolean register(User user) throws UserServiceException, MessagingException {
        /*账号不存在才执行注册*/
        if (!userDao.accountCheck(user.getAccount())) {
            user.setVerification(false);
            user.setCheckcode(UUIDUtils.getUUID());
            /*加密账号密码,通过邮箱防止相同*/
            user.setPassword(MD5Util.encode2hex(user.getPassword()));
            user.setHeadportrait(DEFAULT_HEAD);
            if (userDao.save(user) != null) {
                /*发送验证邮件*/
                emailService.sendVerifyMail(user);
                return true;
            }
            throw new UserServiceException("系统异常,注册失败");
        } else {
            throw new UserServiceException("账号已存在");
        }
    }

    /**
     * TODO 获取用户信息
     * 通过账号获取用户信息
     *
     * @param id 用户账号id
     * @return 返回用户实体
     */
    @Override
    public User getUserById(Long id) {
        return userDao.get(id);
    }

    /**
     * TODO 获取用户信息
     * 通过账号获取用户信息
     *
     * @param account 用户账号
     * @return 返回用户实体
     */
    @Override
    public User getUserByAccount(String account) throws UserServiceException {
        User user = userDao.getUserByAccount(account);
        if (user == null) {
            throw new UserServiceException("账号不存在");
        }
        return user;
    }

    /**
     * TODO 用户分页查询
     * 按用户类型对用户表进行分页查询
     *
     * @param userRoleType 用户角色类型
     * @param page         页码
     * @param pageSize     每页大小
     * @return 对应查询的用户列表
     */
    @Override
    public List<User> listByPage(UserRoleType userRoleType, int page, int pageSize) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("role", userRoleType);
        return userDao.searchPage(conditions, page, pageSize);
    }

    /**
     * TODO 用户信息修改
     * 传入用户实体完成个人信息数据修改,
     * 禁止通过该方法修改用户密码
     *
     * @param user 要修改信息的用户
     * @return 返回操作结果
     */
    @Override
    public boolean updateUserInfo(User user) throws UserServiceException {
        try {
            user.setGmtModified(DateUtil.getCurrentTimestamp());
            userDao.update(user);
        } catch (Exception e) {
            throw new UserServiceException("个人信息更新失败,请重试");
        }
        return true;
    }

    /**
     * TODO 账号密码修改
     * 校验新密码的安全合法性,判断是否与旧密码相同,修改用户账户密码
     *
     * @param user        要修改账户密码的用户
     * @param newPassword 新密码
     * @return 返回操作结果
     */
    @Override
    public boolean changePassword(User user, String newPassword) throws UserServiceException {
        if (newPassword.length() < 6) {
            throw new UserServiceException("密码位数不足");
            //TODO 密码校验
        } else if (MD5Util.validate(user.getPassword(), MD5Util.encode2hex(newPassword))) {
            throw new UserServiceException("新密码不能与旧密码相同");
        } else {
            user.setPassword(MD5Util.encode2hex(newPassword + user.getEmail()));
            userDao.update(user);
            return true;
        }
    }

    /**
     * TODO 用户账户认证
     * 完成用户账户校验,同时比对注册邮箱和校验码,校验通过则修改用户账户为已验证状态
     *
     * @param user      要认证的用户
     * @param email     认证邮箱
     * @param checkCode 校验码
     * @return 返回校验结果
     */
    @Transactional
    @Override
    public boolean Authentication(User user, String email, String checkCode) throws UserServiceException {
        /*重新获取完整用户信息*/
        user = userDao.get(user.getId());
        if (user.isVerification()) {
            throw new UserServiceException("该账号已验证,请勿重复操作!");
        }
        /*判断校验码的有效性*/
        if (DateUtil.TheHourUpToNow(user.getGmtCreate()) > 24) {
            /*删除失效账号*/
            userDao.deleteObject(user);
            throw new UserServiceException("验证码已失效,请重新注册");
        }
        if (email.equals(user.getEmail()) && checkCode.equals(user.getCheckcode())) {
            user.setVerification(true);
            userDao.update(user);
            return true;
        } else {
            throw new UserServiceException("邮箱或验证码错误,校验失败");
        }
    }

    /**
     * TODO 用户账户封禁
     * 通过传入的账号id将指定用户账户的状态改为禁用状态,
     * 此处要验证操作人(管理员)的身份
     *
     * @param id       要禁用的用户id
     * @param operator 执行操作的管理员
     * @return 返回数据库操作结果
     */
    @Override
    public boolean userLock(Long id, User operator) throws UserServiceException {
        if (operator.getRole().equals(UserRoleType.ADMIN.toString())) {
            throw new UserServiceException("权限不足,无法操作");
        }
        User temp = userDao.get(id);
        temp.setIslocked(true);
        userDao.update(temp);
        return true;
    }

    /**
     * TODO 用户账户解禁
     * 通过传入的账号id将指定用户账户的状态改为正常状态,
     * 此处要验证操作人(管理员)的身份
     *
     * @param id       要禁用的用户id
     * @param operator 执行操作的管理员
     * @return 返回数据库操作结果
     */
    @Override
    public boolean userUnLock(Long id, User operator) throws UserServiceException {
        if (operator.getRole().equals(UserRoleType.ADMIN.toString())) {
            throw new UserServiceException("权限不足,无法操作");
        }
        User temp = userDao.get(id);
        temp.setIslocked(false);
        userDao.update(temp);
        return true;
    }

    /**
     * TODO 列出所有被冻结的账号
     *
     * @param page     页码
     * @param pageSize 页面大小
     * @return 被冻结的用户列表
     */
    @Override
    public List<User> listLockUser(int page, int pageSize) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("islocked", 1);
        return userDao.searchPage(conditions, page, pageSize);
    }

    @Override
    public List<User> listUserByStatus(Boolean isLocked, String userType) {
        String hql;
        if (isLocked) {
            hql = "from User where islocked = 1 and role = '" + userType + "'";
        } else {
            hql = "from User where islocked = 0 and role = '" + userType + "'";
        }
        return userDao.searchListDefined(hql);
    }
}
