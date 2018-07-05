<template id="nav-bar">
    <header class="navbar">
        <v-container class="pa-0">
            <v-toolbar flat>
                <a href="/"><img id="logo-img" src="data:image/png;base64,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" alt="Javalinstagram"></a>
                <a href="/"><span id="logo-h1">Javalinstagram</span></a>
                <v-spacer></v-spacer>
                <v-toolbar-items v-if="signedInUser">
                    <v-menu bottom left offset-y>
                        <v-btn slot="activator" flat>
                            <v-icon color="grey darken-2">account_circle</v-icon>
                        </v-btn>
                        <v-list>
                            <v-list-tile><small>Signed in as '{{signedInUser}}'</small></v-list-tile>
                            <v-list-tile href="/my-photos">My photos</v-list-tile>
                            <v-list-tile href="/signout">Sign out</v-list-tile>
                        </v-list>
                    </v-menu>
                </v-toolbar-items>
            </v-toolbar>
        </v-container>
    </header>
</template>
<script>
    Vue.component("nav-bar", {
        template: "#nav-bar",
        data() {
            return {
                signedInUser: ""
            }
        },
        created() {
            this.signedInUser = Cookies.get("signed-in-user");
        }
    });
</script>
<style>
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
</style>
