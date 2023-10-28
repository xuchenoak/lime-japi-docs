<template>
    <loading :loading="firstOpen && loading">
        <div class="project-list-bg">
            <lime-logo-fixed/>
            <div class="project-header">
                <span>文档中心</span>
                <span class="header-btn" v-if="isAdmin">
                    <a-button type="primary" icon="plus-square" @click="$refs['edit_docs_config'].open()">新增</a-button>
                </span>
            </div>
            <div class="project-list-box" :style="dataList && dataList.length > 0 ? '' : 'min-height: 200px'">
                <a-list
                    v-if="dataList && dataList.length > 0"
                    :loading="loading"
                    item-layout="horizontal"
                    :data-source="dataList"
                >
                    <a-list-item slot="renderItem" slot-scope="item, index" style="height: 100px">
                        <a slot="actions" v-if="isAdmin">设置</a>
                        <a slot="actions" v-if="isAdmin">删除</a>
                        <a-list-item-meta>
                            <span slot="description">
<!--                                <span>{{item.createUser}}</span>-->
<!--                                <span style="padding: 0 5px">/</span>-->
                                <span>{{item.createTime}}</span>
                            </span>
                            <span slot="title">
                                <span style="font-size: 18px; cursor: pointer">{{item.docsName}}</span>
                                <a-tag style="margin-left: 10px; cursor: pointer" color="green">{{item.docsVersion}}</a-tag>
                            </span>
                            <div slot="avatar" style="font-size: 30px; color: #666; font-weight: 300; font-style: italic">
                                {{getIndex(index)}}
                            </div>
                        </a-list-item-meta>
                    </a-list-item>
                </a-list>
                <empty-box v-else />
            </div>
        </div>
        <div class="user-box" @click="handleDocsConfigKey">
            <a-icon class="user-icon" :type="isAdmin ? 'user' : 'usergroup-add'" />
        </div>
        <gitee-box v-if="!show.pageShow" style="position: absolute; top: 50px; right: 100px;"/>
        <docs-config-key ref="docs_config_key" @save="handleSaveDocsConfigKey"/>
        <edit-docs-config ref="edit_docs_config" />
    </loading>
</template>

<script>
import DocsConfigKey from "./components/DocsConfigKey.vue"
import EditDocsConfig from "./components/EditDocsConfig.vue"
import {checkConfigKey, list} from "@/api/docsConfig"
export default {
    components: { DocsConfigKey, EditDocsConfig },
    name: "index",
    data() {
        return {
            firstOpen: true,
            loading: false,
            show: {
                pageShow: false,
                initInfoShow: true,
                interfaceShow: false,
            },
            isAdmin: false,
            dataList: []
        };
    },
    created() {
        this.init()
    },

    methods: {

        // 初始化
        init() {
            this.listDocs()
            this.checkDocsConfigKey()
        },

        // 验证文档管理秘钥
        checkDocsConfigKey() {
            const docsConfigKey = localStorage.getItem("docs_config_key")
            this.loading = true
            checkConfigKey(docsConfigKey).then(res => {
                this.isAdmin = res['data'] && res['data']['checkResult']
            }).finally(()=> {
                this.loading = false
            })
        },

        // 验证管理员秘钥
        handleDocsConfigKey() {
            this.$refs['docs_config_key'].init()
        },

        // 文档管理秘钥验证成功
        handleSaveDocsConfigKey(docsConfigKey) {
            localStorage.setItem("docs_config_key", docsConfigKey)
            this.checkDocsConfigKey()
        },

        // 获取文档列表
        listDocs() {
            this.loading = true
            list().then(res => {
                if (res['data']) {
                    this.dataList = res['data']
                    this.dataList = [{
                        docsName: '政务服务系统接口文档（收件）',
                        docsVersion: 'v1.0.1',
                        createTime: '2023-10-21 10:06:01'
                    },{
                        docsName: '政务服务系统接口文档（考评）',
                        docsVersion: 'v1.0.1',
                        createTime: '2023-10-21 10:06:01'
                    }]
                }
            }).finally(()=> {
                this.loading = false
                this.firstOpen = false
            })
        },

        // 获取序号
        getIndex(index) {
            const i = index + 1
            return i > 9 ? i : (this.dataList.length > 99 ? '00' : '0') + i
        }

    },

}
</script>

<style scoped lang="less">
.project-list-bg {
    width: 100%;
    padding: 100px 0;
    .project-header {
        width: 800px;
        margin: 0 auto 15px auto;
        background: #fff;
        height: 60px;
        line-height: 60px;
        padding: 0 25px;
        font-size: 20px;
        position: relative;
        box-shadow: 2px 2px 25px rgba(0,0,0,.05);
        .header-btn {
            position: absolute;
            right: 25px;
            top: -3px;
        }
    }
    .project-list-box {
        position: relative;
        width: 800px;
        margin: 0 auto;
        background: #fff;
        padding: 0 25px;
        box-shadow: 2px 2px 25px rgba(0,0,0,.05);
    }
}
.user-box {
    position: absolute;
    top: 50px;
    right: 140px;
    border: 1.5px solid #999;
    box-sizing: border-box;
    width: 30px;
    height: 30px;
    border-radius: 50%;
    line-height: 30px;
    text-align: center;
    cursor: pointer;
    transition: all .5s;
    .user-icon {
        font-size: 18px;
        color: #999;
        transition: all .5s;
    }
}
.user-box:hover {
    border-color: #666;
    .user-icon {
        color: #666;
    }
}

</style>
