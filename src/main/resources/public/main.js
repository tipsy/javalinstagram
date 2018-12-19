import App from "./vue/App.js";
import Feed from "./vue/views/Feed.js"
import Signin from "./vue/views/Signin.js"
import MyPhotos from "./vue/views/MyPhotos.js"
import NotFound from "./vue/views/NotFound.js";

Object.assign(Vue.prototype.$vuetify.theme, {primary: "#bc50a4"});

Vue.prototype.$injectStyle = (styleText) => {
    const styleId = /id="([^;]+)"/.exec(styleText)[1];
    if (document.querySelector(`style#${styleId}`) === null) {
        document.head.insertAdjacentHTML("beforeEnd", styleText)
    }
};

Vue.prototype.$eventBus = new Vue();

const router = new VueRouter({
    mode: "history",
    routes: [
        {path: "/", component: Feed, name: "Feed"},
        {path: "/sign-in", component: Signin, name: "Signin"},
        {path: "/my-photos", component: MyPhotos, name: "MyPhotos"},
        {path: "/*", component: NotFound, name: "NotFound"},
    ],
});

router.beforeEach((to, from, next) => {
    if (localStorage.getItem("current-user") !== null || to.name === "Signin") {
        return next();
    }
    router.push({name: "Signin"}); // redirect to signin
});

new Vue({
    router: router,
    data: { // shared mutable state
        currentUser: localStorage.getItem("current-user"),
    },
    render: h => h(App)
}).$mount("#app");

