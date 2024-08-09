<template>
  <div class="signupform">
    <h3>회원가입</h3>
    <form @submit.prevent="doSignUp">
      <div class="form-floating mb-3">
        <input
          type="email"
          class="form-control"
          id="floatingEmail"
          placeholder="name@example.com"
          v-model="email"
          required
        />
        <label for="floatingEmail">이메일</label>
      </div>

      <div class="form-floating mb-3">
        <input type="text" class="form-control" id="floatingNickname" placeholder="" v-model="nickname" required />
        <label for="floatingNickname">닉네임</label>
      </div>

      <div class="form-floating mb-3">
        <input
          type="password"
          class="form-control"
          id="floatingPassword"
          placeholder=""
          v-model="password"
          :class="{ 'is-valid': password === confirmPassword && confirmPassword.length > 0 }"
          required
        />
        <label for="floatingPassword">비밀번호</label>
      </div>

      <div class="form-floating mb-3">
        <input
          type="password"
          class="form-control"
          id="floatingConfirm"
          placeholder=""
          v-model="confirmPassword"
          :class="confirmPassword.length === 0 ? '' : password === confirmPassword ? 'is-valid' : 'is-invalid'"
        />
        <label for="floatingConfirm">비밀번호 확인</label>
        <div
          class="text-danger"
          v-text="
            confirmPassword.length === 0 ? '' : password === confirmPassword ? '' : '비밀번호가 일치하지 않습니다.'
          "
        ></div>
      </div>

      <div class="d-grid gap-2">
        <button class="btn btn-primary" type="submit">회원가입</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      email: '',
      password: '',
      confirmPassword: '',
      nickname: '',
    };
  },
  methods: {
    async doSignUp() {
      try {
        const response = await axios.post('/api/signup', {
          email: this.email,
          nickName: this.nickname,
          password: this.password,
        });
        alert(response.data);
        this.$router.push('/');
      } catch (error) {
        alert('회원가입에 실패 : ' + error.response.data);
      }
    },
  },
};
</script>

<style scoped>
.signupform {
  width: 400px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.signupform h3 {
  text-align: center;
  font-weight: bold;
}
</style>
