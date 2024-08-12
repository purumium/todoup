<template>
  <div class="w-100" id="diary-detail-container">
    <div class="diary-wrapper">
      <table class="diary-table">
        <tr>
          <td colspan="4">
            <div class="diary-header">
              <div class="diary-date">{{ formattedDate }}의 일기</div>
              <div class="modify-btn">
                <button @click="editDiary">수정</button>
                <button @click="deleteDiary">삭제</button>
              </div>
            </div>
          </td>
        </tr>
        <tr>
          <td colspan="4" style="text-align: center">
            <div class="weather" style="margin-right: 20px">
              <span>날씨</span>
              <span>
                <font-awesome-icon v-if="weather === 'sunny'" :icon="['fas', 'sun']" class="fontBox" />
                <font-awesome-icon v-if="weather === 'cloudy'" :icon="['fas', 'cloud']" class="fontBox" />
                <font-awesome-icon v-if="weather === 'rainy'" :icon="['fas', 'cloud-showers-heavy']" class="fontBox" />
                <font-awesome-icon v-if="weather === 'snowy'" :icon="['fas', 'snowflake']" class="fontBox" />
              </span>
            </div>
            <div class="emotion">
              <span>기분</span>
              <span>
                <font-awesome-icon v-if="emotion === 'happy'" :icon="['fas', 'smile']" class="fontBox" />
                <font-awesome-icon v-if="emotion === 'sad'" :icon="['fas', 'frown']" class="fontBox" />
                <font-awesome-icon v-if="emotion === 'angry'" :icon="['fas', 'angry']" class="fontBox" />
                <font-awesome-icon v-if="emotion === 'exhaust'" :icon="['fas', 'meh']" class="fontBox" />
              </span>
            </div>
          </td>
        </tr>
        <tr>
          <td colspan="4">
            <div class="photo-grid">
              <img v-if="imgData" :src="`/api/diary/images/${imgData}`" class="upload-photo" />
            </div>
          </td>
        </tr>
        <tr>
          <td colspan="4">
            <div class="diary-content-container">
              <div class="diary-content">
                <div v-for="line in lines" :key="line" class="diary-line">
                  <span>{{ getContentLine(line) }}</span>
                </div>
              </div>
            </div>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: ['date'], // URL 경로에서 date를 전달
  data() {
    return {
      diaryDate: '',
      weather: '',
      emotion: '',
      content: '', // 다이어리 작성 내용
      imgData: '', // 추가한 사진
      lines: 5, // 기본적으로 5줄을 보여줌
    };
  },
  created() {
    this.diaryDate = this.date; // props로 받은 date를 diaryDate에 바인딩
    this.fetchDiaryDetail();
  },
  computed: {
    formattedDate() {
      // date가 변경되면 날짜 포맷 변경
      const [year, month, day] = this.date.split('-');
      return `${year}년 ${month}월 ${day}일`;
    },
  },
  methods: {
    fetchDiaryDetail() {
      axios
        .get(`/api/diary/detail/${this.diaryDate}`)
        .then((response) => {
          const diaryData = response.data;

          this.weather = diaryData.weather;
          this.emotion = diaryData.emotion;
          this.content = diaryData.content;
          this.imgData = diaryData.imageUrl;
        })
        .catch((error) => {
          console.log('diary detail error : ' + error);
        });
    },
    getContentLine(lineIndex) {
      const lines = this.content.split('\n');
      return lines[lineIndex - 1] || '';
    },
    deleteDiary() {
      axios
        .delete(`/api/diary/delete/${this.diaryDate}`)
        .then((response) => {
          alert(response.data); // 서버에서 반환된 메시지
          this.$router.push('/diary'); // 다이어리 캘린더로 이동
        })
        .catch((error) => {
          alert(error.response.data); // 서버에서 반환된 오류 메시지
          this.$router.push('/diary'); // 다이어리 캘린더로 이동
        });
    },
    editDiary() {
      // isEditMode는 이미 라우터에서 자동으로 props로 전달
      this.$router.push(`/diary/edit/${this.diaryDate}`);
    },
  },
};
</script>

<style scoped>
#diary-detail-container {
  border: 1px solid #8080802e;
  border-radius: 12px;
}
.diary-wrapper {
  margin: 18px 20px 28px 20px;
}

.diary-table {
  width: 100%;
}

.diary-table th,
.diary-table td {
  padding: 8px 10px;
  text-align: left;
}

.diary-header {
  position: relative;
  display: flex;
  justify-content: space-between;
  /* align-items: center;
  margin-bottom: 12px; */
}

.diary-date {
  color: #2a2828f5;
  position: relative;
  top: 20px;
  left: 195px;
  font-size: 18px;
  font-weight: bold;
}

.modify-btn {
  display: flex;
  justify-content: space-between;
  gap: 12px;
}

.modify-btn button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 45px;
  height: 42px;
  border: 1px solid #8080803d;
  border-radius: 50%;
  background-color: #f0f0f097;
  font-size: 10px;
  color: #5b5b5b;
  font-weight: 600;
}

.modify-btn button:hover {
  cursor: pointer;
}

.fontBox {
  font-size: 20px;
  color: rgba(133, 175, 17, 0.89);
}

.weather,
.emotion {
  display: inline-block;
  color: #2a2828f5;
  font-size: 12px;
}

.weather :nth-of-type(1),
.emotion :nth-of-type(1) {
  margin: 0 1px 0 10px;
}

.photo-grid {
  border: 1px solid rgba(128, 128, 128, 0.239);
  border-radius: 8px;
  max-width: 600px;
  height: 220px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-photo {
  max-width: 80%;
  height: 70%;
}

.diary-content-container {
  position: relative;
  width: 100%;
  margin-top: 8px;
}

.diary-content {
  width: 100%;
  max-height: 175px; /* 5줄의 높이만큼 설정 */
  overflow-y: auto; /* 컨텐츠가 5줄을 넘을 경우 스크롤 생성 */
  padding: 0 10px; /* 스크롤바와의 간격을 위해 padding 추가 */
}

.diary-line {
  position: relative;
  border-bottom: 1px solid #00000029;
  height: 35px; /* 각 줄의 높이를 설정 */
  line-height: 35px; /* 줄 간격을 설정 */
  font-size: 14px;
  white-space: pre-wrap;
}

.diary-line span {
  display: block;
  margin-left: 5px;
}
</style>
