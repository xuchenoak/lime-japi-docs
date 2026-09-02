<template>
    <drawer-box
        title="文档配置"
        :width="700"
        :loading="loading"
        :visible="visible"
        @close="close"
        @save="handleSave"
    >
        <a-tabs default-active-key="1" @change="handleTab">
            <a-tab-pane key="1" tab="基本信息">
                <a-form :form="form" layout="vertical" >
                    <a-form-item
                        label="文档名称"
                        has-feedback=""
                    >
                        <a-input placeholder="请输入文档名称" v-decorator="['docsName', {rules: [{required: true, message: '请输入文档名称'}]}]" />
                    </a-form-item>
                    <a-form-item
                        label="版本号"
                        has-feedback=""
                    >
                        <a-input placeholder="请输入版本号" v-decorator="['docsVersion', {rules: [{required: true, message: '请输入版本号'}]}]" />
                    </a-form-item>
                    <a-form-item>
                        <span slot="label">
                            <span>启动时是否解析</span>
                            <why-box-text text="若为“是”则每次服务启动后会自动生成一次该文档"/>
                        </span>
                        <a-radio-group v-decorator="['sysStartParse', { initialValue: 0, rules: [{required: true, message: ''}] }]" >
                            <a-radio :value="1">是</a-radio>
                            <a-radio :value="0">否</a-radio>
                        </a-radio-group>
                    </a-form-item>
                    <a-form-item
                    >
                        <span slot="label">
                            <span>文档标识码</span>
                            <why-box-text text="用于触发生成文档时作为入参，可多文档使用同一个标识码，但需要下方的秘钥一致才可生成对应文档"/>
                        </span>
                        <a-input placeholder="请输入文档标识码" v-decorator="['docsKey', {rules: [{required: true, message: '请输入文档标识码'}]}]" />
                    </a-form-item>
                    <a-form-item>
                        <span slot="label">
                            <span>触发文档生成需要的秘钥</span>
                            <why-box-text text="用于触发生成文档时身份验证，为空则不进行验证"/>
                        </span>
                        <a-textarea
                            placeholder="请输入触发文档生成需要的秘钥"
                            :rows="2"
                            v-decorator="['apiRunKey']" />
                    </a-form-item>
                    <a-form-item>
                        <span slot="label">
                            <span>触发文档生成的请求地址[HTTP-GET]</span>
                            <copy-text :pre-tip="false" :disabled="id == null || id === ''" placement="top" :value="getRunParseUrl()">
                                <a style="margin-left: 10px" :disabled="id == null || id === ''">点击复制</a>
                            </copy-text>
                        </span>
                        <a-textarea style="color: #444" :rows="2" :value="getRunParseUrl()" :disabled="true" />
                    </a-form-item>
                    <a-form-item>
                        <span slot="label">
                            <span>排序号</span>
                            <why-box-text text="排序号越大越靠前"/>
                        </span>
                        <a-input-number
                            style="width: 100%"
                            v-decorator="['sort', { initialValue: 0 }]" />
                    </a-form-item>
                </a-form>
            </a-tab-pane>
            <a-tab-pane key="2" tab="源码扫描配置">
                <a-form layout="vertical">
                    <a-form-item>
                        <span slot="label">
                            <span class="pre-rule-item">Java源码来源</span>
                            <why-box-text text="本地：源码在部署服务的本地目录；git仓库：生成前根据配置自动拉取远程源码到本地工作空间"/>
                        </span>
                        <a-radio-group v-model="configItem.codeSource">
                            <a-radio value="本地">本地</a-radio>
                            <a-radio value="git仓库">git仓库</a-radio>
                        </a-radio-group>
                    </a-form-item>
                    <a-form-item v-if="configItem.codeSource === 'git仓库'">
                        <span slot="label">
                            <span class="pre-rule-item">git仓库地址</span>
                            <why-box-text text="仅支持.git地址（若为私有仓库则需要配置下面的账号密码）。注：删除文档时不会删除已拉取的源码，若需删除源码请到部署目录“./data/workspace”下进行手动删除"/>
                        </span>
                        <a-textarea :rows="2" placeholder="请输入git仓库地址" v-model="configItem.gitUrl" />
                    </a-form-item>
                    <a-form-item v-if="configItem.codeSource === 'git仓库'">
                        <span slot="label">
                            <span class="pre-rule-item">
                                <span>git拉取分支</span>
                                <a @click="handleGetGitRemoteBranches" style="margin-left: 10px" :disabled="configItem.gitUrl == null || configItem.gitUrl === '' || !configItem.gitUrl.endsWith('.git')">获取远程分支</a>
                            </span>
                            <why-box-text text="填写git仓库地址（私有仓库请先配置下方账号密码）后请点击“获取远程分支”获取远程分支列表"/>
                        </span>
                        <a-select placeholder="请选择git拉取分支" v-model="configItem.gitBranch">
                            <a-select-option v-for="item in gitRemoteBranches" :key="item" :value="item">
                                {{item}}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                    <a-form-item v-if="configItem.codeSource === 'git仓库'" label="git仓库账号">
                        <a-input placeholder="请输入git仓库账号" v-model="configItem.gitUsername" />
                    </a-form-item>
                    <a-form-item v-if="configItem.codeSource === 'git仓库'" label="git仓库密码">
                        <a-input type="password" placeholder="请输入git仓库密码" v-model="configItem.gitPassword" />
                    </a-form-item>
                    <a-form-item v-if="configItem.codeSource !== 'git仓库'">
                        <span slot="label">
                            <span class="pre-rule-item">Java源码所在目录绝对路径</span>
                            <why-box-text text="会扫描这些目录下所有的java文件进行解析，多模块时需填写最顶级目录或所有引用到的模块目录"/>
                        </span>
                        <div :key="index" v-for="(item, index) in configItem.javaFilePaths">
                            <a-input v-model="configItem.javaFilePaths[index]" placeholder="请输入绝对路径" allowClear style="width: calc(100% - 30px); margin-right: 10px; margin-bottom: 10px"/>
                            <a-icon style="font-size: 18px" type="minus-circle-o" @click="configItem.javaFilePaths.splice(index, 1)"/>
                        </div>
                        <a-button type="dashed" style="width: calc(100% - 30px)" @click="configItem.javaFilePaths.push('')">
                            <a-icon type="plus" /> 添加
                        </a-button>
                    </a-form-item>
                    <a-form-item>
                        <span slot="label">
                            <span>仅扫描解析该包集合下的controller类</span>
                            <why-box-text text="必须位于以上源码目录下的包，默认扫描所有"/>
                        </span>
                        <div :key="index" v-for="(item, index) in configItem.filterPackages">
                            <a-input v-model="configItem.filterPackages[index]" placeholder="请输入包名" allowClear style="width: calc(100% - 30px); margin-right: 10px; margin-bottom: 10px"/>
                            <a-icon style="font-size: 18px" type="minus-circle-o" @click="configItem.filterPackages.splice(index, 1)"/>
                        </div>
                        <a-button type="dashed" style="width: calc(100% - 30px)" @click="configItem.filterPackages.push('')">
                            <a-icon type="plus" /> 添加
                        </a-button>
                    </a-form-item>
                    <a-form-item>
                        <span slot="label">
                            <span>仅扫描的controller类</span>
                            <why-box-text text="类全名，如com.test.UserController，优先级低于下方的排除配置"/>
                        </span>
                        <div :key="index" v-for="(item, index) in configItem.filterClassNames">
                            <a-input v-model="configItem.filterClassNames[index]" placeholder="请输入类全名" allowClear style="width: calc(100% - 30px); margin-right: 10px; margin-bottom: 10px"/>
                            <a-icon style="font-size: 18px" type="minus-circle-o" @click="configItem.filterClassNames.splice(index, 1)"/>
                        </div>
                        <a-button type="dashed" style="width: calc(100% - 30px)" @click="configItem.filterClassNames.push('')">
                            <a-icon type="plus" /> 添加
                        </a-button>
                    </a-form-item>
                    <a-form-item>
                        <span slot="label">
                            <span>需要排除的controller类</span>
                            <why-box-text text="类全名，如com.test.UserController，优先级高于上方的仅扫描配置"/>
                        </span>
                        <div :key="index" v-for="(item, index) in configItem.ignoreClassNames">
                            <a-input v-model="configItem.ignoreClassNames[index]" placeholder="请输入类全名" allowClear style="width: calc(100% - 30px); margin-right: 10px; margin-bottom: 10px"/>
                            <a-icon style="font-size: 18px" type="minus-circle-o" @click="configItem.ignoreClassNames.splice(index, 1)"/>
                        </div>
                        <a-button type="dashed" style="width: calc(100% - 30px)" @click="configItem.ignoreClassNames.push('')">
                            <a-icon type="plus" /> 添加
                        </a-button>
                    </a-form-item>
                </a-form>
            </a-tab-pane>
            <a-tab-pane key="3" tab="参数验证描述注入">
                <code-editor
                    :value="configItem.paramValidFunc"
                    @input="(e)=>{configItem.paramValidFunc = e}"
                    :sub-height="280"
                    language="javascript"
                    btn="恢复默认值"
                    @btn="()=> {configItem.paramValidFunc = defaultParamValidFuc}"
                ></code-editor>
            </a-tab-pane>
            <a-tab-pane key="4" tab="字段默认值注入">
                <code-editor
                    v-model="configItem.paramDefaultValueFunc"
                    :sub-height="280"
                    language="javascript"
                    btn="恢复默认值"
                    @btn="()=> {configItem.paramDefaultValueFunc = defaultParamDefaultValueFunc}"
                ></code-editor>
            </a-tab-pane>
        </a-tabs>
    </drawer-box>
</template>

<script>
import {add, edit, getDocsConfig, getGitRemoteBranches} from '@/api/docsConfig'

export default {
    name: "EditDocsConfig",
    data() {
        return {
            defaultParamValidFuc,
            defaultParamDefaultValueFunc,
            id: null,
            visible: false,
            loading: false,
            labelCol: { span: 4 },
            wrapperCol: { span: 20 },
            form: this.$form.createForm(this),
            configItem: {
                docsName: '',
                docsVersion: '',
                sysStartParse: 0,
                docsKey: '',
                apiRunKey: '',
                sort: 0,
                javaFilePaths: [],
                filterPackages: [],
                filterClassNames: [],
                ignoreClassNames: [],
                paramValidFunc: defaultParamValidFuc,
                paramDefaultValueFunc: defaultParamDefaultValueFunc,
                codeSource: '本地',
                gitUrl: '',
                gitBranch: '111',
                gitUsername: '',
                gitPassword: '',
            },
            gitRemoteBranches: [],
        }
    },
    methods: {
        open(id) {
            this.visible = true
            this.id = id || ''
            if (id) {
                this.loading = true
                getDocsConfig(id).then(res => {
                    if (res['data']) {
                        const data = res['data']
                        this.configItem = {
                            docsName: data.docsName,
                            docsVersion: data.docsVersion,
                            sysStartParse: data.sysStartParse,
                            docsKey: data.docsKey,
                            apiRunKey: data.apiRunKey,
                            javaFilePaths: data.javaFilePaths || [],
                            filterPackages: data.filterPackages || [],
                            filterClassNames: data.filterClassNames || [],
                            ignoreClassNames: data.ignoreClassNames || [],
                            paramValidFunc: data.paramValidFunc,
                            paramDefaultValueFunc: data.paramDefaultValueFunc,
                            sort: data.sort,
                            codeSource: data.codeSource || '本地',
                            gitUrl: data.gitUrl || '',
                            gitBranch: data.gitBranch || '',
                            gitUsername: data.gitUsername || '',
                            gitPassword: data.gitPassword || '',
                        }
                        this.$nextTick(()=> {
                            this.form.setFieldsValue({
                                docsName: data.docsName,
                                docsVersion: data.docsVersion,
                                sysStartParse: data.sysStartParse,
                                docsKey: data.docsKey,
                                apiRunKey: data.apiRunKey,
                                sort: data.sort
                            })
                        })
                    }
                }).finally(()=> {
                    this.loading = false
                })
            }
        },
        handleSave() {
            this.form.validateFields((errors, values)=> {
                if (!errors) {
                    const codeSource = this.configItem.codeSource
                    if (codeSource.length < 1) {
                        this.$message.warn('请先配置源码扫码配置：java源码来源')
                        return
                    }
                    values['javaFilePaths'] = this.configItem.javaFilePaths.filter(item => !!item.trim())
                    const gitUrl = this.configItem.gitUrl || ''
                    const gitBranch = this.configItem.gitBranch || ''
                    if (codeSource === 'git仓库') {
                        if (gitUrl.length < 1) {
                            this.$message.warn('请先配置源码扫码配置：git仓库地址')
                            return
                        }
                        if (!gitUrl.endsWith('.git')) {
                            this.$message.warn('请先配置源码扫码配置：git仓库地址必须以.git结尾')
                            return
                        }
                        if (gitBranch.length < 1) {
                            this.$message.warn('请先配置源码扫码配置：git拉取分支')
                            return
                        }
                    } else {
                        if (values['javaFilePaths'].length < 1) {
                            this.$message.warn('请先配置源码扫码配置：Java源码所在目录绝对路径')
                            return
                        }
                    }
                    values['codeSource'] = codeSource
                    values['gitUrl'] = gitUrl
                    values['gitBranch'] = gitBranch
                    values['gitUsername'] = this.configItem.gitUsername || ''
                    values['gitPassword'] = this.configItem.gitPassword || ''
                    values['filterPackages'] = this.configItem.filterPackages.filter(item => !!item.trim())
                    values['filterClassNames'] = this.configItem.filterClassNames.filter(item => !!item.trim())
                    values['ignoreClassNames'] = this.configItem.ignoreClassNames.filter(item => !!item.trim())
                    values['paramValidFunc'] = this.configItem.paramValidFunc
                    values['paramDefaultValueFunc'] = this.configItem.paramDefaultValueFunc
                    this.loading = true
                    if (!this.id) {
                        add(values).then(()=> {
                            this.$message.success('新增成功')
                            this.$emit('ok')
                            this.close()
                        }).finally(()=> {
                            this.loading = false
                        })
                    } else {
                        values['id'] = this.id
                        edit(values).then(()=> {
                            this.$message.success('保存成功')
                            this.$emit('ok')
                            this.close()
                        }).finally(()=> {
                            this.loading = false
                        })
                    }
                }
            })
        },
        handleGetGitRemoteBranches(showMsg) {
            this.loading = true
            getGitRemoteBranches(this.configItem.gitUrl, this.configItem.gitUsername, this.configItem.gitPassword).then(res => {
                if (res['data']) {
                    const list = res['data'] || []
                    this.gitRemoteBranches = list
                    if (showMsg) {
                        this.$message.success('获取git远程分支成功')
                        if (list.length > 0) {
                            const gitBranch = this.configItem.gitBranch || ''
                            if (gitBranch.length < 1) {
                                this.configItem.gitBranch = list[0]
                            } else {
                                if (!list.includes(gitBranch)) {
                                    this.$message.warning(`分支${gitBranch}不存在远程分支列表，已切换为默认分支${list[0]}`)
                                    this.configItem.gitBranch = list[0]
                                }
                            }
                        }
                    }
                }
            }).finally(()=> {
                this.loading = false
            })
        },
        handleTab(key) {
            if (key == 2 && this.id && this.configItem.codeSource === 'git仓库') {
                this.handleGetGitRemoteBranches(false)
            }
        },
        close() {
            this.form.resetFields()
            this.configItem = {
                docsName: '',
                docsVersion: '',
                sysStartParse: 0,
                docsKey: '',
                apiRunKey: '',
                sort: 0,
                javaFilePaths: [],
                filterPackages: [],
                filterClassNames: [],
                ignoreClassNames: [],
                paramValidFunc: defaultParamValidFuc,
                paramDefaultValueFunc: defaultParamDefaultValueFunc,
                codeSource: '本地',
                gitUrl: '',
                gitBranch: '',
                gitUsername: '',
                gitPassword: '',
            }
            this.gitRemoteBranches = []
            this.visible = false
        },
        getRunParseUrl() {
            const docsKey = this.form.getFieldValue("docsKey") || ""
            if (!docsKey) {
                return ''
            }
            const apiRunKey = this.form.getFieldValue("apiRunKey") || ""
            let url = `${window.location.origin}/lime_japi_docs/api/docs/run_docs_parsing?docsKey=${docsKey}`
            if (apiRunKey) {
                url += `&password=${apiRunKey}`
            }
            return url
        }
    }
}

const defaultParamValidFuc = `
/**
 * 参数验证描述注入回调函数（请勿修改函数名！！！）
 * @param annotationNames 参数的（多个）注解名称数组
 * @param fieldName 参数字段名称
 * @param fieldComment 参数字段注释
 * @return 注入的参数验证描述
 */
function valid(annotationNames, fieldName, fieldComment) {
  if(annotationNames.includes('NotNull')) {
    return '对象非空';
  }
  if(annotationNames.includes('NotBlank')) {
    return '字符串非空';
  }
  return '';
}
`
const defaultParamDefaultValueFunc = `
/**
 * 字段默认值注入回调函数（请勿修改函数名！！！）
 * @param type 字段类型（Java类型，基本数据类型->包装类）
 * @param fieldName 参数字段名称
 * @param fieldComment 参数字段注释
 * @return 注入的默认值
 */
function defaultValue(type, fieldName, fieldComment) {
  if (type == 'String') {
    // 返回字符串的话双引号需要自己拼接
    return '"字符串"';
  }
  if (type == 'Date') {
    return '"2023-10-29 19:16:00"';
  }
  if (type == 'Boolean') {
    return true;
  }
  if (['Integer', 'BigInteger', 'Long'].includes(type)) {
    return 0;
  }
  if (['Double', 'Float', 'BigDecimal'].includes(type)) {
    return 0.1;
  }
  return '';
}
`


</script>

<style scoped lang="less">
///deep/ .ant-form-item-required::before {
//    position: absolute;
//    top: 2px;
//    right: -13px;
//}
/deep/ .ant-drawer-body {
    padding-top: 0!important;
}
</style>
