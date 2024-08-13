export default {
  namespaced: true,
  state() {
    return {
      todo_info: [], // todos 배열을 상태로 관리, 이름을 todo_info로 변경
    };
  },
  mutations: {
    ADD_TODO(state, todo) {
      state.todo_info.push(todo); // 새로운 todo를 추가
    },
    SET_TODOS(state, todos) {
      state.todo_info = todos; // todos 배열을 상태로 저장, todo_info로 변경
    },
    REMOVE_TODO(state, todoId) {
      state.todo_info = state.todo_info.filter((todo) => todo.todo_id != todoId); // 해당 할 일을 상태에서 제거
    },
  },

  getters: {
    allTodos: (state) => state.todo_info, // 모든 todos를 반환하는 getter
    todayTodos: (state) => {
      const today = new Date().toISOString().split('T')[0];
      return state.todo_info.filter((todo) => todo.start_date.split(' ')[0] === today); // 오늘의 todos만 반환하는 getter
    },
  },
};
