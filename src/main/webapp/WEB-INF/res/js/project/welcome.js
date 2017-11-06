/**
 *
 */
var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        user: '',
        isLogin: false,
        formInline: {
            account: '',
            password: ''
        },
        ruleInline: {
            account: [
                {required: true, message: '请填写用户名', trigger: 'blur'}
            ],
            password: [
                {required: true, message: '请填写密码', trigger: 'blur'},
                {type: 'string', min: 6, message: '密码长度不能小于6位', trigger: 'blur'}
            ]
        }
    },
    methods: {
        handleSubmit: function (name) {
            /*校验用户名和密码*/
            this.$refs[name].validate(function (valid) {
                if (valid) {
                    Ajx.post(routerUrl.post.AdminLogin, {
                            account: app.formInline.account,
                            password: app.formInline.password
                        }
                    ).then(function (response) {
                        if (response.data == true) {
                            /*登录成功跳转首页*/
                            app.isLogin = true;
                        }
                    }).catch(function (error) {
                        app.$Modal.error({
                            title: '登录失败',
                            content: error.response.data.message
                        });
                    })
                } else {
                    app.$Modal.error({
                        title: '登录失败',
                        content: '账号或密码有误,请重新输入!'
                    });
                }
            })
        }//end Function(name)
    },
    mounted: function () {
        getLoginUserInfo(this);
    }
});

function getLoginUserInfo(appThis) {
    Ajx.get(routerUrl.get.userInfo).then(function (response) {
        if (response.data != undefined) {
            appThis.user = response.data;
            appThis.isLogin = true;
        }
    }).catch(function (error) {
        appThis.isLogin = false;
    });
}