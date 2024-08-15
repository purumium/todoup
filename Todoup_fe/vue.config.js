const { defineConfig } = require('@vue/cli-service');
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: { '^/api': '' },
      },
    },
  },
});
/* 
백엔드 서버에 localhost:8080/api에 요청을 보냄
pathRewrite : {'^/api :''} : 요청주소의 /api를 지워줌 
*/
