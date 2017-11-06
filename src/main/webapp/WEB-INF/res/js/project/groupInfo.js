/**
 * 团购详情页面js文件
 */
var temp;
var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        groupInfo: '',
        imgList: [],
        goods: '',
        shop: '',
        express: '',
        serverTime: '',
        evaluates: [],
        userLogin: false,
        user: ''
    },
    mounted: function () {
        getGroupInfo(this);
        /*先立即执行一次获取服务器时间的方法,之后每隔5秒更新一次时间*/
        getServerTime();
        window.setInterval(getServerTime, 5000);
        /*商品数据获取成功后在异步获取商品 评价*/
    },
    computed: {
        groupId: function () {
            return this.$refs.groupId.innerHTML;
        },
        /*将时间毫秒转换为年月日格式*/
        startTime: function () {
            return getLocalTime(this.groupInfo.startTime);
        },
        endTime: function () {
            return getLocalTime(this.groupInfo.endTime);
        },
        groupStatus: function () {
            return {
                /*如果开团时间大于服务器时间则团购未开始*/
                start: this.groupInfo.startTime > this.serverTime ? false : true,
                invalid: this.serverTime > this.groupInfo.endTime ? false : true
            };
        },
        /*根据团购状态显示按钮文本*/
        joinButtonText: function () {
            if (this.groupStatus.invalid) {
                return '团购失效'
            }
            /*如果团购已经开始,则显示立即参团*/
            if (this.groupStatus.start) {
                return '立即参团'
            } else {
                return '开团提醒'
            }
        },
        joinButtonStyle: function () {
            if (this.groupStatus.invalid) {
                return 'group-invalid'
            }
            /*如果团购已经开始,则显示立即参团*/
            if (this.groupStatus.start) {
                return 'group-start'
            } else {
                return 'group-unStart'
            }
        }
    },
    methods: {
        getUser: function (data) {
            if (data == undefined || data == null) {
                this.userLogin = false;
                alert('unlogin');
                return;
            }
            this.user = data;
            this.userLogin = true;
        },
        /*添加到收藏夹*/
        addFavorite: function () {
            Ajx.get(routerUrl.get.addToFavorites, {
                params: {
                    goodsId: app.goods.id
                }
            }).then(function (response) {
                if (response.data) {
                    app.$Notice.success({
                        title: '已加入我的收藏夹!'
                    });
                }
            }).catch(function (error) {
                app.$Modal.error({
                    title: '添加失败',
                    content: error.response.data.message
                });
            })

        },
        shareGroup: function () {
            //window.clipboardData.setData("groupUrl", window.location.href + "/" + this.groupId);
            this.$Modal.success({
                title: '链接已复制',
                content: '团购地址已经复制到剪切板,分享时直接粘贴即可哦'
            });
        },
        dateUtil: function (date) {
            return getLocalTime(date);
        },
        /*参团按钮点击事件*/
        joinBtHandler: function () {
            if (!this.userLogin) {
                app.$Modal.error({
                    title: '参团失败',
                    content: '用户未登录,请登录后操作!'
                });
                return;
            }
            if (this.groupStatus.invalid) {
                this.$Modal.error({
                    title: '团购无效',
                    content: '该团购已过期或无效,看看其他团购吧<(￣3￣)> '
                });
            }
            if (this.groupStatus.start) {
                /*切换到订单支付页面*/
                Ajx.get(routerUrl.get.savePayGroup, {
                    params: {groupId: app.groupInfo.id}
                }).then(function (response) {
                    VRouter(routerUrl.router.paymentPage);
                }).catch(function (error) {
                    app.$Modal.error({
                        title: '参团失败',
                        content: error.response.data.message
                    });
                });
            } else {
                /*TODO 开团提醒逻辑*/
            }
        }
    }
});

/*获取团购信息详情*/
function getGroupInfo(appThis) {
    var paramData;
    /*如果页面有后台渲染的groupId则使用此id,否则让服务器从Session中获取*/
    if (appThis.$refs.groupId.innerHTML != '') {
        paramData = appThis.$refs.groupId.innerHTML;
    } else {
        paramData = -1;
    }
    Ajx.get(routerUrl.get.groupInfo, {
        params: {
            groupId: paramData
        }
    }).then(function (response) {
        app.groupInfo = response.data;
        app.express = app.groupInfo.expressDelivery;
        app.goods = app.groupInfo.goods;
        app.goods.goodsPictures.forEach(function (pic) {
            app.imgList.push(pic);
        });
        /*获取商品信息成功后再通过商品信息获取评论列表和店铺信息*/
        axios.all([
            getEvaluate(),
            getShopInfo()]);
    });
}

/*获取服务器时间*/
function getServerTime() {
    Ajx.get(routerUrl.get.serverTime).then(function (response) {
        app.serverTime = response.data;
    });
}

/*获取商品评价*/
function getEvaluate() {
    Ajx.get(routerUrl.get.goodsEvaluate, {
        params: {
            goodsId: app.goods.id
        }
    }).then(function (response) {
        response.data.forEach(function (evaluate) {
            app.evaluates.push(evaluate);
        });
    });
}

/*获取店铺信息*/
function getShopInfo() {
    Ajx.get(routerUrl.get.getShopByGoods + "/" + app.goods.id).then(function (response) {
        app.shop = response.data;
    });
}