<template>
  <div>
    <h3>MyReport</h3>
    <div v-if="!isMyRoom">
      <p>본인의 Report만 확인 가능합니다.</p>
    </div>
    <div v-else>
      <first-report :login-id="loginId" />
      <second-report :login-id="loginId" />
      <!-- 추가 리포트 컴포넌트들 -->
    </div>
  </div>
</template>

<script>
import FirstReport from './FirstReport.vue';
import SecondReport from './SecondReport.vue';

export default {
  name: 'MyReport',
  components: {
    FirstReport,
    SecondReport,
    // 추가 리포트 컴포넌트들
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
