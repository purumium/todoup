<template>
  <div class="loginform">
    <div class="login-img">
      <img src="@/assets/login.png" alt="login" width="75px" />
    </div>
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
        <button class="btn btn-secondary" type="button" @click="signUpPage">회원가입</button>
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
          this.$swal.fire({
            text: '로그인되었습니다.',
            icon: 'success',
            confirmButtonText: '확인',
            confirmButtonColor: '#429f50',
          });
          this.login(userData); // userData를 user-store에 저장
          this.userId = userData.userId; // userId를 data()에 저장

          this.$router.push('/');
        } else {
          this.$swal.fire({
            text: '아이디 혹은 비밀번호를 확인해주세요.',
            icon: 'warning',
            confirmButtonText: '확인',
            confirmButtonColor: '#f39c12',
          });
          this.login({ email: null, check: false });
          this.userId = null; // 로그인 실패 시 userId를 null로 설정
        }
      } catch (error) {
        console.error('Login error:', error);
        this.$swal.fire({
          text: '로그인 중 오류가 발생했습니다.',
          icon: 'error',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
      }
    },
    ...mapActions({ login: 'user/login', logout: 'user/logout', logfail: 'user/logfail' }),
  },
};
</script>

<style scoped>
.loginform {
  width: 420px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid #cac8c8;
  padding: 10px 30px 30px;
  border-radius: 20px;
}

.form-floating > .form-control,
.form-floating > .form-control-plaintext {
  padding: 1rem 5.75rem;
  border-radius: 30px !important;
  height: calc(3rem + calc(var(--bs-border-width) * 2));
  min-height: calc(3rem + calc(var(--bs-border-width) * 2));
  line-height: 1.25;
}

.form-floating > label {
  position: absolute;
  font-size: 12px;
  top: 0px;
  color: gray;
  left: 10px;
  z-index: 2;
  height: 100%;
  padding: 1rem 0.75rem;
  overflow: hidden;
  text-align: start;
  text-overflow: ellipsis;
  white-space: nowrap;
  pointer-events: none;
  border: var(--bs-border-width) solid transparent;
  transform-origin: 0 0;
  transition:
    opacity 0.1s ease-in-out,
    transform 0.1s ease-in-out;
}

.btn-primary,
.btn-secondary {
  --bs-btn-color: #0c0000b0;
  font-size: 13px;
  padding: 10px 0;
  --bs-btn-bg: #ebedef;
  --bs-btn-border-color: #d2d7db;
  --bs-btn-hover-color: #fff;
  --bs-btn-hover-bg: #ffffff;
  --bs-btn-hover-border-color: #ffffff;
  --bs-btn-focus-shadow-rgb: 49, 132, 253;
  --bs-btn-active-color: #fff;
  --bs-btn-active-bg: #f0f6ff;
  --bs-btn-active-border-color: #f8f8f8;
  --bs-btn-active-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);
  --bs-btn-disabled-color: #ffffff;
  --bs-btn-disabled-bg: #ffffff;
  --bs-btn-disabled-border-color: #ffffff;
}

.btn {
  width: auto;
  height: auto;
  background-color: #e5e5e552;
  color: #544545;
  font-weight: 600;
  font-size: 13px;
}

.btn:hover {
  color: #a19696;
  background-color: #d2d7dbb0;
  border-color: #d2d7dbb0;
}

.gap-2 {
  gap: 1rem !important;
}

.login-img {
  display: flex;
  justify-content: center;
}

.login-img img {
  margin: 14px 0;
}
</style>
