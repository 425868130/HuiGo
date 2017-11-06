package com.nchu.service.impl;

import com.nchu.dao.AnnouncementDao;
import com.nchu.entity.Announcement;
import com.nchu.entity.User;
import com.nchu.enumdef.UserRoleType;
import com.nchu.exception.AnnouncementException;
import com.nchu.service.AnnounceService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2017-9-24 15:28:16
 * 系统公告相关业务接口实现类
 */
@Service
public class AnnounceServiceImpl implements AnnounceService {

    @Autowired
    AnnouncementDao ad;

    /**
     * TODO 发布公告
     * 验证操作人身份为管理员
     *
     * @param announcement 公告实体
     * @param operator     操作
     * @return 操作结果
     */
    @Override
    public boolean addAnnounceMent(Announcement announcement, User operator) throws AnnouncementException {
        if (Integer.valueOf(operator.getRole()) == UserRoleType.ADMIN.getIndex()) {
            try {
                ad.save(announcement);
                return true;
            } catch (Exception e) {
                throw new AnnouncementException("公告发布异常");
            }
        } else
            throw new AnnouncementException("公告发布非法操作者");
    }

    /**
     * TODO 获取全部公告
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @return 公告列表
     */
    @Override
    public List<Announcement> listAll(int page, int pageSize) {
        return ad.searchPage(null, page, pageSize);
    }

    @Override
    public List<Announcement> listAll() {
        return ad.listAll();
    }

    /**
     * TODO 删除公告
     * 验证操作人为管理员
     *
     * @param announcement 要删除的公告
     * @param operator     操作人
     * @return 操作结果
     */
    @Override
    public boolean deleteAnnouncement(Announcement announcement, User operator) throws AnnouncementException {
        if (Integer.valueOf(operator.getRole()) == UserRoleType.ADMIN.getIndex()) {
            try {
                ad.deleteObject(announcement);
                return true;
            } catch (Exception e) {
                throw new AnnouncementException("删除公告异常");
            }
        } else
            throw new AnnouncementException("删除公告非法操作者");
    }

    @Override
    public void addAnnounceMent(Announcement announcement) {
        ad.save(announcement);
    }
}
