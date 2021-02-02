package com.tanlifei.app.common.config;

import com.tanlifei.app.BuildConfig;
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
        @Environment(url = BuildConfig.BASE_URL_DEV, alias = "开发环境")
        private String dev;// # 开发环境
        @Environment(url = BuildConfig.BASE_URL_TEST, alias = "测试环境")
        private String test;// # 测试环境
        @Environment(url = BuildConfig.BASE_URL_PRO, alias = "正式环境", isRelease = true)
        private String pro;// # 正式环境


    }

    /**
     * 分享区分环境
     */
    @Module(alias = "分享环境")
    private class Share {

        @Environment(url = BuildConfig.BASE_URL_DEV + "share_dev", alias = "开发环境")
        private String dev;// # 开发环境

        @Environment(url = BuildConfig.BASE_URL_TEST + "share_test", alias = "测试环境")
        private String test;// # 测试环境

        @Environment(url = BuildConfig.BASE_URL_PRO + "share_pro", alias = "正式环境", isRelease = true)
        private String pro;// # 正式环境


    }


}
