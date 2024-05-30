<template>
    <loading :loading="loading">
        <div class="project-header-bg">
            <a-row class="project-header">
                <a-col :span="12">
                    <a-space align="center" size="small">
                        <lime-logo :height="65" :open-click="true"/>
                        <div style="height: 80px; line-height: 80px;">
                            <span style="padding: 3px 15px; border-left: 1px solid #aaa; font-weight: 300">系统初始化</span>
                        </div>
                    </a-space>
                </a-col>
            </a-row>
        </div>
        <div class="project-list-bg">
            <div class="project-list-box">
                <a-form :form="form" layout="vertical" style="padding: 20px 0 5px 0">
                    <a-row :gutter="24">
                        <a-col class="gutter-row" :span="12">
                            <a-form-item
                                has-feedback=""
                            >
                                <span slot="label">
                                    <span>系统名称</span>
                                    <why-box-text text="用于文档中心显示（默认显示“接口文档中心”）"/>
                                </span>
                                <a-input placeholder="请输入系统名称" v-decorator="['sysName', {rules: [{required: true, message: '请输入系统名称'}]}]" />
                            </a-form-item>
                        </a-col>
                        <a-col class="gutter-row" :span="12">
                            <a-form-item
                                has-feedback=""
                            >
                                <span slot="label">
                                    <span>系统标语</span>
                                    <why-box-text text="用于文档首页显示"/>
                                </span>
                                <a-input placeholder="请输入系统标语" v-decorator="['sysSlogan', {rules: [{required: true, message: '请输入系统标语'}]}]" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col class="gutter-row" :span="12">
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
                                    <img :src="logoUrl ? logoUrl : '/lime-logo.png'" width="390px" height="120px" />
                                </a-upload>
                                <a v-if="logoUrl" @click="logoUrl = null">点击恢复默认</a>
                            </a-form-item>
                        </a-col>
                        <a-col class="gutter-row" :span="12">
                            <a-form-item
                                has-feedback=""
                            >
                        <span slot="label">
                            <span>文档中心邀请码</span>
                            <why-box-text text="设置后访问系统主页会提示输入该邀请码，否则无法查看文档"/>
                        </span>
                                <a-input placeholder="请输入文档中心邀请码" v-decorator="['docsViewKey', {rules: [{required: true, message: '请输入文档中心邀请码'}]}]" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col class="gutter-row" :span="12">
                            <a-form-item
                                has-feedback=""
                            >
                                <span slot="label">
                                    <span>管理员账号</span>
                                    <why-box-text text="用于系统管理身份认证"/>
                                </span>
                                <a-input placeholder="请输入管理员账号" v-decorator="['account', {rules: [{required: true, message: '请输入管理员账号'}]}]" />
                            </a-form-item>
                        </a-col>
                        <a-col class="gutter-row" :span="12">
                            <a-form-item
                                has-feedback=""
                            >
                                <span slot="label">
                                    <span>管理员密码</span>
                                    <why-box-text text="用于系统管理身份认证"/>
                                </span>
                                <a-input type="password" autocomplete="off" placeholder="请输入管理员密码" v-decorator="['password', {rules: [{required: true, message: '请输入管理员密码'}]}]" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-form-item style="text-align: right">
                        <a-button type="primary" html-type="submit" @click="handleSubmit">提交</a-button>
                    </a-form-item>
                </a-form>
            </div>
        </div>
        <powered-box style="position:fixed; bottom: 0; background-color: #F3F5F7"/>
    </loading>
</template>

<script>
import {sysInit} from "@/api/sysConfig";
const getBase64 = (img, callback) => {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result));
    reader.readAsDataURL(img);
}
export default {
    components: { },
    name: "index",
    data() {
        return {
            loading: false,
            form: this.$form.createForm(this),
            logoUrl: '',
        };
    },
    created() {
        this.$nextTick(()=> {
            this.logoUrl = ''
            this.form.setFieldsValue({
                sysName: '接口文档中心',
                sysSlogan: '这是一个简单的Java接口文档',
                docsViewKey: '',
                account: '',
                password: ''
            })
        })
    },
    methods: {
        handleSubmit() {
            this.form.validateFields((errors, values)=> {
                if (!errors) {
                    values['logoUrl'] = this.logoUrl
                    this.loading = true
                    sysInit(values).then(() => {
                        this.$router.push({path: '/'})
                    }).finally(() => {
                        this.loading = false
                    })
                }
            })
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
    },

}
</script>

<style scoped lang="less">
.project-header-bg {
    width: 100%;
    z-index: 1;
    background: #fff;
    height: 80px;
    padding: 0 25px;
    font-size: 20px;
    position: fixed;
    box-shadow: 2px 2px 25px rgba(0,0,0,.05);
    .project-header {
        margin: 0 auto;
        min-width: 800px;
        max-width: 1000px;
    }
}
.project-list-bg {
    position: relative;
    width: 100%;
    padding: 110px 0 50px 0;
    background: #F3F5F7;
    max-height: 100vh;
    overflow-y: auto;
    .project-list-box {
        position: relative;
        min-width: 800px;
        max-width: 1000px;
        margin: 0 auto;
        background: #fff;
        padding: 0 25px;
        box-shadow: 2px 2px 25px rgba(0,0,0,.05);
    }
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
