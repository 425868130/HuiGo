package com.nchu.service;

import com.nchu.entity.Favorites;
import com.nchu.entity.User;
import com.nchu.exception.FavoritesException;

import java.util.List;

/**
 * 2017-9-23 18:17:48
 * 收藏夹相关业务接口
 */
public interface FavoriteService {
    /**
     * TODO 创建收藏夹
     *
     * @param favorites 添加商品收藏夹对象
     * @param user      操作者对象
     * @return 操作结果
     * @throws FavoritesException
     */
    boolean addFavorite(Favorites favorites, User user) throws FavoritesException;

    /**
     * TODO 删除收藏夹
     *
     * @param favorites 删除商品收藏夹对象
     * @param user      操作者对象
     * @return 操作结果
     * @throws FavoritesException
     */
    boolean deleteFavorite(Favorites favorites, User user) throws FavoritesException;

    /**
     * TODO 删除所有收藏夹
     *
     * @param favorites 删除商品收藏夹对象
     * @return 操作结果
     * @throws FavoritesException
     */
    boolean deleteAllFavorites(List<Favorites> favorites, User user) throws FavoritesException;

    /**
     * TODO 分页查询用户的所有收藏夹
     *
     * @param user     操作者对象
     * @param page     页面数
     * @param pageSize 页面大小
     * @return 操作结果
     */
    List<Favorites> getAllFavorite(User user, int page, int pageSize);

    Favorites getFavoritesById(Long favoritesId);
}
