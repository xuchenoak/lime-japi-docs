package io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.xuchenoak.limejapidocs.runner.common.exception.CusExc;
import io.gitee.xuchenoak.limejapidocs.runner.util.BeanUtil;
import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 基础业务实现
 */
public class EntityBaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    protected final Class<T> entityType;

    public EntityBaseService() {
        this.entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public T add(T t) {
        if (t == null || !save(t)) {
            CusExc.e("保存失败");
        }
        return t;
    }

    public <V> V add(T t, Class<V> c) {
        return BeanUtil.toBean(add(t), c);
    }

    public <R> T addBean(R t) {
        return add(BeanUtil.toBean(t, entityType));
    }

    public <V, R> V addBean(R t, Class<V> c) {
        return add(BeanUtil.toBean(t, entityType), c);
    }

    public void addBatch(Collection<T> ts) {
        if (ListUtils.isBlank(ts) || !saveBatch(ts)) {
            CusExc.e("批量新增失败");
        }
    }

    public void addBatchIfCount(Collection<T> ts) {
        if (ListUtils.isBlank(ts)) {
            return;
        }
        if (!saveBatch(ts)) {
            CusExc.e("批量新增失败");
        }
    }

    public <R> void addBatchBean(Collection<R> ts) {
        addBatch(BeanUtil.copyToList(ts, entityType));
    }

    public T edit(T t) {
        if (t == null || !updateById(t)) {
            CusExc.e("更新失败");
        }
        return t;
    }

    public <V> V edit(T t, Class<V> c) {
        return BeanUtil.toBean(edit(t), c);
    }

    public <R> T editBean(R t) {
        return edit(BeanUtil.toBean(t, entityType));
    }

    public <V, R> V editBean(R t, Class<V> c) {
        return edit(BeanUtil.toBean(t, entityType), c);
    }

    public void editBatch(Collection<T> ts) {
        if (ListUtils.isBlank(ts) || !updateBatchById(ts)) {
            CusExc.e("批量更新失败");
        }
    }

    public void editBatchIfCount(Collection<T> ts) {
        if (ListUtils.isBlank(ts)) {
            return;
        }
        if (!updateBatchById(ts)) {
            CusExc.e("批量更新失败");
        }
    }

    public <R> void editBatchBean(Collection<R> ts) {
        editBatch(BeanUtil.copyToList(ts, entityType));
    }

    public void delById(Serializable id) {
        T bean = getById(id);
        if (bean == null) {
            CusExc.e("删除对象不存在");
        }
        removeById(id);
    }

    public void delBatch(Collection<T> ts) {
        if (ListUtils.isBlank(ts) || !removeBatchByIds(ts)) {
            CusExc.e("批量删除失败");
        }
    }

    public void delBatchIfCount(Collection<T> ts) {
        if (ListUtils.isBlank(ts)) {
            return;
        }
        if (!removeBatchByIds(ts)) {
            CusExc.e("批量删除失败");
        }
    }

    public <R> R getById(Serializable id, Class<R> c) {
        return BeanUtil.getBeanOrDoing(getById(id), bean -> BeanUtil.toBean(bean, c));
    }

    public <R> R getById(Serializable id, Function<T, R> f) {
        return BeanUtil.getBeanOrDoing(getById(id), f);
    }

    public T getOne(SFunction<T, ?> f, Object v) {
        return getOne(new LambdaQueryWrapper<T>()
                .eq(f, v));
    }

    public <R> R getOne(SFunction<T, ?> f, Object v, Function<T, R> bf) {
        T b = getOne(new LambdaQueryWrapper<T>()
                .eq(f, v));
        if (b == null) {
            return null;
        }
        return BeanUtil.getBeanOrDoing(b, bf);
    }

    public <R> R getOne(SFunction<T, ?> f, Object v, Class<R> c) {
        return BeanUtil.toBean(getOne(f, v), c);
    }

    public <R> List<R> list(Class<R> c) {
        return ListUtils.listOrDoing(list(), list -> BeanUtil.copyToList(list, c));
    }

    public <R> List<R> list(Class<R> c, Function<List<T>, List<R>> f) {
        return ListUtils.listOrDoing(list(), f);
    }

    public <R> List<R> list(Wrapper<T> q, Class<R> c) {
        return ListUtils.listOrDoing(list(q), list -> BeanUtil.copyToList(list, c));
    }

    public <K, V> Map<K, V> getMap(Function<T, K> f1, Function<T, V> f2) {
        Map<K, V> map = new HashMap<>();
        List<T> list = list();
        if (ListUtils.isNotBlank(list)) {
            for (T t : list) {
                map.put(f1.apply(t), f2.apply(t));
            }
        }
        return map;
    }

    public <K, V> Map<K, V> getMap(Wrapper<T> q, Function<T, K> f1, Function<T, V> f2) {
        Map<K, V> map = new HashMap<>();
        List<T> list = list(q);
        if (ListUtils.isNotBlank(list)) {
            for (T t : list) {
                map.put(f1.apply(t), f2.apply(t));
            }
        }
        return map;
    }

}
