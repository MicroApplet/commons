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
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 国籍
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Getter
@AllArgsConstructor
public enum CountryOrDict {
    // 亚洲
    China("0086", "CHN", "China", "China", "中国"),
    ChinaHongKong("00852", "HKG", "HongKong", "Hong Kong of China", "香港"),
    ChinaMacau("00853", "MAC", "Macau", "Macau of China", "澳门"),
    ChinaTaiwan("00886", "TWN", "Taiwan", "Taiwan of China", "台湾"),
    Afghanistan("0093", "AFG", "Afghanistan", "Afghanistan", "阿富汗"),
    Armenia("00374", "ARM", "Armenia", "Armenia", "亚美尼亚"),
    Azerbaijan("00994", "AZE", "Azerbaijan", "Azerbaijan", "阿塞拜疆"),
    Bahrain("00973", "BHR", "Bahrain", "Bahrain", "巴林"),
    Bangladesh("00880", "BGD", "Bangladesh", "Bangladesh", "孟加拉国"),
    Bhutan("00975", "BTN", "Bhutan", "Bhutan", "不丹"),
    Brunei("00673", "BRN", "Brunei", "Brunei", "文莱"),
    Cambodia("00855", "KHM", "Cambodia", "Cambodia", "柬埔寨"),
    India("0091", "IND", "India", "India", "印度"),
    Indonesia("0062", "IDN", "Indonesia", "Indonesia", "印度尼西亚"),
    Iran("0098", "IRN", "Iran", "Iran", "伊朗"),
    Iraq("00964", "IRQ", "Iraq", "Iraq", "伊拉克"),
    Israel("00972", "ISR", "Israel", "Israel", "以色列"),
    Japan("0081", "JPN", "Japan", "Japan", "日本"),
    Jordan("00962", "JOR", "Jordan", "Jordan", "约旦"),
    Kazakhstan("0076", "KAZ", "Kazakhstan", "Kazakhstan", "哈萨克斯坦"),
    Kuwait("00965", "KWT", "Kuwait", "Kuwait", "科威特"),
    Kyrgyzstan("00996", "KGZ", "Kyrgyzstan", "Kyrgyzstan", "吉尔吉斯斯坦"),
    Laos("00856", "LAO", "Laos", "Laos", "老挝"),
    Lebanon("00961", "LBN", "Lebanon", "Lebanon", "黎巴嫩"),
    Malaysia("0060", "MYS", "Malaysia", "Malaysia", "马来西亚"),
    Maldives("00960", "MDV", "Maldives", "Maldives", "马尔代夫"),
    Mongolia("00976", "MNG", "Mongolia", "Mongolia", "蒙古"),
    Myanmar("0095", "MMR", "Myanmar", "Myanmar", "缅甸"),
    Nepal("00977", "NPL", "Nepal", "Nepal", "尼泊尔"),
    NorthKorea("00850", "PRK", "North Korea", "North Korea", "朝鲜"),
    Oman("00968", "OMN", "Oman", "Oman", "阿曼"),
    Pakistan("0092", "PAK", "Pakistan", "Pakistan", "巴基斯坦"),
    Palestine("00970", "PSE", "Palestine", "Palestine", "巴勒斯坦"),
    Philippines("0063", "PHL", "Philippines", "Philippines", "菲律宾"),
    Qatar("00974", "QAT", "Qatar", "Qatar", "卡塔尔"),
    Russia("007", "RUS", "Russia", "Russia", "俄罗斯"),
    SaudiArabia("00966", "SAU", "Saudi Arabia", "Saudi Arabia", "沙特阿拉伯"),
    Singapore("0065", "SGP", "Singapore", "Singapore", "新加坡"),
    SouthKorea("0082", "KOR", "South Korea", "South Korea", "韩国"),
    SriLanka("0094", "LKA", "Sri Lanka", "Sri Lanka", "斯里兰卡"),
    Syria("00963", "SYR", "Syria", "Syria", "叙利亚"),
    Tajikistan("00992", "TJK", "Tajikistan", "Tajikistan", "塔吉克斯坦"),
    Thailand("0066", "THA", "Thailand", "Thailand", "泰国"),
    TimorLeste("00670", "TLS", "Timor-Leste", "Timor-Leste", "东帝汶"),
    Turkey("0090", "TUR", "Turkey", "Turkey", "土耳其"),
    Turkmenistan("00993", "TKM", "Turkmenistan", "Turkmenistan", "土库曼斯坦"),
    UnitedArabEmirates("00971", "ARE", "United Arab Emirates", "United Arab Emirates", "阿拉伯联合酋长国"),
    Uzbekistan("00998", "UZB", "Uzbekistan", "Uzbekistan", "乌兹别克斯坦"),
    Vietnam("0084", "VNM", "Vietnam", "Vietnam", "越南"),
    Yemen("00967", "YEM", "Yemen", "Yemen", "也门"),

    // 非洲
    Algeria("00213", "DZA", "Algeria", "Algeria", "阿尔及利亚"),
    Angola("00244", "AGO", "Angola", "Angola", "安哥拉"),
    Benin("00229", "BEN", "Benin", "Benin", "贝宁"),
    Botswana("00267", "BWA", "Botswana", "Botswana", "博茨瓦纳"),
    BurkinaFaso("00226", "BFA", "Burkina Faso", "Burkina Faso", "布基纳法索"),
    Burundi("00257", "BDI", "Burundi", "Burundi", "布隆迪"),
    Cameroon("00237", "CMR", "Cameroon", "Cameroon", "喀麦隆"),
    CapeVerde("00238", "CPV", "Cape Verde", "Cape Verde", "佛得角"),
    CentralAfricanRepublic("00236", "CAF", "Central African Republic", "Central African Republic", "中非共和国"),
    Chad("00235", "TCD", "Chad", "Chad", "乍得"),
    Comoros("00269", "COM", "Comoros", "Comoros", "科摩罗"),
    Congo("00242", "COG", "Congo", "Congo", "刚果（布）"),
    CongoDR("00243", "COD", "Congo (DRC)", "Congo (DRC)", "刚果（金）"),
    CoteDIvoire("00225", "CIV", "Côte d'Ivoire", "Côte d'Ivoire", "科特迪瓦"),
    Djibouti("00253", "DJI", "Djibouti", "Djibouti", "吉布提"),
    Egypt("0020", "EGY", "Egypt", "Egypt", "埃及"),
    EquatorialGuinea("00240", "GNQ", "Equatorial Guinea", "Equatorial Guinea", "赤道几内亚"),
    Eritrea("00291", "ERI", "Eritrea", "Eritrea", "厄立特里亚"),
    Eswatini("00268", "SWZ", "Eswatini", "Eswatini", "斯威士兰"),
    Ethiopia("00251", "ETH", "Ethiopia", "Ethiopia", "埃塞俄比亚"),
    Gabon("00241", "GAB", "Gabon", "Gabon", "加蓬"),
    Gambia("00220", "GMB", "Gambia", "Gambia", "冈比亚"),
    Ghana("00233", "GHA", "Ghana", "Ghana", "加纳"),
    Guinea("00224", "GIN", "Guinea", "Guinea", "几内亚"),
    GuineaBissau("00245", "GNB", "Guinea-Bissau", "Guinea-Bissau", "几内亚比绍"),
    Kenya("00254", "KEN", "Kenya", "Kenya", "肯尼亚"),
    Lesotho("00266", "LSO", "Lesotho", "Lesotho", "莱索托"),
    Liberia("00231", "LBR", "Liberia", "Liberia", "利比里亚"),
    Libya("00218", "LBY", "Libya", "Libya", "利比亚"),
    Madagascar("00261", "MDG", "Madagascar", "Madagascar", "马达加斯加"),
    Malawi("00265", "MWI", "Malawi", "Malawi", "马拉维"),
    Mali("00223", "MLI", "Mali", "Mali", "马里"),
    Mauritania("00222", "MRT", "Mauritania", "Mauritania", "毛里塔尼亚"),
    Mauritius("00230", "MUS", "Mauritius", "Mauritius", "毛里求斯"),
    Morocco("00212", "MAR", "Morocco", "Morocco", "摩洛哥"),
    Mozambique("00258", "MOZ", "Mozambique", "Mozambique", "莫桑比克"),
    Namibia("00264", "NAM", "Namibia", "Namibia", "纳米比亚"),
    Niger("00227", "NER", "Niger", "Niger", "尼日尔"),
    Nigeria("00234", "NGA", "Nigeria", "Nigeria", "尼日利亚"),
    Rwanda("00250", "RWA", "Rwanda", "Rwanda", "卢旺达"),
    SaoTomePrincipe("00239", "STP", "São Tomé and Príncipe", "São Tomé and Príncipe", "圣多美和普林西比"),
    Senegal("00221", "SEN", "Senegal", "Senegal", "塞内加尔"),
    Seychelles("00248", "SYC", "Seychelles", "Seychelles", "塞舌尔"),
    SierraLeone("00232", "SLE", "Sierra Leone", "Sierra Leone", "塞拉利昂"),
    Somalia("00252", "SOM", "Somalia", "Somalia", "索马里"),
    SouthAfrica("0027", "ZAF", "South Africa", "South Africa", "南非"),
    SouthSudan("00211", "SSD", "South Sudan", "South Sudan", "南苏丹"),
    Sudan("00249", "SDN", "Sudan", "Sudan", "苏丹"),
    Tanzania("00255", "TZA", "Tanzania", "Tanzania", "坦桑尼亚"),
    Togo("00228", "TGO", "Togo", "Togo", "多哥"),
    Tunisia("00216", "TUN", "Tunisia", "Tunisia", "突尼斯"),
    Uganda("00256", "UGA", "Uganda", "Uganda", "乌干达"),
    Zambia("00260", "ZMB", "Zambia", "Zambia", "赞比亚"),
    Zimbabwe("00263", "ZWE", "Zimbabwe", "Zimbabwe", "津巴布韦"),

    // 北美洲
    AntiguaBarbuda("001268", "ATG", "Antigua and Barbuda", "Antigua and Barbuda", "安提瓜和巴布达"),
    Bahamas("001242", "BHS", "Bahamas", "Bahamas", "巴哈马"),
    Barbados("001246", "BRB", "Barbados", "Barbados", "巴巴多斯"),
    Belize("00501", "BLZ", "Belize", "Belize", "伯利兹"),
    Canada("001", "CAN", "Canada", "Canada", "加拿大"),
    CostaRica("00506", "CRI", "Costa Rica", "Costa Rica", "哥斯达黎加"),
    Cuba("0053", "CUB", "Cuba", "Cuba", "古巴"),
    Dominica("001767", "DMA", "Dominica", "Dominica", "多米尼加"),
    DominicanRepublic("001809", "DOM", "Dominican Republic", "Dominican Republic", "多米尼加共和国"),
    ElSalvador("00503", "SLV", "El Salvador", "El Salvador", "萨尔瓦多"),
    Greenland("00299", "GRL", "Greenland", "Greenland", "格陵兰"),
    Grenada("001473", "GRD", "Grenada", "Grenada", "格林纳达"),
    Guatemala("00502", "GTM", "Guatemala", "Guatemala", "危地马拉"),
    Haiti("00509", "HTI", "Haiti", "Haiti", "海地"),
    Honduras("00504", "HND", "Honduras", "Honduras", "洪都拉斯"),
    Jamaica("001876", "JAM", "Jamaica", "Jamaica", "牙买加"),
    Mexico("0052", "MEX", "Mexico", "Mexico", "墨西哥"),
    Nicaragua("00505", "NIC", "Nicaragua", "Nicaragua", "尼加拉瓜"),
    Panama("00507", "PAN", "Panama", "Panama", "巴拿马"),
    SaintKittsNevis("001869", "KNA", "Saint Kitts and Nevis", "Saint Kitts and Nevis", "圣基茨和尼维斯"),
    SaintLucia("001758", "LCA", "Saint Lucia", "Saint Lucia", "圣卢西亚"),
    SaintVincentGrenadines("001784", "VCT", "Saint Vincent and the Grenadines", "Saint Vincent and the Grenadines", "圣文森特和格林纳丁斯"),
    TrinidadTobago("001868", "TTO", "Trinidad and Tobago", "Trinidad and Tobago", "特立尼达和多巴哥"),
    UnitedStates("001", "USA", "United States", "United States", "美国"),

    // 南美洲
    Argentina("0054", "ARG", "Argentina", "Argentina", "阿根廷"),
    Bolivia("00591", "BOL", "Bolivia", "Bolivia", "玻利维亚"),
    Brazil("0055", "BRA", "Brazil", "Brazil", "巴西"),
    Chile("0056", "CHL", "Chile", "Chile", "智利"),
    Colombia("0057", "COL", "Colombia", "Colombia", "哥伦比亚"),
    Ecuador("00593", "ECU", "Ecuador", "Ecuador", "厄瓜多尔"),
    FalklandIslands("00500", "FLK", "Falkland Islands", "Falkland Islands", "福克兰群岛"),
    FrenchGuiana("00594", "GUF", "French Guiana", "French Guiana", "法属圭亚那"),
    Guyana("00592", "GUY", "Guyana", "Guyana", "圭亚那"),
    Paraguay("00595", "PRY", "Paraguay", "Paraguay", "巴拉圭"),
    Peru("0051", "PER", "Peru", "Peru", "秘鲁"),
    Suriname("00597", "SUR", "Suriname", "Suriname", "苏里南"),
    Uruguay("00598", "URY", "Uruguay", "Uruguay", "乌拉圭"),
    Venezuela("0058", "VEN", "Venezuela", "Venezuela", "委内瑞拉"),

    // 欧洲
    Albania("00355", "ALB", "Albania", "Albania", "阿尔巴尼亚"),
    Andorra("00376", "AND", "Andorra", "Andorra", "安道尔"),
    Austria("0043", "AUT", "Austria", "Austria", "奥地利"),
    Belarus("00375", "BLR", "Belarus", "Belarus", "白俄罗斯"),
    Belgium("0032", "BEL", "Belgium", "Belgium", "比利时"),
    BosniaHerzegovina("00387", "BIH", "Bosnia and Herzegovina", "Bosnia and Herzegovina", "波斯尼亚和黑塞哥维那"),
    Bulgaria("00359", "BGR", "Bulgaria", "Bulgaria", "保加利亚"),
    Croatia("00385", "HRV", "Croatia", "Croatia", "克罗地亚"),
    Cyprus("00357", "CYP", "Cyprus", "Cyprus", "塞浦路斯"),
    CzechRepublic("00420", "CZE", "Czech Republic", "Czech Republic", "捷克"),
    Denmark("0045", "DNK", "Denmark", "Denmark", "丹麦"),
    Estonia("00372", "EST", "Estonia", "Estonia", "爱沙尼亚"),
    Finland("00358", "FIN", "Finland", "Finland", "芬兰"),
    France("0033", "FRA", "France", "France", "法国"),
    Georgia("00995", "GEO", "Georgia", "Georgia", "格鲁吉亚"),
    Germany("0049", "DEU", "Germany", "Germany", "德国"),
    Greece("0030", "GRC", "Greece", "Greece", "希腊"),
    Hungary("0036", "HUN", "Hungary", "Hungary", "匈牙利"),
    Iceland("00354", "ISL", "Iceland", "Iceland", "冰岛"),
    Ireland("00353", "IRL", "Ireland", "Ireland", "爱尔兰"),
    Italy("0039", "ITA", "Italy", "Italy", "意大利"),
    Kosovo("00383", "KOS", "Kosovo", "Kosovo", "科索沃"),
    Latvia("00371", "LVA", "Latvia", "Latvia", "拉脱维亚"),
    Liechtenstein("00423", "LIE", "Liechtenstein", "Liechtenstein", "列支敦士登"),
    Lithuania("00370", "LTU", "Lithuania", "Lithuania", "立陶宛"),
    Luxembourg("00352", "LUX", "Luxembourg", "Luxembourg", "卢森堡"),
    Malta("00356", "MLT", "Malta", "Malta", "马耳他"),
    Moldova("00373", "MDA", "Moldova", "Moldova", "摩尔多瓦"),
    Monaco("00377", "MCO", "Monaco", "Monaco", "摩纳哥"),
    Montenegro("00382", "MNE", "Montenegro", "Montenegro", "黑山"),
    Netherlands("0031", "NLD", "Netherlands", "Netherlands", "荷兰"),
    NorthMacedonia("00389", "MKD", "North Macedonia", "North Macedonia", "北马其顿"),
    Norway("0047", "NOR", "Norway", "Norway", "挪威"),
    Poland("0048", "POL", "Poland", "Poland", "波兰"),
    Portugal("00351", "PRT", "Portugal", "Portugal", "葡萄牙"),
    Romania("0040", "ROU", "Romania", "Romania", "罗马尼亚"),
    SanMarino("00378", "SMR", "San Marino", "San Marino", "圣马力诺"),
    Serbia("00381", "SRB", "Serbia", "Serbia", "塞尔维亚"),
    Slovakia("00421", "SVK", "Slovakia", "Slovakia", "斯洛伐克"),
    Slovenia("00386", "SVN", "Slovenia", "Slovenia", "斯洛文尼亚"),
    Spain("0034", "ESP", "Spain", "Spain", "西班牙"),
    Sweden("0046", "SWE", "Sweden", "Sweden", "瑞典"),
    Switzerland("0041", "CHE", "Switzerland", "Switzerland", "瑞士"),
    Ukraine("00380", "UKR", "Ukraine", "Ukraine", "乌克兰"),
    UnitedKingdom("0044", "GBR", "United Kingdom", "United Kingdom", "英国"),

    // 大洋洲
    Australia("0061", "AUS", "Australia", "Australia", "澳大利亚"),
    Fiji("00679", "FJI", "Fiji", "Fiji", "斐济"),
    Kiribati("00686", "KIR", "Kiribati", "Kiribati", "基里巴斯"),
    MarshallIslands("00692", "MHL", "Marshall Islands", "Marshall Islands", "马绍尔群岛"),
    Micronesia("00691", "FSM", "Micronesia", "Micronesia", "密克罗尼西亚"),
    Nauru("00674", "NRU", "Nauru", "Nauru", "瑙鲁"),
    NewZealand("0064", "NZL", "New Zealand", "New Zealand", "新西兰"),
    Palau("00680", "PLW", "Palau", "Palau", "帕劳"),
    PapuaNewGuinea("00675", "PNG", "Papua New Guinea", "Papua New Guinea", "巴布亚新几内亚"),
    Samoa("00685", "WSM", "Samoa", "Samoa", "萨摩亚"),
    SolomonIslands("00677", "SLB", "Solomon Islands", "Solomon Islands", "所罗门群岛"),
    Tonga("00676", "TON", "Tonga", "Tonga", "汤加"),
    Tuvalu("00688", "TUV", "Tuvalu", "Tuvalu", "图瓦卢"),
    Vanuatu("00678", "VUT", "Vanuatu", "Vanuatu", "瓦努阿图"),

    // 地区

    PuertoRico("001787", "PRI", "Puerto Rico", "Puerto Rico", "波多黎各"),
    FrenchPolynesia("00689", "PYF", "French Polynesia", "French Polynesia", "法属波利尼西亚"),
    NewCaledonia("00687", "NCL", "New Caledonia", "New Caledonia", "新喀里多尼亚"),
    Mayotte("00262", "MYT", "Mayotte", "Mayotte", "马约特"),
    Reunion("00262", "REU", "Réunion", "Réunion", "留尼汪"),
    Guadeloupe("00590", "GLP", "Guadeloupe", "Guadeloupe", "瓜德罗普"),
    Martinique("00596", "MTQ", "Martinique", "Martinique", "马提尼克"),
    SaintBarthelemy("00590", "BLM", "Saint Barthélemy", "Saint Barthélemy", "圣巴泰勒米"),
    SaintPierreMiquelon("00509", "SPM", "Saint Pierre and Miquelon", "Saint Pierre and Miquelon", "圣皮埃尔和密克隆"),
    WallisFutuna("00681", "WLF", "Wallis and Futuna", "Wallis and Futuna", "瓦利斯和富图纳"),
    Anguilla("001264", "AIA", "Anguilla", "Anguilla", "安圭拉"),
    Bermuda("001441", "BMU", "Bermuda", "Bermuda", "百慕大"),
    BritishVirginIslands("001284", "VGB", "British Virgin Islands", "British Virgin Islands", "英属维尔京群岛"),
    CaymanIslands("001345", "CYM", "Cayman Islands", "Cayman Islands", "开曼群岛"),
    Gibraltar("00350", "GIB", "Gibraltar", "Gibraltar", "直布罗陀"),
    TurksCaicosIslands("001649", "TCA", "Turks and Caicos Islands", "Turks and Caicos Islands", "特克斯和凯科斯群岛"),
    Aruba("00297", "ABW", "Aruba", "Aruba", "阿鲁巴"),
    Curacao("00599", "CUW", "Curaçao", "Curaçao", "库拉索"),
    SintMaarten("001721", "SXM", "Sint Maarten", "Sint Maarten", "圣马丁"),
    FaroeIslands("00298", "FRO", "Faroe Islands", "Faroe Islands", "法罗群岛"),
    Guernsey("0044", "GGY", "Guernsey", "Guernsey", "根西岛"),
    IsleOfMan("0044", "IMN", "Isle of Man", "Isle of Man", "马恩岛"),
    Jersey("0044", "JEY", "Jersey", "Jersey", "泽西岛"),
    Alderney("0044", "ALD", "Alderney", "Alderney", "奥尔德尼岛"),
    SaintHelena("00290", "SHN", "Saint Helena", "Saint Helena", "圣赫勒拿岛"),
    AscensionIsland("00247", "ACU", "Ascension Island", "Ascension Island", "阿森松岛"),
    TristanDaCunha("00290", "TDA", "Tristan da Cunha", "Tristan da Cunha", "特里斯坦-达库尼亚岛"),
    AkrotiriSovereignBaseArea("00357", "SBA", "Akrotiri Sovereign Base Area", "Akrotiri Sovereign Base Area", "阿克罗蒂里主权基地"),
    DhekeliaSovereignBaseArea("00357", "SBD", "Dhekelia Sovereign Base Area", "Dhekelia Sovereign Base Area", "德凯利亚主权基地"),
    ChristmasIsland("0061", "CXR", "Christmas Island", "Christmas Island", "圣诞岛"),
    CocosIslands("0061", "CCK", "Cocos (Keeling) Islands", "Cocos (Keeling) Islands", "科科斯群岛"),
    NorfolkIsland("00672", "NFK", "Norfolk Island", "Norfolk Island", "诺福克岛"),
    AshmoreCartierIslands("0061", "ATC", "Ashmore and Cartier Islands", "Ashmore and Cartier Islands", "阿什莫尔和卡捷群岛"),
    CoralSeaIslands("0061", "CSI", "Coral Sea Islands", "Coral Sea Islands", "珊瑚海岛屿"),
    HeardMcDonaldIslands("0061", "HMD", "Heard Island and McDonald Islands", "Heard Island and McDonald Islands", "赫德岛和麦克唐纳群岛"),

    // 其他
    Antarctica("00672", "ATA", "Antarctica", "Antarctica", "南极洲"),
    BritishIndianOceanTerritory("00246", "IOT", "British Indian Ocean Territory", "British Indian Ocean Territory", "英属印度洋领地"),
    CookIslands("00682", "COK", "Cook Islands", "Cook Islands", "库克群岛"),
    Niue("00683", "NIU", "Niue", "Niue", "纽埃"),
    Tokelau("00690", "TKL", "Tokelau", "Tokelau", "托克劳"),
    AmericanSamoa("001684", "ASM", "American Samoa", "American Samoa", "美属萨摩亚"),
    Guam("001671", "GUM", "Guam", "Guam", "关岛"),
    NorthernMarianaIslands("001670", "MNP", "Northern Mariana Islands", "Northern Mariana Islands", "北马里亚纳群岛"),
    USVirginIslands("001340", "VIR", "U.S. Virgin Islands", "U.S. Virgin Islands", "美属维尔京群岛"),
    Other("--","Other","Other","Other","其他");

    /**
     * 国籍/地区代码
     */
    private final String code;
    /**
     * 缩写
     */
    private final String abbr;
    /**
     * 名称
     */
    private final String name;
    /**
     * 英文名
     */
    private final String enName;
    /**
     * 中文名
     */
    private final String cnName;

    public static CountryOrDict codeOf(String code){
        return Arrays.stream(values()).filter(item -> StringUtils.equals(code, item.getCode())).findFirst().orElse(Other);
    }

    public static CountryOrDict abbrOf(String abbr){
        return Arrays.stream(values()).filter(item -> StringUtils.equals(item.getAbbr(), abbr)).findFirst().orElse(Other);
    }

    public static CountryOrDict cnNameOf(String cnName){
        return Arrays.stream(values()).filter(item -> StringUtils.equals(cnName, item.getCnName())).findFirst().orElse(Other);
    }
}