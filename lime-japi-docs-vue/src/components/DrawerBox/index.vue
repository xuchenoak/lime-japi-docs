<template>
    <a-drawer
        :title="title"
        :width="width"
        :visible="visible"
        :destroyOnClose="true"
        @close="closeDrawer"
    >
        <loading :loading="loading">
            <slot></slot>
        </loading>
        <div class="edit-view-btn">
            <a-button :style="{ marginRight: '8px' }" @click="closeDrawer">取消</a-button>
            <a-button type="primary" :disabled="saveBtnDisabled" :loading="loading" @click="handleSave">保存</a-button>
        </div>
    </a-drawer>
</template>

<script>

export default {
    name: "index",
    props: {
        visible: {
            type: Boolean,
            default() { return false }
        },
        loading: {
            type: Boolean,
            default() { return false }
        },
        title: {
            type: String,
            default() { return '标题' }
        },
        width: {
            type: Number,
            default() { return 600 }
        },
        saveBtnDisabled: {
            type: Boolean,
            default() { return false }
        }
    },
    methods: {
        closeDrawer() {
            if (!this.loading) {
                this.$emit('close')
            }
        },
        handleSave() {
            this.$emit('save')
        }
    }
}
</script>

<style scoped lang="less">
.edit-view-btn {
    position: absolute;
    right: 0;
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e9e9e9;
    padding: 10px 16px;
    background: #fff;
    text-align: right;
    z-index: 9999;
}

/deep/ .ant-drawer-header {
    padding: 14px 24px;
    button {
        height: 50px;
        line-height: 50px;
    }
}
/deep/ .ant-drawer-body {
    height: calc(100vh - 80px);
    padding-bottom: 80px;
    overflow: auto;
}
</style>
