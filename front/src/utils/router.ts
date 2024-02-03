import { createRouter, createWebHistory } from 'vue-router'
import { RouteLocationNormalized, RouteLocationNormalizedLoaded, Router } from 'vue-router';
import localforage from 'localforage';
import signInVue from '../views/outside/signIn.vue'
import signUpVue from '../views/outside/signUp.vue'
import notFoundVue from '../views/public/notFound.vue'
import serviceUnavailableVue from '../views/public/serviceUnavailable.vue'
import testVue from '../views/public/test.vue';
import homeVue from '../views/inside/home.vue'
import articleVue from '../views/inside/article.vue';
import questionVue from '../views/inside/question.vue';
import resourceVue from '../views/inside/resource.vue';
import userVue from '../views/inside/user.vue';
import http from '../utils/axios'
import foundationVue from '../views/inside/foundation.vue'


const routes = [
  // 重定向
  {
    path: '/:pathMatch(.*)',
    redirect: '/404',
  },
  {
    path:'/',
    redirect:'/home',
  },
  {
    path:'/index',
    redirect:'/home',
  },
  {
    path:'/login',
    redirect:'/sign-in',
  },
  {
    path:'/register',
    redirect:'/sign-up',
  },

  // 公共
  {
    path: '/404',
    name: '404',
    component: notFoundVue,
    meta:{requiresAuth:false},
  },
  {
    path: '/503',
    name: '503',
    component: serviceUnavailableVue,
    meta:{requiresAuth:false},
  },
  {
    path: '/test',
    name: 'test',
    component:testVue ,
    meta:{requiresAuth:true},
    children: [
      {
        // 当 /user/:id/profile 匹配成功
        // UserProfile 将被渲染到 User 的 <router-view> 内部
        path: ':id',
        name: 'dongtaiceshi',
        component:notFoundVue ,
      },
    ]
  },
  

  // 站外
  {
    path: '/sign-in',
    name: 'signIn',
    component: signInVue,
    meta:{requiresAuth:false},
  },
  {
    path: '/sign-up',
    name: 'signUp',
    component: signUpVue,
    meta:{requiresAuth:false},
  },
  // 站内
  {
    path: '/',
    name: 'foundation',
    component: foundationVue,
    meta:{requiresAuth:true},
    children: [
      {
        path: '/home',
        name: 'home',
        component:homeVue ,
      },
      {
        path: '/article/:id',
        name: 'article',
        component: articleVue,
      },
      {
        path: '/question/:id',
        name: 'question',
        component: questionVue,
      },
      {
        path: '/resource/:id',
        name: 'resource',
        component: resourceVue,
      },
      {
        path: '/:username',
        name: 'user',
        component: userVue,
      },
    ]
  },
 
  
]


const router = createRouter({
  history: createWebHistory(),
  routes
})

const authDB = localforage.createInstance({
  name: "authDB"
});






async function redirectToSignIn(router: Router) {
  router.push('/sign-in');
}

async function redirectToUsername(router: Router,username:string) {
  router.push('/'+username);
}

async function handleRefreshJWT(router: Router, authDB: any, http: any, refreshJWT: string) {
  try {
    const res = await http.post("/auth/refresh-jwt", refreshJWT);
    if (res.code === 0) {
      console.log("refresh校验成功，获取到新的access", res);
      await authDB.setItem("accessJWT", res.data);
      handleAccessJWT(router, authDB,http,res.data,refreshJWT)
    } else {
      console.log("refresh校验失败");
      clearJWT(authDB)
      redirectToSignIn(router);
    }
  } catch (err) {
    console.error(err);
    redirectToSignIn(router);
  }
}

async function clearJWT(authDB: any) {
  authDB.removeItem("accessJWT")
  authDB.removeItem("refreshJWT")
}

async function handleAccessJWT(router: Router, authDB: any, http: any, accessJWT: string, refreshJWT: string,toPath:string) {
  try {
    const res = await http.post("/auth/access-jwt", accessJWT);
    console.log("res:", res);
    if (res.code !== 0 || res.data === false) {
      console.log("access校验失败");
      if (refreshJWT !== null) {
        console.log("refresh不为空");
        await handleRefreshJWT(router, authDB, http, refreshJWT);
      } else {
        console.log("refresh为空");
        redirectToSignIn(router);
      }
    } else {
      console.log("access校验成功");
      filter(router,accessJWT,toPath)
    }
  } catch (err) {
    console.error(err);
    redirectToSignIn(router);
  }
}

async function filter(router:Router,accessJWT:string,toPath:string) { 
  console.log('jt',accessJWT)
  if(toPath==='/mine'){
    http.post("/user/get-username",accessJWT)
    .then(res=>{
      if(res.code!=1){
        redirectToUsername(router,res.data)
        console.log("hg",res)
      }
    })
    .catch(err =>{
      console.log(err)
  })
  }
}

router.beforeEach(async (to: RouteLocationNormalized, from: RouteLocationNormalizedLoaded) => {
  const fromPath = from.path;
  const toPath = to.path;
  console.log("from:", fromPath);
  console.log("to:", toPath);

  

  const requiresAuth = to.matched.some(record => record.meta.requiresAuth !== false);
  if (requiresAuth) {
    const accessJWT = await authDB.getItem("accessJWT");
    const refreshJWT = await authDB.getItem("refreshJWT");
    if (accessJWT === null) {
      console.log("access为空");
      if (refreshJWT === null) {
        console.log("refresh为空");
        await redirectToSignIn(router);
      } else {
        console.log("refresh不为空");
        await handleRefreshJWT(router, authDB, http, refreshJWT);
      }
    } else {
      console.log("access不为空");
      await handleAccessJWT(router, authDB, http, accessJWT, refreshJWT,toPath);
    }
  }
});


export default router