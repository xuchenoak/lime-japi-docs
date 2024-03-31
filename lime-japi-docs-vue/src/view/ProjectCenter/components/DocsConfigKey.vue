<template>
    <a-modal
        title="验证系统管理秘钥"
        :width="600"
        :visible="visible"
        okText="验证"
        cancelText="取消"
        @ok="handleSave"
        @cancel="handleCancel"
        :afterClose="handleClose"
        :destroyOnClose="true"
    >
        <a-form :form="form" layout="vertical">
            <a-form-item>
                <span slot="label">
                    <span>系统管理秘钥</span>
                    <why-box-text text="验证通过后可以调整系统配置和管理文档"/>
                </span>
                <a-input
                    class="code-input"
                    allowClear
                    @pressEnter="handleSave"
                    placeholder="请输入系统管理秘钥"
                    v-decorator="['docsConfigKey']"
                />
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script>

export default {
    name: 'DocsConfigKey',
    data () {
        return {
            labelCol: { span: 6 },
            wrapperCol: { span: 18 },
            visible: false,
            form: this.$form.createForm(this),
        }
    },
    methods: {

        init() {
            const docsConfigKey = localStorage.getItem("docs_config_key")
            this.visible = true
            this.$nextTick(()=> {
                this.form.setFieldsValue({docsConfigKey: docsConfigKey || ''})
            })
        },

        handleSave () {
            const { form: { validateFields } } = this
            validateFields((errors, values) => {
                const docsConfigKey = values['docsConfigKey']
                this.handleCancel()
                this.$emit('save', docsConfigKey)
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
