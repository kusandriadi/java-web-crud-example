/**
 * Student Module
 * Handles all student-related operations (CRUD)
 */
const studentModule = {
    // Student data
    data: {
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
        majorOptions: []
    },

    // Student methods
    methods: {
        /**
         * Load all students from API
         */
        loadStudents() {
            fetch('/api/students')
                .then(response => response.json())
                .then(data => {
                    this.studentList = data;
                })
                .catch(error => {
                    console.error('Error loading students:', error);
                    this.showNotification('error', 'Gagal memuat data mahasiswa');
                });
        },

        /**
         * Load major options for dropdown
         */
        loadMajorOptions() {
            fetch('/api/students/major-options')
                .then(response => response.json())
                .then(data => {
                    this.majorOptions = data.options;
                })
                .catch(error => {
                    console.error('Error loading major options:', error);
                });
        },

        /**
         * Show student modal (for add or edit)
         */
        showStudentModal(student) {
            if (student) {
                // Edit mode - assign all student data at once for better reactivity
                Object.assign(this.studentForm, {
                    id: student.id,
                    nim: student.nim,
                    name: student.name,
                    email: student.email,
                    major: student.major,
                    batch: student.batch,
                    status: student.status
                });
            } else {
                // Add mode - reset form
                Object.assign(this.studentForm, {
                    id: null,
                    nim: '',
                    name: '',
                    email: '',
                    major: '',
                    batch: null,
                    status: ''
                });
            }

            // Show Bootstrap modal with slight delay to ensure Vue has updated the DOM
            setTimeout(() => {
                const modalElement = document.getElementById('studentModal');
                if (modalElement) {
                    const modal = new bootstrap.Modal(modalElement);
                    modal.show();
                }
            }, 10);
        },

        /**
         * Edit student (opens modal with student data)
         */
        editStudent(student) {
            this.showStudentModal(student);
        },

        /**
         * Save student (create or update)
         */
        saveStudent() {
            // Determine URL and method based on whether we're creating or updating
            const url = this.studentForm.id
                ? `/api/students/${this.studentForm.id}`  // Update
                : '/api/students';                         // Create

            const method = this.studentForm.id ? 'PUT' : 'POST';

            // Send request to API
            fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.studentForm)
            })
            .then(response => {
                if (response.ok) {
                    this.showNotification('success', 'Data mahasiswa berhasil disimpan');
                    bootstrap.Modal.getInstance(document.getElementById('studentModal')).hide();
                    this.loadStudents();
                } else {
                    this.showNotification('error', 'Gagal menyimpan data mahasiswa');
                }
            })
            .catch(error => {
                console.error('Error saving student:', error);
                this.showNotification('error', 'Terjadi kesalahan saat menyimpan data');
            });
        },

        /**
         * Delete student by ID
         */
        deleteStudent(id) {
            this.showConfirm('Yakin ingin menghapus data mahasiswa ini?', () => {
                fetch(`/api/students/${id}`, { method: 'DELETE' })
                    .then(response => {
                        if (response.ok) {
                            this.showNotification('success', 'Data mahasiswa berhasil dihapus');
                            this.loadStudents();
                        } else {
                            this.showNotification('error', 'Gagal menghapus data mahasiswa');
                        }
                    })
                    .catch(error => {
                        console.error('Error deleting student:', error);
                        this.showNotification('error', 'Terjadi kesalahan saat menghapus data');
                    });
            });
        }
    }
};
