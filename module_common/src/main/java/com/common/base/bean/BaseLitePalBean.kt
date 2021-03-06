/*
 * Copyright (C) tanlifei, Suzhou Quxiang Inc. Open source codes for study only.
 * Do not use for commercial purpose.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.common.base.bean

import org.litepal.crud.LitePalSupport

/**
 * 所有网络通讯数据模型实体类的基类。
 * LitePalSupport 为数据库，litepal.xml中添加模型即保存到数据库
 *
 * @author tanlifei
 * @since 2020/7/20
 */
abstract class BaseLitePalBean : LitePalSupport() {

    /**
     * 获取当前实体类的实体数据id。比如User类就获取userId，Comment类就获取commentId。
     * @return 当前实体类的实体数据id。
     */
    abstract val modelId: Long

}