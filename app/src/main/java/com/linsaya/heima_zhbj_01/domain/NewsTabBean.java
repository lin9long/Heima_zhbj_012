package com.linsaya.heima_zhbj_01.domain;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */

public class NewsTabBean {

    public NewsTab data;

    public class NewsTab {
        public String more;
        public List<NewsData> news;
        public List<NewsTop> topnews;
    }
        //新闻列表对象
        public class NewsData {
            public int id;
            public String listimage;
            public String pubdate;
            public String title;
            public String url;
            public String type;
        }

        //头条新闻
        public class NewsTop {
            public int id;
            public String topimage;
            public String pubdate;
            public String title;
            public String url;
            public String type;
        }
    }
