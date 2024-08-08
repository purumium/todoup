import { createStore } from 'vuex';

import UserStore from '@/store/user-store';
import modalStore from './modal-store';

const store = createStore({
  modules: {
    modal: modalStore,
    user: UserStore,
  },
});

export default store;
