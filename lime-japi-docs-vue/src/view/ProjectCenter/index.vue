<template>
    <loading :loading="firstOpen && loading">
        <div class="project-list-bg">
            <lime-logo-fixed/>
            <div class="project-header">
                <span>文档中心</span>
                <span class="header-btn"><a-button type="primary" icon="plus-square">新增</a-button></span>
            </div>
            <div class="project-list-box" :style="dataList && dataList.length > 0 ? '' : 'min-height: 200px'">
                <a-list
                    v-if="dataList && dataList.length > 0"
                    :loading="loading"
                    item-layout="horizontal"
                    :data-source="dataList"
                >
                    <a-list-item slot="renderItem" slot-scope="item, index" style="height: 100px">
                        <a slot="actions">文档</a>
                        <a slot="actions">设置</a>
                        <a slot="actions">删除</a>
                        <a-list-item-meta>
                            <span slot="description">
                                <span>{{item.createUser}}</span>
                                <span style="padding: 0 5px">/</span>
                                <span>{{item.createTime}}</span>
                            </span>
                            <span slot="title">
                                <span style="font-size: 18px">{{item.docName}}</span>
                                <a-tag style="margin-left: 10px" color="green">{{item.docVersion}}</a-tag>
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
        <div v-if="!show.pageShow" class="page-git">
            <a href="https://gitee.com/xuchenoak/lime-japi-docs">
                <a-icon class="git-icon" type="github" />
            </a>
        </div>
    </loading>
</template>

<script>

import {listCreateTime} from "@/api/docs"
export default {
    components: { },
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
            dataList: [{
                docName: '政务服务系统接口文档（收件）',
                docVersion: 'v1.0.1',
                createTime: '2023-10-21 10:06:01',
                createUser: '张三'
            },{
                docName: '政务服务系统接口文档（考评）',
                docVersion: 'v1.0.1',
                createTime: '2023-10-21 10:06:01',
                createUser: '张三'
            }]
        };
    },
    created() {
        this.init()
    },

    methods: {

        // 初始化
        init() {
        },

        // 获取文档生成时间列表
        getCreateTimeList() {
            this.loading = true
            listCreateTime().then(res => {
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

.page-git {
    position: absolute;
    right: 100px;
    top: 50px;
    .git-icon {
        font-size: 25px;
        color: #888;
        transition: all .5s;
    }
}
.page-git:hover .git-icon{
    color: #666;
}

</style>
