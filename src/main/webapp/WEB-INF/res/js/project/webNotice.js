/**
 *商家账号操作
 */
var temp;
var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        NoticeList: [],
        noticeTxt: '',
        noticeTitle: '',
        user: '',
        isLogin: false,
        page: 1,
        pageSize: 10,
        /*总记录条数*/
        totalRecord: 0,
        /*表格列定义*/
        tableColumns: [
            {
                title: '标题',
                key: 'title'
            }, {
                title: '内容',
                key: 'content'
            },
            {
                title: '发布人',
                key: 'publisherNick'
            },
            {
                title: '发布时间',
                key: 'gmtCreate'
            }
        ]
    },
    methods: {
        /*页面变化*/
        pageChange: function (page) {
            /*修改页码*/
            this.page = page;
            loadNoticeList(this);
        },
        addNotice: function () {
            Ajx.post(routerUrl.post.addAnnouncement, {
                title: app.noticeTitle,
                content: app.noticeTxt
            }).then(function () {
                alert('发布成功');
                app.noticeTitle = '';
                app.noticeTxt = ''
                loadNoticeList(app);
            })
        }
    },
    mounted: function () {
        getLoginUserInfo(this);
    }
});

/*加载商家列表*/
function loadNoticeList(appThis) {
    Ajx.get(routerUrl.get.getAnnouncementList, {
        params: {
            page: appThis.page,
            pageSize: appThis.pageSize,
        }
    }).then(function (response) {
        appThis.totalRecord = response.data.totalRecord;
        appThis.NoticeList = [];
        response.data.dataList.forEach(function (data) {
            data.gmtCreate = getLocalTime(data.gmtCreate);
            appThis.NoticeList.push(data);
        })
    })
}


function getLoginUserInfo(appThis) {
    Ajx.get(routerUrl.get.userInfo).then(function (response) {
        if (response.data != undefined) {
            appThis.user = response.data;
            appThis.isLogin = true;
            loadNoticeList(appThis);
        }
    }).catch(function (error) {
        alert('请先登录!')
        window.location.href = routerUrl.router.admin.welcome;
    });
}

function getLocalTime(timestamp) {
    Date.prototype.toLocaleString = function () {
        return this.getFullYear() + "年" + (this.getMonth() + 1) + "月" + this.getDate() + "日 " + this.getHours() + "点" + this.getMinutes() + "分";
    };
    return new Date(timestamp).toLocaleString();
}
