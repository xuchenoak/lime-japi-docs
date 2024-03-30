<template>
    <drawer-box
        title="系统配置"
        :width="700"
        :loading="loading"
        :visible="visible"
        @close="close"
        @save="handleSave"
    >
        <a-form :form="form" layout="vertical" style="padding-top: 20px">
            <a-form-item
                has-feedback=""
            >
                <span slot="label">
                    <span>系统名称</span>
                    <why-box-text text="用于文档中心显示（默认显示“接口文档中心”）"/>
                </span>
                <a-input placeholder="请输入系统名称" v-decorator="['sysName', {rules: [{required: true, message: '请输入系统名称'}]}]" />
            </a-form-item>
            <a-form-item>
                <span slot="label">
                    <span class="pre-rule-item">系统LOGO</span>
                    <why-box-text text="点击可重新上传更换，图片比例：13:4"/>
                </span>
                <a-upload
                    class="avatar-uploader-high"
                    :show-upload-list="false"
                    accept="image/png"
                    list-type="picture-card"
                    :custom-request="handleUploadLogo"
                    :before-upload="beforeUploadLogo"
                >
                    <img :src="configItem.logoUrl ? configItem.logoUrl : img" width="390px" height="120px" />
                </a-upload>
            </a-form-item>
            <a-form-item
                has-feedback=""
            >
                <span slot="label">
                    <span>系统标语</span>
                    <why-box-text text="用于文档首页显示"/>
                </span>
                <a-input placeholder="请输入系统标语" v-decorator="['sysSlogan', {rules: [{required: true, message: '请输入系统标语'}]}]" />
            </a-form-item>
            <a-form-item
                has-feedback=""
            >
                <span slot="label">
                    <span>系统管理秘钥</span>
                    <why-box-text text="用于管理文档时验证系统管理员身份"/>
                </span>
                <a-input placeholder="请输入文档管理秘钥" v-decorator="['docsConfigKey', {rules: [{required: true, message: '请输入文档管理秘钥'}]}]" />
            </a-form-item>
            <a-form-item
                has-feedback=""
            >
                <span slot="label">
                    <span>文档中心邀请码</span>
                    <why-box-text text="设置后访问系统主页会提示输入该邀请码，否则无法查看文档"/>
                </span>
                <a-input placeholder="请输入文档中心邀请码" v-decorator="['docsViewKey', {rules: [{required: true, message: '请输入文档中心邀请码'}]}]" />
            </a-form-item>
        </a-form>
    </drawer-box>
</template>

<script>
import img from "@/assets/images/lime-logo.png"
// eslint-disable-next-line no-unused-vars
const getBase64 = (img, callback) => {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result));
    reader.readAsDataURL(img);
}
import {add, edit, getDocsConfig} from '@/api/docsConfig'
export default {
    name: "EditDocsConfig",
    data() {
        return {
            img,
            visible: false,
            loading: false,
            labelCol: { span: 4 },
            wrapperCol: { span: 20 },
            form: this.$form.createForm(this),
            configItem: {
                sysName: '',
                logoUrl: '',
                sysSlogan: '',
                docsConfigKey: '',
                docsViewKey: ''
            },
        }
    },
    methods: {
        open() {
            this.visible = true
            // this.loading = true
            // getDocsConfig().then(res => {
            //     if (res['data']) {
            //         const data = res['data']
            const item = {
                sysName: '接口文档中心',
                logoUrl: '',
                sysSlogan: '这是一个简单的Java接口文档',
                docsConfigKey: '',
                docsViewKey: ''
            }
            this.configItem = item
            this.$nextTick(()=> {
                this.form.setFieldsValue({
                    sysName: item.sysName,
                    logoUrl: item.logoUrl,
                    sysSlogan: item.sysSlogan,
                    docsConfigKey: item.docsConfigKey,
                    docsViewKey: item.docsViewKey
                })
            })
            //     }
            // }).finally(()=> {
            //     this.loading = false
            // })
        },
        handleSave() {
            this.form.validateFields((errors, values)=> {
                if (!errors) {
                    values['logoUrl'] = this.configItem.logoUrl
                    console.log("values", values)
                }
            })
        },
        close() {
            this.form.resetFields()
            this.configItem = {
                sysName: '',
                logoUrl: '',
                sysSlogan: '',
                docsConfigKey: '',
                docsViewKey: ''
            }
            this.visible = false
        },
        getRunParseUrl() {
            const apiRunKey = this.form.getFieldValue("apiRunKey") || ""
            return `${window.location.origin}/lime_japi_docs/api/docs/run_docs_parse?docsConfigId=${this.id}&password=${apiRunKey}`
        },
        handleUploadLogo({file}) {
            getBase64(file, imageUrl => {
                this.configItem.logoUrl = imageUrl;
            });
        },
        beforeUploadLogo(file) {
            const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
            if (!isJpgOrPng) {
                this.$notification.warn({message: '仅支持png格式'});
            }
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isLt2M) {
                this.$notification.warn({message: '文件大小仅支持2MB以内'});
            }
            return isJpgOrPng && isLt2M;
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
/deep/ .avatar-uploader-high > .ant-upload {
    width: 390px!important;
    height: 120px!important;;
}
/deep/ .ant-upload.ant-upload-select-picture-card {
    margin: 0;
}
.ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
}
</style>
