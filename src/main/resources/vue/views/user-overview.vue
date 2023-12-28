<template id="user-overview">
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
            Users
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
        <form id="addUser">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="formData.email" name="email" placeholder="Email"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addUser()">Add User</button>
      </div>
    </div>

    <table class="table table-bordered table-striped table-hover">
      <thead>
      <tr>
        <th scope="col" class="text-center">Name</th>
        <th scope="col" class="text-center">Email</th>
        <th scope="col" class="text-center">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(user, index) in paginatedComponent" :key="index">
        <td class="text-center">{{ user.name }}</td>
        <td class="text-center">{{ user.email }}</td>
        <td class="text-center">
          <a :href="`/users/${user.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link mx-1">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                    @click="deleteUser(user, index)">
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
app.component("user-overview", {
  template: "#user-overview",
  data: () => ({
    users: [],
    formData: { name: "", email: "" },
    hideForm: true,
    currentPage: 1,
    pageSize: 5,
    loading: false,
  }),
  created() {
    this.fetchUsers();
  },
  computed: {
    paginatedComponent() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.users.slice(startIndex, endIndex);
    },
    totalPages() {
      return Math.ceil(this.users.length / this.pageSize);
    },
  },
  methods: {
    fetchUsers: function () {
      this.loading = true;

      axios.get("/api/users")
          .then(res => {
            this.users = res.data;
            this.loading = false;
          })
          .catch(() => {
            this.loading = false;
            alert("Error while fetching users");
          });
    },

    deleteUser: function (user, index) {
      if (confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
        const userId = user.id;
        const url = `/api/users/${userId}`;
        axios.delete(url)
            .then(response =>
                this.users.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },

    addUser: function () {
      const url = `/api/users`;
      axios.post(url,
          {
            name: this.formData.name,
            email: this.formData.email
          })
          .then(response => {
            this.users.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
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
