<template>
    <div class="page-box-bg" :style="'--background:' + background">
        <div class="page-box-content-bg">
            <div class="page-box-content-header">
                <span class="syst-heavy" :style="'--lineColor:' + lineColor">{{ title }}</span>
            </div>
            <div class="page-box-content-footer">
                <div class="page-box-content-slot">
                    <slot></slot>
                </div>
                <div v-if="more.enabled" class="page-box-content-more">
                    <a-button v-if="more.link" type="link" class="diy-cursor" @click="handleMore">{{more.text}}</a-button>
                    <span v-else>{{more.text}}</span>
                </div>
                <div v-else-if="total > 0">
                    <a-pagination :default-current="1" :defaultPageSize="pageSize" :total="total" @change="handlePage" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "index",
    props: {
        background: {
            type: String,
            default() {return '#fff'}
        },
        title: {
            type: String,
            default() {return ''}
        },
        lineColor: {
            type: String,
            default() {return '#54d373'}
        },
        more: {
            type: Object,
            default() {
                return {
                    enabled: false,
                    link: false,
                    text: '更多……'
                }
            }
        },
        total: {
            type: Number,
            default() {return 0}
        },
        pageSize: {
            type: Number,
            default() {return 10}
        },
    },
    methods: {
        handleMore() {
            this.$emit('handleMore')
        },
        handlePage(pageNum, pageSize) {
            this.$emit('handlePage', pageNum, pageSize)
        }
    }
}
</script>

<style scoped lang="less">
.page-box-bg {
    height: auto;
    background: var(--background);
    overflow: hidden;

    .page-box-content-bg {
        width: 100%;
        max-width: 1200px;
        margin: 0 auto;
        position: relative;
        .page-box-content-header {
            text-align: center;
            height: 200px;
            >span {
                position: relative;
                display: inline-block;
                margin-top: 90px;
                font-size: 30px;
                font-weight: 900;
                color: #555;
                font-family: 思源宋体;
            }
            >span::before {
                content: '';
                position: absolute;
                bottom: -20px;
                left: 50%;
                width: 50px;
                transform: translateX(-25px);
                height: 5px;
                background: var(--lineColor);
                border-radius: 10px;
            }
        }
        .page-box-content-footer {
            margin-bottom: 50px;
            .page-box-content-slot {
                min-height: 100px;
                //background: #2196F3;
            }
            .page-box-content-more {
                height: 25px;
                line-height: 50px;
                width: 100%;
                margin-top: 50px;
                * {
                    font-size: 17px;
                    padding: 0;
                    font-weight: 100;
                }
            }
        }

    }
}
</style>
