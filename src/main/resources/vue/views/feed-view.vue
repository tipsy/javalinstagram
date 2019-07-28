<template id="feed-view">
    <div class="feed-view">
        <h1>Latest photos</h1>
        <photo-gallery take="8"></photo-gallery>
    </div>
</template>
<script>
    Vue.component("feed-view", {template: "#feed-view"});
</script>
