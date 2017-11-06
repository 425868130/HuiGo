package com.nchu.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nchu.dao.FavoritesDao;
import com.nchu.dao.UserDao;
import com.nchu.entity.Favorites;
import com.nchu.entity.User;
import com.nchu.exception.FavoritesException;
import com.nchu.service.FavoriteService;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    UserDao userDao;
    @Autowired
    FavoritesDao favoritesDao;

    /**
     * TODO 创建收藏夹
     *
     * @param favorites 添加商品收藏夹对象
     * @param user      操作者对象
     * @return 操作结果
     */
    @Override
    public boolean addFavorite(Favorites favorites, User user) throws FavoritesException {
        user = userDao.get(user.getId());
        /*判断商品是否已经添加到收藏夹*/
        if (user.getFavorites().stream().filter(favor -> favor.getGoods().getId() == favorites.getGoods().getId()).count() > 0) {
            throw new FavoritesException("该商品已经添加到收藏夹,请勿重复操作");
        }
        favorites.setUser(user);
        try {
            favoritesDao.save(favorites);
            return true;
        } catch (Exception e) {
            throw new FavoritesException("添加收藏夹异常");
        }
    }

    /**
     * TODO 删除收藏夹
     *
     * @param favorites 删除商品收藏夹对象
     * @param user      操作者对象
     * @return 操作结果
     */
    @Override
    public boolean deleteFavorite(Favorites favorites, User user) throws FavoritesException {
        if (user.getId() == userDao.get(user.getId()).getId()) {
            try {
                favoritesDao.deleteObject(favorites);
                return true;
            } catch (Exception e) {
                throw new FavoritesException("删除收藏夹异常");
            }
        } else
            throw new FavoritesException("删除收藏夹非法操作者");
    }

    /**
     * TODO 删除所有收藏夹
     *
     * @param favorites 删除商品收藏夹列表对象
     * @param user      操作者对象
     * @return 操作结果
     */
    @Override
    public boolean deleteAllFavorites(List<Favorites> favorites, User user) throws FavoritesException {
        if (user.getId() == userDao.get(user.getId()).getId()) {
            try {
                favoritesDao.deleteAll(favorites);
                return true;
            } catch (Exception e) {
                throw new FavoritesException("删除所有收藏夹异常");
            }
        }
        throw new FavoritesException("删除所有收藏夹非法操作者");
    }

    /**
     * TODO 分页查询用户的所有收藏夹
     *
     * @param user     操作者对象
     * @param page     页面数
     * @param pageSize 页面大小
     * @return 操作结果
     */
    @Override
    public List<Favorites> getAllFavorite(User user, int page, int pageSize) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("userid", user.getId());
        return favoritesDao.searchPage(conditions, page, pageSize);
    }

    /**
     * TODO 查询用户的收藏夹
     *
     * @return 操作结果
     */
    @Override
    public Favorites getFavoritesById(Long favoritesId) {
        return favoritesDao.get(favoritesId);
    }
}
