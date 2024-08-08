<template>
  <div class="loginform">
    <h3>로그인</h3>
    <form @submit="doLogin">
      <div class="form-floating mb-3">
        <input
          type="email"
          class="form-control"
          id="floatingInput"
          placeholder="name@example.com"
          v-model="email"
          required
        />
        <label for="floatingInput">이메일</label>
      </div>
      <div class="form-floating mb-3">
        <input
          type="password"
          class="form-control"
          id="floatingPassword"
          placeholder="Password"
          v-model="password"
          required
        />
        <label for="floatingPassword">비밀번호</label>
      </div>
      <div class="d-grid gap-2">
        <button class="btn btn-primary" type="submit">로그인</button>
        <button class="btn btn-secondary" type="button" @click="signUpPage">회원 가입</button>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      email: '',
      password: '',
    };
  },
  methods: {
    signUpPage() {
      this.$router.push('signup');
    },
    doLogin() {
      console.log(this.email, this.password);
      alert('로그인 구현해야됨');
    },
  },
};
</script>

<style scoped>
.loginform {
  width: 400px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.loginform h3 {
  text-align: center;
}
.loginform h3 {
  text-align: center;
  font-weight: bold;
}
</style>
