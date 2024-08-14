<template>
  <div class="friend-item">
    <div class="friend-leftBox">
      <img class="item-img" @click="onImageClick" :src="`/avatar/${this.type}_level${this.level}.png`" />
      <div>{{ nickname }}</div>
    </div>
    <div class="friend-rightBox">
      <div>LEVEL {{ level }}</div>
      <button @click="toggleCheck" :class="buttonClass">{{ buttonText }}</button>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  props: {
    nickname: {
      type: String,
      required: true,
    },
    level: {
      type: Number,
      required: true,
    },
    checked: {
      type: Boolean,
      required: true,
    },
    userId: {
      type: Number,
      required: true,
    },
    type: {
      type: String,
      required: true,
    },
  },
  computed: {
    buttonText() {
      return this.checked ? '언팔로우' : '팔로우';
    },
    buttonClass() {
      return this.checked ? 'unfollow-button' : 'follow-button';
    },
  },

  methods: {
    ...mapActions('modal', ['setSelectedUserId', 'setModalVisible']), // Vuex 액션을 컴포넌트 메서드로 바인딩

    toggleCheck() {
      this.$emit('update:checked', !this.checked);
    },
    onImageClick() {
      // 유저 아이디를 Vuex 스토어에 저장
      this.setSelectedUserId(this.userId);

      // 모달을 먼저 닫고
      this.setModalVisible(false);

      // 라우팅을 컴포넌트에서 직접 처리
      this.$router.push(`/room/${this.userId}/avatarroom`);
    },
  },
};
</script>

<style scoped>
.friend-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
}
.item-img {
  width: 70px;
  height: 70px;
  border-radius: 100%;
  margin-right: 20px;
  cursor: pointer; /* 클릭 가능한 상태로 변경 */
}
.friend-leftBox {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.friend-rightBox {
  justify-content: space-between;
  display: flex;
  align-items: center;
}
.friend-rightBox > div {
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
  margin-right: 28px;
}
button {
  cursor: pointer;
  padding: 6px 12px;
  font-size: 14px;
  border-radius: 4px;
  border: none;
  outline: none;
}
.follow-button {
  background-color: #3a84d8;
  color: white;
}
.unfollow-button {
  background-color: #cf4848;
  color: white;
}
.item-img {
  background-size: cover;
  background-position: center;
}
</style>
