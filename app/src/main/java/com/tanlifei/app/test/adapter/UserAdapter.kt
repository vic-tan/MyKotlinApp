package com.tanlifei.app.test.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tanlifei.app.R
import com.tanlifei.app.core.bean.UserTest

class UserAdapter(private val userList: List<UserTest>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_USERS -> return createUserHolder(parent)
            TYPE_EMPTY -> return createEmptyHolder(parent)
        }
        throw IllegalArgumentException()
    }

    override fun getItemCount(): Int {
        return dataItemCount
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_USERS -> bindUserHolder(holder as UserViewHolder, position)
            TYPE_EMPTY -> bindEmptyHolder(holder as EmptyViewHolder)
        }
    }

    private fun createUserHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_user_item, parent, false)
        val holder = UserViewHolder(view)
        holder.rootLayout.setOnClickListener {
            val position = holder.adapterPosition
        }
        return holder
    }

    private fun createEmptyHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_user_empty, parent, false)
        val holder = EmptyViewHolder(view)
        holder.rootLayout.setOnClickListener {
            val position = holder.adapterPosition
        }
        return holder
    }

    private fun bindEmptyHolder(holder: EmptyViewHolder) {
        holder.description.text = "暂无数据"
    }

    private fun bindUserHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.nickname.text = user.nickname
        holder.description.text = user.description
    }

    private class EmptyViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val rootLayout: LinearLayout = view as LinearLayout
        val description: TextView = view.findViewById(R.id.description)

    }

    private class UserViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val rootLayout: LinearLayout = view as LinearLayout
        var nickname: TextView = view.findViewById(R.id.nickname)
        val description: TextView = view.findViewById(R.id.description)

    }

    /**
     * 获取RecyclerView数据源中元素的数量。
     * @return RecyclerView数据源中元素的数量。
     */
    private val dataItemCount: Int
        get() = userList.size

    /**
     * 根据位置返回不同的view type。
     */
    override fun getItemViewType(position: Int): Int {
        return if (dataItemCount > 1) {
            TYPE_USERS
        } else TYPE_EMPTY
    }

    companion object {

        private const val TAG = "UserAdapter"

        private const val TYPE_USERS = 0//正常数据

        private const val TYPE_EMPTY = 1//空
    }

}