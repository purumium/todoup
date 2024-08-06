import MainCompo from '@/components/MainCompo.vue';
import MainWrapperCompo from '@/components/MainWrapperCompo.vue';
export default [
  {
    path: '/',
    component: MainWrapperCompo,
    children: [
      {
        path: '/',
        component: MainCompo,
      },
    ],
  },
];
