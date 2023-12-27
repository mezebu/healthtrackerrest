<template id="user-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user.</p>
      <p> View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link mx-1"
                    @click="updateUser()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteUser()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form v-if="user">
          <div class="form-group row">
            <label for="input-user-id" class="col-sm-2 col-form-label">User ID</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" v-model="user.id" name="id" readonly>
            </div>
          </div>
          <div class="form-group row">
            <label for="input-user-name" class="col-sm-2 col-form-label">Name</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" v-model="user.name" name="name" placeholder="Name">
            </div>
          </div>
          <div class="form-group row">
            <label for="input-user-email" class="col-sm-2 col-form-label">Email</label>
            <div class="col-sm-10">
              <input type="email" class="form-control" v-model="user.email" name="email" placeholder="Email">
            </div>
          </div>
        </form>
      </div>

      <div class="card-footer text-left">
        <p  v-if="activities.length === 0"> {{user.name}} has not engaged in any activities yet.</p>
        <p  v-if="activities.length > 0"> {{user.name}}'s activities and fitness goals so far...</p>

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
        <div class="row">
          <div v-for="goal in goals" class="col-lg-6">
            <div class="card border-info mb-3" style="box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
              <div class="card-header"> Fitness Goal Id: {{ goal.id }}</div>
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
    </div>
  </app-layout>
</template>

<script>
app.component("user-profile", {
  template: "#user-profile",
  data: () => ({
    user: null,
    noUserFound: false,
    activities: [],
    goals: [],
  }),
  created: function () {
    const userId = this.$javalin.pathParams["user-id"];
    const url = `/api/users/${userId}`
    axios.get(url)
        .then(res => this.user = res.data)
        .catch(error => {
          console.log("No user found for id passed in the path parameter: " + error)
          this.noUserFound = true
        })
    axios.get(url + `/activities`)
        .then(res => this.activities = res.data)
        .catch(error => {
          console.log("No activities added yet (this is ok): " + error)
        })
    axios.get(url + `/goals`)
        .then(res => this.goals = res.data)
        .catch(error => { console.log("No fitness goals yet" + error)})
  },
  methods: {
    updateUser: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}`
      axios.patch(url,
          {
        name: this.user.name,
        email: this.user.email
      })
          .then(res => this.user.push(res.data))
          .catch((error) => { console.log(error) })
      alert("User Updated")
    },
    deleteUser: function () {
      if (confirm("Do you really want to delete?")) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/${userId}`
        axios.delete(url)
            .then(response => {
              alert("User deleted")
              //display the /users endpoint
              window.location.href = '/users';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>