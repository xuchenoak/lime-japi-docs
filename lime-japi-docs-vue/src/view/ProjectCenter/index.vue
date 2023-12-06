<template>
    <loading :loading="firstOpen && loading">
        <div class="project-header-bg">
            <a-row class="project-header">
                <a-col :span="12">
                    <a-space align="center" size="small">
                        <lime-logo :height="65" :open-click="true"/>
                        <div style="height: 80px; line-height: 80px;">
                            <span style="padding: 3px 15px; border-left: 1px solid #aaa; font-weight: 300">接口文档中心</span>
                        </div>
                    </a-space>
                </a-col>
                <a-col :span="12" style="text-align: right">
                    <a-space align="center" size="middle" class="header-func-box">
                        <div class="user-box" @click="handleDocsConfigKey">
                            <a-icon class="user-icon" :type="isAdmin ? 'user' : 'usergroup-add'" />
                        </div>
                        <div class="user-box" v-if="isAdmin" @click="handleDocsConfigKey">
                            <a-icon class="user-icon" type="setting" />
                        </div>
                        <div class="header-btn" v-if="isAdmin">
                            <a-button type="primary" icon="plus-square" @click="$refs['edit_docs_config'].open()">新增</a-button>
                        </div>
                        <div style="height: 80px"></div>
                    </a-space>
                </a-col>
            </a-row>
        </div>
        <div class="project-list-bg">
            <div class="project-list-box" :style="dataList && dataList.length > 0 ? '' : 'min-height: 200px'">
                <a-list
                    v-if="dataList && dataList.length > 0"
                    :loading="loading"
                    item-layout="horizontal"
                    :data-source="dataList"
                >
                    <a-list-item slot="renderItem" slot-scope="item, index" style="height: 100px">
                        <a slot="actions" v-if="isAdmin" @click="$refs['edit_docs_config'].open(item.id)">设置</a>
                        <a-popconfirm
                            slot="actions"
                            v-if="isAdmin"
                            title="确认删除？"
                            ok-text="确认"
                            cancel-text="取消"
                            @confirm="handleDel(item.id)"
                        >
                            <a>删除</a>
                        </a-popconfirm>
                        <a-list-item-meta>
                            <span slot="description">
                                <span>{{item.createTime}}</span>
                            </span>
                            <span slot="title" @click="handleView(item.id)">
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
        <powered-box style="position:fixed; bottom: 0; background-color: #F3F5F7"/>
        <docs-config-key ref="docs_config_key" @save="handleSaveDocsConfigKey"/>
        <edit-docs-config ref="edit_docs_config" @ok="listDocs" />
    </loading>
</template>

<script>
import DocsConfigKey from "./components/DocsConfigKey.vue"
import EditDocsConfig from "./components/EditDocsConfig.vue"
import {checkConfigKey, list, del} from "@/api/docsConfig"
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
            queryParams: {
                viewKey: "",
                views: ""
            },
            dataList: []
        };
    },
    created() {
        this.init()
    },

    methods: {

        // 初始化
        init() {
            sessionStorage.setItem("root", this.$route.fullPath || "/")
            this.queryParams.viewKey = this.$route.query["viewKey"] || ""
            this.queryParams.views = this.$route.query["views"] || ""
            this.checkDocsConfigKey()
        },

        // 验证文档管理秘钥
        checkDocsConfigKey() {
            const docsConfigKey = localStorage.getItem("docs_config_key")
            this.loading = true
            checkConfigKey(docsConfigKey).then(res => {
                this.isAdmin = res['data'] && res['data']['checkResult']
                this.listDocs()
            }).catch(()=> {
                this.loading = false
            })
        },

        // 查看文档
        handleView(id) {
            this.$router.push({
                path: '/docs/' + id
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
            list(this.queryParams).then(res => {
                if (res['data']) {
                    this.dataList = res['data']
                }
            }).finally(()=> {
                this.loading = false
                this.firstOpen = false
            })
        },

        // 删除
        handleDel(id) {
            this.loading = true
            del(id).then(() => {
                this.$message.success("删除成功")
                this.listDocs()
            }).finally(()=> {
                this.loading = false
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
        .header-func-box {
            margin-right: 5px;
            >div:nth-last-child(2) {
                margin-right: 0!important;
            }
            .user-box {
                border: 1.5px solid #999;
                box-sizing: border-box;
                width: 30px;
                height: 30px;
                border-radius: 50%;
                line-height: 27px;
                text-align: center;
                cursor: pointer;
                transition: border .5s;
                .user-icon {
                    font-size: 18px;
                    color: #999;
                    transition: color .5s;
                }
            }
            .user-box:hover {
                border-color: #666;
                .user-icon {
                    color: #666;
                }
            }
            .header-btn {
                line-height: 72px;
                height: 80px;
            }
        }
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


</style>
