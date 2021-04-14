package com.common.base.ui.activity

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.viewmodel.EmptyViewModel
import com.common.constant.GlobalConst
import com.common.databinding.ActivityBaseWebBinding
import com.common.widget.component.extension.setVisible
import com.common.widget.component.extension.startActivity

/**
 * @desc:公用的H5显示基类
 * @author: tanlifei
 * @date: 2021/1/27 16:14
 */
class BaseWebViewActivity : BaseToolBarActivity<ActivityBaseWebBinding, EmptyViewModel>() {

    lateinit var url: String
    var title: String? = null

    companion object {
        fun actionStart(title: String?, url: String) {
            startActivity<BaseWebViewActivity> {
                putExtra(GlobalConst.Extras.URL, url)
                putExtra(GlobalConst.Extras.TITLE, title)
            }
        }
    }

    override fun createViewModel(): EmptyViewModel {
        return EmptyViewModel()
    }

    override fun init() {
        var bundle = this.intent.extras
        url = bundle?.get(GlobalConst.Extras.URL).toString()
        title = bundle?.get(GlobalConst.Extras.TITLE).toString()
        if (ObjectUtils.isNotEmpty(title)) {
            mTitleBar.title = title
        }
        initWebSettings()
        initWebViewClient()
    }

    private fun initWebSettings() {
        val settings: WebSettings = mBinding.webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.blockNetworkImage = false // 解决图片不显示
        mBinding.webView.loadUrl(url)
    }

    private fun initWebViewClient() {
        mBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                httpUrl: String
            ): Boolean {
                if (!httpUrl.startsWith("http")) {
                    try {
                        // 以下固定写法,表示跳转到第三方应用
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl))
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    } catch (e: Exception) {
                        // 防止没有安装的情况
                        e.printStackTrace()
                    }
                    return true
                }
                return false
            }

            override fun onPageFinished(view: WebView, httpUrl: String) {
                super.onPageFinished(view, httpUrl)
                if (TextUtils.isEmpty(title) && ObjectUtils.isNotEmpty(view.title)) {
                    mTitleBar.title = view.title
                }
            }
        }
        mBinding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                mBinding.progressBar.setVisible(newProgress != 100)
                if (newProgress == 100) {
                    mBinding.progressBar.progress = 0
                } else {
                    mBinding.progressBar.progress = newProgress
                }
            }

            override fun onReceivedTitle(view: WebView, title: String) {}
        }
        //链接中有需要跳转下载的链接时跳转浏览器下载

        mBinding.webView.setDownloadListener { url, _, _, _, _ ->
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = Uri.parse(url)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = uri
            startActivity(intent)
        }

    }


}