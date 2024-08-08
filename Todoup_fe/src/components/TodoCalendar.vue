<template>
  <div class="w-100">
    <h3>TODO Calendar</h3>
    <full-calendar :options="calendarOptions"></full-calendar>
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
        dateClick: this.handleDateClick,
        events: [
          { title: '우산 챙기기', date: '2024-08-07' },
          { title: '점심메뉴 정하기', date: '2024-08-07' },
          { title: '발표 d-day', date: '2024-08-16' },
        ],
      },
    };
  },
  methods: {
    handleDateClick: function (info) {
      alert('date click! ' + info.dateStr);
    },
  },
};
</script>

<style scoped>
@import url('https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/6.1.15/main.min.css');
@import url('https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/6.1.15/daygrid.min.css');
</style>
