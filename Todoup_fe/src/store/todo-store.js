export default {
  namespaced: true, // 네임스페이스를 사용하여 모듈을 구분
  state() {
    return {
      todos: [],
    };
  },
  mutations: {
    ADD_TODO(state, todo) {
      state.todos.push(todo);
    },
    REMOVE_TODO(state, todoId) {
      state.todos = state.todos.filter((todo) => todo.id !== todoId);
    },
  },
  actions: {
    addTodo({ commit }, todo) {
      // 추가적인 비즈니스 로직을 여기에 작성 가능
      commit('ADD_TODO', todo);
    },
    removeTodo({ commit }, todoId) {
      commit('REMOVE_TODO', todoId);
    },
  },
  getters: {
    allTodos: (state) => state.todos,
  },
};
