/**
 * 定义前端页面与后端项目的映射路径
 * */
/*修改此变量以更改环境*/

const env = 'prod';

/*const env = 'dev';*/

/*页面跳转地址*/
const router_dev = {
    login: 'login.html',
    vipReg: 'vipReg.html',
    index: 'index.html',
    businessReg: 'businessReg.html',
    userZone: 'personal_Information.html',
    groupInfo: 'groupInfo.html',
    userOrder: 'userOrder.html',
    shopInfo: 'shopInfo.html',
    goodsInfo: 'goodsInfo.html',
    searchResult: 'searchResult.html',
    personOrder: 'personal_Order.html',
    paymentPage: 'payMent.html',
    saleTypePage: 'saleType.html',
    logout: 'login.html',
    personal_Address: 'personal_Address.html',
    admin: {
        welcome: 'welcome.html',
        businessAudit: 'businessAudit.html',
        businessManagement: 'businessManagement.html',
        shopManage: 'shopManage.html',
        userManagement: 'userManagement.html',
        webNotice: 'webNotice.html'
    }
}
const router_prod = {
    login: '/user/login',
    vipReg: '/user/vipReg',
    index: '/index',
    businessReg: '/user/businessReg',
    userZone: '/personal_Information',
    groupInfo: '/group/groupInfo',
    userOrder: '/user/userOrder',
    shopInfo: '/shop/shopInfo',
    goodsInfo: '/shop/goodsInfo',
    searchResult: '/searchGoods',
    personOrder: '/personal_Order',
    paymentPage: '/order/payment',
    saleTypePage: '/group/saleTypePage',
    logout: '/user/logout',
    personal_Address: '/personal_Address',
    personal_Favorites: '/personal_Favorites',
    admin: {
        welcome: '/admin/welcome',
        businessAudit: '/admin/businessAudit',
        businessManagement: '/admin/businessManagement',
        shopManage: '/admin/shopManage',
        userManagement: '/admin/userManagement',
        webNotice: '/admin/webNotice'
    }

}

/*异步请求地址信息,开发环境与生产环境相同*/
const getReq = {
    /*获取销售分类表*/
    saleTypeList: '/shop/saleTypeList',
    /*获取用户信息*/
    userInfo: '/user/userInfo',
    /*注销*/
    logout: '/user/logout',
    /*获取热门团购*/
    indexHotGroup: '/group/indexHotGroup',
    /*首页推荐团购*/
    indexGroupList: '/group/indexGroupList',
    /*获取分页总页码*/
    groupListPageAccount: '/group/groupListPageAccount',
    shopList: '/shop/getHotShop',
    shopInfo: '/shop/shopInfo',
    goodsInfo: '/goods/goodsInfo',
    searchResult: '/searchGoods',
    getSearchResult: '/goods/getSearchResult',
    groupInfo: '/group/getGroupInfo',
    toGroupInfoPage: '/group/groupInfo',
    serverTime: '/group/serverTime',
    goodsEvaluate: '/goods/evaluate',
    addToFavorites: '/goods/addToFavorites',
    getKeyWords: '/goods/getKeyWords',
    savePayGroup: '/group/paymentSave',
    getShopByGoods: '/shop/getShopInfoByGoods',
    receivingAddress: '/user/receivingAddress',
    /*获取支付方式列表*/
    getPaymentMethods: '/order/getPaymentMethods',
    saveSaleTypeParam: '/group/saveSaleTypeParam',
    groupBySaleType: '/group/groupBySaleType',
    getShopInfo: '/shop/getShopInfo',
    saveParams: '/saveParams',
    /*获取店铺下的团购活动信息*/
    getGroupByShop: '/group/getGroupByShop',
    saveGroupIdByGoodsId: '/saveGroupIdByGoodsId',
    goodList: '/goods/getGoodsList',
    getGroupsList: '/group/getGroupsList',
    getBusinessInfo: '/shop/getShopInfo',
    goodInfo: '/goods/getGoodInfo',
    delGoods: '/goods/delGoods',
    upGroupPurchase: "/group/upGroupPurchase",
    delGroupPurchase: "/group/delGroupPurchase",
    getShopInfo: "/shop/getShopInfo",
    goodInfo: "/goods/goodInfo",
    addGoods: '/goods/addGoods',
    upGoods: "goods/upGoods",
    evaluation: "",
    getUncheckShop: "/shop/getUncheckShop",
    shopCheck: '/shop/shopCheck',
    getBusiUserList: '/user/getBusiUserList',
    userLock: '/user/userLock',
    getAnnouncementList: '/user/getAnnouncementList',
    getShopListByLock: '/shop/getShopListByLock',
    shopLock: '/shop/shopLock',
    getAnnounce: '/user/getAnnounce'
};

const postReq = {
    userLogin: '/user/login',
    AdminLogin: '/user/AdminLogin',
    userVipReg: '/user/vipReg',
    businessReg: '/user/businessReg',
    joinGroup: '/group/joinGroup',
    savePayGroup: '/group/paymentSave',
    createOrder: '/order//createOrder',
    orderPayment: '/order/orderPayment',
    addAnnouncement: '/user/addAnnouncement'

};
const Res = {
    /*描述图片为空时显示为*/
    EmptyImg: 'http://benefit-go.oss-cn-hangzhou.aliyuncs.com/webRes/img/EmptyImp.png',
    EmptyEvaluation: 'http://benefit-go.oss-cn-hangzhou.aliyuncs.com/webRes/img/EmptyEvaluation.png',
    EmptySearch: 'http://benefit-go.oss-cn-hangzhou.aliyuncs.com/webRes/img/EmptySearch.png',
    /*logo图片地址*/
    logoPic: 'http://benefit-go.oss-cn-hangzhou.aliyuncs.com/webRes/img/logo.png',
    /*js及css*/
    iViewJs: 'https://cdn.bootcss.com/iview/2.4.0/iview.min.js',
    iViewCss: 'https://cdn.bootcss.com/iview/2.4.0/styles/iview.css',
    paymentImg: 'http://benefit-go.oss-cn-hangzhou.aliyuncs.com/webRes/img/AliPayment.jpg',
    commonCss: 'css/style/common.css',
    searchResultCSS: 'css/style/searchResult.css',
    EmptySaleType: 'http://benefit-go.oss-cn-hangzhou.aliyuncs.com/webRes/img/EmptySaleTypeResult.jpg'
}

/*开发环境地址信息*/
const Url_dev = {
    ajaxUrl: 'http://127.0.0.1',
    /*router表示页面跳转请求*/
    router: router_dev,
    /*get请求列表*/
    get: getReq,
    /*post请求列表*/
    post: postReq,
    res: Res,
    uploadUrl: 'http://127.0.0.1/fileUpload'
}
/*线上环境地址信息*/
const Url_prod = {
    ajaxUrl: 'http://127.0.0.1',
    uploadUrl: 'http://127.0.0.1/fileUpload',
    router: router_prod,
    get: getReq,
    res: Res,
    post: postReq,
}

/*根据不同的环境获取不同的路径信息*/
function getRouterUrl() {
    switch (env) {
        case 'dev':
            return Url_dev;
        case 'prod':
            return Url_prod;
    }
}

var routerUrl = getRouterUrl();

/*进行页面跳转*/
function VRouter(url) {
    window.location.href = url;
}

/**
 * 以下是axios的配置
 * */
var Ajx = axios.create({
    baseURL: routerUrl.ajaxUrl,
    timeout: 30000,
    responseType: 'json',
    header: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
    },
    /*设置该属性后可以使每次的请求使用相同的SessionID*/
    withCredentials: true
});
//添加一个请求拦截器
/*
Ajx.interceptors.request.use(function(config){
    return config;
},function(err){
    //Do something with request error
    return Promise.reject(error);
});*/

/*****************Util.js工具函数************************/

function getLocalTime(timestamp) {
    Date.prototype.toLocaleString = function () {
        return this.getFullYear() + "年" + (this.getMonth() + 1) + "月" + this.getDate() + "日 " + this.getHours() + "点" + this.getMinutes() + "分";
    };
    return new Date(timestamp).toLocaleString();
}

