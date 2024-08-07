<template>
  <div class="d-flex w-100" style="height: 100vh">
    <!-- <aside-compo></aside-compo> -->
    <aside>
      <profile-compo />
      <today-todo />
    </aside>
    <modal-compo class="test"></modal-compo>
    <div class="view-wrapper">
      <router-view></router-view>
      <!-- 자식 라우트를 렌더링 : 현재 라우트에 따라 다른 컴포넌트를 렌더링 -->
    </div>
  </div>
</template>

<script>
// import AsideCompo from './common/AsideCompo.vue';
import ProfileCompo from './common/ProfileCompo.vue';
import TodayTodo from './common/TodayTodo.vue';
import ModalCompo from './modal/ModalCompo.vue';

export default {
  components: {
    // AsideCompo,
    ModalCompo,
    ProfileCompo,
    TodayTodo,
  },
};
</script>

<style scoped>
.view-wrapper {
  flex: 1;
}
</style>
