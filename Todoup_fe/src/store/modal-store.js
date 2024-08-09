import axios from 'axios';

const modalStore = {
  state: {
    isModalVisible: false,
    followUsers: [],
  },
  mutations: {
    SET_FOLLWED_USERS(state, users) {
      state.followUsers = users;
    },
    TOGGLE_MODAL(state) {
      state.isModalVisible = !state.isModalVisible;
    },
    SET_MODAL_VISIBLE(state, visible) {
      state.isModalVisible = visible;
      if (!visible) {
        state.followUsers = [];
      }
    },
  },
  actions: {
    async fetchFollowedUsers({ commit }, userId) {
      try {
        const reponse = await axios.get(`/following/${userId}`);
        commit('TOGGLE_MODAL');
        commit('SET_FOLLWED_USERS', reponse.data);
      } catch (e) {
        console.error('fetching followed users', e);
      }
    },
    setModalVisible({ commit }, visible) {
      commit('SET_MODAL_VISIBLE', visible);
    },
  },
  getters: {
    isModalVisible: (state) => state.isModalVisible,
    followUsers: (state) => state.followUsers,
  },
};

export default modalStore;
