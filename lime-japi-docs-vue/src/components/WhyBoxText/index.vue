<template>
    <p class="why-box-text">{{text}}</p>
</template>

<script>
export default {
    name: "index",
    props: {
        text: {
            type: String,
            default() {return ""}
        },
    },
}
</script>
<style scoped lang="less">
.why-box-text {
    position: relative;
    margin: 0;
    padding: 0;
    font-size: 12px;
    color: #999;
}
</style>