/**
 *会员账号操作
 */
var temp;
var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        /*待审核列表*/
        UserList: [],
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
                title: '会员账号',
                key: 'account'
            }, {
                title: '昵称',
                key: 'nickName'
            },
            {
                title: '邮箱',
                key: 'email'
            },
            {
                title: '联系方式',
                key: 'telephone'
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
            loadUserList(this, this.tabName == 'tb1' ? false : true);
        },
        /*通过审核*/
        changeLock: function (index) {
            var id = this.UserList[index].id
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
                loadUserList(this, false);
            } else {
                this.buttonTxt = '解冻';
                loadUserList(this, true);
            }
        }
    },
    mounted: function () {
        getLoginUserInfo(this);
    }
});

/*加载商家列表*/
function loadUserList(appThis, status) {
    Ajx.get(routerUrl.get.getBusiUserList, {
        params: {
            status: status,
            page: appThis.page,
            pageSize: appThis.pageSize,
            userType: 'CUSTOMER'
        }
    }).then(function (response) {
        appThis.totalRecord = response.data.totalRecord;
        appThis.UserList = [];
        response.data.dataList.forEach(function (data) {
            appThis.UserList.push(data);
        })
    })
}

/*发送操作结果*/
function postResult(appThis, result, id) {
    Ajx.get(routerUrl.get.userLock, {
        params: {
            isLock: result,
            userId: id
        }
    }).then(function () {
        if (result) {
            alert('该会员账号已冻结')
        } else {
            alert('该会员账号已解冻')
        }
        loadUserList(appThis, appThis.tabName == 'tb1' ? false : true)
    })
}

function getLoginUserInfo(appThis) {
    Ajx.get(routerUrl.get.userInfo).then(function (response) {
        if (response.data != undefined) {
            appThis.user = response.data;
            appThis.isLogin = true;
            loadUserList(appThis, false);
        }
    }).catch(function (error) {
        alert('请先登录!')
        window.location.href = routerUrl.router.admin.welcome;
    });
}