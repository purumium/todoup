<template>
  <div>
    <back-ground>
      <template #avatar>
        <div class="avatar-wrapper" :class="{ 'is-friend-room': !isMyRoom }">
          <avatar-character
            v-if="loginUserInfo"
            :profile-img="loginUserProfileImg"
            :lv="loginUserInfo.lv"
            :points="loginUserInfo.points"
            :nick-name="loginUserInfo.nickName"
            class="avatar"
          />
          <avatar-character
            v-if="!isMyRoom && ownerUserInfo"
            :profile-img="ownerUserInfo.profileImg"
            :lv="ownerUserInfo.lv"
            :points="ownerUserInfo.points"
            :nick-name="ownerUserInfo.nickName"
            class="avatar friend-avatar"
          />
        </div>
      </template>
      <template #guestbook>
        <guest-book :login-id="loginId" :owner-id="ownerId" />
      </template>
    </back-ground>
  </div>
</template>

<script>
import AvatarCharacter from './AvatarCharacter.vue';
import BackGround from './BackGround.vue';
import GuestBook from './GuestBook.vue';

export default {
  name: 'AvataRoom',
  components: { BackGround, AvatarCharacter, GuestBook },
  props: {
    loginId: {
      type: String,
      required: true,
    },
    ownerId: {
      type: String,
      required: true,
    },
    loginUserInfo: {
      type: Object,
      required: true,
    },
    loginUserProfileImg: {
      type: String,
      required: true,
    },
    ownerUserInfo: {
      type: Object,
      required: false, // 친구 방일 때만 필요
    },
  },
  computed: {
    isMyRoom() {
      return this.loginId === this.ownerId;
    },
  },
};
</script>

<style scoped>
.avatar-wrapper {
  display: flex;
  justify-content: center; /* 기본 중앙 정렬 */
  align-items: center;
  width: 100%;
  height: 100%;
}

.avatar-wrapper.is-friend-room {
  justify-content: space-between;
}

.avatar {
  width: 45%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.friend-avatar {
  width: 45%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
