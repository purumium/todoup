<template>
  <div class="avatar-section">
    <p class="avatar-title">{{ nickName }} 님</p>
    <div class="avatar-img">
      <img :src="profileImg" alt="Avatar" />
    </div>
    <div class="avatar-info">
      <div class="profile-level-progress">
        <div class="level-container">
          <span class="level-icon">🏆</span> LEVEL&nbsp;<span class="level-value">{{ lv }}</span>
        </div>
        <div class="progress-bar-container">
          <div class="progress-bar">
            <div class="progress" :style="{ width: progressWidth + '%' }"></div>
          </div>
          <p class="progress-text">
            <b>{{ points % 100 }}</b> / 100 points
          </p>
        </div>
      </div>
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
/* AvatarCharacter 스타일 */
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

/* 프로필 레벨과 포인트 프로그레스바 스타일 */
.profile-level-progress {
  width: 92%;
  margin: 18px;
}

.level-container {
  display: flex;
  align-items: center;
  font-size: 12px;
  font-weight: 600;
  color: #5b5b5b;
  margin-bottom: 2px;
}

.level-icon {
  margin-right: 5px;
}

.level-value {
  font-weight: bold;
  color: #5b5b5b;
}

.progress-bar-container {
  width: 100%;
}

.progress-bar {
  height: 12px;
  background-color: #e0e0e0;
  border-radius: 10px;
  overflow: hidden;
}

.progress {
  height: 100%;
  background-color: rgb(66, 147, 66);
  transition: width 0.5s ease-in-out;
}

.progress-text {
  text-align: right;
  font-size: 11px;
  color: #555;
  margin-top: 5px;
  margin-bottom: 0;
}
</style>
