package com.nchu.util;

import java.util.List;

/*获取分页*/
public class PageUtil {
    public static List getPage(List list, int page, int pageSize) {
        return list.subList((page - 1) * pageSize, page * pageSize > list.size() ? list.size() - 1 : (page * pageSize - 1));
    }
}
