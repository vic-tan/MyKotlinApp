package com.tanlifei.app.common.widget.video

import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import cn.jzvd.JZMediaInterface
import cn.jzvd.Jzvd
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.IjkTimedText
import java.io.IOException

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/31 11:19
 */
class JZMediaIjk(jzvd: Jzvd) : JZMediaInterface(jzvd), IMediaPlayer.OnPreparedListener,
    IMediaPlayer.OnVideoSizeChangedListener,
    IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener,
    IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener,
    IMediaPlayer.OnTimedTextListener {
    var ijkMediaPlayer: IjkMediaPlayer? = null

    override fun start() {
        if (ijkMediaPlayer != null) ijkMediaPlayer!!.start()
    }

    override fun prepare() {
        release()
        mMediaHandlerThread = HandlerThread("JZVD")
        mMediaHandlerThread.start()
        mMediaHandler = Handler(mMediaHandlerThread.looper) //主线程还是非主线程，就在这里

        handler = Handler()

        mMediaHandler.post {
            ijkMediaPlayer = IjkMediaPlayer()
            ijkMediaPlayer?.let {
                it.apply {
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    ////1为硬解 0为软解
                    setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0)
                    setOption(
                        IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                        "mediacodec-auto-rotate",
                        1
                    )
                    setOption(
                        IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                        "mediacodec-handle-resolution-change",
                        1
                    )
                    //使用opensles把文件从java层拷贝到native层
                    setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0)
                    //视频格式
                    setOption(
                        IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                        "overlay-format",
                        IjkMediaPlayer.SDL_FCC_RV32.toLong()
                    )
                    //跳帧处理（-1~120）。CPU处理慢时，进行跳帧处理，保证音视频同步
                    setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1)
                    //0为一进入就播放,1为进入时不播放
                    setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)
                    ////域名检测
                    setOption(
                        IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                        "http-detect-range-support",
                        0
                    )
                    //设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
                    setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48)
                    //最大缓冲大小,单位kb
                    setOption(
                        IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                        "max-buffer-size",
                        1024 * 1024.toLong()
                    )
                    //某些视频在SeekTo的时候，会跳回到拖动前的位置，这是因为视频的关键帧的问题，通俗一点就是FFMPEG不兼容，视频压缩过于厉害，seek只支持关键帧，出现这个情况就是原始的视频文件中i 帧比较少
                    setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)
                    //是否重连
                    setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1)
                    //http重定向https
                    setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1)
                    //设置seekTo能够快速seek到指定位置并播放
                    setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "fastseek")
                    //播放前的探测Size，默认是1M, 改小一点会出画面更快
                    setOption(
                        IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                        "probesize",
                        1024 * 10.toLong()
                    )
                    //1变速变调状态 0变速不变调状态
                    setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1)
                    setOnPreparedListener(this@JZMediaIjk)
                    setOnVideoSizeChangedListener(this@JZMediaIjk)
                    setOnCompletionListener(this@JZMediaIjk)
                    setOnErrorListener(this@JZMediaIjk)
                    setOnInfoListener(this@JZMediaIjk)
                    setOnBufferingUpdateListener(this@JZMediaIjk)
                    setOnSeekCompleteListener(this@JZMediaIjk)
                    setOnTimedTextListener(this@JZMediaIjk)
                    try {
                        dataSource = jzvd.jzDataSource.currentUrl.toString()
                        setAudioStreamType(AudioManager.STREAM_MUSIC)
                        setScreenOnWhilePlaying(true)
                        prepareAsync()
                        setSurface(Surface(jzvd.textureView.surfaceTexture))
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

        }
    }

    override fun pause() {
        ijkMediaPlayer?.let {
            pause()
        }
    }

    override fun isPlaying(): Boolean {
        return if (null != ijkMediaPlayer) {
            isPlaying
        } else false
    }

    override fun seekTo(time: Long) {
        try {
            ijkMediaPlayer?.let {
                seekTo(time)
            }
        } catch (e: Exception) {
        }
    }

    override fun release() {
        if (mMediaHandler != null && mMediaHandlerThread != null && ijkMediaPlayer != null) { //不知道有没有妖孽
            val tmpHandlerThread = mMediaHandlerThread
            val tmpMediaPlayer = ijkMediaPlayer
            SAVED_SURFACE = null
            mMediaHandler.post {
                if (null != tmpMediaPlayer) {
                    tmpMediaPlayer.setSurface(null)
                    tmpMediaPlayer.release()
                    tmpHandlerThread.quit()
                }
            }
            ijkMediaPlayer = null
        }
    }

    override fun getCurrentPosition(): Long {
        return if (null != ijkMediaPlayer) {
            ijkMediaPlayer!!.currentPosition
        } else {
            0
        }
    }

    override fun getDuration(): Long {
        return if (ijkMediaPlayer == null) 0 else ijkMediaPlayer!!.duration
    }

    override fun setVolume(leftVolume: Float, rightVolume: Float) {
        ijkMediaPlayer?.let {
            setVolume(leftVolume, rightVolume)
        }
    }

    override fun setSpeed(speed: Float) {
        ijkMediaPlayer?.let {
            setSpeed(speed)
        }
    }

    override fun onPrepared(iMediaPlayer: IMediaPlayer?) {
        handler.post { jzvd.onPrepared() }
    }

    override fun onVideoSizeChanged(
        iMediaPlayer: IMediaPlayer,
        i: Int,
        i1: Int,
        i2: Int,
        i3: Int
    ) {
        handler.post {
            jzvd.onVideoSizeChanged(
                iMediaPlayer.videoWidth,
                iMediaPlayer.videoHeight
            )
        }
    }

    override fun onError(iMediaPlayer: IMediaPlayer?, what: Int, extra: Int): Boolean {
        handler.post { jzvd.onError(what, extra) }
        return true
    }

    override fun onInfo(iMediaPlayer: IMediaPlayer?, what: Int, extra: Int): Boolean {
        handler.post { jzvd.onInfo(what, extra) }
        return false
    }

    override fun onBufferingUpdate(iMediaPlayer: IMediaPlayer?, percent: Int) {
        handler.post { jzvd.setBufferProgress(percent) }
    }

    override fun onSeekComplete(iMediaPlayer: IMediaPlayer?) {
        handler.post { jzvd.onSeekComplete() }
    }

    override fun onTimedText(iMediaPlayer: IMediaPlayer?, ijkTimedText: IjkTimedText?) {}

    override fun setSurface(surface: Surface?) {
        ijkMediaPlayer?.let {
            setSurface(surface)
        }
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        if (SAVED_SURFACE == null) {
            SAVED_SURFACE = surface
            prepare()
        } else {
            jzvd.textureView.setSurfaceTexture(SAVED_SURFACE)
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return false
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

    }

    override fun onCompletion(p0: IMediaPlayer?) {
        handler.post { jzvd.onCompletion() }
    }


}