<template>
  <div class="modal-overlay">
    <div>
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
      <div class="modal-content">
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
import FindModalCompo from './FindModalCompo.vue';

export default {
  components: { FindModalCompo },
  data() {
    return {
      username: '',
      users: [
        { id: 1, nickname: '홍길동', level: 1, checked: true },
        { id: 2, nickname: '김찬희', level: 10, checked: false },
        { id: 3, nickname: '최유환', level: 5, checked: true },
      ],
    };
  },
  computed: {
    filteredUsers() {
      return this.users.filter((user) => user.nickname.includes(this.username));
    },
  },
  methods: {
    getOriginalIndex(user) {
      return this.users.findIndex((u) => u.id === user.id);
    },
    updateChecked(index, checked) {
      this.users[index].checked = checked;
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
  background-color: rgba(0, 0, 0, 0.5); /* 반투명 검은색 배경 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* 다른 요소 위에 표시되도록 z-index 설정 */
}

.modal-content {
  width: 600px;
  height: 370px;
  background-color: white;
  display: flex;
  flex-direction: column;
  border-radius: 8px; /* 둥근 모서리 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  padding: 20px; /* 내부 여백 추가 */
}
</style>
