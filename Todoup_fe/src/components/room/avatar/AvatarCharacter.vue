<!-- MyAvatar.vue -->
<template>
  <div class="avatar-section">
    <p class="avatar-title">{{ characterId }}'s Avatar</p>
    <!-- 아바타 내용 -->
    <div class="avatar-img">
      <img src="@/assets/avatar_test.png" alt="Avatar" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'AvatarCharacter',
  props: {
    characterId: {
      type: String,
      required: true,
    },
  },
};
</script>

<style scoped>
/* 스타일 정의 */
.avatar-section {
  display: flex;
  flex-direction: column;
  margin-top: 3rem;
}
.avatar-title {
  font-size: 20px;
  margin-bottom: 0;
}
.avatar-img > img {
  height: 210px;
}
</style>
