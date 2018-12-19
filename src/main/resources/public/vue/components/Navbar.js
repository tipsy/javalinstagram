export default {
    name: "NavBar",
    methods: {
        signOut() {
            axios.post("/api/account/sign-out").then(() => {
                localStorage.removeItem("current-user");
                this.$root.$data.currentUser = "";
                this.$router.push({name: "Signin"});
            });
        }
    },
    template: `
        <header class="navbar">
            <v-container class="pa-0">
                <v-toolbar flat>
                    <router-link to="/"><img class="logo-img" src="/logo.png" alt="Javalinstagram"></router-link>
                    <router-link to="/"><span class="logo-text">Javalinstagram</span></router-link>
                    <v-spacer></v-spacer>
                    <v-toolbar-items v-if="$root.$data.currentUser">
                        <v-menu bottom left offset-y>
                            <v-btn slot="activator" flat>
                                <v-icon color="grey darken-2">account_circle</v-icon>
                            </v-btn>
                            <v-list>
                                <v-list-tile>
                                    <small>Signed in as '{{$root.$data.currentUser}}'</small>
                                </v-list-tile>
                                <v-list-tile><router-link to="/my-photos">My photos</router-link></v-list-tile>
                                <v-list-tile @click="signOut">Sign out</v-list-tile>
                            </v-list>
                        </v-menu>
                    </v-toolbar-items>
                </v-toolbar>
            </v-container>
        </header>
    `,
    beforeCreate() {
        this.$injectStyle(`<style id="nav-bar">
            .navbar {
                background: #f5f5f5;
                border-bottom: 1px solid #ddd;
            }
            .navbar .logo-img {
                height: 44px;
            }
            .navbar .logo-text {
                margin-left: 16px;
                font-family: 'Lobster Two', cursive;
                font-weight: 400;
                color: #444;
                font-size: 28px;
            }
        </style>`);
    }
};
