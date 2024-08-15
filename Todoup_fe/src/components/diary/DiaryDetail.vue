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
                <div v-for="line in computedLines" :key="line" class="diary-line">
                  <!-- computedLines: 최소 5줄을 반환하거나, 실제 줄 수가 5줄을 초과하면 그 줄 수를 반환 -->
                  <span>{{ getContentLine(line) }}</span>
                  <!-- 각 줄에 대해 getContentLine(line) 메서드를 호출하여 텍스트를 가져옴-->
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
import { mapState } from 'vuex';
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
    };
  },
  created() {
    this.diaryDate = this.date; // props로 받은 date를 diaryDate에 바인딩
    this.fetchDiaryDetail();
  },
  computed: {
    // computed의 속성이기 때문에, 이를 필요로 하는 곳에서 자동으로 참조
    ...mapState('user', {
      // userId 가지고 오기
      userId: (state) => state.user_info.userId,
    }),
    formattedDate() {
      // date가 변경되면 날짜 포맷 변경
      const [year, month, day] = this.date.split('-');
      return `${year}년 ${month}월 ${day}일`;
    },
    computedLines() {
      // 5줄 미만이면, 5줄
      // 5줄 초과하면 실제 내용에 해당하는 줄 개수(6개 이상)
      const minimumLines = 5;
      const contentLine = this.content.split('\n').length; // 줄바꿈 기준으로 줄 수 몇개?

      return Math.max(minimumLines, contentLine);
    },
  },
  methods: {
    fetchDiaryDetail() {
      const userId = this.userId;
      axios
        .get(`/api/diary/detail/${this.diaryDate}`, {
          params: { userId },
        })
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
      const ArrayByContentLine = this.content.split('\n'); // 줄바꿈을 기준으로 잘라서, 1줄씩 배열로 담김

      // 배열은 0부터 시작
      const result = ArrayByContentLine[lineIndex - 1] ? ArrayByContentLine[lineIndex - 1] : '';
      return result;
    },
    deleteDiary() {
      // alert창
      this.$swal
        .fire({
          text: `${this.diaryDate}의 일기를 삭제하시겠습니까?`,
          icon: 'info',
          showCancelButton: true, // 취소버튼 추가
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          confirmButtonColor: '#429f50',
        })
        .then((result) => {
          if (result.isConfirmed) {
            const userId = this.userId;
            axios
              .delete(`/api/diary/delete/${this.diaryDate}`, {
                params: { userId },
              })
              .then((response) => {
                // 포인트를 down 로직 추가
                let pointsToAdd = 0;
                if (response.data) {
                  pointsToAdd = -5; // 삭제시 -5 포인트
                }
                this.$store.dispatch('user/updatePoints', pointsToAdd);

                // alert창
                this.$swal.fire({
                  text: response.data, // 서버에서 반환된 메시지
                  icon: 'info',
                  confirmButtonText: '확인',
                  confirmButtonColor: '#429f50',
                });

                this.$router.push('/diary'); // 다이어리 캘린더로 이동
              })
              .catch((error) => {
                this.$swal.fire({
                  text: error.response.data, // 서버에서 반환된 오류 메시지
                  icon: 'error',
                  confirmButtonText: '확인',
                  confirmButtonColor: '#429f50',
                });
              });
          } else {
            // "취소" 버튼을 누른 경우 아무 작업도 하지 않음
            this.$swal.fire('취소', '삭제가 취소되었습니다.', 'info');
          }
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
.w-100 {
  width: 100% !important;
  height: 594px !important;
}

#diary-detail-container {
  border-radius: 12px;
  border: 2px solid #635e5e21;
}
.diary-wrapper {
  margin: 18px 20px 28px 20px;
}

.diary-table {
  width: 100%;
}

.diary-table th,
.diary-table td {
  padding: 13px 10px;
}

.diary-header {
  position: relative;
  display: flex;
  justify-content: space-between;
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
  width: 6em;
  height: 2.5em;
  background-color: #e5e5e51f;
  color: #544545;
  border-radius: 12px;
  border: 2px solid #635e5e21;
  font-weight: 600;
  font-size: 12px;
  border-radius: 20px;
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
  margin: 0 4px 0 10px;
  font-weight: 600;
}

.photo-grid {
  border: 2px solid #635e5e21;
  border-radius: 8px;
  max-width: 570px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 10px;
}

.upload-photo {
  max-width: 80%;
  height: 70%;
}

.diary-content-container {
  position: relative;
  width: 100%;
}

.diary-content {
  width: 100%;
  max-height: 180px; /* 5줄의 높이만큼 설정 */
  overflow-y: auto; /* 컨텐츠가 5줄을 넘을 경우 스크롤 생성 */
  padding: 0 10px; /* 스크롤바와의 간격을 위해 padding 추가 */
}

.diary-line {
  position: relative;
  border-bottom: 2px solid #635e5e21;
  height: 35px; /* 각 줄의 높이를 설정 */
  line-height: 37px; /* 줄 간격을 설정 */
  font-size: 14px;
  white-space: pre-wrap;
}

.diary-line span {
  display: block;
  margin-left: 5px;
}
</style>
