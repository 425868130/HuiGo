package com.nchu.service;

import com.nchu.entity.User;
import com.nchu.entity.Vouchers;
import com.nchu.exception.VouchersException;

import java.util.List;

/**
 * 2017-9-27 17:03:01
 * 优惠券相关业务接口
 */
public interface VoucherService {
    /**
     * TODO 创建优惠券
     *
     * @param vouchers 优惠券
     * @return 操作 结果
     */
    boolean createVouchers(Vouchers vouchers) throws VouchersException;

    /**
     * TODO 通过id获取团购券
     *
     * @param id 团购券记录id
     * @return 团购券对象
     */
    Vouchers getById(Long id);

    /**
     * TODO 获取用户全部优惠券
     *
     * @param user     用户
     * @param isused   是否使用
     * @param page     页码
     * @param pageSize 页大小
     * @return 优惠券列表
     */
    List<Vouchers> listAllByUser(User user, boolean isused, int page, int pageSize) throws VouchersException;

    /**
     * TODO　判断优惠券是否有效
     * 判断优惠券的有效期和是否被使用
     *
     * @param vouchers 　优惠券
     * @return 判断结果
     */
    boolean isValid(Vouchers vouchers);

    /**
     * TODO 使用优惠券
     * 判断团购券是否有效,将团购券设置为已使用
     *
     * @param vouchers 团购券
     * @param operator 操作人
     * @return 操作结果
     */
    boolean userVouchers(Vouchers vouchers, User operator) throws VouchersException;

    /**
     * TODO 删除用户所有已使用和已失效的团购券
     *
     * @param user 用户
     * @return 操作结果
     */
    boolean deletedVouchers(User user) throws VouchersException;

}
