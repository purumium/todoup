<template>
  <div class="w-100">
    <div>
      <div class="calendar-title">DIARY 캘린더</div>
    </div>
    <full-calendar v-if="calendarOptions" :options="calendarOptions"></full-calendar>
    <router-view></router-view>
  </div>
</template>

<script>
import axios from 'axios';
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid'; // 연,월,주 별로 캘린더 사용
import interactionPlugin from '@fullcalendar/interaction'; // 풀캘린더 메서드 사용
import koLocale from '@fullcalendar/core/locales/ko'; // 풀캘린더 한글 설정

export default {
  components: {
    'full-calendar': FullCalendar,
  },
  data() {
    return {
      emotions: [], // 날짜별 emotion 데이터
      calendarOptions: {
        plugins: [dayGridPlugin, interactionPlugin],
        initialView: 'dayGridMonth',
        headerToolbar: {
          left: 'title',
          center: '',
          right: 'prev today next',
        },
        events: [], // 초기 상태에서는 비어있음, `fetchEmotionDataForMonth`에서 데이터 채워짐
        eventContent: this.renderEmotionImgContent, // 셀에 이미지 추가(커스텀한 이벤트) 콘텐츠 -> fetch되고 자동 실행
        dateClick: this.handleDateClick, // 날짜를 클릭했을 때의 이벤트 핸들러(일기 작성 페이지로 이동)
        datesSet: this.handleChangeView, // 뷰가 변경될때마다 호출되는 이벤트 핸들러
        height: 550,
        locale: koLocale, // 한글 설정
        dayCellContent: (args) => ({ html: args.dayNumberText.replace('일', '') }), // 날짜 텍스트에서 '일'을 제거
      },
    };
  },
  created() {
    this.initialLoad(); // 초기 로드를 위한 메서드 호출
  },
  methods: {
    initialLoad() {
      // 초기의 캘린더 로드를 위한 메서드
      const currentDate = new Date();
      const currentYear = currentDate.getFullYear();
      const currentMonth = String(currentDate.getMonth() + 1).padStart(2, 0); // getMonth: 월을 0부터 시작, 1을 더함
      //padStart(2자리 아니면, 0을 채움)

      const yearMonth = `${currentYear}-${currentMonth}`;

      console.log('캘린더가 처음 로드: ' + yearMonth);

      this.fetchEmotionDataForMonth(yearMonth);
    },

    fetchEmotionDataForMonth(yearMonth) {
      axios
        .get('/api/diary/emotions', {
          params: { yearMonth },
        })
        .then((response) => {
          this.emotions = response.data;

          this.calendarOptions.events = this.emotions.map((data) => ({
            title: '',
            start: data.diaryDate,
            display: 'background', // 배경 이벤트
            extendedProps: {
              emotion: data.emotion,
            },
          }));
        })
        .catch((error) => {
          console.log(`fullcalendar first fetch error : ${error}`);
        });
    },

    // 일반적인 뷰 변화 이벤트를 처리하는 메서드
    handleChangeView(info) {
      // 현재 뷰의 시작 날짜 (연-월-일 형식)
      const startDate = info.view.currentStart;

      const year = startDate.getFullYear();
      const month = String(startDate.getMonth() + 1).padStart(2, '0'); // 월을 2자리로 맞추기, padStart(2자리 아니면, 0채움)
      const yearMonth = `${year}-${month}`;

      console.log('바뀐 뷰의 연도- 월 : ' + yearMonth);

      // 바뀐 뷰의 연도-월에 맞는 데이터를 가지고 옴
      this.fetchEmotionDataForMonth(yearMonth);
    },

    // 날짜 셀에 출력할 이벤트 : 이미지 출력
    renderEmotionImgContent(info) {
      const moods = info.event.extendedProps.emotion;
      var imgSrc = '';

      // img는 public/diary 폴더에 추가함
      if (moods === 'happy') {
        imgSrc = '/diary/happy.png';
      } else if (moods === 'sad') {
        imgSrc = '/diary/sad.png';
      } else if (moods === 'angry') {
        imgSrc = '/diary/angry.png';
      } else if (moods === 'exhaust') {
        imgSrc = '/diary/exhaust.png';
      }

      // imgSrc가 있는 경우에만, 날짜 셀에 표시
      if (imgSrc) {
        return {
          html: `<img src="${imgSrc}" class="calendar-img"/>`,
        };
      }
      return {};
    },

    // 날짜 셀 누르면, diary detail or create
    handleDateClick(info) {
      const event = this.calendarOptions.events.find((event) => event.start === info.dateStr); // 선택한 날짜에 해당하는 이벤트

      if (event && event.extendedProps && event.extendedProps.emotion) {
        // event가 존재하고, event에 extendedProps가 있는지 확인
        this.$router.push(`/diary/detail/${info.dateStr}`); // emotion 데이터가 있음(작성된 일기 보기)
      } else {
        this.$router.push(`/diary/create/${info.dateStr}`); // emotion 데이터가 없는 경우(일기 작성 페이지로 이동)
      }
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

.calendar-title {
  color: #2b2222b8 !important;
  font-weight: 600;
  font-size: 22px;
  display: flex;
  justify-content: center;
  padding: 7px 0;
  border-bottom: 2px solid #cfcece70;
  border-top: 2px solid #cfcece70;
  justify-content: center;
}

.fc .fc-bg-event {
  --fc-bg-event-opacity: 0.9 !important; /* 투명도를 0.8로 설정 */
  background: none !important;
  opacity: var(--fc-bg-event-opacity) !important;
}

.calendar-container {
  width: 700px !important; /* 원하는 너비로 설정 */
  margin: 0 auto !important; /* 가운데 정렬 */
}

.fc .fc-toolbar.fc-header-toolbar {
  margin: 0.5em 0 1em 0;
}

.fc .fc-toolbar-title {
  color: #544f4fe3 !important;
  font-weight: 600;
  font-size: 1em !important;
}

.fc .fc-toolbar {
  display: flex !important;
  align-items: center !important;
  justify-content: flex-end !important;
  position: relative !important;
  left: 2.5em !important;
}

.fc-direction-ltr .fc-toolbar > * > :not(:first-child) {
  margin-left: -30px !important;
}

/* 버튼의 백그라운드 컬러를 화이트로 변경하고, 기호의 컬러를 블랙으로 변경 */
.fc-button {
  background-color: white !important;
  color: #534f4f !important;
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
</style>
