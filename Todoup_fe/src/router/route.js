import LoginCompo from '@/components/login/LoginCompo.vue';
import SignupCompo from '@/components/login/SignupCompo.vue';
import MainWrapperCompo from '@/components/MainWrapperCompo.vue';
import MainCompo from '@/components/MainCompo.vue';
import HomeCompo from '@/components/HomeCompo.vue';
import TodoCalendar from '@/components/todo/TodoCalendar.vue';
import DiaryCalendar from '@/components/diary/DiaryCalendar.vue';
import DiaryRoom from '@/components/diary/DiaryRoom.vue';
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
                //props: true, // props를 통해 params(mood값)를 전달받기 위해 설정
              },
              {
                path: 'diary/:date',
                component: DiaryRoom,
                props: true,
              },
            ],
          },
          {
            path: '/room/:ownerId',
            component: RoomCompo,
            props: true,
            children: [
              {
                path: '',
                redirect: 'avatarroom', // 기본 경로를 avatarroom으로 리다이렉트
              },
              {
                path: 'avatarroom',
                component: AvatarRoom,
                props: true, // URL 파라미터를 props로 전달
              },
              {
                path: 'report',
                component: MyReport,
                props: true,
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
