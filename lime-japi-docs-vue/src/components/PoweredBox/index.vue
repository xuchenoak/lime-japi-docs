<template>
    <div class="powered-box">
        <a-space align="center" size="small">
            <span style="color: #666">Powered by </span>
            <a href="https://gitee.com/xuchenoak/lime-japi-docs" target="_blank">
                lime-japi-docs
            </a>
            <span style="color: #aaa">|</span>
            <div style="margin-top: 2px">
                <gitee-box :size="16"/>
            </div>
        </a-space>
    </div>
</template>

<script>
export default {
    name: "index",
    props: {
        size: {
            type: Number,
            default(){return 30}
        }
    }
}
</script>

<style scoped lang="less">
.powered-box {
    height: 30px;
    line-height: 30px;
    text-align: center;
    width: 100%;
    font-size: 14px;
}
</style>