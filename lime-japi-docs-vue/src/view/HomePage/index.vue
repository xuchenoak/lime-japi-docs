<template>
<div class="page-container">
    <div class="page-header-bg">
        <a-row class="page-header">
            <a-col :span="12">
                <lime-logo :height="65" :open-click="true"/>
            </a-col>
            <a-col :span="12" style="text-align: right">
                <a-space align="center" size="middle" class="header-func-box">

                </a-space>
            </a-col>
        </a-row>
    </div>
</div>
</template>

<script>

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
    font-size: 20px;
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
        }
    }
}
</style>