<template>
    <page-box title="v2.0.1" :background="background">
        <div class="info-btn">
            <a href="https://gitee.com/xuchenoak/lime-japi-docs/releases/download/v2.0.1/lime-japi-docs-runner-2.0.1.zip">
                <a-button size="large" type="primary" class="info-btn-item">下载</a-button>
            </a>
            <a href="">
                <a-button size="large" type="primary" class="info-btn-item" ghost>文档</a-button>
            </a>
        </div>
        <div class="version-box">
            <div class="version-box-item" :class="isWhite ? 'version-box-item-white' : ''">
                <div class="item-image">
                    <img src="https://pic4.zhimg.com/v2-5290caf1f5c8c270e6da56b0361c9e1b_r.jpg" alt="">
                </div>
                <div class="item-info">
                    <div>该项目为springboot项目，数据库使用H2，前端页面打包到了jar包中，提供了执行脚本，下载zip包解压后即可在java环境下运行。</div>
                </div>
            </div>
            <div class="version-box-item">
                <div class="item-image">
                    <img src="https://pic4.zhimg.com/v2-5290caf1f5c8c270e6da56b0361c9e1b_r.jpg" alt="">
                </div>
                <div class="item-info">
                    <div>该项目为springboot项目，数据库使用H2，前端页面打包到了jar包中，提供了执行脚本，下载zip包解压后即可在java环境下运行。</div>
                </div>
            </div>
            <div class="version-box-item">
                <div class="item-image">
                    <img src="https://pic4.zhimg.com/v2-5290caf1f5c8c270e6da56b0361c9e1b_r.jpg" alt="">
                </div>
                <div class="item-info">
                    <div>该项目为springboot项目，数据库使用H2，前端页面打包到了jar包中，提供了执行脚本，下载zip包解压后即可在java环境下运行。</div>
                </div>
            </div>
            <div class="version-box-item">
                <div class="item-image">
                    <img src="https://pic4.zhimg.com/v2-5290caf1f5c8c270e6da56b0361c9e1b_r.jpg" alt="">
                </div>
                <div class="item-info">
                    <div>该项目为springboot项目，数据库使用H2，前端页面打包到了jar包中，提供了执行脚本，下载zip包解压后即可在java环境下运行。</div>
                </div>
            </div>
        </div>
    </page-box>
</template>

<script>
export default {
    props: {
        background: {
            type: String,
            default() {return "#fff"}
        }
    },
    computed: {
        isWhite() {
            return this.background === "#fff"
        }
    }
}
</script>

<style scoped lang="less">
.info-btn {
    text-align: center;
    margin-bottom: 40px;
    .info-btn-item {
        width: 120px;
        border-radius: 25px;
        margin: 0 20px;
    }
}
.version-box {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    .version-box-item {
        width: 360px;
        transition: all .3s;
        border-radius: 16px;
        box-shadow: 2px 2px 20px rgba(0,0,0,.05);
        overflow: hidden;
        margin-bottom: 50px;
        border: 1px solid #eee;
        .item-image {
            overflow: hidden;
            >img {
                width: 100%;
            }
        }
        .item-info {
            padding: 20px;
            background-color: #fff;
            >div {
                text-align: justify;
                line-height: 30px;
                font-size: 18px;
            }
        }
    }
    .version-box-item-white {
        box-shadow: 2px 2px 20px rgba(0,0,0,.1);
    }
    .version-box-item:hover {
        box-shadow: 2px 2px 30px rgba(0,0,0,.15);
    }
}
</style>