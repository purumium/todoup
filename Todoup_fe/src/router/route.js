import LoginCompo from '@/components/login/LoginCompo.vue';
import SignupCompo from '@/components/login/SignupCompo.vue';
import MainWrapperCompo from '@/components/MainWrapperCompo.vue';
import MainCompo from '@/components/MainCompo.vue';
import HomeCompo from '@/components/HomeCompo.vue';
import TodoCalendar from '@/components/todo/TodoCalendar.vue';
import DiaryCalendar from '@/components/DiaryCalendar.vue';
import DiaryEntry from '@/components/DiaryEntry.vue';
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
              },
              {
                path: 'diary/:date',
                component: DiaryEntry,
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
