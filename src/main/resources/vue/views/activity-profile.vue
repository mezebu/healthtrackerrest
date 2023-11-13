<template id="activity-profile">
  <app-layout>
    <div>
      <div v-if="activity">
        <div class="card border-success mb-3" style="box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
          <div class="card-header bg-success text-white"> Activity Id: {{ activity.id }}</div>
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
  </app-layout>
</template>

<script>
app.component("activity-profile", {
  template: "#activity-profile",
  data: () => ({
    activity: null
  }),
  created: function () {
    const activityId = this.$javalin.pathParams["activity-id"]
    const url = `/api/activities/${activityId}`
    axios.get(url)
        .then(res => this.activity = res.data)
        .catch(() => alert("Error while fetching activity" + activityId))
  }
})
</script>