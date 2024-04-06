<template>
    <loading :loading="firstOpen && loading">
        <div v-if="firstOpen && loading" style="height: 100vh; background-color: #fff"></div>
        <div v-else style="position: relative; height: 100vh">
            <div v-if="show.pageShow">
                <a-layout id="components-layout-demo-custom-trigger">
                    <a-layout-sider v-model="collapsed" width="300" collapsed-width="0">
                        <a-layout-sider class="container-left" v-model="collapsed" width="300" collapsed-width="0">
                            <a-layout-header class="menu-header">
                                <lime-logo :open-click="true"/>
                            </a-layout-header>
                            <a-layout-footer class="menu-comment">
                                <span>目录</span>
                            </a-layout-footer>
                            <a-layout-content class="menu-content" style="background: transparent">
                                <catalog ref="catalogRef" @handleMenuItem="handleMenuItem"/>
                            </a-layout-content>
                        </a-layout-sider>
                    </a-layout-sider>
                    <a-layout style="min-width: 1100px">
                        <a-layout-header class="content-header space-align-container">
                            <div class="docs-config-bg space-align-block">
                                <a-space align="center">
                                    <a-icon
                                        class="trigger"
                                        :type="collapsed ? 'menu-unfold' : 'menu-fold'"
                                        @click="() => (collapsed =! collapsed)"
                                    />
                                    <div class="config-doc-name">
                                        <span>{{docsConfig.docsName}}</span>
                                        <a-tag style="margin-left: 10px" color="green">{{docsConfig.docsVersion}}</a-tag>
                                    </div>
                                    <div class="config-doc-run-parse diy-cursor">
                                        <a-tooltip placement="bottom">
                                            <template slot="title">
                                                <span v-if="!parseRunning">生成文档</span>
                                                <span v-else>正在生成</span>
                                            </template>
                                            <a-icon :type="parseRunning ? 'loading' : 'plus-square'" style="font-size: 20px" @click="showDrawer"/>
                                        </a-tooltip>
                                    </div>
                                    <div class="config-doc-search" v-if="createTimeList && createTimeList.length > 0">
                                        <a-input-group compact >
                                            <a-select
                                                v-model="createTime"
                                                @change="showCatalog"
                                                style="width: 155px"
                                                :options="createTimeList"
                                                :showArrow="false"
                                            ></a-select>
                                            <a-select v-model="searchConfig.type" default-value="interface" :showArrow="false" style="width: 70px">
                                                <a-select-option value="controller">搜目录</a-select-option>
                                                <a-select-option value="interface">搜接口</a-select-option>
                                            </a-select>
                                            <a-input-search v-model="searchConfig.value" placeholder="请输入搜索关键字" style="width: 300px" @search="handleSearch" @pressEnter="handleSearch" :allowClear="true" :loading="searchConfig.loading"/>
                                        </a-input-group>
                                    </div>
                                </a-space>
                                <a-space class="docs-config-right" align="center" :style="{right: collapsed ? '40px' : '340px'}">
                                    <div class="home-box" @click="backToRoot">
                                        <a-icon class="home-icon" type="home" />
                                    </div>
                                </a-space>
                            </div>
                        </a-layout-header>
                        <a-layout-content style="margin-top: 64px; min-height: calc(100vh - 64px); position: relative">
                            <interface v-show="show.interfaceShow" ref="interface"/>
                            <div v-if="show.initInfoShow" style="position: absolute; top: 250px; left: 50%; transform: translateX(-50%);">
                                <lime-logo :width="360"/>
                                <div style="width: 100%; text-align: center; margin-top: 30px; font-size: 25px; font-weight: 200">{{slogan}}</div>
                            </div>
                            <powered-box style="position:fixed; z-index: 99999; bottom: 0; background-color: #F3F5F7" :style="{width: collapsed ? '100%' : 'calc(100% - 300px)'}"/>
                        </a-layout-content>
                    </a-layout>
                </a-layout>
            </div>
            <div v-else class="empty-interface-bg">
                <lime-logo class="lime-logo" :width="460"/>
                <div class="empty-box">
                    <empty-box :text="docsNotFound ? '文档不存在' : '未生成接口数据'"/>
                    <div class="empty-box-btn">
                        <a-button v-if="docsNotFound" @click="backToRoot" type="primary">
                            返回主页</a-button>
                        <a-button v-else :icon="parseRunning ? 'loading' : 'plus-square'" @click="showDrawer" type="primary">
                            {{ parseRunning ? '正在生成' : '去生成' }}</a-button>
                    </div>
                </div>
            </div>
            <powered-box v-if="!show.pageShow" style="position:fixed; z-index: 99999; bottom: 0; background-color: #F3F5F7"/>
            <div>
                <a-drawer
                    placement="right"
                    :closable="false"
                    :visible="drawerVisible"
                    @close="onClose"
                    width="500"
                >
                    <template slot="title">
                        <a-input-search :disabled="parseRunning" allowClear placeholder="请输入文档生成秘钥" v-model="password" @pressEnter="handleParse">
                            <a-button slot="enterButton" :disabled="parseRunning" @click.native="handleParse">开始</a-button>
                        </a-input-search>
                    </template>
                    <div style="overflow-y: auto; height: 100%">
                        <div v-if="msgList.length == 0 && !parseRunning" style="font-size: 13px">请输入文档生成秘钥后点击“开始”生成文档……</div>
                        <div v-else>
                            <p style="font-size: 13px; color: #666" :key="index" v-for="(msg, index) in msgList">{{msg}}</p>
                        </div>
                        <div v-if="parseRunning" style="margin-top: 10px; font-size: 20px">
                            <a-icon type="loading" />
                        </div>
                    </div>
                </a-drawer>
            </div>
        </div>
        <div v-if="!show.pageShow" class="empty-handle-box">
            <a-space align="center" size="small">
                <div class="home-box" @click="backToRoot">
                    <a-icon class="home-icon" type="home" />
                </div>
            </a-space>
        </div>
    </loading>
</template>

<script>

import Interface from "@/view/DocsPage/components/Interface"
import Catalog from "./components/Catalog"
import {listCreateTime, runDocsParse, getPareMsg} from "@/api/docs"
import {getDocsConfigSimple} from "@/api/docsConfig"
export default {
    components: { Interface, Catalog },
    name: "index",
    data() {
        return {
            firstOpen: true,
            collapsed: false,
            createTimeList: [],
            docsCatalogList: [],
            loading: false,
            show: {
                pageShow: false,
                initInfoShow: true,
                interfaceShow: false,
            },
            docsConfig: {
                docsName: 'XX项目接口文档',
                docsVersion: 'V1.0'
            },
            slogan: '',
            createTime: '',
            checkedInterface: {
                controllerId: '',
                controllerName: ''
            },
            searchConfig: {
                type: 'interface',
                value: '',
                loading: false
            },
            drawerVisible: false,
            password: '',
            msgList: [],
            parseRunning: false,
            parseTimestamp: null,
            docsConfigId: null,
            docsNotFound: false
        };
    },
    created() {
        this.init()
    },
    methods: {

        // 初始化
        init() {
            this.slogan = this.$store.getters.sysConfig.sysSlogan
            let docsConfigId = this.$route.params['id']
            if (!docsConfigId || Number(docsConfigId) < 1) {
                this.docsNotFound = true
                return
            }
            this.docsConfigId = docsConfigId
            this.getDocsConfig()
        },

        // 获取文档配置
        getDocsConfig() {
            this.loading = true
            getDocsConfigSimple(this.docsConfigId).then(res => {
                if (!res["data"]) {
                    this.docsNotFound = true
                } else {
                    this.docsConfig = res.data
                }
                this.getCreateTimeList()
                this.refreshParseMsg()
            }).catch(()=> {
                this.loading = false
            })
        },

        // 获取文档生成时间列表
        getCreateTimeList() {
            this.loading = true
            listCreateTime(this.docsConfigId).then(res => {
                if (res.code == 200) {
                    let createTimeList = []
                    res.data.map(item => {
                        createTimeList.push({
                            value: item,
                            label: item
                        })
                    })
                    if (createTimeList.length > 0) {
                        this.createTimeList = createTimeList
                        this.createTime = createTimeList[0].value
                        this.show.pageShow = true
                        this.showCatalog()
                    }
                }
            }).finally(()=> {
                this.loading = false
                this.firstOpen = false
            })
        },

        // 获取目录列表
        showCatalog() {
            this.$nextTick(()=> {
                this.initDocsCatalog()
            })
        },

        // 初始化接口目录
        initDocsCatalog() {
            const catalogRef = this.$refs['catalogRef']
            if (catalogRef == null || catalogRef == 'undefined') {
                setTimeout(()=> {
                    this.initDocsCatalog()
                }, 100)
            } else {
                this.show.interfaceShow = false
                this.show.initInfoShow = true
                const likeStr = this.searchConfig.type == 'controller' ? this.searchConfig.value : null
                this.$refs['catalogRef'].init(this.docsConfigId, this.createTime, likeStr)
            }
        },

        // 触发点击目录
        handleMenuItem({key, item}) {
            this.checkedInterface.controllerId = key
            this.checkedInterface.controllerName = item.value.name
            let likeStr = null
            if (this.searchConfig.type == 'interface') {
                likeStr = this.searchConfig.value
            }
            if (key) {
                this.show.interfaceShow = true
                this.$refs['interface'].init(this.createTime, key, item.value.name, likeStr)
            }
            this.show.initInfoShow = false
        },

        // 触发搜索
        handleSearch() {
            this.searchConfig.loading = true
            switch (this.searchConfig.type) {
                case 'interface':
                    if (this.checkedInterface.controllerId) {
                        this.show.interfaceShow = true
                        this.$refs['interface'].init(this.createTime, this.checkedInterface.controllerId, this.checkedInterface.controllerName, this.searchConfig.value)
                    }
                    break
                case 'controller':
                    this.showCatalog()
                    break
            }
            this.searchConfig.loading = false
        },

        showDrawer() {
            this.drawerVisible = true;
        },
        onClose() {
            this.drawerVisible = false;
        },

        // 执行解析
        handleParse() {
            this.msgList = []
            runDocsParse(this.docsConfigId, this.password).then(res => {
                if (res['code'] === 200) {
                    const msg = res.data['msg']
                    const parseRunning = res.data['running']
                    const parseTimestamp = res.data['parseTimestamp']
                    this.parseRunning = parseRunning
                    if (parseRunning) {
                        this.parseTimestamp = parseTimestamp
                        this.refreshParseMsg()
                    } else if (msg) {
                        this.msgList.push(msg)
                    }
                }
            })
        },

        // 刷新解析消息
        refreshParseMsg() {
            getPareMsg(this.docsConfigId, this.parseTimestamp).then(res => {
                if (res['code'] === 200) {
                    const msgList = res.data['msgList']
                    const parseTimestamp = res.data['parseTimestamp']
                    const parseRunning = res.data['running']
                    this.parseRunning = parseRunning
                    const l = this.msgList.length
                    if ((parseRunning && parseTimestamp) || (msgList && l > 0 && msgList.length != l)) {
                        this.parseTimestamp = parseTimestamp
                        setTimeout(() => {
                            this.refreshParseMsg()
                        }, 3000)
                        if (!this.parseRunning) {
                            this.getCreateTimeList()
                            this.onClose()
                        }
                    }
                    if (msgList && msgList.length > 0) {
                        this.msgList = msgList
                    }
                }
            })
        },

        // 返回主页
        backToRoot() {
            this.$router.push({path: sessionStorage.getItem("root") || "/"})
        }

    },



}
</script>

<style scoped lang="less">
.empty-handle-box {
    position: absolute;
    top: 50px;
    right: 100px;
    >div {
        float: left;
    }
    .home-box {
        border: 1.5px solid #999;
        box-sizing: border-box;
        width: 30px;
        height: 30px;
        border-radius: 50%;
        line-height: 30px;
        text-align: center;
        cursor: pointer;
        transition: all .5s;
        .home-icon {
            font-size: 18px;
            color: #999;
            transition: all .5s;
        }
    }
    .home-box:hover {
        border-color: #666;
        .home-icon {
            color: #666;
        }
    }
}
.empty-interface-bg {
    height: 300px;
    position: absolute;
    width: 100%;
    top: 50%;
    transform: translateY(-50%);
    .lime-logo {
        position: absolute;
        left: 50%;
        transform: translateX(-50%);
    }
    .empty-box {
        position: absolute;
        height: 134px;
        width: 100%;
        bottom: 0;
        border-top: 1px dashed #ccc;
        .empty-box-btn {
            position: absolute;
            width: 100%;
            bottom: -30px;
            text-align: center;
        }
    }
}

.codeEditBox {
    margin-top: 0px;
}

.container-left {
    position: fixed;
    height: 100vh;
    background: #fff;
    border-right: 1px solid #e8e8e8;
}
.menu-header {
    height: 100px;
    background-color: #fff;
    padding: 10px;
    box-sizing: border-box;
}
.menu-comment {
    padding: 5px;
    margin: 10px 23px;
    background: #fff;
    border-bottom: 1px solid #e8e8e8;
}
.menu-content {
    height: calc(100vh - 170px);
    overflow-y: auto;
    overflow-x: hidden;
}

.content-header {
    background: #fff;
    padding: 0;
    position: fixed;
    width: 100%;
    height: 64px;
    min-width: 1200px;
    z-index: 999;
    box-shadow: rgba(0,0,0,.1) 10px 5px 20px;
    .docs-config-bg {
        height: 64px;
    }
    .config-doc-name {
        font-size: 18px;
        line-height: 0px;
    }
    .config-doc-run-parse {
        margin-left: 5px;
        line-height: 64px;
        transition: all .2s;
    }
    .config-doc-run-parse:hover {
        color: #3cba92;
    }
    .config-doc-search {
        margin-left: 20px;
    }
    .docs-config-right {
        position: absolute;
        right: 340px;
        height: 64px;
        .home-box {
            border: 1.5px solid #999;
            box-sizing: border-box;
            width: 25px;
            height: 25px;
            border-radius: 50%;
            line-height: 25px;
            text-align: center;
            cursor: pointer;
            transition: all .5s;
            .home-icon {
                font-size: 15px;
                color: #999;
                transition: all .5s;
            }
        }
        .home-box:hover {
            border-color: #666;
            .home-icon {
                color: #666;
            }
        }
    }
}

/deep/ .ant-anchor-wrapper {
    background: transparent;
}

#components-layout-demo-custom-trigger .trigger {
    font-size: 18px;
    line-height: 64px;
    padding: 0 24px;
    cursor: pointer;
    transition: color 0.3s;
    float: left;
}

#components-layout-demo-custom-trigger .trigger:hover {
    color: #3cba92;
}

.ant-layout {
    background: #F3F5F7;
}

</style>
