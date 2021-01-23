package com.tanlifei.mykotlinapp.test.activity

import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.tanlifei.mykotlinapp.common.activity.BaseActivity
import com.tanlifei.mykotlinapp.R
import kotlinx.android.synthetic.main.test_activity_base_adapter_recycler.*
import kotlinx.android.synthetic.main.test_activity_event.backBtn

class BaseAdapterRecyclerActivity : BaseActivity(), View.OnClickListener {


    override fun layoutResId(): Int {
        return R.layout.test_activity_base_adapter_recycler
    }

    override fun initView() {
        backBtn.setOnClickListener(this)
        singleItemBtn.setOnClickListener(this)
        multiBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backBtn -> ActivityUtils.finishActivity(this)
            R.id.singleItemBtn -> {
                ActivityUtils.startActivity(Intent(this, SingleAdapterItemActivity::class.java))
            }
            R.id.multiBtn -> {
                ActivityUtils.startActivity(Intent(this, MultiAdapterItemActivity::class.java))
            }

        }
    }
}