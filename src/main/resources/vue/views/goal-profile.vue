<template id="goal-profile">
  <app-layout>
    <div v-if="loading">
      <loading-spinner></loading-spinner>
    </div>
    <div v-if="noGoalFound">
      <p>We're sorry, we were not able to retrieve this fitness goal.</p>
      <p>View <a :href="'/goals'">all fitness goals</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noGoalFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6">Fitness Goal</div>
          <div class="col" style="text-align: right;">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link mx-1" @click="updateGoal()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link" @click="deleteGoal()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
    <div v-else>
      <div v-if="goal">
        <div class="card border-info mb-3">
          <div class="card-body">
            <h5 class="card-title"> Goal Type: {{goal.goalType}}</h5>
            <div> Goal Id: {{ goal.id }}</div>
            <form>
              <div class="form-group row">
                <label for="target" class="col-sm-2 col-form-label">Target</label>
                <div class="col-sm-10">
                  <input type="text" class="form-control" v-model="goal.target">
                </div>
              </div>
              <div class="form-group row">
                <label for="duration" class="col-sm-2 col-form-label">Duration</label>
                <div class="col-sm-10">
                  <input type="text" class="form-control" v-model="goal.duration">
                </div>
              </div>
              <div class="form-group row">
                <label for="userStatus" class="col-sm-2 col-form-label">User Status</label>
                <div class="col-sm-10">
                  <input type="text" class="form-control" v-model="goal.initialUserStatus">
                </div>
              </div>
              <div class="form-group row">
                <label for="startDate" class="col-sm-2 col-form-label">Start Date</label>
                <div class="col-sm-10">
                  <input type="datetime-local" class="form-control" v-model="goal.startDate">
                </div>
              </div>
              <div class="form-group row">
                <label for="endDate" class="col-sm-2 col-form-label">End Date</label>
                <div class="col-sm-10">
                  <input type="datetime-local" class="form-control" v-model="goal.endDate">
                </div>
              </div>
              <div class="form-group row">
                <label for="userId" class="col-sm-2 col-form-label">User ID</label>
                <div class="col-sm-10">
                  <input type="text" class="form-control" v-model="goal.userId">
                </div>
              </div>
            </form>
            <div>
            </div>
            <div v-if="goal">
              <div class="card" style="box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
                <div class="card-header">Start Date: {{ goal.startDate }}</div>
              </div>
              <div class="card" style="box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
                <div class="card-header">End Date: {{ goal.endDate }}</div>
              </div>
            </div>
          </div>
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
    noGoalFound: false
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
  },
  methods: {
    updateGoal: function () {
      const goalId = this.$javalin.pathParams["goal-id"];
      const url = `/api/goals/${goalId}`
      axios.patch(url,
          {
            goalType: this.goal.goalType,
            target: this.goal.target,
            duration: this.goal.duration,
            initialUserStatus: this.goal.initialUserStatus,
            startDate: this.goal.startDate,
            endDate: this.goal.endDate,
            userId: this.goal.userId
          })
          .then(res => this.goal.push(res.data))
          .catch((error) => { console.log(error) })
      alert("Fitness Goal Updated")
    },
    deleteGoal: function () {
      if (confirm("Do you really want to delete?")) {
        const goalId = this.$javalin.pathParams["goal-id"];
        const url = `/api/users/${goalId}`
        axios.delete(url)
            .then(response => {
              alert("Fitness Goal deleted")
              //display the /goals endpoint
              window.location.href = '/goals';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
})
</script>