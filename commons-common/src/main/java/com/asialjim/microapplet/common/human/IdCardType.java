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

package com.asialjim.microapplet.common.human;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 证件类型
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Getter
@AllArgsConstructor
public enum IdCardType {
    ResidentIdentityCard("01", "ResidentIdentityCard", "居民身份证"),
    HouseholdRegister("02", "HouseholdRegister", "户口簿"),
    DriverLicense("03", "DriverLicense", "驾驶证"),
    SocialSecurityCard("04", "SocialSecurityCard", "社会保障卡"),
    MilitaryID("05", "MilitaryID", "军官证"),
    PoliceOfficerID("06", "PoliceOfficerID", "警官证"),
    OfficerID("07", "OfficerID", "公务员证"),
    HongKongMacaoPass("08", "HongKongMacaoPass", "港澳通行证"),
    TaiwanPass("09", "TaiwanPass", "台湾通行证"),
    BorderPass("10", "BorderPass", "边境通行证"),
    SeamanBook("11", "SeamanBook", "海员证"),
    LawyerLicense("12", "LawyerLicense", "律师执业证"),
    ForResCard("13","ForResCard","外国人居住证"),
    Passport("99", "Passport", "护照");

    /**
     * 证件类型代码
     */
    private final String code;

    /**
     * 证件类型英文名
     */
    private final String enName;
    /**
     * 证件类型中文名
     */
    private final String cnName;
}