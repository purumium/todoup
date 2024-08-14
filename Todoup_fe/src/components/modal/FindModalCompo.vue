<template>
  <div class="friend-item">
    <div class="friend-leftBox">
      <img class="item-img" @click="onImageClick" :src="`/avatar/${this.type}_level${this.level}_title.png`" />
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

      // 라우팅을 컴포넌트에서 직접 처리
      this.$router.push(`/room/${this.userId}/avatarroom`);
      // 모달을 먼저 닫고
      this.setModalVisible(false);
    },
  },
};
</script>

<style scoped>
.friend-item {
  display: flex;
  border: 1px solid #80808033;
  background-color: #c1baba14;
  align-items: center;
  justify-content: space-between;
  padding: 9px 16px 7px 15px;
  margin-bottom: 11px;
  border-radius: 20px;
}

.item-img {
  border: 2px solid #8080801a;
  width: 80px;
  height: 75px;
  border-radius: 100%;
  margin-right: 11px;
  cursor: pointer;
  padding: 8px;
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
  font-size: 9px;
  font-weight: 500;
  white-space: nowrap;
  margin-right: 40px;
  border: 1px solid #cecbcb;
  border-radius: 20px;
  padding: 2px 10px;
  letter-spacing: 1px;
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
  background-color: #e5e7e9ab;
  color: #313030;
  border-radius: 20px;
  font-size: 12px;
}

.unfollow-button {
  background-color: #fcf07387;
  color: #313030;
  border-radius: 20px;
  font-size: 12px;
}

.item-img {
  background-size: cover;
  background-position: center;
}

.input-group > .form-control,
.input-group > .form-floating,
.input-group > .form-select {
  position: relative;
  width: 76%;
  min-width: 0;
  border-radius: 20px;
  border: 1px solid #80808059;
}
</style>
