/**
 *
 */
var temp;
var shopId = document.getElementById('shopId').innerHTML;
var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        shopInfo: '',
        goodsList: [],
        indexPage: 1,
        pageSize: 30,
        /*总记录条数*/
        totalRecord: 0,
        groupList: []
    },
    methods: {
        /*页面变化*/
        pageChange: function (page) {
            /*修改页码*/
            this.indexPage = page;
            getGroupList(this);
        },
        /*跳转到对应的商品详情页*/
        routerToGroup: function (groupId) {
            Ajx.get(routerUrl.get.toGroupInfoPage + "/" + groupId).then(function () {
                VRouter(routerUrl.router.groupInfo);
            }).catch(function () {
                VRouter(routerUrl.router.groupInfo);
            });

        }
    },
    mounted: function () {
        getShopInfo(this);
    }
});

/*获取店铺详细详细*/
function getShopInfo(appThis) {
    if (shopId == '') {
        shopId = -1;
    }
    Ajx.get(routerUrl.get.getShopInfo + "/" + shopId).then(function (response) {
        appThis.shopInfo = response.data;
        getGoodsByShop(appThis);
    });
}

/*获取当前销售类型下的全部团购信息*/
function getGoodsByShop(appThis) {
    Ajx.get(routerUrl.get.getGroupByShop,
        {
            params: {
                shopInfoId: appThis.shopInfo.id,
                page: appThis.indexPage,
                pageSize: appThis.pageSize,
            }
        }
    ).then(function (response) {
        /*先清空数组在将新页面数据加入*/
        appThis.groupList = [];
        /*加载团购活动列表*/
        response.data.dataList.forEach(function (group) {
            appThis.groupList.push(group);
        });
        appThis.totalRecord = response.data.totalRecord;
    }).catch(function (error) {
        appThis.groupList = [];
    });
}