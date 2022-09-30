import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'oj',
    component: () => import('../views/oj/oj'),
    redirect: "/home",
    children: [
      {
        path: 'home',
        name: 'home',
        component: () => import('../views/oj/home')
      },
      {
        path: 'problem',
        name: 'problem_list',
        component: () => import('../views/oj/problem/problem_list')
      },
      {
        path: 'problem/:id',
        name: 'problem',
        component: () => import('../views/oj/problem/problem')
      },
      {
        path: 'user_information/:id',
        name: 'user_information',
        component: () => import('../views/oj/user/information')
      },
      {
        path: 'user_setting/:id',
        name: 'user_setting',
        component: () => import('../views/oj/user/setting')
      },
    ]
  },
  {
    path: '/admin',
    name: 'admin',
    component: () => import('../views/admin/admin'),
    redirect: "/admin/home",
    children: [
      {
        path: 'home',
        name: 'admin_home',
        component: () => import('../views/admin/home')
      },

      {
        path: 'user_list',
        name: 'user_list',
        component: () => import('../views/admin/user/list'),
      },
      {
        path: 'user_update_password',
        name: "user_update_password",
        component: () => import('../views/admin/user/update_password')
      },
      {
        path: 'file_list',
        name: "file_list",
        component: () => import('../views/admin/file')
      },

    ]
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/share/login_register')
  },
  {
    path: '/404',
    name: 'not_find_404',
    component: () => import('../views/share/not_find_404')
  },

  {
    path: '*',
    component: () => import("../views/share/not_find_404"),
    hidden: true,
  }

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})



export default router
