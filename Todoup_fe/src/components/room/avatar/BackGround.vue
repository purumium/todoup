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
  height: 520px;
  /* background-image: url(http://localhost:8000/img/supermario_background.c3ac70be.jpg); */
  background-image: url('@/assets/avatar_background.png');
  background-size: cover;
  background-position: center;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin-top: 16px;
}

.avatar-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50%; /* 위쪽 절반을 차지 */
}

.guest-book {
  height: 40%;
  display: flex;
  overflow: hidden;
  justify-content: end;
  align-items: center;
}
</style>
