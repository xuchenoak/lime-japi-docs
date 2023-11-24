<template>
    <div id="app">
        <loading :loading="loading">
            <router-view/>
        </loading>
    </div>
</template>

<script>

import {mapState} from "vuex";

export default {
    name: 'App',
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
