<template id="activity-overview">
  <app-layout>
      <div class="row">
        <div v-for="activity in activities" :key="activity.id" class="col-md-4">
          <a :href="`/activities/${activity.id}`">
            <div class="card border-primary mb-3">
              <div class="card-header"> Activity Id: {{ activity.id }}</div>
              <div class="card-body text-primary">
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
  data: () => ({
    activities: [],
  }),
  created() {
    this.fetchActivities();
  },
  methods: {
    fetchActivities: function () {
      axios.get("api/activities")
          .then(res => this.activities = res.data)
          .catch(() => alert("Error while fetching activities"))
    }
  }
})
</script>