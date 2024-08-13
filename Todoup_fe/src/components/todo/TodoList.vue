<template>
  <div>
    <h4 class="header">{{ formattedDate }} TODO</h4>
    <div class="todo-container">
      <div class="todo-list">
        <form @submit.prevent="addTodo" class="add-todo-form">
          <input class="add-input" v-model="newTodo.title" type="text" placeholder="할 일을 작성해주세요" required />
          <button type="submit" class="add-todo-button">추가</button>
        </form>
        <ul class="todo-ul">
          <li v-for="todo in todos" :key="todo.todoId" @click="selectTodo(todo)" class="todo-item">
            <input type="checkbox" :checked="todo.completed" @change="toggleCompletion(todo)" class="todo-checkbox" />
            <p class="todo-title mb-0" :class="{ completed: todo.completed }">
              {{ todo.title }}
            </p>
          </li>
        </ul>
      </div>
      <div class="todo-detail-container">
        <todo-detail v-if="selectedTodo" :todo="selectedTodo" @todo-deleted="handleTodoDeleted"></todo-detail>
        <div v-else class="no-selection">
          <img src="@/assets/avatar_test.png" alt="No selection" />
          <p>할일을 선택해주세요</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import TodoDetail from './TodoDetail.vue';
import { mapState } from 'vuex';

export default {
  components: { TodoDetail },
  data() {
    return {
      date: this.$route.params.date,
      todos: [],
      selectedTodo: null,
      newTodo: {
        user_id: 6,
        title: '',
        memo: '',
        category: '기타',
        start_date: '',
        end_date: '',
      },
    };
  },
  computed: {
    ...mapState('user', {
      userId: (state) => state.user_info.userId,
      points: (state) => state.user_info.points,
    }),
    formattedDate() {
      const date = new Date(this.date);
      return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      });
    },
  },
  created() {
    this.getTodos();
  },
  watch: {
    '$route.params.date'(newDate) {
      this.date = newDate;
      this.getTodos();
    },
  },

  methods: {
    async getTodos() {
      try {
        const userId = this.userId;
        const response = await axios.get(`/api/todo/date/${this.date}`, {
          params: { userId },
        });
        this.todos = response.data;
        const selectedTodoId = this.$route.query.selectedTodoId;
        if (selectedTodoId) {
          this.selectedTodo = this.todos.find((todo) => todo.todo_id === parseInt(selectedTodoId));
        }
      } catch (error) {
        console.error('Error getting todos:', error);
      }
    },
    async toggleCompletion(todo) {
      try {
        const userId = this.userId;
        const newCompletionStatus = todo.completed ? 0 : 1;
        await axios.post(`/api/todo/completion/${todo.todo_id}`, null, {
          params: {
            completed: newCompletionStatus,
            userId,
          },
        });
        todo.completed = newCompletionStatus;
        // 포인트를 update하는 로직 추가
        const pointsToAdd = newCompletionStatus ? 5 : -5; // 완료시 +5 포인트, 취소시 -5 포인트
        //const newPoints = this.points + pointsToAdd;
        this.$store.dispatch('user/updatePoints', pointsToAdd);

        this.$swal.fire({
          text: 'TODO의 완료 상태가 변경되었습니다.',
          icon: 'success',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
      } catch (error) {
        console.error('Error toggling completion status:', error);
        this.$swal.fire({
          text: 'TODO의 완료 상태가 변경에 실패하었습니다.',
          icon: 'error',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
      }
    },
    selectTodo(todo) {
      this.selectedTodo = todo;
    },
    handleTodoDeleted(deletedTodoId) {
      this.todos = this.todos.filter((todo) => todo.todo_id !== deletedTodoId);
      this.selectedTodo = null;
    },
    async addTodo() {
      try {
        this.newTodo.user_id = this.userId;
        this.newTodo.start_date = this.date;
        this.newTodo.end_date = this.date;
        const response = await axios.post('/api/todo/insert', this.newTodo);
        const createdTodoId = response.data;
        await this.getTodos();
        const newlyAddedTodo = this.todos.find((todo) => todo.todo_id === createdTodoId);
        this.selectTodo(newlyAddedTodo);
        this.newTodo.title = '';
        this.newTodo.memo = '';
      } catch (error) {
        console.error('Error creating todo:', error);
        this.$swal.fire({
          text: '새로운 TODO 생성에 실패하었습니다.',
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
/* 전체 레이아웃 및 컨테이너 스타일 */
.todo-container {
  height: 525px;
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  gap: 20px;
  background-color: #e8f1f2;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

/* 투두 리스트 스타일 */
.todo-list {
  overflow-x: none;
  overflow-y: auto;
  flex: 1;
  background-color: #ffffff;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.add-todo-form {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.add-input {
  margin-right: 2px;
  flex: 1;
  padding: 10px 12px;
  border-radius: 25px;
  border: 2px solid #4d9de0;
  background-color: #f0f8ff;
}

.add-todo-button {
  white-space: nowrap;
  padding: 10px 18px;
  background-color: #4d9de0; /* 기존 테마의 파란색 계열로 변경 */
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-weight: bold;
  color: #fff; /* 텍스트 색상을 흰색으로 설정 */
}

.add-todo-button:hover {
  background-color: #3c7bb0; /* 호버 시 조금 더 진한 파란색으로 */
}

/* 투두 항목 스타일 */
.todo-ul {
  list-style: none;
  padding-left: 0;
  margin-top: 0;
}

.todo-item {
  padding: 10px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.todo-item:hover {
  background-color: #c5e0f6;
}

.todo-item:last-child {
  border-bottom: none;
}

.todo-checkbox {
  margin-right: 10px;
  transform: scale(1.2);
  cursor: pointer;
}

.todo-title {
  font-size: 16px;
  cursor: pointer;
  color: #333;
}

.todo-title.completed {
  text-decoration: line-through;
  color: #888;
}

/* 상세보기 컨테이너 스타일 */
.todo-detail-container {
  width: 267px;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ffffff;
  border-radius: 15px;
  padding: 0 10px;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.no-selection {
  text-align: center;
  color: #888;
}

.no-selection img {
  max-width: 200px;
  margin-bottom: 20px;
}

.no-selection p {
  font-size: 18px;
  font-weight: bold;
}
</style>
