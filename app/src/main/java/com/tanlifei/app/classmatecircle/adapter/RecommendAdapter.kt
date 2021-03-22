package com.tanlifei.app.classmatecircle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.adapter.CommonRvHolder
import com.common.core.base.adapter.CommonRvMultiItemAdapter
import com.common.utils.GlideUtils
import com.common.utils.extension.drawable
import com.common.utils.extension.screenWidth
import com.common.utils.extension.setVisible
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
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
    CommonRvMultiItemAdapter<ClassmateCircleBean>() {
    private lateinit var mContext: Context
    private val mCoverWidth =
        ((screenWidth - ConvertUtils.dp2px(20f)) * 0.5).toInt()//左右及中间边距为20
    private val mSource = 0 // 0、主页同学圈推荐 1、为人个中心作品

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ViewBinding> {
        mContext = parent.context
        return CommonRvHolder(ItemRecommendBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewBinding,
        position: Int,
        bean: ClassmateCircleBean
    ) {
        holder as ItemRecommendBinding
        //审核状态:1=有效,0=无效
        holder.caveat.setVisible(mSource == 1 && bean.checkStatus == 0 && bean.uid == UserInfoUtils.getUid())
        holder.userName.text = bean.nickName
        GlideUtils.loadAvatar(mContext, bean.avatar, holder.userHead)

        holder.play.setVisible(bean.mediaType == 1)

        holder.content.text = bean.content
        holder.content.setVisible(ObjectUtils.isNotEmpty(bean.content))
        holder.topicLayout.setVisible(ObjectUtils.isNotEmpty(bean.entertainmentTopicName))
        holder.topicTxt.text = bean.entertainmentTopicName
        holder.tag.text = bean.entertainmentTagName
        holder.tag.setVisible(ObjectUtils.isNotEmpty(bean.entertainmentTagName))
        holder.praiseCount.text = NumberUtils.setPraiseCount(bean.star)
        holder.praiseCount.helper.iconNormalLeft =
            drawable(if (bean.isStar) R.mipmap.ic_praise_pre else R.mipmap.ic_praise_gray)

        GlideUtils.load(mContext, bean.image?.url, holder.cover)
        holder.cover.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        holder.cover.layoutParams.height =
            AutoHeightUtils.getHeightParams(mCoverWidth, bean.image)
    }

    override fun addChildClickViewIds(holder: ViewBinding): LinkedHashSet<View> {
        holder as ItemRecommendBinding
        return linkedSetOf(holder.root)
    }


}