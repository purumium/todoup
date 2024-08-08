<template>
  <div class="background">
    <div class="avatar-container">
      <slot name="avatar"></slot>
    </div>
    <div class="guest-book">
      <slot name="guestbook"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BackGround',
};
</script>

<style scoped>
.background {
  position: relative;
  width: 100%;
  height: 100vh;
  background-color: #f0f0f0; /* 실제 배경 스타일 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.avatar-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50%; /* 위쪽 절반을 차지 */
  border: 1px solid red; /* 구분을 위해 테두리 추가 */
}

.guest-book {
  height: 50%; /* 아래쪽 절반을 차지 */
  border: 1px solid green; /* 구분을 위해 테두리 추가 */
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
