<template>
    <a-modal
        title="验证文档管理秘钥"
        :width="600"
        :visible="visible"
        okText="验证"
        cancelText="取消"
        @ok="handleSave"
        @cancel="handleCancel"
        :afterClose="handleClose"
        :destroyOnClose="true"
    >
        <a-form :form="form">
            <a-form-item
                label="文档管理秘钥"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
            >
                <a-input
                    class="code-input"
                    allowClear
                    @pressEnter="handleSave"
                    placeholder="请输入文档管理秘钥"
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
            labelCol: { span: 5 },
            wrapperCol: { span: 19 },
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
