<template>
    <drawer-box
        :width="800"
        :closable="false"
        :visible="visible"
        :loading="loading"
        :has-btn="false"
        @close="handleClose"
    >
        <template slot="title">
            <a-input-group compact >
                <a-select v-model="searchParams.searchFrom" default-value="interface" :showArrow="false" style="width: 70px" @change="handleSearch">
                    <a-select-option :value="1">搜全部</a-select-option>
                    <a-select-option :value="2">搜目录</a-select-option>
                    <a-select-option :value="3">搜接口</a-select-option>
                </a-select>
                <a-input-search v-model="searchParams.likeStr" placeholder="请输入搜索关键字" style="width: calc(100% - 70px)" @change="handleSearch" @search="handleSearch" @pressEnter="handleSearch" :allowClear="true" :loading="loading"/>
            </a-input-group>
        </template>
        <div style="min-height: 200px">
            <div v-if="dataList.length > 0 || loading">
                <div class="search-content-item" v-for="item in dataList" :key="item.controllerId">
                    <div class="item-controller diy-cursor" @click="handleItem(item.controllerId)">
                        <a-tag color="green" class="diy-cursor">C</a-tag>
                        <span v-html="toRed(item.controllerComment, searchParams.likeStr, [1,2].includes(searchParams.searchFrom))"></span>
                    </div>
                    <div class="item-interface diy-cursor" v-for="inter in item.interfaceSearchVoList" :key="inter.interfaceId" @click="handleItem(item.controllerId, inter.interfaceId)">
                        <div>
                            <a-tag color="orange" class="diy-cursor" :key="inter.interfaceId + requestType" v-for="requestType in inter.requestTypeList">{{requestType}}</a-tag>
                            <span v-html="toRed(inter.interfaceComment, searchParams.likeStr, [1,3].includes(searchParams.searchFrom))"></span>
                        </div>
                        <div v-html="toRed(parseUri(inter.uriList), searchParams.likeStr, [1,3].includes(searchParams.searchFrom))"></div>
                    </div>
                </div>
            </div>
            <empty-box v-else text="没有搜索到数据哦"></empty-box>
        </div>
    </drawer-box>
</template>

<script>

import {listDocsSearch} from "@/api/docs";

export default {
    name: "index",
    data() {
        return {
            visible: false,
            loading: false,
            searchParams: {
                docsConfigId: '',
                createTime: '',
                searchFrom: 1,
                likeStr: ''
            },
            dataList: []
        }
    },
    methods: {
        init(docsConfigId, createTime, searchFrom, likeStr) {
            this.visible = true
            this.searchParams.docsConfigId = docsConfigId
            this.searchParams.createTime = createTime
            this.searchParams.searchFrom = searchFrom
            this.searchParams.likeStr = likeStr
            this.handleSearch()
        },
        handleSearch() {
            this.dataList = []
            if (!this.searchParams.docsConfigId || !this.searchParams.createTime || !this.searchParams.searchFrom || !this.searchParams.likeStr) {
                return
            }
            this.loading = true
            listDocsSearch(this.searchParams).then(res => {
                this.dataList = res.data || []
            }).finally(()=>{
                this.loading = false
            })
        },
        handleItem(controllerId, interfaceId = '') {
            const hash = interfaceId ? '#_' + interfaceId : ''
            const path = `/docs/${this.searchParams.docsConfigId}/${controllerId}`
            if (this.$route.path !== path) {
                this.$router.push({
                    path: path,
                    hash: hash
                })
            } else if (this.$route.hash !== hash) {
                this.$router.push({
                    hash: hash
                })
            }
            this.$emit('ok')
            this.handleClose()
        },
        handleClose() {
            this.visible = false
        },
        parseUri(uriList) {
            if (!uriList || uriList.length == 0) {
                return ''
            }
            return uriList.join('；')
        },
        // HTML转义，防止源码注释等内容注入
        escapeHtml(str) {
            if (str == null) {
                return ''
            }
            return String(str).replace(/[&<>"']/g, c => ({
                '&': '&amp;',
                '<': '&lt;',
                '>': '&gt;',
                '"': '&quot;',
                "'": '&#39;'
            }[c]))
        },
        toRed(source, repStr, isRep = false) {
            const s = this.escapeHtml(source)
            const r = this.escapeHtml(repStr)
            if (s && r && isRep) {
                return s.replace(r, `<span style="color: #f40">${r}</span>`)
            }
            return s
        }
    }
}
</script>

<style scoped lang="less">
.search-content-item {
    .item-controller {
        line-height: 30px;
        padding: 5px 5px 5px 24px;
    }
    .item-controller:hover {
        background-color: #ebfaf3;
    }
    .item-interface {
        padding: 8px 5px 8px 57px;
    }
    .item-interface:hover {
        background-color: #ebfaf3;
    }
}
/deep/ .ant-drawer-body {
    padding: 20px 0!important;
}
</style>
