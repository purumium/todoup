<template>
  <div class="todo-section">
    <div>Today's TODO</div>
    <ul class="todo-list">
      <li v-for="(todo, idx) in todoList" :key="idx">
        <input type="checkbox" v-model="todo.checked" />
        <span :class="{ 'check-line-through': todo.checked }">
          {{ todo.text }}
        </span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'TodayTodo',
  data() {
    return {
      todoList: [
        { text: '★ 회원 가입하기 ★', checked: false },
        { text: '로그인 하기', checked: false },
        { text: "Today's TODO 작성하기", checked: false },
        { text: '성장일기 쓰기', checked: false },
        { text: '오늘 하루 잘 살기', checked: false },
        { text: '행복하기', checked: false },
        { text: '로그아웃', checked: false },
      ],
    };
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
