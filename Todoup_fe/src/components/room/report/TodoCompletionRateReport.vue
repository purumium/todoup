<template>
  <div>
    <h4>월별 할일 달성률</h4>
    <canvas id="todoCompletionRateChart"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from 'chart.js';
import axios from 'axios';

Chart.register(...registerables);

export default {
  name: 'TodoCompletionRateReport',
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
        const response = await axios.get('/api/report/todo-completion-rate', {
          params: {
            userId: this.loginId,
          },
        });
        return response.data;
      } catch (error) {
        console.error('Error fetching todo completion rate data:', error);
        return { labels: [], completionRates: [] }; // 에러 발생 시 빈 데이터 반환
      }
    },
    async renderChart() {
      const reportData = await this.fetchReportData();
      // 필요한 데이터만 추출
      const labels = reportData.map((item) => item.MONTH);
      const completionRates = reportData.map((item) => item.COMPLETION_RATE);

      const canvas = document.getElementById('todoCompletionRateChart');
      const ctx = document.getElementById('todoCompletionRateChart').getContext('2d');

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
        type: 'line',
        data: {
          labels: labels, // 월별 레이블
          datasets: [
            {
              label: '달성률 (%)',
              data: completionRates, // 달성률 데이터
              backgroundColor: 'rgba(153, 102, 255, 0.2)',
              borderColor: 'rgba(153, 102, 255, 1)',
              borderWidth: 1,
            },
          ],
        },
        options: {
          scales: {
            y: {
              beginAtZero: true,
              max: 100,
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
