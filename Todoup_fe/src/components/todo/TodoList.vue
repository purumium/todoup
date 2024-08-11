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
      <todo-detail :todo="selectedTodo"></todo-detail>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import TodoDetail from './TodoDetail.vue';

export default {
  components: { TodoDetail },
  data() {
    return {
      date: this.$route.params.date, // URL에서 날짜를 가져와 초기화
      todos: [], // 투두 리스트를 저장할 배열
      selectedTodo: null, // 선택된 투두 항목을 저장할 객체
      newTodo: {
        user_id: 6,
        // 새로 추가할 투두 항목
        title: '',
        memo: '',
        category: '기타',
        start_date: '',
        end_date: '',
      },
    };
  },
  computed: {
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
        const response = await axios.get(`/api/todo/date/${this.date}`);
        this.todos = response.data; // 응답 데이터를 todos 배열에 저장
      } catch (error) {
        console.error('Error getting todos:', error);
      }
    },
    async toggleCompletion(todo) {
      try {
        const newCompletionStatus = todo.completed === 1 ? 0 : 1;

        await axios.post(`/api/todo/completion/${todo.todo_id}`, null, {
          params: {
            completed: newCompletionStatus,
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
    async addTodo() {
      try {
        this.newTodo.start_date = this.date;
        this.newTodo.end_date = this.date;

        await axios.post('/api/todo/insert', this.newTodo);
        alert('Todo created successfully!');
        this.getTodos(); // 새로 추가된 할일 포함하여 리스트 갱신
        this.newTodo.title = '';
      } catch (error) {
        console.error('Error creating todo:', error);
        alert('할일을 생성하는 중 오류가 발생했습니다.');
      }
    },
  },
};
</script>

<style scoped></style>
