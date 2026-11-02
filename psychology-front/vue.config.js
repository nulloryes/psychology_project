const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  publicPath: './',
  devServer:{
    port: 8081,
    proxy: {
      '/api':{
        target: 'http://localhost:8080',
        pathRewrite:{'^/api' : ''}
      }
    }
  }
})
