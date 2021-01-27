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
    @Module
    private class App {
        @Environment(url = "https://www.wanandroid.com", isRelease = true)
        private String online;
        @Environment(url = "https://wanandroid.com/wxarticle/chapters/json")
        private String test;
    }

    /**
     * Special module Music environment
     */
    @Module
    private class Music {
        @Environment(url = "https://www.codexiaomai.top/api/", isRelease = true)
        private String online;

        @Environment(url = "http://test.codexiaomai.top/api/")
        private String test;
    }

    /**
     * Special module News environment
     */
    @Module
    private class News {
        @Environment(url = "http://news/release/", isRelease = true)
        private String release;

        @Environment(url = "http://news/test/")
        private String test;

        @Environment(url = "http://news/test1/")
        private String test1;

        @Environment(url = "http://news/sandbox/")
        private String sandbox;
    }
}
