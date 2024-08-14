<template>
  <div>
    <div class="room-btn-group">
      <router-link :to="`/room/${ownerId}/avatarroom`">
        <button>AVATARROOM</button>
      </router-link>
      <router-link v-if="isMyRoom" :to="`/room/${ownerId}/report`">
        <button>REPORT</button>
      </router-link>
    </div>
    <router-view :login-id="loginId" :owner-id="ownerId"></router-view>
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
      loginId: (state) => String(state.user_info.userId), // 화살표 함수: Vuex의 user_info.userId를 loginId로 매핑 // ownerId와 맞춰서 String
    }),
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
