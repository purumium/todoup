import { createStore } from 'vuex';
import modalStore from './modal-store';

const store = createStore({
  modules: {
    modal: modalStore,
  },
});

export default store;
