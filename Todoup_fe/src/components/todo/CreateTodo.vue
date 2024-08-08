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
        <input type="date" id="startDate" v-model="todo.startDate" class="form-control" required />
      </div>
      <div class="form-group mb-3">
        <label for="endDate">종료 날짜</label>
        <input type="date" id="endDate" v-model="todo.endDate" class="form-control" />
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
        userId: 6,
        title: '',
        memo: '',
        category: '',
        startDate: '',
        endDate: '',
      },
      categories: categories,
    };
  },
  created() {
    this.loadCategories();
  },
  methods: {
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
        if (!this.todo.endDate) {
          this.todo.endDate = this.todo.startDate;
        }
        console.log(this.todo.title, this.todo.memo, this.todo.category, this.todo.startDate, this.todo.endDate);
        await axios.post('/api/todo/insert', this.todo);
        alert('Todo created successfully!');
        this.todo = { userId: 6, title: '', memo: '', category: '', startDate: '', endDate: '' };
      } catch (error) {
        console.error('Error creating todo:', error);
        alert('There was an error creating the todo.');
      }
    },
  },
};
</script>

<style scoped></style>
