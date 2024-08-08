<template>
  <div>
    <h3>AvatarRoom</h3>
    <back-ground>
      <template #avatar>
        <div class="avatar-wrapper" :class="{ 'is-friend-room': !isMyRoom }">
          <avatar-character class="avatar" />
          <avatar-character v-if="!isMyRoom" class="avatar friend-avatar" />
        </div>
      </template>
      <template #guestbook>
        <guest-book />
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
  data() {
    return {
      isMyRoom: false, // 임시로 내 방 여부를 설정 (내 방이면 true, 친구 방이면 false)
    };
  },
  /*
  props: {
    ownerId: {
      type: String,
      required: true,
    },
  },
  computed: {
    isMyRoom() {
      return this.$store.state.user.id === this.ownerId;
    },
  }
  */
};
</script>

<style scoped>
.avatar-wrapper {
  display: flex;
  justify-content: center; /* 기본 중앙 정렬 */
  align-items: center;
  width: 100%;
  height: 100%;
  /* border: 1px solid blue; 구분을 위해 테두리 추가 */
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
  /* 친구 아바타의 추가 스타일이 필요한 경우 여기에 추가 */
}
</style>
