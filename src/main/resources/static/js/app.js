/**
 * Main Application File
 * Integrates all modules: student, subject, and dashboard
 */

// Create Vue Application
const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            // User data
            user: {
                name: '',
                email: '',
                picture: '',
                isAdmin: false,
                roles: ''
            },

            // UI state
            sidebarOpen: true,
            activePage: 'Dashboard',
            dataMenuOpen: false,
            classMenuOpen: false,

            // Student data from studentModule
            studentList: [],
            studentForm: {
                id: null,
                nim: '',
                name: '',
                email: '',
                major: '',
                batch: null,
                status: ''
            },
            majorOptions: [],

            // Subject data from subjectModule
            subjectList: [],
            subjectForm: {
                id: null,
                code: '',
                name: '',
                sks: null
            },

            // Class data from classModule
            classList: [],
            classForm: {
                id: null,
                code: '',
                name: '',
                subjectId: '',
                subjectName: '',
                semester: '',
                year: null,
                studentIds: []
            },
            availableStudents: [],
            enrolledStudents: [],
            subjectOptions: [],

            // Dashboard data from dashboardModule
            stats: {
                siTotal: 0,
                tiTotal: 0,
                siActive: 0,
                tiActive: 0,
                siNotActive: 0,
                tiNotActive: 0,
                totalStudents: 0
            },

            // Custom modal data
            notificationModal: {
                type: 'success',
                message: ''
            },
            confirmModal: {
                message: '',
                onConfirm: null
            }
        }
    },

    methods: {
        // ===== Custom Modal Methods =====
        showNotification(type, message) {
            this.notificationModal.type = type;
            this.notificationModal.message = message;
            const modal = new bootstrap.Modal(document.getElementById('notificationModal'));
            modal.show();
        },

        showConfirm(message, onConfirm) {
            this.confirmModal.message = message;
            this.confirmModal.onConfirm = onConfirm;
            const modal = new bootstrap.Modal(document.getElementById('confirmModal'));
            modal.show();
        },
        // ===== User Methods =====
        fetchUser() {
            fetch('/api/user')
                .then(response => response.json())
                .then(data => {
                    this.user = {
                        name: data.name || '',
                        email: data.email || '',
                        picture: data.picture || '',
                        isAdmin: data.isAdmin || false,
                        roles: data.roles || ''
                    };
                })
                .catch(error => {
                    console.error('Error fetching user:', error);
                });
        },

        // ===== Sidebar Methods =====
        toggleSidebar() {
            this.sidebarOpen = !this.sidebarOpen;
        },

        toggleDataMenu() {
            this.dataMenuOpen = !this.dataMenuOpen;
        },

        toggleClassMenu() {
            this.classMenuOpen = !this.classMenuOpen;
        },

        setActivePage(page) {
            this.activePage = page;

            // Load data when page changes
            if (page === 'Dashboard') {
                this.loadStatistics();
            } else if (page === 'Mahasiswa') {
                this.loadStudents();
            } else if (page === 'MataKuliah') {
                this.loadSubjects();
            } else if (page === 'AddClass') {
                this.loadSubjectOptions();
                this.showClassModal();
            } else if (page === 'ViewClasses') {
                this.loadClasses();
            }
        },

        // Merge methods from modules
        ...studentModule.methods,
        ...subjectModule.methods,
        ...classModule.methods,
        ...dashboardModule.methods
    },

    mounted() {
        // Load initial data when app starts
        this.fetchUser();
        this.loadMajorOptions();
        this.loadStatistics();
    }
});

// Mount the app
app.mount('#app');
