<template>
  <div>
    <p>FirstReport for {{ loginId }}</p>
    <!-- 리포트 내용 -->
  </div>
</template>

<script>
export default {
  name: 'FirstReport',
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
