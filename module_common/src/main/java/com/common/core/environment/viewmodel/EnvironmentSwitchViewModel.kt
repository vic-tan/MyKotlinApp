package com.common.core.environment.viewmodel

import com.blankj.utilcode.util.ObjectUtils
import com.common.core.environment.utils.ApiEnvironmentConst
import com.common.base.bean.ListDataChangePrams
import com.common.base.viewmodel.BaseListViewModel
import com.common.constant.GlobalEnumConst
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
        val group: Long = (mData as MutableList<EnvironmentBean>)[pos].group
        for (lst in mData as MutableList<EnvironmentBean>) {
            if (lst.group == group && lst.defaultCheck) {
                lst.defaultCheck = false
            }
        }
        (mData as MutableList<EnvironmentBean>)[pos].defaultCheck = true
        setDataChange(ListDataChangePrams(GlobalEnumConst.UiType.NOTIFY))
    }


    fun saveAllSelect() {
        if (!isUpdate)
            return
        for (lst in mData as MutableList<EnvironmentBean>) {
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
            for (list in mData as MutableList<EnvironmentBean>) {
                if (list.group == EnvironmentBean.GROUP_API) {
                    list.defaultCheck = list.url == ApiEnvironmentConst.BASE_URL
                }
            }
            return
        }
        for (lt in saveEnvironmentList) {
            for (list in mData as MutableList<EnvironmentBean>) {
                if (list.group == lt.group) {
                    list.defaultCheck = list.url == lt.url
                }
            }
        }
    }

    /**
     * 设置列表数据
     */
    fun initListData(mapJsonStr: String?) {
        if (ObjectUtils.isNotEmpty(mapJsonStr)) {
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
                moduleBean.itemType = EnvironmentBean.TITLE
                mData.add(moduleBean)
                for (environmentBean in modules.list) {
                    environmentBean.itemType = EnvironmentBean.CONTENT
                    environmentBean.group = modules.groupId
                    mData.add(environmentBean)
                }
            }
            setDefaultCheck()
        }
    }

    override fun requestList() {
    }
}