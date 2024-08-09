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
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'RoomCompo',
  props: {
    ownerId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      loginId: 'temporaryUserId', // 임시 로그인 ID
    };
  },
  computed: {
    isMyRoom() {
      return this.loginId === this.ownerId;
    },
  },
};
</script>

<style scoped>
.room-btn-group {
  display: flex;
  justify-content: flex-start;
  gap: 10px;
  margin-bottom: 20px;
}
</style>
