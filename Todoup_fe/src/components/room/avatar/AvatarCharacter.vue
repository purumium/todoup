<!-- MyAvatar.vue -->
<template>
  <div class="avatar-section">
    <h2>{{ characterId }}'s Avatar</h2>
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
  border: 1px solid black;
  display: flex;
  flex-direction: column;
}
</style>
