<template>
    <div class="box-bg-fixed"
         :style="'--width:' + width + 'px'"/>
</template>

<script>
export default {
    name: "index",
    props: {
        width: {
            type: Number,
            default() {return 500}
        }
    },
}
</script>

<style scoped lang="less">
    .box-bg-fixed {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        z-index: -1;
        opacity: 0.2; /* 控制水印透明度 */
        pointer-events: none; /* 防止水印遮挡用户交互 */
        background-image: url("~@/assets/images/lime-logo-fixed.png"); /* 替换成你的 Logo 图片路径 */
        background-size: var(--width);
        background-repeat: repeat; /* 控制水印重复方式 */
    }
</style>
