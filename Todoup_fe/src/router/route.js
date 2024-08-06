import MainCompo from '@/components/MainCompo.vue';
export default [
  {
    path: '/',
    component: MainCompo,
    children: [
      {
        path: '/',
      },
    ],
  },
];
