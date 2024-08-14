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
      const labels = reportData.map((item) => item.month);
      const completionRates = reportData.map((item) => item.completion_rate);

      const ctx = document.getElementById('todoCompletionRateChart').getContext('2d');
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
