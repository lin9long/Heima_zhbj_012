package com.linsaya.heima_zhbj_01.domain;

import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */

public class PhotoBean {
    public PhotoData data;

    public class PhotoData {
        public List<PhotoNews> news;
    }

    public class PhotoNews {
        public int id;
        public String listimage;
        public String title;
    }
}
