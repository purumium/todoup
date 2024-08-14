import { createStore } from 'vuex';
import createPersistedState from 'vuex-persistedstate';

import UserStore from '@/store/user-store';
import modalStore from './modal-store';
import todoStore from './todo-store';

const store = createStore({
  modules: {
    modal: modalStore,
    user: UserStore,
    todo: todoStore,
  },
  plugins: [
    createPersistedState({
      key: 'vuex-user-info', // 로컬 스토리지에 저장될 키 이름
      paths: ['user.user_info', 'user.profileImg'],
    }),
  ],
});

export default store;
