<template id="goal-overview">
  <app-layout>
    <div class="mt-50" v-if="loading">
      <loading-spinner></loading-spinner>
    </div>
    <div v-else>
      <h3 class="mb-3 text-center">Fitness Goals List</h3>
      <div class="row">
        <div v-for="(goal, index) in paginatedComponent" :key="index" class="col-lg-6">
          <div class="card border-info mb-3" style="box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
            <div class="card-header d-flex justify-content-between align-items-center">
              <div>Goal Id: {{ goal.id }}</div>
              <div>
                <a :href="`/goals/${goal.id}`">
                  <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link mx-1">
                    <i class="fa fa-pencil" aria-hidden="true"></i>
                  </button>
                </a>
              </div>
            </div>
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
app.component("goal-overview", {
  template: "#goal-overview",
  data: () => ({
    goals: [],
    currentPage: 1,
    pageSize: 2,
    loading: false,
  }),
  computed: {
    paginatedComponent() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.goals.slice(startIndex, endIndex);
    },
    totalPages() {
      return Math.ceil(this.goals.length / this.pageSize);
    },
  },
  created() {
    this.fetchGoals();
  },
  methods: {
    fetchGoals: function() {
      this.loading = true;
      axios.get("/api/goals")
          .then(res => {
            this.goals = res.data;
            this.loading = false;
          })
          .catch(() => {
            this.loading = false;
            alert("Error while fetching fitness goals")
          });
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