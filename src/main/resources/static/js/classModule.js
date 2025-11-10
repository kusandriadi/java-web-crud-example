/**
 * Class Module
 * Handles all class-related operations (CRUD, student management)
 */
const classModule = {
    // Class data
    data: {
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
        availableStudents: [],  // All students for selection
        enrolledStudents: [],    // Students currently in the class
        subjectOptions: []       // All subjects for dropdown
    },

    // Class methods
    methods: {
        /**
         * Load all classes from API
         */
        loadClasses() {
            fetch('/api/classes')
                .then(response => response.json())
                .then(data => {
                    this.classList = data;
                })
                .catch(error => {
                    console.error('Error loading classes:', error);
                    this.showNotification('error', 'Gagal memuat data kelas');
                });
        },

        /**
         * Load all subjects for dropdown
         */
        loadSubjectOptions() {
            fetch('/api/subjects')
                .then(response => response.json())
                .then(data => {
                    this.subjectOptions = data;
                })
                .catch(error => {
                    console.error('Error loading subjects:', error);
                });
        },

        /**
         * Load all students for selection
         */
        loadAvailableStudents() {
            fetch('/api/students')
                .then(response => response.json())
                .then(data => {
                    this.availableStudents = data;
                })
                .catch(error => {
                    console.error('Error loading students:', error);
                });
        },

        /**
         * Show class modal (for add or edit)
         */
        showClassModal(classRoom) {
            // Load subjects first
            this.loadSubjectOptions();

            if (classRoom) {
                // Edit mode - copy class data with all fields
                this.classForm.id = classRoom.id;
                this.classForm.code = classRoom.code;
                this.classForm.name = classRoom.name;
                this.classForm.subjectId = classRoom.subjectId;
                this.classForm.subjectName = classRoom.subjectName;
                this.classForm.semester = classRoom.semester;
                this.classForm.year = classRoom.year;
                this.classForm.studentIds = classRoom.studentIds || [];
                // Load enrolled students
                this.loadEnrolledStudents(classRoom.id);
            } else {
                // Add mode - reset form
                this.classForm.id = null;
                this.classForm.code = '';
                this.classForm.name = '';
                this.classForm.subjectId = '';
                this.classForm.subjectName = '';
                this.classForm.semester = '';
                this.classForm.year = new Date().getFullYear();
                this.classForm.studentIds = [];
                this.enrolledStudents = [];
            }

            // Load available students
            this.loadAvailableStudents();

            // Show Bootstrap modal
            const modal = new bootstrap.Modal(document.getElementById('classModal'));
            modal.show();
        },

        /**
         * Load enrolled students for a class
         */
        loadEnrolledStudents(classId) {
            fetch(`/api/classes/${classId}`)
                .then(response => response.json())
                .then(classData => {
                    // Fetch full student details
                    if (classData.studentIds && classData.studentIds.length > 0) {
                        fetch('/api/students')
                            .then(response => response.json())
                            .then(allStudents => {
                                this.enrolledStudents = allStudents.filter(s =>
                                    classData.studentIds.includes(s.id)
                                );
                            });
                    } else {
                        this.enrolledStudents = [];
                    }
                })
                .catch(error => {
                    console.error('Error loading enrolled students:', error);
                });
        },

        /**
         * Update class name based on subject, year, and semester
         */
        updateClassName() {
            if (this.classForm.subjectId && this.classForm.year && this.classForm.semester) {
                const selectedSubject = this.subjectOptions.find(s => s.id === this.classForm.subjectId);
                if (selectedSubject) {
                    this.classForm.subjectName = selectedSubject.name;
                    // Format: "Subject Name - Year - Semester"
                    this.classForm.name = `${selectedSubject.name} - ${this.classForm.year} - ${this.classForm.semester}`;
                }
            }
        },

        /**
         * Save class (create or update)
         */
        saveClass() {
            // Ensure class name is set
            if (!this.classForm.name && this.classForm.subjectId && this.classForm.year && this.classForm.semester) {
                this.updateClassName();
            }

            // Set subject name based on selected subject
            if (this.classForm.subjectId && !this.classForm.subjectName) {
                const selectedSubject = this.subjectOptions.find(s => s.id === this.classForm.subjectId);
                if (selectedSubject) {
                    this.classForm.subjectName = selectedSubject.name;
                }
            }

            // Determine URL and method
            const url = this.classForm.id
                ? `/api/classes/${this.classForm.id}`
                : '/api/classes';

            const method = this.classForm.id ? 'PUT' : 'POST';

            // Send request to API
            fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.classForm)
            })
            .then(response => {
                if (response.ok) {
                    this.showNotification('success', 'Data kelas berhasil disimpan');
                    bootstrap.Modal.getInstance(document.getElementById('classModal')).hide();
                    this.loadClasses();
                } else {
                    this.showNotification('error', 'Gagal menyimpan data kelas');
                }
            })
            .catch(error => {
                console.error('Error saving class:', error);
                this.showNotification('error', 'Terjadi kesalahan saat menyimpan data');
            });
        },

        /**
         * Show modal to manage students in a class
         */
        manageStudents(classRoom) {
            // Copy class data with all fields
            this.classForm.id = classRoom.id;
            this.classForm.code = classRoom.code;
            this.classForm.name = classRoom.name;
            this.classForm.subjectId = classRoom.subjectId;
            this.classForm.subjectName = classRoom.subjectName;
            this.classForm.semester = classRoom.semester;
            this.classForm.year = classRoom.year;
            this.classForm.studentIds = classRoom.studentIds || [];

            this.loadEnrolledStudents(classRoom.id);
            this.loadAvailableStudents();

            const modal = new bootstrap.Modal(document.getElementById('manageStudentsModal'));
            modal.show();
        },

        /**
         * Add student to class
         */
        addStudentToClass(studentId) {
            if (!this.classForm.id) return;

            fetch(`/api/classes/${this.classForm.id}/students/${studentId}`, {
                method: 'POST'
            })
            .then(response => {
                if (response.ok) {
                    this.showNotification('success', 'Mahasiswa berhasil ditambahkan ke kelas');
                    this.loadEnrolledStudents(this.classForm.id);
                    this.loadClasses();  // Refresh class list
                } else {
                    this.showNotification('error', 'Gagal menambahkan mahasiswa');
                }
            })
            .catch(error => {
                console.error('Error adding student to class:', error);
                this.showNotification('error', 'Terjadi kesalahan');
            });
        },

        /**
         * Remove student from class
         */
        removeStudentFromClass(studentId) {
            if (!this.classForm.id) return;

            this.showConfirm('Yakin ingin menghapus mahasiswa ini dari kelas?', () => {
                fetch(`/api/classes/${this.classForm.id}/students/${studentId}`, {
                    method: 'DELETE'
                })
                .then(response => {
                    if (response.ok) {
                        this.showNotification('success', 'Mahasiswa berhasil dihapus dari kelas');
                        this.loadEnrolledStudents(this.classForm.id);
                        this.loadClasses();  // Refresh class list
                    } else {
                        this.showNotification('error', 'Gagal menghapus mahasiswa');
                    }
                })
                .catch(error => {
                    console.error('Error removing student from class:', error);
                    this.showNotification('error', 'Terjadi kesalahan');
                });
            });
        },

        /**
         * Get students not enrolled in current class
         */
        getUnenrolledStudents() {
            const enrolledIds = this.enrolledStudents.map(s => s.id);
            return this.availableStudents.filter(s => !enrolledIds.includes(s.id));
        }
    }
};
