/**
 * 2017-10-5 21:40:11
 * 自定义Vue组件用于加载页脚,该js文件必须在根Vue实例定义的js文件之前加载,然后在页面上使用
 * <my-footer></my-footer>标签即可引入页脚,为了使样式生效请将该标签作为Vue根实例的子节点
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