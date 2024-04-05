import request from '@/util/request'

// 获取公共配置
export function common() {
  return request({
    url: '/lime_japi_docs/api/sys_config/common',
    method: 'get'
  })
}

// 系统初始化
export function sysInit(data) {
  return request({
    url: '/lime_japi_docs/api/sys_config/sys_init',
    method: 'post',
    data: data
  })
}

// 保存系统配置
export function saveSysConfig(data) {
  return request({
    url: '/lime_japi_docs/api/sys_config/save_sys_config',
    method: 'post',
    data: data
  })
}

// 获取系统配置
export function getSysConfig() {
  return request({
    url: '/lime_japi_docs/api/sys_config/get_sys_config',
    method: 'get'
  })
}