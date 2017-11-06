package com.nchu.service;

import com.nchu.entity.ReceivingAddress;
import com.nchu.entity.User;
import com.nchu.exception.ReceivingAddressException;
import com.nchu.exception.UserServiceException;

/**
 * 2017年9月22日08:35:30
 * 收货地址相关业务接口
 */
public interface ReceivingAddressService {

    /**
     * TODO 保存收货地址
     *
     * @param receivingAddress 收货地址对象
     * @return 返回保存结果
     * @throws ReceivingAddressException
     */
    boolean saveReceivingAddress(ReceivingAddress receivingAddress) throws ReceivingAddressException;

    /**
     * TODO 更新收货地址
     *
     * @param receivingAddress 收货地址对象
     * @return 返回更新结果
     * @throws ReceivingAddressException
     */
    boolean updateReceivingAddress(ReceivingAddress receivingAddress) throws ReceivingAddressException;

    /**
     * TODO 删除收货地址
     *
     * @param receivingAddress 收货地址对象
     * @return 返回删除结果
     * @throws ReceivingAddressException
     */
    boolean deleteReceivingAddress(Long Id) throws ReceivingAddressException;

    /**
     * TODO 获取收货地址
     *
     * @param id 收货地址id
     * @return 返回地址对象
     */
    ReceivingAddress getReceivingAddress(Long id);
}
