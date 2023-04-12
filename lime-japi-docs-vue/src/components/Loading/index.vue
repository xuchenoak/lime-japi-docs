<template>
    <a-spin :spinning="loading" :indicator="indicator(icon)" :tip="text">
        <slot/>
    </a-spin>
</template>

<script>
export default {
    name: "index",
    props: {
        loading: {
            type: Boolean,
            default() {
                return false
            }
        }
    },
    data() {
        return {
            text: '努力加载中……',
            icon: 'loading',
        }
    },
    methods: {
        indicator(icon) {
            return <a-icon type={icon} style="font-size: 24px" spin />;
        },
        setText(text) {
            this.text = text;
        }
    }
}
</script>

<style scoped lang="less">
    /deep/ .ant-spin {
        position: fixed;
        top: 50%!important;
        transform: translateY(-50%);
    }
</style>
