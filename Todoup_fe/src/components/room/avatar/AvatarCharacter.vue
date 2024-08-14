<template>
  <div class="avatar-section">
    <p class="avatar-title">{{ nickName }}'s Avatar</p>
    <div class="avatar-img">
      <img :src="profileImg" alt="Avatar" />
    </div>
    <div class="avatar-info">
      <p>Level: {{ lv }}</p>
      <p>Points: {{ points }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AvatarCharacter',
  props: {
    profileImg: {
      type: String,
      required: true,
    },
    lv: {
      type: Number,
      required: true,
    },
    points: {
      type: Number,
      required: true,
    },
    nickName: {
      type: String,
      required: true,
    },
  },
};
</script>

<style scoped>
.avatar-section {
  display: flex;
  flex-direction: column;
  margin-top: 3rem;
  align-items: center;
  text-align: center;
}

.avatar-title {
  font-size: 14px;
  margin-bottom: 0.5rem;
}

.avatar-img > img {
  height: 150px;
  /* width: 210px; 추가 */
  object-fit: cover; /* 이미지 크기 조정 */
}

.avatar-info {
  margin-top: 1rem;
  font-size: 16px;
}
</style>
