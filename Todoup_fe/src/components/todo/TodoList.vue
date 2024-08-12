<template>
  <div>
    <h4>{{ formattedDate }}</h4>
    <div class="d-flex">
      <div class="todo-list border">
        <div>TodoList</div>
        <form @submit.prevent="addTodo">
          <input v-model="newTodo.title" type="text" placeholder="할일 추가" required />
          <button type="submit">추가</button>
        </form>
        <ul>
          <li v-for="todo in todos" :key="todo.todoId" class="d-flex align-items-center">
            <input type="checkbox" :checked="todo.completed" @change="toggleCompletion(todo)" />
            <p class="align-middle" @click="selectTodo(todo)">{{ todo.title }}</p>
          </li>
        </ul>
      </div>
      <!-- Todo Detail or Placeholder Image -->
      <div class="todo-detail-container">
        <todo-detail v-if="selectedTodo" :todo="selectedTodo" @todo-deleted="handleTodoDeleted"></todo-detail>
        <div v-else class="no-selection">
          <img src="@/assets/avatar_test.png" alt="No selection" />
          <p>할일을 선택해주세요</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import TodoDetail from './TodoDetail.vue';
import { mapState } from 'vuex';

export default {
  components: { TodoDetail },
  data() {
    return {
      date: this.$route.params.date, // URL에서 날짜를 가져와 초기화
      todos: [], // 투두 리스트를 저장할 배열
      selectedTodo: null, // 선택된 투두 항목을 저장할 객체
      newTodo: {
        user_id: 6,
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
    }),
    formattedDate() {
      // 날짜 포맷팅
      const date = new Date(this.date);
      return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      });
    },
  },
  created() {
    this.getTodos(); // 컴포넌트가 생성될 때 투두 리스트를 가져옴
  },
  methods: {
    async getTodos() {
      try {
        const userId = this.userId;
        const response = await axios.get(`/api/todo/date/${this.date}`, {
          params: { userId },
        });
        this.todos = response.data; // 응답 데이터를 todos 배열에 저장

        // todos를 가져온 후 selectedTodoId를 처리
        const selectedTodoId = this.$route.query.selectedTodoId;
        if (selectedTodoId) {
          this.selectedTodo = this.todos.find((todo) => todo.todo_id === parseInt(selectedTodoId));
        }
      } catch (error) {
        console.error('Error getting todos:', error);
      }
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
        alert('할일 상태 변경');
      } catch (error) {
        console.error('Error toggling completion status:', error);
        alert('할일 상태를 업데이트하는 중 오류가 발생했습니다.');
      }
    },
    selectTodo(todo) {
      this.selectedTodo = todo; // 선택된 투두 항목을 selectedTodo에 저장
    },
    handleTodoDeleted(deletedTodoId) {
      // 삭제된 투두를 todos 배열에서 제거
      this.todos = this.todos.filter((todo) => todo.todo_id !== deletedTodoId);

      // 선택된 투두 초기화
      this.selectedTodo = null;
    },
    async addTodo() {
      try {
        this.newTodo.user_id = this.userId;
        this.newTodo.start_date = this.date;
        this.newTodo.end_date = this.date;

        // 서버에 새로운 투두를 생성하고, 생성된 투두의 ID를 응답받음
        const response = await axios.post('/api/todo/insert', this.newTodo);
        const createdTodoId = response.data; // 서버에서 새로 생성된 todoId를 응답받음

        alert('Todo created successfully!');

        // 투두 리스트를 다시 가져옴
        await this.getTodos();

        // 새로 추가된 투두를 todoId로 선택
        const newlyAddedTodo = this.todos.find((todo) => todo.todo_id === createdTodoId);
        this.selectTodo(newlyAddedTodo);

        // input 필드 초기화
        this.newTodo.title = '';
        this.newTodo.memo = '';
      } catch (error) {
        console.error('Error creating todo:', error);
        alert('할일을 생성하는 중 오류가 발생했습니다.');
      }
    },
  },
};
</script>

<style scoped>
.todo-detail-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  border-left: 1px solid #ccc;
  padding: 20px;
}

.no-selection {
  text-align: center;
  color: #888;
}

.no-selection img {
  max-width: 150px;
  margin-bottom: 20px;
}

.no-selection p {
  font-size: 16px;
  font-weight: bold;
}
</style>
