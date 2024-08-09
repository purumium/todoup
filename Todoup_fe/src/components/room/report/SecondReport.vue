<template>
  <div>
    <p>SecondReport for {{ loginId }}</p>
    <!-- 리포트 내용 -->
  </div>
</template>

<script>
export default {
  name: 'SecondReport',
  props: {
    loginId: {
      type: String,
      required: true,
    },
  },
};
</script>

<style scoped>
/* 스타일 정의 */
</style>
