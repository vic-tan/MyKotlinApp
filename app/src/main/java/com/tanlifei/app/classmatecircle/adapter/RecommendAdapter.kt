package com.tanlifei.app.classmatecircle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.adapter.BaseRvAdapter
import com.common.base.adapter.BaseRvHolder
import com.common.utils.GlideUtils
import com.common.widget.component.extension.drawable
import com.common.widget.component.extension.screenWidth
import com.common.widget.component.extension.setVisible
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.bean.CircleBean
import com.tanlifei.app.common.utils.AutoHeightUtils
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ItemRecommendBinding
import java.util.*

/**
 * @desc:推荐适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class RecommendAdapter :
    BaseRvAdapter<CircleBean>() {
    private val mCoverWidth =
        ((screenWidth - ConvertUtils.dp2px(20f)) * 0.5).toInt()//左右及中间边距为20
    private val mSource = 0 // 0、主页同学圈推荐 1、为人个中心作品

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRvHolder<ViewBinding> {
        return BaseRvHolder(ItemRecommendBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: CircleBean
    ) {
        holder as ItemRecommendBinding
        holder.apply {
            //审核状态:1=有效,0=无效
            caveat.setVisible(mSource == 1 && bean.checkStatus == 0 && bean.uid == UserInfoUtils.getUid())
            userName.text = bean.nickName
            GlideUtils.loadAvatar(mContext, bean.avatar, userHead)

            play.setVisible(bean.mediaType == 1)

            content.text = bean.content
            content.setVisible(ObjectUtils.isNotEmpty(bean.content))
            topicLayout.setVisible(ObjectUtils.isNotEmpty(bean.entertainmentTopicName))
            topicTxt.text = bean.entertainmentTopicName
            tag.text = bean.entertainmentTagName
            tag.setVisible(ObjectUtils.isNotEmpty(bean.entertainmentTagName))
            praiseCount.text = NumberUtils.setPraiseCount(bean.star)
            praiseCount.helper.iconNormalLeft =
                drawable(if (bean.isStar) R.mipmap.ic_praise_pre else R.mipmap.ic_praise_gray)

            GlideUtils.load(mContext, bean.image?.url, cover)
            cover.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            cover.layoutParams.height =
                AutoHeightUtils.getHeightParams(mCoverWidth, bean.image)
        }

    }

    override fun addChildClickView(holder: ViewBinding): LinkedHashSet<View> {
        holder as ItemRecommendBinding
        return linkedSetOf(holder.root)
    }


}