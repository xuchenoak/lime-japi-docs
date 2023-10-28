import request from '@/util/request'

// 获取文档列表
export function list(params) {
    return request({
        url: '/lime_japi_docs/api/config/list',
        method: 'get',
        params: params
    })
}

// 获取单个文档配置（简单版）
export function getDocsConfigSimple(id) {
  return request({
    url: '/lime_japi_docs/api/config/get_docs_config_simple',
    method: 'get',
    params: {id}
  })
}

// 获取单个文档配置
export function getDocsConfig(id) {
  return request({
    url: '/lime_japi_docs/api/config/get_docs_config',
    method: 'get',
    params: {id}
  })
}

// 验证文档管理秘钥是否正确
export function checkConfigKey(docsConfigKey) {
    return request({
        url: '/lime_japi_docs/api/config/check_config_key',
        method: 'get',
        params: {docsConfigKey}
    })
}

// 新增文档
export function add(data) {
    return request({
        url: '/lime_japi_docs/api/config/add',
        method: 'post',
        data: data
    })
}

// 编辑文档
export function edit(data) {
    return request({
        url: '/lime_japi_docs/api/config/edit',
        method: 'post',
        data: data
    })
}

// 删除文档
export function del(id) {
    return request({
        url: '/lime_japi_docs/api/config/del',
        method: 'post',
        data: {id}
    })
}