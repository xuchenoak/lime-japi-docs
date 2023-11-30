<template>
    <div>
        <div class="page-content">
            <div class="content-row">
                <div class="info-box">
                    <div class="info-title">
                        <h1>lime-japi-docs</h1>
                        <div style="position: relative; width: 70px; min-height: 32px;">
                            <div class="info-version">v2.0.1</div>
                        </div>
                    </div>
                    <h4>
                        lime-japi-docs是一个简单的Java接口文档生成工具，可以解析Java源码及其注释生成Controller接口文档，支持在网页端查看和一键生成文档。
                    </h4>
                    <div class="info-btn">
                        <a href="https://gitee.com/xuchenoak/lime-japi-docs/releases/download/v2.0.1/lime-japi-docs-runner-2.0.1.zip">
                            <a-button size="large" type="primary" class="info-btn-item">下载</a-button>
                        </a>
                        <a href="">
                            <a-button size="large" type="primary" class="info-btn-item" ghost>文档</a-button>
                        </a>
                        <a href="https://gitee.com/xuchenoak/lime-japi-docs" target="_blank">
                            <a-button size="large" type="danger" class="info-btn-item" ghost>Gitee</a-button>
                        </a>
                    </div>
                </div>
                <a-carousel class="carousel-box" arrows>
                    <div slot="prevArrow" class="custom-slick-arrow" style="left: 15px;zIndex: 1">
                        <a-icon type="left-circle" />
                    </div>
                    <div slot="nextArrow" class="custom-slick-arrow" style="right: 15px">
                        <a-icon type="right-circle" />
                    </div>
                    <div class="carousel-item">
                        <img src="https://xuchenoak.com/prod-file/sc/20230628/e045110ee1c14e388e5cbe2a120dafd8.jfif" alt="">
                    </div>
                    <div class="carousel-item">
                        <img src="https://xuchenoak.com/prod-file/sc/20230628/e045110ee1c14e388e5cbe2a120dafd8.jfif" alt="">
                    </div>
                </a-carousel>
            </div>
            <div class="content-row">
                <div class="feature-box">
                    <h2>简单好部署</h2>
                    <div>该项目为springboot项目，数据库使用H2，前端页面打包到了jar包中，提供了执行脚本，下载即可在java环境下启动。</div>
                </div>
                <div class="feature-box">
                    <h2>项目无侵入</h2>
                    <div>该项目独立运行，只需在目标项目进行基本javadoc注释，并在该项目内配置好目标项目的源码路径等配置即可执行解析得到文档。</div>
                </div>
                <div class="feature-box">
                    <h2>开源可扩展</h2>
                    <div>该项目的解析器已开源上传maven仓库，可单独引入项目解析源码得到接口数据，项目也将在未来的版本迭代中更新更多的功能。</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

</script>

<style scoped lang="less">
.page-content {
    padding-top: 50px;
    margin: 0 auto;
    max-width: 1200px;
    .content-row {
        padding: 30px 0;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
    }
    .info-box {
        height: 400px;
        width: 450px;
        .info-title {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            margin-bottom: 20px;
            >h1 {
                font-family: "dingding";
                color: #6EC4C4;
                font-size: 52px;
                margin: 0;
            }
            .info-version {
                position: absolute;
                right: 0;
                bottom: 10px;
                width: 70px;
                height: 32px;
                line-height: 32px;
                text-align: center;
                background: #EDCE02;
                font-size: 20px;
                color: #fff;
                padding: 0 10px;
                border-radius: 2px;
                user-select: none;
            }
        }
        >h4 {
            color: #666;
            text-align: justify;
            font-size: 25px;
            line-height: 45px;
        }
        .info-btn {
            margin-top: 45px;
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            .info-btn-item {
                width: 120px;
            }
        }
    }
    .carousel-box {
        max-width: 700px;
        height: 400px;
        overflow: hidden;
        user-select: none;
        cursor: pointer;
        .custom-slick-arrow {
            width: 25px;
            height: 25px;
            font-size: 25px;
            color: #444;
            opacity: 0;
            transition: all .5s;
        }
        .custom-slick-arrow:before {
            display: none;
        }
        .custom-slick-arrow:hover {
            opacity: 0.6;
        }
        .carousel-item {
            >img {
                height: 400px;
                width: 700px;
            }
        }
    }
    .carousel-box:hover {
        .custom-slick-arrow {
            opacity: 0.3;
        }
    }
    .feature-box {
        height: 200px;
        width: 360px;
        padding: 20px;
        border: 1px solid #eee;
        transition: all .5s;
        border-radius: 2px;
        >h2 {
            width: 100%;
            text-align: center;
        }
        >div {
            text-align: justify;
            line-height: 30px;
            font-size: 18px;
        }
    }
    .feature-box:hover {
        border: 1px solid #999;
    }
}
</style>