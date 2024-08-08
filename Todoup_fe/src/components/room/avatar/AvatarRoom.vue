<template>
  <div>
    <h3>AvatarRoom</h3>
    <back-ground />
    <avatar-character />
    <guest-book />
  </div>
</template>

<script>
import AvatarCharacter from './AvatarCharacter.vue';
import BackGround from './BackGround.vue';
import GuestBook from './GuestBook.vue';
export default {
  name: 'AvataRoom',
  components: { BackGround, AvatarCharacter, GuestBook },
};
</script>

<style scoped>
/* 스타일 정의 */
</style>
