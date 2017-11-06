/**
 *2017-10-16 10:28:16
 * 团购分类页面
 */
var saleType = document.getElementById('saleTypeId').innerHTML;
var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        groupList: [],
        indexPage: 1,
        pageSize: 30,
        /*总记录条数*/
        totalRecord: 0,
        saleTypeId: saleType,
        saleTypeName: ''
    },
    methods: {
        /*跳转到对应的商品详情页*/
        routerToGroup: function (groupId) {
            Ajx.get(routerUrl.get.toGroupInfoPage + "/" + groupId).then(function () {
                VRouter(routerUrl.router.groupInfo);
            }).catch(function () {
                VRouter(routerUrl.router.groupInfo);
            });

        },
        /*页面变化*/
        pageChange: function (page) {
            /*修改页码*/
            this.indexPage = page;
            getGroupList(this);
        }
    },
    mounted: function () {
        getGroupList(this);
    }
});

/*获取当前销售类型下的全部团购信息*/
function getGroupList(appThis) {
    /*如果页面上没有后台渲染的ID数据则将id设置为-1表示由服务端从Session中读取*/
    if (appThis.saleTypeId == '') {
        appThis.saleTypeId = -1;
    }
    Ajx.get(routerUrl.get.groupBySaleType + "/" + appThis.saleTypeId,
        {
            params: {
                page: appThis.indexPage,
                pageSize: appThis.pageSize,
                totalRecord: appThis.totalRecord
            }
        }
    ).then(function (response) {
        /*先清空数组在将新页面数据加入*/
        appThis.groupList = [];
        /*加载团购活动列表*/
        response.data.dataList.forEach(function (group) {
            appThis.groupList.push(group);
        });
        appThis.saleTypeName = response.data.title;
        appThis.totalRecord = response.data.totalRecord;
    }).catch(function (error) {
    });
}