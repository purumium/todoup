<template>
  <div class="todo-detail">
    <h3 class="todo-title">{{ todo.title }}</h3>
    <p v-if="todo.memo !== todo.title" class="todo-memo">{{ todo.memo }}</p>
    <div class="todo-meta">
      <span class="todo-status" :class="{ completed: todo.completed }">
        <font-awesome-icon :icon="todo.completed ? 'check-circle' : 'clock'" />
        &nbsp;{{ todo.completed ? ' 완료됨' : ' 미완료' }}
      </span>
      <span class="todo-category d-flex align-items-center">
        <font-awesome-icon icon="tags" /> &nbsp;{{ todo.category }}
      </span>
    </div>
    <div class="todo-dates">
      <span class="todo-date"> <font-awesome-icon icon="calendar-alt" /> 시작 {{ formattedStartDate }} </span>
      <span class="todo-date"> <font-awesome-icon icon="calendar-alt" /> 종료 {{ formattedEndDate }} </span>
    </div>
    <div class="button-group">
      <button type="button" class="icon-button" @click="editTodo">
        <font-awesome-icon icon="edit" />
      </button>
      <button type="button" class="icon-button" @click="deleteTodo">
        <font-awesome-icon icon="trash-alt" />
      </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    todo: {
      type: Object,
      required: false,
    },
  },
  computed: {
    formattedStartDate() {
      return this.formatDate(this.todo.start_date);
    },
    formattedEndDate() {
      return this.formatDate(this.todo.end_date);
    },
  },
  methods: {
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      });
    },
    async deleteTodo() {
      if (!this.todo) return;

      try {
        const isCompleted = this.todo.completed ? 1 : 0;
        await axios.delete(`/api/todo/delete/${this.todo.todo_id}`, {
          params: {
            completed: isCompleted,
            userId: this.todo.user_id,
          },
        });

        alert('할일이 성공적으로 삭제되었습니다.');
        this.$emit('todo-deleted', this.todo.todo_id);
      } catch (error) {
        console.error('Error deleting todo:', error);
        alert('할일 삭제 중 오류가 발생했습니다.');
      }
    },
    editTodo() {
      alert('수정 기능은 구현 중입니다.');
    },
  },
};
</script>

<style scoped>
.todo-detail {
  border-radius: 15px;
  color: #333;
  width: 215px;
  height: 300px;
  overflow-y: auto;
  overflow-x: hidden;
}

.todo-title {
  font-size: 22px;
  color: #000;
  font-weight: bold;
  margin-bottom: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis; /* 제목이 길어질 경우 말줄임표 처리 */
}

.todo-memo {
  margin: 10px 0;
  font-size: 16px;
  color: #555;
  white-space: nowrap;
  overflow: hidden;
}

.todo-meta {
  display: flex;
  gap: 15px;
  margin-bottom: 10px;
}

.todo-status.completed {
  color: #4caf50;
}

.todo-status {
  font-weight: bold;
  display: flex;
  align-items: center;
}

.todo-category {
  background-color: #f0f8ff;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 14px;
  color: #333;
  display: flex;
  align-items: center;
}

.todo-dates {
  font-size: 14px;
  color: #555;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  gap: 10px;
}

.icon-button {
  padding: 8px 12px;
  background-color: transparent;
  color: #4d9de0;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 18px;
}

.icon-button:hover {
  color: #2a6b9d;
}
</style>
