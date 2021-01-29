package com.tanlifei.app.common.config;

import com.xiaomai.environmentswitcher.annotation.Environment;
import com.xiaomai.environmentswitcher.annotation.Module;

/**
 * 此类是用来做正式环境还测试之前切换
 */
public class EnvironmentConfig {
    /**
     * 整个应用程序环境
     */
    @Module(alias = "接口环境")
    private class App {
        @Environment(url = UrlConst.URL_BASE_PRO, alias = "正式环境")
        private String pro;// # 正式环境
        @Environment(url = UrlConst.URL_BASE_DEV, alias = "开发环境")
        private String dev;// # 开发环境
        @Environment(url = UrlConst.URL_BASE_TEST, alias = "测试环境", isRelease = true)
        private String test;// # 测试环境
    }

    /**
     * 分享区分环境
     */
    @Module(alias = "分享环境")
    private class Share {
        @Environment(url = UrlConst.URL_BASE_PRO, alias = "正式环境")
        private String pro;// # 正式环境

        @Environment(url = UrlConst.URL_BASE_DEV, alias = "开发环境")
        private String dev;// # 开发环境

        @Environment(url = UrlConst.URL_BASE_TEST, alias = "测试环境", isRelease = true)
        private String test;// # 测试环境
    }


}
