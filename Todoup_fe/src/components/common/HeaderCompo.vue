<template>
  <div class="header-header">
    <img src="../../assets/logo.png" width="300" @click="goToHome" />
    <div class="header-rightBox">
      <div class="header-InBox" @click="goToLogin">
        <font-awesome-icon icon="fa-solid fa-right-to-bracket" class="fontBox" />
        <span class="fontBox-Text">로그인</span>
      </div>
      <div class="header-InBox" @click="goToSignup">
        <font-awesome-icon icon="fa-solid fa-user-plus" class="fontBox" />
        <span class="fontBox-Text">회원가입</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    goToHome() {
      this.$router.push('/');
    },
    goToLogin() {
      this.$router.push('/login');
    },
    goToSignup() {
      this.$router.push('/signup');
    },
  },
};
</script>

<style scoped>
.header-header {
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin: 40px auto;
}
.header-rightBox {
  display: flex;
}
.header-InBox {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}
.fontBox {
  margin-top: 10px;
  margin-bottom: 0;
  width: 35px;
  height: 35px;
  transition: color 0.25s ease-in-out;
}
.fontBox:hover {
  color: #0077ff;
}
.fontBox-Text {
  margin: 0;
}
</style>
