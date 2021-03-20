package com.tanlifei.app.profile.ui.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.Html.ImageGetter
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.tanlifei.app.databinding.ActivityManualDetailBinding
import com.tanlifei.app.profile.viewmodel.ManualViewModel
import java.net.URL


/**
 * @desc:操作手册详情
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ManualDetailActivity : BaseToolBarActivity<ActivityManualDetailBinding, ManualViewModel>() {

    companion object {
        fun actionStart(id: Long) {
            var intent = Intent(
                ActivityUtils.getTopActivity(),
                ManualDetailActivity::class.java
            ).apply {
                putExtra(GlobalConst.Extras.ID, id)
            }
            ActivityUtils.startActivity(intent)
        }
    }

    override fun createViewModel(): ManualViewModel {
        return ManualViewModel()
    }

    override fun init() {
        mViewModel.requestManualDetail(intent.getLongExtra(GlobalConst.Extras.ID, 0))
        mViewModel.mBean.observe(this, Observer {
            mBinding.name.text = Html.fromHtml(mViewModel.mBean.value?.content)
            val imgGetter = ImageGetter { source ->
                var drawable: Drawable? = null
                val url: URL
                try {
                    url = URL(source)
                    drawable = Drawable.createFromStream(url.openStream(), "") //获取网路图片
                } catch (e: Exception) {
                    return@ImageGetter null
                }
                drawable.setBounds(
                    0, 0, drawable.intrinsicWidth, drawable
                        .intrinsicHeight
                )
                drawable
            }
            mBinding.name.text = Html.fromHtml(mViewModel.mBean.value?.content, imgGetter, null)
        })

    }

}