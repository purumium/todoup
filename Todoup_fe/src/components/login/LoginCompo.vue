<template>
  <div class="loginform">
    <h3>로그인</h3>
    <form @submit.prevent="doLogin">
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
import axios from 'axios';
import { mapGetters, mapActions } from 'vuex';

const checkUser = async (email, password) => {
  const response = await axios.get('/api/login', {
    params: {
      email: email,
      password: password,
    },
    withCredentials: true,
  });

  return response.data[0]; // 사용자 정보를 반환
};

export default {
  data() {
    return {
      email: '',
      password: '',
      userId: null,
    };
  },
  computed: {
    ...mapGetters({ user_info: 'user/getUserInfo' }),
  },
  methods: {
    signUpPage() {
      this.$router.push('signup');
    },
    async doLogin() {
      try {
        const userData = await checkUser(this.email, this.password);
        if (userData) {
          alert('로그인 되었습니다.');
          this.login(userData); // userData를 user-store에 저장
          this.userId = userData.userId; // userId를 data()에 저장
          console.log('로그인-vuex 스토어에 있는 값입니다.', this.user_info);
          this.$router.push('/');
        } else {
          alert('아이디 혹은 비밀번호를 확인해주세요.');
          this.login({ email: null, check: false });
          this.userId = null; // 로그인 실패 시 userId를 null로 설정
        }
      } catch (error) {
        console.error('Login error:', error);
        alert('로그인 중 오류가 발생했습니다.');
      }
    },
    ...mapActions({ login: 'user/login', logout: 'user/logout', logfail: 'user/logfail' }),
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
