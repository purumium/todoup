<template>
  <div class="">
    <div class="room-btn-group">
      <router-link to="/room/avatarroom">
        <button>AVATARROOM</button>
      </router-link>
      <router-link to="/room/report">
        <button>REPORT</button>
      </router-link>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'RoomCompo',
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
