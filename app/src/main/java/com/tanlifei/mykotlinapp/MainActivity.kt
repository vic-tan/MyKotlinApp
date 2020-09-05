package com.tanlifei.mykotlinapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.tanlifei.mykotlinapp.activity.CategoryActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : ActionBarActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryBtn.setOnClickListener(this)
        exitBtn.setOnClickListener(this)
    }

    override fun layoutResId(): Int {
        return R.layout.activity_main
    }

    override fun setToolbar(visibility: Int) {
        super.setToolbar(View.GONE)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.categoryBtn -> {
                LogUtils.dTag(TAG, "categoryBtn")
                var intent = Intent(this, CategoryActivity::class.java)
                var bundle = Bundle();
                bundle.putString("key", "test")
                intent.putExtras(bundle)
                startActivity(intent)
            }
            R.id.exitBtn -> finish()
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
