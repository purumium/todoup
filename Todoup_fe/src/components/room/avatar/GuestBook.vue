<template>
  <div class="guestbook-container" ref="guestbookContainer" @scroll="handleScroll">
    <!-- <h2>{{ ownerId }}'s GuestBook</h2> -->
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
        <div class="message-content">{{ message.content }}</div>
        <div class="d-flex justify-content-between align-items-center">
          <div class="d-flex justify-content-between">
            <small>Written by: {{ message.writerId }}</small>
            <small class="date">&emsp; {{ formatRelativeDate(message.regDateAt) }}</small>
          </div>
          <div class="button-wrapper d=flex align-items-center">
            <button type="button" class="icon-button" v-if="isAuthor(message.writerId)" @click="editMessage(index)">
              <font-awesome-icon icon="edit" /> 수정
            </button>
            <button
              type="button"
              class="icon-button"
              v-if="isAuthor(message.writerId)"
              @click="confirmDeleteMessageAsWriter(message.guestbookId, index)"
            >
              <font-awesome-icon icon="trash-alt" /> 삭제
            </button>
          </div>
        </div>

        <button
          type="button"
          class="icon-button"
          v-if="isMyRoom"
          @click="confirmDeleteMessageAsOwner(message.guestbookId, index)"
        >
          <font-awesome-icon icon="trash-alt" /> 삭제
        </button>
      </div>
      <div v-else class="d-flex align-items-end">
        <textarea
          class="edit-textarea"
          v-model="message.content"
          @keydown.enter.prevent="submitEdit(index, message.guestbookId)"
        ></textarea>
        <div class="button-wrapper">
          <button type="button" class="icon-button" @click="submitEdit(index, message.guestbookId)">
            <font-awesome-icon icon="save" /> 저장
          </button>
          <button type="button" class="icon-button" @click="cancelEdit(index)">
            <font-awesome-icon icon="times" /> 취소
          </button>
        </div>
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
  watch: {
    ownerId(newOwnerId, oldOwnerId) {
      if (newOwnerId !== oldOwnerId) {
        // ownerId가 변경되면 메시지 배열과 페이지 초기화
        this.messages = [];
        this.page = 0;
        this.allDataLoaded = false;
        this.fetchGuestbookMessages(); // 새로운 방의 방명록을 로드
      }
    },
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
          // 전체 리스트를 다시 로드
          this.page = 0;
          this.allDataLoaded = false;
          this.messages = [];
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
  align-items: flex-start;
  width: 88%;
  padding-left: 10px;
  padding-bottom: 10px;
  height: 210px;
  margin-top: 15px;
  overflow-y: auto;
  padding-right: 15px;
  box-sizing: border-box;
}

.guest-book {
  height: 47%;
  display: flex;
  overflow: hidden;
  justify-content: end;
  align-items: center;
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

.new-message-form {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-top: 0rem;
  margin-bottom: 0rem;
  font-size: 14px;
  width: 485px;
  border: 1px solid #635e5e21;
  border-radius: 22px;
  background-color: #f1f2f3;
}

.new-message-form > textarea {
  width: 100%;
  height: 100px;
  padding: 14px 12px;
  border: none;
  border-top-left-radius: 20px;
  border-bottom-left-radius: 20px;
  background-color: inherit;
  margin: 0;
  border: 1px solid #635e5e21;
}

.new-message-form > button {
  color: #5b5b5b;
  border: 2px solid #635e5e21;
  border-radius: 0 20px 20px 0;
  background-color: #f1f2f3;
  height: 100px;
  margin: 0;
  padding: 0 20px;
  display: flex;
  align-items: center;
  font-size: 12px;
  justify-content: center;
  white-space: nowrap;
}

.loading-indicator {
  text-align: center;
  padding: 10px;
  font-size: 1em;
  color: #888;
}

.message-content {
  box-sizing: content-box;
  white-space: normal;
  word-wrap: break-word;
  word-break: break-word;
  overflow-wrap: break-word;
  font-size: 13px;
  margin: 3px 0px;
}

.bubble {
  position: relative;
  background: #f1f2f3;
  border-radius: 15px;
  padding: 10px 20px;
  color: #5b5b5b;
  width: 380px;
  text-align: left;
  font-size: 1em;
  margin: 14px 0;
  border: 1px solid #f9f9f9;
}

.bubble::after {
  content: '';
  position: absolute;
  border-style: solid;
  border-width: 10px 10px 10px 0;
  border-color: transparent #f9f9f9 transparent transparent;
  display: block;
  width: 0;
  z-index: 1;
  left: -10px; /* 말풍선 꼬리를 왼쪽에 배치 */
  top: 10px; /* 말풍선의 상단에서 시작 */
}

.bubble small {
  color: #888;
  font-size: 10px;
  white-space: nowrap;
}

/* 방명록이 없을 때 표시할 카드 스타일 */
.no-guestbook-card {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f1f2f3;
  border-radius: 30px;
  padding: 20px;
  color: #4e4646;
  max-width: 490px;
  text-align: center;
  font-size: 14px;
  margin: 16px -7px;
  border: 1px solid #ccc;
}
.no-guestbook-card > p {
  margin: 0;
}

.edit-textarea {
  width: 250px;
  height: 50px;
}

.button-wrapper {
  margin-left: 30px;
  box-sizing: border-box;
  white-space: nowrap;
  display: flex;
}

.icon-button {
  width: 50px;
  height: 21px;
  cursor: pointer;
  outline: none;
  color: #7e7777;
  font-size: 8px;
  border-radius: 20px;
  background: none;
  border: 1px solid #80808047;
  border-radius: 16px;
  margin-left: 5px;
  padding: 0;
  float: right;
}

.icon-button:hover {
  color: #888;
}
</style>
