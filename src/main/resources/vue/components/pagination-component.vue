<template id="pagination-component">
  <nav aria-label="Pagination">
    <ul class="pagination justify-content-center mt-4">
      <li class="page-item" :class="{ disabled: currentPage === 1 }">
        <a class="page-link" href="#" @click.prevent="previousPage">Previous</a>
      </li>
      <li class="page-item" v-for="page in totalPages" :key="page" :class="{ active: currentPage === page }">
        <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
      </li>
      <li class="page-item" :class="{ disabled: currentPage === totalPages }">
        <a class="page-link" href="#" @click.prevent="nextPage">Next</a>
      </li>
    </ul>
  </nav>
</template>

<script>
app.component("pagination-component", {
  template: "#pagination-component",
  props: {
    currentPage: {
      type: Number,
      required: true,
    },
    totalPages: {
      type: Number,
      required: true,
    },
    changePage: {
      type: Function,
      required: true,
    },
    previousPage: {
      type: Function,
      required: true,
    },
    nextPage: {
      type: Function,
      required: true,
    },
  },
});
</script>
