package com.tanlifei.mykotlinapp.test.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.tanlifei.mykotlinapp.common.activity.BaseActivity
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.test.adapter.UserSingleAdapter
import com.tanlifei.mykotlinapp.core.model.User
import kotlinx.android.synthetic.main.activity_recycler.*
import java.util.ArrayList

class SingleAdapterItemActivity : BaseActivity(), View.OnClickListener {


    lateinit var recyclerView: RecyclerView

    /**
     * RecyclerView的数据源。
     */
    private var userList: MutableList<User> = ArrayList()

    internal lateinit var adapter: UserSingleAdapter

    override fun layoutResId(): Int {
        return R.layout.activity_single_adpate_item
    }

    override fun initView() {
        backBtn.setOnClickListener(this)
        initData()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(mActivity)
        adapter = UserSingleAdapter(R.layout.user_item, userList)
        recyclerView.adapter = adapter
    }

    fun initData() {
        userList.add(User(222, "tanlifei2", "tanlifei2"))
        userList.add(User(222, "tanlifei2", "tanlifei2"))
        userList.add(User(222, "tanlifei2", "tanlifei2"))
        userList.add(User(222, "tanlifei2", "tanlifei2"))
        userList.add(User(222, "tanlifei2", "tanlifei2"))
        userList.add(User(222, "tanlifei2", "tanlifei2"))
        userList.add(User(222, "tanlifei2", "tanlifei2"))
        userList.add(User(222, "tanlifei2", "tanlifei2"))
        userList.add(User(222, "tanlifei2", "tanlifei2"))
        userList.add(User(222, "tanlifei2", "tanlifei2"))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backBtn -> ActivityUtils.finishActivity(this)
        }
    }
}