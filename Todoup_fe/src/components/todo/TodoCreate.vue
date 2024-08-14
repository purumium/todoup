<template>
  <div class="container">
    <h4 class="todo-add-title">Todo 추가</h4>
    <div class="form-container">
      <form @submit.prevent="submitTodo">
        <div class="d-flex justify-content-end create-button">
          <button type="submit" class="create-btn"><font-awesome-icon :icon="['fas', 'check-double']" /> CREATE</button>
        </div>
        <div class="form-group mbb-3">
          <label for="title">제목</label>
          <input type="text" id="title" v-model="todo.title" class="form-control" required />
        </div>
        <div class="form-group mbb-3">
          <label for="memo">메모</label>
          <textarea id="memo" v-model="todo.memo" class="form-control"></textarea>
        </div>
        <div class="form-group mbb-3">
          <label for="category">카테고리</label>
          <select id="category" v-model="todo.category" class="form-select" required>
            <option v-for="category in categories" :key="category.id" :value="category.name">
              {{ category.name }}
            </option>
          </select>
        </div>
        <div class="form-group mbb-3">
          <label for="startDate">시작 날짜</label>
          <input type="date" id="startDate" v-model="todo.start_date" class="form-control" required />
        </div>
        <div class="form-group mbb-3">
          <label for="endDate">종료 날짜</label>
          <input type="date" id="endDate" v-model="todo.end_date" class="form-control" />
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import categories from '@/assets/categories.json';
import { mapState } from 'vuex';

export default {
  data() {
    return {
      todo: {
        user_id: '',
        title: '',
        memo: '',
        category: '',
        start_date: '',
        end_date: '',
      },
      categories: categories,
    };
  },
  computed: {
    ...mapState('user', {
      userId: (state) => state.user_info.userId,
    }),
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
        this.todo.user_id = this.userId;
        if (!this.todo.end_date) {
          this.todo.end_date = this.todo.start_date;
        }
        await axios.post('/api/todo/insert', this.todo);
        this.$swal.fire({
          text: '새로운 TODO가 생성되었습니다.',
          icon: 'success',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
        this.todo = { user_id: this.userId, title: '', memo: '', category: '', start_date: '', end_date: '' };
        this.setDefaultDates(); // 새로운 TODO를 생성한 후에도 디폴트 날짜를 설정
        this.$router.push('/');
      } catch (error) {
        console.error('Error creating todo:', error);
        this.$swal.fire({
          text: '새로운 TODO 생성에 실패하였습니다.',
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
.form-container {
  border: 2px solid #8080802e;
  padding: 6px 30px 20px;
  border-radius: 10px;
}

.todo-add-title {
  margin-bottom: 20px;
  color: #2b2222b8 !important;
  font-weight: 600;
  font-size: 22px;
  padding: 10px 0;
  text-align: center;
  border-bottom: 2px solid #cfcece70;
  border-top: 2px solid #cfcece70;
}

.form-control {
  border-radius: 20px;
  display: block;
  width: 100%;
  padding: 8px 21px;
  font-size: 15px;
  font-weight: 400;
  line-height: 1.6;
  color: rgb(36, 34, 34);
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  background-color: var(--bs-body-bg);
  background-clip: padding-box;
  border: var(--bs-border-width) solid var(--bs-border-color);
  transition:
    border-color 0.15s ease-in-out,
    box-shadow 0.15s ease-in-out;
}

.mbb-3 {
  margin-bottom: 0.8rem !important;
}

.create-button {
  position: relative;
}

.create-btn {
  width: 113px;
  height: 38px;
  padding: 4px;
  background-color: #c1b4b41f;
  color: #544545;
  border: 2px solid #1d13132e;
  font-weight: 600;
  font-size: 13px;
  border-radius: 20px;
  position: relative;
  top: 17px;
  left: -4px;
}

.form-select {
  width: 100%;
  padding: 5px 21px;
  font-size: 1rem;
  font-weight: 400;
  line-height: 1.5;
  color: var(--bs-body-color);
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  background-color: var(--bs-body-bg);
  background-image: var(--bs-form-select-bg-img), var(--bs-form-select-bg-icon, none);
  background-repeat: no-repeat;
  background-position: right 0.75rem center;
  background-size: 16px 12px;
  border-radius: 20px;
  border: var(--bs-border-width) solid var(--bs-border-color);
  transition:
    border-color 0.15s ease-in-out,
    box-shadow 0.15s ease-in-out;
}

label {
  padding: 5px 6px 10px 5px;
  font-weight: 600;
  font-size: 14px;
  color: #2b2222b8 !important;
}

.button-group {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
}
</style>
