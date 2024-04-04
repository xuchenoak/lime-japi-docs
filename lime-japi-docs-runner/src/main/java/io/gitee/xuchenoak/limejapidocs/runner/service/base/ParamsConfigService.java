package io.gitee.xuchenoak.limejapidocs.runner.service.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.CacheKeyEnum;
import io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus.EntityBaseService;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ParamsConfig;
import io.gitee.xuchenoak.limejapidocs.runner.mapper.SysConfigMapper;
import io.gitee.xuchenoak.limejapidocs.runner.util.CacheUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统参数业务实现
 *
 * @author xuchenoak
 **/
@Service
public class ParamsConfigService extends EntityBaseService<SysConfigMapper, ParamsConfig> {

    /**
     * 获取系统参数缓存标识
     * @param key
     * @return
     */
    private String getKey(String key) {
        return CacheKeyEnum.Param.getKey(key);
    }

    /**
     * 获取参数
     * @param key
     * @return
     */
    public String getParamCache(String key) {
        String cacheKey = getKey(key);
        String value = CacheUtil.get(cacheKey);
        if (value == null) {
            ParamsConfig config = getOne(ParamsConfig::getKey, key);
            if (config != null) {
                value = config.getValue();
                CacheUtil.set(cacheKey, value);
            }
        }
        return value;
    }

    /**
     * 获取参数对象
     * @param key
     * @param c
     * @return
     * @param <T>
     */
    public <T> T getParamCache(String key, Class<T> c) {
        if (StrUtil.isBlank(key) || c == null) {
            return null;
        }
        try {
            return JSONUtil.toBean(getParamCache(key), c);
        } catch (Exception e) {
            log.error("对象转换异常", e);
            return null;
        }
    }

    /**
     * 获取参数对象列表
     * @param key
     * @param c
     * @return
     * @param <T>
     */
    public <T> List<T> getParamsCache(String key, Class<T> c) {
        if (StrUtil.isBlank(key) || c == null) {
            return null;
        }
        try {
            return JSONUtil.toList(getParamCache(key), c);
        } catch (Exception e) {
            log.error("对象转换异常", e);
            return null;
        }
    }

    /**
     * 添加或修改配置
     * @param key
     * @param value
     */
    public synchronized void addOrEdit(String key, String value) {
        ParamsConfig bean = getOne(ParamsConfig::getKey, key);
        if (bean == null) {
            bean = new ParamsConfig(null, key, value, DateUtil.date(), DateUtil.date());
        } else {
            bean.setValue(value);
            bean.setUpdateTime(DateUtil.date());
        }
        saveOrUpdate(bean);
        CacheUtil.remove(getKey(key));
    }
}
