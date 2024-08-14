<template>
  <div class="todo-section" v-if="userInfo.userId">
    <div class="d-flex align-items-center justify-content-between">
      <span>Today's TODO</span>
      <font-awesome-icon @click="goToTodayTodo" :icon="['fas', 'arrow-up-right-from-square']" />
    </div>
    <ul class="todo-list" v-if="todoInfo.length > 0">
      <li v-for="(todo, idx) in todayTodos" :key="idx">
        <input type="checkbox" :checked="todo.completed" @change="toggleCompletion(todo)" />

        <span :class="{ 'check-line-through': todo.completed }">
          {{ todo.title }}
        </span>
      </li>
    </ul>
    <ul class="todo-list rainbow" v-else>
      <li class="todo-empty"><span class="text-rainbow">조회된 데이터가 없습니다.!</span></li>
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
    return {
      todoList: [
        { text: '오늘 하루 잘 살기', checked: true },
        { text: '★ 회원 가입하기 ★', checked: false },
        { text: '로그인 하기', checked: false },
        { text: "Today's TODO 작성하기", checked: true },
        { text: '성장일기 쓰기', checked: false },
        { text: '행복하기', checked: true },
      ],
    };
  },
  computed: {
    ...mapState('user', {
      userInfo: 'user_info', // Vuex의 user_info 상태를 userInfo로 매핑
    }),
    ...mapState('todo', {
      todoInfo: 'todo_info',
      todayInfo: 'today_info', // Vuex의 todo_info 상태를 todoInfo로 매핑
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
        this.$swal.fire({
          text: 'TODO의 완료 상태가 변경에 실패하었습니다.',
          icon: 'error',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
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
  padding: 13px 20px;
  font-size: 19px;
  font-weight: 600;
  color: #635a5a;
}

.todo-list {
  font-weight: 500;
  max-height: 190px;
  height: 190px;
  overflow-y: auto;
  padding: 18px 25px;
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

li {
  list-style-type: none;
  font-size: 18px;
  margin-bottom: 8px;
}

li input[type='checkbox'] {
  margin-right: 10px;
}

.check-line-through {
  text-decoration: line-through;
}
</style>
