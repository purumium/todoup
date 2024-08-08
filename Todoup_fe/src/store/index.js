import { createStore } from 'vuex';

export default createStore({
  state: {
    isModalVisible: false,
  },
  mutations: {
    TOGGLE_MODAL(state) {
      state.isModalVisible = !state.isModalVisible;
    },
    SET_MODAL_VISIBLE(state, visible) {
      state.isModalVisible = visible;
    },
  },
  actions: {
    toggleModal({ commit }) {
      commit('TOGGLE_MODAL');
    },
    setModalVisible({ commit }, visible) {
      commit('SET_MODAL_VISIBLE', visible);
    },
  },
  getters: {
    isModalVisible: (state) => state.isModalVisible,
  },
});
