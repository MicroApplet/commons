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

package com.asialjim.microapplet.web.mvc.page;

import com.asialjim.microapplet.common.page.PageParameter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 基于MVC 的分页参数函数
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
public final class MVCPageParameterFun implements Supplier<PageParameter> {
    public static final Supplier<PageParameter> INSTANCE = new MVCPageParameterFun();

    @Override
    public PageParameter get() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            if (Objects.nonNull(servletRequestAttributes)) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                String page = request.getParameter(PageParameter.pageName);
                String size = request.getParameter(PageParameter.sizeName);
                if (StringUtils.isAnyBlank(page, size))
                    return null;
                long pageNum = NumberUtils.toLong(page);
                long sizeNum = NumberUtils.toLong(size);
                return new PageParameter(pageNum, sizeNum);
            }
        } catch (Throwable ignored){
        }
        return null;
    }
}