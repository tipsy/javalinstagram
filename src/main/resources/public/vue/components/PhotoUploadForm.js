export default {
    name: "PhotoUploadForm",
    data: () => ({
        photopreview: null
    }),
    methods: {
        previewPhoto() {
            if (document.querySelector("#photoinput").files[0]) {
                const reader = new FileReader();
                reader.onload = e => this.photopreview = e.target.result;
                reader.readAsDataURL(document.querySelector("#photoinput").files[0]);
            }
        },
        clearUploadForm() {
            document.querySelector("#photoinput").value = "";
            this.photopreview = null;
        },
        uploadPhoto() {
            const data = new FormData(document.getElementById("upload-form"));
            const config = {headers: {"Content-Type": "multipart/form-data"}};
            axios.post("/api/photos", data, config).then(() => {
                this.clearUploadForm();
                this.$eventBus.$emit("photo-uploaded")
            }).catch(error => {
                alert("An error occurred while uploading the photo.");
                console.log(error);
            });
        },
    },
    computed: {
        dialog: function () {
            return this.photopreview !== null;
        }
    },
    template: `
        <div class="photo-upload-form">
            <div id="dropzone">
                <div>Drag or click to choose photo</div>
                <form id="upload-form">
                    <input id="photoinput" @change="previewPhoto" name="photo" type="file" accept="image/*">
                </form>
            </div>
            <v-dialog v-model="dialog" max-width="460" persistent>
                <v-card>
                    <v-card-title class="headline">Publish photo?</v-card-title>
                    <v-card-text>
                        <img :src="photopreview" class="preview">
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="gray darken-2" flat="flat" @click="clearUploadForm">Cancel</v-btn>
                        <v-btn color="primary" flat="flat" @click="uploadPhoto">Publish</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
        </div>
    `,
    beforeCreate() {
        this.$injectStyle(`<style id="photo-upload-form">
            #dropzone {
                position: relative;
                width: 100%;
                height: 120px;
                line-height: 120px;
                text-align: center;
                font-size: 18px;
                border: 3px dashed #ddd;
                background: #fff;
                margin-bottom: 32px;
            }
            #photoinput {
                position: absolute;
                top: 0;
                left: 0;
                height: 100%;
                width: 100%;
                opacity: 0;
            }
            .preview {
                width: 400px;
                height: 400px;
                max-width: 100%;
                object-fit: cover;
            }
        </style>`);
    }
};
