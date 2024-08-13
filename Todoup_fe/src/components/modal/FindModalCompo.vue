<template>
  <div class="friend-item">
    <div class="friend-leftBox">
      <div class="item-img"></div>
      <div>{{ nickname }}</div>
    </div>
    <div class="friend-rightBox">
      <div>Lv : {{ level }}</div>
      <font-awesome-icon
        v-if="checked"
        icon="square-check"
        style="color: #0077ff"
        class="realcheck"
        @click="toggleCheck"
      />
      <font-awesome-icon v-else icon="square-xmark" style="color: #ff0000" class="realcheck" @click="toggleCheck" />
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
  padding: 10px; /* 내부 여백 추가 */
  margin-bottom: 10px; /* 아이템 간 간격 추가 */
  border-radius: 5px; /* 둥근 모서리 */
}
.item-img {
  background-color: blue;
  width: 80px;
  height: 80px;
  border-radius: 100%;
  margin-right: 10px; /* 이미지와 텍스트 간 간격 추가 */
}
.friend-leftBox {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 200px;
}
.friend-rightBox {
  width: 140px;
  justify-content: space-between;
  display: flex;
  align-items: center;
}
.realcheck {
  cursor: pointer; /* 클릭 가능한 영역임을 표시 */
  width: 25px;
  height: 25px;
}
</style>
