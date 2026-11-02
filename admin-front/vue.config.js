const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  publicPath: './',
  devServer:{
    port: 8082,
    proxy: {
      '/api':{
        target: 'http://localhost:8080',
        pathRewrite:{'^/api' : ''}
      }
    }
  }
})
