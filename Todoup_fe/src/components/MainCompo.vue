<template>
  <div class="d-flex w-100" style="height: 100vh">
    <aside-compo></aside-compo>
    <view-compo></view-compo>
  </div>
</template>

<script>
import AsideCompo from './common/AsideCompo.vue';
import ViewCompo from './ViewCompo.vue';
export default {
  components: { AsideCompo, ViewCompo },
};
</script>

<style scoped></style>
