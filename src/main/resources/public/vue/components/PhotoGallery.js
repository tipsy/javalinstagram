export default {
    name: "PhotoGallery",
    props: ["forUser", "take"],
    data: () => ({
        lightbox: false,
        lightboxedPhoto: null,
        photos: [],
    }),
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
            axios.post("/api/likes?photo-id=" + photoId).then(res => this.loadPhotos());
        },
        unlikePhoto(photoId) {
            axios.delete("/api/likes?photo-id=" + photoId).then(res => this.loadPhotos());
        },
        loadPhotos() {
            let owner = this.forUser ? "owner-id=" + this.$currentUser : "";
            let take = this.take ? "take=" + this.take : "";
            axios.get("/api/photos?" + owner + "&" + take).then(res => this.photos = res.data);
        }
    },
    mounted() {
        this.$eventBus.$on("photo-uploaded", () => {
            this.loadPhotos();
        });
    },
    template: `
        <div class="photo-gallery">
            <div class="photos">
                <p v-if="photos.length === 0">
                    There are no photos on Javalinstagram yet. 
                    You should <router-link to="/my-photos">upload something</router-link>.
                </p>
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
                <v-card class="photo-gallery">
                    <img v-if="lightboxedPhoto !== null" :src="'/static/p/' + lightboxedPhoto">
                </v-card>
            </v-dialog>
        </div>
    `,
    created() {
        this.loadPhotos();
    },
    beforeCreate() {
        this.$injectStyle(`<style id="photo-gallery">
            .photo-gallery img {
                display: block;
                max-width: 100%;
            }
            .photo-gallery h1 {
                font-family: 'Lobster Two', cursive;
                font-weight: 400;
                color: #444;
                font-size: 28px;
            }
            .photo-gallery .photos {
                display: flex;
                flex-wrap: wrap;
                justify-content: space-between;
                animation: fadeIn .2s forwards;
            }
            .photo-gallery .photo {
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
            .photo-gallery .photo .meta {
                font-size: 13px;
                padding: 5px;
                height: 42px;
                overflow: hidden;
            }
            .photo-gallery .like-btn {
                cursor: pointer;
                position: absolute;
                right: 8px;
                bottom: 48px;
                opacity: 0.8;
            }
            @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
        </style>`);
    }
};
