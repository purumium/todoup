<template>
  <div style="width: 1024px; height: 100vh; margin: 0 auto">
    <header-compo></header-compo>
    <main-wrapper-compo></main-wrapper-compo>
    <footer-compo></footer-compo>
  </div>
</template>

<script>
import HeaderCompo from './components/common/HeaderCompo.vue';
import MainWrapperCompo from './components/MainWrapperCompo.vue';
import FooterCompo from './components/common/FooterCompo.vue';

export default {
  name: 'App',
  components: { HeaderCompo, MainWrapperCompo, FooterCompo },
};
</script>

<style></style>
