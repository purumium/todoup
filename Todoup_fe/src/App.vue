<template>
  <div style="width: 1060px; height: 100vh; margin: 0 auto">
    <header-compo></header-compo>
    <main-wrapper-compo></main-wrapper-compo>
  </div>
</template>

<script>
import HeaderCompo from './components/common/HeaderCompo.vue';
import MainWrapperCompo from './components/MainWrapperCompo.vue';

export default {
  name: 'App',
  components: { HeaderCompo, MainWrapperCompo },
};
</script>

<style></style>
