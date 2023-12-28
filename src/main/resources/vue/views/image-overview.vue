<template id="image-overview">
  <app-layout>
    <div class="row">
      <div v-for="image in images" :key="image.id" class="col-md-4">
        <a :href="`/images/${image.id}`" style="text-decoration: none">
          <div class="card border-info text-white mb-3">
            <div class="card-header bg-info"> Image Id: {{ image.id }}</div>
            <div class="card-body text-info" style="padding: 0;">
              <div style="position: relative; width: 100%; height: 150px; overflow: hidden;">
                <img :src="image.imagePath" alt="Image" style="width: 100%; height: 100%; object-fit: cover;">
              </div>
              <h5 class="card-title">{{ image.imageDescription }}</h5>
            </div>
          </div>
        </a>
      </div>
    </div>
  </app-layout>
</template>


<script>


app.component("image-overview", {
  template: "#image-overview",
  data: () => ({
    images: [],
  }),
  created() {
    this.fetchImages();
  },
  methods: {
    async fetchImages() {
      try {
        const response = await axios.get("/api/images");
        this.images = response.data;
      } catch (error) {
        alert("Error while fetching images");
      }
    },
  }
});
</script>
