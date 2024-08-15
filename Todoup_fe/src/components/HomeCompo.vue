<template>
  <div class="w-100">
    <router-view></router-view>
    <!-- 자식 라우트를 렌더링 : 현재 라우트와 일치하는 컴포넌트를 렌더링 -->
  </div>
</template>

<script>
export default {};
</script>

<style>
.calendar-btn-group {
  display: flex;
  justify-content: end;
  gap: 18px;
  margin-bottom: 30px;
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
