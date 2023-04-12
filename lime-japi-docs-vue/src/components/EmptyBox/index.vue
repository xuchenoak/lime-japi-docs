<template>
    <div class="box-bg">
        <a-empty :image="simpleImage" :description="text" />
    </div>
</template>

<script>
import { Empty } from 'ant-design-vue';
export default {
    name: "index",
    props: {
        text: {
            type: String,
            default() {return '数据走丢啦'}
        }
    },
    data() {
        return {
            simpleImage: Empty.PRESENTED_IMAGE_SIMPLE
        }
    }
}
</script>

<style scoped lang="less">
    .box-bg {
        position: absolute;
        margin: 0;
        top: 50%;
        width: 100%;
        transform: translateY(-50%);
    }
</style>
