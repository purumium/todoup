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

      let labels = this.chartData.map((item) => item.CATEGORY);
      let data = this.chartData.map((item) => item.TASK_COUNT);

      // 항목 묶기
      const maxItems = 4;
      if (labels.length > maxItems) {
        const otherData = data.slice(maxItems).reduce((a, b) => a + b, 0);
        labels = [...labels.slice(0, maxItems), '그 외'];
        data = [...data.slice(0, maxItems), otherData];
      }

      // 비율 계산
      const total = data.reduce((a, b) => a + b, 0);
      data = data.map((item) => ((item / total) * 100).toFixed(2));

      const canvas = document.getElementById('categoryRegistrationChart');
      const ctx = document.getElementById('categoryRegistrationChart').getContext('2d');

      // 고해상도 지원을 위해 캔버스의 크기와 비율 설정
      const devicePixelRatio = window.devicePixelRatio || 1;
      canvas.width = canvas.clientWidth * devicePixelRatio;
      canvas.height = canvas.clientHeight * devicePixelRatio;
      ctx.scale(devicePixelRatio, devicePixelRatio);

      const centerTextPlugin = {
        id: 'centerText',
        beforeDraw: function (chart) {
          const ctx = chart.ctx;
          ctx.restore();
          const fontSize = (chart.height / 280).toFixed(2);
          ctx.font = `${fontSize}em sans-serif`;
          ctx.textBaseline = 'middle';

          // 가장 높은 비율의 항목을 찾음
          const highestValue = Math.max(...data);
          const highestIndex = data.indexOf(highestValue.toString()); // .toString()으로 비교

          if (highestIndex !== -1) {
            const text = `${labels[highestIndex]}: ${highestValue}%`;
            const textX = Math.round((chart.width - ctx.measureText(text).width) / 2);
            const textY = chart.height / 2;

            ctx.fillText(text, textX, textY);
          }

          ctx.save();
        },
      };

      new Chart(ctx, {
        type: 'doughnut',
        data: {
          labels: labels, // 카테고리 레이블
          datasets: [
            {
              label: '등록 비율',
              data: data, // 카테고리별 등록 비율 데이터
              backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'],
            },
          ],
        },
        options: {
          responsive: true,
          // plugins: [centerTextPlugin], // 특정 차트에만 플러그인 적용
        },
        plugins: [centerTextPlugin], // 플러그인을 다시 명시적으로 추가
      });
    },
  },
};
</script>

<style scoped>
/* 스타일 정의 */
</style>
