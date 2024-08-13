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

    <ul class="todo-list rainbow" v-if="!userInfo.userId || todoList.length == 0">
      <li class="todo-empty"><span class="text-rainbow">조회된 데이터가 없습니다.!</span></li>
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
  color: transparent;

  font-weight: bold;
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
