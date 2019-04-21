package com.lsj.videocompress.compress.info;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.util.LongSparseArray;
import android.text.TextUtils;
import android.util.SparseArray;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类，包含字符串判断，替换 等<br>
 * Author: lishoajie<br>
 * Create 2014-5-28<br>
 * fixed by louweijun on 2016-8-26.<br>
 * Version 1.1.0
 */
public class CommonUtil {

    /**
     * 服务器输入框标签
     */
    public static final String SERVER_INPUT_TEXT = "<a href=\"http://www.w3school.com.cn\">白日依山尽</a>";
    /**
     *
     */
    public static final String KEY_PAY_STATUS = "KEY_PAY_STATUS";

    /**
     * 用于解决在DialogActivity中，使用getContext转换对象不是Activity的问题
     *
     * @param cont
     * @return
     */
    public static Activity scanForActivity(Context cont) {
        if (cont == null) {
            return null;
        } else if (cont instanceof Activity) {
            return (Activity) cont;
        } else if (cont instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) cont).getBaseContext());
        }

        return null;
    }

    /**
     * 替换字符 "/"-->"-"
     */
    public static String replaceUrl(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        return string.replace("/", "-");
    }

    /**
     * 将浏览器问号之后的参数进行转义
     */
    public static String encodeString(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }

        int index = url.indexOf('?');

        if (index < 0 || index == url.length() - 1) {
            return url;
        }

        String prefix = url.substring(0, index);
        String last = url.substring(index + 1);
        last = URLEncoder.encode(last);
        last = last.replace("+", "%20");
        return String.format("%s?%s", prefix, last);
    }

    /**
     * 是否不是"1"
     *
     * @param booleanStr string
     * @return false 空或者不是"1"
     */
    public static boolean isOne(String booleanStr) {
        return !TextUtils.isEmpty(booleanStr) && booleanStr.equals("1");
    }

    /**
     * 是否不是“0”
     */
    public static boolean isNotZero(String booleanStr) {
        return !TextUtils.isEmpty(booleanStr) && !booleanStr.equals("0");
    }

    /**
     * 是否是“0”
     */
    public static boolean isZero(String booleanStr) {
        return !TextUtils.isEmpty(booleanStr) && booleanStr.equals("0");
    }

    public static boolean isZero(int value) {
        return value == 0;
    }

    public static boolean isOne(int value) {
        return value == 1;
    }

    /**
     * 是否不是“0”
     */
    public static boolean isNotZero(int intValue) {
        return intValue != 0;
    }


    /**
     * 将元素切分成集合
     *
     * @param str        分割对象
     * @param splitRegex 分隔符
     * @return 分割后的list
     */
    public static List<String> getSplitStr(String str, String splitRegex) {
        String[] excludeIdArray = str.split(splitRegex);
        List<String> list = new ArrayList<>(excludeIdArray.length);
        list.addAll(Arrays.asList(excludeIdArray));
        return list;
    }

    public static String getJpgUrlFormVideoUrl(String videoUrl) {
        if (TextUtils.isEmpty(videoUrl)) {
            return "";
        }
        String finalKey = videoUrl;
        try {
            int start = videoUrl.lastIndexOf("/");
            int end = videoUrl.lastIndexOf(".");
            finalKey = videoUrl.substring(start + 1, end);

            if (finalKey.startsWith("mp4_")) {
                finalKey = finalKey.substring(4);
            }
        } catch (Exception e) {
        }
        String format = String.format("http://dl.xueleyun.com/mediaimages/1/200x200_%s.jpg", finalKey);
        return format;
    }

    /**
     * 获得 ① 类似的数字
     *
     * @param pos
     * @return
     */
    public static String getCircleNumberChar(int pos) {
        int startCharCode = '①';
        startCharCode += pos;
        return String.valueOf((char) startCharCode);
    }

    /**
     * 把题目标示代替为____
     */
    public static String transInputTextSymbol(String text) {
        return text.replaceAll(SERVER_INPUT_TEXT, " ____ ");
    }

    /**
     * 分页是否是下拉刷新
     *
     * @param timeLine
     * @return
     */
    public static boolean isNewTime(String timeLine) {
        if (TextUtils.isEmpty(timeLine)) {
            return true;
        }

        return "0".equals(timeLine);
    }

    /**
     * 一般来说，遇到异常情况的文字（比如EditText的ImageSpan）
     *
     * @param symbol
     * @return
     */
    public static boolean isCharUnReadable(char symbol) {
        return ((int) symbol) >= 65532;
    }

    /**
     * 统计某个字符串内出现另外个字符串的次数
     *
     * @param one 要统计的字符串
     * @param two 出现的字符串
     * @return
     */
    public static int countAppearTime(String one, String two) {
        return (one.length() - one.replace(two, "").length()) / two.length();
    }

    /**
     * list 是否为空
     **/
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(Iterable iterable) {
        return iterable == null || iterable.iterator() == null || !iterable.iterator().hasNext();
    }

    /**
     * set 是否为空
     **/
    public static boolean isEmpty(Set set) {
        return set == null || set.isEmpty();
    }

    /**
     * SparseArray 是否为空
     **/
    public static boolean isEmpty(SparseArray array) {
        return array == null || array.size() == 0;
    }

    public static boolean isEmpty(LongSparseArray array) {
        return array == null || array.size() == 0;
    }

    /**
     * map 是否为空
     **/
    public static boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    /**
     * list 是否为空
     **/
    public static <T> boolean isEmpty(T[] list) {
        return list == null || list.length == 0;
    }

    /**
     * 获取List大小，如果为空则返回0
     **/
    public static int getSize(List list) {
        return list == null ? 0 : list.size();
    }

    public static <T> List<T> valueToArray(SparseArray<T> list) {
        if (CommonUtil.isEmpty(list))
            return null;

        List<T> result = new ArrayList<>(list.size());

        for (int i = 0; i < list.size(); i++) {
            result.add(list.valueAt(i));
        }
        return result;
    }

    public static boolean equals(String firstStr, String secondStr) {
        if (firstStr == null || secondStr == null)
            return false;

        return TextUtils.equals(firstStr, secondStr);
    }

    public static boolean equalsIgnoreCase(String firstStr, String secondStr) {
        if (firstStr == null || secondStr == null)
            return false;

        return firstStr.equalsIgnoreCase(secondStr);
    }

    /**
     * WeakReference 是不否为空
     */
    public static boolean isRefNotNull(WeakReference ref) {
        return ref != null && ref.get() != null;
    }

    /**
     * 判断pos 对于list是否可以用
     *
     * @return false pos 越界或小于0
     */
    public static boolean isContain(List list, int pos) {
        if (pos < 0) {
            return false;
        }

        if (isEmpty(list)) {
            return false;
        }

        return pos < list.size();

    }

    /**
     * 获取有数据的字符串，优先获取str1
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String getAvailableStr(String str1, String str2) {
        return TextUtils.isEmpty(str1) ? str2 : str1;
    }

    /**
     * 将输入框的换行符，解析为html的换行符 <br/>
     *
     * @param str
     * @return
     */
    public static String transToBR(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }

        return str.replace("\n", "<br/>");
    }

    public static String transBrackets(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }

        return str.replace("<", "&lt;");
    }

    /**
     * 数组是否包含字符串
     *
     * @param sourceList 数组
     * @param dest       目标string
     * @return true 数组中包含这个字符串
     */
    public static boolean containStr(String[] sourceList, String dest) {
        if (isEmpty(sourceList) || TextUtils.isEmpty(dest)) {
            return false;
        }

        dest = dest.toLowerCase();

        for (int i = 0; i < sourceList.length; i++) {
            if (sourceList[i].equals(dest)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 按照时间格式生成 .jpg格式的文件名
     *
     * @param fileName 文件名
     * @return 时间+filename+ .jpg 格式
     */
    public static String generateRandomJPGName(String fileName) {
        return generateRandomName(fileName, ".jpg");
    }

    /**
     * 按照时间格式生成 .
     *
     * @param fileName         文件名
     * @param defaultExtension 文件后缀
     * @return 时间+filename+defaultExtension  格式
     */
    public static String generateRandomName(String fileName, String defaultExtension) {
        String randomStr = String.valueOf(new Date().getTime());

        int n = fileName.lastIndexOf(".");
        if (n > 0) {
            return randomStr + fileName.substring(n, fileName.length());
        }
        return randomStr + defaultExtension;
    }

    /**
     * 替换字符串末尾的文字 ，比如 你好世界 => 你好..
     *
     * @param string 需要替换的字符串
     * @param symbol 用来替代的符号
     * @param length 末尾需要替代的长度
     * @return 替换后的字符串
     */
    public static String replaceEnd(String string, char symbol, int length) {
        int dotLength = string.length();
        dotLength = dotLength >= length ? length : dotLength;

        String dot = "";
        for (int i = 0; i < dotLength; i++) {
            dot += symbol;
        }

        string = string.substring(0, string.length() - dotLength) + dot;
        return string;
    }

    //获得头像等 图片链接的filekey
    public static String getShortIcon(String icon) {
        if (TextUtils.isEmpty(icon))
            return "";
        if (icon.contains("_") && icon.contains(".")) {
            int start = icon.lastIndexOf("_") + 1;
            int end = icon.lastIndexOf(".");
            if (start < end)
                return icon.substring(start, end);
        }
        return "";
    }

    /**
     * 获取行数
     * 5,3 =>2   5,6=>1
     *
     * @param total  总数
     * @param column 每行个数
     * @return 行数
     */
    public static int getRowNumber(int total, int column) {
        if (column == 0) {
            return total;
        }

        return (int) Math.ceil((double) total / column);
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
            //加入笑脸兼容
            if (codePoint == 9786) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || (
                (codePoint >= 0xE000) && (codePoint <= 0xFFFD));
//                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF))
    }


    public static Uri resourceToUri(Context context, int resId) {
        Resources resources = context.getResources();
        return new Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE).authority(resources.getResourcePackageName(resId)).appendPath(resources
                .getResourceTypeName(resId)).appendPath(resources.getResourceEntryName(resId)).build();

    }

    /**
     * 获取img标签的正则表达式
     *
     * @param
     * @return
     */
    public static String getImgHtml(String s) {
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");//<img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");
        Matcher m = p.matcher(s);
        while (m.find()) {
            return m.group();
        }
        return null;
    }

    /**
     * 获取img标签的src的正则表达式
     *
     * @param
     * @return
     */
    public static String getImgSrcHtml(String s) {
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");//<img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");
        Matcher m = p.matcher(s);
        while (m.find()) {
            return m.group(1);
        }
        return null;
    }

    /**
     * 利用序列化的方式实现深拷贝
     * <p>
     * 失败返回null
     */
    public static Object deepClone(Serializable serializable) throws Exception {
        // 序列化
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(serializable);

            // 反序列化
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
    }

    public static boolean isPaySuccess(Intent intent) {
        if (intent == null)
            return false;
        return intent.getBooleanExtra(KEY_PAY_STATUS, false);

    }


    /**
     * 把list<String>  ids 转为为 id,id,id,id格式
     */
    public static String stringListToString(List<String> ids) {
        return stringListToString(ids, ",");
    }

    public static String stringListToString(List<String> ids, String spliter) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i != 0) {
                stringBuffer.append(spliter);
            }
            stringBuffer.append(ids.get(i));
        }
        return stringBuffer.toString();
    }

    /**
     * 返回str或空字符串
     */
    public static String nonNullStr(String str) {
        return str == null ? "" : str;
    }

    /**
     * 返回非空List
     */
    public static <T> List<T> nonNullList(List<T> list) {
        return list == null ? new ArrayList<T>() : list;
    }




}

