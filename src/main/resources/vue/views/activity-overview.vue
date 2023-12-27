<template id="activity-overview">
  <app-layout>
      <div class="row">
        <div v-for="activity in activities" :key="activity.id" class="col-md-4">
          <a :href="`/activities/${activity.id}`" style="text-decoration: none">
            <div class="card border-success text-white mb-3">
              <div class="card-header bg-success"> Activity Id: {{ activity.id }}</div>
              <div class="card-body text-success">
                <h5 class="card-title">{{activity.description}}</h5>
              </div>
            </div>
          </a>
        </div>
      </div>
  </app-layout>
</template>

<script>
app.component("activity-overview", {
  template: "#activity-overview",
  data() {
    return {
      activities: [],
    };
  },
  created() {
    this.fetchActivities();
  },
  methods: {
    async fetchActivities() {
      try {
        const response = await axios.get("api/activities");
        this.activities = response.data;
      } catch (error) {
        alert("Error while fetching activities");
      }
    },
  },
});
</script>

<style>
.card {
  transition: transform 0.3s ease-in-out;
}

.card:hover {
  transform: translateY(-5px);
}

</style>