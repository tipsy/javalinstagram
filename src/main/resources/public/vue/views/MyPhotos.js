import PhotoGallery from "../components/PhotoGallery.js"
import PhotoUploadForm from "../components/PhotoUploadForm.js"

export default {
    name: "MyPhotosView",
    components: {PhotoGallery, PhotoUploadForm},
    data: () => ({}),
    methods: {},
    template: `
        <div class="my-photos-view">
            <h1>My photos</h1>
            <photo-upload-form></photo-upload-form>
            <photo-gallery :owner="true"></photo-gallery>
        </div>
    `,
};
