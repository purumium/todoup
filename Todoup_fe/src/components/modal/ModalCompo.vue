<template>
  <div v-if="isModalVisible" class="modal-overlay" @click="handleCloseModal">
    <div class="modal-content" @click.stop>
      <div class="modal-Xcontainer">
        <p class="modal-title">{{ modalTitle }}</p>
        <font-awesome-icon class="modal-Xbox" i :icon="['fas', 'x']" @click="handleCloseModal" />
      </div>
      <div class="modal-body">
        <!-- followUsers가 있을 때 -->

        <template v-if="followUsers.length > 0">
          <div class="input-group flex-nowrap">
            <input type="text" class="form-control" placeholder="Username" v-model="username" />
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
              :checked="isFollowArr[idx]"
              @update:checked="handleCheckedChange(idx, $event)"
            ></find-modal-compo>
          </div>
        </template>

        <template v-else-if="followedUsers.length > 0">
          <div class="input-group flex-nowrap">
            <input type="text" class="form-control" placeholder="Username" v-model="username" />
          </div>
          <div class="find-modal-box">
            <find-modal-compo
              v-for="(user, idx) in filteredUsers(followedUsers)"
              :key="idx"
              :userId="user.userId"
              :followid="user.followId"
              :type="user.followUserAvatarType"
              :nickname="user.userNickname"
              :level="user.lv"
              :imgUrl="user.imgUrl"
              :checked="isFolledArr[idx]"
              @update:checked="CheckedChange(idx, $event)"
            ></find-modal-compo>
          </div>
        </template>

        <template v-else-if="allUsers.length > 0">
          <div class="input-group flex-nowrap">
            <input type="text" class="form-control" placeholder="Username" v-model="username" />
          </div>
          <div class="find-modal-box">
            <find-modal-compo
              v-for="(user, idx) in filteredUsers(allUsers)"
              :key="idx"
              :userId="user.userId"
              :nickname="user.userNickname"
              :level="user.lv"
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
        return 'title';
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

.modal-Xcontainer {
  width: 100%;
  margin-bottom: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 201;
}
.modal-Xbox {
  font-size: 12px;
  cursor: pointer;
  z-index: 202;
}
.modal-content {
  background-color: white;
  padding: 10px 20px 20px;
  width: 600px;
  height: 380px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 200;
}

.modal-body {
  max-height: 100%;
  overflow-y: auto;
  flex-direction: column;
  display: flex;
  justify-content: space-between;
}
.input-group {
  position: fixed;
  max-width: 540px;
}
.find-modal-box {
  margin-top: 50px;
}

.modal-title {
  font-size: 22px;
  font-weight: 600;
  color: rgba(0, 0, 0, 0.719);
}
</style>
