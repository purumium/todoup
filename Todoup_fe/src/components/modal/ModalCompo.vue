<template>
  <div v-if="isModalVisible" class="modal-overlay" @click="handleCloseModal">
    <div class="modal-content" @click.stop>
      <div class="input-group flex-nowrap">
        <input
          type="text"
          class="form-control"
          placeholder="Username"
          aria-label="Username"
          aria-describedby="addon-wrapping"
          v-model="username"
        />
      </div>
      <div class="modal-body">
        <template v-if="followUsers.length > 0">
          <find-modal-compo
            v-for="(user, idx) in followUsers"
            :key="idx"
            :userid="user.userId"
            :followid="user.followId"
            :nickname="user.followNickname"
            :level="user.lv"
            :imgUrl="user.imgUrl"
            :checked="isFollowArr[idx]"
            @update:checked="handleCheckedChange(idx, $event)"
          ></find-modal-compo>
        </template>
        <template v-else>
          <find-modal-compo
            v-for="(user, idx) in followedUsers"
            :key="idx"
            :userid="user.userId"
            :followid="user.followId"
            :nickname="user.userNickname"
            :level="user.lv"
            :imgUrl="user.imgUrl"
            :checked="isFolledArr[idx]"
            @update:checked="CheckedChange(idx, $event)"
          ></find-modal-compo>
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
      isFolledArr: [],
    };
  },
  computed: {
    ...mapState('user', {
      userInfo: 'user_info',
    }),
    ...mapState('modal', {
      followUsers: 'followUsers',
      followedUsers: 'followedUsers',
    }),
    ...mapGetters('modal', {
      isModalVisible: 'isModalVisible',
    }),
  },
  watch: {
    async isModalVisible(newValue) {
      if (newValue) {
        await this.initializeFollowStatus();
        // Initialize completed, now show the modal
        this.setModalVisible(true);
        console.log(this.isFolledArr);
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
    ]),
    async openModal() {
      this.setModalVisible(false); // 초기화 동안 모달이 보이지 않도록 설정
      await this.initializeFollowStatus(); // 상태 초기화
      this.setModalVisible(true); // 초기화 후 모달 열기
    },

    async initializeFollowStatus() {
      const currentUserId = this.userInfo.userId;
      // this.isFollowArr = []; // 초기화

      for (let i = 0; i < this.followUsers.length; i++) {
        const user = this.followUsers[i];
        const isFollowing = await this.checkIfFollowing({ userId: currentUserId, followId: user.followId });
        this.isFollowArr[i] = isFollowing; // isFollowArr 배열에 true/false 값을 추가
      }
      for (let i = 0; i < this.followedUsers.length; i++) {
        const user = this.followedUsers[i];
        console.log('currentUserId', currentUserId, 'user', this.followedUsers[i].userId);
        const isFollowed = await this.checkIfFollowers({ userId: user.userId, followId: currentUserId });
        this.isFolledArr[i] = isFollowed; // isFollowArr 배열에 true/false 값을 추가
      }
      console.log('isFolledArrasdasdasdas:', this.isFolledArr); // 확인용
      console.log('isFollowArr:', this.isFollowArr); // 확인용
    },

    handleCheckedChange(index) {
      // followUsers 배열이 렌더링된 경우
      console.log('handleCheckedChange : 첫 번째');
      this.isFollowArr[index] = !this.isFollowArr[index];
    },
    CheckedChange(index) {
      this.isFolledArr[index] = !this.isFolledArr[index];
    },
    async handleCloseModal() {
      await this.applyChanges(); // 모달이 닫힐 때 변경사항 적용
      this.setModalVisible(false);
    },

    async applyChanges() {
      const currentUserId = this.userInfo.userId;
      console.log('currentUserIdcurrentUserIdcurrentUserIdcurrentUserId', currentUserId);
      console.log('this.followUsers.length', this.followUsers.length);
      console.log('this.followedUsers.lengthssssssssssss', this.followedUsers.length);
      if (this.followUsers.length > 0) {
        await this.applyFollowUsersChanges(currentUserId);
      } else if (this.followedUsers.length > 0) {
        await this.applyFollowedUsersChanges(currentUserId);
      }
    },

    async applyFollowUsersChanges(currentUserId) {
      for (let i = 0; i < this.followUsers.length; i++) {
        const followId = this.followUsers[i].followId;
        const shouldFollow = this.isFollowArr[i];

        const user = this.followUsers[i];
        const result = await this.checkIfFollowing({ userId: user.followId, followId: currentUserId });

        console.log('followId', followId);
        console.log('currentUserId', currentUserId);
        if (shouldFollow && result) {
          try {
            await this.followUser({ userId: currentUserId, followId: currentUserId });
            console.log('Follow user successful', currentUserId, followId);
          } catch (error) {
            console.error(`Failed to follow user ${followId}:`, error);
          }
        } else {
          try {
            await this.unfollowUser({ userId: currentUserId, followId: followId });
            console.log('Unfollow user successful', currentUserId, followId);
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
        console.log('내가', currentUserId);
        console.log('shouldFollow======================', this.followedUsers[i].userId);
        // const user = this.followedUsers[i];
        // const result = await this.checkIfFollowers({ userId: currentUserId, followId: user.followId });
        console.log('이게 실행됩니다.');
        if (shouldFollow) {
          try {
            await this.followersUser({ userId: currentUserId, followId: followId });
            console.log('Follow follower successful', currentUserId, followId);
          } catch (error) {
            console.error(`Failed to follow user ${currentUserId}:`, error);
          }
        } else {
          try {
            await this.unfollowersUser({ userId: currentUserId, followId: followId });
            console.log('Unfollow follower successful', currentUserId, followId);
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
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  width: 600px;
  height: 380px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
}

.modal-body {
  max-height: 300px;
  overflow-y: auto;
}
</style>
