import NavBar from "./components/Navbar.js"

export default {
    name: "App",
    components: {NavBar},
    template: `
       <v-app>
            <v-content>
                <nav-bar></nav-bar>
                <v-container>
                    <router-view></router-view>
                </v-container>
            </v-content>
        </v-app>
    `,
};
