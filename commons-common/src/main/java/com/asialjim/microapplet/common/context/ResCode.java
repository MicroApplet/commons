/*
 * Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asialjim.microapplet.common.context;

import com.asialjim.microapplet.common.exception.RsEx;

import java.util.List;
import jakarta.validation.Payload;

/**
 * 通用响应代码
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface ResCode {

    /**
     * 获得地位
     *
     * @return int
     */
    int getStatus();

    /**
     * 是用力推
     *
     * @return boolean
     */
    boolean isThr();

    /**
     * 获取代码
     *
     * @return {@link String}
     */
    String getCode();

    /**
     * 得到味精
     *
     * @return {@link String}
     */
    String getMsg();

    /**
     * 创建
     *
     * @return {@link Result<T>}
     */
    default<T> Result<T> create(){
        return result(null);
    }

    /**
     * 创建
     *
     * @param body 身体
     * @return {@link Result<T>}
     */
    default<T> Result<T> create(T body){
       return result(body);
   }

    /**
     * 结果
     *
     * @return {@link Result<Void>}
     */
    default Result<Void> result() {
        return result(null);
    }

    /**
     * 结果
     *
     * @param data 数据
     * @return {@link Result<T>}
     */
    default <T> Result<T> result(T data) {
        return result(data, null);
    }

    /**
     * 结果犯错误
     *
     * @param errs 犯错误
     * @return {@link Result<T>}
     */
    default <T> Result<T> resultErrs(List<?> errs){
        return result(null, errs);
    }

    /**
     * 结果
     *
     * @param data 数据
     * @param errs 犯错误
     * @return {@link Result<T>}
     */
    default <T> Result<T> result(T data, List<?> errs) {
        return new Result<T>().setStatus(getStatus()).setPageable(false).setThr(isThr()).setCode(getCode()).setMsg(getMsg()).setData(data).setErrs(errs);
    }

    /**
     * 页面
     *
     * @param page  页面
     * @param size  大小
     * @param pages 页面
     * @param total 总计
     * @param data  数据
     * @return {@link Result<List<T>>}
     */
    default <T> Result<List<T>> page(int page, int size, int pages, int total, List<T> data) {
        return new Result<List<T>>().setStatus(getStatus())
                .setPageable(true)
                .setThr(isThr())
                .setCode(getCode())
                .setMsg(getMsg())
                .setData(data)
                .setPage(page)
                .setSize(size)
                .setPages(pages)
                .setTotal(total);
    }

    /**
     * 交货
     *
     * @return {@link RsEx}
     */
    default RsEx ex() {
        return ex(null);
    }

    /**
     * 交货
     *
     * @param errs 犯错误
     * @return {@link RsEx}
     */
    default RsEx ex(List<Object> errs) {
        return new RsEx().setStatus(getStatus()).setThr(isThr()).setCode(getCode()).setMsg(getMsg()).setErrs(errs);
    }

    /**
     * 用力推
     *
     */
    default void thr(){
        ex().cast();
    }

    /**
     * 用力推
     *
     * @param errs 犯错误
     */
    default void thr(List<Object> errs){
        ex(errs).cast();
    }

}