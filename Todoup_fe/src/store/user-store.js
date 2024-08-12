const store = {
  namespaced: true,
  state() {
    return {
      user_info: {},
    };
  },
  mutations: {
    login(state, payload) {
      state.user_info = payload;
    },
    logout(state) {
      state.user_info = {};
    },
    logfail(state, payload) {
      state.user_info = payload;
    },
  },

  actions: {
    login(context, payload) {
      context.commit('login', payload);
    },
    logout(context) {
      localStorage.removeItem('user_info');
      context.commit('logout');
    },
    logfail(context, payload) {
      context.commit('logfail', payload);
    },
  },
  getters: {
    getUserInfo(state) {
      return state.user_info;
    },
  },
};

export default store;
