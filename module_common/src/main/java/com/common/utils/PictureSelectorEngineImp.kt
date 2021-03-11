package com.common.utils

import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.engine.PictureSelectorEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/11 17:11
 */
class PictureSelectorEngineImp : PictureSelectorEngine {
    override fun getResultCallbackListener(): OnResultCallbackListener<LocalMedia?> {
        return object : OnResultCallbackListener<LocalMedia?> {
            override fun onResult(result: List<LocalMedia?>) {
                // TODO 这种情况是内存极度不足的情况下，比如开启开发者选项中的不保留活动或后台进程限制，导致OnResultCallbackListener被回收
                // 可以在这里进行一些补救措施，通过广播或其他方式将结果推送到相应页面，防止结果丢失的情况
            }

            override fun onCancel() {
            }
        }
    }

    override fun createEngine(): ImageEngine {
        return GlideEngine
    }
}