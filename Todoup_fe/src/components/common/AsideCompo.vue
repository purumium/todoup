<template>
  <div>
    <div class="profile-section">
      <div>
        <img src="@/assets/profile.png" alt="Profile" />
      </div>
      <div class="profile-bottom">
        <div class="my-name">
          <div>김투두</div>
          <div>todolist@gmail.com</div>
        </div>
        <div class="my-follow-btn">
          <div>팔로잉</div>
          <div>팔로워</div>
          <div>친구찾기</div>
        </div>
      </div>
    </div>
    <div class="todo-section">
      <div>Today's TODO</div>
      <ul class="todo-list">
        <li v-for="(todo, idx) in todoList" v-bind:key="idx">
          <input type="checkbox" v-model="todo.checked" />
          <span v-bind:class="{ 'check-line-through': todo.checked }">
            <!-- 조건부로 클래스(속성) 적용, 특정 조건이 참일 떄만 클래스 요소를 적용-->
            {{ todo.text }}
          </span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AsideCompo',
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
.profile-section {
  margin-bottom: 20px;
  border: 1.2px solid #635e5e21;
  padding: 22px 13px;
  border-radius: 11px;
  display: flex;
  /* flex-direction: column; */
  /* text-align: center; */
}

.profile-section img {
  border: 1px solid #8080803d;
  margin-right: 15px;
  border-radius: 70%;
  width: 130px;
}

.profile-bottom {
  margin-top: 10px;
}

.my-name {
  font-size: 13px;
  color: #5b5b5b;
}

.my-follow-btn {
  display: flex;
  justify-content: space-around;
  margin-top: 18px;
  gap: 10px;
}

.my-follow-btn > div {
  width: 57px;
  height: 53px;
  border-radius: 50%;
  background-color: #f0f0f03b;
  font-size: 10px;
  color: #5b5b5b;
  border: 1px solid #8080803d;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
}

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
  overflow-y: auto; /* 세로 스크롤바 */
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
