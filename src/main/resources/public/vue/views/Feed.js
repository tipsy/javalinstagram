import PhotoGallery from "../components/PhotoGallery.js";

export default {
    name: "FeedView",
    components: {PhotoGallery},
    data: () => ({}),
    methods: {},
    template: `
        <div class="feed-view">
            <h1>Latest photos</h1>
            <photo-gallery take="8"></photo-gallery>
        </div>
    `,
};
