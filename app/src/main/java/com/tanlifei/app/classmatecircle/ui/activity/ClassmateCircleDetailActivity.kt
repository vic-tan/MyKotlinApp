package com.tanlifei.app.classmatecircle.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.ui.activity.BaseRecyclerBVMActivity
import com.common.utils.AntiShakeUtils
import com.common.utils.ViewUtils
import com.tanlifei.app.classmatecircle.adapter.CommentAdapter
import com.tanlifei.app.classmatecircle.bean.CommentBean
import com.tanlifei.app.classmatecircle.viewmodel.ClassmateCircleDetailViewModel
import com.tanlifei.app.databinding.ActivityClassmateCircleDetailBinding


/**
 * @desc:详情界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ClassmateCircleDetailActivity :
    BaseRecyclerBVMActivity<ActivityClassmateCircleDetailBinding, ClassmateCircleDetailViewModel>(),
    View.OnClickListener {
    private lateinit var adapter: CommentAdapter

    companion object {
        fun actionStart(id: Long) {
            var intent =
                Intent(
                    ActivityUtils.getTopActivity(),
                    ClassmateCircleDetailActivity::class.java
                ).apply {
                    putExtra(GlobalConst.Extras.ID, id)
                }
            ActivityUtils.startActivity(intent)
        }
    }

    override fun createViewModel(): ClassmateCircleDetailViewModel {
        return ClassmateCircleDetailViewModel(intent.getLongExtra(GlobalConst.Extras.ID, 0))
    }

    override fun init() {
        initViewModelObserve()
        initListener()
        initData()
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {

    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        ViewUtils.setOnClickListener(this)
    }

    private fun initData() {
        adapter = CommentAdapter()
        adapter.mData = viewModel.mData as MutableList<CommentBean>
        initRefreshView(
            binding.refreshLayout.smartRefreshLayout,
            binding.refreshLayout.refreshLoadingLayout,
            binding.refreshLayout.refreshRecycler
        )
    }

    override fun onClick(v: View) {
        if (AntiShakeUtils.isInvalidClick(v)) return

    }

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }


}