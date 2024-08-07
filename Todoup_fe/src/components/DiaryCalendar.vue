<template>
  <div class="w-100">
    <h3>Diary Calendar</h3>
    <full-calendar :options="calendarOptions" @dateClick="handleDateClick"></full-calendar>
  </div>
</template>

<script>
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';

export default {
  components: {
    'full-calendar': FullCalendar,
  },
  data() {
    return {
      calendarOptions: {
        plugins: [dayGridPlugin],
        initialView: 'dayGridMonth',
        events: [
          { title: '목표 달성!', date: '2024-08-07' },
          { title: 'ㅠㅠ 실패', date: '2024-08-08' },
          { title: '성공의 어머니', date: '2024-08-16' },
        ],
      },
    };
  },
  methods: {
    handleDateClick(info) {
      console.log('handledateclick');
      alert(`Date Clicked : ${info.dateStr}`);
    },
  },
};
</script>

<style scoped>
@import url('https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/6.1.15/main.min.css');
@import url('https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/6.1.15/daygrid.min.css');
</style>
