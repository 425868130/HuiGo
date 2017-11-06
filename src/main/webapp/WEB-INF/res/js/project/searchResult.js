/*搜索结果页面的js文件*/
var pageKeyWords = document.getElementById('keyWords').innerHTML;
var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        page: 1,
        pageSize: 10,
        resultList: [],
        goodsKeyWords: pageKeyWords
    },
    methods: {
        toGoodsInfo: function (goodsId) {
            Ajx.get(routerUrl.get.saveGroupIdByGoodsId, {
                params: {
                    goodsId: goodsId
                }
            }).then(function (response) {
                VRouter(routerUrl.router.groupInfo);
            })
        }

    },
    computed: {},
    mounted: function () {
        getSearchResult(this, 1, 10);
    }
});

/*获取查询结果*/
function getSearchResult(appThis, page, pageSize) {
    Ajx.get(routerUrl.get.getSearchResult, {
        params: {
            goodsKeyWords: pageKeyWords,
            page: page,
            pageSize: pageSize
        }
    }).then(function (response) {
        response.data.forEach(function (goods) {
            app.resultList.push(goods);
        })
    }).catch(function (error) {

    });
}