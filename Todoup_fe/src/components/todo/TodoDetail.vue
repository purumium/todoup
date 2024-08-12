<template>
  <div v-if="todo">
    <h3>{{ todo.title }}</h3>
    <p>{{ todo.memo }}</p>
    <p>완료 상태: {{ todo.completed ? '완료됨' : '미완료' }}</p>
    <p>카테고리: {{ todo.category }}</p>
    <p>시작 날짜: {{ formattedStartDate }}</p>
    <p>종료 날짜: {{ formattedEndDate }}</p>
    <div class="d-flex justify-content-end">
      <button type="button">수정</button>
      <button type="button" @click="deleteTodo">삭제</button>
    </div>
  </div>
  <div v-else class="no-selection">
    <img src="@/assets/avatar_test.png" alt="No selection" />
    <p>할일을 선택해주세요</p>
  </div>
</template>

<script>
import axios from 'axios';
// import { mapState } from 'vuex';

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
  methods: {
    async deleteTodo() {
      if (!this.todo) return;

      try {
        const isCompleted = this.todo.completed ? 1 : 0;
        // 서버에 삭제 요청
        await axios.delete(`/api/todo/delete/${this.todo.todo_id}`, {
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
  },
};
</script>
