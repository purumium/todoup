import LoginCompo from '@/components/login/LoginCompo.vue';
import SignupCompo from '@/components/login/SignupCompo.vue';
import MainWrapperCompo from '@/components/MainWrapperCompo.vue';
import MainCompo from '@/components/MainCompo.vue';
import HomeCompo from '@/components/HomeCompo.vue';
import TodoCalendar from '@/components/todo/TodoCalendar.vue';
import DiaryCalendar from '@/components/diary/DiaryCalendar.vue';
import DiaryCreate from '@/components/diary/DiaryCreate.vue';
import DiaryDetail from '@/components/diary/DiaryDetail.vue';
import RoomCompo from '@/components/room/RoomCompo.vue';
import AvatarRoom from '@/components/room/avatar/AvatarRoom.vue';
import MyReport from '@/components/room/report/MyReport.vue';
import CreateTodo from '@/components/todo/TodoCreate.vue';
import TodoList from '@/components/todo/TodoList.vue';

export default [
  {
    path: '/',
    component: MainWrapperCompo,
    children: [
      {
        path: '/',
        component: MainCompo,
        children: [
          {
            path: '',
            component: HomeCompo,
            alias: 'calendar',
            children: [
              {
                path: '',
                component: TodoCalendar,
                alias: 'todo',
              },
              {
                path: 'todo/create',
                component: CreateTodo,
                props: true,
              },
              {
                path: 'todo/:date',
                component: TodoList,
                props: true,
              },
              {
                path: 'diary',
                name: 'DiaryCalendar',
                component: DiaryCalendar,
              },
              {
                path: 'diary/create/:date',
                component: DiaryCreate,
                props: true,
              },
              {
                path: 'diary/detail/:date',
                component: DiaryDetail,
                props: true,
              },
              {
                path: 'diary/edit/:date',
                component: DiaryCreate, // create 컴포넌트를 재사용
                props: (route) => ({
                  ...route.params,
                  isEditMode: true, // 추가적으로 편집 모드를 나타내는 플래그를 전달
                }),
              },
            ],
          },
          {
            path: '/room/:ownerId',
            component: RoomCompo,
            props: true, // URL 파라미터를 props로 전달
            children: [
              {
                path: '',
                redirect: 'avatarroom', // 기본 경로를 avatarroom으로 리다이렉트
              },
              {
                path: 'avatarroom',
                component: AvatarRoom,
                // props: true,
              },
              {
                path: 'report',
                component: MyReport,
                // props: true,
              },
            ],
          },
        ],
      },
      {
        path: 'login',
        component: LoginCompo,
      },
      {
        path: 'signup',
        component: SignupCompo,
      },
    ],
  },
];
