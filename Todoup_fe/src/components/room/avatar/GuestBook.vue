<template>
  <div class="guestbook-container" ref="guestbookContainer" @scroll="handleScroll">
    <h2>{{ ownerId }}'s GuestBook</h2>
    <!-- 방명록 입력창: 친구의 방일 경우에만 표시 -->
    <div v-if="!isMyRoom" class="new-message-form">
      <textarea
        v-model="newMessageContent"
        placeholder="친구에게 응원의 한마디!"
        rows="3"
        @keydown.enter="handleKeyDown"
      ></textarea>
      <button @click="submitMessage">남기기</button>
    </div>

    <!-- 방명록이 없을 때 표시할 메시지 -->
    <div v-if="messages.length === 0" class="no-guestbook-card">
      <p>새 방명록이 없습니다. 오늘도 To Do Up!</p>
    </div>

    <div v-for="(message, index) in messages" :key="index" class="bubble">
      <div v-if="!message.isEditing">
        <p class="message-content">{{ message.content }}</p>
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
    <div v-if="loading" class="loading-indicator">Loading...</div>
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
      // 동적 로딩 관련
      page: 0, // 현재 페이지 번호
      size: 3, // 한번에 불러올 데이터 개수
      loading: false, // 데이터 로딩 상태
      allDataLoaded: false, // 모든 데이터를 불러왔는지 여부
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
    // 데이터 받아오기
    async fetchGuestbookMessages() {
      console.log(this.page);
      if (this.loading || this.allDataLoaded) return; // 중복 호출 방지 및 모든 데이터 로드 시 종료

      this.loading = true; // 로딩 시작

      try {
        // 데이터 받아오기
        let response;
        if (this.isMyRoom) {
          response = await axios.get(`/api/guestbooks/users/${this.ownerId}/recent`, {
            params: {
              page: this.page,
              size: this.size,
            },
          });
        } else {
          response = await axios.get(`/api/guestbooks/users/${this.ownerId}/writers/${this.loginId}`, {
            params: {
              page: this.page,
              size: this.size,
            },
          });
        }

        // 페이지네이션 관련
        const newMessages =
          response.data && Array.isArray(response.data)
            ? response.data.map((message) => ({
                ...message,
                isEditing: false,
              }))
            : [];

        if (newMessages < this.size) {
          this.allDataLoaded = true; // 전부 다 불러옴.
        }

        this.messages = [...this.messages, ...newMessages]; // 새 메시지 추가
        this.page += 1; // 다음 페이지를 로드하도록 페이지 번호 증가
      } catch (e) {
        console.error('방명록을 제대로 불러오지 못 했습니다.', e);
      } finally {
        this.loading = false; // 로딩 끝
      }
    },

    // 스크롤
    handleScroll() {
      const container = this.$refs.guestbookContainer;
      if (container.scrollTop + container.clientHeight >= container.scrollHeight - 10) {
        this.fetchGuestbookMessages(); // 스크롤이 끝에 도달하면 데이터 로드
      }
    },

    // 등록
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

        const response = await axios.post(`/api/guestbooks/users/${this.ownerId}/writers/${this.loginId}`, newMessage);

        if (response.status === 201) {
          // 전체 리스트를 다시 로드
          this.page = 0;
          this.allDataLoaded = false; // 모든 데이터를 불러온 상태를 초기화
          this.messages = []; // 기존 메시지 목록 초기화
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

    // 엔터로 등록
    handleKeyDown(event) {
      if (event.shiftKey) {
        // Shift + Enter: 줄바꿈
        return;
      }

      // Enter 키만 눌렸을 때는 submitMessage 실행
      event.preventDefault(); // Enter의 기본 동작인 줄바꿈을 막음
      this.submitMessage();
    },

    // 작성자인지
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

    // 삭제 작성자
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

    // 삭제 방 주인
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

    // 수정 중
    editMessage(index) {
      this.originalMessageContent = this.messages[index].content; // 현재 메시지 내용을 저장
      this.messages[index].isEditing = true;
    },

    // 수정 취소
    cancelEdit(index) {
      this.messages[index].content = this.originalMessageContent; // 원래 메시지 내용으로 복원
      this.messages[index].isEditing = false;
    },

    // 수정 완료
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

    // 날짜 포맷
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
  width: 90%; /* 나중에 제대로 맞추기 */
  padding-left: 10px;
  height: 300px; /* 원하는 높이 설정 */
  overflow-y: auto; /* 세로 스크롤바 활성화 */
  padding-right: 15px; /* 스크롤바가 콘텐츠에 겹치지 않도록 여유 공간 추가 */
}

/* 스크롤 관련 css */
.guestbook-container::-webkit-scrollbar {
  width: 12px; /* 스크롤바의 너비 설정 */
}

.guestbook-container::-webkit-scrollbar-thumb {
  background-color: #888; /* 스크롤바 색상 설정 */
  border-radius: 10px; /* 스크롤바의 모서리 둥글게 */
}

.guestbook-container::-webkit-scrollbar-track {
  background-color: #f1f1f1; /* 스크롤바 트랙의 색상 설정 */
}

.loading-indicator {
  text-align: center;
  padding: 10px;
  font-size: 1em;
  color: #888;
}

.message-content {
  white-space: pre-wrap;
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

/* 방명록이 없을 때 표시할 카드 스타일 */
.no-guestbook-card {
  position: relative;
  background: #f9f9f9;
  border-radius: 15px;
  padding: 20px;
  color: #888;
  max-width: 500px;
  text-align: center;
  font-size: 1em;
  margin: 20px 0; /* 위아래 간격 */
  border: 1px solid #ccc;
}
</style>
