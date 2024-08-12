<template>
  <div class="todo-section">
    <div>Today's TODO</div>
    <ul class="todo-list" v-if="userInfo.userId && todoList.length > 0">
      <li v-for="(todo, idx) in todoList" :key="idx">
        <input type="checkbox" v-model="todo.checked" />
        <span :class="{ 'check-line-through': todo.checked }">
          {{ todo.text }}
        </span>
      </li>
    </ul>

    <ul class="todo-list" v-if="!userInfo.userId || todoList.length == 0">
      <li>조회된 목록이 없습니다.</li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';
import { mapState } from 'vuex';

export default {
  name: 'TodayTodo',
  computed: {
    ...mapState('user', {
      userInfo: 'user_info', // Vuex의 user_info 상태를 userInfo로 매핑
    }),
  },
  data() {
    return {
      todoList: [],
    };
  },
  watch: {
    userInfo: {
      handler(newValue) {
        if (!newValue.userId) {
          this.todayList();
        }
      },
      immediate: true, // 컴포넌트 생성 시에도 watcher를 즉시 호출
    },
  },
  created() {
    this.todayList();
  },
  methods: {
    async todayList() {
      try {
        const response = await axios.get('/api/todo/today', {
          params: { userId: this.userInfo.userId },
        });
        this.todoList = response.data.map((todo) => ({
          todo_id: todo.todo_id,
          text: todo.title, // title을 text로 변환
          checked: todo.completed != 1 ? false : true, // 체크박스 상태를 초기화
        }));
      } catch (error) {
        console.log('에러');
      }
    },
  },
};
</script>

<style scoped>
.todo-section {
  margin-top: 25px;
  border: 1.2px solid #635e5e21;
  border-radius: 11px;
}

.todo-section div {
  border-bottom: 1px solid #8080803d;
  padding: 12px 18px;
  font-size: 13px;
  font-weight: 600;
  color: #635a5a;
}

.todo-list {
  max-height: 175px;
  overflow-y: auto;
  padding: 15px 20px;
}

li {
  list-style-type: none;
  font-size: 13px;
  margin-bottom: 7px;
}

li input[type='checkbox'] {
  margin-right: 5px;
}

.check-line-through {
  text-decoration: line-through;
}
</style>
