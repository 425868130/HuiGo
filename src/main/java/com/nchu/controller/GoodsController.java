package com.nchu.controller;

import com.nchu.entity.Favorites;
import com.nchu.entity.Goods;
import com.nchu.entity.User;
import com.nchu.entity.dataView.EvaluationDataView;
import com.nchu.entity.dataView.KeyWords;
import com.nchu.exception.FavoritesException;
import com.nchu.exception.GoodsException;
import com.nchu.exception.ShopException;
import com.nchu.exception.UserSessionException;
import com.nchu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 商品相关控制器
 *
 * @author 42586
 */
@CrossOrigin
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    UserSessionService sessionService;
    @Autowired
    EvaluationService evaluationService;
    @Autowired
    FavoriteService favoriteService;
    @Autowired
    ShopService shopService;
    @Autowired
    UserService userService;


    /*获取商品详*/
    @RequestMapping(value = "/goodsInfo/{goodsId}", method = RequestMethod.GET)
    public Goods getGoodsInfo(@PathVariable("goodsId") Long goodsid, HttpServletRequest request) {
        if (goodsid != null) {
            return goodsService.getById(goodsid);
        }
        Long goodId = (Long) sessionService.getAttr(request, "goodsId");
        return goodsService.getById(goodId);
    }

    /*获取商品评论*/
    @RequestMapping(value = "/evaluate", method = RequestMethod.GET)
    public List<EvaluationDataView> getGoodsEvaluation(@RequestParam("goodsId") Long goodsId) {
        Goods goods = new Goods();
        goods.setId(goodsId);
        return EvaluationDataView.transFrom(evaluationService.listGoodsEvaluation(goods, 1, -1));
    }

    /*添加到收藏夹*/
    @RequestMapping(value = "/addToFavorites", method = RequestMethod.GET)
    public Boolean addToFavorites(@RequestParam("goodsId") Long goodsId, HttpServletRequest request) throws UserSessionException, FavoritesException {
        User user = sessionService.getUser(request);
        Favorites favorites = new Favorites();
        Goods goods = new Goods();
        goods.setId(goodsId);
        favorites.setGoods(goods);
        favoriteService.addFavorite(favorites, user);
        return true;
    }

    /*获取查询结果*/
    @RequestMapping(value = "/getSearchResult")
    public List<Goods> getSearchResult(@RequestParam("goodsKeyWords") String goodsKeyWords,
                                       @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, HttpServletRequest request) {
        String keyWords;
        /*如果请求参数中关键词为空则尝试从Session中读取*/
        if (!goodsKeyWords.isEmpty()) {
            keyWords = goodsKeyWords;
        } else {
            keyWords = (String) sessionService.getAttr(request, "goodsKeyWords");
        }
        return goodsService.searchGoods(keyWords, page, pageSize);
    }

    /*获取当前查询关键词*/
    @RequestMapping(value = "/getKeyWords", method = RequestMethod.GET)
    public KeyWords getKeyWords(HttpServletRequest request) {
        return new KeyWords((String) sessionService.getAttr(request, "goodsKeyWords"));
    }

    /*获取当前登录的商家的全部商品*/
    @RequestMapping(value = "/getGoodsList", method = RequestMethod.GET)
    public Set<Goods> getGoodsList(HttpServletRequest request) throws UserSessionException, ShopException {
        User user = sessionService.getUser(request);
        //return shopService.getById(user.getId()).getGoods();
        return shopService.getByOwner(user.getId()).getGoods();
    }

    @RequestMapping(value = "/addGoods", method = RequestMethod.POST)
    public boolean addGoods(@RequestBody Goods goods, HttpServletRequest request) throws GoodsException, ShopException, UserSessionException {
        User user = userService.getUserById(sessionService.getUser(request).getId());
        goods.setShop(shopService.getByOwner(user.getId()));
        goodsService.addGoods(goods, user);
        return true;
    }

    @RequestMapping(value = "/goodInfo/{goodsId}", method = RequestMethod.GET)
    public Goods getGoodInfo(@PathVariable("goodsId") Long goodsId) {
        System.out.println(goodsId);
        return goodsService.getById(goodsId);
    }

    @RequestMapping(value = "/delGoods/{goodsId}", method = RequestMethod.GET)
    public String delGoods(@PathVariable("goodsId") Long goodsId, HttpServletRequest request) throws UserSessionException, GoodsException {
        User user = sessionService.getUser(request);
        Goods goods = goodsService.getById(goodsId);
        goodsService.deleteGoods(goods, user);
        return "user/businessGoodsInfo";
    }

    @RequestMapping(value = "/upGoods", method = RequestMethod.POST)
    public boolean upGoods(@RequestBody Goods goods) {
        Goods olderGoods = goodsService.getById(goods.getId());
        olderGoods.setIsonshelves(goods.isIsonshelves());
        olderGoods.setSurplusInventory(goods.getSurplusInventory());
        olderGoods.setDescription(goods.getDescription());
        olderGoods.setCategory(goods.getCategory());
        olderGoods.setName(goods.getName());
        olderGoods.setPicture(goods.getPicture());
        olderGoods.setOriginalprice(goods.getOriginalprice());
        olderGoods.setTotalInventory(goods.getTotalInventory());
        olderGoods.setPreferentialprice(goods.getPreferentialprice());
        return goodsService.updateInventory(olderGoods);
    }
}
