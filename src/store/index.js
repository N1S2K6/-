import vue from 'vue'
import Vuex from 'vuex'
import router,{resetRouter} from "@/router";
import createPersistedState from 'vuex-persistedstate'
vue.use(Vuex)

function addNewRoute(menuList) {
  console.log(menuList)
  let routes = router.options.routes
  console.log(routes)
  routes.forEach(routeItem=>{
    if (routeItem.path=='/Index') {
      menuList.forEach(menu=>{
        let childRoute = {
            path:'/'+menu.menuClick,
            name:menu.manuName,
            meta:{
              title:menu.manuName
            },
            component: () => import('../components/'+menu.menuComponent)
          }

          routeItem.children.push(childRoute)
      })

      resetRouter()
      router.addRoutes(routes)
    }
  })
}

//会用就行
export default new Vuex.Store({
  state:{
    menu:[]
  },
  mutations:{
    setMenu(state,menuList) {
      state.menu = menuList
      //添加路由
      addNewRoute(menuList)
    }
  },
  getters:{
    getMenu(state){
      return state.menu
    }
  },
  //持久化存储，菜单刷新不消失：安装插件:npm i vuex-persistedstate 并在上面导入
  //但是不解决路由消失问题
  plugins: [createPersistedState()]
})
