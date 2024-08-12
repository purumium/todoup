<template>
  <div class="guestbook-container">
    <h2>{{ ownerId }}'s GuestBook</h2>
    <div v-for="(message, index) in messages" :key="index" class="bubble">
      <p>{{ message }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GuestBook',
  props: {
    loginId: {
      type: String,
      required: true,
    },
    ownerId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      messages: [
        '오 레벨 높은데~오 레벨 높은데~오 레벨 높은데~오 레벨 높은데~오 레벨 높은데~',
        '좋튀',
        '멋진 방이네요!',
      ], // 예시 방명록 메시지
    };
  },
};
</script>

<style scoped>
.guestbook-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* 왼쪽 정렬 */
  width: 500px; /* 나중에 제대로 맞추기 */
  padding-left: 10px;
}

.bubble {
  position: relative;
  background: white;
  border-radius: 15px;
  padding: 10px 20px;
  color: #333;
  max-width: 500px;
  text-align: left;
  font-size: 1em;
  margin: 10px 0; /* 방명록 간 간격 */
  border: 1px solid #ccc;
}

.bubble::after {
  content: '';
  position: absolute;
  border-style: solid;
  border-width: 10px 10px 10px 0;
  border-color: transparent white transparent transparent;
  display: block;
  width: 0;
  z-index: 1;
  left: -10px; /* 말풍선 꼬리를 왼쪽에 배치 */
  top: 10px; /* 말풍선의 상단에서 시작 */
}
</style>
