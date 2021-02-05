package com.common.core.environment

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.common.R
import com.common.cofing.constant.ApiEnvironmentConst
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.databinding.ActivityEnvironmentSwitchBinding
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal

/**
 * @desc:环境切换acitity
 * @author: tanlifei
 * @date: 2021/2/2 15:21
 */
class EnvironmentSwitchActivity : BaseToolBarActivity<ActivityEnvironmentSwitchBinding>() {
    lateinit var environmentList: MutableList<EnvironmentBean>
    internal lateinit var adapter: EnvironmentAdapter

    companion object {
        private const val EXTRAS_DATA = "extras_data"
        fun actionStart(activity: Activity, list: MutableList<ModuleBean>) {
            val mapStr = Gson().toJson(EnvironmentUtils.initEnvironmentSwitcher())
            var intent = Intent(activity, EnvironmentSwitchActivity::class.java).apply {
                putExtra(EXTRAS_DATA, mapStr)
            }
            ActivityUtils.startActivity(intent)
        }

        fun actionStart(activity: Activity) {
            actionStart(activity, EnvironmentUtils.initEnvironmentSwitcher())
        }
    }


    override fun init() {
        environmentList = ArrayList()
        val mapJsonStr: String = intent.getStringExtra(EXTRAS_DATA)
        if (mapJsonStr.isNotEmpty()) {
            val environmentModuleList: MutableList<ModuleBean> =
                Gson().fromJson<Array<ModuleBean>>(mapJsonStr, Array<ModuleBean>::class.java)
                    .toMutableList()

            for ((i, modules) in environmentModuleList.withIndex()) {//遍历
                val moduleBean: EnvironmentBean =
                    EnvironmentBean(modules.alias, "", defaultCheck = false)
                moduleBean.type = EnvironmentBean.TITLE
                environmentList.add(moduleBean)
                for (environmentBean in modules.list) {
                    environmentBean.type = EnvironmentBean.CONTENT
                    environmentBean.group = modules.groupId
                    environmentList.add(environmentBean)
                }
            }
            setDefaultCheck()
        }
        initRecyclerView()
    }

    private fun setDefaultCheck() {
        val saveEnvironmentList: List<EnvironmentBean> =
            LitePal.findAll(EnvironmentBean::class.java)
        if (saveEnvironmentList.isEmpty()) {
            for (list in environmentList) {
                if (list.group == EnvironmentBean.GROUP_API) {
                    list.defaultCheck = list.url == ApiEnvironmentConst.BASE_URL
                }
            }
            return
        }
        for (lt in saveEnvironmentList) {
            for (list in environmentList) {
                if (list.group == lt.group) {
                    list.defaultCheck = list.url == lt.url
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recycler.layoutManager = LinearLayoutManager(mActivity)
        adapter = EnvironmentAdapter(environmentList)
        binding.recycler.adapter = adapter
        adapter.addChildClickViewIds(R.id.title, R.id.url, R.id.radio)
        adapter.setOnItemClickListener { _, _, position ->
            run {
                adapter.setSelect(
                    environmentList[position].group,
                    position
                )
                var environment: EnvironmentBean = environmentList[position]
                LitePal.delete(EnvironmentBean::class.java, environment.group)
                LitePal.deleteAll(
                    EnvironmentBean::class.java,
                    "${EnvironmentBean.DB_GROUP} = ? ",
                    "${EnvironmentBean.GROUP_API}"
                )
                environment.save()
                EnvironmentUtils.onEnvironmentChanged(environment)
                EventBus.getDefault().post(EnvironmentEvent(environmentList[position]))
            }

        }

    }
}


