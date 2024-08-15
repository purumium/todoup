import { createStore } from 'vuex';
import createPersistedState from 'vuex-persistedstate';

import UserStore from '@/store/user-store';
import modalStore from './modal-store';
import todoStore from './todo-store';
import todayStore from './today-store';

const store = createStore({
  modules: {
    modal: modalStore,
    user: UserStore,
    todo: todoStore,
    todaytodo: todayStore,
  },
  plugins: [
    createPersistedState({
      key: 'vuex-user-info', // 로컬 스토리지에 저장될 키 이름
      paths: [
        'user.user_info',
        'user.profileImg',
        'modal.selectedUserId',
        'modal.selectedUserLv',
        'modal.selectedUserType',
        'modal.selectedUserPoint',
        'modal.selectedUserNickname',
      ],
    }),
  ],
});

export default store;
