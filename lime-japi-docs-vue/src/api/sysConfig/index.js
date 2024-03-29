import request from '@/util/request'

// 获取公共配置
export function common() {
  return request({
    url: '/lime_japi_docs/api/sys_config/common',
    method: 'get'
  })
}