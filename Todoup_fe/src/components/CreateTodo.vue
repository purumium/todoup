<template>
  <div>
    <h2>Create Todo</h2>
    <form @submit.prevent="submitTodo">
      <div>
        <label for="title">Title:</label>
        <input type="text" id="title" v-model="todo.title" required />
      </div>
      <div>
        <label for="memo">Memo:</label>
        <textarea id="memo" v-model="todo.memo"></textarea>
      </div>
      <div>
        <label for="category">Category:</label>
        <select id="category" v-model="todo.category" required>
          <option v-for="category in categories" :key="category.id" :value="category.name">
            {{ category.name }}
          </option>
        </select>
      </div>
      <button type="submit">Create</button>
    </form>
  </div>
</template>

<script>
import axios from '@/plugins/axios';
import categories from '@/assets/categories.json';

export default {
  data() {
    return {
      todo: {
        userId: '1',
        title: '',
        memo: '',
        category: '',
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
          this.todo.category = this.categories.length > 0 ? this.categories[3].name : '';
        }
      } catch (error) {
        console.error('Error loading categories:', error);
      }
    },
    async submitTodo() {
      try {
        console.log(this.todo.title, this.todo.memo, this.todo.category);
        await axios.post('/api/todo', this.todo);
        alert('Todo created successfully!');
        this.todo = { userId: '1', title: '', memo: '', category: '' }; // form 초기화
      } catch (error) {
        console.error('Error creating todo:', error);
        alert('There was an error creating the todo.');
      }
    },
  },
};
</script>

<style scoped></style>
