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

      const ctx = document.getElementById('monthlyDiaryChart').getContext('2d');
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
/* 스타일 정의 */
</style>
