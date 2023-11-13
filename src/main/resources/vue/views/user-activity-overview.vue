<template id="user-activity-overview">
  <app-layout>
  <div>
    <h3 class="mb-3">Activities list </h3>
    <div class="row">
      <div v-for="activity in activities" class="col-lg-6">
        <div class="card border-success mb-3" style="box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
          <div class="card-header"> Activity Id: {{ activity.id }}</div>
          <div class="card-body">
            <h5 class="card-title">{{activity.description}}</h5>
            <ul class="list-group">
              <li class="list-group-item">Duration: {{activity.duration}} minutes</li>
              <li class="list-group-item">Calories: {{activity.calories}}</li>
              <li class="list-group-item">Started: {{activity.started}}</li>
              <li class="list-group-item">User ID: {{activity.userId}}</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
  </app-layout>
</template>

<script>
app.component("user-activity-overview",{
  template: "#user-activity-overview",
  data: () => ({
    activities: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/activities`)
        .then(res => this.activities = res.data)
        .catch(() => alert("Error while fetching activities"));
  }
});
</script>