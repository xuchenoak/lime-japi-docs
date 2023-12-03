<template>
    <div class="page-content">
        <div class="content-row">
            <div class="info-box">
                <div class="info-title">
                    <h1>lime-japi-docs</h1>
                    <div style="position: relative; width: 70px; min-height: 32px;">
                        <a-tag class="info-version" color="orange">v2.0.1</a-tag>
                    </div>
                </div>
                <h4>
                    lime-japi-docs是一个简单的Java接口文档生成工具，可以解析Java源码生成Controller接口文档，在网页上点一点就能生成和共享多个不同Java项目的文档，再也不用担心前端找您要接口文档了！
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
            <div class="image-box">
                <div class="checked-box">
                    <div class="checked-item"
                         v-for="(item, index) in carouselList"
                         :key="item.name + '_' + index"
                         :class="item.checked ? 'checked-item-active' : ''"
                         @click="handleCarouselName(index)"
                    >
                        <span class="checked-item-text">{{item.name}}</span>
                    </div>
                </div>
                <a-carousel ref="carousel_box" class="carousel-box" arrows :dots="false" autoplay :beforeChange="carouselBeforeChange">
                    <div class="carousel-item"
                         v-for="(item, index) in carouselList"
                         :key="item.name + '_' + index">
                        <img :src="item.image" :alt="item.name">
                    </div>
                </a-carousel>
            </div>
        </div>
        <div class="content-row">
            <div class="feature-box">
                <h2>简单好部署</h2>
                <div>该项目为springboot项目，数据库使用H2，前端页面打包到了jar包中，提供了执行脚本，下载zip包解压后即可在java环境下运行。</div>
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
</template>

<script>
const carouselList = [{
    name: "文档中心",
    image: "https://xuchenoak.com/prod-file/sc/20230628/e045110ee1c14e388e5cbe2a120dafd8.jfif",
    checked: true,
},{
    name: "文档配置",
    image: "https://pic4.zhimg.com/v2-5290caf1f5c8c270e6da56b0361c9e1b_r.jpg",
    checked: false,
},{
    name: "文档预览",
    image: "https://ts1.cn.mm.bing.net/th/id/R-C.5056d20a2fcfe1794835f90f4c4eca64?rik=SEpfVgjeh03QcA&riu=http%3a%2f%2fpic.bizhi360.com%2fbbpic%2f48%2f1848.jpg&ehk=dBHaUToqhCbgvxKsMlNzjIiUgH63MJ6%2fgD3Y7Hbk4Es%3d&risl=&pid=ImgRaw&r=0",
    checked: false,
},{
    name: "文档解析",
    image: "https://ts1.cn.mm.bing.net/th/id/R-C.15e970cd0765096178a6da16993cfbb1?rik=IT5KfevidZcTig&riu=http%3a%2f%2fimg.pconline.com.cn%2fimages%2fupload%2fupc%2ftx%2fwallpaper%2f1210%2f22%2fc0%2f14558824_1350879506501.jpg&ehk=X9ro%2fg%2fGTmsglVrbV%2bmy8c3wsAvcHseqcEhsf80RMWA%3d&risl=&pid=ImgRaw&r=0",
    checked: false,
}]
export default {
    data() {
        return {
            carouselList: carouselList
        }
    },
    methods: {
        // 轮播图回调
        carouselBeforeChange(form, to) {
            this.carouselList.map(item => {
                item.checked = false
            })
            this.carouselList[to].checked = true
        },
        // 点击轮播图标题
        handleCarouselName(index) {
            this.carouselBeforeChange(null, index)
            this.$nextTick(()=> {
                this.$refs["carousel_box"].goTo(index, true)
            })
        }
    }
}
</script>

<style scoped lang="less">
.page-content {
    padding: 50px 0 70px 0;
    margin: 0 auto;
    max-width: 1200px;
    min-height: calc(100vh - 80px);
    .content-row {
        padding: 40px 0;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
    }
    .info-box {
        height: 400px;
        width: 460px;
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
                line-height: 32px;
                text-align: center;
                font-size: 20px;
                padding: 0 10px;
                border-radius: 2px;
                user-select: none;
                margin: 0;
                border-radius: 16px;
            }
        }
        >h4 {
            color: #666;
            text-align: justify;
            font-size: 22px;
            line-height: 40px;
        }
        .info-btn {
            margin-top: 40px;
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            .info-btn-item {
                width: 120px;
                border-radius: 25px;
            }
        }
    }
    .image-box {
        position: relative;
        padding: 20px 0;
        .checked-box {
            position: absolute;
            bottom: 20px;
            left: 0;
            width: 100%;
            z-index: 1;
            display: flex;
            justify-content: space-between;
            border-radius: 0 0 16px 16px;
            cursor: pointer;
            overflow: hidden;
            .checked-item {
                width: 25%;
                height: 40px;
                line-height: 40px;
                text-align: center;
                color: #fff;
                background: rgba(0,0,0,.5);
                user-select: none;
            }
            .checked-item-active {
                background: rgba(0,0,0,.6);
                .checked-item-text {
                    position: relative;
                }
                .checked-item-text::before {
                    content: '';
                    width: 6px;
                    height: 6px;
                    background: #fff;
                    position: absolute;
                    left: -12px;
                    top: 50%;
                    transform: translateY(-3px);
                    border-radius: 50%;
                }
            }
        }
        .carousel-box {
            max-width: 650px;
            height: 360px;
            overflow: hidden;
            user-select: none;
            cursor: pointer;
            border-radius: 16px;
            //border: 1px solid #eee;
            box-shadow: 0 3px 20px rgba(0,0,0,.1);
            .carousel-item {
                >img {
                    height: 360px;
                    width: 650px;
                }
            }
        }
        .carousel-box:hover {
            .custom-slick-arrow {
                opacity: 0.3;
            }
        }
    }
    .feature-box {
        height: 200px;
        width: 360px;
        padding: 20px;
        border: 1px solid #eee;
        transition: all .3s;
        border-radius: 16px;
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
        //border: 1px solid #999;
        box-shadow: 0 2px 20px rgba(0,0,0,.1);
    }
}
</style>