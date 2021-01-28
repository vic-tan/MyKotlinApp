package com.tanlifei.app.common.bean

import androidx.lifecycle.ViewModel

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/28 15:55
 */
open class BaseViewModel : ViewModel() {

    /**
     * 由于屏幕旋转导致的Activity重建，该方法不会被调用
     *
     * 只有ViewModel已经没有任何Activity与之有关联，系统则会调用该方法，你可以在此清理资源
     */
    override fun onCleared() {
        super.onCleared()
    }
}