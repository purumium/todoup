<template>
  <div>
    <h4>월별 일기 작성 수</h4>
    <canvas id="monthlyDiaryChart"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from 'chart.js';
import axios from 'axios';

Chart.register(...registerables);

export default {
  name: 'MonthlyDiaryReport',
  props: {
    loginId: {
      type: String,
      required: true,
    },
  },
  mounted() {
    this.renderChart();
  },
  methods: {
    async fetchReportData() {
      try {
        const response = await axios.get(`/api/report/monthly-diary-count`, {
          params: {
            userId: this.loginId,
          },
        });
        return response.data;
      } catch (error) {
        console.error('Error fetching monthly diary data:', error);
        return null;
      }
    },
    async renderChart() {
      const data = await this.fetchReportData();
      if (!data) return;

      const labels = data.map((item) => item.MONTH); // 'month' 컬럼을 사용
      const diaryCounts = data.map((item) => item.DIARY_COUNT); // 'diary_count' 컬럼을 사용

      const canvas = document.getElementById('monthlyDiaryChart');
      const ctx = document.getElementById('monthlyDiaryChart').getContext('2d');

      // 고해상도 지원을 위해 캔버스의 크기와 비율 설정
      const devicePixelRatio = 1;
      // 물리적 크기 설정
      canvas.width = canvas.clientWidth * devicePixelRatio;
      canvas.height = canvas.clientHeight * devicePixelRatio;
      // CSS 크기 설정 (화면에 보이는 크기)
      canvas.style.width = `${canvas.clientWidth}px`;
      canvas.style.height = `${canvas.clientHeight}px`;
      // 스케일 조정
      ctx.scale(devicePixelRatio, devicePixelRatio);

      new Chart(ctx, {
        type: 'bar',
        data: {
          labels: labels, // 월별 레이블
          datasets: [
            {
              label: '작성된 일기 수',
              data: diaryCounts, // 일기 작성 수 데이터
              backgroundColor: 'rgba(255, 159, 64, 0.2)',
              borderColor: 'rgba(255, 159, 64, 1)',
              borderWidth: 1,
            },
          ],
        },
        options: {
          scales: {
            y: {
              beginAtZero: true,
            },
          },
        },
      });
    },
  },
};
</script>

<style scoped>
/* h4 태그에 대한 스타일을 정의 */
h4 {
  font-size: 1.2em; /* 크기를 작게 조정 */
  font-weight: bold; /* 굵게 */
  color: #333; /* 부드러운 검정색 */
  text-align: center; /* 텍스트 중앙 정렬 */
  margin-bottom: 20px; /* 차트와의 간격 조정 */
  font-family: 'Arial', sans-serif; /* 예쁜 폰트 적용 */
}
</style>
