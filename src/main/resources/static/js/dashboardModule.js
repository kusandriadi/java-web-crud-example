/**
 * Dashboard Module
 * Handles dashboard statistics operations
 */
const dashboardModule = {
    // Dashboard data
    data: {
        stats: {
            siTotal: 0,
            tiTotal: 0,
            siActive: 0,
            tiActive: 0,
            siNotActive: 0,
            tiNotActive: 0,
            totalStudents: 0
        }
    },

    // Dashboard methods
    methods: {
        /**
         * Load statistics from API
         */
        loadStatistics() {
            fetch('/api/students/statistics')
                .then(response => response.json())
                .then(data => {
                    this.stats = data;
                })
                .catch(error => {
                    console.error('Error loading statistics:', error);
                });
        }
    }
};
