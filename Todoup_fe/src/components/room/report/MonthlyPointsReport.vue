<template>
  <div>
    <h4>월별 획득 포인트</h4>
    <canvas id="monthlyPointsChart"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from 'chart.js';
import axios from 'axios';

Chart.register(...registerables);

export default {
  name: 'MonthlyPointsReport',
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
        const response = await axios.get('/api/report/monthly-points', {
          params: {
            userId: this.loginId,
          },
        });
        return response.data;
      } catch (error) {
        console.error('Error fetching monthly points data:', error);
        return [];
      }
    },

    async renderChart() {
      const data = await this.fetchReportData();

      const canvas = document.getElementById('monthlyPointsChart');
      const ctx = document.getElementById('monthlyPointsChart').getContext('2d');

      // 고해상도 지원을 위해 캔버스의 크기와 비율 설정
      const devicePixelRatio = window.devicePixelRatio || 1;
      canvas.width = canvas.clientWidth * devicePixelRatio;
      canvas.height = canvas.clientHeight * devicePixelRatio;
      ctx.scale(devicePixelRatio, devicePixelRatio);

      // 예시: 데이터 구조가 { month: '2024-08', points: 50 } 형태라고 가정
      const labels = data.map((item) => item.MONTH); // 월별 레이블 추출
      const pointsData = data.map((item) => item.POINTS); // 포인트 데이터 추출

      new Chart(ctx, {
        type: 'bar',
        data: {
          labels: labels, // 월별 레이블
          datasets: [
            {
              label: '포인트',
              data: pointsData, // 포인트 데이터
              backgroundColor: 'rgba(75, 192, 192, 0.2)',
              borderColor: 'rgba(75, 192, 192, 1)',
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
/* 스타일 정의 */
</style>
