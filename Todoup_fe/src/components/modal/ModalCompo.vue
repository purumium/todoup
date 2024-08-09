<template>
  <div v-if="isModalVisible" class="modal-overlay" @click="closeModal">
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
        <find-modal-compo
          v-for="(user, idx) in filteredUsers"
          :key="idx"
          :nickname="user.nickname"
          :level="user.level"
          :checked="user.checked"
          :original-index="getOriginalIndex(user)"
          @update:checked="updateChecked"
        ></find-modal-compo>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import FindModalCompo from './FindModalCompo.vue';

export default {
  components: { FindModalCompo },
  data() {
    return {
      username: '',
      users: [],
    };
  },
  computed: {
    ...mapGetters(['isModalVisible']),
    filteredUsers() {
      return this.users.filter((user) => user.nickname.includes(this.username));
    },
  },
  methods: {
    ...mapActions(['setModalVisible']),
    getOriginalIndex(user) {
      return this.users.findIndex((u) => u.id === user.id);
    },
    updateChecked(index, checked) {
      this.users[index].checked = checked;
    },
    closeModal() {
      this.setModalVisible(false);
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
