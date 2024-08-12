<template>
  <div class="guestbook-container">
    <h2>{{ ownerId }}'s GuestBook</h2>
    <div v-for="(message, index) in messages" :key="index" class="bubble">
      <p>{{ message.content }}</p>
      <small>Written by: {{ message.writerId }}</small>
      <small class="date">&emsp; {{ formatRelativeDate(message.regDateAt) }}</small>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { formatDistanceToNow } from 'date-fns';

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
        // 예시
        // '오 레벨 높은데~오 레벨 높은데~오 레벨 높은데~오 레벨 높은데~오 레벨 높은데~',
        // '좋튀',
        // '멋진 방이네요!',
      ],
    };
  },
  computed: {
    isMyRoom() {
      return this.loginId === this.ownerId;
    },
  },
  created() {
    this.fetchGuestbookMessages();
  },
  methods: {
    async fetchGuestbookMessages() {
      try {
        if (this.isMyRoom) {
          const response = await axios.get(`/api/guestbooks/users/${this.ownerId}/recent`);
          this.messages = response.data;
        } else {
          const response = await axios.get(`/api/guestbooks/users/${this.ownerId}/writers/${this.loginId}`);
          this.messages = response.data;
        }
      } catch (e) {
        console.error('Failed to load guestbook messages:', e);
      }
    },
    formatRelativeDate(date) {
      return formatDistanceToNow(new Date(date), { addSuffix: true });
    },
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

.bubble small {
  color: #888;
  font-size: 0.85em;
  margin-top: 10px;
}
</style>
