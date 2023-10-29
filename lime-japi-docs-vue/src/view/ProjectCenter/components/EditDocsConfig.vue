<template>
    <drawer-box
        title="文档配置"
        :width="700"
        :loading="loading"
        :visible="visible"
        @close="close"
        @save="handleSave"
    >
        <a-tabs default-active-key="1">
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
                    <a-form-item
                        label="启动时是否解析"
                    >
                        <a-radio-group v-decorator="['sysStartParse', { initialValue: 0, rules: [{required: true, message: ''}] }]" >
                            <a-radio :value="1">是</a-radio>
                            <a-radio :value="0">否</a-radio>
                        </a-radio-group>
                    </a-form-item>
                    <a-form-item
                        label="文档解析执行秘钥"
                    >
                        <a-textarea
                            placeholder="请输入文档解析执行秘钥"
                            :rows="3"
                            v-decorator="['apiRunKey']" />
                    </a-form-item>
                </a-form>
            </a-tab-pane>
            <a-tab-pane key="2" tab="源码扫描配置">
                <a-form-item
                    label="Java源码所在目录绝对路径（必须写到java目录，多模块时填写多个）"
                >
                    <div :key="index" v-for="(item, index) in configItem.javaFilePaths">
                        <a-input v-model="configItem.javaFilePaths[index]" placeholder="请输入绝对路径" allowClear style="width: calc(100% - 30px); margin-right: 10px; margin-bottom: 10px"/>
                        <a-icon style="font-size: 18px" type="minus-circle-o" @click="configItem.javaFilePaths.splice(index, 1)"/>
                    </div>
                    <a-button type="dashed" style="width: calc(100% - 30px)" @click="configItem.javaFilePaths.push('')">
                        <a-icon type="plus" /> 添加
                    </a-button>
                </a-form-item>
                <a-form-item
                    label="仅扫描解析该包集合下的controller类（必须位于源码目录下的包，默认扫描所有）"
                >
                    <div :key="index" v-for="(item, index) in configItem.filterPackages">
                        <a-input v-model="configItem.filterPackages[index]" placeholder="请输入包名" allowClear style="width: calc(100% - 30px); margin-right: 10px; margin-bottom: 10px"/>
                        <a-icon style="font-size: 18px" type="minus-circle-o" @click="configItem.filterPackages.splice(index, 1)"/>
                    </div>
                    <a-button type="dashed" style="width: calc(100% - 30px)" @click="configItem.filterPackages.push('')">
                        <a-icon type="plus" /> 添加
                    </a-button>
                </a-form-item>
                <a-form-item
                    label="仅扫描的controller类名集（非类全名，如UserController，优先级高于下方排除）"
                >
                    <div :key="index" v-for="(item, index) in configItem.filterClassNames">
                        <a-input v-model="configItem.filterClassNames[index]" placeholder="请输入类名（非类全名，如UserController）" allowClear style="width: calc(100% - 30px); margin-right: 10px; margin-bottom: 10px"/>
                        <a-icon style="font-size: 18px" type="minus-circle-o" @click="configItem.filterClassNames.splice(index, 1)"/>
                    </div>
                    <a-button type="dashed" style="width: calc(100% - 30px)" @click="configItem.filterClassNames.push('')">
                        <a-icon type="plus" /> 添加
                    </a-button>
                </a-form-item>
                <a-form-item
                    label="需要排除的controller类名集（非类全名，如UserController）"
                >
                    <div :key="index" v-for="(item, index) in configItem.ignoreClassNames">
                        <a-input v-model="configItem.ignoreClassNames[index]" placeholder="请输入绝对路径" allowClear style="width: calc(100% - 30px); margin-right: 10px; margin-bottom: 10px"/>
                        <a-icon style="font-size: 18px" type="minus-circle-o" @click="configItem.ignoreClassNames.splice(index, 1)"/>
                    </div>
                    <a-button type="dashed" style="width: calc(100% - 30px)" @click="configItem.ignoreClassNames.push('')">
                        <a-icon type="plus" /> 添加
                    </a-button>
                </a-form-item>
            </a-tab-pane>
            <a-tab-pane key="3" tab="参数验证描述注入">
                <code-editor
                    :value="configItem.paramValidFunc"
                    @input="(e)=>{configItem.paramValidFunc = e}"
                    :sub-height="280"
                    language="javascript"
                ></code-editor>
            </a-tab-pane>
            <a-tab-pane key="4" tab="字段默认值注入">
                <code-editor
                    v-model="configItem.paramDefaultValueFunc"
                    :sub-height="280"
                    language="javascript"
                ></code-editor>
            </a-tab-pane>
        </a-tabs>
    </drawer-box>
</template>

<script>
import {add, edit, getDocsConfig} from '@/api/docsConfig'
export default {
    name: "EditDocsConfig",
    data() {
        return {
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
                apiRunKey: '',
                javaFilePaths: [],
                filterPackages: [],
                filterClassNames: [],
                ignoreClassNames: [],
                paramValidFunc: defaultParamValidFuc,
                paramDefaultValueFunc: defaultParamDefaultValueFunc,
            },
        }
    },
    methods: {
        open(id) {
            this.visible = true
            this.id = id
            if (id) {
                this.loading = true
                getDocsConfig(id).then(res => {
                    if (res['data']) {
                        const data = res['data']
                        this.configItem = {
                            docsName: data.docsName,
                            docsVersion: data.docsVersion,
                            sysStartParse: data.sysStartParse,
                            apiRunKey: data.apiRunKey,
                            javaFilePaths: data.javaFilePaths || [],
                            filterPackages: data.filterPackages || [],
                            filterClassNames: data.filterClassNames || [],
                            ignoreClassNames: data.ignoreClassNames || [],
                            paramValidFunc: data.paramValidFunc,
                            paramDefaultValueFunc: data.paramDefaultValueFunc
                        }
                        this.$nextTick(()=> {
                            this.form.setFieldsValue({
                                docsName: data.docsName,
                                docsVersion: data.docsVersion,
                                sysStartParse: data.sysStartParse,
                                apiRunKey: data.apiRunKey
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
                    values['javaFilePaths'] = this.configItem.javaFilePaths.filter(item => !!item.trim())
                    values['filterPackages'] = this.configItem.filterPackages.filter(item => !!item.trim())
                    values['filterClassNames'] = this.configItem.filterClassNames.filter(item => !!item.trim())
                    values['ignoreClassNames'] = this.configItem.ignoreClassNames.filter(item => !!item.trim())
                    values['paramValidFunc'] = this.configItem.paramValidFunc
                    values['paramDefaultValueFunc'] = this.configItem.paramDefaultValueFunc
                    console.log(values, 'v')
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
        close() {
            this.form.resetFields()
            this.configItem = {
                docsName: '',
                docsVersion: '',
                sysStartParse: 0,
                apiRunKey: '',
                javaFilePaths: [],
                filterPackages: [],
                filterClassNames: [],
                ignoreClassNames: [],
                paramValidFunc: defaultParamValidFuc,
                paramDefaultValueFunc: defaultParamDefaultValueFunc,
            }
            this.visible = false
        },
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
/deep/ .ant-form-item-required::before {
    position: absolute;
    top: 2px;
    right: -13px;
}
/deep/ .ant-drawer-body {
    padding-top: 0!important;
}
</style>
