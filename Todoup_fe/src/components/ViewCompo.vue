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
  justify-content: flex-start;
  gap: 10px;
  margin-bottom: 20px;
}
</style>
