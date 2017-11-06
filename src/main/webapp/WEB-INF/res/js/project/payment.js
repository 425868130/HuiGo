/**
 *订单支付页面js
 */
var paymentGroupId = document.getElementById('paymentGroup').innerHTML;

var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        paymentId: paymentGroupId,
        groupInfo: '',
        goods: '',
        shop: '',
        express: '',
        /*收货地址列表*/
        receivingAddress: [],
        /*支付方式列表*/
        paymentMethods: [],
        paymentSelected: '',
        userLogin: false,
        user: '',
        addressId: '',
        /*留言*/
        remark: '',
        orderId: '',
        loading: false,
        /*是否显示支付界面*/
        paymentShow: false,
        password: '',
        paymentTips: ''
    },
    methods: {
        /*获取header组件通过事件传递出来的user对象*/
        getUser: function (data) {
            if (data == undefined || data == null) {
                this.userLogin = false;
                VRouter(routerUrl.url.router.login);
                return;
            }
            this.user = data;
            this.userLogin = true;
        },
        /*提交订单*/
        submitOrder: function () {
            if (this.addressId == '') {
                this.$Modal.error({
                    title: '地址不能为空',
                    content: '请选择收货地址!'
                })
                return
            }
            /*构建订单数据*/
            var orderData = {
                goods: this.goods,
                expressDelivery: this.express,
                receivingAddress: {
                    id: this.addressId
                },
                remark: this.remark
            };
            Ajx.post(routerUrl.post.createOrder, orderData).then(function (response) {
                app.orderId = response.data;
                alert('参团成功!');
                app.paymentShow = true;
            }).catch(function (error) {
                app.$Modal.error({
                    title: '订单创建失败',
                    content: error.response.data.message
                });
            })

        },
        /*订单支付逻辑*/
        paymentHandler: function () {
            this.loading = true;
            if (app.password == '') {
                app.paymentTips = '请输入密码'
            }
            Ajx.post(routerUrl.post.orderPayment, {
                orderId: app.orderId,
                paymentMethod: app.paymentSelected,
                password: app.password
            }).then(function (response) {
                app.loading = false;
                alert('恭喜,支付成功');
                VRouter(routerUrl.router.groupInfo);
            }).catch(function (error) {
                app.paymentTips = error.response.data.message
                app.loading = false;
            })
        },
        cancelPayment: function () {
            this.$Message.error({
                content: '支付已取消,请一个小时内在我的团购中完成支付!',
                duration: 5
            });
            VRouter(routerUrl.router.groupInfo);
        }
    },
    computed: {
        /*订单总价*/
        totalPrice: function () {
            return this.goods.preferentialprice + this.express.price;
        }
    },
    mounted: function () {
        getGroupInfo(this);
        getPaymentMethods();
    }

});

/*获取团购信息详情*/
function getGroupInfo(appThis) {
    var paramData;
    /*如果页面有后台渲染的groupId则使用此id,否则让服务器从Session中获取*/
    if (appThis.paymentId != '') {
        paramData = appThis.paymentId;
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
        /*获取商品信息成功后再通过商品信息获取评论列表和店铺信息*/
        axios.all([
            getShopInfo(),
            getReceivingAddress()
        ]);
    });
}

/*获取店铺信息*/
function getShopInfo() {
    Ajx.get(routerUrl.get.getShopByGoods + "/" + app.goods.id).then(function (response) {
        app.shop = response.data;
    });
}

/*获取用户收货地址*/
function getReceivingAddress() {
    Ajx.get(routerUrl.get.receivingAddress).then(function (response) {
        response.data.forEach(function (address) {
            app.receivingAddress.push(address);
        })
    });
}

/*获取支付方式*/
function getPaymentMethods() {
    Ajx.get(routerUrl.get.getPaymentMethods).then(function (response) {
        response.data.forEach(function (pay) {
            app.paymentMethods.push(pay);
        });
    });
}