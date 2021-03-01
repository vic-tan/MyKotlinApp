package com.tanlifei.app.classmatecircle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ScreenUtils
import com.common.ComApplication.Companion.context
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.common.utils.GlideUtils
import com.common.utils.ResUtils
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
    CommonRvAdapter<ClassmateCircleBean, ItemRecommendBinding>() {
    private val screenWidth =
        ((ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(20f)) * 0.5).toInt()//左右及中间边距为20
    val source = 0 // 0、主页同学圈推荐 1、为人个中心作品

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ItemRecommendBinding> {
        return CommonRvHolder(ItemRecommendBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: CommonRvHolder<ItemRecommendBinding>,
        position: Int,
        binding: ItemRecommendBinding,
        bean: ClassmateCircleBean
    ) {
        when (source) {
            //审核状态:1=有效,0=无效
            1 ->
                holder.binding.caveat.visibility =
                    if (bean.checkStatus == 0 && bean.uid == UserInfoUtils.getUid()) View.VISIBLE else View.GONE
            else -> holder.binding.caveat.visibility = View.GONE
        }
        holder.binding.userName.text = bean.nickName
        GlideUtils.loadAvatar(context, bean.avatar, holder.binding.userHead)

        holder.binding.play.visibility = if (bean.mediaType == 1) View.VISIBLE else View.GONE

        holder.binding.content.text = bean.content
        holder.binding.content.visibility =
            if (ObjectUtils.isNotEmpty(bean.content)) View.VISIBLE else View.GONE

        holder.binding.topicLayout.visibility =
            if (ObjectUtils.isEmpty(bean.entertainmentTopicName)) View.GONE else View.VISIBLE
        holder.binding.topicTxt.text = bean.entertainmentTopicName
        holder.binding.tag.text = bean.entertainmentTagName
        holder.binding.tag.visibility =
            if (ObjectUtils.isEmpty(bean.entertainmentTagName)) View.GONE else View.VISIBLE

        holder.binding.praiseCount.text = NumberUtils.setPraiseCount(bean.star)
        holder.binding.praiseCount.helper.iconNormalLeft =
            ResUtils.getDrawable(if (bean.isStar) R.mipmap.ic_praise_pre else R.mipmap.ic_praise)

        GlideUtils.load(context, bean.image?.url, holder.binding.cover)
        holder.binding.cover.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        holder.binding.cover.layoutParams.height =
            AutoHeightUtils.getHeightParams(screenWidth, bean.image)
    }

    override fun addChildClickViewIds(binding: ItemRecommendBinding): LinkedHashSet<View> {
        return linkedSetOf(binding.root)
    }


}