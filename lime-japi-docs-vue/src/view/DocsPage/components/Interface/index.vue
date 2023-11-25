<template>
    <loading :loading="loading">
        <div ref="interfaceContainer" class="interface-container">
            <a-row >
                <a-col :span="interfaceShow ? 18 : 24">
                    <div class="interface-bg">
                        <copy-text :value="controllerName" placement="right">
                            <h1>
                                <span class="diy-cursor">{{controllerName}}</span>
                            </h1>
                        </copy-text>
                        <div class="interface-content-bg">
                            <div v-if="interfaceShow">
                                <a-descriptions class="interface-content" layout="vertical" bordered size="small"
                                                :key="inter.interfaceId + '_' + index"
                                                v-for="(inter, index) in interfaceList">
                                    <template #title>
                                        <h2>
                                            <copy-text :value="inter.interfaceName" placement="right">
                                            <span style="padding: 0px 5px">
                                              {{getInterfaceIndex(index)}}
                                            </span>
                                                <span class="diy-cursor" :id="inter.interfaceId">{{inter.interfaceName}}</span>
                                            </copy-text>
                                        </h2>
                                    </template>
                                    <a-descriptions-item :span="3">
                                        <template #label>
                                            <span style="padding-right: 20px">请求地址</span>
                                            <copy-text :value="reqType" placement="right" :key="inter.interfaceId + '_' + reqType" v-for="reqType in inter.requestTypeList">
                                                <a-tag color="orange" class="diy-cursor">
                                                    {{ reqType }}
                                                </a-tag>
                                            </copy-text>
                                        </template>
                                        <copy-text :value="uri" placement="right" :key="inter.interfaceId + '_' + uri" v-for="uri in inter.uriList">
                                            <span class="request-uri diy-cursor">{{uri}}</span>
                                        </copy-text>
                                    </a-descriptions-item>
                                    <a-descriptions-item :span="3" v-if="interfaceFormDataView(inter.formData) || inter.bodyData">
                                        <template #label>
                                            <span style="padding-right: 20px">请求参数</span>
                                            <copy-text :value="inter.requestContentType" placement="right">
                                                <a-tag color="orange" class="diy-cursor">
                                                    {{inter.requestContentType}}
                                                </a-tag>
                                            </copy-text>
                                        </template>
                                        <a-tabs class="content-tab" v-if="interfaceFormDataView(inter.formData)"
                                                default-active-key="1" size="small">
                                            <a-tab-pane key="1" tab="字段参数">
                                                <a-table
                                                    :pagination="false"
                                                    :columns="columns"
                                                    rowKey="name"
                                                    :data-source="inter.formData"
                                                    size="small"
                                                    :bordered="true"/>
                                            </a-tab-pane>
                                            <a-tab-pane key="2" tab="JSON示例">
                                                <div class="codeEditBox">
                                                    <code-viewer
                                                        :value="inter.formDataJson"
                                                        :options="{readOnly: true}"
                                                        language="json"
                                                    ></code-viewer>
                                                </div>
                                            </a-tab-pane>
                                            <a-tab-pane key="3" tab="JS Object">
                                                <div class="codeEditBox">
                                                    <code-viewer
                                                        :value="inter.formDataJsObj"
                                                        :options="{readOnly: true}"
                                                        language="javascript"
                                                    ></code-viewer>
                                                </div>
                                            </a-tab-pane>
                                        </a-tabs>
                                        <a-tabs class="content-tab" v-if="inter.bodyData" default-active-key="1" size="small">
                                            <a-tab-pane key="1" tab="JSON参数">
                                                <div class="codeEditBox">
                                                    <code-viewer
                                                        :value="inter.bodyData"
                                                        :options="{readOnly: true}"
                                                        language="json"
                                                    ></code-viewer>
                                                </div>
                                            </a-tab-pane>
                                            <a-tab-pane key="2" tab="JS Object">
                                                <div class="codeEditBox">
                                                    <code-viewer
                                                        :value="inter.bodyDataJsObj"
                                                        :options="{readOnly: true}"
                                                        language="javascript"
                                                    ></code-viewer>
                                                </div>
                                            </a-tab-pane>
                                        </a-tabs>
                                    </a-descriptions-item>
                                    <a-descriptions-item :span="3" v-if="inter.resData">
                                        <template #label>
                                            <span style="padding-right: 20px">响应示例</span>
                                            <copy-text value="application/json" placement="right">
                                                <a-tag color="orange" class="diy-cursor">
                                                    application/json
                                                </a-tag>
                                            </copy-text>
                                        </template>
                                        <a-tabs class="content-tab" default-active-key="1" size="small">
                                            <a-tab-pane v-if="inter.resData" key="1" tab="响应示例">
                                                <div class="codeEditBox">
                                                    <code-viewer
                                                        :value="inter.resData"
                                                        :options="{readOnly: true}"
                                                        language="json"
                                                    ></code-viewer>
                                                </div>
                                            </a-tab-pane>
                                            <a-tab-pane v-if="inter.resDataJsObj" key="2" tab="JS Object">
                                                <div class="codeEditBox">
                                                    <code-viewer
                                                        :value="inter.resDataJsObj"
                                                        :options="{readOnly: true}"
                                                        language="javascript"
                                                    ></code-viewer>
                                                </div>
                                            </a-tab-pane>
                                        </a-tabs>
                                    </a-descriptions-item>
                                </a-descriptions>
                            </div>
                            <div v-else>
                                <empty-box text="暂无接口数据"/>
                            </div>
                        </div>
                    </div>
                </a-col>
                <a-col :span="6" v-if="interfaceShow">
                    <div class="catalog-bg">
                        <div class="checkbox-bg" v-show="checkBoxShow">
                            <a-space direction="vertical">
                                <a-checkbox v-model="interfaceParams.hasComment" @change="checkEnabled">
                                    显示注释
                                </a-checkbox>
                                <a-checkbox v-model="interfaceParams.hasType" :disabled="disabledStatus.hasType"
                                            @change="getInterfaceList">
                                    显示类型
                                </a-checkbox>
                                <a-checkbox v-model="interfaceParams.hasValid" :disabled="disabledStatus.hasValid"
                                            @change="getInterfaceList">
                                    显示验证
                                </a-checkbox>
                                <a-checkbox v-model="interfaceParams.addDefaultValue" @change="getInterfaceList">
                                    显示默认值
                                </a-checkbox>
                            </a-space>
                        </div>
                        <a-anchor :offsetTop="200" style="min-width: 1px" :getContainer="getContainer">
                            <a-anchor-link :href="'#' + item.interfaceId"
                                           :key="getInterfaceIndex(index)"
                                           :title="getInterfaceIndex(index) + ' ' + item.interfaceName"
                                           v-for="(item, index) in interfaceList"/>
                        </a-anchor>
                    </div>
                </a-col>
            </a-row>
            <div>
                <a-back-top :target="getContainer" />
            </div>
        </div>
    </loading>
</template>

<script>

import {listInterface} from "@/api/docs"

export default {
    name: "index",
    data() {
        return {
            controllerName: '',
            loading: false,
            interfaceList: [],
            interfaceShow: false,
            checkBoxShow: false,
            interfaceParams: {
                createTime: '',
                controllerId: '',
                // 是否有注释
                hasComment: true,
                // 是否有类型
                hasType: true,
                // 是否有验证
                hasValid: true,
                // 是否有默认值
                addDefaultValue: true,
                // 搜索关键字
                likeStr: ''
            },
            disabledStatus: {
                hasValid: false,
                hasType: false
            },
            columns: [
                {
                    title: '名称',
                    dataIndex: 'name',
                    width: 150
                },
                {
                    title: '类型',
                    dataIndex: 'type',
                    width: 100
                },
                {
                    title: '验证',
                    dataIndex: 'validation',
                    width: 100
                },
                {
                    title: '描述',
                    dataIndex: 'comment',
                },
            ],
        }
    },
    methods: {

        // 初始化
        init(createTime, controllerId, controllerName, likeStr) {
            this.controllerName = controllerName ? controllerName : '未知接口分组'
            if (!createTime || !controllerId) {
                this.interfaceShow = false
                return
            }
            this.interfaceParams.createTime = createTime
            this.interfaceParams.controllerId = controllerId
            this.interfaceParams.likeStr = likeStr
            this.getInterfaceList()
        },

        // 获取接口数据
        getInterfaceList() {
            this.checkBoxShow = false
            this.interfaceList = []
            this.loading = true
            listInterface(this.interfaceParams).then(res => {
                if (res.code == 200 && res.data && res.data.length > 0) {
                    this.interfaceList = res.data
                    this.interfaceShow = true
                    this.checkBoxShow = true
                    this.$nextTick(()=> {
                        this.getContainer().scrollTop = 0
                    })
                    return
                }
                this.interfaceShow = false
            }).finally(()=> {
                this.loading = false
            })
        },

        // 表单参数是否可见
        interfaceFormDataView(formData) {
            return formData && formData.length > 0
        },

        // 获取接口序号
        getInterfaceIndex(index) {
            const i = index + 1
            return i > 9 ? i : (this.interfaceList.length > 99 ? '00' : '0') + i
        },

        // 触发显示注释
        checkEnabled() {
            const check = this.interfaceParams.hasComment
            this.interfaceParams.hasType = check
            this.interfaceParams.hasValid = check
            this.disabledStatus.hasType = !check
            this.disabledStatus.hasValid = !check
            this.getInterfaceList()
            return check
        },

        // 获取滚动容器
        getContainer() {
            return this.$refs['interfaceContainer']
        }
    }
}
</script>

<style scoped lang="less">
.interface-container {
    overflow-y: auto;
    height: calc(100vh - 64px);
}
.interface-bg {
    width: 800px;
    height: auto;
    background: #fff;
    margin: 30px auto 50px auto;
    box-shadow: rgba(0, 0, 0, 0.06) 0px 0px 10px 0px, rgba(0, 0, 0, 0.04) 0px 0px 0px 1px;
    padding: 50px;
    .interface-content-bg {
        margin-top: 40px;
        min-height: calc(100vh - 310px);
        position: relative;
    }
    .interface-content {
        margin-bottom: 40px;
    }
    .request-uri {
        font-size: 1.1em;
    }
    .content-tab {
        margin-bottom: 10px;
    }
}
.catalog-bg .checkbox-bg {
    position: fixed;
    margin-top: 50px;
    min-width: 200px;
}

h1 {
    font-family: "Oxygen", "Helvetica Neue", Helvetica, Arial, sans-serif;
    font-weight: 500;
    line-height: 1.1;
    color: #1d365d;
    font-size: 28px;
    margin: 0;
}

</style>
