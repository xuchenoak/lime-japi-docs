<template>
    <div class="box-bg"
         @click="handleLogo"
         :style="style"/>
</template>

<script>
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
        },
    },
    data() {
        return {
            style: {
                "--width": this.realWidth + 'px',
                "--height": this.realHeight + 'px',
                background: ''
            }
        }
    },
    watch: {
        '$store.getters.sysConfig.logoUrl'() {
            this.refreshStyle()
        }
    },
    mounted() {
        this.refreshStyle()
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
        },
        refreshStyle() {
            const url = this.$store.getters.sysConfig.logoUrl || ''
            if (url) {
                this.style['background'] = 'url(' + url + ') no-repeat'
            }
            this.style['--width'] = this.realWidth + 'px'
            this.style['--height'] = this.realHeight + 'px'
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
