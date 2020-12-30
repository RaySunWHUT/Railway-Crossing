import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import LingHandle from '@/views/LingHandle.vue'
import OilHandle from '@/views/OilHandle.vue'
import VillageHandle from '@/views/VillageHandle.vue'
import Navigation from '@/components/Navigation.vue'

import NotFound from '@/components/NotFound.vue';

Vue.use(VueRouter)

const routes = [

    // {
    //     path: "/ideaInfo",
    //     name: "ideaInfo",
    //     components: {
    //         default: IdeaInfo,
    //         nav: Navigation
    //     }
    // },

    {
        path: '/',
        redirect: '/Login'
    },

    {
        path: '/Login',
        name: 'login',
        components: {
            default: Login
        }
    },
    {
        path: '/Home',
        name: 'home',
        components: {
            default: Home,
            nav: Navigation
        }
    },
    {
        path: '/LingHandle',
        name: 'lingHandle',
        components: {
            default: LingHandle,
            nav: Navigation
        }

    },
    {
        path: '/VillageHandle',
        name: 'villiageHandle',
        components: {
            default: VillageHandle,
            nav: Navigation
        }
    },
    {
        path: '/OilHandle',
        name: 'OilHandle',
        components: {
            default: OilHandle,
            nav: Navigation
        }
    },

    {
        path: "/notFound",
        name: "notFound",
        component: NotFound
    },

    {
        path: "*", // 访问路径不存在时, 重定向到登录界面
        redirect: "/notFound"
    }



]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router