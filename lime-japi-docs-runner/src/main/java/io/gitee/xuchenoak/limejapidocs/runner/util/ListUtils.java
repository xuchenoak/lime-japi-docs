package io.gitee.xuchenoak.limejapidocs.runner.util;

import cn.hutool.core.util.ReflectUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * list集合工具
 * @author xuchenoak
 **/
public class ListUtils {

    private static List<String> typeList = new ArrayList<>();
    static {
        typeList.add("java.lang.Integer");
        typeList.add("java.lang.Long");
        typeList.add("java.lang.Double");
        typeList.add("java.lang.Float");
        typeList.add("java.lang.String");
        typeList.add("java.math.BigDecimal");
        typeList.add("java.math.BigInteger");
    }

    public static boolean isBlank(Collection list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(Collection list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    public static <T>T getOneOrNull(Collection<T> list) {
        return getOneOrElse(list, null);
    }

    public static <T>T getOneOrElse(Collection<T> list, T t) {
        if (isBlank(list)) {
            return t;
        }
        return list.stream().findFirst().orElse(t);
    }

    public static <T> List<T> listOrEmpty(List<T> list) {
        if (isBlank(list)) {
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * 获取集合，为空时得到一个空ArrayList，不为空时执行表达式返回集合
     * @param list
     * @param f
     * @return
     * @param <T>
     * @param <R>
     */
    public static <T,R> List<R> listOrDoing(List<T> list, Function<List<T>, List<R>> f) {
        if (isNotBlank(list)) {
            return f.apply(list);
        }
        return new ArrayList<>();
    }

    /**
     * 获取集合，为空时得到一个空ArrayList，不为空时执行表达式返回遍历的集合元素
     * @param list
     * @param f
     * @return
     * @param <T>
     * @param <R>
     */
    public static <T,R> List<R> listOrMapDoing(List<T> list, Function<T, R> f) {
        if (isNotBlank(list)) {
            List<R> collection = ReflectUtil.newInstanceIfPossible(list.getClass());
            for (T t : list) {
                collection.add(f.apply(t));
            }
            return collection;
        }
        return new ArrayList<>();
    }

    /**
     * 多对多关联表的修改数据处理（需要重写equals定义对象对比规则）
     * @param oldAllList 所有旧数据集合（入参）
     * @param newAllList 所有新数据集合（入参）
     * @param newList 需新增的新数据集合（返回）
     * @param oldList 不改变的旧数据集合（返回）
     * @param delList 需清除的旧数据集合（返回）
     */
    public static <T>void setOldListToNewList(List<T> oldAllList, List<T> newAllList, List<T> newList, List<T> oldList, List<T> delList) {
        if (newList == null) {
            newList = new ArrayList<>();
        }
        if (delList == null) {
            delList = new ArrayList<>();
        }
        // 如果新提交数据不存在，则将旧数据清除即可
        if (newAllList == null || newAllList.size() == 0) {
            if (oldAllList != null && oldAllList.size() >0) {
                delList.addAll(oldAllList);
            }
            return;
        }
        // 如果旧数据不存在，则将新数据新增即可
        if (oldAllList == null || oldAllList.size() == 0) {
            if (newAllList != null && newAllList.size() > 0) {
                newList.addAll(newAllList);
            }
            return;
        }
        // 循环所有旧数据区分 旧数据和需要清除的数据
        for (T oldItem : oldAllList) {
            if (!newAllList.contains(oldItem)) {
                delList.add(oldItem);
            } else if (oldList != null){
                oldList.add(oldItem);
            }
        }
        // 循环所有新数据区分 旧数据和需要新增的新数据
        for (T newItem : newAllList) {
            if (!oldAllList.contains(newItem)) {
                newList.add(newItem);
            }
        }
    }

}
