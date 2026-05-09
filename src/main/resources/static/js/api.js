/* ============================================
   HealthTech API Service Layer v2
   Tüm backend API çağrılarını yöneten modül
   ============================================ */

const API_BASE = '/api';

const Api = {
    token: localStorage.getItem('ht_token') || null,

    setToken(t) {
        this.token = t;
        localStorage.setItem('ht_token', t);
    },
    clearToken() {
        this.token = null;
        localStorage.removeItem('ht_token');
        localStorage.removeItem('ht_user');
    },

    headers() {
        const h = { 'Content-Type': 'application/json' };
        if (this.token) h['Authorization'] = 'Bearer ' + this.token;
        return h;
    },

    async request(method, path, body) {
        const opts = { method, headers: this.headers() };
        if (body !== undefined) opts.body = JSON.stringify(body);

        let res;
        try {
            res = await fetch(API_BASE + path, opts);
        } catch (e) {
            throw new Error('Sunucuya ulaşılamıyor. İnternet bağlantınızı kontrol edin.');
        }

        // Oturum süresi dolmuş
        if (res.status === 401 && path !== '/auth/login' && path !== '/auth/register') {
            Api.clearToken();
            App.toast('Oturumunuz sona erdi. Lütfen tekrar giriş yapın.', 'error');
            setTimeout(() => window.location.reload(), 1500);
            return null;
        }

        // Boş response (204 No Content gibi)
        if (res.status === 204 || res.headers.get('content-length') === '0') return null;

        const text = await res.text();
        if (!text) return null;

        let data;
        try { data = JSON.parse(text); } catch { data = text; }

        if (!res.ok) {
            const msg = typeof data === 'object'
                ? (data.message || data.error || data.title || 'Bir hata oluştu')
                : (data || 'Bir hata oluştu');
            throw new Error(msg);
        }
        return data;
    },

    // ===== AUTH =====
    login(email, password) { return this.request('POST', '/auth/login', { email, password }); },
    register(data) { return this.request('POST', '/auth/register', data); },

    // ===== USERS =====
    getMe() { return this.request('GET', '/users/me'); },
    getAllUsers() { return this.request('GET', '/users'); },
    getUserById(id) { return this.request('GET', '/users/' + id); },
    getDoctors() { return this.request('GET', '/users/doctors'); },
    updateUser(id, data) { return this.request('PUT', '/users/' + id, data); },
    deleteUser(id) { return this.request('DELETE', '/users/' + id); },

    // ===== APPOINTMENTS =====
    createAppointment(data) { return this.request('POST', '/appointments', data); },
    getMyAppointments() { return this.request('GET', '/appointments/my'); },
    getMyDoctorAppointments() { return this.request('GET', '/appointments/doctor/my'); },
    getPatientAppointments(patientId) { return this.request('GET', '/appointments/patient/' + patientId); },
    getDoctorAppointments(doctorId) { return this.request('GET', '/appointments/doctor/' + doctorId); },
    approveAppointment(id) { return this.request('PUT', '/appointments/' + id + '/approve'); },
    cancelAppointment(id) { return this.request('PUT', '/appointments/' + id + '/cancel'); },
    completeAppointment(id) { return this.request('PUT', '/appointments/' + id + '/complete'); },

    // ===== PRESCRIPTIONS =====
    createPrescription(data) { return this.request('POST', '/prescriptions', data); },
    getMyPrescriptions() { return this.request('GET', '/prescriptions/my'); },

    // ===== MEDICAL RECORDS =====
    getMyMedicalRecord() { return this.request('GET', '/medical-records/my'); },
    updateMyMedicalRecord(data) { return this.request('POST', '/medical-records/my', data); },
    getPatientMedicalRecord(patientId) { return this.request('GET', '/medical-records/patient/' + patientId); },

    // ===== REPORTS =====
    getDashboardReport() { return this.request('GET', '/reports/dashboard'); },
};
