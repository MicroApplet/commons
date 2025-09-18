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

/**
 * 通用响应代码
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface ResCode {

    int getStatus();

    boolean isThr();

    String getCode();

    String getMsg();

    default<T> Result<T> create(){
        return result(null);
    }
   default<T> Result<T> create(T body){
       return result(body);
   }

    default Result<Void> result() {
        return result(null);
    }

    default <T> Result<T> result(T data) {
        return result(data, null);
    }

    default <T> Result<T> resultErrs(List<?> errs){
        return result(null, errs);
    }

    default <T> Result<T> result(T data, List<?> errs) {
        return new Result<T>().setStatus(getStatus()).setPageable(false).setThr(isThr()).setCode(getCode()).setMsg(getMsg()).setData(data).setErrs(errs);
    }

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

    default RsEx ex() {
        return ex(null);
    }

    default RsEx ex(List<Object> errs) {
        return new RsEx().setStatus(getStatus()).setThr(isThr()).setCode(getCode()).setMsg(getMsg()).setErrs(errs);
    }

    default void thr(){
        ex().cast();
    }

    default void thr(List<Object> errs){
        ex(errs).cast();
    }

}