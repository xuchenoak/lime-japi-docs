<template>
    <span style="padding: 0 5px">
        <a-tooltip :placement="position" :title="text || position" arrow-point-at-center :destroyTooltipOnHide="true">
          <a-icon class="why-icon" type="question-circle" theme="filled"/>
        </a-tooltip>
    </span>
</template>

<script>
export default {
    name: "index",
    props: {
        position: {
            type: String,
            default() {return "top"}
        },
        text: {
            type: String,
            default() {return ""}
        },
    },
}
</script>
<style scoped lang="less">
.why-icon {
    color: #777;
    transition: all .5s;
}
.why-icon:hover {
    color: #444;
}
</style>