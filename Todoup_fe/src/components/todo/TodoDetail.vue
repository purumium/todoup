<template>
  <div v-if="todo">
    <div v-if="changeContent">
      <h3>{{ todo.title }}</h3>
      <p>{{ todo.memo }}</p>
      <p>완료 상태: {{ todo.completed ? '완료됨' : '미완료' }}</p>

      <p>카테고리: {{ todo.category }}</p>
      <p>시작 날짜: {{ formattedStartDate }}</p>
      <p>종료 날짜: {{ formattedEndDate }}</p>
      <div class="d-flex justify-content-end">
        <button type="button" @click="showContent">수정</button>
        <button type="button" @click="deleteTodo">삭제</button>
      </div>
    </div>
    <div v-else>
      <div class="form-group mb-3">
        <label for="title">제목</label>
        <input type="text" id="title" v-model="title" class="form-control" required />
      </div>
      <div class="form-group mb-3">
        <label for="memo">메모</label>
        <textarea id="memo" v-model="memo" class="form-control"></textarea>
      </div>
      <div class="form-group mb-3">
        <label for="category">카테고리</label>
        <select id="category" v-model="category" class="form-select" required>
          <option v-for="category in categories" :key="category.id" :value="category.name">
            {{ category.name }}
          </option>
        </select>
      </div>
      <div class="form-group mb-3">
        <label for="startDate">시작 날짜</label>
        <input type="date" id="startDate" v-model="start_date" class="form-control" required />
      </div>
      <div class="form-group mb-3">
        <label for="endDate">종료 날짜</label>
        <input type="date" id="endDate" v-model="end_date" class="form-control" />
      </div>
      <div class="d-flex justify-content-end">
        <button type="button" @click="modifyTodo">수정하기</button>
        <button type="button" @click="showContent">취소</button>
      </div>
    </div>
  </div>
  <div v-else class="no-selection">
    <img src="@/assets/avatar_test.png" alt="No selection" />
    <p>할일을 선택해주세요</p>
  </div>
</template>

<script>
import axios from 'axios';
import categories from '@/assets/categories.json';
/* import { mapState } from 'vuex'; */

export default {
  props: {
    todo: {
      type: Object,
      required: false,
    },
  },
  // computed: {
  //   ...mapState('user', {
  //     userId: (state) => state.user_info.userId,
  //   }),
  // },
  data() {
    return {
      todo_id: '',
      user_id: '',
      title: '',
      memo: '',
      category: '',
      start_date: '',
      end_date: '',
      categories: categories,
      changeContent: true,
    };
  },
  methods: {
    showContent() {
      this.changeContent = !this.changeContent;
      if (this.changeContent) {
        this.todo_id = '';
        this.user_id = '';
        this.title = '';
        this.memo = '';
        this.category = '';
        this.start_date = '';
        this.end_date = '';
      } else {
        this.todo_id = this.todo.todo_id;
        this.user_id = this.todo.user_id;
        this.title = this.todo.title;
        this.memo = this.todo.memo;
        this.category = this.todo.category;
        this.start_date = this.todo.start_date;
        this.end_date = this.todo.end_date;
      }
      console.log('S.C: ', this.todo_id, '/', this.user_id);
    },
    async deleteTodo() {
      if (!this.todo) return;

      try {
        const isCompleted = this.todo.completed ? 1 : 0;
        // 서버에 삭제 요청
        await axios.delete(`/api/todo/delete/${this.todo_id}`, {
          params: {
            completed: isCompleted,
            userId: this.todo.user_id,
          },
        });

        alert('할일이 성공적으로 삭제되었습니다.');

        // 상위 컴포넌트에 삭제된 항목을 알림
        this.$emit('todo-deleted', this.todo.todo_id);
      } catch (error) {
        console.error('Error deleting todo:', error);
        alert('할일 삭제 중 오류가 발생했습니다.');
      }
    },

    async modifyTodo() {
      if (!this.todo) return;
      console.log('M.T:', this.todo.todo_id, '/', this.todo.user_id);
      try {
        await axios.patch(`/api/todo/modify/${this.todo.todo_id}`, {
          userId: this.user_id,
          title: this.title,
          memo: this.memo,
          category: this.category,
          start_date: this.start_date,
          end_date: this.end_date,
        });
        alert('수정되었습니다.');
        this.showContent();
      } catch (error) {
        console.log();
      }
    },
  },
};
</script>
