<template>
  <div v-if="todo">
    <h3>{{ todo.title }}</h3>
    <p>{{ todo.memo }}</p>
    <p>완료 상태: {{ todo.completed ? '완료됨' : '미완료' }}</p>
    <p>카테고리: {{ todo.category }}</p>
    <p>시작 날짜: {{ formattedStartDate }}</p>
    <p>종료 날짜: {{ formattedEndDate }}</p>
  </div>
</template>

<script>
export default {
  props: {
    todo: {
      type: Object,
      required: false,
    },
  },
  computed: {
    formattedStartDate() {
      return this.formatDate(this.todo.start_date);
    },
    formattedEndDate() {
      return this.formatDate(this.todo.end_date);
    },
  },
  methods: {
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      });
    },
  },
};
</script>

<style scoped></style>
