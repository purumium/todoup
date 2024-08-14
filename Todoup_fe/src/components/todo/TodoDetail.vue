<template>
  <div v-if="todo">
    <div v-if="changeContent">
      <div class="todo-detail">
        <div>
          <h3 class="todo-title">{{ todo.title }}</h3>
          <div class="todo-meta">
            <span class="todo-status" :class="{ completed: todo.completed }">
              <font-awesome-icon :icon="todo.completed ? 'check-circle' : 'clock'" />
              &nbsp;{{ todo.completed ? ' 완료' : ' 미완료' }}
            </span>
            <span class="todo-category d-flex align-items-center">
              <font-awesome-icon icon="tags" /> &nbsp;{{ todo.category }}
            </span>
          </div>
          <div class="todo-dates">
            <span class="todo-date">
              <font-awesome-icon icon="calendar-alt" /> 시작날짜 &nbsp;&nbsp;&nbsp;&nbsp;
              {{ formattedStartDate }}
            </span>
            <span class="todo-date">
              <font-awesome-icon icon="calendar-alt" /> 종료날짜 &nbsp;&nbsp;&nbsp;&nbsp;
              {{ formattedEndDate }}
            </span>
          </div>
          <p v-if="todo.memo !== todo.title" class="todo-memo">{{ todo.memo }}</p>
        </div>
        <div>
          <div class="button-group">
            <button type="button" class="icon-button" @click="showContent">
              <font-awesome-icon icon="edit" /> 수정
            </button>
            <button type="button" class="icon-button" @click="deleteTodo">
              <font-awesome-icon icon="trash-alt" /> 삭제
            </button>
          </div>
        </div>
      </div>
    </div>

    <div v-else>
      <div class="form-group margin-b-3">
        <label for="title">제목</label>
        <input type="text" id="title" v-model="title" class="form-control" required />
      </div>
      <div class="form-group margin-b-3">
        <label for="memo">메모</label>
        <textarea id="memo" v-model="memo" class="form-control"></textarea>
      </div>
      <div class="form-group margin-b-3">
        <label for="category">카테고리</label>
        <select id="category" v-model="category" class="form-select" required>
          <option v-for="category in categories" :key="category.id" :value="category.name">
            {{ category.name }}
          </option>
        </select>
      </div>
      <div class="form-group margin-b-3">
        <label for="startDate">시작날짜</label>
        <input type="date" id="startDate" v-model="start_date" class="form-control" required />
      </div>
      <div class="form-group margin-b-3">
        <label for="endDate">종료날짜</label>
        <input type="date" id="endDate" v-model="end_date" class="form-control" />
      </div>
      <div class="button-group">
        <button type="button" class="icon-button" @click="modifyTodo"><font-awesome-icon icon="save" /> 저장</button>
        <button type="button" class="icon-button" @click="showContent"><font-awesome-icon icon="times" /> 취소</button>
      </div>
    </div>
  </div>
  <div v-else class="no-selection">
    <p>할 일을 선택해주세요</p>
    <img :src="`/avatar/${profileImg}.png`" alt="No selection" />
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import axios from 'axios';
import categories from '@/assets/categories.json';

export default {
  props: {
    todo: {
      type: Object,
      required: false,
    },
  },
  computed: {
    ...mapState('todo', {
      todos: 'todo_info', // Vuex의 todo_info 상태를 todos로 매핑
    }),
    ...mapGetters({
      profileImg: 'user/getProfileImg', // Vuex의 profileImg 상태를 컴포넌트에 매핑
    }),
    formattedStartDate() {
      return this.formatDate(this.todo.start_date);
    },
    formattedEndDate() {
      return this.formatDate(this.todo.end_date);
    },
  },
  data() {
    return {
      todo_id: '',
      user_id: '',
      title: '',
      memo: '',
      category: '',
      start_date: '',
      end_date: '',
      categories: categories,
      changeContent: true,
    };
  },
  methods: {
    formatModDate(dateString) {
      return dateString ? dateString.split(' ')[0] : ''; // 날짜만 추출
    },
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      });
    },
    async deleteTodo() {
      if (!this.todo) return;
      const kor_time = new Date();
      const today =
        kor_time.getFullYear() +
        '-' +
        (kor_time.getMonth() + 1 < 10 ? '0' + (kor_time.getMonth() + 1) : kor_time.getMonth() + 1) +
        '-' +
        kor_time.getDate();
      try {
        // 첫 번째 알림: 삭제 확인 요청
        const result = await this.$swal.fire({
          text: 'TODO를 삭제하시겠습니까?',
          icon: 'question',
          showCancelButton: true,
          confirmButtonText: '확인',
          confirmButtonColor: '#e74c3c',
          cancelButtonText: '취소',
        });

        // 사용자가 '확인' 버튼을 클릭했을 때만 삭제 진행
        if (result.isConfirmed) {
          const isCompleted = this.todo.completed ? 1 : 0;
          await axios.delete(`/api/todo/delete/${this.todo.todo_id}`, {
            params: {
              completed: isCompleted,
              userId: this.todo.user_id,
            },
          });

          // 삭제 완료 알림
          await this.$swal.fire({
            text: 'TODO가 삭제되었습니다.',
            icon: 'success',
            confirmButtonText: '확인',
            confirmButtonColor: '#429f50',
          });
          this.$store.commit('todo/REMOVE_TODO', this.todo.todo_id);
          if (this.todo.start_date.split(' ')[0] === today) {
            this.$store.commit('todo/REMOVE_TODAY', this.todo.todo_id);
          }
          // 삭제 완료 후 이벤트 발생
          //this.$emit('todo-deleted', this.todo.todo_id);
        }
      } catch (error) {
        console.error('Error deleting todo:', error);
        this.$swal.fire({
          text: '할일 삭제 중 오류가 발생했습니다.',
          icon: 'error',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
      }
    },

    showContent() {
      this.changeContent = !this.changeContent;
      if (this.changeContent) {
        this.todo_id = '';
        this.user_id = '';
        this.title = '';
        this.memo = '';
        this.category = '';
        this.start_date = '';
        this.end_date = '';
      } else {
        this.todo_id = this.todo.todo_id;
        this.user_id = this.todo.user_id;
        this.title = this.todo.title;
        this.memo = this.todo.memo;
        this.category = this.todo.category;
        this.start_date = this.formatModDate(this.todo.start_date);
        this.end_date = this.formatModDate(this.todo.end_date);
      }
    },
    async modifyTodo() {
      if (!this.todo) return;

      try {
        await axios.patch(`/api/todo/modify/${this.todo.todo_id}`, {
          userId: this.user_id,
          title: this.title,
          memo: this.memo,
          category: this.category,
          start_date: this.start_date,
          end_date: this.end_date,
        });
        this.$swal.fire({
          text: 'TODO가 수정되었습니다.',
          icon: 'success',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
        const newTodo = {
          todo_id: this.todo_id,
          user_id: this.user_id,
          title: this.title,
          memo: this.memo,
          category: this.category,
          start_date: this.formatModDate(this.start_date),
          end_date: this.formatModDate(this.end_date),
        };

        this.$store.commit('todo/UPDATE_TODO', newTodo);

        const kor_time = new Date();
        const today =
          kor_time.getFullYear() +
          '-' +
          (kor_time.getMonth() + 1 < 10 ? '0' + (kor_time.getMonth() + 1) : kor_time.getMonth() + 1) +
          '-' +
          kor_time.getDate();
        if (this.star_time === today) {
          this.$store.commit('todo/UPDATE_TODAY', newTodo);
        }

        this.showContent();
      } catch (error) {
        console.log();
      }
    },
  },
};
</script>

<style scoped>
.todo-detail {
  color: #333;
  width: 260px;
  height: 440px;
  padding: 15px;
  box-sizing: border-box;
  overflow-y: auto;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-sizing: border-box;
}

.todo-title {
  font-size: 22px;
  overflow: none !important;
  color: #333;
  font-weight: bold;
  margin-bottom: 20px;
  white-space: normal;
  word-wrap: break-word;
}

.todo-memo {
  border: 1px solid #80808061;
  margin-top: 20px;
  height: 115px;
  max-height: 115px;
  overflow-y: auto;
  border-radius: 7px;
  padding: 10px;
  font-size: 16px;
  color: #555;
  white-space: normal;
  word-wrap: break-word;
}

.todo-meta {
  display: flex;
  justify-content: space-between;
  gap: 16px;
}

.todo-status.completed {
  color: #4caf50;
}

.todo-status {
  font-weight: bold;
  display: flex;
  align-items: center;
}

.todo-category {
  background-color: #f0f8ff;
  padding: 4px 10px;
  border-radius: 10px;
  font-size: 14px;
  color: #333;
  display: flex;
  align-items: center;
  font-weight: 500;
}

.todo-dates {
  font-size: 14px;
  color: #555;
  display: flex;
  flex-direction: column;
  gap: 12px;
  font-weight: 500;
  margin-top: 12px;
  border: 1px solid #8080804a;
  padding: 10px;
  border-radius: 10px;
}

.button-group {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
  gap: 15px;
}

.icon-button {
  cursor: pointer;
  outline: none;
  width: 70px;
  height: 35px;
  color: #544545;
  border: 1px solid #1d13132e;
  font-size: 11px;
  border-radius: 20px;
  background-color: #a4a4a41a;
  border: none;
  border-radius: 16px;
  margin-left: 12px;
}

.icon-button:hover {
  color: #0800002e;
}

.form-group > label {
  margin-bottom: 2px;
}

.no-selection {
  position: relative;
}

.no-selection img {
  max-width: 200px;
  margin-bottom: 20px;
}

.no-selection p {
  position: relative;
  top: 10em;
  text-align: center;
  padding: 10px 8px 12px 8px;
  border-radius: 25px;
  border: 2px solid #969ea442;
  font-size: 14px;
  font-weight: bold;
  color: #464040;
  background-color: #ffffff;
}

.no-selection p::after {
  content: '';
  position: absolute;
  bottom: -12px; /* 말풍선의 아래쪽에 위치하도록 */
  left: 50%; /* 가운데 정렬 */
  transform: translateX(-50%);
  border-width: 12px 10px 0 10px; /* 꼬리의 크기 */
  border-style: solid;
  background-color: white;
  border-color: #969ea442 transparent transparent transparent; /* 말풍선의 색과 일치 */
  display: block;
  width: 0;
}

.todo-add-title {
  color: #2b2222b8 !important;
  font-weight: 600;
  font-size: 22px;
  padding: 10px 0;
  text-align: center;
  border-bottom: 2px solid #cfcece70;
  border-top: 2px solid #cfcece70;
}

.margin-b-3 {
  margin-bottom: 7px !important;
}

.form-control {
  border-radius: 20px;
  display: block;
  width: 230px;
  padding: 4px 17px;
  font-size: 14px;
  font-weight: 400;
  line-height: 1.6;
  color: rgb(36, 34, 34);
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  background-color: var(--bs-body-bg);
  background-clip: padding-box;
  border: var(--bs-border-width) solid var(--bs-border-color);
  transition:
    border-color 0.15s ease-in-out,
    box-shadow 0.15s ease-in-out;
}

.mbb-3 {
  margin-bottom: 1.3rem !important;
}

.create-btn {
  width: 113px;
  height: 38px;
  padding: 4px;
  background-color: #e5e5e51f;
  color: #544545;
  border: 2px solid #1d13132e;
  font-weight: 600;
  font-size: 13px;
  border-radius: 20px;
}

.form-select {
  width: 100%;
  padding: 5px 21px;
  font-size: 1rem;
  font-weight: 400;
  line-height: 1.5;
  color: var(--bs-body-color);
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  background-color: var(--bs-body-bg);
  background-image: var(--bs-form-select-bg-img), var(--bs-form-select-bg-icon, none);
  background-repeat: no-repeat;
  background-position: right 0.75rem center;
  background-size: 16px 12px;
  border-radius: 20px;
  border: var(--bs-border-width) solid var(--bs-border-color);
  transition:
    border-color 0.15s ease-in-out,
    box-shadow 0.15s ease-in-out;
}

label {
  padding: 5px 6px 3px 5px;
  font-weight: 600;
  font-size: 14px;
  color: #2b2222b8 !important;
}
</style>
