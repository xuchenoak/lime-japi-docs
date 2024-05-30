<template>
    <loading :loading="loading">
        <div style="min-height: calc(100vh - 250px)">
            <a-menu
                v-if="docsCatalogList && docsCatalogList.length > 0"
                class="menu-container"
                mode="inline"
                :defaultSelectedKeys="defaultSelectedKeys"
                @click="({key, item}) => handleMenuItem(key, item.value.name)"
            >
                <a-menu-item :key="item.id" :value="item" v-for="item in docsCatalogList">
                    {{item.name}}
                </a-menu-item>
            </a-menu>
            <empty-box v-else-if="!loading" text="暂无目录数据"/>
        </div>
    </loading>
</template>

<script>

import {listCatalog} from "@/api/docs";

export default {
    name: "index",
    data() {
        return {
            docsCatalogList: [],
            loading: false,
            cataLogParams: {
                // 生成时间
                createTime: '',
                // 搜索关键字
                likeStr: ''
            },
            defaultSelectedKeys: []
        }
    },
    methods: {

        // 初始化
        init(docsConfigId, createTime, likeStr) {
            if (!createTime) {
                this.docsCatalogList = []
                return
            }
            this.cataLogParams.docsConfigId = docsConfigId
            this.cataLogParams.createTime = createTime
            this.cataLogParams.likeStr = likeStr
            this.getCatalogList()
        },

        // 获取目录列表
        getCatalogList() {
            this.docsCatalogList = []
            this.loading = true
            const currentMenuItemKey = localStorage.getItem("currentMenuItemKey")
            listCatalog(this.cataLogParams).then(res => {
                if (res.code === 200) {
                    this.docsCatalogList = res.data
                    if (currentMenuItemKey) {
                        for (let item of this.docsCatalogList) {
                            if (item.id === currentMenuItemKey) {
                                this.defaultSelectedKeys = [currentMenuItemKey]
                                this.handleMenuItem(currentMenuItemKey, item.name)
                                break;
                            }
                        }
                    }
                }
            }).finally(()=> {
                this.loading = false
            });
        },

        // 触发点击目录
        handleMenuItem(key, name) {
            localStorage.setItem("currentMenuItemKey", key)
            this.$emit("handleMenuItem", key, name)
        },
    }
}
</script>

<style scoped lang="less">
    .menu-container {
        border: 0px;
    }

</style>
