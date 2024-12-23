const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        port: 7000
    },
    chainWebpack: config => {
        config.plugin('html')
            .tap(args => {
                args[0].title = "臻享商城";
                return args;
            })
    }

})
