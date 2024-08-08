<template>
  <div class="w-100">
    <div class="calendar-btn-group">
      <router-link to="/todo">
        <button>TODO</button>
      </router-link>
      <router-link to="/diary">
        <button>DIARY</button>
      </router-link>
    </div>
    <router-view></router-view>
    <!-- 자식 라우트를 렌더링 : 현재 라우트와 일치하는 컴포넌트를 렌더링 -->
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
.calendar-btn-group {
  display: flex;
  justify-content: end;
  gap: 18px;
  margin-bottom: 20px;
}
div button {
  width: 90px;
  height: 35px;
  background-color: #e5e5e552;
  color: #544545;
  border: 1.5px solid #544545;
  font-weight: 600;
  font-size: 15px;
}
</style>
