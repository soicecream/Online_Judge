import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

//解决vue路由重复导航错误
//获取原型对象上的push函数
const originalPush = VueRouter.prototype.push
//修改原型对象中的push方法
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

// 初始化路由
const routes = [
    {
        path: '/', name: 'oj', component: () => import('@/views/oj/oj'), redirect: "/home",
        children: [
            {path: 'home', name: 'home', component: () => import('@/views/oj/home')},
            {path: 'problem', name: 'problemList', component: () => import('@/views/oj/problem/problemList')},
            {path: 'problem/:id', name: 'problem', component: () => import('@/views/oj/problem/problem')},
            {path: 'information/:id', name: 'userInformation', component: () => import('@/views/oj/user/information')},
            {path: 'setting', name: 'userSetting', component: () => import('@/views/oj/user/setting')},
        ]
    },

    // {
    //     path: '/admin', name: 'admin', component: () => import('../views/admin/admin'), redirect: "/admin/home",
    //     children: [
    //         {path: 'home', name: 'admin_home', component: () => import('../views/admin/home')},
    //
    //         {path: 'user/list', name: 'user_list', component: () => import('../views/admin/user/list'),},
    //         {path: 'user/updatepassword', name: "user_update_password", component: () => import('../views/admin/user/updatePassword')},
    //
    //
    //         {path: 'role/information', name: "role_information", component: () => import('../views/admin/role/information')},
    //         {path: 'role/list', name: 'role_list', component: () => import('../views/admin/role/list'),},
    //         {path: 'role/addrole', name: 'role_add', component: () => import('../views/admin/role/addRole'),},
    //
    //         {path: 'menu/list', name: 'menu_list', component: () => import('../views/admin/menu/list'),},
    //
    //         {path: 'file/list', name: "file_list", component: () => import('../views/admin/system/fileList')},
    //
    //     ]
    // },

    {path: '/login', name: 'login', component: () => import('@/views/share/loginRegister')},
    {path: '/404', name: 'notFind404', component: () => import('@/views/share/notFind404')},

    {path: '*', component: () => import('@/views/share/notFind404')}

]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

// 重置路由
export const resetRouter = () => {
    router.matcher = new VueRouter({
        mode: 'history',
        base: process.env.BASE_URL,
        routes
    })
}

// 注意刷新页面会重置路由
export const setRoutes = () => {
    const user = localStorage.getItem("user")
    if (user) {
        // 获取当前路由对象名称数组
        const currentRouteNames = router.getRoutes().map(v => v.name)
        if (!currentRouteNames.includes('admin')) {
            const mangeRoute = {path: '/admin', name: 'admin', component: () => import('../views/admin/admin.vue'), redirect: '/admin/home', children: []}
            const menus = JSON.parse(user).menus
            if (menus) {
                menus.forEach(item => {
                    if (item.path) {
                        let itemMenu = {path: item.path, name: item.name, component: () => import('../views' + item.pagePath + '.vue')}
                        mangeRoute.children.push(itemMenu)
                    } else if (item.children.length) {
                        item.children.forEach(item => {
                            if (item.path) {
                                let itemMenu = {path: item.path, name: item.name, component: () => import('../views' + item.pagePath + '.vue')}
                                mangeRoute.children.push(itemMenu)
                            }
                        })
                    }
                })

                // 动态添加到现在的路由对象中去
                router.addRoute(mangeRoute)
            }
        }
    }
}

// 重置我就再set一次路由
setRoutes()


// 全局前置导航守卫 -- 主要用来通过跳转或取消的方式守卫导航
// to: router即将进入的路由对象
// from: 当前导航即将离开的路由
// next: Function函数，一定要调用该方法 resolve这个钩子。执行效果依赖next方法的调用参数。
//       进行管道中的一个钩子，如果执行完了，则导航状态就是confirmed(确认的); 否则就是false, 终止导航。
router.beforeEach((to, from, next) => {

    const user = localStorage.getItem("user")
    if (!to.matched.length) {
        if (user) {
            next("/404")
        } else {
            next("/login")
        }
    } else {
        if (user || to.path === '/login') {
            next()
        } else {
            next("/login")
        }
    }

})

export default router
