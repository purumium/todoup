<template>
  <div v-if="todo">
    <div v-if="changeContent">
      <div class="todo-detail">
        <div>
          <h3 class="todo-title">{{ todo.title }}</h3>
          <div class="todo-meta">
            <span class="todo-status" :class="{ completed: todo.completed }">
              <font-awesome-icon :icon="todo.completed ? 'check-circle' : 'clock'" />
              &nbsp;{{ todo.completed ? ' 완료' : ' 미완료' }}
            </span>
            <span class="todo-category d-flex align-items-center">
              <font-awesome-icon icon="tags" /> &nbsp;{{ todo.category }}
            </span>
          </div>
          <div class="todo-dates">
            <span class="todo-date">
              <font-awesome-icon icon="calendar-alt" /> 시작날짜 &nbsp; {{ formattedStartDate }}
            </span>
            <span class="todo-date">
              <font-awesome-icon icon="calendar-alt" /> 종료날짜 &nbsp; {{ formattedEndDate }}
            </span>
          </div>
          <p v-if="todo.memo !== todo.title" class="todo-memo">{{ todo.memo }}</p>
        </div>
        <div>
          <div class="button-group">
            <button type="button" class="icon-button" @click="showContent">
              <font-awesome-icon icon="edit" /> 수정
            </button>
            <button type="button" class="icon-button" @click="deleteTodo">
              <font-awesome-icon icon="trash-alt" /> 삭제
            </button>
          </div>
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
        <label for="startDate">시작날짜</label>
        <input type="date" id="startDate" v-model="start_date" class="form-control" required />
      </div>
      <div class="form-group mb-3">
        <label for="endDate">종료날짜</label>
        <input type="date" id="endDate" v-model="end_date" class="form-control" />
      </div>
      <div class="button-group">
        <button type="button" class="icon-button" @click="modifyTodo"><font-awesome-icon icon="save" /> 저장</button>
        <button type="button" class="icon-button" @click="showContent"><font-awesome-icon icon="times" /> 취소</button>
      </div>
    </div>
  </div>
  <div v-else class="no-selection">
    <img src="@/assets/avatar_test.png" alt="No selection" />
    <p>할 일을 선택해주세요</p>
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
    formatModDate(dateString) {
      return dateString ? dateString.split(' ')[0] : ''; // 날짜만 추출
    },
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
        // 첫 번째 알림: 삭제 확인 요청
        const result = await this.$swal.fire({
          text: 'TODO를 삭제하시겠습니까?',
          icon: 'question',
          showCancelButton: true,
          confirmButtonText: '확인',
          confirmButtonColor: '#e74c3c',
          cancelButtonText: '취소',
        });

        // 사용자가 '확인' 버튼을 클릭했을 때만 삭제 진행
        if (result.isConfirmed) {
          const isCompleted = this.todo.completed ? 1 : 0;
          await axios.delete(`/api/todo/delete/${this.todo.todo_id}`, {
            params: {
              completed: isCompleted,
              userId: this.todo.user_id,
            },
          });

          // 삭제 완료 알림
          await this.$swal.fire({
            text: 'TODO가 삭제되었습니다.',
            icon: 'success',
            confirmButtonText: '확인',
            confirmButtonColor: '#429f50',
          });
          this.$store.commit('todo/REMOVE_TODO', this.todo.todo_id);
          // 삭제 완료 후 이벤트 발생
          this.$emit('todo-deleted', this.todo.todo_id);
        }

      } catch (error) {
        console.error('Error deleting todo:', error);
        this.$swal.fire({
          text: '할일 삭제 중 오류가 발생했습니다.',
          icon: 'error',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
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
        this.start_date = this.formatModDate(this.todo.start_date);
        this.end_date = this.formatModDate(this.todo.end_date);
      }
      console.log('S.C: ', this.todo_id, '/', this.user_id, this.start_date, this.end_date);
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
        this.$swal.fire({
          text: 'TODO가 수정되었습니다.',
          icon: 'success',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
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
  color: #333;
  width: 267px;
  height: 450px;
  padding: 15px;
  box-sizing: border-box;
  overflow-y: auto;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-sizing: border-box;
}

.todo-title {
  font-size: 22px;
  color: #333;
  font-weight: bold;
  margin-bottom: 20px;
  white-space: normal;
  word-wrap: break-word;
}

.todo-memo {
  margin-top: 20px;
  padding: 0 0 16px;
  font-size: 16px;
  color: #555;
  white-space: normal;
  word-wrap: break-word;
}

.todo-meta {
  display: flex;
  gap: 14px;
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
  padding: 4px 10px;
  border-radius: 10px;
  font-size: 14px;
  color: #333;
  display: flex;
  align-items: center;
  font-weight: 500;
}

.todo-dates {
  font-size: 16px;
  color: #555;
  display: flex;
  flex-direction: column;
  gap: 12px;
  font-weight: 500;
  margin-top: 30px;
}

.button-group {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin-top: 20px;
}

.icon-button {
  width: auto;
  padding: 8px 12px;
  background-color: transparent;
  color: #4d9de0;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
}

.icon-button:hover {
  color: #2a6b9d;
}

.form-group > label {
  margin-bottom: 2px;
}
</style>
