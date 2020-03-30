package com.vcredit.vmc.rules.drools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 身份证工具类
 * @author niumangyuan
 * @version v1.0
 * @date 2020/1/7
 **/
public class IdCardUtils {

    /**
     * 中国公民身份证号码最小长度。
     */
    private static final int CHINA_ID_MIN_LENGTH = 15;
    /**
     * 中国公民身份证号码最大长度。
     */
    private static final int CHINA_ID_MAX_LENGTH = 18;
    /**
     * 最低年限
     */
    private static final int MIN = 1930;

    /**
     * 每位加权因子
     */
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 城市编码
     */
    private static Map<String, String> cityCodes = new HashMap<String, String>();

    /**
     * 台湾身份首字母对应数字
     */
    private static Map<String, Integer> twFirstCode = new HashMap<String, Integer>();

    static {
        cityCodes.put("11", "北京市");
        cityCodes.put("12", "天津市");
        cityCodes.put("13", "河北省");
        cityCodes.put("14", "山西省");
        cityCodes.put("15", "内蒙古自治区");
        cityCodes.put("21", "辽宁省");
        cityCodes.put("22", "吉林省");
        cityCodes.put("23", "黑龙江省");
        cityCodes.put("31", "上海市");
        cityCodes.put("32", "江苏省");
        cityCodes.put("33", "浙江省");
        cityCodes.put("34", "安徽省");
        cityCodes.put("35", "福建省");
        cityCodes.put("36", "江西省");
        cityCodes.put("37", "山东省");
        cityCodes.put("41", "河南省");
        cityCodes.put("42", "湖北省");
        cityCodes.put("43", "湖南省");
        cityCodes.put("44", "广东省");
        cityCodes.put("45", "广西壮族自治区");
        cityCodes.put("46", "海南省");
        cityCodes.put("50", "重庆市");
        cityCodes.put("51", "四川省");
        cityCodes.put("52", "贵州省");
        cityCodes.put("53", "云南省");
        cityCodes.put("54", "西藏自治区");
        cityCodes.put("61", "陕西省");
        cityCodes.put("62", "甘肃省");
        cityCodes.put("63", "青海省");
        cityCodes.put("64", "宁夏回族自治区");
        cityCodes.put("65", "新疆维吾尔自治区");
        cityCodes.put("71", "台湾省");
        cityCodes.put("81", "香港特别行政区");
        cityCodes.put("82", "澳门特别行政区");
        cityCodes.put("91", "国外");
        twFirstCode.put("A", 10);
        twFirstCode.put("B", 11);
        twFirstCode.put("C", 12);
        twFirstCode.put("D", 13);
        twFirstCode.put("E", 14);
        twFirstCode.put("F", 15);
        twFirstCode.put("G", 16);
        twFirstCode.put("H", 17);
        twFirstCode.put("J", 18);
        twFirstCode.put("K", 19);
        twFirstCode.put("L", 20);
        twFirstCode.put("M", 21);
        twFirstCode.put("N", 22);
        twFirstCode.put("P", 23);
        twFirstCode.put("Q", 24);
        twFirstCode.put("R", 25);
        twFirstCode.put("S", 26);
        twFirstCode.put("T", 27);
        twFirstCode.put("U", 28);
        twFirstCode.put("V", 29);
        twFirstCode.put("X", 30);
        twFirstCode.put("Y", 31);
        twFirstCode.put("W", 32);
        twFirstCode.put("Z", 33);
        twFirstCode.put("I", 34);
        twFirstCode.put("O", 35);
    }

    /**
     * 数字验证
     * @param val 值
     * @return 提取的数字。
     */
    private static boolean isNum(String val) {
        return val != null && !"".equals(val) && val.matches("^[0-9]*$");
    }

    /**
     * 将15位身份证号码转换为18位
     * @param idCard 15位身份编码
     * @return 18位身份编码
     */
    private static String convert15CardTo18(String idCard) {
        String idCard18;
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (isNum(idCard)) {
            // 获取出生年月日
            String birthday = idCard.substring(6, 12);
            Date birthDate = null;
            try {
                birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null) {
                cal.setTime(birthDate);
            }
            // 获取出生年(完全表现形式,如：2010)
            String sYear = String.valueOf(cal.get(Calendar.YEAR));
            idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
            // 转换字符数组
            char[] cArr = idCard18.toCharArray();
            int[] iCard = convertCharToInt(cArr);
            int iSum17 = getPowerSum(iCard);
            // 获取校验位
            String sVal = getCheckCode18(iSum17);
            if (sVal.length() > 0) {
                idCard18 += sVal;
            } else {
                return null;
            }
        } else {
            return null;
        }
        return idCard18;
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断
     * @param iSum 数字
     * @return 校验位
     */
    private static String getCheckCode18(int iSum) {
        String sCode;
        switch (iSum % 11) {
            case 10:
                sCode = "2";
                break;
            case 9:
                sCode = "3";
                break;
            case 8:
                sCode = "4";
                break;
            case 7:
                sCode = "5";
                break;
            case 6:
                sCode = "6";
                break;
            case 5:
                sCode = "7";
                break;
            case 4:
                sCode = "8";
                break;
            case 3:
                sCode = "9";
                break;
            case 2:
                sCode = "x";
                break;
            case 1:
                sCode = "0";
                break;
            case 0:
                sCode = "1";
                break;
            default:
                sCode = "";
                break;
        }
        return sCode;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     * @param iArr 字符串
     * @return 身份证编码。
     */
    private static int getPowerSum(int[] iArr) {
        int iSum = 0;
        if (POWER.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                for (int j = 0; j < POWER.length; j++) {
                    if (i == j) {
                        iSum = iSum + iArr[i] * POWER[j];
                    }
                }
            }
        }
        return iSum;
    }

    /**
     * 将字符数组转换成数字数组
     * @param ca 字符数组
     * @return 数字数组
     */
    private static int[] convertCharToInt(char[] ca) {
        int len = ca.length;
        int[] iArr = new int[len];
        try {
            for (int i = 0; i < len; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return iArr;
    }

    /**
     * 根据身份编号获取年龄
     * @param idCard 身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idCard) {
        int iAge;
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = convert15CardTo18(idCard);
        }
        assert idCard != null;
        String year = idCard.substring(6, 10);
        String month = idCard.substring(10, 12);
        String day = idCard.substring(12, 14);
        Calendar cal = Calendar.getInstance();
        int iCurrYear = cal.get(Calendar.YEAR);
        int calMonth = cal.get(Calendar.MONTH) + 1;//月份下标从0开始
        int calDay = cal.get(Calendar.DAY_OF_MONTH);
        if (Integer.parseInt(month) > calMonth || (Integer.parseInt(month) == calMonth && Integer.parseInt(day) > calDay)) {
            iAge = iCurrYear - Integer.parseInt(year) - 1;
        } else {
            iAge = iCurrYear - Integer.parseInt(year);
        }

        return iAge;
    }

    /**
     * 验证身份证是否合法
     */
    public static boolean validateCard(String idCard) {
        String card = idCard.trim();
        if (validateIdCard18(card)) {
            return true;
        }
        if (validateIdCard15(card)) {
            return true;
        }
        String[] cardVal = validateIdCard10(card);
        if (cardVal != null) {
            return "true".equals(cardVal[2]);
        }
        return false;
    }

    /**
     * 验证18位身份编码是否合法
     * @param idCard 身份编码
     * @return 是否合法
     */
    private static boolean validateIdCard18(String idCard) {
        boolean bTrue = false;
        if (idCard.length() == CHINA_ID_MAX_LENGTH) {
            // 前17位
            String code17 = idCard.substring(0, 17);
            // 第18位
            String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
            if (isNum(code17)) {
                char[] cArr = code17.toCharArray();
                int[] iCard = convertCharToInt(cArr);
                int iSum17 = getPowerSum(iCard);
                // 获取校验位
                String val = getCheckCode18(iSum17);
                if (val.length() > 0) {
                    if (val.equalsIgnoreCase(code18)) {
                        bTrue = true;
                    }
                }
            }
        }
        return bTrue;
    }

    /**
     * 验证15位身份编码是否合法
     * @param idCard 身份编码
     * @return 是否合法
     */
    private static boolean validateIdCard15(String idCard) {
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return false;
        }
        if (isNum(idCard)) {
            String proCode = idCard.substring(0, 2);
            if (cityCodes.get(proCode) == null) {
                return false;
            }
            String birthCode = idCard.substring(6, 12);
            Date birthDate = null;
            try {
                birthDate = new SimpleDateFormat("yy").parse(birthCode.substring(0, 2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null) {
                cal.setTime(birthDate);
            }
            return validate(cal.get(Calendar.YEAR), Integer.parseInt(birthCode.substring(2, 4)), Integer.parseInt(birthCode.substring(4, 6)));
        } else {
            return false;
        }
    }

    /**
     * 验证小于当前日期 是否有效
     * @param iYear  待验证日期(年)
     * @param iMonth 待验证日期(月 1-12)
     * @param iDate  待验证日期(日)
     * @return 是否有效
     */
    private static boolean validate(int iYear, int iMonth, int iDate) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int datePerMonth;
        if (iYear < MIN || iYear >= year) {
            return false;
        }
        if (iMonth < 1 || iMonth > 12) {
            return false;
        }
        switch (iMonth) {
            case 4:
            case 6:
            case 9:
            case 11:
                datePerMonth = 30;
                break;
            case 2:
                boolean dm = (iYear % 4 == 0 && iYear % 100 != 0 || iYear % 400 == 0) && iYear > MIN;
                datePerMonth = dm ? 29 : 28;
                break;
            default:
                datePerMonth = 31;
        }
        return (iDate >= 1) && (iDate <= datePerMonth);
    }

    /**
     * 验证10位身份编码是否合法
     * @param idCard 身份编码
     * @return 身份证信息数组
     * <p>
     * [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false)
     * 若不是身份证件号码则返回null
     * </p>
     */
    private static String[] validateIdCard10(String idCard) {
        String[] info = new String[3];
        String card = idCard.replaceAll("[\\(|\\)]", "");
        if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
            return null;
        }
        if (idCard.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
            info[0] = "台湾";
            System.out.println("11111");
            String char2 = idCard.substring(1, 2);
            if ("1".equals(char2)) {
                info[1] = "M";
                System.out.println("MMMMMMM");
            } else if ("2".equals(char2)) {
                info[1] = "F";
                System.out.println("FFFFFFF");
            } else {
                info[1] = "N";
                info[2] = "false";
                System.out.println("NNNN");
                return info;
            }
            info[2] = validateTWCard(idCard) ? "true" : "false";
        } else if (idCard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
            info[0] = "澳门";
            info[1] = "N";
        } else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
            info[0] = "香港";
            info[1] = "N";
            info[2] = validateHKCard(idCard) ? "true" : "false";
        } else {
            return null;
        }
        return info;
    }

    /**
     * 验证台湾身份证号码
     * @param idCard 身份证号码
     * @return 验证码是否符合
     */
    private static boolean validateTWCard(String idCard) {
        String start = idCard.substring(0, 1);
        String mid = idCard.substring(1, 9);
        String end = idCard.substring(9, 10);
        Integer iStart = twFirstCode.get(start);
        int sum = iStart / 10 + (iStart % 10) * 9;
        char[] chars = mid.toCharArray();
        int iflag = 8;
        for (char c : chars) {
            sum = sum + Integer.parseInt(c + "") * iflag;
            iflag--;
        }
        return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.parseInt(end);
    }

    /**
     * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
     * <p>
     * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35
     * 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
     * </p>
     * <p>
     * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
     * </p>
     * @param idCard 身份证号码
     * @return 验证码是否符合
     */
    private static boolean validateHKCard(String idCard) {
        String card = idCard.replaceAll("[\\(|\\)]", "");
        int sum = 0;
        if (card.length() == 9) {
            sum = (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 9 + (Integer.valueOf(card.substring(1, 2).toUpperCase().toCharArray()[0]) - 55) * 8;
            card = card.substring(1, 9);
        } else {
            sum = 522 + (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 8;
        }
        String mid = card.substring(1, 7);
        String end = card.substring(7, 8);
        char[] chars = mid.toCharArray();
        int iflag = 7;
        for (char c : chars) {
            sum = sum + Integer.parseInt(c + "") * iflag;
            iflag--;
        }
        if ("A".equals(end.toUpperCase())) {
            sum = sum + 10;
        } else {
            sum = sum + Integer.parseInt(end);
        }
        return sum % 11 == 0;
    }
}
