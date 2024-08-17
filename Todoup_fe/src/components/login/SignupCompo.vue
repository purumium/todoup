<template>
  <div class="signupform">
    <div class="login-img">
      <img src="@/assets/register.png" alt="login" width="75px" />
    </div>
    <form @submit.prevent="doSignUp">
      <div class="form-floating mb-3">
        <input
          type="email"
          class="form-control"
          id="floatingEmail"
          placeholder="name@example.com"
          v-model="email"
          @keyup="isValidEmail"
          :class="email.length == 0 ? '' : checkEmail"
          required
        />
        <label for="floatingEmail">이메일</label>
        <div class="text-danger" v-text="checkEmail != 'is-invalid' ? '' : '이미 사용 중인 이메일입니다.'"></div>
      </div>

      <div class="form-floating mb-3">
        <input
          type="text"
          class="form-control"
          id="floatingNickname"
          placeholder=""
          v-model="nickname"
          @keyup="isValidNickname"
          :class="nickname.length == 0 ? '' : checkNickname"
          required
        />
        <label for="floatingNickname">닉네임</label>
        <div class="text-danger" v-text="checkNickname != 'is-invalid' ? '' : '이미 사용 중인 닉네임입니다.'"></div>
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
      checkEmail: '',
      checkNickname: '',
    };
  },

  methods: {
    async isValidEmail() {
      try {
        console.log(this.email);
        const response = await axios.get('api/checkEmail', {
          params: { email: this.email },
        });
        console.log('응답 : ', response);
        console.log('응답 데이터: ', response.data);
        if (response.data) {
          this.checkEmail = 'is-valid';
        } else {
          this.checkEmail = 'is-invalid';
        }
        return response.data;
      } catch (error) {
        this.$swal.fire({
          text: '회원가입에 실패 : ' + error.response.data,
          icon: 'error',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
      }
    },
    async isValidNickname() {
      try {
        console.log(this.email);
        const response = await axios.get('api/checkNickname', {
          params: { nickname: this.nickname },
        });
        if (response.data) {
          this.checkNickname = 'is-valid';
        } else {
          this.checkNickname = 'is-invalid';
        }
        return response.data;
      } catch (error) {
        this.$swal.fire({
          text: '회원가입에 실패 : ' + error.response.data,
          icon: 'error',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
      }
    },
    async doSignUp() {
      if (this.password == this.confirmPassword && this.isValidEmail && this.isValidNickname) {
        try {
          const response = await axios.post('/api/signup', {
            email: this.email,
            nickName: this.nickname,
            password: this.password,
          });
          this.$swal.fire({
            text: response.data,
            icon: 'success',
            confirmButtonText: '확인',
            confirmButtonColor: '#429f50',
          });
          this.$router.push('/');
        } catch (error) {
          this.$swal.fire({
            text: '회원가입에 실패 : ' + error.response.data,
            icon: 'error',
            confirmButtonText: '확인',
            confirmButtonColor: '#429f50',
          });
        }
      } else {
        this.$swal.fire({
          text: '회원정보를 다시 확인해주세요.',
          icon: 'error',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
      }
    },
  },
};
</script>

<style scoped>
.signupform {
  width: 420px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid #cac8c8;
  padding: 10px 30px 30px;
  border-radius: 20px;
}
.signupform h3 {
  text-align: center;
  font-weight: bold;
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

.form-control {
  padding: 1rem 5.75rem;
  border-radius: 30px !important;
  height: calc(3rem + calc(var(--bs-border-width) * 2));
  min-height: calc(3rem + calc(var(--bs-border-width) * 2));
  line-height: 1.25;
  display: block;
  width: 100%;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  font-weight: 400;
  line-height: 1.5;
  color: var(--bs-body-color);
  -moz-appearance: none;
  appearance: none;
  background-color: var(--bs-body-bg);
  background-clip: padding-box;
  border: var(--bs-border-width) solid var(--bs-border-color);
  border-radius: var(--bs-border-radius);
  transition:
    border-color 0.15s ease-in-out,
    box-shadow 0.15s ease-in-out;
}

.login-img {
  display: flex;
  justify-content: center;
}

.login-img img {
  margin: 14px 0;
}
</style>
