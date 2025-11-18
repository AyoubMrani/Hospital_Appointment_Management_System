/**
 * Live Search Functionality
 * Works on patient, doctor, and appointment list pages
 */

document.addEventListener('DOMContentLoaded', function () {
    const searchForm = document.getElementById('searchForm');
    const searchInput = document.getElementById('searchInput');

    if (!searchForm || !searchInput) {
        return; // Search form not available on this page
    }

    // Get current page type
    const currentPath = window.location.pathname;
    let searchEndpoint = null;
    let listContainerId = null;
    let rowSelector = null;

    if (currentPath.includes('/patients')) {
        searchEndpoint = '/patients/api/search';
        listContainerId = 'patientTable';
        rowSelector = 'tr[data-patient-id]';
    } else if (currentPath.includes('/doctors')) {
        searchEndpoint = '/doctors/api/search';
        listContainerId = 'doctorTable';
        rowSelector = 'tr[data-doctor-id]';
    } else if (currentPath.includes('/appointments')) {
        searchEndpoint = '/appointments/api/search';
        listContainerId = 'appointmentTable';
        rowSelector = 'tr[data-appointment-id]';
    }

    if (!searchEndpoint) {
        return; // Not a list page
    }

    // Prevent default form submission
    searchForm.addEventListener('submit', function (e) {
        e.preventDefault();
        performSearch();
    });

    // Live search as user types
    searchInput.addEventListener('input', function () {
        if (this.value.length > 0) {
            performSearch();
        } else {
            // Show all rows if search is empty
            const rows = document.querySelectorAll(rowSelector);
            rows.forEach(row => {
                row.style.display = '';
            });
        }
    });

    function performSearch() {
        const query = searchInput.value.trim();

        if (query.length === 0) {
            const rows = document.querySelectorAll(rowSelector);
            rows.forEach(row => {
                row.style.display = '';
            });
            return;
        }

        fetch(`${searchEndpoint}?query=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(data => {
                const rows = document.querySelectorAll(rowSelector);
                const resultIds = data.map(item => item.id);

                rows.forEach(row => {
                    const rowId = row.getAttribute('data-patient-id') ||
                        row.getAttribute('data-doctor-id') ||
                        row.getAttribute('data-appointment-id');

                    if (resultIds.includes(rowId)) {
                        row.style.display = '';
                    } else {
                        row.style.display = 'none';
                    }
                });
            })
            .catch(error => {
                console.error('Search error:', error);
            });
    }
});
