<template id="goal-profile">
  <app-layout>
    <div v-if="loading">
      <loading-spinner></loading-spinner>
    </div>
    <div v-else>
      <div v-if="goal">
        <div class="card border-info mb-3">
          <div class="card-header bg-info text-white"> Goal Id: {{ goal.id }}</div>
          <div class="card-body">
            <h5 class="card-title"> Goal Type: {{goal.goalType}}</h5>
            <ul class="list-group">
              <li class="list-group-item">Target: {{goal.target}}</li>
              <li class="list-group-item">Duration: {{goal.duration}}</li>
              <li class="list-group-item">User Status: {{goal.initialUserStatus}}</li>
              <li class="list-group-item">Start Date: {{goal.startDate}}</li>
              <li class="list-group-item">End Date: {{goal.endDate}}</li>
              <li class="list-group-item">User ID: {{goal.userId}}</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("goal-profile", {
  template: "#goal-profile",
  data: () => ({
    goal: null,
    loading: false,
  }),
  created: function () {
    const goalId = this.$javalin.pathParams["goal-id"]
    const url = `/api/goals/${goalId}`

    this.loading = true;

    axios.get(url)
        .then(res => {
          this.goal = res.data;
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
          alert("Error while fetching goal " + goalId);
        })
  }
})
</script>