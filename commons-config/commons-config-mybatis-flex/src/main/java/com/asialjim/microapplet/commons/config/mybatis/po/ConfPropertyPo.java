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

package com.asialjim.microapplet.commons.config.mybatis.po;

import com.asialjim.microapplet.commons.config.core.ConfProperty;
import com.asialjim.microapplet.commons.config.core.ConfType;
import com.asialjim.microapplet.commons.config.core.Env;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 配置信息ORM映射
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/14, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
@Table("sys_conf_property")
public class ConfPropertyPo implements Serializable {
    private static final long serialVersionUID = -2446896856552366694L;

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private String id;

    private String tp;
    private String biz;
    private String bizName;
    private String name;
    private String nameCn;
    private String value;
    private Integer env;
    private String describe;
    private String remark;
    private Integer version;
    private Boolean enable;

    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;

    @Column(onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updateTime;

    public static ConfPropertyPo toPo(ConfProperty body) {
        ConfPropertyPo po = new ConfPropertyPo();
        po.setId(body.getId());
        if (Objects.nonNull(body.getType()))
            po.setTp(body.getType().name());
        po.setBiz(body.getBusiness());
        po.setBizName(body.getBusinessName());
        po.setName(body.getConfCode());
        po.setNameCn(body.getConfCodeName());
        po.setValue(body.getValue());
        if (Objects.nonNull(body.getEnv()))
            po.setEnv(body.getEnv().getCode());
        //noinspection DuplicatedCode
        po.setDescribe(body.getDescribe());
        po.setRemark(body.getRemark());
        po.setVersion(body.getVersion());
        po.setEnable(body.getEnable());
        po.setCreateTime(body.getCreateTime());
        po.setUpdateTime(body.getUpdateTime());
        return po;
    }

    public static ConfProperty fromPo(ConfPropertyPo body) {
        ConfProperty po = new ConfProperty();
        po.setId(body.getId());
        ConfType confType = ConfType.valueOf(body.getTp());
        po.setType(confType);
        po.setBusiness(body.getBiz());
        po.setBusinessName(body.getBizName());
        po.setConfCode(body.getName());
        po.setConfCodeName(body.getNameCn());
        po.setValue(body.getValue());
        Env env = Env.codeOf(body.getEnv());
        po.setEnv(env);
        //noinspection DuplicatedCode
        po.setDescribe(body.getDescribe());
        po.setRemark(body.getRemark());
        po.setVersion(body.getVersion());
        po.setEnable(body.getEnable());
        po.setCreateTime(body.getCreateTime());
        po.setUpdateTime(body.getUpdateTime());
        return po;
    }
}