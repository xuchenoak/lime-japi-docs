
module.exports = {
    // baseUrl:'/', // baseUrl根路径
    publicPath: '/',
    outputDir:'dist',// 执行npm run build打包时的构建输出目录名称，通常命名为dist
    assetsDir: 'assets',// 静态资源目录（js、css、img、fonts）
    lintOnSave: false,// 是否开启eslint保存检测，建议关闭。可选项：true || false || 'error'
    css: {
        loaderOptions: {
            less: {
                modifyVars: {
                    // less vars，customize ant design theme

                    'primary-color': '#3cba92',
                    'link-color': '#0ba360',
                    'border-radius-base': '2px'
                },
                // DO NOT REMOVE THIS LINE
                javascriptEnabled: true
            }
        }
    },
    devServer:{
        port: 8081, // 配置端口号
        open: false, // 启动项目后自动打开浏览器,可选项：true || false
        // host: 'localhost', // 定义域名。如果你希望服务器外部访问，可以将host设置成'0.0.0.0'，外部可以通过ip地址进行访问
        https: false, // https协议开关
        // 跨域
        proxy: {
            // 配置跨域
            '/lime_japi_docs':{
                target:'http://localhost:2023/', // target host
                ws:true, // proxy websockets 是否支持 websocket
                secure: false, // 如果是 https 接口，需要配置这个参数
                changeOrigin: true, // 设置成true,发送请求头中host会设置成target。request URL是http://localhost:5000/api/。设置成false, 请求头中host仍然是浏览器发送过来的host。也就是localhost:9000
                pathRewrite: {
                    '^/':'' // 重写路径。eg.'^/api': '/' 这种接口配置出来http://127.0.0.1:10001/login; '^/api': '/api' 这种接口配置出来http://127.0.0.1:10001/api/login
                }

            }
        }
    }
}
