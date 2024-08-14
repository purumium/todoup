<template>
  <div>
    <h3>MyReport</h3>
    <div v-if="!isMyRoom">
      <p>본인의 Report만 확인 가능합니다.</p>
    </div>
    <div v-else>
      <monthly-points-report :login-id="loginId" />
      <todo-completion-rate-report :login-id="loginId" />
      <category-registration-report :login-id="loginId" />
      <category-completion-report :login-id="loginId" />
      <monthly-diary-report :login-id="loginId" />
      <!-- 추가 리포트 컴포넌트들 -->
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
    loginId: {
      type: String,
      required: true,
    },
    ownerId: {
      type: String,
      required: true,
    },
  },
  computed: {
    isMyRoom() {
      return this.loginId === this.ownerId;
    },
  },
};
</script>

<style scoped>
/* 스타일 정의 */
</style>
