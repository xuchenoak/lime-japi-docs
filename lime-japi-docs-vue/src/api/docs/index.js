import request from '@/util/request'

// 获取生成时间集
export function listCreateTime(docsConfigId) {
    return request({
        url: '/lime_japi_docs/api/docs/list_create_time',
        method: 'get',
        params: {docsConfigId}
    })
}

// 获取接口文档目录
export function listCatalog(params) {
  return request({
    url: '/lime_japi_docs/api/docs/list_catalog',
    method: 'get',
    params: params
  })
}

// 获取接口文档列表
export function listInterface(params) {
  return request({
    url: '/lime_japi_docs/api/docs/list_interface',
    method: 'get',
    params: params
  })
}

// 执行文档解析
export function runDocsParse(docsConfigId, password) {
    return request({
        url: '/lime_japi_docs/api/docs/run_docs_parse',
        method: 'get',
        params: {docsConfigId, password}
    })
}

// 获取解析消息
export function getPareMsg(docsConfigId, parseTimestamp) {
    return request({
        url: '/lime_japi_docs/api/docs/get_parse_msg',
        method: 'get',
        params: {docsConfigId, parseTimestamp}
    })
}
