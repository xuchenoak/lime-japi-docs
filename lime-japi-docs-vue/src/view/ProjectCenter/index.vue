<template>
    <loading :loading="firstOpen && loading">
        <div class="project-header-bg">
            <a-row class="project-header">
                <a-col :span="12">
                    <a-space align="center" size="small">
                        <lime-logo :height="65" :open-click="true"/>
                        <div style="height: 80px; line-height: 80px;">
                            <span style="padding: 3px 15px; border-left: 1px solid #aaa; font-weight: 300">{{sysName}}</span>
                        </div>
                    </a-space>
                </a-col>
                <a-col :span="12" style="text-align: right">
                    <a-space align="center" size="middle" class="header-func-box">
                        <div v-if="!isLogin" class="user-box" @click="$refs['docs_config_login'].init()">
                            <a-icon class="user-icon" :type="isLogin ? 'user' : 'usergroup-add'" />
                        </div>
                        <a-dropdown v-else>
                            <div class="user-box">
                                <a-icon class="user-icon" :type="isLogin ? 'user' : 'usergroup-add'" />
                            </div>
                            <a-menu slot="overlay">
                                <a-menu-item>
                                    <a href="javascript:;" @click="handleDocsConfigLogout">退出登录</a>
                                </a-menu-item>
                            </a-menu>
                        </a-dropdown>
                        <div class="user-box" v-if="isLogin" @click="$refs['edit_system_config'].open()">
                            <a-icon class="user-icon" type="setting" />
                        </div>
                        <div class="header-btn" v-if="isLogin">
                            <a-button type="primary" icon="plus-square" @click="$refs['edit_docs_config'].open()">新增</a-button>
                        </div>
                        <div style="height: 80px"></div>
                    </a-space>
                </a-col>
            </a-row>
        </div>
        <div v-if="canView" class="project-list-bg">
            <div class="project-list-box" :style="dataList && dataList.length > 0 ? '' : 'min-height: 200px'">
                <a-list
                    v-if="dataList && dataList.length > 0"
                    :loading="loading"
                    item-layout="horizontal"
                    :data-source="dataList"
                >
                    <a-list-item slot="renderItem" slot-scope="item, index" style="height: 100px">
                        <a slot="actions" v-if="isLogin" @click="$refs['edit_docs_config'].open(item.id)">设置</a>
                        <a-popconfirm
                            slot="actions"
                            v-if="isLogin"
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
        <div v-else class="project-view-key-bg" >
            <div class="project-view-key-box">
                <a-input-search placeholder="请输入邀请码" enter-button="确定" @search="handleViewKey" v-model="viewKey" style="width: 400px" />
            </div>
        </div>
        <powered-box style="position:fixed; bottom: 0; background-color: #F3F5F7"/>
        <docs-config-login ref="docs_config_login"/>
        <edit-docs-config ref="edit_docs_config" @ok="listDocs" />
        <edit-system-config ref="edit_system_config"/>
    </loading>
</template>

<script>
import DocsConfigLogin from "./components/DocsConfigLogin.vue"
import EditDocsConfig from "./components/EditDocsConfig.vue"
import EditSystemConfig from "./components/EditSystemConfig.vue"
import {list, del, checkViewKey} from "@/api/docsConfig"
import {logout} from "@/api/storage/loginStorage"
export default {
    components: { DocsConfigLogin, EditDocsConfig,EditSystemConfig },
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
            isLogin: false,
            canView: false,
            sysName: '',
            viewKey: '',
            dataList: []
        };
    },
    created() {
        this.init()
    },
    watch: {
        '$store.getters.sysConfig.hasLogin'() {
            // 未登录且有邀请码，则需要验证邀请码是否有效
            if (!this.$store.getters.sysConfig.hasLogin && this.viewKey) {
                checkViewKey(this.viewKey).then(res => {
                    if (!res['data']) {
                        this.$router.push({path: '/'})
                    }
                }).finally(() => {
                    this.init()
                })
            } else {
                this.init()
            }
        },
        '$store.getters.sysConfig.sysName'() {
            this.sysName = this.$store.getters.sysConfig.sysName
        }
    },
    methods: {

        // 初始化
        async init() {
            sessionStorage.setItem("root", this.$route.fullPath || "/")
            this.isLogin = this.$store.getters.sysConfig.hasLogin
            this.sysName = this.$store.getters.sysConfig.sysName
            const viewKey = this.$route.params['viewKey'] || ''
            if (viewKey == null || viewKey == undefined || viewKey.trim().length < 1) {
                this.canView = this.isLogin || false
                this.viewKey = ''
            } else {
                this.canView = true
                this.viewKey = viewKey
            }
            if (this.canView) {
                this.listDocs()
            }
        },

        // 查看文档
        handleView(id) {
            const currentMenuItemKey = localStorage.getItem("currentMenuItemKey") || 666
            this.$router.push({
                path: `/docs/${id}/${currentMenuItemKey}`
            })
        },

        // 登出
        handleDocsConfigLogout() {
            this.$confirm({
                title: '确定退出登录？',
                onOk: () => {
                    logout().then(() => {
                        this.$message.success("登出成功")
                    })
                },
                onCancel() {},
            })
        },

        // 获取文档列表
        listDocs() {
            this.loading = true
            list({viewKey: this.viewKey}).then(res => {
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

        // 设置邀请码
        handleViewKey() {
            const viewKey = this.viewKey
            if (viewKey && viewKey.trim()) {
                checkViewKey(viewKey).then(res => {
                    if (!res['data']) {
                        this.$message.warning("邀请码无效")
                    } else {
                        // window.location.href = '/' + viewKey.trim()
                        this.$router.push({path: '/' + viewKey.trim()})
                        this.init()
                    }
                })
            }
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

.project-view-key-bg {
    height: 100vh;
    .project-view-key-box {
        position: absolute;
        top: 40%;
        left: 50%;
        transform: translateX(-50%);
    }
}

</style>
