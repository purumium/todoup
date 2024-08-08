<template>
  <div class="d-flex w-100" style="height: 100vh">
    <aside>
      <profile-compo />
      <today-todo />
    </aside>
    <modal-compo></modal-compo>
    <div class="view-wrapper">
      <view-compo />
    </div>
  </div>
</template>

<script>
import ProfileCompo from './common/ProfileCompo.vue';
import TodayTodo from './common/TodayTodo.vue';
import ModalCompo from './modal/ModalCompo.vue';
import { mapActions } from 'vuex';
import ViewCompo from './ViewCompo.vue';

export default {
  components: {
    ModalCompo,
    ProfileCompo,
    TodayTodo,
    ViewCompo,
  },
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
