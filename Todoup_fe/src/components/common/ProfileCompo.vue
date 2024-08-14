<template>
  <div class="profile-section">
    <div class="profile-top">
      <div class="profile-image-container">
        <!-- <img src="@/assets/profile.png" alt="Profile" @click="goToRoom" /> -->
        <img v-if="userInfo.userId" :src="`/avatar/${profileImg}`" alt="Profile" @click="goToRoom" />
        <img v-else src="/avatar/defaultAvatar.png" alt="Profile" @click="goToRoom" />
        <div v-if="showMessage" class="message-bubble">{{ pointsUpMessage }}</div>
      </div>
      <div class="profile-details">
        <div class="my-name">
          <div>{{ userInfo?.nickName || '김투두' }}</div>
          <div>{{ userInfo?.email || 'todolist@gmail.com' }}</div>
        </div>
        <!-- 레벨과 포인트 프로그레스바 컨테이너 -->
        <div class="profile-level-progress">
          <div class="level-container">
            <span class="level-icon">🏆</span> LEVEL&nbsp;<span class="level-value">{{ userInfo.lv }}</span>
          </div>
          <div class="progress-bar-container">
            <div class="progress-bar">
              <div class="progress" :style="{ width: progressWidth + '%' }"></div>
            </div>
            <p class="progress-text">
              <b>{{ userInfo.points % 100 }}</b> / 100 points
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="profile-bottom">
      <div class="my-follow-btn">
        <div @click="fetchFollowedUsers(userInfo.userId)">팔로잉</div>
        <div @click="fetchFollowers(userInfo.userId)">팔로워</div>
        <div @click="fetchAllUsers(userInfo.userId)">친구찾기</div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex';

export default {
  name: 'ProfileCompo',
  data() {
    return {
      showMessage: false,
      pointsUpMessage: '',
    };
  },
  computed: {
    ...mapState('user', {
      userInfo: 'user_info', // Vuex의 user_info 상태를 userInfo로 매핑
    }),
    ...mapGetters({
      profileImg: 'user/getProfileImg', // Vuex의 profileImg 상태를 컴포넌트에 매핑
    }),
    progressWidth() {
      return this.userInfo.points % 100;
    },
  },
  created() {
    console.log(this.profileImg);
  },
  methods: {
    ...mapActions('modal', {
      loadFollowedUsers: 'fetchFollowedUsers',
      loadFollowers: 'fetchFollowers',
      loadAllUsers: 'fetchAllUsers',
    }),
    fetchAllUsers(userId) {
      console.log('fetchAllUsers');
      if (userId) {
        console.log('여까지는 왔니');
        this.loadAllUsers(userId);
      } else if (!userId) {
        this.$swal
          .fire({
            text: '로그인이 필요합니다.',
            icon: 'warning',
            confirmButtonText: '확인',
            confirmButtonColor: '#f39c12',
          })
          .then(() => {
            this.$router.push('/login');
          });
      } else {
        console.error('User ID가 유효하지 않습니다.');
      }
      this.loadAllUsers();
    },
    fetchFollowedUsers(userId) {
      console.log('fetchFollowedUsers', userId);
      if (userId) {
        console.log('여까지는 왔니');
        this.loadFollowedUsers(userId); // 변경된 메서드 이름 사용
      } else if (!userId) {
        this.$swal
          .fire({
            text: '로그인이 필요합니다.',
            icon: 'warning',
            confirmButtonText: '확인',
            confirmButtonColor: '#f39c12',
          })
          .then(() => {
            this.$router.push('/login');
          });
      } else {
        console.error('User ID가 유효하지 않습니다.');
      }
    },
    fetchFollowers(userId) {
      console.log('fetchFollowers', userId);
      if (userId) {
        this.loadFollowers(userId); // 팔로워 목록 로드
        console.log(this.loadFollowers);
      } else if (!userId) {
        this.$swal
          .fire({
            text: '로그인이 필요합니다.',
            icon: 'warning',
            confirmButtonText: '확인',
            confirmButtonColor: '#f39c12',
          })
          .then(() => {
            this.$router.push('/login');
          });
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
    showMessageBubble(message) {
      this.pointsUpMessage = message;
      this.showMessage = true;
      setTimeout(() => {
        this.showMessage = false;
      }, 3000);
    },
  },
  watch: {
    'userInfo.points'(newPoints, oldPoints) {
      if (newPoints > oldPoints) {
        this.showMessageBubble('👍');
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
  padding: 26px 24px 24px 16px;
  border-radius: 11px;
  width: 360px;
  height: 270px;
}

/* 프로필 이미지 및 팔로우 버튼들 */
.profile-top {
  display: flex;
  align-items: center;
  margin-left: 9px;
}

.profile-image-container {
  /* position: relative; */
}

.profile-top img {
  /* border: 1px solid #8080803d;
  border-radius: 70%;
  width: 130px;
  margin-right: 20px; */
  border: 1px solid #8080803d;
  border-radius: 70%;
  width: 110px;
  height: 100px;
  margin-right: 20px;
}

.message-bubble {
  position: absolute;
  top: -40px;
  right: 28px;
  background-color: #429f50;
  color: white;
  padding: 10px 15px;
  border-radius: 10px;
  font-size: 12px;
  white-space: nowrap;
  animation: fade-in-out 3s ease forwards;
  z-index: 1;
}

.message-bubble::after {
  content: '';
  position: absolute;
  bottom: -6px; /* 말풍선의 아래쪽에 꼬리를 추가 */
  right: 7px; /* 말풍선의 오른쪽에 꼬리 위치 */
  width: 0;
  height: 0;
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-top: 10px solid #429f50; /* 말풍선 색과 동일한 색상으로 설정 */
}

@keyframes fade-in-out {
  0% {
    opacity: 0;
    transform: translateY(-10px);
  }
  10% {
    opacity: 1;
    transform: translateY(0);
  }
  90% {
    opacity: 1;
    transform: translateY(0);
  }
  100% {
    opacity: 0;
    transform: translateY(-10px);
  }
}

.profile-details {
  flex-grow: 1;
}

.my-name {
  font-size: 14px;
  color: #5b5b5b;
  margin: 12px 0;
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

/* 레벨과 포인트 프로그레스바 */
.profile-level-progress {
  width: 100%;
  text-align: left;
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
