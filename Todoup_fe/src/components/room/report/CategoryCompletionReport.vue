<template>
  <div>
    <h4>카테고리별 할일 완료 비율</h4>
    <canvas id="categoryCompletionChart"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from 'chart.js';
import axios from 'axios';
import { format } from 'date-fns';

Chart.register(...registerables);

export default {
  name: 'CategoryCompletionReport',
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
        const response = await axios.get('/api/report/category-completion-ratio', {
          params: {
            userId: this.loginId,
            yearMonth: this.yearMonth, // 계산된 yearMonth 사용
          },
        });
        return response.data;
      } catch (error) {
        console.error('Error fetching category completion data:', error);
        return null;
      }
    },
    async renderChart() {
      this.chartData = await this.fetchData();
      if (!this.chartData) return;

      let labels = this.chartData.map((item) => item.CATEGORY);
      let data = this.chartData.map((item) => item.COMPLETED_COUNT);

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

      const canvas = document.getElementById('categoryCompletionChart');
      const ctx = document.getElementById('categoryCompletionChart').getContext('2d');

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

      // 플러그인 생성
      const centerTextPlugin = {
        id: 'centerText',
        beforeDraw: function (chart) {
          const ctx = chart.ctx;
          ctx.restore();
          const fontSize = (chart.height / 280).toFixed(2);
          ctx.font = `${fontSize}em sans-serif`;
          ctx.textBaseline = 'middle';

          // 데이터에서 가장 높은 비율을 찾기
          const highestValue = Math.max(...data.map((item) => parseFloat(item)));
          const highestIndex = data.findIndex((item) => parseFloat(item) === highestValue);

          if (highestIndex !== -1) {
            const text = `${labels[highestIndex]}: ${highestValue}%`;
            const textX = Math.round((chart.width - ctx.measureText(text).width) / 2);
            const textY = chart.height / 1.7;

            ctx.fillText(text, textX, textY);
          }
          // const text = `${labels[highestIndex]}: ${highestValue}%`;

          // const textX = Math.round((chart.width - ctx.measureText(text).width) / 2);
          // const textY = chart.height / 2;

          // ctx.fillText(text, textX, textY);
          ctx.save();
        },
      };

      // 차트 생성
      new Chart(ctx, {
        type: 'doughnut',
        data: {
          labels: labels, // 카테고리 레이블
          datasets: [
            {
              label: '완료 비율',
              data: data, // 카테고리별 완료 비율 데이터
              backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'],
            },
          ],
        },
        options: {
          responsive: true,
        },
        plugins: [centerTextPlugin], // 플러그인을 이 차트에만 적용
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
