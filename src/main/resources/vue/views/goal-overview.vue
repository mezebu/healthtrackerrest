<template id="goal-overview">
  <app-layout>
    <div class="row">
      <div v-for="goal in goals" :key="goal.id" class="col-md-4">
        <a :href="`/goals/${goal.id}`" style="text-decoration: none">
          <div class="card border-info text-white mb-3">
            <div class="card-header bg-info"> Goal Id: {{ goal.id }}</div>
            <div class="card-body text-info">
              <h5 class="card-title">{{goal.goalType}}</h5>
            </div>
          </div>
        </a>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("goal-overview", {
  template: "#goal-overview",
  data: () => ({
    goals: [],
  }),
  created() {
    this.fetchGoals();
  },
  methods: {
    fetchGoals: function() {
      axios.get("/api/goals")
          .then(res => this.goals = res.data)
          .catch(() => alert("Error while fitness goals"))
    }
  }
})
</script>