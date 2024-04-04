<template>
    <div class="box-bg"
         @click="handleLogo"
         :style="'--width:' + realWidth + 'px; --height:' + realHeight + 'px; background: url(' + url + ') no-repeat'"/>
</template>

<script>
import img from "@/assets/images/lime-logo.png"
export default {
    name: "index",
    props: {
        width: {
            type: Number,
            default() {return 0}
        },
        height: {
            type: Number,
            default() {return 0}
        },
        openClick: {
            type: Boolean,
            default() {return false}
        }
    },
    data() {
        return {
            url: img
        }
    },
    mounted() {
        this.url = this.$store.getters.sysConfig.logoUrl || img
    },
    computed: {
        realWidth() {
            if (this.width) {
                return this.width
            }
            else if (this.height) {
                return this.height * 3.25
            }
            return 260
        },
        realHeight(){
            if (this.width) {
                return this.width / 3.25
            }
            else if (this.height) {
                return this.height
            }
            return 80
        }
    },
    methods: {
        handleLogo() {
            if (this.openClick) {
                window.location.reload()
            }
        }
    }
}
</script>

<style scoped lang="less">
.box-bg {
    height: var(--height);
    width: var(--width);
    background: url("~@/assets/images/lime-logo.png") no-repeat;
    background-size: var(--width) var(--height)!important;
    cursor: pointer;
}
</style>
