package com.tanlifei.mykotlinapp.test.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.tanlifei.mykotlinapp.common.activity.BaseActivity
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.test.adapter.UserAdapter
import com.tanlifei.mykotlinapp.core.model.User
import kotlinx.android.synthetic.main.test_activity_recycler.*
import java.util.ArrayList

class RecyclerActivity : BaseActivity(), View.OnClickListener {


    lateinit var recyclerView: RecyclerView

    /**
     * RecyclerView的数据源。
     */
    private var userList: MutableList<User> = ArrayList()

    internal lateinit var adapter: RecyclerView.Adapter<*>

    internal lateinit var layoutManager: LinearLayoutManager

    override fun layoutResId(): Int {
        return R.layout.test_activity_recycler
    }

    override fun initView() {
        var bundle = this.intent.extras
        var key = bundle?.get("key").toString()
        ToastUtils.showShort(key)
        recyclerView = findViewById(R.id.recyclerView)
        backBtn.setOnClickListener(this)
        deleteBtn.setOnClickListener(this)
        resetBtn.setOnClickListener(this)
        initData()
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(mActivity)
        recyclerView.layoutManager = layoutManager
        adapter = UserAdapter(userList)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
    }

    fun initData() {
        userList.add(User(111, "tanlifei1", "tanlifei1"))
        userList.add(User(111, "tanlifei1", "tanlifei1"))
        userList.add(User(111, "tanlifei1", "tanlifei1"))
        userList.add(User(111, "tanlifei1", "tanlifei1"))
        userList.add(User(111, "tanlifei1", "tanlifei1"))
        userList.add(User(111, "tanlifei1", "tanlifei1"))
        userList.add(User(111, "tanlifei1", "tanlifei1"))
        userList.add(User(111, "tanlifei1", "tanlifei1"))
        userList.add(User(111, "tanlifei1", "tanlifei1"))
        userList.add(User(111, "tanlifei1", "tanlifei1"))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backBtn -> ActivityUtils.finishActivity(this)
            R.id.deleteBtn -> {
                if (userList.size > 1) {
                    userList.clear()
                    userList.add(User(111, "tanlifei1", "tanlifei1"))
                }
                adapter.notifyDataSetChanged()
            }
            R.id.resetBtn -> {
                initData()
                adapter.notifyDataSetChanged()
            }

        }
    }
}