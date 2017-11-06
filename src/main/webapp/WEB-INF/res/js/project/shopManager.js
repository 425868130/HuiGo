/**
 *会员账号操作
 */
var temp;
var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        /*待审核列表*/
        ShopList: [],
        user: '',
        isLogin: false,
        page: 1,
        pageSize: 10,
        /*总记录条数*/
        totalRecord: 0,
        buttonTxt: '冻结',
        tabName: 'tb1',
        /*表格列定义*/
        tableColumns: [
            {
                title: '店铺名称',
                key: 'name'
            }, {
                title: '所有者',
                key: 'owerName'
            },
            {
                title: '销售类型',
                key: 'saleTypeName'
            },
            {
                title: '许可证号',
                key: 'licenseCode'
            },
            {
                title: '开业时间',
                key: 'gmtCreate'
            },
            {
                title: '操作',
                key: 'action',
                width: 150,
                align: 'center',
                render: function (h, params) {
                    return h('div', [
                        h('Button', {
                                props: {
                                    type: 'primary',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: function () {
                                        app.changeLock(params.index)
                                    }
                                }
                            },
                            app.buttonTxt
                        )
                    ]);
                }
            }
        ]
    },
    methods: {
        /*页面变化*/
        pageChange: function (page) {
            /*修改页码*/
            this.page = page;
            loadShopList(this, this.tabName == 'tb1' ? false : true);
        },
        changeLock: function (index) {
            var id = this.ShopList[index].id
            /*如果在第一页则执行冻结操作否则执行解冻操作*/
            var result = this.tabName == 'tb1' ? true : false;
            postResult(this, result, id);
        }

    },
    watch: {
        /*监听tab切换*/
        tabName: function (tab) {
            if (tab == 'tb1') {
                this.buttonTxt = '冻结';
                loadShopList(this, false);
            } else {
                this.buttonTxt = '解冻';
                loadShopList(this, true);
            }
        }
    },
    mounted: function () {
        getLoginUserInfo(this);
    }
});

function loadShopList(appThis, status) {
    Ajx.get(routerUrl.get.getShopListByLock, {
        params: {
            status: status,
            page: appThis.page,
            pageSize: appThis.pageSize
        }
    }).then(function (response) {
        appThis.totalRecord = response.data.totalRecord;
        appThis.ShopList = [];
        response.data.dataList.forEach(function (data) {
            appThis.ShopList.push(data);
        })
    })
}

/*发送操作结果*/
function postResult(appThis, result, id) {
    Ajx.get(routerUrl.get.shopLock, {
        params: {
            isLock: result,
            userId: id
        }
    }).then(function () {
        if (result) {
            alert('已将该店铺封禁')
        } else {
            alert('已将该店铺解封')
        }
        loadShopList(appThis, appThis.tabName == 'tb1' ? false : true)
    })
}

function getLoginUserInfo(appThis) {
    Ajx.get(routerUrl.get.userInfo).then(function (response) {
        if (response.data != undefined) {
            appThis.user = response.data;
            appThis.isLogin = true;
            loadShopList(appThis, false);
        }
    }).catch(function (error) {
        alert('请先登录!')
        window.location.href = routerUrl.router.admin.welcome;
    });
}