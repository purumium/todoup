<template>
  <div>
    <div class="d-flex justify-content-between align-items-center todo-calendar-top">
      <div class="todo-calendar">TODO 캘린더</div>
      <div class="add-todo-wrap">
        <button type="button" class="px-2 add-todo" @click="$router.push('/todo/create')">
          <font-awesome-icon :icon="['fas', 'check-double']" /> TODO 추가
        </button>
      </div>
    </div>

    <full-calendar :options="calendarOptions"> </full-calendar>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import koLocale from '@fullcalendar/core/locales/ko';
import axios from 'axios';

export default {
  components: {
    'full-calendar': FullCalendar,
  },
  data() {
    return {
      calendarOptions: {
        plugins: [dayGridPlugin, interactionPlugin],
        initialView: 'dayGridMonth',
        headerToolbar: {
          left: 'title',
          center: '',
          right: 'prev today next',
        },
        dateClick: this.handleMoveToTodo,
        eventClick: this.handleEventClick, // 이벤트 클릭 시 호출될 핸들러
        events: [], // 초기 events 배열
        height: 525,
        locale: koLocale,
        dayCellContent: (args) => ({ html: args.dayNumberText.replace('일', '') }),
        datesSet: this.handleDatesSet, // 캘린더 날짜가 변경될 때 호출
        eventContent: this.renderEventContent, // 이벤트 콘텐츠 커스텀
        dayMaxEvents: 2,
      },
      currentMonth: '', // 현재 월과 연도 저장
    };
  },
  computed: {
    ...mapState('user', {
      userId: (state) => state.user_info.userId,
    }),
  },
  created() {
    this.setInitialMonth(); // 초기 월과 연도 설정
    this.fetchTodos(); // 초기 데이터를 가져옴
  },
  methods: {
    setInitialMonth() {
      const today = new Date();
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, '0'); // 현재 월을 0부터 시작하므로 1을 더함
      this.currentMonth = `${year}-${month}`;
    },
    handleMoveToTodo(info) {
      this.$router.push(`/todo/${info.dateStr}`);
    },
    handleEventClick(info) {
      const date = new Date(info.event.start); // Date 객체 생성
      date.setDate(date.getDate() + 1); // 1일 추가
      const dateStr = date.toISOString().split('T')[0]; // YYYY-MM-DD 형식으로 변환
      const todoId = info.event.extendedProps.todoId;
      this.$router.push(`/todo/${dateStr}?selectedTodoId=${todoId}`);
    },
    async fetchTodos() {
      try {
        const userId = this.userId;
        const response = await axios.get(`/api/todo/month/${this.currentMonth}`, {
          params: { userId },
        });
        const todos = response.data;

        // todos 배열을 FullCalendar의 events 배열 형식에 맞게 변환
        this.calendarOptions.events = todos.map((todo) => {
          return {
            title: todo.title,
            start: todo.start_date, // 시작 날짜 설정
            end: this.formatEndDate(todo.end_date), // 종료 날짜 설정 (포함되지 않으므로 다음 날로 설정)
            completed: todo.completed, // 완료 여부 추가
            todoId: todo.todo_id,
          };
        });
      } catch (error) {
        console.error('Error fetching todos:', error);
      }
    },
    formatEndDate(endDate) {
      // 종료 날짜를 다음 날로 설정하여 이벤트 범위에 포함되도록 함
      const date = new Date(endDate);
      date.setDate(date.getDate() + 2);
      return date.toISOString().split('T')[0]; // YYYY-MM-DD 형식으로 변환
    },
    handleDatesSet(arg) {
      const year = arg.view.currentStart.getFullYear();
      const month = String(arg.view.currentStart.getMonth() + 1).padStart(2, '0'); // getMonth() + 1으로 올바른 월 얻기
      this.currentMonth = `${year}-${month}`;
      this.fetchTodos(); // 월 설정될 때마다 할일 목록 불러오기
    },
    renderEventContent(eventInfo) {
      return {
        html: `
          <div class="todo-event ${eventInfo.event.extendedProps.completed ? 'completed' : ''}">
            <span class="todo-title">${eventInfo.event.title}</span>
          </div>
        `,
      };
    },
  },
};
</script>

<style>
.todo-calendar-top {
  color: #2b2222b8 !important;
  font-weight: 600;
  font-size: 22px;
  display: flex;
  padding: 7px 0;
  border-bottom: 2px solid #cfcece70;
  border-top: 2px solid #cfcece70;
  justify-content: center;
}

.todo-calendar {
  left: 39%;
  position: relative;
}

.add-todo-wrap {
  position: absolute;
}

.add-todo {
  position: relative;
  top: 4.2em;
  width: 105px;
  height: 30px;
  background-color: #e5e5e51f;
  color: #544545;
  border: 1px solid #1d13132e;
  font-weight: 600;
  font-size: 12px;
  border-radius: 20px;
  z-index: 99;
}

.add-todo :hover {
  cursor: pointer;
}

.todo-event {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 2px;
  border-radius: 4px;
  background-color: #d4efdf;
  border: 1px solid #f1f2f3;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  position: relative;
  cursor: pointer;
}

.todo-event.completed {
  color: #333;
  background-color: #e0e0e0;
  text-decoration: line-through;
  border: 1px solid #f1f2f3;
}

.todo-title {
  font-weight: bold;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}

.fc-daygrid-more-link.fc-more-link {
  color: #333;
  margin-top: 2px;
  font-size: 10px;
  font-weight: 500;
}

.fc-h-event {
  background-color: #d4efdf !important;
  border: none !important;
}
</style>
