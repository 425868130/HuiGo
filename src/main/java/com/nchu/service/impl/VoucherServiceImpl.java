package com.nchu.service.impl;

import com.nchu.dao.VouchersDao;
import com.nchu.entity.User;
import com.nchu.entity.Vouchers;
import com.nchu.enumdef.VoucherStatus;
import com.nchu.exception.VouchersException;
import com.nchu.service.VoucherService;
import com.nchu.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * 2017-9-27 21:27:21
 * 优惠券业务实现类
 */
@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    VouchersDao vouchersDao;

    /**
     * TODO 创建优惠券
     *
     * @param vouchers 优惠券,需要包含所有者信息
     * @return 操作结果
     */
    @Transactional
    @Override
    public boolean createVouchers(Vouchers vouchers) throws VouchersException {
        vouchers.setGmtModified(DateUtil.getCurrentTimestamp());
        vouchers.setGmtCreate(DateUtil.getCurrentTimestamp());
        vouchers.setIsused(false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        /*优惠券的有效期为获取后的两天之内*/
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        vouchers.setIndate(Timestamp.from(calendar.getTime().toInstant()));
        try {
            vouchersDao.save(vouchers);
        } catch (Exception e) {
            throw new VouchersException("团购券创建失败");
        }
        return true;
    }

    /**
     * TODO 通过id获取团购券
     *
     * @param id 团购券记录id
     * @return 团购券对象
     */
    @Override
    public Vouchers getById(Long id) {
        return vouchersDao.get(id);
    }

    /**
     * TODO 获取用户全部优惠券
     *
     * @param user     用户
     * @param isused   是否使用
     * @param page     页码
     * @param pageSize 页大小
     * @return 优惠券列表
     */
    @Override
    public List<Vouchers> listAllByUser(User user, boolean isused, int page, int pageSize) throws VouchersException {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("userid", user.getId());
        conditions.put("isused", isused);
        List<Vouchers> vouchersList;
        try {
            vouchersList = vouchersDao.searchPage(conditions, page, pageSize);
        } catch (Exception e) {
            throw new VouchersException("暂无相关优惠券");
        }
        return vouchersList;
    }

    /**
     * TODO　判断优惠券是否有效
     * 判断优惠券的有效期和是否被使用
     *
     * @param vouchers 　优惠券
     * @return 判断结果
     */
    @Override
    public boolean isValid(Vouchers vouchers) {
        vouchers = vouchersDao.get(vouchers.getId());
        Timestamp timestamp = DateUtil.getCurrentTimestamp();
        /*如果当前时间在优惠券有效期之前则优惠券有效*/
        if (!vouchers.isIsused() && timestamp.before(vouchers.getIndate())) {
            return true;
        }
        return false;
    }

    /**
     * TODO 使用优惠券
     * 判断团购券是否有效,将团购券设置为已使用
     *
     * @param vouchers 团购券
     * @param operator 操作人
     * @return 操作结果
     */
    @Transactional
    @Override
    public boolean userVouchers(Vouchers vouchers, User operator) throws VouchersException {
        if (isValid(vouchers) && vouchers.getUser().getId() == operator.getId()) {
            vouchers.setIsused(true);
            vouchers.setStatus(VoucherStatus.DISABLED.name());
            vouchers.setGmtModified(DateUtil.getCurrentTimestamp());
            return true;
        } else {
            throw new VouchersException("无效的团购券");
        }
    }

    /**
     * TODO 删除用户所有已使用和已失效的团购券
     *
     * @param user 用户
     * @return 操作结果
     */
    @Transactional
    @Override
    public boolean deletedVouchers(User user) throws VouchersException {
        try {
            vouchersDao.deleteInvalid(user);
        } catch (Exception e) {
            throw new VouchersException("操作失败,请重试");
        }
        return true;
    }
}
