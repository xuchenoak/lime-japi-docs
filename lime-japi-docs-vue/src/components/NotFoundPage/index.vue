<template>
    <div class="not-found-bg">
        <div class="empty-interface-bg">
            <div class="lime-logo">
                <lime-logo :width="460"/>
                <div class="lime-logo-slogan">{{slogan}}</div>
            </div>
            <div class="empty-box">
                <div class="empty-box-title">404</div>
                <div class="empty-box-not-found">PAGE NOT FOUND</div>
                <div class="empty-box-btn">
                    <a-button @click="backToRoot" type="primary">返回主页</a-button>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    name: "index",
    data() {
        return {
            slogan: ''
        }
    },
    created() {
        this.slogan = this.$store.getters.sysConfig.sysSlogan
    },
    methods: {
        // 返回主页
        backToRoot() {
            this.$router.push({path: '/'})
        }
    }
}
</script>

<style scoped lang="less">
.not-found-bg {
    height: 100vh;
    .empty-interface-bg {
        height: 390px;
        position: absolute;
        width: 100%;
        top: 40%;
        transform: translateY(-50%);
        .lime-logo {
            position: absolute;
            left: 50%;
            transform: translateX(-50%);
            .lime-logo-slogan {
                width: 100%;
                text-align: center;
                margin-top: 20px;
                font-size: 25px;
                font-weight: 200;
            }
        }
        .empty-box {
            position: absolute;
            height: 134px;
            width: 100%;
            bottom: 0;
            border-top: 1px dashed #ccc;
            .empty-box-title {
                padding-top: 20px;
                text-align: center;
                line-height: 70px;
                font-size: 40px;
                color: #999;
                letter-spacing: 3px;
            }
            .empty-box-not-found {
                text-align: center;
                line-height: 10px;
                color: #bbb;
            }
            .empty-box-btn {
                position: absolute;
                width: 100%;
                bottom: -30px;
                text-align: center;
            }
        }
    }
}
</style>