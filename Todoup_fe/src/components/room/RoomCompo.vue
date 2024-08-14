<template>
  <div>
    <div class="d-flex align-items-center justify-content-between">
      <h3>{{ nickName }}님의 방</h3>
      <div class="room-btn-group">
        <router-link :to="`/room/${ownerId}/avatarroom`">
          <button>AVATARROOM</button>
        </router-link>
        <router-link v-if="isMyRoom" :to="`/room/${ownerId}/report`">
          <button>REPORT</button>
        </router-link>
      </div>
    </div>
    <!-- loginUserInfo와 ownerUserInfo를 함께 전달 -->
    <router-view
      :login-id="loginId"
      :owner-id="ownerId"
      :login-user-info="loginUserInfo"
      :login-user-profile-img="loginUserProfileImg"
      :owner-user-info="ownerUserInfo"
    ></router-view>
  </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
  name: 'RoomCompo',
  props: {
    ownerId: {
      type: String,
      required: true,
    },
  },
  computed: {
    ...mapState('user', {
      loginId: (state) => String(state.user_info.userId),
      loginUserInfo: (state) => state.user_info, // 로그인 유저의 전체 정보
      loginUserProfileImg: (state) => '/avatar/' + state.profileImg + '.png',
      nickName: (state) => state.user_info.nickName,
    }),
    ownerUserInfo() {
      // 여기에 친구(혹은 방 소유자) 정보를 가져오는 로직을 추가할 수 있습니다.
      // 예를 들어, API 호출 등을 통해 가져올 수 있습니다.
      return this.isMyRoom ? this.loginUserInfo : {}; // 나중에 로직 변경 가능
    },
    // chan's code
    //   loginId: (state) => String(state.user_info.userId), // Vuex의 user_info.userId를 loginId로 매핑
    //   nickName: (state) => String(state.user_info.nickName),
    // }),
    // ...mapState('modal', {
    //   selectedUserId: (state) => state.selectedUserId,
    //   selectedUserLv: (state) => state.selectedUserLv,
    //   selectedUserType: (state) => state.selectedUserType,
    //   selectedUserPoint: (state) => state.selectedUserPoint,
    //   selectedUserNickname: (state) => state.selectedUserNickname,
    // }),
    isMyRoom() {
      return this.loginId === this.ownerId;
    },
  },
  mounted() {
    // 컴포넌트가 마운트될 때 콘솔에 Vuex 상태를 출력합니다.
    console.log('Selected User ID:', this.selectedUserId);
    console.log('Selected User Level:', this.selectedUserLv);
    console.log('Selected User Type:', this.selectedUserType);
    console.log('Selected User Point:', this.selectedUserPoint);
    console.log('Selected User Nickname:', this.selectedUserNickname);
  },
};
</script>

<style scoped>
.room-btn-group {
  display: flex;
  justify-content: end;
  gap: 10px;
  margin-bottom: 20px;
}

.room-btn-group button {
  width: 10em;
  height: 3em;
  background-color: #e5e5e51f;
  color: #544545;
  border: 1.5px solid #1d13132e;
  font-weight: 600;
  font-size: 13px;
  border-radius: 20px;
}
</style>
