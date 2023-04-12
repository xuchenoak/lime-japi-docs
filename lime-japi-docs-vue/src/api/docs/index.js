import request from '@/util/request'

// 获取生成时间集
export function listCreateTime() {
    return request({
        url: '/lime_japi_docs/api/list_create_time',
        method: 'get'
    })
}

// 获取接口文档目录
export function listCatalog(params) {
  return request({
    url: '/lime_japi_docs/api/list_catalog',
    method: 'get',
    params: params
  })
}

// 获取接口文档列表
export function listInterface(params) {
  return request({
    url: '/lime_japi_docs/api/list_interface',
    method: 'get',
    params: params
  })
}

// 获取接口文档配置
export function getDocsConfig() {
    return request({
        url: '/lime_japi_docs/api/get_docs_config',
        method: 'get'
    })
}

// 执行文档解析
export function runDocsParse(password) {
    return request({
        url: '/lime_japi_docs/api/run_docs_parse',
        method: 'get',
        params: {password}
    })
}

// 获取解析消息
export function getPareMsg(parseTimestamp) {
    return request({
        url: '/lime_japi_docs/api/get_parse_msg',
        method: 'get',
        params: {parseTimestamp}
    })
}
