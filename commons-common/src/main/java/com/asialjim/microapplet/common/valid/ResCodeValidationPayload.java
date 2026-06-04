/*
 *    Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.asialjim.microapplet.common.valid;

import com.asialjim.microapplet.common.context.Res;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Payload;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 从错误中提取错误代码信息
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/22, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Component
public class ResCodeValidationPayload implements Payload {

    /**
     * From bind result
     *
     * @param result 结果
     * @return {@link ErrorInfo}
     */
    public List<ErrorInfo> fromBindResult(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        if (CollectionUtils.isEmpty(fieldErrors))
            return List.of(new ErrorInfo(Res.ParameterValidEx.getCode(), result.getObjectName(), Res.ParameterValidEx.getMsg()));

        return fieldErrors.stream()
                .map(this::fromFieldError)
                .toList();
    }

    /**
     * 从场误差
     *
     * @param error 错误
     * @return {@link ErrorInfo}
     */
    public ErrorInfo fromFieldError(FieldError error) {
        String defaultMessage = error.getDefaultMessage();
        String field = error.getField();
        return new ErrorInfo(code(defaultMessage), field, msg(defaultMessage));

    }

    /**
     * 违反约束
     *
     * @param violation 违反
     * @return {@link ErrorInfo}
     */
    public ErrorInfo fromConstraintViolation(ConstraintViolation<?> violation) {
        if (Objects.isNull(violation))
            return new ErrorInfo(Res.ParameterValidEx.getCode(), "UnKnownField", Res.ParameterValidEx.getMsg());

        String message = violation.getMessage();

        return new ErrorInfo(code(message), violation.getPropertyPath().toString(), msg(message));
    }

    /**
     * 代码
     *
     * @param msg 味精
     * @return {@link String}
     */
    private String code(String msg) {
        if (StringUtils.contains(msg, "|"))
            return StringUtils.split(msg, "|")[0];
        return Res.ParameterValidEx.getCode();
    }

    /**
     * 味精
     *
     * @param msg 味精
     * @return {@link String}
     */
    private String msg(String msg) {
        if (StringUtils.contains(msg, "|"))
            return StringUtils.split(msg, "|")[1];
        return msg;
    }
}