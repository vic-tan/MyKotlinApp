package com.tanlifei.mykotlinapp.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.tanlifei.mykotlinapp.BaseActivity
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.core.event.SendTestEvent
import kotlinx.android.synthetic.main.activity_event_send.*
import kotlinx.android.synthetic.main.activity_recycler.*
import kotlinx.android.synthetic.main.activity_recycler.backBtn
import org.greenrobot.eventbus.EventBus

class EventSendActivity: BaseActivity(),View.OnClickListener{



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_send)
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