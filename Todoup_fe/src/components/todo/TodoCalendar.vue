<template>
  <div class="w-100">
    <div class="d-flex justify-content-between">
      <h3>Todo Calendar</h3>
      <button type="button" class="px-2" @click="$router.push('/todo/create')">Todo 추가</button>
    </div>

    <full-calendar :options="calendarOptions"></full-calendar>
  </div>
</template>

<script>
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction'; // 메서드 사용
import koLocale from '@fullcalendar/core/locales/ko'; // 한글 로케일 데이터 가져오기

export default {
  props: ['mood'], // params를 받기 위해서 props 설정이 필요
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
        events: [
          { title: '우산 챙기기', date: '2024-08-07' },
          { title: '점심메뉴 정하기', date: '2024-08-07' },
          { title: '발표 d-day', date: '2024-08-16' },
        ],
        height: 550,
        locale: koLocale, // 한글 설정
        dayCellContent: (args) => ({ html: args.dayNumberText.replace('일', '') }), // 날짜 텍스트에서 '일'을 제거
      },
    };
  },
  methods: {
    handleMoveToTodo: function (info) {
      this.$router.push(`/todo/${info.dateStr}`);
    },
  },
};
</script>

<style scoped>
@import url('https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/6.1.15/main.min.css');
@import url('https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/6.1.15/daygrid.min.css');
</style>
