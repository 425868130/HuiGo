package com.nchu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nchu.dao.ReceiverAddressDao;
import com.nchu.entity.ReceivingAddress;
import com.nchu.exception.ReceivingAddressException;
import com.nchu.service.ReceivingAddressService;


/**
 * 2017-9-24 15:46:13
 * 收货地址相关业务接口实现类
 */
@Service
public class ReceivingAddressServiceImpl implements ReceivingAddressService {

    @Autowired
    ReceiverAddressDao receivingAddressDao;

    /**
     * TODO 保存收货地址
     *
     * @param receivingAddress 收货地址对象
     * @return 返回保存结果
     * @throws ReceivingAddressException
     */
    @Override
    public boolean saveReceivingAddress(ReceivingAddress receivingAddress) throws ReceivingAddressException {
        try {
            receivingAddressDao.save(receivingAddress);
            return true;
        } catch (Exception e) {
            throw new ReceivingAddressException("保存收货地址错误");
        }
    }

    /**
     * TODO 更新收货地址
     *
     * @param receivingAddress 收货地址对象
     * @return 返回更新结果
     * @throws ReceivingAddressException
     */
    @Override
    public boolean updateReceivingAddress(ReceivingAddress receivingAddress) throws ReceivingAddressException {
        try {
            receivingAddressDao.update(receivingAddress);
            return true;
        } catch (Exception e) {
            throw new ReceivingAddressException("更新收货地址错误");
        }
    }

    /**
     * TODO 删除收货地址
     *
     * @param receivingAddress 收货地址对象
     * @return 返回删除结果
     * @throws ReceivingAddressException
     */
    @Override
    public boolean deleteReceivingAddress(Long Id) throws ReceivingAddressException {
        try {
            receivingAddressDao.delete(Id);
            return true;
        } catch (Exception e) {
            throw new ReceivingAddressException("删除收货地址错误");
        }
    }

    /**
     * TODO 获取收货地址
     *
     * @param id 收货地址对象id
     * @return 返回收货地址对象
     */
    @Override
    public ReceivingAddress getReceivingAddress(Long id) {
        return receivingAddressDao.get(id);
    }
}
