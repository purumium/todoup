<template>
  <div>
    <h4 class="header">{{ formattedDate }}의 TODO</h4>
    <div class="todo-container">
      <div class="todo-list">
        <form @submit.prevent="addTodo" class="add-todo-form">
          <div class="input-container">
            <input class="add-input" v-model="newTodo.title" type="text" placeholder="할 일을 작성해주세요" required />
            <button type="submit" class="add-todo-button">추가</button>
          </div>
        </form>
        <ul class="todo-ul">
          <li v-for="todo in todayTodos" :key="todo.todo_id" @click="selectTodo(todo)" class="todo-item">
            <input type="checkbox" :checked="todo.completed" @change="toggleCompletion(todo)" class="todo-checkbox" />
            <p class="todo-title mb-0" :class="{ completed: todo.completed }">
              {{ todo.title }}
            </p>
          </li>
        </ul>
      </div>
      <div class="todo-detail-container">
        <todo-detail v-if="selectedTodo" :todo="selectedTodo" @todo-deleted="handleTodoDeleted"></todo-detail>
        <div v-else class="no-selection">
          <p>할 일을 선택해주세요</p>
          <img :src="`/avatar/${profileImg}.png`" alt="No selection" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import TodoDetail from './TodoDetail.vue';
import { mapState, mapGetters } from 'vuex';

export default {
  components: { TodoDetail },
  data() {
    return {
      date: this.$route.params.date,
      todayTodos: [],
      selectedTodo: null,
      newTodo: {
        user_id: this.userId,
        title: '',
        memo: '',
        category: '기타',
        start_date: '',
        end_date: '',
      },
    };
  },
  computed: {
    ...mapState('user', {
      userId: (state) => state.user_info.userId,
      points: (state) => state.user_info.points,
    }),

    ...mapState('todo', {
      todos: 'todo_info', // Vuex의 todo_info 상태를 todos로 매핑
    }),
    ...mapGetters({
      profileImg: 'user/getProfileImg', // Vuex의 profileImg 상태를 컴포넌트에 매핑
    }),
    formattedDate() {
      const date = new Date(this.date);
      return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      });
    },
  },
  created() {
    this.getTodos();
  },
  /*watch: {
    '$route.params.date'(newDate) {
      this.date = newDate;
      this.getTodos();
    },
  }, */

  methods: {
    /* async getTodos() {
      try {
        const userId = this.userId;
        const response = await axios.get(`/api/todo/date/${this.date}`, {
          params: { userId },
        });
        this.todos = response.data;

        const kor_time = new Date();
        const today =
          kor_time.getFullYear() +
          '-' +
          (kor_time.getMonth() + 1 < 10 ? '0' + (kor_time.getMonth() + 1) : kor_time.getMonth() + 1) +
          '-' +
          kor_time.getDate();

        const todayTodos = this.todos.filter((todo) => todo.start_date.split(' ')[0] === today);
        console.log(todayTodos);
        if (todayTodos.length > 0) {
          this.$store.commit('todo/SET_TODOS', todayTodos);
        }
        const selectedTodoId = this.$route.query.selectedTodoId;
        if (selectedTodoId) {
          this.selectedTodo = this.todos.find((todo) => todo.todo_id === parseInt(selectedTodoId));
        }
      } catch (error) {
        console.error('Error getting todos:', error);
      }
    }, */

    getTodos() {
      this.todayTodos = this.todos.filter((todo) => todo.start_date.split(' ')[0] === this.date);
    },
    async toggleCompletion(todo) {
      try {
        const userId = this.userId;
        const newCompletionStatus = todo.completed ? 0 : 1;
        await axios.post(`/api/todo/completion/${todo.todo_id}`, null, {
          params: {
            completed: newCompletionStatus,
            userId,
          },
        });
        todo.completed = newCompletionStatus;

        // 포인트를 update하는 로직 추가
        const pointsToAdd = newCompletionStatus ? 5 : -5; // 완료시 +5 포인트, 취소시 -5 포인트

        this.$store.dispatch('user/updatePoints', pointsToAdd);
      } catch (error) {
        console.error('Error toggling completion status:', error);
        this.$swal.fire({
          text: 'TODO의 완료 상태 변경에 실패하었습니다.',
          icon: 'error',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
      }
    },
    selectTodo(todo) {
      this.selectedTodo = todo;
    },
    handleTodoDeleted(deletedTodoId) {
      this.todos = this.todos.filter((todo) => todo.todo_id !== deletedTodoId);
      this.selectedTodo = null;
    },
    async addTodo() {
      try {
        this.newTodo.user_id = this.userId;
        this.newTodo.start_date = this.date;
        this.newTodo.end_date = this.date;
        const response = await axios.post('/api/todo/insert', this.newTodo);
        const createdTodoId = response.data;
        const todo = { todo_id: createdTodoId, ...this.newTodo, completed: false };
        console.log('추가된 값: ', todo);
        this.$store.commit('todo/ADD_TODO', todo);
        const newlyAddedTodo = this.todayTodos.find((todo) => todo.todo_id === createdTodoId);
        this.selectTodo(newlyAddedTodo);
        this.newTodo.title = '';
        this.newTodo.memo = '';
      } catch (error) {
        console.error('Error creating todo:', error);
        this.$swal.fire({
          text: '새로운 TODO 생성에 실패하었습니다.',
          icon: 'error',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
      }
    },
  },
};
</script>

<style scoped>
/* 전체 레이아웃 및 컨테이너 스타일 */
.todo-container {
  height: 525px;
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  gap: 20px;
  background-color: #8f91911a;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

/* 투두 리스트 스타일 */
.todo-list {
  overflow-x: none;
  overflow-y: auto;
  flex: 1;
  background-color: #ffffff;
  border-radius: 15px;
  padding: 35px 20px;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.header {
  color: #2b2222b8 !important;
  font-weight: 600;
  font-size: 18px;
  display: flex;
  padding: 14px 0;
  border-bottom: 2px solid #cfcece70;
  border-top: 2px solid #cfcece70;
  justify-content: center;
}

.input-container {
  display: flex;
  align-items: center;
  border-radius: 5px;
  overflow: hidden;
  padding: 4px 12px 4px 8px;
  border-radius: 25px;
  border: 2px solid #969ea442;
  margin-bottom: 8px;
}

.add-input {
  flex: 1;
  border: none;
  padding: 10px;
  outline: none;
  font-size: 12px;
}

.add-todo-button {
  outline: none;
  width: 67px;
  height: 25px;
  color: #544545;
  border: 1px solid #1d13132e;
  font-size: 11px;
  border-radius: 20px;
  background-color: #a4a4a426;
  border: none;
  border-radius: 16px;
}

.add-todo-button:hover {
  background-color: #d7d9db86;
}

/* 투두 항목 스타일 */
.todo-ul {
  list-style: none;
  padding-left: 0;
  margin-top: 0;
}

.todo-item {
  padding: 12px 10px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.todo-item:hover {
  background-color: #c5e0f6;
}

.todo-item:last-child {
  border-bottom: none;
}

.todo-checkbox {
  margin-right: 10px;
  transform: scale(1.2);
  cursor: pointer;
}

.todo-title {
  font-size: 16px;
  cursor: pointer;
  color: #333;
}

.todo-title.completed {
  text-decoration: line-through;
  color: #888;
}

/* 상세보기 컨테이너 스타일 */
.todo-detail-container {
  width: 267px;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ffffff;
  border-radius: 15px;
  padding: 0 10px;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

/* 
.no-selection {
  text-align: center;
  color: #888;
}

.no-selection img {
  max-width: 200px;
  margin-bottom: 20px;
}

.no-selection p {
  border-radius: 5px;
  padding: 10px 8px 12px 8px;
  border-radius: 25px;
  border: 2px solid #969ea442;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: bold;
} */

.no-selection {
  position: relative;
}

.no-selection img {
  max-width: 200px;
  margin-bottom: 20px;
}

.no-selection p {
  position: relative;
  top: 10em;
  text-align: center;
  padding: 10px 8px 12px 8px;
  border-radius: 25px;
  border: 2px solid #969ea442;
  font-size: 14px;
  font-weight: bold;
  color: #464040;
  background-color: #ffffff;
}

.no-selection p::after {
  content: '';
  position: absolute;
  bottom: -12px; /* 말풍선의 아래쪽에 위치하도록 */
  left: 50%; /* 가운데 정렬 */
  transform: translateX(-50%);
  border-width: 12px 10px 0 10px; /* 꼬리의 크기 */
  border-style: solid;
  background-color: white;
  border-color: #969ea442 transparent transparent transparent; /* 말풍선의 색과 일치 */
  display: block;
  width: 0;
}
</style>
