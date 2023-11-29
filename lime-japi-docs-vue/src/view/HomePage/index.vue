<template>
<div class="page-container">
    <div class="page-header-bg">
        <a-row class="page-header">
            <a-col :span="12">
                <lime-logo :height="70" :open-click="true" style="margin-top: 5px"/>
            </a-col>
            <a-col :span="12" style="text-align: right">
                <a-space align="center" size="large" class="header-func-box">
                    <div>
                        <a-cascader :options="[{value: 'V1.0.1',label: 'V1.0.1'}, {value: 'V1.1.1',label: 'V1.1.1'}]">
                            <a href="#">版本</a>
                        </a-cascader>
                    </div>
                    <div style="height: 80px">
                        <GiteeBox />
                    </div>
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
    font-size: 17px;
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