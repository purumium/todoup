<template>
  <div>
    <h3>MyReport</h3>
    <first-report />
    <second-report />

    <!-- 추가 리포트 컴포넌트들 -->
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
};
</script>

<style scoped>
/* 스타일 정의 */
</style>
