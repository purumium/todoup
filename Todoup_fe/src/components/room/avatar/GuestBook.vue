<template>
  <div class="guestbook-container">
    <h2>{{ ownerId }}'s GuestBook</h2>
    <!-- 방명록 입력창: 친구의 방일 경우에만 표시 -->
    <div v-if="!isMyRoom" class="new-message-form">
      <textarea v-model="newMessageContent" placeholder="친구에게 응원의 한마디!" rows="3"></textarea>
      <button @click="submitMessage">남기기</button>
    </div>

    <div v-for="(message, index) in messages" :key="index" class="bubble">
      <div v-if="!message.isEditing">
        <p>{{ message.content }}</p>
        <small>Written by: {{ message.writerId }}</small>
        <small class="date">&emsp; {{ formatRelativeDate(message.regDateAt) }}</small>
        <button v-if="isAuthor(message.writerId)" @click="editMessage(index)">수정</button>
        <button v-if="isAuthor(message.writerId)" @click="confirmDeleteMessageAsWriter(message.guestbookId, index)">
          삭제
        </button>
        <button v-if="isMyRoom" @click="confirmDeleteMessageAsOwner(message.guestbookId, index)">삭제</button>
      </div>
      <div v-else>
        <textarea v-model="message.content"></textarea>
        <button @click="submitEdit(index, message.guestbookId)">저장</button>
        <button @click="cancelEdit(index)">취소</button>
      </div>
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
      originalMessageContent: '', // 수정 전 메시지를 저장하는 변수
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
          this.messages = response.data.map((message) => ({
            ...message,
            isEditing: false,
          }));
        } else {
          const response = await axios.get(`/api/guestbooks/users/${this.ownerId}/writers/${this.loginId}`);
          this.messages = response.data.map((message) => ({
            ...message,
            isEditing: false,
          }));
        }
      } catch (e) {
        console.error('방명록을 제대로 불러오지 못 했습니다.', e);
      }
    },
    async submitMessage() {
      if (this.newMessageContent.trim() === '') {
        this.$swal.fire({
          text: '내용을 채워주세요.',
          icon: 'warning',
          confirmButtonText: '확인',
          confirmButtonColor: '#f39c12',
        });
        return;
      }

      try {
        const newMessage = {
          userId: this.ownerId,
          writerId: this.loginId,
          content: this.newMessageContent,
        };

        const response = await axios.post(`/api/guestbooks/users/${this.ownerId}/writers/${this.loginId}`, newMessage);

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
    isAuthor(writerId) {
      return this.loginId === String(writerId);
    },
    // 작성자가 삭제하기 전 확인창
    confirmDeleteMessageAsWriter(guestbookId, index) {
      if (confirm('이 방명록을 삭제하시겠습니까?')) {
        this.deleteMessageAsWriter(guestbookId, index);
      }
    },

    // 방 주인이 삭제하기 전 확인창
    confirmDeleteMessageAsOwner(guestbookId, index) {
      if (confirm('이 방명록을 삭제하시겠습니까?')) {
        this.deleteMessageAsOwner(guestbookId, index);
      }
    },
    async deleteMessageAsWriter(guestbookId, index) {
      try {
        const response = await axios.delete(`/api/guestbooks/${guestbookId}/writers/${this.loginId}`);
        if (response.status === 204) {
          this.messages.splice(index, 1);
        } else {
          console.error('방명록 삭제가 제대로 완료되지 못 했습니다.', response.status, response.statusText);
        }
      } catch (e) {
        console.error('방명록 삭제가 제대로 완료되지 못 했습니다.', e);
      }
    },
    async deleteMessageAsOwner(guestbookId, index) {
      try {
        const response = await axios.delete(`/api/guestbooks/${guestbookId}/users/${this.ownerId}`);
        if (response.status === 204) {
          this.messages.splice(index, 1);
        } else {
          console.error('방명록 삭제가 제대로 완료되지 못 했습니다.', response.status, response.statusText);
        }
      } catch (e) {
        console.error('방명록 삭제가 제대로 완료되지 못 했습니다.', e);
      }
    },
    editMessage(index) {
      this.originalMessageContent = this.messages[index].content; // 현재 메시지 내용을 저장
      this.messages[index].isEditing = true;
    },
    cancelEdit(index) {
      this.messages[index].content = this.originalMessageContent; // 원래 메시지 내용으로 복원
      this.messages[index].isEditing = false;
    },
    async submitEdit(index, guestbookId) {
      const message = this.messages[index];
      try {
        const response = await axios.put(`/api/guestbooks/${guestbookId}/writers/${this.loginId}`, {
          guestbookId: guestbookId,
          writerId: this.loginId,
          content: message.content,
        });
        if (response.status === 200) {
          await this.fetchGuestbookMessages();
        } else {
          console.error('방명록 수정이 제대로 완료되지 못 했습니다.', response.status, response.statusText);
        }
      } catch (e) {
        console.error('방명록 수정이 제대로 완료되지 못 했습니다.', e);
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
