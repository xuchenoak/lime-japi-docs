<template>
    <a-modal
        title="登录"
        :width="600"
        :visible="visible"
        okText="登录"
        cancelText="取消"
        @ok="handleSave"
        @cancel="handleCancel"
        :afterClose="handleClose"
        :destroyOnClose="true"
    >
        <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item
                label="账号"
                has-feedback=""
            >
                <a-input placeholder="请输入账号" @keyup.enter="handleSave" v-decorator="['account', {rules: [{required: true, message: '请输入账号'}]}]" />
            </a-form-item>
            <a-form-item
                label="密码"
                has-feedback=""
            >
                <a-input type="password" autocomplete="off" placeholder="请输入密码" @keyup.enter="handleSave" v-decorator="['password', {rules: [{required: true, message: '请输入密码'}]}]" />
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script>

import {login} from "@/api/storage/loginStorage";

export default {
    name: 'DocsConfigLogin',
    data () {
        return {
            labelCol: { span: 4 },
            wrapperCol: { span: 18 },
            visible: false,
            form: this.$form.createForm(this),
        }
    },
    methods: {

        init() {
            this.visible = true
        },

        handleSave () {
            const { form: { validateFields } } = this
            validateFields((errors, values) => {
                if (!errors) {
                    const account = values['account']
                    const password = values['password']
                    login(account, password).then(res => {
                        if (res['data'].hasLogin) {
                            this.handleCancel()
                            this.$message.success("登录成功")
                            return
                        }
                        this.$message.warn("账号或密码错误")
                    })

                }
            })
        },

        handleCancel () {
            this.form.resetFields()
            this.visible = false
        },

        handleClose() {
            this.$emit('close')
        }

    }
}
</script>
<style scoped lang="less">
</style>
