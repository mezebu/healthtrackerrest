<template id="activity-profile">
  <app-layout>
    <div v-if="noActivityFound">
      <p>We're sorry, we were not able to retrieve this Activity.</p>
      <p>View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noActivityFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6">User Activity</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link mx-1" @click="updateActivity()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link" @click="deleteActivity()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form v-if="activity">
          <div class="form-group row">
            <label for="input-activity-id" class="col-sm-2 col-form-label">Activity ID</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" v-model="activity.id" name="id" readonly>
            </div>
          </div>
          <div class="form-group row">
            <label for="input-activity-description" class="col-sm-2 col-form-label">Activity</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" v-model="activity.description" name="activity" placeholder="Activity">
            </div>
          </div>
          <div class="form-group row">
            <label for="input-activity-duration" class="col-sm-2 col-form-label">Duration</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" v-model="activity.duration" name="activity" placeholder="Duration">
            </div>
          </div>
          <div class="form-group row">
            <label for="input-activity-calories" class="col-sm-2 col-form-label">Calories</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" v-model="activity.calories" name="calories" placeholder="Calories">
            </div>
          </div>
          <div class="form-group row">
            <label for="input-activity-started" class="col-sm-2 col-form-label">Started</label>
            <div class="col-sm-10">
              <input type="datetime-local" class="form-control" v-model="activity.started" name="started" placeholder="Date Activity Started">
            </div>
          </div>
          <div class="form-group row">
            <label for="input-activity-userId" class="col-sm-2 col-form-label">User Id</label>
            <div class="col-sm-10">
              <input type="number" class="form-control" v-model="activity.userId" name="userId" placeholder="User-Id">
            </div>
          </div>
        </form>
      </div>
      <div>
        <div v-if="activity">
          <div class="card" style="box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
            <div class="card-header bg-info text-white">Date Activity Started: {{ activity.started }}</div>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>


<script>
app.component("activity-profile", {
  template: "#activity-profile",
  data() {
    return {
      activity: null,
      noActivityFound: false,
    };
  },
  created() {
    const activityId = this.$javalin.pathParams["activity-id"];
    const url = `/api/activities/${activityId}`;
    axios.get(url)
        .then(res => {
          this.activity = res.data;
        })
        .catch(() => {
          alert("Error while fetching activity" + activityId);
        });
  },
  methods: {
    updateActivity() {
      const activityId = this.$javalin.pathParams["activity-id"];
      const url = `/api/activities/${activityId}`;
      axios.patch(url, {
        description: this.activity.description,
        duration: this.activity.duration,
        calories: this.activity.calories,
        started: this.activity.started,
        userId: this.activity.userId
      })
          .then(res => {
            this.activity.push(res.data);
          })
          .catch(err => { console.log(err); });
      alert("Activity Updated")
    },
    deleteActivity() {
      if (confirm("Are you sure you want to delete this activity?")) {
        const activityId = this.$javalin.pathParams["activity-id"];
        const url = `/api/activities/${activityId}`;

        axios.delete(url)
            .then(() => {
              alert("Activity deleted")
              // Navigate to the activities page after deletion
              window.location.href = '/activities';
            })
            .catch(err => {
              console.error('Error deleting activity:', err);
            });
      }
    }
  }
});

</script>