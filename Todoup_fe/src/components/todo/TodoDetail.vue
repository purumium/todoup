<template>
  <div v-if="todo">
    <div v-if="changeContent">
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
          <button type="button" class="icon-button" @click="showContent">
            <font-awesome-icon icon="edit" />
          </button>
          <button type="button" class="icon-button" @click="deleteTodo">
            <font-awesome-icon icon="trash-alt" />
          </button>
        </div>
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

        console.log('detail: ', this.todo.todo_id);
        console.log('Before committing to Vuex - Vuex State:', this.$store.state.todo_info);
        this.$store.commit('todo/REMOVE_TODO', this.todo.todo_id);
        console.log('After committing to Vuex - Vuex State:', this.$store.state.todo_info);

        alert('할일이 성공적으로 삭제되었습니다.');
        this.$emit('todo-deleted', this.todo.todo_id);
      } catch (error) {
        console.error('Error deleting todo:', error);
        alert('할일 삭제 중 오류가 발생했습니다.');
      }
    },
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

<style scoped>
.todo-detail {
  border-radius: 15px;
  padding: 20px;
  color: #333;
  width: 250px;
  height: 300px;
  overflow-y: auto;
  overflow-x: hidden;
  background-color: #f9f9f9;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
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
