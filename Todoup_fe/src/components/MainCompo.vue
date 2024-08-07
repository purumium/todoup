<template>
  <div class="d-flex w-100" style="height: 100vh">
    <aside-compo></aside-compo>
    <modal-compo></modal-compo>
    <div class="view-wrapper">
      <router-view></router-view>
      <!-- 자식 라우트를 렌더링 : 현재 라우트에 따라 다른 컴포넌트를 렌더링 -->
    </div>
  </div>
</template>

<script>
import AsideCompo from './common/AsideCompo.vue';
import ModalCompo from './modal/ModalCompo.vue';
import { mapActions } from 'vuex';

export default {
  components: { AsideCompo, ModalCompo },
  methods: {
    ...mapActions(['toggleModal']),
  },
};
</script>

<style scoped>
.view-wrapper {
  flex: 1;
}
</style>
