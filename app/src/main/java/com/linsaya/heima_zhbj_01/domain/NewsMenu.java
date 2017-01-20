package com.linsaya.heima_zhbj_01.domain;

import java.util.List;

/**
 * 分类信息封装
 * 使用Gson解析时，逢{}创建对象，逢[]创建集合
 * * Created by Administrator on 2017/1/18.
 */

public class NewsMenu {
    public int retcode;
    public List<Integer> extend;
    public List<NewsMenuData> data;


    public class NewsMenuData {
        public int id;
        public String title;
        public int type;
        public List<NewsTabData> children;
    }

    public class NewsTabData {
        public int id;
        public int type;
        public String title;
        public String url;

    }
}



