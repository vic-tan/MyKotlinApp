package com.tanlifei.mykotlinapp.test.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.rxLifeScope
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.tanlifei.mykotlinapp.common.activity.ToolBarActivity
import com.tanlifei.mykotlinapp.BuildConfig
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.core.http.*
import com.tanlifei.mykotlinapp.core.model.Banner
import com.xiaomai.environmentswitcher.EnvironmentSwitchActivity
import com.xiaomai.environmentswitcher.EnvironmentSwitcher
import com.xiaomai.environmentswitcher.bean.EnvironmentBean
import com.xiaomai.environmentswitcher.bean.ModuleBean
import com.xiaomai.environmentswitcher.listener.OnEnvironmentChangeListener
import kotlinx.android.synthetic.main.activity_category.*
import rxhttp.RxHttp
import rxhttp.toClass


class CategoryActivity : ToolBarActivity(), View.OnClickListener, OnEnvironmentChangeListener {



    override fun layoutResId(): Int {
        return R.layout.activity_category
    }


    override fun initView() {
        recyclerBtn.setOnClickListener(this)
        recyclerUtilsBtn.setOnClickListener(this)
        categoryBackBtn.setOnClickListener(this)
        eventBtn.setOnClickListener(this)
        http1Btn.setOnClickListener(this)
        http2Btn.setOnClickListener(this)
        EnvironmentSwitcher.addOnEnvironmentChangeListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.recyclerBtn -> {
                var a:Int = 19/0
                LogUtils.dTag(TAG, "a"+a)
                var intent = Intent(this, RecyclerActivity::class.java)
                var bundle = Bundle();
                bundle.putString("key", "test")
                intent.putExtras(bundle)
                startActivity(intent)
            }
            R.id.recyclerUtilsBtn -> {
                LogUtils.dTag(TAG, "recyclerUtilsBtn")
                var intent = Intent(
                    this,
                    BaseAdapterRecyclerActivity::class.java
                )
                var bundle = Bundle()
                bundle.putString("key", "test")
                intent.putExtras(bundle)
                ActivityUtils.startActivity(intent)
            }
            R.id.categoryBackBtn -> {
                ActivityUtils.finishActivity(this)
            }
            R.id.eventBtn -> {
                var intent = Intent(this, EventActivity::class.java)
                startActivity(intent)
            }
            R.id.http1Btn -> {
                EnvironmentSwitchActivity.launch(this);
            }
            R.id.http2Btn -> {
                sendGet();
            }
        }

    }

    //发送Get请求，获取文章列表
    private fun sendGet() {
        val appEnvironmentBean: EnvironmentBean =
            EnvironmentSwitcher.getAppEnvironmentBean(this, BuildConfig.DEBUG)
        RxHttpUtils.start(hud,rxLifeScope) {
            val banner = RxHttp.get(appEnvironmentBean.url + "/banner/json")
                .toClass<Banner>()
                .await()
            LogUtils.dTag(TAG, banner?.title)
        }
//
//        RxHttpUtils.start(hud,rxLifeScope) {
//            val treeList = RxHttp.get(appEnvironmentBean.url + "/tree/json")
//                .asParser(object : ResponseParser<List<TreeList>>() {})
//                .await()
//        }
//
//        RxHttpUtils.start(hud,rxLifeScope) {
//            val treeList = RxHttp.get(appEnvironmentBean.url+"/article/list/0/json")
//                    .asResponsePageList(Banner::class.java)
//                    .await()
//        }

    }

    /**
     * companion 静态
     * object 与companion 同用为伴生对象为单例，原对象没影响。
     */
    companion object {

        //const 可见性为public final static const 必须修饰val const 只允许在top-level级别和object中声明
        private const val TAG = "tanlifei_log"
    }

    override fun onEnvironmentChanged(
        module: ModuleBean?,
        oldEnvironment: EnvironmentBean?,
        newEnvironment: EnvironmentBean?
    ) {

        LogUtils.e(
            TAG,
            module?.getName() + "oleEnvironment=" + oldEnvironment?.getName() + "，oldUrl=" + oldEnvironment?.getUrl()
                    + ",newNevironment=" + newEnvironment?.getName() + "，newUrl=" + newEnvironment?.getUrl()
        )
        val appEnvironmentBean: EnvironmentBean =
            EnvironmentSwitcher.getAppEnvironmentBean(this, BuildConfig.DEBUG)
        LogUtils.e(
            TAG,
            "appEnvironmentBean url = ${appEnvironmentBean.url},name = ${appEnvironmentBean.name}"
        )
    }

}
