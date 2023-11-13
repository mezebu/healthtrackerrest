<template id="user-goal-overview">
  <app-layout>
    <div>
      <h3 class="mb-3">Fitness Goals List </h3>
      <div class="row">
        <div v-for="goal in goals" class="col-lg-6">
          <div class="card border-info mb-3" style="box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
            <div class="card-header"> Goal Id: {{ goal.id }}</div>
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
    </div>
  </app-layout>
</template>

<script>
app.component("user-goal-overview", {
  template: "#user-goal-overview",
  data: () => ({
    goals: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/goals`)
        .then(res => this.goals = res.data)
        .catch(() => alert("Error while fetching user fitness goals"))
  }
})
</script>