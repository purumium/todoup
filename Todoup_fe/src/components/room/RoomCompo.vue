<template>
  <div>
    <div class="d-flex align-items-center justify-content-between room-title">
      <div>{{ roomOwnerNickName }}님의 방</div>
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
      :login-user-info="loginUserInfo"
      :owner-user-info="isMyRoom ? null : ownerUserInfo"
      :owner-id="ownerId"
    >
    </router-view>
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
      loginUserInfo: (state) => ({
        userId: String(state.user_info.userId),
        lv: state.user_info.lv,
        points: state.user_info.points,
        nickName: state.user_info.nickName,
        profileImg: `/avatar/${state.profileImg}.png`,
      }),
    }),
    ...mapState('modal', {
      ownerUserInfo: (state) => ({
        userId: String(state.selectedUserId),
        lv: state.selectedUserLv,
        points: state.selectedUserPoint,
        nickName: state.selectedUserNickname,
        profileImg: `/avatar/${state.selectedUserType}_level${state.selectedUserLv}.png`,
      }),
    }),
    isMyRoom() {
      return this.loginUserInfo.userId === this.ownerId;
    },
    roomOwnerNickName() {
      return this.isMyRoom ? this.loginUserInfo.nickName : this.ownerUserInfo.nickName;
    },
  },
};
</script>

<style scoped>
.room-title {
  color: #2b2222b8 !important;
  font-weight: 600;
  font-size: 18px;
  display: flex;
  padding: 7px 0;
  border-bottom: 2px solid #cfcece70;
  border-top: 2px solid #cfcece70;
  height: 55px;
  align-items: center;
}

.room-btn-group {
  display: flex;
  justify-content: end;
  gap: 10px;
}

.room-btn-group button {
  width: 10em;
  height: 2.5em;
  background-color: #e5e5e51f;
  color: #544545;
  border: 1.5px solid #1d13132e;
  font-weight: 600;
  font-size: 13px;
  border-radius: 20px;
}
</style>
