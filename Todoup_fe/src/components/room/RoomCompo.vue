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
    isMyRoom() {
      return this.loginId === this.ownerId;
    },
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
