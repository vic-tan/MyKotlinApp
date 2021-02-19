package com.common.core.base.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.webkit.*
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.ui.viewmodel.EmptyViewModel
import com.common.databinding.ActivityBaseWebBinding

/**
 * @desc:公用的WebView
 * @author: tanlifei
 * @date: 2021/1/27 16:14
 */
class BaseWebViewActivity : BaseToolBarActivity<ActivityBaseWebBinding, EmptyViewModel>() {

    lateinit var url: String
    var title: String? = null

    companion object {
        private const val EXTRAS_URL = "extras_url"
        private const val EXTRAS_TITLE = "extras_title"
        fun actionStart(title: String?, url: String) {
            var intent = Intent(ActivityUtils.getTopActivity(), BaseWebViewActivity::class.java).apply {
                putExtra(EXTRAS_URL, url)
                putExtra(EXTRAS_TITLE, title)
            }
            ActivityUtils.startActivity(intent)
        }
    }

    override fun createViewModel(): EmptyViewModel {
        return EmptyViewModel()
    }

    override fun init() {
        var bundle = this.intent.extras
        url = bundle?.get(EXTRAS_URL).toString()
        title = bundle?.get(EXTRAS_TITLE).toString()
        if (ObjectUtils.isNotEmpty(title)) {
            titleBar.title = title
        }
        initWebSettings()
        initWebViewClient()
    }

    private fun initWebSettings() {
        val settings: WebSettings = binding.webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.blockNetworkImage = false // 解决图片不显示
        binding.webView.loadUrl(url)
    }

    private fun initWebViewClient() {
        binding.webView.webViewClient = object : WebViewClient() {
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
                    titleBar.title = view.title
                }
            }
        }
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBar.progress = 0
                } else {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressBar.progress = newProgress
                }
            }

            override fun onReceivedTitle(view: WebView, title: String) {}
        }
        //链接中有需要跳转下载的链接时跳转浏览器下载

        binding.webView.setDownloadListener { url, _, _, _, _ ->
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = Uri.parse(url)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = uri
            startActivity(intent)
        }

    }


}