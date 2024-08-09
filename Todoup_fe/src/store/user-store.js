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
      localStorage.setItem('user_info', JSON.stringify(payload));
    },
    logout(state, payload) {
      state.user_info = payload;
      localStorage.removeItem('user_info');
    },
    logfail(state, payload) {
      state.user_info = payload;
    },
  },

  actions: {
    login(context, payload) {
      context.commit('login', payload);
    },
    logout(context, payload) {
      context.commit('logout', payload);
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
