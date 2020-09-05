package com.tanlifei.mykotlinapp.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.tanlifei.mykotlinapp.BaseActivity
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.core.event.MessageEvent
import com.tanlifei.mykotlinapp.core.event.SendTestEvent
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_recycler.*
import kotlinx.android.synthetic.main.activity_recycler.backBtn
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class EventActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        backBtn.setOnClickListener(this)
        gotoSendBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.gotoSendBtn -> ActivityUtils.startActivity(EventSendActivity::class.java)
            R.id.backBtn -> ActivityUtils.finishActivity(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(event: MessageEvent) {
        if(event is SendTestEvent){
            var log = "name = ${event.name},  age =  ${event.age} , commentId = ${event.commentId},  isLogin = ${event.isLogin}, type = ${event.type}"
            ToastUtils.showShort(log)
            LogUtils.dTag(TAG,log)
        }
    }

    /**
     * companion 静态
     * object 与companion 同用为伴生对象为单例，原对象没影响。
     */
    companion object {

        //const 可见性为public final static const 必须修饰val const 只允许在top-level级别和object中声明
        private const val TAG = "tanlifei_log"
    }

}