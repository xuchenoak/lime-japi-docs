<template>
    <div class="page-container">
        <div class="page-header-bg">
            <a-row class="page-header">
                <a-col :span="12">
                    <lime-logo :height="60" :open-click="true" style="margin-top: 10px"/>
                </a-col>
                <a-col :span="12" style="text-align: right">
                    <a-space align="center" :size="45" class="header-func-box">
                        <a-dropdown>
                            <a class="func-box-text">
                                <span style="padding-right: 5px">v2.0.1</span>
                                <a-icon type="down" />
                            </a>
                            <a-menu slot="overlay">
                                <a-menu-item>
                                    <a href="#v_2.0.1">v2.0.1</a>
                                </a-menu-item>
                                <a-menu-item>
                                    <a href="#v_1.0.1">v1.0.1</a>
                                </a-menu-item>
                            </a-menu>
                        </a-dropdown>
                        <a-dropdown>
                            <span class="func-box-text">下载</span>
                            <a-menu slot="overlay">
                                <a-menu-item>
                                    <a href="https://gitee.com/xuchenoak/lime-japi-docs/releases/download/v2.0.1/lime-japi-docs-runner-2.0.1.zip">lime-japi-docs-runner-2.0.1.zip</a>
                                </a-menu-item>
                                <a-menu-item>
                                    <a href="https://gitee.com/xuchenoak/lime-japi-docs/releases/download/v1.0.1/lime-japi-docs-runner-1.0.1.zip">lime-japi-docs-runner-1.0.1.zip</a>
                                </a-menu-item>
                            </a-menu>
                        </a-dropdown>
                        <a-dropdown>
                            <span class="func-box-text">文档</span>
                            <a-menu slot="overlay">
                                <a-menu-item>
                                    <a href="#v_2.0.1">v2.0.1 文档</a>
                                </a-menu-item>
                                <a-menu-item>
                                    <a href="#v_1.0.1">v1.0.1 文档</a>
                                </a-menu-item>
                            </a-menu>
                        </a-dropdown>
                        <div>
                            <a class="func-box-text" href="">关于</a>
                        </div>
                        <GiteeBox :size="20" />
                        <div style="height: 80px"></div>
                    </a-space>
                </a-col>
            </a-row>
        </div>
        <div class="page-content-bg">
            <page-front/>
        </div>
    </div>
</template>

<script>
import PageFront from "./components/PageFront.vue";

export default {
    components: {PageFront},
    name: "index",
    data() {
        return {}
    }
}
</script>

<style scoped lang="less">
.page-container {
    height: 100vh;
    background: #fff;
}
.page-header-bg {
    width: 100%;
    z-index: 1;
    background: #fff;
    height: 80px;
    padding: 0 25px;
    font-size: 15px;
    position: fixed;
    border-bottom: 1px solid #eee;
    box-shadow: 2px 2px 25px rgba(0,0,0,.05);
    .page-header {
        margin: 0 auto;
        min-width: 800px;
        max-width: 1200px;
        .header-func-box {
            margin-right: 5px;
            >div:nth-last-child(2) {
                margin-right: 0!important;
            }
            .func-box-text {
                color: #666;
                cursor: pointer;
                transition: all .5s;
            }
            .func-box-text:hover {
                color: #28b071;
            }
        }
    }
}
.page-content-bg {
    width: 100%;
    padding: 80px 25px 0 25px;
}
</style>