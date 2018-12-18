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
Vue.prototype.$currentUser = localStorage.getItem("current-user");

new Vue({
    router: new VueRouter({
        mode: "history",
        routes: [
            {path: "/", component: Feed, name: "Feed"},
            {path: "/signin", component: Signin, name: "Signin"},
            {path: "/my-photos", component: MyPhotos, name: "MyPhotos"},
            {path: "/*", component: NotFound, name: "NotFound"},
        ]
    }),
    render: h => h(App)
}).$mount("#app");
