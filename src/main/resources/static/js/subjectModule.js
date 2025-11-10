/**
 * Subject Module
 * Handles all subject-related operations (CRUD)
 */
const subjectModule = {
    // Subject data
    data: {
        subjectList: [],
        subjectForm: {
            id: null,
            code: '',
            name: '',
            major: '',
            sks: null
        }
    },

    // Subject methods
    methods: {
        /**
         * Load all subjects from API
         */
        loadSubjects() {
            fetch('/api/subjects')
                .then(response => response.json())
                .then(data => {
                    this.subjectList = data;
                })
                .catch(error => {
                    console.error('Error loading subjects:', error);
                    this.showNotification('error', 'Gagal memuat data mata kuliah');
                });
        },

        /**
         * Show subject modal (for add or edit)
         */
        showSubjectModal(subject) {
            if (subject) {
                // Edit mode - copy subject data with all fields
                this.subjectForm.id = subject.id;
                this.subjectForm.code = subject.code;
                this.subjectForm.name = subject.name;
                this.subjectForm.major = subject.major;
                this.subjectForm.sks = subject.sks;
            } else {
                // Add mode - reset form
                this.subjectForm.id = null;
                this.subjectForm.code = '';
                this.subjectForm.name = '';
                this.subjectForm.major = '';
                this.subjectForm.sks = null;
            }

            // Show Bootstrap modal
            const modal = new bootstrap.Modal(document.getElementById('subjectModal'));
            modal.show();
        },

        /**
         * Edit subject (opens modal with subject data)
         */
        editSubject(subject) {
            this.showSubjectModal(subject);
        },

        /**
         * Save subject (create or update)
         */
        saveSubject() {
            // Determine URL and method based on whether we're creating or updating
            const url = this.subjectForm.id
                ? `/api/subjects/${this.subjectForm.id}`  // Update
                : '/api/subjects';                         // Create

            const method = this.subjectForm.id ? 'PUT' : 'POST';

            // Send request to API
            fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.subjectForm)
            })
            .then(response => {
                if (response.ok) {
                    this.showNotification('success', 'Data mata kuliah berhasil disimpan');
                    bootstrap.Modal.getInstance(document.getElementById('subjectModal')).hide();
                    this.loadSubjects();
                } else {
                    this.showNotification('error', 'Gagal menyimpan data mata kuliah');
                }
            })
            .catch(error => {
                console.error('Error saving subject:', error);
                this.showNotification('error', 'Terjadi kesalahan saat menyimpan data');
            });
        },

        /**
         * Delete subject by ID
         */
        deleteSubject(id) {
            this.showConfirm('Yakin ingin menghapus data mata kuliah ini?', () => {
                fetch(`/api/subjects/${id}`, { method: 'DELETE' })
                    .then(response => {
                        if (response.ok) {
                            this.showNotification('success', 'Data mata kuliah berhasil dihapus');
                            this.loadSubjects();
                        } else {
                            this.showNotification('error', 'Gagal menghapus data mata kuliah');
                        }
                    })
                    .catch(error => {
                        console.error('Error deleting subject:', error);
                        this.showNotification('error', 'Terjadi kesalahan saat menghapus data');
                    });
            });
        }
    }
};
