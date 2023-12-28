<template id="loading-spinner">
  <div class="d-flex align-items-center justify-content-center">
    <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>
</template>

<script>
app.component("loading-spinner", {
  template: "#loading-spinner",
});
</script>