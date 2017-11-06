/**
 * 顶部搜索栏组价
 */

Vue.component('my-header', {
    props: ['keyWords'],
    template: '<div id="my-header">\n' +
    '    <row class="topMenu">\n' +
    '        <i-col :lg="{span:3,offset:3}" :xs="{span:10,offset:1}" v-if="!isLogin" key="Login">\n' +
    '            <p>您好,请先&nbsp;<a :href="url.router.login" style="color: red">登录</a>&nbsp;&nbsp;&nbsp;\n' +
    '                <a :href="url.router.vipReg">免费注册</a></p></i-col>\n' +
    '        <i-col :lg="{span:3,offset:3}" :xs="{span:10,offset:1}" v-if="isLogin" key="unLogin">\n' +
    '            <p>欢迎,&nbsp;<a :href="url.router.userZone" style="color: red">{{user.nickName}}</a>&nbsp;&nbsp;&nbsp;\n' +
    '                <a @click="logout">注销</a></p></i-col>\n' +
    '        <i-col :lg="{span:4,offset:10}" :xs="{offset:10}" style="max-width: 60%">\n' +
    '            <a>我的团购</a>&nbsp;&nbsp;\n' +
    '            <a>收藏夹</a>&nbsp;&nbsp;\n' +
    '            <a>优惠券</a>&nbsp;&nbsp;\n' +
    '            <a>帮助</a>\n' +
    '        </i-col>\n' +
    '    </row>\n' +
    '    <Affix class="affix-search">\n' +
    '        <row class="topBarRow">\n' +
    '            <div class="topBar">\n' +
    '                <i-col :lg="{span:4,offset:3}" :md="{span:4,offset:2}" :xs="{span:0}">\n' +
    '                    <a :href="url.router.index">\n' +
    '                        <img :src="url.res.logoPic" width="50" height="45" class="logoPic"/>\n' +
    '                        <h1 style="color: #CC0033;" class="logoText">惠Go</h1></a>\n' +
    '                </i-col>\n' +
    '                <i-col :lg="{span:0}" :md="{span:0}" :xs="{span:5,offset:1}">\n' +
    '                    <a :href="url.router.index"><h1 style="color: red" class="logoText">惠Go</h1></a>\n' +
    '                </i-col>\n' +
    '                <i-col class="searchCol" :lg="{span:10}" :xs="{span:17}">\n' +
    '                    <i-input v-model="searchVal" placeholder="输入商品名称" class="searchInput" size="large" v-on:on-enter="search">\n' +
    '                        <i-button slot="append" icon="ios-search-strong" class="searchButton" @click="search">搜索\n' +
    '                        </i-button>\n' +
    '                    </i-input>\n' +
    '                </i-col>\n' +
    '            </div>\n' +
    '        </row>\n' +
    '    </Affix>\n' +
    '    <div class="topBarRow"></div>\n' +
    '</div>',
    data: function () {
        return {
            url: routerUrl,
            searchVal: '',
            isLogin: false,
            user: {}
        }
    },
    methods: {
        search: function () {
            Ajx.get(routerUrl.get.searchResult + "/" + this.searchVal).then(function () {
                VRouter(routerUrl.router.searchResult);
            }).catch(function () {
                VRouter(routerUrl.router.searchResult);
            });

        },
        logout: function () {
            Ajx.get(routerUrl.get.logout);
            /*返回登录页面*/
            VRouter(routerUrl.router.login);
        }
    },
    mounted: function () {
        header = this;
        /*先获取Session的用户信息,不存在表示用户未登录*/
        Ajx.get(routerUrl.get.userInfo).then(function (response) {
            if (response.data != undefined) {
                header.user = response.data;
                header.isLogin = true;
                /*向父组件传递用户登录状态*/
                header.$emit('user-status', header.user);
            }
        }).catch(function (error) {
            header.isLogin = false;
        });
        /*获取查询关键词*/
        /* if (this.keyWords != '' || this.keyWords.length != 0) {
             this.searchVal = this.keyWords;
         } else {*/
        Ajx.get(routerUrl.get.getKeyWords).then(function (response) {
            if (response.data.keyWord != 'null' || response.date != undefined) {
                header.searchVal = response.data.keyWord;
            }
        });
        // }
    }
})
;

/**
 * 底部页脚组件
 * */

Vue.component(
    'my-footer',
    {
        template:
        '<div style="background-color: #f8f8f9;margin-top: 50px;max-height: 80px;">\n' +
        '<div style="border: solid 1px #e9eaec;width: 90%;margin-left: 5%"></div>' +
        '    <row class="footer" type="flex" justify="center">\n' +
        '        <i-col>\n' +
        '            <a style="color: #80848f">关于网站</a>&nbsp;\n' +
        '            <a style="color: #80848f">帮助中心</a>&nbsp;\n' +
        '            <a style="color: #80848f">开放平台</a>&nbsp;\n' +
        '            <a style="color: #80848f">联系我们</a>&nbsp;\n' +
        '            <a style="color: #80848f">网站合作</a>&nbsp;\n' +
        '            <a style="color: #80848f">法律声明及隐私权政策</a>\n' +
        '        </i-col>\n' +
        '    </row>\n' +
        '    <row style="margin-top: 10px">\n' +
        '        <i-col :lg="{span:7,offset:10}" :xs="{span:13,offset:6}" style="color: #80848f">Copyright © 2017 &nbsp;&nbsp; 版权所有 NCHU</i-col>\n' +
        '    </row>\n' +
        '</div>'
    }
)