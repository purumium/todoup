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
import CreateTodo from '@/components/todo/CreateTodo.vue';
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
            path: '/room',
            component: RoomCompo,
            children: [
              {
                path: '/room',
                component: AvatarRoom,
                alias: '/room/avatarroom',
              },
              {
                path: '/room/report',
                component: MyReport,
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
