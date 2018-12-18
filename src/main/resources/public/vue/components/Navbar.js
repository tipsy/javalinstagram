export default {
    name: "NavBar",
    methods: {
        signOut() {
            localStorage.clear();
            location.href = "/api/signout";
        }
    },
    template: `
        <div class="nav-bar">
            <header class="navbar">
                <v-container class="pa-0">
                    <v-toolbar flat>
                        <router-link to="/"><img id="logo-img" src="/logo.png" alt="Javalinstagram"></router-link>
                        <router-link to="/"><span id="logo-h1">Javalinstagram</span></router-link>
                        <v-spacer></v-spacer>
                        <v-toolbar-items v-if="$currentUser">
                            <v-menu bottom left offset-y>
                                <v-btn slot="activator" flat>
                                    <v-icon color="grey darken-2">account_circle</v-icon>
                                </v-btn>
                                <v-list>
                                    <v-list-tile>
                                        <small>Signed in as '{{$currentUser}}'</small>
                                    </v-list-tile>
                                    <v-list-tile><router-link to="/my-photos">My photos</router-link></v-list-tile>
                                    <v-list-tile @click="signOut">Sign out</v-list-tile>
                                </v-list>
                            </v-menu>
                        </v-toolbar-items>
                    </v-toolbar>
                </v-container>
            </header>
        </div>
    `,
    beforeCreate() {
        this.$injectStyle(`<style id="nav-bar">
            header.navbar {
                background: #f5f5f5;
                border-bottom: 1px solid #ddd;
            }
            #logo-img {
                height: 44px;
            }
            #logo-h1 {
                margin-left: 16px;
                font-family: 'Lobster Two', cursive;
                font-weight: 400;
                color: #444;
                font-size: 28px;
            }
        </style>`);
    }
};
