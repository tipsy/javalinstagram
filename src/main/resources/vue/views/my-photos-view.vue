<template id="my-photos-view">
    <div class="my-photos-view">
        <h1>My photos</h1>
        <photo-upload-form></photo-upload-form>
        <photo-gallery :forUser="true"></photo-gallery>
    </div>
</template>
<script>
    Vue.component("my-photos-view", {template: "#my-photos-view"});
</script>
