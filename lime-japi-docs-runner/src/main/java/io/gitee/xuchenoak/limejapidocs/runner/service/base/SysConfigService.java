package io.gitee.xuchenoak.limejapidocs.runner.service.base;

import cn.hutool.json.JSONUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus.EntityBaseService;
import io.gitee.xuchenoak.limejapidocs.runner.domain.SysConfig;
import io.gitee.xuchenoak.limejapidocs.runner.mapper.SysConfigMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置业务实现
 *
 * @author xuchenoak
 **/
@Service
public class SysConfigService extends EntityBaseService<SysConfigMapper, SysConfig> {

    private static final Map<String, String> cacheMap = new HashMap<>();

    /**
     * 获取配置对象
     * @param key
     * @param c
     * @return
     * @param <T>
     */
    public <T> T getConfig(String key, Class<T> c) {
        String value = getConfig(key);
        return value == null ? null : JSONUtil.toBean(value, c);
    }

    /**
     * 获取配置对象
     * @param key
     * @param c
     * @return
     * @param <T>
     */
    public <T> List<T> listConfig(String key, Class<T> c) {
        String value = getConfig(key);
        return value == null ? null : JSONUtil.toList(value, c);
    }

    /**
     * 获取配置
     * @param key
     * @return
     */
    public String getConfig(String key) {
        String value = cacheMap.get(key);
        if (value == null) {
            SysConfig config = getOne(SysConfig::getKey, key);
            if (config != null) {
                value = config.getValue();
                cacheMap.put(key, value);
            }
        }
        return value;
    }

    @Override
    public SysConfig edit(SysConfig sysConfig) {
        SysConfig b = super.edit(sysConfig);
        cacheMap.remove(sysConfig.getKey());
        return b;
    }
}
