<template>
  <div class="w-100">
    <div class="diary-wrapper">
      <button @submit="submitDiary" class="diary-btn">일기 끝!</button>
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
            <label for="sunny">
              <input type="radio" id="sunny" value="sunny" v-model="weather" />
              <font-awesome-icon :icon="['fas', 'sun']" class="fontBox" />
            </label>
            <label for="cloudy">
              <input type="radio" id="cloudy" value="cloudy" v-model="weather" />
              <font-awesome-icon :icon="['fas', 'cloud']" class="fontBox" />
            </label>
            <label for="rainy">
              <input type="radio" id="rainy" value="rainy" v-model="weather" />
              <font-awesome-icon :icon="['fas', 'cloud-showers-heavy']" class="fontBox" />
            </label>
            <label for="snowy">
              <input type="radio" id="snowy" value="snowy" v-model="weather" />
              <font-awesome-icon :icon="['fas', 'snowflake']" class="fontBox" />
            </label>
          </td>
        </tr>
        <tr>
          <th colspan="1">오늘의 기분</th>
          <td colspan="4">
            <label for="happy">
              <input type="radio" id="happy" value="happy" v-model="mood" />
              <font-awesome-icon :icon="['fas', 'smile']" class="fontBox" />
            </label>
            <label for="sad">
              <input type="radio" id="sad" value="sad" v-model="mood" />
              <font-awesome-icon :icon="['fas', 'frown']" class="fontBox" />
            </label>
            <label for="angry">
              <input type="radio" id="angry" value="angry" v-model="mood" />
              <font-awesome-icon :icon="['fas', 'angry']" class="fontBox" />
            </label>
            <label for="neutral">
              <input type="radio" id="neutral" value="neutral" v-model="mood" />
              <font-awesome-icon :icon="['fas', 'meh']" class="fontBox" />
            </label>
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
            <textarea v-model="diaryContent" class="textarea-diary"></textarea>
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
      diaryContent: '', // 다이어리 작성 내용
      weather: '',
      mood: '',
      imgData: null, // 추가한 사진
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
    // 숨겨진 파일 입력 요소를 클릭하여 + 버튼 눌렀을 때 파일 선택 대화 상자를 표시
    triggerBtnFileUpload() {
      this.$refs.fileInput.click(); // + 버튼을 누르면, ref=fileInput이 클릭됨
    },
    // 이미지 미리보기 설정: 파일을 읽어서 img url로 생성
    onFileChange(e) {
      // 파일을 선택(파일 입력 요소의 변화가 발생)하면 호출
      const file = e.target.files[0]; // 선택한 파일 가져오기

      // onload 이벤트 핸들러: 파일이 성공적으로 읽혀지면 호출
      if (file) {
        const reader = new FileReader(); // 1. 파일을 읽기 위한 객체

        // 3. 파일이 성공적으로 읽혀지면, onload 이벤트 핸들러 발생해 읽은 데이터 처리
        reader.onload = (e) => {
          this.imgData = e.target.result; // 읽은 파일의 데이터 URL이 포함된 속성
          this.file = file; // 실제 파일 객체 저장(DB저장시 사용)
        };

        // 2. 파일을 데이터 URL 형식으로 읽기 시작(사진 미리보기를 위해 url로 변경)
        reader.readAsDataURL(file);
      }
    },
    submitDiary() {
      // FormData: 이미지 파일을 포함해서 전송시 사용
      const diaryData = new FormData();
      diaryData.append('diaryDate', this.date);
      diaryData.append('emotion', this.mood);
      diaryData.append('weather', this.weather);
      diaryData.append('content', this.diaryContent);

      // 파일이 선택된 경우에만 데이터 추가
      if (this.file) {
        diaryData.append('imgUrl', this.file);
      }

      axios
        .post('/api/diary/insert', diaryData, {
          header: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then((response) => {
          console.log('Diary saved successfully:', response.data);
          // 일기를 저장한 후, diarycalendar로 이동
          this.$router.push('/diary');
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

label {
  margin-right: 10px;
}

.photo-upload {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
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

.fontBox {
  margin-left: 5px;
  width: 13px;
  height: auto;
  transition: color 0.25s ease-in-out;
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
