<template>
  <div class="header-header">
    <img src="../../assets/logo.png" width="340" @click="goToHome" />
    <div class="header-rightBox" v-if="!userInfo.userId">
      <div class="header-InBox" @click="goToSignup">
        <img src="@/assets/logo/signup-icon.png" class="fontBox" />
      </div>
      <div class="header-InBox" @click="goToLogin">
        <img src="@/assets/logo/login-icon.png" class="fontBox" />
      </div>
    </div>
    <div class="header-rightBox" v-if="userInfo.userId">
      <div class="header-InBox" @click="goToRoom">
        <img src="@/assets/logo/room-icon.png" class="fontBox" />
      </div>
      <div class="header-InBox" @click="goToTodo">
        <img src="@/assets/logo/todo-icon.png" class="fontBox" />
      </div>
      <div class="header-InBox" @click="goToDiary">
        <img src="@/assets/logo/diary-icon.png" class="fontBox" />
      </div>
      <div class="header-InBox" @click="doLogout">
        <img src="@/assets/logo/logout-icon.png" class="fontBox" />
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
      this.$swal.fire({
        text: '로그아웃 되었습니다.',
        icon: 'success',
        confirmButtonText: '확인',
        confirmButtonColor: '#429f50',
      });
    },
    ...mapActions('user', ['logout']),
  },
};
</script>

<style scoped>
.header-header {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 3.6em 0;
}

.header-rightBox {
  display: flex;
  gap: 25px;
}

.header-InBox {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.header-InBox:hover {
  transform: scale(1.1);
}

.fontBox {
  margin-top: 10px;
  margin-bottom: 0;
  width: 55px;
  height: 55px;
  transition: color 0.25s ease-in-out;
}
.fontBox:hover {
  color: #0077ff;
}
.fontBox-Text {
  margin: 0;
}
</style>
