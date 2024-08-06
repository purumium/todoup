import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'normalize.css';

// FontAwesome 관련 import
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';

// FontAwesome 라이브러리에 아이콘 추가
library.add(fas);

// Vue 애플리케이션 생성
const app = createApp(App);

// Router와 Store를 Vue 인스턴스에 추가
app.use(router);
app.use(store);

// FontAwesome 아이콘 컴포넌트 등록
app.component('font-awesome-icon', FontAwesomeIcon);

// 애플리케이션을 마운트
app.mount('#app');
