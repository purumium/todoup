import axios from 'axios';

const modalStore = {
  state: {
    isModalVisible: false,
    followUsers: [], // 팔로우한 사용자를 저장할 상태
  },
  mutations: {
    SET_FOLLOWED_USERS(state, users) {
      state.followUsers = users; // API 응답 데이터를 상태에 저장
    },
    TOGGLE_MODAL(state) {
      state.isModalVisible = !state.isModalVisible;
    },
    SET_MODAL_VISIBLE(state, visible) {
      state.isModalVisible = visible;
      if (!visible) {
        state.followUsers = []; // 모달을 닫을 때 팔로우 사용자 목록 초기화
      }
    },
  },
  actions: {
    async fetchFollowedUsers({ commit }, userId) {
      try {
        console.log('Fetching users for userId:', userId); // userId 값을 확인
        const response = await axios.get(`/api/following`, { params: { userId } });
        commit('SET_FOLLOWED_USERS', response.data);
        commit('TOGGLE_MODAL');
      } catch (e) {
        console.error('Error fetching followed users:', e);
      }
    },
    async followUser({ dispatch }, { userId, followId }) {
      try {
        await axios.post(`/api/add/following`, null, { params: { userId, followId } });
        await dispatch('fetchFollowedUsers', userId); // 팔로우 후 팔로우 사용자 목록 갱신
      } catch (e) {
        console.error('Error following user:', e);
      }
    },
    async unfollowUser({ dispatch }, { userId, followId }) {
      try {
        await axios.delete(`/api/remove/following`, { params: { userId, followId } });
        await dispatch('fetchFollowedUsers', userId); // 언팔로우 후 팔로우 사용자 목록 갱신
      } catch (e) {
        console.error('Error unfollowing user:', e);
      }
    },
    setModalVisible({ commit }, visible) {
      commit('SET_MODAL_VISIBLE', visible);
    },
  },
  getters: {
    isModalVisible: (state) => state.isModalVisible,
    followUsers: (state) => state.followUsers, // followUsers 상태를 반환하는 게터
  },
};

export default modalStore;
