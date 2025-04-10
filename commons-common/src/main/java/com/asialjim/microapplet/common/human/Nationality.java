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
 * 民族
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Getter
@AllArgsConstructor
public enum Nationality {
    None("-", "-", "未提供", "-", "未提供"),
    Han("HAN", "Han", "汉族", "CHN", "汉族，中国主体民族，人口约14亿，占中国总人口的91.5%。主要分布在中国大陆，使用汉语。"),
    Uzbek("UZB", "Uzbek", "乌兹别克人", "CHN,UZB", "乌兹别克人，全球约3500万人，主要分布在乌兹别克斯坦，使用乌兹别克语。"),
    Kazakh("KAZ", "Kazakh", "哈萨克人", "CHN,KAZ", "哈萨克人，全球约1800万人，主要分布在哈萨克斯坦，使用哈萨克语。"),
    Korean("KOR", "Korean", "朝鲜族", "CHN,KOR", "朝鲜族，全球约7600万人，主要分布在韩国和朝鲜，使用韩语。"),
    Russian("RUS", "Russian", "俄罗斯人", "CHN,RUS", "俄罗斯人，全球约1.3亿人，主要分布在俄罗斯，使用俄语。"),
    Mongolian("MGL", "Mongolian", "蒙古族", "CHN,MNG", "蒙古族，人口约1000万，主要分布在蒙古国、内蒙古自治区、辽宁、新疆、吉林、黑龙江、青海、河北、河南、甘肃、云南。使用蒙古语。"),
    Tibetan("TIB", "Tibetan", "藏族", "CHN", "藏族，全球约700万人，主要分布在中国西藏自治区和青海、四川等地，使用藏语。"),
    Uyghur("UIG", "Uyghur", "维吾尔族", "CHN", "维吾尔族，全球约1200万人，主要分布在中国新疆，使用维吾尔语。"),
    Zhuang("ZHA", "Zhuang", "壮族", "CHN", "壮族，全球约1800万人，主要分布在中国广西，使用壮语。"),
    Hui("HUI", "Hui", "回族", "CHN", "回族，全球约1100万人，主要分布在中国西北地区，使用汉语。"),
    Tujia("TUJ", "Tujia", "土家族", "CHN", "土家族，全球约900万人，主要分布在中国湖北、湖南和重庆，使用土家语。"),
    Dong("DON", "Dong", "侗族", "CHN", "侗族，全球约300万人，主要分布在中国贵州、湖南和广西，使用侗语。"),
    Yi("YI", "Yi", "彝族", "CHN", "彝族，全球约900万人，主要分布在中国西南地区，使用彝语。"),
    Dai("DAI", "Dai", "傣族", "CHN", "傣族，全球约150万人，主要分布在中国云南，使用傣语。"),
    Bai("BAI", "Bai", "白族", "CHN", "白族，全球约200万人，主要分布在中国云南，使用白语。"),
    Lisu("LSU", "Lisu", "傈僳族", "CHN", "傈僳族，全球约70万人，主要分布在中国云南，使用傈僳语。"),
    Naxi("NAX", "Naxi", "纳西族", "CHN", "纳西族，全球约35万人，主要分布在中国云南，使用纳西语。"),
    Shui("SHU", "Shui", "水族", "CHN", "水族，全球约45万人，主要分布在中国贵州，使用水语。"),
    Yao("YAO", "Yao", "瑶族", "CHN", "瑶族，全球约300万人，主要分布在中国广西、湖南和云南，使用瑶语。"),
    Miao("MIA", "Miao", "苗族", "CHN", "苗族，人口约1107万，主要分布在贵州、云南、湖南、广西、四川、广东、湖北。使用苗语。"),
    Buyei("BUI", "Buyei", "布依族", "CHN", "布依族，人口约350万，主要分布在贵州。使用布依语。"),
    Manchu("MAN", "Manchu", "满族", "CHN", "满族，人口约1042万，主要分布在辽宁、吉林、黑龙江、河北、北京、内蒙古。使用汉语。"),
    Hani("HNI", "Hani", "哈尼族", "CHN", "哈尼族，人口约160万，主要分布在云南。使用哈尼语。"),
    Li("LI", "Li", "黎族", "CHN", "黎族，人口约130万，主要分布在海南。使用黎语。"),
    Va("VA", "Va", "佤族", "CHN", "佤族，人口约43万，主要分布在云南。使用佤语。"),
    She("SHE", "She", "畲族", "CHN", "畲族，人口约75万，主要分布在福建、浙江、江西、广东、安徽。使用畲语。"),
    Gaoshan("GSH", "Gaoshan", "高山族", "CHN", "高山族，人口约3.5万，主要分布在台湾、福建。使用多种南岛语言。"),
    Lahhu("LAH", "Lahhu", "拉祜族", "CHN", "拉祜族，人口约49万，主要分布在云南。使用拉祜语。"),
    Dongxiang("DXX", "Dongxiang", "东乡族", "CHN", "东乡族，人口约77万，主要分布在甘肃、新疆。使用东乡语。"),
    Jingpo("JPO", "Jingpo", "景颇族", "CHN", "景颇族，人口约16万，主要分布在云南。使用景颇语。"),
    Kirgiz("KIR", "Kirgiz", "柯尔克孜族", "CHN", "柯尔克孜族，人口约20万，主要分布在新疆、黑龙江。使用柯尔克孜语。"),
    Tu("TU", "Tu", "土族", "CHN", "土族，人口约28万，主要分布在青海、甘肃。使用土语。"),
    Daur("DAU", "Daur", "达斡尔族", "CHN", "达斡尔族，人口约13万，主要分布在内蒙古、黑龙江、新疆。使用达斡尔语。"),
    Mulao("MUL", "Mulao", "仫佬族", "CHN", "仫佬族，人口约27万，主要分布在广西。使用仫佬语。"),
    Qiang("QIA", "Qiang", "羌族", "CHN", "羌族，人口约31万，主要分布在四川。使用羌语。"),
    Blang("PUB", "Blang", "布朗族", "CHN", "布朗族，人口约12万，主要分布在云南。使用布朗语。"),
    Salar("SAR", "Salar", "撒拉族", "CHN", "撒拉族，人口约16万，主要分布在青海、甘肃。使用撒拉语。"),
    Maonan("MON", "Maonan", "毛南族", "CHN", "毛南族，人口约12万，主要分布在广西。使用毛南语。"),
    Gelao("GLA", "Gelao", "仡佬族", "CHN", "仡佬族，人口约67万，主要分布在贵州、广西、云南。使用仡佬语。"),
    Xibe("XIB", "Xibe", "锡伯族", "CHN", "锡伯族，人口约19万，主要分布在新疆、辽宁、吉林。使用锡伯语。"),
    Achang("ACH", "Achang", "阿昌族", "CHN", "阿昌族，人口约4.3万，主要分布在云南。使用阿昌语。"),
    Pumi("PMI", "Pumi", "普米族", "CHN", "普米族，人口约4.5万，主要分布在云南。使用普米语。"),
    Tajik("TAJ", "Tajik", "塔吉克族", "CHN", "塔吉克族，人口约5万，主要分布在新疆。使用塔吉克语。"),
    Nu("NU", "Nu", "怒族", "CHN", "怒族，人口约3.6万，主要分布在云南。使用怒语。"),
    Evenki("EVE", "Evenki", "鄂温克族", "CHN", "鄂温克族，人口约3.4万，主要分布在内蒙古、黑龙江。使用鄂温克语。"),
    Deang("DEA", "Deang", "德昂族", "CHN", "德昂族，人口约2.2万，主要分布在云南。使用德昂语。"),
    Bonan("BON", "Bonan", "保安族", "CHN", "保安族，人口约2.4万，主要分布在甘肃。使用保安语。"),
    Yugur("YUG", "Yugur", "裕固族", "CHN", "裕固族，人口约1.4万，主要分布在甘肃。使用裕固语。"),
    Jing("JIN", "Jing", "京族", "CHN", "京族，人口约3.3万，主要分布在广西。使用京语。"),
    Tatar("TAT", "Tatar", "塔塔尔族", "CHN", "塔塔尔族，人口约0.35万，主要分布在新疆。使用塔塔尔语。"),
    Dulong("DUL", "Dulong", "独龙族", "CHN", "独龙族，人口约0.73万，主要分布在云南。使用独龙语。"),
    Ewenki("EVE", "Ewenki", "鄂伦春族", "CHN", "鄂伦春族，人口约0.91万，主要分布在内蒙古、黑龙江。使用鄂伦春语。"),
    Hezhen("HEZ", "Hezhen", "赫哲族", "CHN", "赫哲族，人口约0.53万，主要分布在黑龙江。使用赫哲语。"),
    Menba("MEN", "Menba", "门巴族", "CHN", "门巴族，人口约0.11万，主要分布在西藏。使用门巴语。"),
    Lhoba("LHO", "Lhoba", "珞巴族", "CHN", "珞巴族，人口约0.42万，主要分布在西藏。使用珞巴语。"),
    Jino("JIN", "Jino", "基诺族", "CHN", "基诺族，人口约0.26万，主要分布在云南。使用基诺语。"),
    Arab("ARA", "Arab", "阿拉伯人", "SAU", "阿拉伯人，全球约4.5亿人，主要分布在中东和北非的22个阿拉伯国家，使用阿拉伯语。"),
    Indian("IND", "Indian", "印度人", "IND", "印度人，全球约13亿人，主要分布在印度和巴基斯坦，使用印地语、乌尔都语等。"),
    Japanese("JPN", "Japanese", "日本人", "JPN", "日本人，全球约1.25亿人，主要分布在日本，使用日语。"),
    Punjabi("PUN", "Punjabi", "旁遮普人", "PAK", "旁遮普人，全球约1.3亿人，主要分布在巴基斯坦和印度，使用旁遮普语。"),
    Bengali("BEN", "Bengali", "孟加拉人", "BGD", "孟加拉人，全球约2.5亿人，主要分布在孟加拉国和印度，使用孟加拉语。"),
    Jat("JAT", "Jat", "贾特人", "IND", "贾特人，全球约1.3亿人，主要分布在印度和巴基斯坦，使用印地语、旁遮普语等。"),
    Turk("TUR", "Turk", "土耳其人", "TUR", "土耳其人，全球约8000万人，主要分布在土耳其，使用土耳其语。"),
    Pashtun("PAS", "Pashtun", "普什图人", "AFG", "普什图人，全球约5000万人，主要分布在阿富汗和巴基斯坦，使用普什图语。"),
    German("GER", "German", "德意志人", "DEU", "德意志人，全球约1亿人，主要分布在德国，使用德语。"),
    French("FRA", "French", "法兰西人", "FRA", "法兰西人，全球约6500万人，主要分布在法国，使用法语。"),
    British("GBR", "British", "不列颠人", "GBR", "不列颠人，全球约6700万人，主要分布在英国，使用英语。"),
    Italian("ITA", "Italian", "意大利人", "ITA", "意大利人，全球约6000万人，主要分布在意大利，使用意大利语。"),
    Spanish("ESP", "Spanish", "西班牙人", "ESP", "西班牙人，全球约4700万人，主要分布在西班牙，使用西班牙语。"),
    Mexican("MEX", "Mexican", "墨西哥人", "MEX", "墨西哥人，全球约1.3亿人，主要分布在墨西哥，使用西班牙语。"),
    American("USA", "American", "美利坚人", "USA", "美利坚人，全球约3.3亿人，主要分布在美国，使用英语。"),
    Brazilian("BRA", "Brazilian", "巴西人", "BRA", "巴西人，全球约2.1亿人，主要分布在巴西，使用葡萄牙语。"),
    Nigerian("NGA", "Hausa-Fulani", "豪萨-富拉尼人", "NGA", "豪萨-富拉尼人，全球约8000万人，主要分布在尼日利亚北部，使用豪萨语。"),
    Yoruba("YOR", "Yoruba", "约鲁巴人", "NGA", "约鲁巴人，全球约3500万人，主要分布在尼日利亚西部，使用约鲁巴语。"),
    Igbo("IGB", "Igbo", "伊博人", "NGA", "伊博人，全球约3200万人，主要分布在尼日利亚东南部，使用伊博语。"),
    Somali("SOM", "Somali", "索马里人", "SOM", "索马里人，全球约2200万人，主要分布在索马里，使用索马里语。"),
    Amhara("AMH", "Amhara", "阿姆哈拉人", "ETH", "阿姆哈拉人，全球约3000万人，主要分布在埃塞俄比亚，使用阿姆哈拉语。"),
    Oromo("ORO", "Oromo", "奥罗莫人", "ETH", "奥罗莫人，全球约3500万人，主要分布在埃塞俄比亚，使用奥罗莫语。"),
    Turkmen("TKM", "Turkmen", "土库曼人", "TKM", "土库曼人，全球约800万人，主要分布在土库曼斯坦，使用土库曼语。"),
    Kyrgyz("KGZ", "Kyrgyz", "柯尔克孜人", "KGZ", "柯尔克孜人，全球约700万人，主要分布在吉尔吉斯斯坦，使用柯尔克孜语。"),
    Buryat("BRT", "Buryat", "布里亚特人", "RUS", "布里亚特人，全球约50万人，主要分布在俄罗斯布里亚特共和国，使用布里亚特语。"),
    Maori("MAO", "Maori", "毛利人", "NZL", "毛利人，全球约70万人，主要分布在新西兰，使用毛利语。"),
    Aboriginal("ABO", "Aboriginal", "澳大利亚原住民", "AUS", "澳大利亚原住民，全球约90万人，主要分布在澳大利亚，使用多种原住民语言。"),
    Inuit("INU", "Inuit", "因纽特人", "CAN", "因纽特人，全球约17万人，主要分布在加拿大北极地区，使用因纽特语。"),
    Sami("SAMI", "Sami", "萨米人", "NOR", "萨米人，全球约10万人，主要分布在挪威、瑞典、芬兰和俄罗斯北部，使用萨米语。"),
    Berber("BRB", "Berber", "柏柏尔人", "MOR", "柏柏尔人，全球约3000万人，主要分布在摩洛哥和阿尔及利亚，使用柏柏尔语。"),
    Fulani("FUL", "Fulani", "富拉尼人", "NGA", "富拉尼人，全球约4000万人，主要分布在西非多个国家，使用富拉尼语。"),
    Zulu("ZUL", "Zulu", "祖鲁人", "ZAF", "祖鲁人，全球约1200万人，主要分布在南非，使用祖鲁语。"),
    Xhosa("XHO", "Xhosa", "科萨人", "ZAF", "科萨人，全球约800万人，主要分布在南非，使用科萨语。"),
    Sotho("SOT", "Sotho", "索托人", "ZAF", "索托人，全球约600万人，主要分布在南非和莱索托，使用索托语。"),
    Tswana("TSW", "Tswana", "茨瓦纳人", "BWA", "茨瓦纳人，全球约300万人，主要分布在博茨瓦纳和南非，使用茨瓦纳语。"),
    Swazi("SWA", "Swazi", "斯威士人", "SWZ", "斯威士人，全球约150万人，主要分布在斯威士兰和南非，使用斯威士语。"),
    Shona("SHO", "Shona", "绍纳人", "ZWE", "绍纳人，全球约1200万人，主要分布在津巴布韦，使用绍纳语。"),
    Ndebele("NDE", "Ndebele", "恩德贝勒人", "ZWE", "恩德贝勒人，全球约200万人，主要分布在津巴布韦，使用恩德贝勒语。"),
    Luo("LUO", "Luo", "卢奥人", "KEN", "卢奥人，全球约500万人，主要分布在肯尼亚和南苏丹，使用卢奥语。"),
    Kikuyu("KKY", "Kikuyu", "基库尤人", "KEN", "基库尤人，全球约800万人，主要分布在肯尼亚，使用基库尤语。"),
    Kalenjin("KLN", "Kalenjin", "卡伦金人", "KEN", "卡伦金人，全球约500万人，主要分布在肯尼亚，使用卡伦金语。"),
    Maasai("MAS", "Maasai", "马赛人", "KEN", "马赛人，全球约150万人，主要分布在肯尼亚和坦桑尼亚，使用马赛语。"),
    Amazigh("AMZ", "Amazigh", "阿马齐格人", "MOR", "阿马齐格人，全球约2500万人，主要分布在摩洛哥和阿尔及利亚，使用柏柏尔语。"),
    Tuareg("TUA", "Tuareg", "图阿雷格人", "MLI", "图阿雷格人，全球约200万人，主要分布在马里、尼日尔和利比亚，使用图阿雷格语。"),
    Bedouin("BEO", "Bedouin", "贝都因人", "SAU", "贝都因人，全球约500万人，主要分布在中东沙漠地区，使用阿拉伯语。"),
    Dinka("DIN", "Dinka", "丁卡人", "SSD", "丁卡人，全球约500万人，主要分布在南苏丹，使用丁卡语。"),
    Nuer("NUER", "Nuer", "努尔人", "SSD", "努尔人，全球约300万人，主要分布在南苏丹，使用努尔语。"),
    Aztec("AZT", "Aztec", "阿兹特克人", "MEX", "阿兹特克人，全球约150万人，主要分布在墨西哥，使用纳瓦特尔语。"),
    Maya("MAY", "Maya", "玛雅人", "MEX", "玛雅人，全球约700万人，主要分布在墨西哥、危地马拉和伯利兹，使用玛雅语。"),
    Inca("INC", "Inca", "印加人", "PER", "印加人，全球约800万人，主要分布在秘鲁、玻利维亚和厄瓜多尔，使用克丘亚语。"),
    Quechua("QUE", "Quechua", "克丘亚人", "PER", "克丘亚人，全球约1000万人，主要分布在秘鲁、玻利维亚和厄瓜多尔，使用克丘亚语。"),
    Aymara("AYM", "Aymara", "艾马拉人", "BOL", "艾马拉人，全球约250万人，主要分布在玻利维亚和秘鲁，使用艾马拉语。"),
    Guarani("GUA", "Guarani", "瓜拉尼人", "PRY", "瓜拉尼人，全球约900万人，主要分布在巴拉圭，使用瓜拉尼语。"),
    Mapuche("MAP", "Mapuche", "马普切人", "CHL", "马普切人，全球约180万人，主要分布在智利和阿根廷，使用马普切语。"),
    Apache("APA", "Apache", "阿帕奇人", "USA", "阿帕奇人，全球约70万人，主要分布在美国西南部，使用阿帕奇语。"),
    Navajo("NAV", "Navajo", "纳瓦霍人", "USA", "纳瓦霍人，全球约30万人，主要分布在美国西南部，使用纳瓦霍语。"),
    Cherokee("CHC", "Cherokee", "切诺基人", "USA", "切诺基人，全球约80万人，主要分布在美国东南部，使用切诺基语。"),
    Sioux("SIO", "Sioux", "苏族人", "USA", "苏族人，全球约17万人，主要分布在美国中西部，使用苏语。"),
    Cree("CRE", "Cree", "克里人", "CAN", "克里人，全球约20万人，主要分布在加拿大，使用克里语。"),
    Romani("ROM", "Romani", "罗姆人", "IND", "罗姆人，全球约1200万人，主要分布在东欧和中欧，使用罗姆语。"),
    Armenian("ARM", "Armenian", "亚美尼亚人", "ARM", "亚美尼亚人，全球约700万人，主要分布在亚美尼亚，使用亚美尼亚语。"),
    Kurdish("KUR", "Kurdish", "库尔德人", "TUR", "库尔德人，全球约3000万人，主要分布在土耳其、伊拉克、伊朗和叙利亚，使用库尔德语。"),
    Jewish("JEW", "Jewish", "犹太人", "ISR", "犹太人，全球约1500万人，主要分布在以色列和世界各地，使用希伯来语和意第绪语。");;

    /**
     * 民族代码
     */
    private final String code;
    /**
     * 民族英文名
     */
    private final String enName;
    /**
     * 民族中文名
     */
    private final String cnName;
    /**
     * 民族主要分布国家或地区代码
     */
    private final String ISO_3166_1_Alpha_3;
    /**
     * 民族描述
     */
    private final String desc;

    public static Nationality codeOf(String code) {
        return Arrays.stream(values()).filter(item -> StringUtils.equals(code, item.getCode())).findFirst().orElse(None);
    }
}