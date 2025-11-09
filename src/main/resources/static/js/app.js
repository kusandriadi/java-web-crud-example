// Create Vue Application
const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            // User data
            user: {
                name: '',
                email: '',
                picture: ''
            },

            // UI state
            sidebarOpen: true,
            activePage: 'Dashboard',
            dataMenuOpen: false,

            // Dashboard stats
            stats: {
                siTotal: 0,
                tiTotal: 0,
                siActive: 0,
                tiActive: 0,
                siNotActive: 0,
                tiNotActive: 0,
                totalStudents: 0
            },

            // Student data
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
            jurusanOptions: [],

            // Subject data
            subjectList: [],
            subjectForm: {
                id: null,
                code: '',
                name: '',
                sks: null
            }
        }
    },

    methods: {
        // ===== User Methods =====
        fetchUser() {
            fetch('/api/user')
                .then(response => response.json())
                .then(data => {
                    this.user = data;
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        },

        // ===== Sidebar Methods =====
        toggleSidebar() {
            this.sidebarOpen = !this.sidebarOpen;
        },

        toggleDataMenu() {
            this.dataMenuOpen = !this.dataMenuOpen;
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
            }
        },

        // ===== Dashboard Methods =====
        loadStatistics() {
            fetch('/api/students/statistics')
                .then(response => response.json())
                .then(data => {
                    this.stats = data;
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        },

        // ===== Student Methods =====
        loadStudents() {
            fetch('/api/students')
                .then(response => response.json())
                .then(data => {
                    this.studentList = data;
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Gagal memuat data mahasiswa');
                });
        },

        loadMajorOptions() {
            fetch('/api/students/major-options')
                .then(response => response.json())
                .then(data => {
                    this.jurusanOptions = data.options;
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        },

        showStudentModal(student) {
            if (student) {
                // Edit mode - copy student data
                this.studentForm = { ...student };
            } else {
                // Add mode - reset form
                this.studentForm = {
                    id: null,
                    nim: '',
                    name: '',
                    email: '',
                    major: '',
                    batch: null,
                    status: ''
                };
            }

            // Show modal
            const modal = new bootstrap.Modal(document.getElementById('studentModal'));
            modal.show();
        },

        editStudent(student) {
            this.showStudentModal(student);
        },

        saveStudent() {
            const url = this.studentForm.id
                ? `/api/students/${this.studentForm.id}`  // Update
                : '/api/students';                         // Create

            const method = this.studentForm.id ? 'PUT' : 'POST';

            fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.studentForm)
            })
            .then(response => {
                if (response.ok) {
                    alert('Data berhasil disimpan');
                    bootstrap.Modal.getInstance(document.getElementById('studentModal')).hide();
                    this.loadStudents();
                } else {
                    alert('Gagal menyimpan data');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Terjadi kesalahan');
            });
        },

        deleteStudent(id) {
            if (!confirm('Yakin ingin menghapus data ini?')) {
                return;
            }

            fetch(`/api/students/${id}`, { method: 'DELETE' })
                .then(response => {
                    if (response.ok) {
                        alert('Data berhasil dihapus');
                        this.loadStudents();
                    } else {
                        alert('Gagal menghapus data');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Terjadi kesalahan');
                });
        },

        // ===== Subject Methods =====
        loadSubjects() {
            fetch('/api/subjects')
                .then(response => response.json())
                .then(data => {
                    this.subjectList = data;
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Gagal memuat data mata kuliah');
                });
        },

        showSubjectModal(subject) {
            if (subject) {
                // Edit mode - copy subject data
                this.subjectForm = { ...subject };
            } else {
                // Add mode - reset form
                this.subjectForm = {
                    id: null,
                    code: '',
                    name: '',
                    sks: null
                };
            }

            // Show modal
            const modal = new bootstrap.Modal(document.getElementById('subjectModal'));
            modal.show();
        },

        editSubject(subject) {
            this.showSubjectModal(subject);
        },

        saveSubject() {
            const url = this.subjectForm.id
                ? `/api/subjects/${this.subjectForm.id}`  // Update
                : '/api/subjects';                         // Create

            const method = this.subjectForm.id ? 'PUT' : 'POST';

            fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.subjectForm)
            })
            .then(response => {
                if (response.ok) {
                    alert('Data berhasil disimpan');
                    bootstrap.Modal.getInstance(document.getElementById('subjectModal')).hide();
                    this.loadSubjects();
                } else {
                    alert('Gagal menyimpan data');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Terjadi kesalahan');
            });
        },

        deleteSubject(id) {
            if (!confirm('Yakin ingin menghapus data ini?')) {
                return;
            }

            fetch(`/api/subjects/${id}`, { method: 'DELETE' })
                .then(response => {
                    if (response.ok) {
                        alert('Data berhasil dihapus');
                        this.loadSubjects();
                    } else {
                        alert('Gagal menghapus data');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Terjadi kesalahan');
                });
        }
    },

    mounted() {
        // Load data when app starts
        this.fetchUser();
        this.loadMajorOptions();
        this.loadStatistics();
    }
});

// Mount the app
app.mount('#app');
