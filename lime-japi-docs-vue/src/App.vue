<template>
    <div id="app">
        <a-config-provider :locale="zh_CN">
            <loading :loading="loading">
                <router-view/>
            </loading>
        </a-config-provider>
    </div>
</template>

<script>

import {mapState} from "vuex";
import zh_CN from 'ant-design-vue/lib/locale-provider/zh_CN';
export default {
    name: 'App',
    data() {
        return {
            zh_CN,
        }
    },
    computed: {
        ...mapState(["loading"]),
    }
}
</script>

<style scoped lang="less">
#app {
    height: 100%;
    position: relative;
    z-index: 0;
    background-color: #F3F5F7;
}
</style>
