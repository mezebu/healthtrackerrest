<template id="goal-overview">
  <app-layout>
    <div class="mt-50" v-if="loading">
      <loading-spinner></loading-spinner>
    </div>
    <div v-else>
      <div class="card bg-light mb-3">
        <div class="card-header">
          <div class="row">
            <div class="col-6">
              Fitness Goals
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
          <form id="addFitnessGoal">
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-goal-type">Goal Type</span>
              </div>
              <input type="text" class="form-control" v-model="formData.goalType" name="goal type" placeholder="Fitness Goal Type"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-goal-duration">Duration</span>
              </div>
              <input type="text" class="form-control" v-model="formData.duration" name="duration" placeholder="Duration"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-goal-duration">Goal Target</span>
              </div>
              <input type="text" class="form-control" v-model="formData.target" name="target" placeholder="Fitness Goal Target"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-goal-status">Status</span>
              </div>
              <input type="text" class="form-control" v-model="formData.initialUserStatus" name="status" placeholder="Status"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-goal-started">Start Date</span>
              </div>
              <input type="datetime-local" class="form-control" v-model="formData.startDate" name="start" placeholder="Start Date"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-goal-end">End Date</span>
              </div>
              <input type="datetime-local" class="form-control" v-model="formData.endDate" name="end" placeholder="End Date"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-userId-started">User Id</span>
              </div>
              <input type="number" class="form-control" v-model="formData.userId" name="userId" placeholder="User Id"/>
            </div>
          </form>
          <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addFitnessGoal()">Add Fitness Goal</button>
        </div>
      </div>
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
                  <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                          @click="deleteFitnessGoal(goal, index)">
                    <i class="fas fa-trash" aria-hidden="true"></i>
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
const GOAL_API_URL = "/api/goals";

app.component("goal-overview", {
  template: "#goal-overview",
  data() {
    return {
      goals: [],
      currentPage: 1,
      pageSize: 2,
      loading: false,
      hideForm: true,
      formData: {
        goalType: "",
        duration: "",
        target: "",
        initialUserStatus: "",
        startDate: "",
        endDate: "",
        userId: "",
      },
    };
  },
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
    fetchGoals() {
      this.loading = true;
      axios
          .get(GOAL_API_URL)
          .then((res) => {
            this.goals = res.data;
            this.loading = false;
          })
          .catch(() => {
            this.loading = false;
            alert("Error while fetching fitness goals");
          });
    },
    addFitnessGoal() {
      const { formData } = this;
      const goalData = {
        goalType: formData.goalType,
        duration: formData.duration,
        target: formData.target,
        initialUserStatus: formData.initialUserStatus,
        startDate: formData.startDate,
        endDate: formData.endDate,
        userId: formData.userId,
      };
      axios
          .post(GOAL_API_URL, goalData)
          .then((response) => {
            this.goals.push(response.data);
            this.hideForm = true;
          })
          .catch((error) => {
            console.log(error);
          });
    },
    deleteFitnessGoal(goal, index) {
      if (
          confirm(
              "Are you sure you want to delete this fitness goal? This action cannot be undone."
          )
      ) {
        const goalId = goal.id;
        const url = `${GOAL_API_URL}/${goalId}`;
        axios
            .delete(url)
            .then((response) => {
              this.goals.splice(index, 1, response.data);
              window.location.href = '/goals'
            })
            .catch((error) => {
              console.log(error);
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
