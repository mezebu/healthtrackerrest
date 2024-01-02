<template id="activity-overview">
  <app-layout>
    <!-- Loading spinner -->
    <div class="mt-50" v-if="loading">
      <loading-spinner></loading-spinner>
    </div>

    <div v-else>
      <div class="card bg-light mb-3">
        <div class="card-header">
          <div class="row">
            <div class="col-6">
              Activities
            </div>
            <div class="col" align="right">
              <button rel="tooltip" title="Add"
                      class="btn btn-info btn-simple btn-link"
                      @click="hideForm = !hideForm">
                <i class="fa fa-plus" aria-hidden="true"></i>
              </button>
            </div>
          </div>
        </div>
        <div class="card-body" :class="{ 'd-none': hideForm}">
          <form id="addActivity">
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-activity-name">Description</span>
              </div>
              <input type="text" class="form-control" v-model="formData.description" name="description" placeholder="Description"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-activity-duration">Duration</span>
              </div>
              <input type="number" class="form-control" v-model="formData.duration" name="duration" placeholder="Duration"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-activity-calories">Calories</span>
              </div>
              <input type="text" class="form-control" v-model="formData.calories" name="calories" placeholder="Calories"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-activity-started">Started</span>
              </div>
              <input type="datetime-local" class="form-control" v-model="formData.started" name="started" placeholder="Started"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-userId-started">User Id</span>
              </div>
              <input type="number" class="form-control" v-model="formData.userId" name="userId" placeholder="User Id"/>
            </div>
          </form>
          <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addActivity()">Add Activity</button>
        </div>
      </div>
      <table class="table table-bordered table-striped table-hover">
        <thead>
        <tr>
          <th scope="col" class="text-center">Activity ID</th>
          <th scope="col" class="text-center">Description</th>
          <th scope="col" class="text-center">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(activity, index) in paginatedComponent" :key="index">
          <td class="text-center">{{ activity.id }}</td>
          <td class="text-center">{{ activity.description }}</td>
          <td class="text-center">
            <a :href="`/activities/${activity.id}`">
              <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link mx-1">
                <i class="fa fa-pencil" aria-hidden="true"></i>
              </button>
              <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                      @click="deleteActivity(activity, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </a>
          </td>
        </tr>
        </tbody>
      </table>

      <pagination-component
          :current-page="currentPage"
          :total-pages="totalPages"
          :change-page="changePage"
          :previous-page="previousPage"
          :next-page="nextPage"
      ></pagination-component>
    </div>

  </app-layout>
</template>

<script>
app.component("activity-overview", {
  template: "#activity-overview",
  data() {
    return {
      activities: [],
      formData: { description: "", duration: "", calories: "", started: "", userId: "" },
      currentPage: 1,
      pageSize: 5,
      loading: true,
      hideForm: true
    };
  },
  created() {
    this.fetchActivities();
  },
  computed: {
    paginatedComponent() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.activities.slice(startIndex, endIndex);
    },
    totalPages() {
      return Math.ceil(this.activities.length / this.pageSize);
    },
  },
  methods: {
     fetchActivities: function() {
       this.loading = true;

       axios.get("api/activities")
           .then(res => {
             this.activities = res.data;
             this.loading = false;
           })
           .catch(() => {
         this.loading = false;
         alert("Error while fetching activities");
       });
    },
    addActivity: function() {
      const url = `/api/activities`;
      axios.post(url,
          {
            description: this.formData.description,
            duration: this.formData.duration,
            calories: this.formData.calories,
            started: this.formData.started,
            userId: this.formData.userId
          })
          .then(response => {
            this.activities.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteActivity: function (activity, index) {
      if (confirm('Are you sure you want to delete this activity? This action cannot be undone.')) {
        const activityId = activity.id;
        const url = `/api/activities/${activityId}`;
        axios.delete(url)
            .then(response =>
                this.activities.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    changePage(page) {
      this.currentPage = page;
    },
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
  },
});
</script>
