import MainWrapperCompo from '@/components/MainWrapperCompo.vue';
import MainCompo from '@/components/MainCompo.vue';
import ViewCompo from '@/components/ViewCompo.vue';
import CalendarCompo from '@/components/CalendarCompo.vue';
import TodoCalendar from '@/components/TodoCalendar.vue';
import DiaryCalendar from '@/components/DiaryCalendar.vue';

export default [
  {
    path: '/',
    component: MainWrapperCompo,
    children: [
      {
        path: '',
        component: MainCompo,
        children: [
          {
            path: '',
            component: ViewCompo,
            children: [
              {
                path: '',
                component: CalendarCompo,
                alias: 'calendar',
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
        ],
      },
    ],
  },
];
