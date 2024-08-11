<template>
  <div class="container mt-5">
    <h4 class="mb-4">Todo 추가</h4>
    <form @submit.prevent="submitTodo" class="w-100">
      <div class="form-group mb-3">
        <label for="title">제목</label>
        <input type="text" id="title" v-model="todo.title" class="form-control" required />
      </div>
      <div class="form-group mb-3">
        <label for="memo">메모</label>
        <textarea id="memo" v-model="todo.memo" class="form-control"></textarea>
      </div>
      <div class="form-group mb-3">
        <label for="category">카테고리</label>
        <select id="category" v-model="todo.category" class="form-select" required>
          <option v-for="category in categories" :key="category.id" :value="category.name">
            {{ category.name }}
          </option>
        </select>
      </div>
      <div class="form-group mb-3">
        <label for="startDate">시작 날짜</label>
        <input type="date" id="startDate" v-model="todo.start_date" class="form-control" required />
      </div>
      <div class="form-group mb-3">
        <label for="endDate">종료 날짜</label>
        <input type="date" id="endDate" v-model="todo.end_date" class="form-control" />
      </div>
      <div class="d-flex justify-content-end">
        <button type="submit" class="px-3 py-2">Create</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import categories from '@/assets/categories.json';

export default {
  data() {
    return {
      todo: {
        user_id: 6,
        title: '',
        memo: '',
        category: '',
        start_date: '',
        end_date: '',
      },
      categories: categories,
    };
  },
  created() {
    this.setDefaultDates();
    this.loadCategories();
  },
  methods: {
    setDefaultDates() {
      const today = new Date().toISOString().split('T')[0]; // YYYY-MM-DD 형식으로 현재 날짜 가져오기
      this.todo.start_date = today;
      this.todo.end_date = today;
    },
    async loadCategories() {
      try {
        // JSON 파일로부터 카테고리 데이터를 가져옴
        const response = categories;
        this.categories = response;
        // 카테고리 데이터가 로드된 후 기본값 설정
        if (!this.categories.find((cat) => cat.name === this.todo.category)) {
          this.todo.category = this.categories.length > 0 ? this.categories[0].name : '';
        }
      } catch (error) {
        console.error('Error loading categories:', error);
      }
    },
    async submitTodo() {
      try {
        if (!this.todo.end_date) {
          this.todo.end_date = this.todo.start_date;
        }
        console.log(this.todo.title, this.todo.memo, this.todo.category, this.todo.start_date, this.todo.end_date);

        await axios.post('/api/todo/insert', this.todo);
        alert('Todo created successfully!');
        this.todo = { user_id: 6, title: '', memo: '', category: '', start_date: '', end_date: '' };
        this.setDefaultDates(); // 새로운 TODO를 생성한 후에도 디폴트 날짜를 설정
        this.$router.push('/');
      } catch (error) {
        console.error('Error creating todo:', error);
        alert('There was an error creating the todo.');
      }
    },
  },
};
</script>

<style scoped></style>
