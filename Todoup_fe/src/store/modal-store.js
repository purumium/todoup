import axios from 'axios';

const modalStore = {
  namespaced: true,
  state: {
    isModalVisible: false,
    followUsers: [], // 팔로우한 사용자를 저장할 상태
    followedUsers: [], // 나를 팔로우한 사용자들
    allUsers: [], // 모든 유저들
    selectedUserId: null, // 선택된 유저 아이디를 저장할 상태
  },
  mutations: {
    SET_FOLLOWING_USERS(state, users) {
      state.followUsers = users; // 팔로우한 사용자 명단을 상태에 저장
    },
    SET_FOLLOWED_USERS(state, users) {
      state.followedUsers = users; // 팔로워 명단을 상태에 저장
    },
    SET_ALL_USERS(state, users) {
      state.allUsers = users; // 모든 유저를 상태에 저장
    },
    SET_SELECTED_USER_ID(state, userId) {
      state.selectedUserId = userId; // 선택된 유저 아이디를 상태에 저장
    },
    TOGGLE_MODAL(state) {
      state.isModalVisible = !state.isModalVisible;
    },
    SET_MODAL_VISIBLE(state, visible) {
      state.isModalVisible = visible;
      if (!visible) {
        state.followUsers = []; // 모달을 닫을 때 팔로우 사용자 목록 초기화
        state.followedUsers = []; // 모달을 닫을 때 나를 팔로우하는 사용자 목록 초기화
        state.allUsers = []; // 모달을 닫을 때 모든 유저 목록 초기화
        state.selectedUserId = null; // 선택된 유저 아이디 초기화
      }
    },
  },
  actions: {
    async fetchAllUsers({ commit }, userId) {
      try {
        const response = await axios.get('/api/allusers', { params: { userId } });
        commit('SET_ALL_USERS', response.data);
        commit('TOGGLE_MODAL');
      } catch (e) {
        console.error('Error fetching all users:', e);
      }
    },
    async fetchFollowedUsers({ commit }, userId) {
      try {
        const response = await axios.get(`/api/following`, { params: { userId } });
        commit('SET_FOLLOWING_USERS', response.data);
        commit('TOGGLE_MODAL');
      } catch (e) {
        console.error('Error fetching followed users:', e);
      }
    },
    async fetchFollowers({ commit }, userId) {
      try {
        const response = await axios.get(`/api/followers`, { params: { userId } });
        commit('SET_FOLLOWED_USERS', response.data);
        commit('TOGGLE_MODAL');
      } catch (e) {
        console.error('Error fetching followers:', e);
      }
    },
    async followUser(context, { userId, followId }) {
      try {
        await axios.post(`/api/add/following`, {
          userId: userId,
          followId: followId,
        });
      } catch (e) {
        console.error('Error following user:', e);
      }
    },
    async unfollowUser(context, { userId, followId }) {
      try {
        await axios.delete(`/api/cancel/following`, {
          params: {
            userId: userId,
            followId: followId,
          },
        });
      } catch (e) {
        console.error('Error unfollowing user:', e);
      }
    },
    async checkIfFollowing(context, { userId, followId }) {
      try {
        const response = await axios.get(`/api/isFollowing`, {
          params: {
            userId: userId,
            followId: followId,
          },
        });
        return response.data; // API에서 받은 true 또는 false 값 반환
      } catch (error) {
        console.error('Error checking follow status:', error);
        return false;
      }
    },
    async followersUser(context, { userId, followId }) {
      try {
        await axios.post(`/api/add/followers`, {
          userId: userId,
          followId: followId,
        });
      } catch (e) {
        console.error('Error following user:', e);
      }
    },
    async unfollowersUser(context, { userId, followId }) {
      try {
        await axios.delete(`/api/cancel/followers`, {
          params: {
            userId: userId,
            followId: followId,
          },
        });
      } catch (e) {
        console.error('Error unfollowing user:', e);
      }
    },
    async checkIfFollowers(context, { userId, followId }) {
      try {
        const response = await axios.get(`/api/isfollowers`, {
          params: {
            userId: userId,
            followId: followId,
          },
        });
        return response.data; // API에서 받은 true 또는 false 값 반환
      } catch (error) {
        console.error('Error checking follow status:', error);
        return false;
      }
    },
    setSelectedUserId({ commit }, userId) {
      commit('SET_SELECTED_USER_ID', userId); // 유저 아이디 설정
    },
    goToAvatarRoom({ state }) {
      const userId = state.selectedUserId;
      if (userId) {
        // Vue Router를 사용하여 라우팅
        this.$router.push(`/room/${userId}/avatarroom`);
      } else {
        console.error('No userId is selected');
      }
    },
    setModalVisible({ commit }, visible) {
      commit('SET_MODAL_VISIBLE', visible);
    },
  },
  getters: {
    isModalVisible: (state) => state.isModalVisible,
    followUsers: (state) => state.followUsers, // followUsers 상태를 반환하는 게터
    allUsers: (state) => state.allUsers, // allUsers 상태를 반환하는 게터
    selectedUserId: (state) => state.selectedUserId, // 선택된 유저 아이디 반환
  },
};

export default modalStore;
