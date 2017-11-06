/**
 * 网站首页
 */
var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        searchVal: '',
        isLogin: false,
        user: {},
        hotGroupList: [],
        saleTypeList: [],
        groupList: [],
        shopList: [],
        indexPage: 1,
        pageSize: 50,
        /*总记录条数*/
        totalRecord: 0,
        /*总页数*/
        pageCount: 1,
        visiable: false,
        Announce: ''
    },
    methods: {
        search: function () {
            Ajx.get(routerUrl.get.searchResult + "/" + this.searchVal);
            VRouter(routerUrl.router.searchResult);
        },
        gotoUrl: function (url) {
            window.location.href = url;
        },
        /*注销*/
        logout: function () {
            Ajx.get(routerUrl.get.logout);
            /*返回登录页面*/
            VRouter(routerUrl.router.login);
        },
        /*跳转到对应的商品详情页*/
        routerToGroup: function (groupId) {
            Ajx.get(routerUrl.get.toGroupInfoPage + "/" + groupId).then(function () {
                VRouter(routerUrl.router.groupInfo);
            }).catch(function () {
                VRouter(routerUrl.router.groupInfo);
            });
        },
        /*跳转到商家详情页*/
        goToShopInfo: function (shopid) {
            Ajx.get(routerUrl.get.saveParams, {
                params: {
                    paramsName: 'shopInfoId',
                    value: shopid
                }
            }).then(function () {
                VRouter(routerUrl.router.shopInfo);
            });
        },
        /*点击团购分类项目,name为团购分类id*/
        saleTypeClick: function (name) {
            Ajx.get(routerUrl.get.saveSaleTypeParam, {
                params: {
                    saleTypeId: name
                }
            }).then(function (response) {
                VRouter(routerUrl.router.saleTypePage);
            });
        },
        /*页面变化*/
        pageChange: function (page) {
            /*修改页码*/
            this.indexPage = page;
            getIndexGroup(this);
        }
    },
    /*组件创建时的回调函数,在此完成数据的异步加载*/
    created: function () {
        /*并发请求*/
        axios.all([
            getLoginUser(),
            hotGroup(),
            getSaleType(),
            getIndexGroup(this),
            getShopList(),
            getAnnounce()
        ]);
    },
    mounted: function () {
    }
});

/*获取登录的用户信息*/
function getLoginUser() {
    /*先获取Session的用户信息,不存在表示用户未登录*/
    return Ajx.get(routerUrl.get.userInfo).then(function (response) {
        if (response.data != undefined) {
            app.user = response.data;
            app.isLogin = true;
        }
    }).catch(function (error) {
        app.isLogin = false;
    });
}

/*获取用于滚动播放的热门团购*/
function hotGroup() {
    return Ajx.get(routerUrl.get.indexHotGroup).then(function (response) {
        response.data.forEach(function (group) {
            app.hotGroupList.push(group);
        });
    }).catch(function () {
    });
}

/*获取商品分类*/
function getSaleType() {
    return Ajx.get(routerUrl.get.saleTypeList).then(function (response) {
        response.data.forEach(function (type) {
            app.saleTypeList.push(type);
        })
    }).catch(function () {
        this.saleTypeList = [{id: '', name: ''}];
    });
}

/*获取团购列表*/
function getIndexGroup(appThis) {
    return Ajx.get(routerUrl.get.indexGroupList, {
        params: {
            page: appThis.indexPage,
            pageSize: appThis.pageSize,
            totalRecord: appThis.totalRecord
        }
    }).then(function (response) {
        /*先清空数组在将新页面数据加入*/
        appThis.groupList = [];
        response.data.dataList.forEach(function (group) {
            app.groupList.push(group);
        });
        appThis.totalRecord = response.data.totalRecord;
    });
}

/*获取总页码*/
/*function groupListPageAccount() {
    return Ajx.get(routerUrl.get.groupListPageAccount, {
        params: {
            pageSize: 50
        }
    }).then(function (response) {
        app.pageCount = response.data;
    })
}*/

/*获取推荐商家*/
function getShopList() {
    return Ajx.get(routerUrl.get.shopList, {
        params: {
            number: 4
        }
    }).then(function (response) {
        response.data.forEach(function (shop) {
            app.shopList.push(shop);
            /*防止部分组件提前加载造成页面闪烁*/
            app.visiable = true;
        })
    }).catch(function () {
        app.visiable = true;
    });
}

function getAnnounce() {
    Ajx.get(routerUrl.get.getAnnounce).then(function (response) {
        app.Announce = response.data
    })
}