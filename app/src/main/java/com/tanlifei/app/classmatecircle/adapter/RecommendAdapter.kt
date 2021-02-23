package com.tanlifei.app.classmatecircle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ScreenUtils
import com.common.core.base.holder.BaseBindingAdapter
import com.common.core.base.holder.BaseVBViewHolder
import com.common.utils.GlideUtils
import com.common.utils.ResUtils
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.common.utils.AutoHeightUtils
import com.tanlifei.app.common.utils.NumberUtils
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ItemRecommendBinding

/**
 * @desc:推荐适配器
 * @author: tanlifei
 * @date: 2021/2/8 10:41
 */
class RecommendAdapter(data: MutableList<ClassmateCircleBean>) :
    BaseBindingAdapter<ClassmateCircleBean, ItemRecommendBinding>(data) {
    private val screenWidth =
        ((ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(20f)) * 0.5).toInt()//左右及中间边距为20
    val source = 0 // 0、主页同学圈推荐 1、为人个中心作品

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemRecommendBinding {
        return ItemRecommendBinding.inflate(inflater, parent, false)
    }


    override fun convert(
        holder: BaseVBViewHolder<ItemRecommendBinding>,
        item: ClassmateCircleBean
    ) {
        when (source) {
            //审核状态:1=有效,0=无效
            1 ->
                holder.binding.caveat.visibility =
                    if (item.checkStatus == 0 && item.uid == UserInfoUtils.getUid()) View.VISIBLE else View.GONE
            else -> holder.binding.caveat.visibility = View.GONE
        }
        holder.binding.userName.text = item.nickName
        GlideUtils.loadAvatar(context, item.avatar, holder.binding.userHead)

        holder.binding.play.visibility = if (item.mediaType == 1) View.VISIBLE else View.GONE

        holder.binding.content.text = item.content
        holder.binding.content.visibility =
            if (ObjectUtils.isNotEmpty(item.content)) View.VISIBLE else View.GONE

        holder.binding.topicLayout.visibility =
            if (ObjectUtils.isEmpty(item.entertainmentTopicName)) View.GONE else View.VISIBLE
        holder.binding.topicTxt.text = item.entertainmentTopicName
        holder.binding.tag.text = item.entertainmentTagName
        holder.binding.tag.visibility =
            if (ObjectUtils.isEmpty(item.entertainmentTagName)) View.GONE else View.VISIBLE

        holder.binding.praiseCount.text = NumberUtils.setPraiseCount(item.star)
        holder.binding.praiseCount.helper.iconNormalLeft =
            ResUtils.getDrawable(if (item.isStar) R.mipmap.icon_cc_praise_pre else R.mipmap.icon_cc_praise)

        GlideUtils.load(context, item.image?.url, holder.binding.cover)
        holder.binding.cover.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        holder.binding.cover.layoutParams.height =
            AutoHeightUtils.getHeightParams(screenWidth, item.image)
    }


}