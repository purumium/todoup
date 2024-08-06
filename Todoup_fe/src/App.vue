<template>
  <div style="width: 1024px; height: 100vh; margin: 0 auto">
    <header-compo></header-compo>
    <main-compo></main-compo>
    <footer-compo></footer-compo>
  </div>
</template>

<script>
import HeaderCompo from './components/common/HeaderCompo.vue';
import MainCompo from './components/MainCompo.vue';
import FooterCompo from './components/common/FooterCompo.vue';

export default {
  name: 'App',
  components: { HeaderCompo, MainCompo, FooterCompo },
};
</script>

<style></style>
