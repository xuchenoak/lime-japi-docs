import request from '@/util/request'

// 获取文档列表
export function list(params) {
  return request({
    url: '/lime_japi_docs/api/docs_config/list',
    method: 'get',
    params: params
  })
}

// 验证邀请码
export function checkViewKey(viewKey) {
  return request({
    url: '/lime_japi_docs/api/docs_config/check_view_key',
    method: 'get',
    params: {viewKey}
  })
}

// 获取单个文档配置（简单版）
export function getDocsConfigSimple(id) {
  return request({
    url: '/lime_japi_docs/api/docs_config/get_docs_config_simple',
    method: 'get',
    params: {id}
  })
}

// 获取单个文档配置
export function getDocsConfig(id) {
  return request({
    url: '/lime_japi_docs/api/docs_config/get_docs_config',
    method: 'get',
    params: {id}
  })
}

// 验证文档管理秘钥是否正确
export function checkConfigKey(docsConfigKey) {
  return request({
    url: '/lime_japi_docs/api/docs_config/check_config_key',
    method: 'get',
    params: {docsConfigKey}
  })
}

// 新增文档
export function add(data) {
  return request({
    url: '/lime_japi_docs/api/docs_config/add',
    method: 'post',
    data: data
  })
}

// 编辑文档
export function edit(data) {
  return request({
    url: '/lime_japi_docs/api/docs_config/edit',
    method: 'post',
    data: data
  })
}

// 删除文档
export function del(id) {
  return request({
    url: '/lime_japi_docs/api/docs_config/del',
    method: 'post',
    data: {id}
  })
}

// 获取git仓库远程分支列表
export function getGitRemoteBranches(gitUrl, username, password) {
  return request({
    url: '/lime_japi_docs/api/docs_config/get_git_remote_branches',
    method: 'get',
    params: {gitUrl, username, password}
  })
}
