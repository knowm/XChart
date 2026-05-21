// XChart Site — main.js
// Category filtering and search

(function () {
  const cards = Array.from(document.querySelectorAll('.chart-card'));
  const catLinks = Array.from(document.querySelectorAll('.cat-link'));
  const searchInput = document.getElementById('search');
  const noResults = document.getElementById('no-results');

  let activeCategory = 'all';
  let searchQuery = '';

  function applyFilters() {
    let visible = 0;
    cards.forEach(function (card) {
      const cat = card.dataset.category;
      const name = card.querySelector('.chart-name').textContent.toLowerCase();
      const catLabel = card.querySelector('.chart-category').textContent.toLowerCase();
      const matchesCat = activeCategory === 'all' || cat === activeCategory;
      const matchesSearch =
        searchQuery === '' || name.includes(searchQuery) || catLabel.includes(searchQuery);

      if (matchesCat && matchesSearch) {
        card.classList.remove('hidden');
        visible++;
      } else {
        card.classList.add('hidden');
      }
    });
    noResults.style.display = visible === 0 ? 'block' : 'none';
  }

  catLinks.forEach(function (link) {
    link.addEventListener('click', function (e) {
      e.preventDefault();
      catLinks.forEach(function (l) { l.classList.remove('active'); });
      link.classList.add('active');
      activeCategory = link.dataset.category;
      applyFilters();
    });
  });

  searchInput.addEventListener('input', function () {
    searchQuery = searchInput.value.trim().toLowerCase();
    applyFilters();
  });
})();
