<template>
    <a-tooltip :visible="copyStatus" :destroyTooltipOnHide="true" :placement="placement" :title="copyTitle">
        <div @click="handleCopy"
             @mouseenter.stop="handleMouseenter"
             @mouseleave.stop="handleMouseleave"
             class="copy-box"
             :data-clipboard-text="copyValue"
             :get-popup-container="getPopupContainer"
        >
            <slot></slot>
        </div>
    </a-tooltip>
</template>

<script>
    import Clipboard from 'clipboard';
    export default {
        props: {
            value: {
                type: String,
                default() { return "" }
            },
            placement: {
                type: String,
                default() { return "left" }
            },
            preTip: {
                type: Boolean,
                default() {return true}
            },
            disabled: {
                type: Boolean,
                default() {return false}
            }
        },
        data() {
            return {
                copyStatus: false,
                copyTitle: "点击复制",
                copyValue: ""
            }
        },
        methods: {
            /**
             * 鼠标移上
             */
            handleMouseenter() {
                if (this.preTip) {
                    this.copyTitle = "点击复制"
                    this.copyStatus = true
                }
            },
            /**
             * 鼠标移开
             */
            handleMouseleave() {
                if (this.copyTitle === "点击复制") this.copyStatus = false
            },
            /**
             * 点击触发复制
             */
            handleCopy() {
                if (this.disabled) return
                this.copyValue = this.value
                const btnCopy = new Clipboard('.copy-box');
                // 复制成功后执行的回调函数
                btnCopy.on('success', ()=> {
                    this.copyAfter(btnCopy, "复制成功")
                });
                // 复制失败后执行的回调函数
                btnCopy.on('error', ()=> {
                    this.copyAfter(btnCopy, "复制失败")
                });
            },
            /**
             * 复制之后
             */
            copyAfter(btnCopy, copyTitle) {
                this.copyTitle = copyTitle
                this.copyStatus = true
                setTimeout(()=> {
                    this.copyStatus = false
                    btnCopy.destroy()
                    this.copyValue = ""
                }, 1000)
            },
            /**
             * 如果被遮挡自动调整
             */
            getPopupContainer(trigger) {
                return trigger.parentElement;
            },
        }
    };
</script>

<style lang="less" scoped>
    .copy-box {
        display: inline-block;
    }
</style>
