<template>
  <div class="todo-section" v-if="userInfo.userId">
    <div class="d-flex align-items-center justify-content-between">
      <span class="today-todo">
        오늘의 할 일
        <span class="today-date">{{ todayDate }}</span>
      </span>

      <font-awesome-icon @click="goToTodayTodo" :icon="['fas', 'arrow-up-right-from-square']" />
    </div>
    <ul class="todo-list" v-if="todayTodos.length > 0">
      <li v-for="(todo, idx) in todayTodos" :key="idx">
        <input type="checkbox" :checked="todo.completed" @change="toggleCompletion(todo)" />

        <span :class="{ 'check-line-through': todo.completed }">
          {{ todo.title }}
        </span>
      </li>
    </ul>
    <ul class="todo-list rainbow" v-else>
      <li class="todo-empty"><span>조회된 데이터가 없습니다!</span></li>
    </ul>
  </div>
  <div class="todo-section" v-else>
    <div class="d-flex align-items-center justify-content-between">
      <span>Today's TODO</span>
      <font-awesome-icon @click="goToTodayTodo" :icon="['fas', 'arrow-up-right-from-square']" />
    </div>
    <ul class="todo-list rainbow">
      <li class="text-example" v-for="(todo, idx) in todoList" :key="idx">
        <input type="checkbox" v-model="todo.checked" />
        <span :class="{ 'check-line-through': todo.checked }">
          {{ todo.text }}
        </span>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

import { mapState, mapGetters } from 'vuex';

export default {
  name: 'TodayTodo',
  data() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
    const day = String(today.getDate()).padStart(2, '0');

    return {
      todoList: [
        { text: 'TO DO UP 사용해보기!', checked: true },
        { text: 'TO DO 추가!', checked: false },
        { text: 'TO DO 달성하고 레벨업하기!', checked: false },
      ],
      todayDate: `${year}-${month}-${day}`, // "YYYY-MM-DD" 형식의 날짜
    };
  },
  computed: {
    ...mapState('user', {
      userInfo: 'user_info', // Vuex의 user_info 상태를 userInfo로 매핑
    }),
    ...mapGetters('todo', {
      todayTodos: 'todayTodos', // Vuex의 todayTodos getter를 todayTodos로 매핑
    }),
  },

  methods: {
    async toggleCompletion(todo) {
      if (!this.userInfo.userId) {
        this.$swal
          .fire({
            text: '로그인이 필요합니다.',
            icon: 'warning',
            confirmButtonText: '확인',
            confirmButtonColor: '#f39c12',
          })
          .then(() => {
            this.$router.push('/login');
          });
        return;
      }

      try {
        const userId = todo.user_id;
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
        this.$store.commit('todo/TODO_COMPLETION', todo.todo_id, !todo.completed);

        this.$store.commit('todo/TODAY_COMPLETION', todo.todo_id, !todo.completed);
      } catch (error) {
        console.error('Error toggling completion status:', error);
        alert('할일 상태를 업데이트하는 중 오류가 발생했습니다.');
      }
    },

    goToTodayTodo() {
      const kor_time = new Date();
      const todayDate =
        kor_time.getFullYear() +
        '-' +
        (kor_time.getMonth() + 1 < 10 ? '0' + (kor_time.getMonth() + 1) : kor_time.getMonth() + 1) +
        '-' +
        kor_time.getDate();
      this.$router.push(`/todo/${todayDate}`);
    },
  },
};
</script>

<style scoped>
.todo-section {
  margin-top: 25px;
  border: 2px solid #635e5e21;
  border-radius: 11px;
}

.todo-section div {
  border-bottom: 2px solid #635e5e21;
  padding: 12px 20px;
  font-size: 16px;
  font-weight: 600;
  color: #635a5a;
}

.todo-list {
  font-weight: 500;
  max-height: 163px;
  height: 163px;
  overflow-y: auto;
  padding: 15px 25px 13px 24px;
}

.todo-empty {
  width: 230px;
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.text-rainbow {
  background-image: linear-gradient(90deg, red, orange, yellow, green, blue, navy, purple);
  -webkit-background-clip: text;
  color: #635a5a; /*무지개가 그립다면 transparent로 바꿔주세요. */
  font-weight: bold;
}
.text-example {
  list-style-type: none;
  font-size: 15px;
  margin-bottom: 8px;
}

.today-date {
  border: 1px solid #cfb50952;
  background-color: #fddd0a29;
  border-radius: 20px;
  padding: 2px 6px;
  font-size: 11px;
  margin-left: 6px;
}

li {
  list-style-type: none;
  font-size: 15px;
  margin-bottom: 8px;
}

li input[type='checkbox'] {
  margin-right: 10px;
}

.check-line-through {
  text-decoration: line-through;
}
</style>
