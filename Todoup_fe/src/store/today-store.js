export default {
  namespaced: true,
  state() {
    return {
      /* todo_info: [], */ // todos 배열을 상태로 관리, 이름을 todo_info로 변경
      today_info: [],
    };
  },
  mutations: {
    /* ADD_TODO(state, todo) {
        state.todo_info.push(todo); // 새로운 todo를 추가
      },
      SET_TODOS(state, todos) {
        state.todo_info = todos; // todos 배열을 상태로 저장, todo_info로 변경
      },
      REMOVE_TODO(state, todoId) {
        state.todo_info = state.todo_info.filter((todo) => todo.todo_id != todoId); // 해당 할 일을 상태에서 제거
      },
      UPDATE_TODO(state, newTodo) {
        let todo = state.todo_info.find((t) => t.todo_id === newTodo.todo_id);
        if (todo) {
          Object.assign(todo, newTodo); // 기존 객체의 프로퍼티를 newTodo의 값으로 덮어씀
        }
      },
      TODO_COMPLETION(state, { todoId, completed }) {
        const todo = state.todo_info.find((t) => t.todo_id === todoId);
        if (todo) {
          todo.completed = completed;
        }
      }, */
    ADD_TODAY(state, todo) {
      state.today_info.push(todo); // 새로운 todo를 추가
    },
    SET_TODAYS(state, todos) {
      state.today_info = todos; // todos 배열을 상태로 저장, todo_info로 변경
    },
    REMOVE_TODAY(state, todoId) {
      state.today_info = state.today_info.filter((todo) => todo.todo_id != todoId); // 해당 할 일을 상태에서 제거
    },
    UPDATE_TODAY(state, newTodo) {
      let todo = state.today_info.find((t) => t.todo_id === newTodo.todo_id);
      if (todo) {
        Object.assign(todo, newTodo); // 기존 객체의 프로퍼티를 newTodo의 값으로 덮어씀
      }
    },
    TODAY_COMPLETION(state, { todoId, completed }) {
      const todo = state.today_info.find((t) => t.todo_id === todoId);
      if (todo) {
        todo.completed = completed;
      }
    },
  },

  getters: {
    /* allTodos: (state) => state.todo_info, // 모든 todos를 반환하는 getter */
    todayTodos: (state) => state.today_info,
  },
};
