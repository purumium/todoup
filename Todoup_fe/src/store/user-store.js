import router from '@/router';
import Swal from 'sweetalert2';

const store = {
  namespaced: true,
  state() {
    return {
      user_info: {},
      profileImg: '', // 프로필 이미지를 저장하는 상태
    };
  },
  mutations: {
    login(state, payload) {
      state.user_info = payload;
      state.profileImg = `${state.user_info.avatarType}_level${state.user_info.lv}.png`; // 로그인 시에 아바타 세팅
    },
    logout(state) {
      state.user_info = {};
      state.profileImg = ''; // 로그아웃 시 프로필 이미지도 초기화
      state.user_info.lv = '';
      state.user_info.points = '';
    },
    logfail(state, payload) {
      state.user_info = payload;
    },
    updatePointsAndLevel(state, { newPoints, newLevel }) {
      // state의 user_info 속성을 업데이트함
      // 새로운 포인트와 레벨이 user_info에 저장
      state.user_info.points = newPoints;
      state.user_info.lv = newLevel;
    },
    updateProfileImg(state, newProfileImg) {
      state.profileImg = newProfileImg; // 프로필 이미지를 업데이트
    },
  },

  actions: {
    login(context, payload) {
      context.commit('login', payload);
    },
    logout(context) {
      localStorage.removeItem('user_info');
      context.commit('logout');
      router.push('/');
    },
    logfail(context, payload) {
      context.commit('logfail', payload);
    },
    updatePoints({ state, commit }, pointsToAdd) {
      let newPoints = state.user_info.points + pointsToAdd;
      let newLevel = state.user_info.lv;
      let newProfileImg = state.profileImg;
      const avatarType = state.user_info.avatarType;
      let levelChanged = false; // 레벨 변경 여부를 추적하기 위한 변수

      // 레벨 업
      if (newPoints >= 100) {
        newLevel += 1;
        newPoints -= 100; // 포인트가 100이상이면, 레벨 up후, 포인트 감소
        levelChanged = true;

        if (newLevel <= 5) {
          // 레벨 올라갈 때, 프로필 이미지 변경
          newProfileImg = `${avatarType}_level${newLevel}.png`;
        }
      }
      // 레벨 다운
      if (newPoints < 0 && 1 < newLevel) {
        newLevel -= 1;
        newPoints += 100; // 포인트가 0미만이면, 레벨 down, 포인트 보정

        // 레벨 다운 시 프로필 이미지를 avatar_type에 따라 변경
        newProfileImg = `${avatarType}_level${newLevel}.png`;
      } else if (newPoints < 0 && newLevel === 1) {
        newPoints = 0; // 포인트가 0미만이고 레벨이 1일때, 포인트를 0으로 설저
        newProfileImg = `${avatarType}_level1.png`; // 레벨 1 이미지
      }
      commit('updatePointsAndLevel', { newPoints, newLevel });
      commit('updateProfileImg', newProfileImg);

      // 레벨이 변경된 경우 alert를 띄움
      if (levelChanged) {
        Swal.fire({
          text: `축하합니다! 🏆 LEVEL ${newLevel} 🏆 로 업그레이드 되었습니다.`,
          icon: 'success',
          confirmButtonText: '확인',
          confirmButtonColor: '#429f50',
        });
      }
    },
  },
  getters: {
    getUserInfo(state) {
      return state.user_info;
    },
    getProfileImg(state) {
      return state.profileImg; // 프로필 이미지 가져오기
    },
  },
};

export default store;
