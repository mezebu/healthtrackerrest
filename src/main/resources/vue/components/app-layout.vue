<template id="app-layout">
  <div class="app-layout">

      <!-- Start of navbar -->
      <nav class="navbar navbar-expand-md navbar-light">
        <div class="container">
        <a class="navbar-brand" href="/">Home</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse"
                data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
              <a class="nav-link" href="/users">
                Users <span class="sr-only">(current)</span>
              </a>
            </li>
            <li class="nav-item active">
              <a class="nav-link" href="/activities">Activities</a>
            </li>
            <li class="nav-item active">
              <a class="nav-link" href="/goals">Fitness Goals</a>
            </li>
          </ul>
        </div>
        </div>
      </nav>
      <!--End of nav bar-->
      <!--Start of main content area-->

      <div class="content mt-3">
        <div class="container">
          <slot></slot>
        </div>
      </div>

      <!--End of main content area-->

  </div>
</template>

<script>
app.component("app-layout", {
  template: "#app-layout"
});
</script>

<style>
.navbar{
  background-color: #e3f2fd;
}
</style>