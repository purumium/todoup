<template>
  <div class="w-100">
    <div class="diary-wrapper">
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
          <th colspan="4">오늘의 사진</th>
          <th colspan="1">
            <!-- + 버튼 클릭시 파일 업로드를 트리거-->
            <button @click="triggerBtnFileUpload" class="file-btn">+</button>
          </th>
        </tr>
        <tr>
          <td colspan="5">
            <div class="photo-upload">
              <!-- onFileChange: 파일 선택 시 fileReader를 이용해 파일을 읽고, imgData를 저장-->
              <input type="file" @change="onFileChange" ref="fileInput" style="display: none" />
              <!-- imgData가 설정되면, src를 통해서 업로드 사진을 미리보기로 표시-->
              <img v-if="imgData" :src="imgData" class="uploaded-photo" />
            </div>
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
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: ['date'], // 경로에서 일기를 작성하는 date를 전달
  data() {
    return {
      diaryContext: '', // 다이어리 작성 내용
      imgData: null, // 추가한 사진
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
    triggerBtnFileUpload() {
      // 숨겨진 파일 입력 요소를 클릭하여 + 버튼 눌렀을 때 파일 선택 대화 상자를 표시
      this.$refs.fileInput.click(); // + 버튼을 누르면, ref=fileInput이 클릭됨
    },
    onFileChange(e) {
      // 파일을 선택(파일 입력 요소의 변화가 발생)하면 호출
      const file = e.target.files[0]; // 선택한 파일 가져오기

      // onload 이벤트 핸들러: 파일이 성공적으로 읽혀지면 호출
      if (file) {
        const reader = new FileReader(); // 1. 파일을 읽기 위한 객체

        // 3. 파일이 성공적으로 읽혀지면, onload 이벤트 핸들러 발생해 읽은 데이터 처리
        reader.onload = (e) => {
          this.imgData = e.target.result; // 읽은 파일의 데이터 URL이 포함된 속성
        };

        // 2. 파일을 데이터 URL 형식으로 읽기 시작(사진 미리보기를 위해 url로 변경)
        reader.readAsDataURL(file);
      }
    },
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
.diary-wrapper {
  margin: 0 auto;
  padding: 25px;
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
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.uploaded-photo {
  max-width: 100%;
  max-height: 100%;
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
