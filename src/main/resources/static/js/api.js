/* ============================================
   HealthTech API Service Layer v3
   Tüm backend API çağrılarını yöneten modül
   Faz 1: Profil, adres, acil durum, sigorta API'leri eklendi
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
    forgotPassword(email) { return this.request('POST', '/auth/forgot-password', { email }); },
    changePassword(currentPassword, newPassword) {
        return this.request('PUT', '/auth/change-password', { currentPassword, newPassword });
    },

    // ===== USERS =====
    getMe() { return this.request('GET', '/users/me'); },
    getAllUsers() { return this.request('GET', '/users'); },
    getUserById(id) { return this.request('GET', '/users/' + id); },
    getDoctors() { return this.request('GET', '/users/doctors'); },
    updateUser(id, data) { return this.request('PUT', '/users/' + id, data); },
    deleteUser(id) { return this.request('DELETE', '/users/' + id); },

    // ===== PROFILE (Faz 1) =====
    getFullProfile() { return this.request('GET', '/profile/me'); },
    updateProfile(data) { return this.request('PUT', '/profile/me', data); },

    // Adres
    getAddress() { return this.request('GET', '/profile/address'); },
    saveAddress(data) { return this.request('POST', '/profile/address', data); },

    // Acil Durum Kişileri
    getEmergencyContacts() { return this.request('GET', '/profile/emergency-contacts'); },
    addEmergencyContact(data) { return this.request('POST', '/profile/emergency-contacts', data); },
    deleteEmergencyContact(id) { return this.request('DELETE', '/profile/emergency-contacts/' + id); },

    // Sigorta
    getInsurance() { return this.request('GET', '/profile/insurance'); },
    saveInsurance(data) { return this.request('POST', '/profile/insurance', data); },

    // Hesap Yönetimi
    freezeAccount() { return this.request('PUT', '/profile/freeze'); },
    deleteAccount() { return this.request('DELETE', '/profile/delete-account'); },

    // ===== DOCTOR PROFILES (Faz 2) =====
    getMyDoctorProfile() { return this.request('GET', '/doctor-profiles/me'); },
    saveDoctorProfile(data) { return this.request('POST', '/doctor-profiles/me', data); },
    searchDoctors2(specialization) {
        const q = specialization ? '?specialization=' + encodeURIComponent(specialization) : '';
        return this.request('GET', '/doctor-profiles/search' + q);
    },
    getDoctorDetail(profileId) { return this.request('GET', '/doctor-profiles/' + profileId); },
    getPendingDoctors() { return this.request('GET', '/doctor-profiles/admin/pending'); },
    approveDoctorProfile(profileId) { return this.request('PUT', '/doctor-profiles/admin/' + profileId + '/approve'); },
    rejectDoctorProfile(profileId, reason) { return this.request('PUT', '/doctor-profiles/admin/' + profileId + '/reject', { reason }); },
    addDoctorCertificate(data) { return this.request('POST', '/doctor-profiles/certificates', data); },
    deleteDoctorCertificate(certId) { return this.request('DELETE', '/doctor-profiles/certificates/' + certId); },
    addDoctorReview(data) { return this.request('POST', '/doctor-profiles/reviews', data); },
    getDoctorReviews(profileId) { return this.request('GET', '/doctor-profiles/' + profileId + '/reviews'); },

    // ===== APPOINTMENTS =====
    createAppointment(data) { return this.request('POST', '/appointments', data); },
    getMyAppointments() { return this.request('GET', '/appointments/my'); },
    getMyDoctorAppointments() { return this.request('GET', '/appointments/doctor/my'); },
    getPatientAppointments(patientId) { return this.request('GET', '/appointments/patient/' + patientId); },
    getDoctorAppointments(doctorId) { return this.request('GET', '/appointments/doctor/' + doctorId); },
    approveAppointment(id) { return this.request('PUT', '/appointments/' + id + '/approve'); },
    cancelAppointment(id) { return this.request('PUT', '/appointments/' + id + '/cancel'); },
    completeAppointment(id) { return this.request('PUT', '/appointments/' + id + '/complete'); },
    rescheduleAppointment(id, newDate) { return this.request('PUT', '/appointments/' + id + '/reschedule', { newDate }); },

    // Doktor takvimi
    getDoctorSchedule(doctorId) { return this.request('GET', '/appointments/schedule/' + doctorId); },
    saveSchedule(data) { return this.request('POST', '/appointments/schedule', data); },
    getAvailableSlots(doctorId, date) { return this.request('GET', '/appointments/available-slots/' + doctorId + '?date=' + date); },
    getNearestSlot(doctorId) { return this.request('GET', '/appointments/nearest-slot/' + doctorId); },
    getWeekAvailability(doctorId, startDate) { return this.request('GET', '/appointments/week-availability/' + doctorId + '?startDate=' + startDate); },
    addDayOff(data) { return this.request('POST', '/appointments/day-off', data); },
    removeDayOff(date) { return this.request('DELETE', '/appointments/day-off?date=' + date); },
    getDayOffs() { return this.request('GET', '/appointments/day-offs'); },

    // ===== NOTIFICATIONS (Faz 4) =====
    getNotifications() { return this.request('GET', '/notifications'); },
    getUnreadCount() { return this.request('GET', '/notifications/unread-count'); },
    markNotificationRead(id) { return this.request('PUT', '/notifications/' + id + '/read'); },
    markAllNotificationsRead() { return this.request('PUT', '/notifications/read-all'); },

    // ===== MESSAGES (Faz 5) =====
    sendMessage(receiverId, content) { return this.request('POST', '/messages', { receiverId, content }); },
    getConversations() { return this.request('GET', '/messages/conversations'); },
    getConversation(partnerId) { return this.request('GET', '/messages/conversation/' + partnerId); },
    getMessageUnreadCount() { return this.request('GET', '/messages/unread-count'); },

    // ===== PRESCRIPTIONS =====
    createPrescription(data) { return this.request('POST', '/prescriptions', data); },
    getMyPrescriptions() { return this.request('GET', '/prescriptions/my'); },

    // ===== MEDICAL RECORDS =====
    getMyMedicalRecord() { return this.request('GET', '/medical-records/my'); },
    updateMyMedicalRecord(data) { return this.request('POST', '/medical-records/my', data); },
    getPatientMedicalRecord(patientId) { return this.request('GET', '/medical-records/patient/' + patientId); },

    // Faz 4: Lab sonuçları
    getMyLabResults() { return this.request('GET', '/medical-records/lab-results/my'); },
    getPatientLabResults(patientId) { return this.request('GET', '/medical-records/lab-results/patient/' + patientId); },
    addLabResult(patientId, data) { return this.request('POST', '/medical-records/lab-results/' + patientId, data); },

    // Faz 5: Gelişmiş reçete
    getDoctorPrescriptions() { return this.request('GET', '/prescriptions/doctor/my'); },
    updatePrescriptionStatus(id, status) { return this.request('PUT', '/prescriptions/' + id + '/status', { status }); },

    // ===== VIDEO CALLS (Faz 9) =====
    createVideoCall(data) { return this.request('POST', '/video-calls', data); },
    joinVideoCall(roomId) { return this.request('POST', '/video-calls/join/' + roomId); },
    endVideoCall(roomId) { return this.request('POST', '/video-calls/end/' + roomId); },
    saveCallNotes(callId, notes) { return this.request('PUT', '/video-calls/' + callId + '/notes', { notes }); },
    getMyCalls() { return this.request('GET', '/video-calls/my'); },
    getActiveCalls() { return this.request('GET', '/video-calls/active'); },
    getCallByRoom(roomId) { return this.request('GET', '/video-calls/room/' + roomId); },

    // ===== EMERGENCY (Faz 10) =====
    createEmergency(data) { return this.request('POST', '/emergency', data); },
    respondEmergency(id) { return this.request('PUT', '/emergency/' + id + '/respond'); },
    resolveEmergency(id, notes) { return this.request('PUT', '/emergency/' + id + '/resolve', { notes }); },
    cancelEmergency(id) { return this.request('PUT', '/emergency/' + id + '/cancel'); },
    getMyEmergencies() { return this.request('GET', '/emergency/my'); },
    getPendingEmergencies() { return this.request('GET', '/emergency/pending'); },
    getAllEmergencies() { return this.request('GET', '/emergency/all'); },
    getAssignedEmergencies() { return this.request('GET', '/emergency/assigned'); },
    getPendingEmergencyCount() { return this.request('GET', '/emergency/pending-count'); },

    // ===== ADMIN PANEL (Faz 11) =====
    getAdminReport() { return this.request('GET', '/admin/report'); },
    getAdminUsers() { return this.request('GET', '/admin/users'); },
    getAdminUsersByRole(role) { return this.request('GET', '/admin/users/role/' + role); },
    changeUserStatus(userId, status) { return this.request('PUT', '/admin/users/' + userId + '/status', { status }); },
    changeUserRole(userId, role) { return this.request('PUT', '/admin/users/' + userId + '/role', { role }); },
    deleteAdminUser(userId) { return this.request('DELETE', '/admin/users/' + userId); },
    getAuditLogs() { return this.request('GET', '/admin/audit-logs'); },
    getAuditLogsByUser(email) { return this.request('GET', '/admin/audit-logs/user/' + email); },
    broadcastNotification(message, role) { return this.request('POST', '/admin/broadcast', { message, role }); },

    // ===== REPORTS =====
    getDashboardReport() { return this.request('GET', '/reports/dashboard'); },
};
