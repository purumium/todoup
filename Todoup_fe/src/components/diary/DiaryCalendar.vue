<template>
  <div class="w-100">
    <div>
      <h6>나의 성장일기</h6>
      <full-calendar v-if="calendarOptions" :options="calendarOptions"></full-calendar>
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction'; // 메서드 사용
import koLocale from '@fullcalendar/core/locales/ko'; // 한글 로케일 데이터 가져오기

export default {
  // props: ['moods'], // 라우터에서 전달받은 moods 데이터를 props로 설정
  components: {
    'full-calendar': FullCalendar,
  },
  data() {
    return {
      moods: [], // 초기상태
      calendarOptions: null,
    };
  },
  created() {
    // 컴포넌트가 생성될 때, moods 데이터를 가지고 옴
    this.fetchMoods();
  },

  methods: {
    fetchMoods() {
      axios
        .get('/api/diary/moods')
        .then((response) => {
          // mood 데이터가 없더라도 빈 배열로 설정 => 데이터 없이도 캘린더는 떠야함
          this.moods = response.data || [];
          this.setupCalendarOptions();
        })
        .catch((error) => {
          // 오류가 발생해도 setupCalendarOptions를 호출하여 빈 배열로 설정된 moods를 처리
          console.error('There was an error fetching the moods:', error);
          this.setupCalendarOptions();
        });
    },

    // 캘린더 생성시 필요한 데이터 설정
    setupCalendarOptions() {
      this.calendarOptions = {
        plugins: [dayGridPlugin, interactionPlugin],
        initialView: 'dayGridMonth',
        headerToolbar: {
          left: 'title',
          center: '',
          right: 'prev today next',
        },
        events: this.moods.map((thismood) => ({
          title: '',
          start: thismood.date,
          display: 'background', // 배경 이벤트
          extendedProps: {
            mood: thismood.mood,
          },
        })),
        eventContent: this.renderMoodImgContent, // 커스텀한 이벤트 콘텐츠(셀에 이미지 추가)
        dateClick: this.handleMoveToDiary, // dateClick 이벤트 핸들러를 추가
        height: 550,
        locale: koLocale, // 한글 설정
        dayCellContent: (args) => ({ html: args.dayNumberText.replace('일', '') }), // 날짜 텍스트에서 '일'을 제거
      };
    },

    // 날짜 셀에 출력할 이벤트 : 이미지 출력
    renderMoodImgContent(eventInfo) {
      const mood = eventInfo.event.extendedProps.mood;
      var imgSrc = '';

      // img 폴더를 public에 추가
      if (mood === 'happy') {
        imgSrc = '/diary/happy.png';
      } else if (mood === 'sad') {
        imgSrc = '/diary/sad.png';
      } else if (mood === 'angry') {
        imgSrc = '/diary/angry.png';
      } else if (mood === 'exhaust') {
        imgSrc = '/diary/exhaust.png';
      }

      console.log(imgSrc);

      if (imgSrc) {
        return {
          html: `<img src="${imgSrc}" class="calendar-img"/>`,
        };
      }
      return {};
    },

    // 날짜 셀 눌러서, 다이어리로 이동
    handleMoveToDiary(eventInfo) {
      this.$router.push(`/diary/${eventInfo.dateStr}`);
    },
  },
};
</script>

<style>
@import url('https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/6.1.15/main.min.css');
@import url('https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/6.1.15/daygrid.min.css');

.calendar-img {
  width: 65px;
  height: 65px;
  position: relative;
  top: 10px;
  left: 8px;
}

.calendar-container {
  width: 700px !important; /* 원하는 너비로 설정 */
  margin: 0 auto !important; /* 가운데 정렬 */
}

.fc-toolbar-title {
  font-size: 1em !important;
}

/* 버튼의 백그라운드 컬러를 화이트로 변경하고, 기호의 컬러를 블랙으로 변경 */
.fc-button {
  background-color: white !important;
  color: #b1aeae !important;
  border: none !important;
  font-size: 0.8em !important;
}

.fc-button:hover {
  color: black !important;
  cursor: pointer;
}

/* 버튼 클릭 후 포커스 상태에서 border 제거 */
.fc-button:focus,
.fc-button:active {
  outline: none !important;
  box-shadow: none !important;
}

/* 날짜 숫자 밑줄 삭제 */
.fc-daygrid-day-number {
  text-decoration: none !important;
}

/* 셀 안의 목표 글자 크기를 1em으로 설정 */
.fc-daygrid-day-number,
.fc-daygrid-day-events {
  font-size: 0.8em !important;
}

/* 요일 셀에서 글자 검정색으로 변경하고 밑줄 삭제 */
.fc-scrollgrid-sync-inner {
  color: black !important;
  text-decoration: none !important;
}

/* 요일 셀의 글자색을 회색으로 변경하고 밑줄 제거, 글자 크기를 1em으로 설정 */
.fc-col-header-cell-cushion {
  color: rgba(128, 128, 128, 0.637) !important;
  text-decoration: none !important;
  font-size: 0.8em !important;
}

/* 지나간 날짜의 글자색을 회색으로 변경 */
.fc-daygrid-day.fc-day-past .fc-daygrid-day-number {
  color: rgba(185, 174, 174, 0.635) !important;
}

/* 날짜 셀에 있는 날짜 텍스트의 색을 모두 검정색으로 설정 */
.fc-daygrid-day-number {
  color: black !important;
  text-decoration: none !important;
  font-size: 0.8em !important;
}

/* 일요일과 토요일의 날짜 셀 배경색을 회색으로 변경 */
.fc-day-sun,
.fc-day-sat {
  background-color: #e6dfdf3b !important;
}

/* 일요일과 토요일의 요일 셀 배경색을 흰색으로 변경 */
.fc-col-header-cell.fc-day-sun,
.fc-col-header-cell.fc-day-sat {
  background-color: white !important;
}

/* 요일 셀의 배경색을 흰색으로 변경 */
.fc-col-header-cell-cushion {
  color: rgba(184, 171, 171, 0.872) !important;
  text-decoration: none !important;
  font-size: em !important;
  background-color: white !important;
}

/* 오늘 날짜 숫자를 감싸는 동그란 원을 만들고 빨간색으로 채우기 */
.fc-day-today .fc-daygrid-day-number {
  background-color: red !important;
  color: white !important;
  border-radius: 50% !important;
  display: inline-block !important;
  width: 20px !important;
  height: 22px !important;
  line-height: 15px !important;
  text-align: center !important;
  top: 4px !important;
  left: -3px !important;
}

.fc .fc-bg-event {
  background: var(--fc-bg-event-color) important;
  opacity: var(--fc-bg-event-opacity) important;
}
</style>
