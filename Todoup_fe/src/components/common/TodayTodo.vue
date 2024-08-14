<template>
  <div class="todo-section">
    <div>Today's TODO</div>
    <ul class="todo-list" v-if="todoList.length > 0">
      <li v-for="(todo, idx) in todoList" :key="idx">
        <input type="checkbox" v-model="todo.completed" @change="toggleCompletion(todo)" />
        <span :class="{ 'check-line-through': todo.completed }">
          {{ todo.title }}
        </span>
      </li>
    </ul>

    <ul class="todo-list rainbow" v-else>
      <li class="todo-empty"><span>조회된 데이터가 없습니다.!</span></li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';
import { mapState } from 'vuex';

export default {
  name: 'TodayTodo',
  data() {
    return {
      todoList: [],
    };
  },
  computed: {
    ...mapState('user', {
      userInfo: 'user_info', // Vuex의 user_info 상태를 userInfo로 매핑
    }),
    ...mapState('todo', {
      todoInfo: 'todo_info', // Vuex의 todo_info 상태를 todoInfo로 매핑
    }),
  },
  watch: {
    userInfo: {
      handler(newValue) {
        if (!newValue.userId) {
          this.loadExampleTodos(); // 예시 데이터를 불러옴
        } else {
          this.loadTodayTodos();
        }
      },
      immediate: true, // 컴포넌트 생성 시에도 watcher를 즉시 호출
    },
  },
  created() {
    if (!this.userInfo.userId) {
      this.loadExampleTodos();
    } else {
      this.loadTodayTodos();
    }
  },
  methods: {
    async loadTodayTodos() {
      try {
        const response = await axios.get('/api/todo/today', {
          params: { userId: this.userInfo.userId },
        });
        this.todoList = response.data.map((todo) => ({
          todo_id: todo.todo_id,
          title: todo.title, // title을 text로 변환
          completed: todo.completed != 1 ? false : true, // 체크박스 상태를 초기화
        }));
      } catch (error) {
        console.error("Error loading today's todos:", error);
      }
    },
    loadExampleTodos() {
      const exampleTodos = [
        { title: 'TO DO UP 사용해보기!', completed: true },
        { title: 'TO DO 추가!', completed: false },
        { title: 'TO DO 달성하고 레벨업하기!', completed: false },
      ];
      this.todoList = exampleTodos;
    },
    async toggleCompletion(todo) {
      if (!this.userInfo.userId) {
        this.$swal
          .fire({
            text: '로그인이 필요합니다.',
            icon: 'warning',
            confirmButtonText: '확인',
            confirmButtonColor: '#f39c12',
          })
          .then(() => {
            this.$router.push('/login');
          });
        return;
      }

      try {
        const userId = todo.user_id;
        const newCompletionStatus = todo.completed ? 0 : 1;
        await axios.post(`/api/todo/completion/${todo.todo_id}`, null, {
          params: {
            completed: newCompletionStatus,
            userId,
          },
        });
        todo.completed = newCompletionStatus;
        alert('할일 상태 변경');
      } catch (error) {
        console.error('Error toggling completion status:', error);
        alert('할일 상태를 업데이트하는 중 오류가 발생했습니다.');
      }
    },
  },
};
</script>

<style scoped>
.todo-section {
  margin-top: 25px;
  border: 2px solid #635e5e21;
  border-radius: 11px;
}

.todo-section div {
  border-bottom: 2px solid #635e5e21;
  padding: 12px 20px;
  font-size: 16px;
  font-weight: 600;
  color: #635a5a;
}

.todo-list {
  font-weight: 500;
  max-height: 163px;
  height: 163px;
  overflow-y: auto;
  padding: 15px 25px 13px 24px;
}

.todo-empty {
  width: 230px;
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.text-rainbow {
  background-image: linear-gradient(90deg, red, orange, yellow, green, blue, navy, purple);
  -webkit-background-clip: text;
  color: transparent;
  font-weight: bold;
}

li {
  list-style-type: none;
  font-size: 18px;
  margin-bottom: 8px;
}

li input[type='checkbox'] {
  margin-right: 10px;
}

.check-line-through {
  text-decoration: line-through;
}
</style>
