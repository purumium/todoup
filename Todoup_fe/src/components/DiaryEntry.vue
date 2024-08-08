<template>
  <div class="diary-entry">
    <button @click="submitDiary" class="diary-btn">일기 끝!</button>
    <table class="diary-table">
      <tr>
        <td colspan="5">
          <div class="diary-nav">
            <img src="@/assets/ogudiary.png" class="diary-image" />
            <span class="write-date"> {{ formattedDate }}에 쓰는 일기 </span>
          </div>
        </td>
      </tr>
      <tr>
        <th colspan="1">오늘의 날씨</th>
        <td colspan="4">
          <input type="checkbox" id="sunny" v-model="weather.sunny" />
          <label for="sunny">맑음</label>
          <input type="checkbox" id="cloudy" v-model="weather.cloudy" />
          <label for="cloudy">흐림</label>
          <input type="checkbox" id="rainy" v-model="weather.rainy" />
          <label for="rainy">비</label>
          <input type="checkbox" id="snowy" v-model="weather.snowy" />
          <label for="snowy">눈</label>
        </td>
      </tr>
      <tr>
        <th colspan="1">오늘의 기분</th>
        <td colspan="4">
          <input type="checkbox" id="happy" v-model="mood.happy" />
          <label for="happy">기쁨</label>
          <input type="checkbox" id="sad" v-model="mood.sad" />
          <label for="sad">슬픔</label>
          <input type="checkbox" id="angry" v-model="mood.angry" />
          <label for="angry">화남</label>
          <input type="checkbox" id="neutral" v-model="mood.neutral" />
          <label for="neutral">평범</label>
        </td>
      </tr>
      <tr>
        <th colspan="5">오늘의 사진</th>
      </tr>
      <tr>
        <td colspan="5">
          <div class="photo-upload"></div>
        </td>
      </tr>
      <tr>
        <th colspan="5">오늘의 하루</th>
      </tr>
      <tr>
        <td colspan="5">
          <textarea v-model="diaryContext" class="textarea-diary"></textarea>
        </td>
      </tr>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: ['date'], // 경로에서 일기를 작성하는 date를 전달
  data() {
    return {
      diaryContext: '', // 다이어리 작성 내용
      mood: {
        // 오늘 기분
        happy: false,
        sad: false,
        angry: false,
        neutral: false,
      },
      weather: {
        // 날씨
        sunny: false,
        cloudy: false,
        rainy: false,
        snowy: false,
      },
    };
  },
  computed: {
    // date가 변경되면 날짜 포맷 변경
    formattedDate() {
      const [year, month, day] = this.date.split('-');
      return `${year}년 ${month}월 ${day}일`;
    },
  },
  methods: {
    submitDiary() {
      const diaryData = {
        date: this.date,
        mood: this.mood,
        weather: this.weather,
        diaryContext: this.diaryContext,
      };

      axios
        .post('/api/diary', diaryData)
        .then((response) => {
          console.log('Diary saved successfully:', response.data);
          // 일기를 저장한 후, 각 날짜별 mood 데이터를 가져옴
          return axios.get('/api/moods');
        })
        .then((response) => {
          const moods = response.data;
          // 가져온 mood 데이터를 diarycalendar로 전달
          this.$router.push({
            name: 'DiaryCalendar',
            params: { moods },
          });
        })
        .catch((error) => {
          console.error('There was an error saving the diary:', error);
        });
    },
  },
};
</script>

<style scoped>
.diary-entry {
  margin: 0 auto;
  max-width: 600px;
  padding: 30px;
  border: 1px solid #0000005e;
}

.diary-nav {
  display: flex;
  justify-content: start;
  align-items: center;
}

.diary-table {
  width: 100%;
  border-collapse: collapse;
}

.diary-table th,
.diary-table td {
  border: 1px solid #0000005e;
  padding: 8px;
  text-align: left;
  font-size: 12px;
}

.textarea-mood,
.textarea-diary {
  width: 100%;
  height: 100px;
  border: none;
  resize: none;
}

.write-date {
  font-size: 14px !important;
}

.photo-upload {
  width: 100%;
  height: 100px;
  border: 0px solid #000;
}

.diary-image {
  width: 180px; /* 이미지 크기를 적절하게 조정 */
  height: auto;
  margin-left: 20px;
}

.diary-btn {
  width: 25%;
  height: 30px;
  background-color: #e5e5e51f;
  color: #544545;
  border: 1px solid #5445458c;
  font-weight: 600;
  font-size: 12px;
  border-radius: 7px;
  margin-bottom: 10px;
}
</style>
