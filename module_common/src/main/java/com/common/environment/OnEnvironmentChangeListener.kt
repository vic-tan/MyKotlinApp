package com.common.environment

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/2 16:51
 */
interface OnEnvironmentChangeListener {
    /**
     * 在模块的环境发生变化时调用。
     *
     *
     * Called when a environment of a module has been changed.
     *
     * @param module         环境发生变化的模块
     *
     *
     * Environment changing module
     * @param oldEnvironment 该模块的旧环境
     *
     *
     * The old environment of the module
     * @param newEnvironment 该模块的最新环境
     *
     *
     * The latest environment for this module
     */
    fun onEnvironmentChanged(
        module: ModuleBean,
        oldEnvironment: EnvironmentBean,
        newEnvironment: EnvironmentBean
    )
}