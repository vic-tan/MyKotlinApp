package com.tanlifei.mykotlinapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.tanlifei.mykotlinapp.BaseActivity
import com.tanlifei.mykotlinapp.R
import kotlinx.android.synthetic.main.activity_base_adapter_recycler.*
import kotlinx.android.synthetic.main.activity_recycler.backBtn

class BaseAdapterRecyclerActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_adapter_recycler)
        backBtn.setOnClickListener(this)
        singleItemBtn.setOnClickListener(this)
        multiBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backBtn -> ActivityUtils.finishActivity(this)
            R.id.singleItemBtn -> {
                ActivityUtils.startActivity(Intent(this,SingleAdapterItemActivity::class.java))
            }
            R.id.multiBtn -> {
                ActivityUtils.startActivity(Intent(this,MultiAdapterItemActivity::class.java))
            }

        }
    }
}