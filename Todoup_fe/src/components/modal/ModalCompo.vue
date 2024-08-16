<template>
  <div v-if="isModalVisible" class="modal-overlay" @click="handleCloseModal">
    <div class="modal-content" @click.stop>
      <div class="modal-Xcontainer" v-if="modalTitle.length < 10">
        <p class="modal-title" :class="modalTitle.length > 10 ? 'not-found' : ''">{{ modalTitle }}</p>
        <span class="x-btn">
          <font-awesome-icon class="modal-Xbox" i :icon="['fas', 'x']" @click="handleCloseModal" />
        </span>
      </div>

      <div class="modal-body" v-if="modalTitle == ''">
        <div class="not-found"><span>조회된 데이터가 없습니다!</span></div>
      </div>

      <div class="modal-body" v-else>
        <!-- followUsers가 있을 때 -->

        <template v-if="followUsers.length > 0">
          <div class="input-groups-container">
            <div class="input-groups flex-nowrap">
              <span class="input-groups-text">
                <font-awesome-icon :icon="['fas', 'magnifying-glass']" />
              </span>
              <input type="text" class="form-control-input" placeholder="Username" v-model="username" />
            </div>
          </div>

          <div class="find-modal-box">
            <find-modal-compo
              v-for="(user, idx) in filteredUsers(followUsers)"
              :key="idx"
              :userId="user.followId"
              :followid="user.userId"
              :type="user.followUserAvatarType"
              :nickname="user.followNickname"
              :level="user.followUserLv"
              :imgUrl="user.imgUrl"
              :points="user.followUserPoints"
              :checked="isFollowArr[idx]"
              @update:checked="handleCheckedChange(idx, $event)"
            ></find-modal-compo>
          </div>
        </template>

        <template v-else-if="followedUsers.length > 0">
          <div class="input-groups-container">
            <div class="input-groups flex-nowrap">
              <span class="input-groups-text">
                <font-awesome-icon :icon="['fas', 'magnifying-glass']" />
              </span>
              <input type="text" class="form-control-input" placeholder="Username" v-model="username" />
            </div>
          </div>
          <div class="find-modal-box">
            <find-modal-compo
              v-for="(user, idx) in filteredUsers(followedUsers)"
              :key="idx"
              :userId="user.userId"
              :followid="user.followId"
              :type="user.followUserAvatarType"
              :nickname="user.userNickname"
              :points="user.points"
              :level="user.lv"
              :imgUrl="user.imgUrl"
              :checked="isFolledArr[idx]"
              @update:checked="CheckedChange(idx, $event)"
            ></find-modal-compo>
          </div>
        </template>

        <template v-else-if="allUsers.length > 0">
          <div class="input-groups-container">
            <div class="input-groups flex-nowrap">
              <span class="input-groups-text">
                <font-awesome-icon :icon="['fas', 'magnifying-glass']" />
              </span>
              <input type="text" class="form-control-input" placeholder="Username" v-model="username" />
            </div>
          </div>

          <div class="find-modal-box">
            <find-modal-compo
              v-for="(user, idx) in filteredUsers(allUsers)"
              :key="idx"
              :userId="user.userId"
              :nickname="user.userNickname"
              :level="user.lv"
              :points="user.points"
              :imgUrl="user.imgUrl"
              :type="user.userAvatarType"
              :checked="AllUserArr[idx]"
              @update:checked="CheckedAllUsers(idx, $event)"
            ></find-modal-compo>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions, mapState } from 'vuex';
import FindModalCompo from './FindModalCompo.vue';

export default {
  components: { FindModalCompo },
  data() {
    return {
      username: '',
      isFollowArr: [], // 팔로우 상태를 저장할 배열
      isFolledArr: [], // 나를 팔로우한 사람들 배열
      AllUserArr: [], // 모든 유저들 배열
    };
  },
  computed: {
    ...mapState('user', {
      userInfo: 'user_info',
    }),
    ...mapState('modal', {
      followUsers: 'followUsers',
      followedUsers: 'followedUsers',
      allUsers: 'allUsers',
    }),
    ...mapGetters('modal', {
      isModalVisible: 'isModalVisible',
    }),
    modalTitle() {
      if (this.followUsers.length > 0) {
        return '팔로잉';
      } else if (this.followedUsers.length > 0) {
        return '팔로워';
      } else if (this.allUsers.length > 0) {
        return '친구 찾기';
      } else {
        return '';
      }
    },
  },
  watch: {
    isModalVisible(newValue) {
      if (newValue) {
        this.initializeFollowStatus();
        // Initialize completed, now show the modal
        this.setModalVisible(true);
      }
    },
  },
  methods: {
    ...mapActions('modal', [
      'setModalVisible',
      'followUser',
      'unfollowUser',
      'checkIfFollowing',
      'checkIfFollowers',
      'followersUser',
      'unfollowersUser',
      'fetchAllUsers',
    ]),
    filteredUsers(users) {
      const searchTerm = this.username.toLowerCase();
      const filtered = users.filter((user) =>
        (user.followNickname || user.userNickname).toLowerCase().includes(searchTerm)
      );
      console.log('아이디값이 있나요?', filtered); // 필터링된 사용자 데이터를 확인
      return filtered;
    },

    // filteredUsers(users) {
    //   const searchTerm = this.username.toLowerCase();
    //   return users.filter((user) => (user.followNickname || user.userNickname).toLowerCase().includes(searchTerm));
    // },
    async openModal() {
      this.setModalVisible(false); // 초기화 동안 모달이 보이지 않도록 설정
      await this.initializeFollowStatus(); // 상태 초기화
      this.setModalVisible(true); // 초기화 후 모달 열기
    },

    async initializeFollowStatus() {
      const currentUserId = this.userInfo.userId;

      // followUsers 체크
      if (this.followUsers.length > 0) {
        for (let i = 0; i < this.followUsers.length; i++) {
          const user = this.followUsers[i];
          const isFollowing = await this.checkIfFollowing({ userId: currentUserId, followId: user.followId });
          this.isFollowArr[i] = isFollowing; // isFollowArr 배열에 true/false 값을 추가
        }
      }

      // followedUsers 체크
      if (this.followedUsers.length > 0) {
        for (let i = 0; i < this.followedUsers.length; i++) {
          const user = this.followedUsers[i];
          const isFollowed = await this.checkIfFollowers({ userId: user.userId, followId: currentUserId });
          this.isFolledArr[i] = isFollowed; // isFollowArr 배열에 true/false 값을 추가
        }
      }

      // allUsers 체크
      if (this.allUsers.length > 0) {
        for (let i = 0; i < this.allUsers.length; i++) {
          const user = this.allUsers[i];
          const isAllUserArr = await this.checkIfFollowers({ userId: user.userId, followId: currentUserId });
          this.AllUserArr[i] = isAllUserArr;
        }
      }
    },
    handleCheckedChange(index) {
      this.isFollowArr[index] = !this.isFollowArr[index];
    },
    CheckedChange(index) {
      this.isFolledArr[index] = !this.isFolledArr[index];
    },
    CheckedAllUsers(index) {
      this.AllUserArr[index] = !this.AllUserArr[index];
    },
    async handleCloseModal() {
      await this.applyChanges(); // 모달이 닫힐 때 변경사항 적용
      this.username = ''; // username 초기화
      this.setModalVisible(false);
    },

    async applyChanges() {
      const currentUserId = this.userInfo.userId;
      if (this.followUsers.length > 0) {
        await this.applyFollowUsersChanges(currentUserId);
      } else if (this.followedUsers.length > 0) {
        await this.applyFollowedUsersChanges(currentUserId);
      } else {
        await this.applyAllUsersChanges(currentUserId);
      }
    },
    async applyAllUsersChanges(currentUserId) {
      for (let i = 0; i < this.allUsers.length; i++) {
        const followId = this.allUsers[i].userId;
        const shouldFollow = this.AllUserArr[i];

        if (shouldFollow) {
          try {
            await this.followersUser({ userId: currentUserId, followId: followId });
          } catch (error) {
            console.error(`AllUsersChanges Failed to follow user ${currentUserId}:`, error);
          }
        } else {
          try {
            await this.unfollowersUser({ userId: currentUserId, followId: followId });
          } catch (error) {
            console.error(`AllUsersChanges Failed to unfollow user ${currentUserId}:`, error);
          }
        }
      }
    },
    async applyFollowUsersChanges(currentUserId) {
      for (let i = 0; i < this.followUsers.length; i++) {
        const followId = this.followUsers[i].followId;
        const shouldFollow = this.isFollowArr[i];

        if (shouldFollow) {
          try {
            await this.followUser({ userId: currentUserId, followId: currentUserId });
          } catch (error) {
            console.error(`Failed to follow user ${followId}:`, error);
          }
        } else {
          try {
            await this.unfollowUser({ userId: currentUserId, followId: followId });
          } catch (error) {
            console.error(`Failed to unfollow user ${followId}:`, error);
          }
        }
      }
    },

    async applyFollowedUsersChanges(currentUserId) {
      for (let i = 0; i < this.followedUsers.length; i++) {
        const followId = this.followedUsers[i].userId;
        const shouldFollow = this.isFolledArr[i];

        if (shouldFollow) {
          try {
            await this.followersUser({ userId: currentUserId, followId: followId });
          } catch (error) {
            console.error(`Failed to follow user ${currentUserId}:`, error);
          }
        } else {
          try {
            await this.unfollowersUser({ userId: currentUserId, followId: followId });
          } catch (error) {
            console.error(`Failed to unfollow user ${currentUserId}:`, error);
          }
        }
      }
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 100;
}

.modal-content {
  background-color: white;
  padding: 21px 28px 24px;
  width: 580px;
  height: 485px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 200;
  display: flex;
  flex-direction: column;
}

.modal-Xcontainer {
  padding: 12px 0px 16px 9px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.not-found {
  width: 230px;
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 18px;
  font-weight: bold;
}

.modal-body {
  flex-grow: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.modal-title {
  font-size: 22px;
  font-weight: 600;
  color: rgba(0, 0, 0, 0.719);
}

.x-btn {
  font-size: 13px;
  color: #171313e0;
  padding: 4px 6px;
}

.input-groups-container {
  position: sticky;
  top: 0;
  background-color: white;
  z-index: 10;
}

.input-groups {
  display: flex;
  align-items: center;
}

.input-groups-text {
  position: absolute;
  padding: 0 8px;
  border: 0 solid gray;
  left: 10px; /* 아이콘의 위치를 input의 내부 왼쪽에 고정 */
  pointer-events: none; /* 아이콘이 클릭되지 않도록 설정 */
  color: #999;
  z-index: 2;
}

.form-control-input {
  width: 100%;
  padding: 10px;
  padding-left: 46px;
  color: #544545;
  border: 1px solid #1d13132e;
  font-size: 14px;
  border-radius: 20px;
}

.find-modal-box {
  max-height: 320px;
  overflow-y: auto;
  padding-top: 17px;
}
</style>
