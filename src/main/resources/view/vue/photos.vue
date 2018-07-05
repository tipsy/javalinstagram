<template id="photo-component">
    <div class="photo-component">
        <photo-upload-form @uploaded="getUserPhotos" v-if="showUploadForm"></photo-upload-form>
        <h1 v-if="forUser">My photos</h1>
        <h1 v-else>Latest photos</h1>
        <div class="photos">
            <div v-if="photos.length === 0 && forUser">Hmm... There's nothing here. You should upload something.</div>
            <div v-for="photo in photos" class="photo">
                <img @click="openLightbox(photo.id)" :src="'/static/p/' + photo.id">
                <div class="meta">
                    <div>{{formatDate(photo.created)}}</div>
                    <div>{{photo.likes}} {{photo.likes === 1 ? "like" : "likes"}}</div>
                    <div v-if="photo.liked" class="like-btn" @click="unlikePhoto(photo.id)">
                        <v-icon color="white" size="42">favorite</v-icon>
                    </div>
                    <div v-if="!photo.liked" class="like-btn" @click="likePhoto(photo.id)">
                        <v-icon color="white" size="42">favorite_border</v-icon>
                    </div>
                </div>
            </div>
        </div>
        <v-dialog hide-overlay v-model="lightbox" max-width="800" @input="v => v || closeLightbox()">
            <v-card>
                <img v-if="lightboxedPhoto !== null" :src="'/static/p/' + lightboxedPhoto">
            </v-card>
        </v-dialog>
    </div>
</template>
<script>
    Vue.component("photo-component", {
        template: "#photo-component",
        props: ["showUploadForm", "forUser", "justLatest"],
        data() {
            return {
                signedInUser: Cookies.get("signed-in-user"),
                lightbox: false,
                lightboxedPhoto: null,
                photos: [],
            }
        },
        methods: {
            openLightbox(photoId) {
                this.lightboxedPhoto = photoId;
                this.lightbox = true;
            },
            closeLightbox() {
                this.lightbox = false;
                setTimeout(() => this.lightboxedPhoto = null, 300);
            },
            formatDate(timeInMillis) {
                return moment(timeInMillis).format("Do of MMMM, YYYY");
            },
            likePhoto(photoId) {
                axios.post("/api/likes?photo-id=" + photoId).then(response => {
                    this.getUserPhotos();
                });
            },
            unlikePhoto(photoId) {
                axios.delete("/api/likes?photo-id=" + photoId).then(response => {
                    this.getUserPhotos();
                });
            },
            getUserPhotos() {
                let ownerQp = this.forUser ? "owner-id=" + Cookies.get("signed-in-user") : "";
                let commandQp = this.justLatest ? "command=latest" : "";
                axios.get("/api/photos?" + ownerQp + "&" + commandQp).then(response => {
                    this.photos = response.data;
                });
            }
        },
        created() {
            this.getUserPhotos();
        }
    });
</script>
<style>
    img {
        display: block;
        max-width: 100%;
    }

    .photo-component h1 {
        font-family: 'Lobster Two', cursive;
        font-weight: 400;
        color: #444;
        font-size: 28px;
    }

    .photos {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        animation: fadeIn .2s forwards;
    }

    .photos .photo {
        position: relative;
        display: block;
        width: 23%;
        margin-bottom: 24px;
        float: left;
        border-radius: 3px;
        border: 2px solid #fff;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.4);
        background: #fff;
    }

    .photo .meta {
        font-size: 13px;
        padding: 5px;
        height: 42px;
        overflow: hidden;
    }

    .like-btn {
        cursor: pointer;
        position: absolute;
        right: 8px;
        bottom: 48px;
        opacity: 0.8;
    }

    @keyframes fadeIn {
        from {
            opacity: 0;
        }
        to {
            opacity: 1;
        }
    }
</style>
