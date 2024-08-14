<template>
  <div class="friend-item">
    <div class="friend-leftBox">
      <div class="item-img"></div>
      <div>{{ nickname }}</div>
    </div>
    <div class="friend-rightBox">
      <div>LEVEL {{ level }}</div>
      <button @click="toggleCheck" :class="buttonClass">{{ buttonText }}</button>
    </div>
  </div>
</template>

<script>
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
  },
  emits: ['update:checked'],
  computed: {
    buttonText() {
      return this.checked ? '언팔로우' : '팔로우';
    },
    buttonClass() {
      return this.checked ? 'unfollow-button' : 'follow-button';
    },
  },
  methods: {
    toggleCheck() {
      this.$emit('update:checked', !this.checked);
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
  background-color: #333;
  width: 70px;
  height: 70px;
  border-radius: 100%;
  margin-right: 20px;
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
</style>
