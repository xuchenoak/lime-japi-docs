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
                    <img :src="logoUrl ? logoUrl : img" width="390px" height="120px" />
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
                    <span>文档中心邀请码</span>
                    <why-box-text text="设置后访问系统主页会提示输入该邀请码，否则无法查看文档"/>
                </span>
                <a-input placeholder="请输入文档中心邀请码" v-decorator="['docsViewKey', {rules: [{required: true, message: '请输入文档中心邀请码'}]}]" />
            </a-form-item>
            <a-form-item
                has-feedback=""
            >
                <span slot="label">
                    <span>管理员账号</span>
                    <why-box-text text="用于系统管理身份认证"/>
                </span>
                <a-input placeholder="请输入管理员账号" v-decorator="['account', {rules: [{required: true, message: '请输入管理员账号'}]}]" />
            </a-form-item>
            <a-form-item
                has-feedback=""
            >
                <span slot="label">
                    <span>管理员密码</span>
                    <why-box-text text="用于系统管理身份认证"/>
                </span>
                <a-input type="password" placeholder="请输入管理员密码" v-decorator="['password', {rules: [{required: true, message: '请输入管理员密码'}]}]" />
            </a-form-item>
        </a-form>
    </drawer-box>
</template>

<script>
import img from "@/assets/images/lime-logo.png"
const getBase64 = (img, callback) => {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result));
    reader.readAsDataURL(img);
}
import {getSysConfig, saveSysConfig} from '@/api/sysConfig'
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
            logoUrl: '',
        }
    },
    methods: {
        open() {
            this.visible = true
            this.loading = true
            getSysConfig().then(res => {
                if (res['data']) {
                    const data = res['data']
                    this.logoUrl = data.logoUrl
                    this.$nextTick(()=> {
                        this.form.setFieldsValue({
                            sysName: data.sysName || '接口文档中心',
                            sysSlogan: data.sysSlogan || '这是一个简单的Java接口文档',
                            docsViewKey: data.docsViewKey || '',
                            account: data.account || '',
                            password: data.password || ''
                        })
                    })
                }
            }).finally(()=> {
                this.loading = false
            })
        },
        handleSave() {
            this.form.validateFields((errors, values)=> {
                if (!errors) {
                    values['logoUrl'] = this.logoUrl
                    this.loading = true
                    saveSysConfig(values).then(() => {
                        this.close()
                    }).finally(() => {
                        this.loading = false
                    })
                }
            })
        },
        close() {
            this.form.resetFields()
            this.logoUrl = ''
            this.visible = false
        },
        getRunParseUrl() {
            const apiRunKey = this.form.getFieldValue("apiRunKey") || ""
            return `${window.location.origin}/lime_japi_docs/api/docs/run_docs_parse?docsConfigId=${this.id}&password=${apiRunKey}`
        },
        handleUploadLogo({file}) {
            getBase64(file, imageUrl => {
                this.logoUrl = imageUrl;
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
    height: 120px!important;
}
/deep/ .ant-upload.ant-upload-select-picture-card {
    margin: 0;
}
.ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
}
</style>
