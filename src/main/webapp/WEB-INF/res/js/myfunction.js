﻿/*我的js函数*/
var provinceData = ["江西", "湖北", "福建"];
var cityData = {
    "江西": ["南昌", "赣州", "景德镇", "萍乡", "新余", "九江", "鹰潭", "吉安", "宜春", "抚州", "上饶"],
    "湖北": ["武汉", "黄石", "宜昌", "荆州", "鄂州", "黄冈", "盐宁", "十堰", "襄樊"],
    "福建": ["福州", "厦门", "三明", "莆田", "泉州", "漳州", "南平", "龙岩", "宁德"]
};
var areaData = {
    "南昌": ["新建", "南昌县", "青山湖", "东湖", "西湖", "青云谱", "湾里", "安义", "进贤"],
    "赣州": ["赣县", "大余", "瑞金", "南康", "章贡", "兴国", "宁都", "于都", "石城", "安远"],
    "景德镇": ["珠山", "乐平", "浮梁", "昌江"],
    "萍乡": ["安源", "湘东", "莲花", "芦溪", "上栗"],
    "新余": ["渝水", "分宜"],
    "九江": ["修水", "武宁", "浔阳", "庐山", "瑞昌", "彭泽", "永修", "湖口", "都昌", "德安", "星子"],
    "鹰潭": ["月湖", " 贵溪", "余江"],
    "吉安": ["吉安", "永丰", "永新", "新干", "泰和", "峡江", "遂川", "安福", "吉水", "万安"],
    "宜春": ["袁州", "丰城", "高安", "铜鼓", "靖安", "宜丰", "奉新", "上高", "万载"],
    "抚州": ["南丰", "临川", "乐安", "金溪", "南城", "东乡", "资溪", "宜黄", "广昌", "黎川", "崇仁"],
    "上饶": ["信州", "德新", "上饶", "广新", "鄱阳", "婺源", "铅山", "余干", "横峰", "弋阳", "玉山", "万年"],

    "黄石": ["黄石港", "石灰窑", "下陆", "铁山", "大治", "阳新"],
    "宜昌": ["西陵", "点军", "夷陵", "伍家岗", "宜都", "当阳", "枝江", "远安", "新山"],
    "荆州": ["东宝", "钟祥", "京山", "沙祥"],
    "鄂州": ["鄂城", "华容", "梁子湖"],
    "黄冈": ["黄州", "麻城", "武穴", "红安", "罗田", "浠水", "黄梅", "英山", "团风"],
    "盐宁": ["盐安", "赤壁", "瑞金", "嘉鱼", "通山", "崇阳", "通城"],
    "十堰": ["张湾", "茅箭", "丹江口", "郧县", "竹山", "房县", "竹溪"],
    "襄樊": ["襄城", "樊城", "襄阳", "老河口", "枣阳", "宜城", "南漳", "谷城", "保漳"],

    "武汉": ["江岸", "江汉", "汉阳", "武昌", "硚口", "青山", "东西湖"],
    "福州": ["鼓楼", "台江", "仓山", "马尾", "晋安", "福清", "长乐"],
    "厦门": ["开元", "思明", "鼓浪屿", "杏林", "同安", "集美", "湖里"],
    "三明": ["梅列", "三元", "永安", "沙县", "明溪", "泰宁", "尤溪", "清流"],
    "莆田": ["城厢", "涵江", "荔城", "秀屿", "仙游"],
    "泉州": ["鲤城", "丰泽", "洛江", "泉港", "石狮", "晋江", "南安", "惠安", "永春", "安溪", " 德化", "金门"],
    "漳州": ["芗城", "龙文", "龙海", "平和", "南靖", "诏安", "漳浦", "华安", "东山", "长泰", "云霄"],
    "南平": ["延平", "建瓯", "邵武", "武夷山", "建阳", "松溪", "光泽", "顺昌", "浦城", "政和"],
    "龙岩": ["新罗", "漳平", "长汀", "武平", "上杭", "永定", "连城"],
    "宁德": ["蕉城", "福安", "福鼎", "寿宁", "霞浦", "柘荣", "屏南", "古田", "周宁"]
};

function changeCity() {
    $('#city').empty();
    var temp = $('#province').val();
    var city = cityData[temp];
    var cities = "";
    cities += "<option value='-1' selected='selected'>------市------</option>"
    for (var i in city) {
        cities += "<option value=" + city[i] + ">" + city[i] + "</option>"
    }
    $('#city').append(cities);
}

function changeArea() {
    $('#area').empty();
    var temp = $('#city').val();
    var area = areaData[temp];
    var areas = "";
    areas += "<option value='-1' selected='selected'>------县/区------</option>"
    for (var i in area) {
        areas += "<option value=" + area[i] + ">" + area[i] + "</option>"
    }
    $('#area').append(areas);
}