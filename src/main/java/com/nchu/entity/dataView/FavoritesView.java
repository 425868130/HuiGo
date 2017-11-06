package com.nchu.entity.dataView;

import java.util.ArrayList;
import java.util.List;

import com.nchu.entity.Evaluation;
import com.nchu.entity.Favorites;

public class FavoritesView {
    /*收藏编号*/
    private Long id;
    /*收藏商品编号*/
    private Long goodId;
    /*商品名称*/
    private String goodName;
    /*商品图片*/
    private String picture;
    /*商店名称*/
    private String shopName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public static List<FavoritesView> getFavoritesView(List<Favorites> favorites) {
        List<FavoritesView> favoritesViews = new ArrayList<FavoritesView>();
        for (int i = 0; i < favorites.size(); i++) {
            FavoritesView favoritesView = new FavoritesView();
            favoritesView.setId(favorites.get(i).getId());
            favoritesView.setGoodId(favorites.get(i).getGoods().getId());
            favoritesView.setGoodName(favorites.get(i).getGoods().getName());
            favoritesView.setShopName(favorites.get(i).getGoods().getShop().getName());
            favoritesView.setPicture(favorites.get(i).getGoods().getPicture());
            favoritesViews.add(favoritesView);
        }
        return favoritesViews;
    }
}
