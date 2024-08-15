<template>
  <div class="report-wrapper">
    <div v-if="!isMyRoom">
      <p>본인의 Report만 확인 가능합니다.</p>
    </div>
    <div v-else class="report-container">
      <div class="full-width">
        <monthly-points-report :login-id="loginUserInfo.userId" />
      </div>
      <div class="full-width">
        <todo-completion-rate-report :login-id="loginUserInfo.userId" />
      </div>
      <div class="half-width-container">
        <category-registration-report :login-id="loginUserInfo.userId" />
        <category-completion-report :login-id="loginUserInfo.userId" />
      </div>
      <div class="full-width">
        <monthly-diary-report :login-id="loginUserInfo.userId" />
      </div>
    </div>
  </div>
</template>

<script>
import MonthlyPointsReport from './MonthlyPointsReport.vue';
import TodoCompletionRateReport from './TodoCompletionRateReport.vue';
import CategoryRegistrationReport from './CategoryRegistrationReport.vue';
import CategoryCompletionReport from './CategoryCompletionReport.vue';
import MonthlyDiaryReport from './MonthlyDiaryReport.vue';

export default {
  name: 'MyReport',
  components: {
    MonthlyPointsReport,
    TodoCompletionRateReport,
    CategoryRegistrationReport,
    CategoryCompletionReport,
    MonthlyDiaryReport,
  },
  props: {
    loginUserInfo: {
      type: Object,
      required: true,
    },
    ownerId: {
      type: String,
      required: true,
    },
  },
  computed: {
    isMyRoom() {
      return this.loginUserInfo.userId === this.ownerId;
    },
  },
};
</script>

<style scoped>
.report-wrapper {
  box-sizing: border-box;
}

.report-container {
  display: flex;
  flex-direction: column;
  /* gap: 100px; 컴포넌트 간의 간격 조정 */
}

.full-width {
  width: 100%;
  margin-bottom: 150px;
}

.half-width-container {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 150px;
}

.half-width-container > * {
  flex: 1 1 48%; /* 두 개의 컴포넌트를 한 줄에 배치 */
}

canvas {
  max-width: 100%; /* 차트 크기를 컨테이너에 맞추어 조정 */
  height: auto; /* 비율을 유지하면서 높이 조정 */
  display: block;
}

/* @media (max-width: 768px) {
  .half-width-container {
    flex-direction: column;
  }

  .half-width-container > * {
    flex: 1 1 100%; /* 작은 화면에서는 차트가 전체 너비를 차지하게 조정 *v/
  }
} */
</style>
