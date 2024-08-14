<template>
  <div>
    <h4>카테고리별 할일 등록 비율</h4>
    <canvas id="categoryRegistrationChart"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from 'chart.js';
import axios from 'axios';
import { format } from 'date-fns';

Chart.register(...registerables);

export default {
  name: 'CategoryRegistrationReport',
  props: {
    loginId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      chartData: null,
      yearMonth: format(new Date(), 'yyyy-MM'), // 현재 날짜를 기반으로 yearMonth 계산
    };
  },
  async mounted() {
    await this.renderChart();
  },
  methods: {
    async fetchData() {
      try {
        const response = await axios.get('/api/report/category-registration-ratio', {
          params: {
            userId: this.loginId,
            yearMonth: this.yearMonth, // 계산된 yearMonth 사용
          },
        });
        return response.data;
      } catch (error) {
        console.error('Error fetching category registration data:', error);
        return null;
      }
    },
    async renderChart() {
      this.chartData = await this.fetchData();
      if (!this.chartData) return;

      const labels = this.chartData.map((item) => item.category);
      const data = this.chartData.map((item) => item.task_count);

      const ctx = document.getElementById('categoryRegistrationChart').getContext('2d');
      new Chart(ctx, {
        type: 'pie',
        data: {
          labels: labels, // 카테고리 레이블
          datasets: [
            {
              label: '등록 비율',
              data: data, // 카테고리별 등록 비율 데이터
              backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
            },
          ],
        },
        options: {
          responsive: true,
        },
      });
    },
  },
};
</script>

<style scoped>
/* 스타일 정의 */
</style>
