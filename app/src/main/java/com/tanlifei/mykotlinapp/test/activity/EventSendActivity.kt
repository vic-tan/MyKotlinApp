package com.tanlifei.mykotlinapp.test.activity

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.tanlifei.mykotlinapp.common.activity.BaseActivity
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.core.event.SendTestEvent
import kotlinx.android.synthetic.main.test_activity_event.*
import kotlinx.android.synthetic.main.test_activity_event.backBtn
import kotlinx.android.synthetic.main.test_activity_event_send.*
import org.greenrobot.eventbus.EventBus

class EventSendActivity: BaseActivity(),View.OnClickListener{


    override fun layoutResId(): Int {
        return R.layout.test_activity_event_send
    }

    override fun initView() {
        backBtn.setOnClickListener(this)
        sendBtn.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backBtn ->ActivityUtils.finishActivity(this)
            R.id.sendBtn ->{
                var send = SendTestEvent()
                send.name="tanlifei"
                send.age=1
                send.commentId =2000
                send.isLogin = false
                send.type =SendTestEvent.LOGIN_SUCCESS
                EventBus.getDefault().post(send)
                ActivityUtils.finishActivity(this)
            }
        }
    }
}