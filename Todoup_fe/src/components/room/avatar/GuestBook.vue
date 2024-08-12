<template>
  <div class="guestbook-container">
    <h2>{{ ownerId }}'s GuestBook</h2>
    <!-- 방명록 입력창: 친구의 방일 경우에만 표시 -->
    <div v-if="!isMyRoom" class="new-message-form">
      <textarea v-model="newMessageContent" placeholder="친구에게 응원의 한마디!" rows="3"></textarea>
      <button @click="submitMessage">남기기</button>
    </div>

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
      messages: [],
      newMessageContent: '',
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
        console.error('방명록을 제대로 불러오지 못 했습니다.', e);
      }
    },
    async submitMessage() {
      if (this.newMessageContent.trim() === '') {
        alert('내용을 채워주세요.');
        return;
      }

      try {
        const newMessage = {
          userId: this.ownerId,
          writerId: this.loginId,
          content: this.newMessageContent,
        };
        console.log('등록1: 세팅');

        const response = await axios.post(`/api/guestbooks/users/${this.ownerId}/writers/${this.loginId}`, newMessage);
        console.log('등록2: db 갖다옴');
        console.log('Response status:', response.status);

        if (response.status === 201) {
          // 전체 리스트를 다시 로드
          await this.fetchGuestbookMessages();
          // 입력 필드 초기화
          this.newMessageContent = '';
        } else {
          console.error('방명록 남기기가 제대로 완료되지 못 했습니다.', response.status, response.statusText);
        }
      } catch (e) {
        console.error('방명록 남기기가 제대로 완료되지 못 했습니다.', e);
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
