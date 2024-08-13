<template>
  <div class="header-header">
    <img src="../../assets/logo.png" width="340" @click="goToHome" />
    <div class="header-rightBox" v-if="!userInfo.userId">
      <div class="header-InBox" @click="goToLogin">
        <font-awesome-icon icon="fa-solid fa-right-to-bracket" class="fontBox" />
        <span class="fontBox-Text">로그인</span>
      </div>
    </div>
    <div class="header-rightBox" v-if="userInfo.userId">
      <div class="header-InBox" @click="goToRoom">
        <font-awesome-icon icon="fa-solid fa-user-plus" class="fontBox" />
        <span class="fontBox-Text">ROOM</span>
      </div>
      <div class="header-InBox" @click="goToTodo">
        <font-awesome-icon icon="fa-solid fa-user-plus" class="fontBox" />
        <span class="fontBox-Text">TODO</span>
      </div>
      <div class="header-InBox" @click="goToDiary">
        <font-awesome-icon icon="fa-solid fa-user-plus" class="fontBox" />
        <span class="fontBox-Text">DIARY</span>
      </div>
      <div class="header-InBox" @click="doLogout">
        <font-awesome-icon icon="fa-solid fa-right-to-bracket" class="fontBox" />
        <span class="fontBox-Text">LOGOUT</span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';

export default {
  computed: {
    ...mapState('user', {
      userInfo: 'user_info', // Vuex의 user_info 상태를 userInfo로 매핑
    }),
  },
  created() {
    console.log('userinfo:', this.userInfo.userId);
  },
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
    goToRoom() {
      this.$router.push(`/room/${this.userInfo.userId}/avatarroom`);
    },
    goToTodo() {
      this.$router.push('/');
    },
    goToDiary() {
      this.$router.push('/diary');
    },
    doLogout() {
      this.logout();
      alert('로그아웃 되었습니다.');
    },
    ...mapActions('user', ['logout']),
  },
};
</script>

<style scoped>
.header-header {
  width: 100%;
  display: flex;
  align-items: end;
  justify-content: space-between;
  margin: 3.6em 0;
}
.header-rightBox {
  display: flex;
  gap: 20px;
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
