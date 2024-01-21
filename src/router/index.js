import VueRouter from 'vue-router';

const routes = [
  {
    path:'/',
    name:'login',
    component:()=>import('../components/Login.vue')
  },
  {
    path:'/Index',
    name:'index',
    component: () => import('../components/Index.vue'),
    children:[
      {
        path:'/Home',
        name:'home',
        meta:{
          title:'首页'
          },
        component: () => import('../components/Home.vue')
      },
      /*{
        path:'/Admin',
        name:'admin',
        meta:{
          title:'管理员管理'
        },
        component: () => import('../components/admin/AdminManage.vue')
      },
      {
        path:'/User',
        name:'user',
        meta:{
          title:'用户管理'
        },
        component: () => import('../components/user/UserManage.vue')
      },*/
    ]
  }
]

const router = new VueRouter({
  mode:'history',
  routes
})

export function  resetRouter() {
  router.matcher = new VueRouter({
    mode:'history',
    routes:[]
  }).matcher
}

// const VueRouterPush = VueRouter.prototype.push
// VueRouter.prototype.push = function push (to) {
//   return VueRouterPush.call(this. to).catch(err => err)
// }
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
  return VueRouterPush.call(this, to).catch(err => err)
}

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
// const originalPush = VueRouter.prototype.push
// VueRouter.prototype.push = function push (location) {
//   return originalPush.call(this, location).catch(err => err)
//
// }
export default router;
