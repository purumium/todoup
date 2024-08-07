import LoginCompo from '@/components/login/LoginCompo.vue';
import SignupCompo from '@/components/login/SignupCompo.vue';
import MainWrapperCompo from '@/components/MainWrapperCompo.vue';
import MainCompo from '@/components/MainCompo.vue';
import ViewCompo from '@/components/ViewCompo.vue';
import TodoCalendar from '@/components/TodoCalendar.vue';
import DiaryCalendar from '@/components/DiaryCalendar.vue';

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
            component: ViewCompo,
            children: [
              {
                path: '',
                component: TodoCalendar,
                alias: 'todo',
              },
              {
                path: 'diary',
                component: DiaryCalendar,
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
