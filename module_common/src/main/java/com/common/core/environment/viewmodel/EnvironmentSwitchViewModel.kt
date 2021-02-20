package com.common.core.environment.viewmodel

import com.common.cofing.constant.ApiEnvironmentConst
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.core.environment.bean.EnvironmentBean
import com.common.core.environment.bean.ModuleBean
import com.common.core.environment.event.EnvironmentEvent
import com.common.core.environment.utils.EnvironmentUtils
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 17:51
 */
class EnvironmentSwitchViewModel : BaseListViewModel() {

    var environmentList: MutableList<EnvironmentBean> = ArrayList()
    private var isUpdate: Boolean = false//内容是否修改过

    /**
     * 保存选中的到数据库
     */
    private fun saveDB(
        environment: EnvironmentBean
    ) {
        LitePal.deleteAll(
            EnvironmentBean::class.java,
            "${EnvironmentBean.DB_GROUP} = ? ",
            "${environment.group}"
        )
        environment.save()
        EnvironmentUtils.onEnvironmentChanged(environment)
        EventBus.getDefault().post(
            EnvironmentEvent(
                environment
            )
        )
    }

    fun setSelect(pos: Int) {
        isUpdate = true
        val group: Long = environmentList[pos].group
        for (lst in environmentList) {
            if (lst.group == group && lst.defaultCheck) {
                lst.defaultCheck = false
            }
        }
        environmentList[pos].defaultCheck = true
        notifyDataSetChanged(DataChagedType.NOTIFY)
    }


    fun saveAllSelect() {
        if (!isUpdate)
            return
        for (lst in environmentList) {
            if (lst.defaultCheck) {
                saveDB(lst)
            }
        }
    }


    /**
     * 设置默认选择
     */
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

    /**
     * 设置列表数据
     */
    fun initListData(mapJsonStr: String) {
        if (mapJsonStr.isNotEmpty()) {
            val environmentModuleList: MutableList<ModuleBean> =
                Gson().fromJson<Array<ModuleBean>>(mapJsonStr, Array<ModuleBean>::class.java)
                    .toMutableList()

            for ((i, modules) in environmentModuleList.withIndex()) {//遍历
                val moduleBean: EnvironmentBean =
                    EnvironmentBean(
                        modules.alias,
                        "",
                        defaultCheck = false
                    )
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
    }

    override fun requestList(dataChangedType: DataChagedType) {
    }
}