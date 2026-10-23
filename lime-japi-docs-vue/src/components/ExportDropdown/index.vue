<template>
    <a-dropdown :trigger="['hover']" :disabled="disabled" placement="bottomRight">
        <a-icon
            class="export-icon"
            :type="exporting ? 'loading' : 'export'"
            :spin="exporting"
            :title="tip"
        />
        <a-menu slot="overlay" @click="handleMenuClick">
            <a-menu-item key="openapi-preview">
                <a-icon type="global"/>
                OpenAPI 在线预览
            </a-menu-item>
            <a-menu-item key="openapi-file">
                <a-icon type="file-text"/>
                OpenAPI JSON 文件
            </a-menu-item>
            <a-menu-item key="markdown">
                <a-icon type="file-text"/>
                Markdown 文件
            </a-menu-item>
        </a-menu>
    </a-dropdown>
</template>

<script>
import {downloadMarkdown, downloadOpenApi, previewOpenApi} from '@/util/docsExport'

export default {
    name: "ExportDropdown",
    props: {
        /**
         * 导出参数
         */
        params: {
            type: Object,
            default() {
                return {}
            }
        },
        /**
         * 是否禁用
         */
        disabled: {
            type: Boolean,
            default: false
        },
        /**
         * 悬浮提示
         */
        tip: {
            type: String,
            default: '导出'
        }
    },
    data() {
        return {
            exporting: false
        }
    },
    methods: {
        /**
         * 菜单点击
         *
         * @param key 菜单标识
         */
        handleMenuClick({key}) {
            if (this.disabled || this.exporting) {
                return
            }
            if (key === 'openapi-preview') {
                if (!previewOpenApi(this.params)) {
                    this.$message.warning('浏览器阻止了新标签页，请允许弹窗后重试')
                }
                return
            }
            this.exporting = true
            const action = key === 'markdown' ? downloadMarkdown : downloadOpenApi
            action(this.params)
                .then(() => {
                    this.$message.success('导出成功')
                })
                .catch(e => {
                    this.$message.error(e.message || '导出失败')
                })
                .finally(() => {
                    this.exporting = false
                })
        }
    }
}
</script>

<style scoped lang="less">
.export-icon {
    display: inline-block;
    padding: 4px;
    font-size: 16px;
    color: #999;
    cursor: pointer;
    border-radius: 2px;
    transition: all .2s;
}

.export-icon:hover {
    color: #3cba92;
    background-color: #f5f5f5;
}
</style>
