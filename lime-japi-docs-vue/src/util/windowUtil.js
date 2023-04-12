

const windowUtil = {
    //获取窗口尺寸
    getViewportOffset: ()=> {
        if (windowUtil.innerWidth) {
            return {
                x:windowUtil.innerWidth,
                y:windowUtil.innerHeight
            }
        } else {
            if (document.compatMode === "BackMode") {
                return {
                    x:document.body.clientWidth,
                    y:document.body.clientHeight
                }
            } else {
                return {
                    x:document.documentElement.clientWidth,
                    y:document.documentElement.clientHeight
                }
            }
        }
    }
}

export default windowUtil
