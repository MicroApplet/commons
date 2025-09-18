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

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 通用响应结果
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/8/22, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
@Accessors(chain = true)
public final class Result<T> implements ResCode, Serializable {
    private int status;
    private boolean thr;
    private boolean pageable;
    private String code;
    private String msg;
    private T data;
    private List<Object> errs;
    private Integer page;
    private Integer size;
    private Integer pages;
    private Integer total;

    public Result<T> setErrs(List<?> errs) {
        this.errs = new ArrayList<>(errs);
        return this;
    }

    public List<Object> getErrs(){
        return Optional.ofNullable(errs).orElseGet(ArrayList::new);
    }

    public static <Q, R> Result<R> result(Result<Q> s, Function<Q, R> f) {
        Result<R> r = new Result<>();
        r.setStatus(s.getStatus());
        r.setPageable(s.isPageable());
        r.setThr(s.isThr());
        r.setCode(s.getCode());
        r.setMsg(s.getMsg());
        r.setData(f.apply(s.getData()));
        r.setErrs(s.getErrs());
        r.setPage(s.getPage());
        r.setSize(s.getSize());
        r.setPages(s.getPages());
        r.setTotal(s.getTotal());
        return r;
    }
}