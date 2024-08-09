<template>
  <div>
    <h4>{{ formattedDate }}</h4>
    <div class="d-flex">
      <div class="todo-list border">
        <div>TodoList</div>
        <ul>
          <li v-for="todo in todos" :key="todo.todoId" class="d-flex align-items-center">
            <input type="checkbox" :checked="todo.completed" />
            <p class="align-middle" @click="this.$router.push(`/todo/${todo.todoId}`)">{{ todo.title }}</p>
            <p>{{ todo.todoId }}</p>
          </li>
        </ul>
      </div>
      <todo-detail></todo-detail>
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
        console.log(this.todos);
      } catch (error) {
        console.error('Error getting todos:', error);
      }
    },
  },
};
</script>

<style scoped></style>
