package com.common.environment

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.common.R
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.databinding.ActivityEnvironmentSwitchBinding
import com.common.utils.LogTools
import com.google.gson.Gson
import com.hjq.bar.OnTitleBarListener

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
        const val REQUEST_CODE = 1000
        fun actionStart(activity: Activity, list: MutableList<ModuleBean>) {
            val mapStr = Gson().toJson(list)
            var intent = Intent(activity, EnvironmentSwitchActivity::class.java).apply {
                putExtra(EXTRAS_DATA, mapStr)
            }
            ActivityUtils.startActivityForResult(
                activity, intent,
                REQUEST_CODE
            )
        }
    }

    override fun setTitleBarListener() {
        titleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(v: View) {
                val intent = Intent()
                intent.putExtra("room", 1)
                intent.putExtra("roomId", 2)
                setResult(REQUEST_CODE, intent)
                ActivityUtils.finishActivity(mActivity)
            }

            override fun onTitleClick(v: View) {
            }

            override fun onRightClick(v: View) {
            }
        })
    }

    override fun layoutResId(): Int {
        return R.layout.activity_environment_switch
    }

    override fun createBinding(layoutView: View): ActivityEnvironmentSwitchBinding {
        return ActivityEnvironmentSwitchBinding.bind(layoutView)
    }

    override fun initView() {
        environmentList = ArrayList()
        val mapJsonStr: String = intent.getStringExtra(EXTRAS_DATA)
        if (mapJsonStr.isNotEmpty()) {
            val environmentModuleList: MutableList<ModuleBean> =
                Gson().fromJson<Array<ModuleBean>>(mapJsonStr, Array<ModuleBean>::class.java)
                    .toMutableList()

            for ((i, modulelist) in environmentModuleList.withIndex()) {                                  //遍历
                val moduleBean: EnvironmentBean = EnvironmentBean(modulelist.alias, "", false)
                moduleBean.type = EnvironmentBean.TITLE
                moduleBean.group = i
                environmentList.add(moduleBean)
                for (environmentBean in modulelist.list) {
                    environmentBean.type = EnvironmentBean.CONTENT
                    environmentBean.group = i
                    environmentList.add(environmentBean)
                }
            }
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recycler.layoutManager = LinearLayoutManager(mActivity)
        adapter = EnvironmentAdapter(environmentList)
        binding.recycler.adapter = adapter
    }

}


