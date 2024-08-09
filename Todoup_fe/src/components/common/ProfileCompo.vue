<template>
  <div class="profile-section">
    <div>
      <img src="@/assets/profile.png" alt="Profile" @click="goToRoom" />
    </div>
    <div class="profile-bottom">
      <div class="my-name">
        <div>{{ userInfo?.nickName || '김투두' }}</div>
        <div>{{ userInfo?.email || 'todolist@gmail.com' }}</div>
      </div>
      <div class="my-follow-btn">
        <div @click="fetchFollowedUsers(userInfo.userId)">팔로잉</div>
        <div @click="showUserId">팔로워</div>
        <div>친구찾기</div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';

export default {
  name: 'ProfileCompo',
  computed: {
    ...mapState('user', {
      userInfo: 'user_info', // Vuex의 user_info 상태를 userInfo로 매핑
    }),
  },
  methods: {
    ...mapActions({
      loadFollowedUsers: 'fetchFollowedUsers',
    }),
    fetchFollowedUsers(userId) {
      if (userId) {
        this.loadFollowedUsers(userId); // 변경된 메서드 이름 사용
      } else {
        console.error('User ID가 유효하지 않습니다.');
      }
    },
    goToRoom() {
      console.log(this.userInfo.userId);
      if (this.userInfo && this.userInfo.userId) {
        this.$router.push(`/room/${this.userInfo.userId}/avatarroom`);
      } else {
        console.error('User ID is not available.');
      }
    },
    showUserId() {
      if (this.userInfo && this.userInfo.userId) {
        console.log('userInfo-vuex:', this.userInfo);
      } else {
        console.log('userInfo가 정의되지 않았습니다.');
      }
    },
  },
};
</script>

<style scoped>
.profile-section {
  margin-bottom: 20px;
  border: 1.2px solid #635e5e21;
  padding: 22px 13px;
  border-radius: 11px;
  display: flex;
}

.profile-section img {
  border: 1px solid #8080803d;
  margin-right: 15px;
  border-radius: 70%;
  width: 130px;
}

.profile-bottom {
  margin-top: 10px;
}

.my-name {
  font-size: 13px;
  color: #5b5b5b;
}

.my-follow-btn {
  display: flex;
  justify-content: space-around;
  margin-top: 18px;
  gap: 10px;
}

.my-follow-btn > div {
  width: 57px;
  height: 53px;
  border-radius: 50%;
  background-color: #f0f0f03b;
  font-size: 10px;
  color: #5b5b5b;
  border: 1px solid #8080803d;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
