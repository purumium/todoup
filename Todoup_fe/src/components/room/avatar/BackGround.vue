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
  height: 515px;
  background-image: url('@/assets/supermario_background.jpg');
  background-size: cover;
  background-position: center;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.avatar-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50%; /* 위쪽 절반을 차지 */
}

.guest-book {
  height: 50%; /* 아래쪽 절반을 차지 */
  display: flex;
  justify-content: end;
  align-items: center;
}
</style>
