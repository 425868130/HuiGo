/**
 *商家审核
 */
var temp;
var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        /*待审核列表*/
        businessCheck: [],
        user: '',
        isLogin: false,
        page: 1,
        pageSize: 10,
        /*总记录条数*/
        totalRecord: 0,
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
                title: '申请时间',
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
                                        app.pass(params.index)
                                    }
                                }
                            },
                            '通过'
                        ),
                        h('Button', {
                                props: {
                                    type: 'error',
                                    size: 'small'
                                },
                                on: {
                                    click: function () {
                                        app.refuse(params.index)
                                    }
                                }
                            },
                            '拒绝'
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
            loadBusiness(this);
        },
        /*通过审核*/
        pass: function (index) {
            var id = this.businessCheck[index].id
            postResult(this, true, id);
        },
        /*拒绝审核*/
        refuse: function (index) {
            var id = this.businessCheck[index].id
            postResult(this, false, id);
        }

    },
    mounted: function () {
        getLoginUserInfo(this);
    }
});

/*加载待审核的店铺列表*/
function loadBusiness(appThis) {
    Ajx.get(routerUrl.get.getUncheckShop, {
        params: {
            page: appThis.page,
            pageSize: appThis.pageSize
        }
    }).then(function (response) {
        appThis.totalRecord = response.data.totalRecord;
        appThis.businessCheck = [];
        response.data.dataList.forEach(function (data) {
            appThis.businessCheck.push(data);
        })
    })
}

function postResult(appThis, result, id) {
    Ajx.get(routerUrl.get.shopCheck, {
        params: {
            isPass: result,
            shopId: id
        }
    }).then(function () {
        if (result) {
            appThis.$Modal.success({
                title: '审核通过',
                content: '该店铺已通过审核'
            });
        } else {
            appThis.$Modal.error({
                title: '结果已提交',
                content: '该店铺未通过审核'
            });
        }
        loadBusiness(appThis)
    })
}

function getLoginUserInfo(appThis) {
    Ajx.get(routerUrl.get.userInfo).then(function (response) {
        if (response.data != undefined) {
            appThis.user = response.data;
            appThis.isLogin = true;
            loadBusiness(appThis);
        }
    }).catch(function (error) {
        alert('请先登录!')
        window.location.href = routerUrl.router.admin.welcome;
    });
}