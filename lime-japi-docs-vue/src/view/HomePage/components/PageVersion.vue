<template>
    <page-box title="v2.0.1" :background="background">
        <div class="version-box">
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
            <div class="func-box"></div>
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
    }
}
</script>

<style scoped lang="less">
.version-box {
    //box-shadow: 2px 2px 20px rgba(0,0,0,.05);
    display: flex;
    justify-content: space-between;
    .carousel-box {
        width: 600px;
        height: 350px;
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
                height: 350px;
                width: 600px;
            }
        }
    }
    .carousel-box:hover {
        .custom-slick-arrow {
            opacity: 0.3;
        }
    }
    .func-box {
        width: 50%;
        background: #fff;
    }
}
</style>