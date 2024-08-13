<template>
  <div class="profile-section">
    <div class="profile-top">
      <img src="@/assets/profile.png" alt="Profile" @click="goToRoom" />
      <div class="my-name">
        <div>{{ userInfo?.nickName || 'KimTodoUp' }}</div>
        <div>{{ userInfo?.email || 'todoup-kim@gmail.com' }}</div>
      </div>
    </div>
    <div class="profile-bottom">
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
  display: flex;
  flex-direction: column;
  margin-bottom: 40px;
  border: 2px solid #635e5e21;
  padding: 30px 20px 25px;
  border-radius: 11px;
  width: 340px;
  height: 270px;
}

.profile-top {
  display: flex;
  align-items: center;
  margin-left: 9px;
}

.profile-top img {
  border: 1px solid #8080803d;
  border-radius: 70%;
  width: 120px;
  margin-right: 15px;
}

.my-name {
  font-size: 14px;
  color: #5b5b5b;
}

.my-name :nth-of-type(1) {
  font-size: 20px;
  font-weight: 600;
}

.my-follow-btn {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

.my-follow-btn > div {
  width: 73px;
  height: 67px;
  border-radius: 50%;
  background-color: #f0f0f08c;
  font-size: 12px;
  color: #5b5b5b;
  border: 2px solid #635e5e21;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
