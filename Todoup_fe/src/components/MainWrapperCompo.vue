<template>
  <div>
    <!-- login, signup, main 페이지 자리 -->
    <router-view></router-view>
  </div>
</template>

<script>
import LoginCompo from './login/LoginCompo.vue';
import SignupCompo from './login/SignupCompo.vue';

export default {
  name: 'MainWrapper',
  components: [LoginCompo, SignupCompo],
};
</script>

<style scoped></style>
