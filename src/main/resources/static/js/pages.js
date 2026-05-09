/* ============================================
   HealthTech Pages Module
   Tüm sayfa render fonksiyonları
   ============================================ */

const Pages = {

    // =================== PATIENT DASHBOARD ===================
    async patientDashboard(user) {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="stats-grid" id="p-stats"></div><div class="card"><div class="card-header"><h2><i class="fa-solid fa-calendar-check"></i> Randevularım</h2><button class="btn btn-primary btn-sm" id="btn-new-appt"><i class="fa-solid fa-plus"></i> Yeni Randevu</button></div><div class="card-body"><div class="table-responsive" id="p-appt-table"></div></div></div><div class="card"><div class="card-header"><h2><i class="fa-solid fa-pills"></i> Reçetelerim</h2></div><div class="card-body" id="p-presc-list"></div></div>';

        document.getElementById('btn-new-appt').addEventListener('click', () => this.showNewAppointmentModal());

        try {
            const [appts, prescs] = await Promise.all([
                Api.getMyAppointments(user.id).catch(() => []),
                Api.getMyPrescriptions().catch(() => [])
            ]);

            const pending = appts.filter(a => a.status === 'PENDING').length;
            const approved = appts.filter(a => a.status === 'APPROVED').length;
            const completed = appts.filter(a => a.status === 'COMPLETED').length;

            document.getElementById('p-stats').innerHTML = `
                <div class="stat-card primary"><div class="stat-icon primary"><i class="fa-solid fa-calendar"></i></div><div class="stat-info"><h3>${appts.length}</h3><p>Toplam Randevu</p></div></div>
                <div class="stat-card warning"><div class="stat-icon warning"><i class="fa-solid fa-clock"></i></div><div class="stat-info"><h3>${pending}</h3><p>Bekleyen</p></div></div>
                <div class="stat-card success"><div class="stat-icon success"><i class="fa-solid fa-check-circle"></i></div><div class="stat-info"><h3>${approved}</h3><p>Onaylanan</p></div></div>
                <div class="stat-card info"><div class="stat-icon info"><i class="fa-solid fa-pills"></i></div><div class="stat-info"><h3>${prescs.length}</h3><p>Reçete</p></div></div>`;

            this.renderAppointmentTable(appts, 'p-appt-table', 'patient');
            this.renderPrescriptionList(prescs, 'p-presc-list');
        } catch (err) { App.toast(err.message, 'error'); }
    },

    // =================== DOCTOR DASHBOARD ===================
    async doctorDashboard(user) {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="stats-grid" id="d-stats"></div><div class="card"><div class="card-header"><h2><i class="fa-solid fa-calendar-check"></i> Randevularım</h2></div><div class="card-body"><div class="table-responsive" id="d-appt-table"></div></div></div>';

        try {
            const appts = await Api.getDoctorAppointments(user.id).catch(() => []);
            const pending = appts.filter(a => a.status === 'PENDING').length;
            const approved = appts.filter(a => a.status === 'APPROVED').length;
            const completed = appts.filter(a => a.status === 'COMPLETED').length;

            document.getElementById('d-stats').innerHTML = `
                <div class="stat-card primary"><div class="stat-icon primary"><i class="fa-solid fa-calendar"></i></div><div class="stat-info"><h3>${appts.length}</h3><p>Toplam Randevu</p></div></div>
                <div class="stat-card warning"><div class="stat-icon warning"><i class="fa-solid fa-clock"></i></div><div class="stat-info"><h3>${pending}</h3><p>Bekleyen Onay</p></div></div>
                <div class="stat-card success"><div class="stat-icon success"><i class="fa-solid fa-circle-check"></i></div><div class="stat-info"><h3>${approved}</h3><p>Onaylanan</p></div></div>
                <div class="stat-card info"><div class="stat-icon accent"><i class="fa-solid fa-check-double"></i></div><div class="stat-info"><h3>${completed}</h3><p>Tamamlanan</p></div></div>`;

            this.renderAppointmentTable(appts, 'd-appt-table', 'doctor');
        } catch (err) { App.toast(err.message, 'error'); }
    },

    // =================== ADMIN DASHBOARD ===================
    async adminDashboard() {
        const content = document.getElementById('page-content');
        content.innerHTML = `<div class="stats-grid" id="a-stats"></div>
            <div class="card">
                <div class="card-header" style="justify-content:space-between">
                    <h2><i class="fa-solid fa-users"></i> Tüm Kullanıcılar</h2>
                    <select id="a-role-filter" class="form-select" style="width:auto; padding:0.25rem 2rem 0.25rem 0.75rem;">
                        <option value="ALL">Tümü</option>
                        <option value="PATIENT">Hastalar</option>
                        <option value="DOCTOR">Doktorlar</option>
                        <option value="ADMIN">Yöneticiler</option>
                    </select>
                </div>
                <div class="card-body"><div class="table-responsive" id="a-users-table"></div></div>
            </div>`;

        try {
            const [report, users] = await Promise.all([
                Api.getDashboardReport().catch(() => ({})),
                Api.getAllUsers().catch(() => [])
            ]);

            document.getElementById('a-stats').innerHTML = `
                <div class="stat-card primary"><div class="stat-icon primary"><i class="fa-solid fa-users"></i></div><div class="stat-info"><h3>${report.totalUsers || 0}</h3><p>Toplam Kullanıcı</p></div></div>
                <div class="stat-card info"><div class="stat-icon accent"><i class="fa-solid fa-user-injured"></i></div><div class="stat-info"><h3>${report.totalPatients || 0}</h3><p>Hasta</p></div></div>
                <div class="stat-card success"><div class="stat-icon success"><i class="fa-solid fa-user-doctor"></i></div><div class="stat-info"><h3>${report.totalDoctors || 0}</h3><p>Doktor</p></div></div>
                <div class="stat-card warning"><div class="stat-icon warning"><i class="fa-solid fa-calendar"></i></div><div class="stat-info"><h3>${report.totalAppointments || 0}</h3><p>Toplam Randevu</p></div></div>
                <div class="stat-card"><div class="stat-icon info"><i class="fa-solid fa-pills"></i></div><div class="stat-info"><h3>${report.totalPrescriptions || 0}</h3><p>Reçete</p></div></div>
                <div class="stat-card"><div class="stat-icon danger"><i class="fa-solid fa-notes-medical"></i></div><div class="stat-info"><h3>${report.totalMedicalRecords || 0}</h3><p>Tıbbi Kayıt</p></div></div>`;

            this.renderUsersTable(users, 'a-users-table');
            
            document.getElementById('a-role-filter').addEventListener('change', (e) => {
                const val = e.target.value;
                const filtered = val === 'ALL' ? users : users.filter(u => u.role === val);
                this.renderUsersTable(filtered, 'a-users-table');
            });
        } catch (err) { App.toast(err.message, 'error'); }
    },

    // =================== PROFILE PAGE ===================
    async profilePage(user) {
        const content = document.getElementById('page-content');
        content.innerHTML = `
            <div class="card">
                <div class="card-header" style="justify-content:space-between">
                    <h2><i class="fa-solid fa-user"></i> Profilim</h2>
                    <button class="btn btn-primary btn-sm" id="btn-edit-profile"><i class="fa-solid fa-pen"></i> Düzenle</button>
                </div>
                <div class="card-body profile-card">
                    <div class="profile-avatar"><i class="fa-solid fa-user"></i></div>
                    <h2>${user.fullName}</h2>
                    <p class="email">${user.email}</p>
                    <span class="badge badge-${user.role.toLowerCase()}">${this.roleLabel(user.role)}</span>
                    <div class="info-grid">
                        <div class="info-item"><label>ID</label><span>${user.id}</span></div>
                        <div class="info-item"><label>Telefon</label><span>${user.phone || '—'}</span></div>
                        <div class="info-item"><label>E-posta</label><span>${user.email}</span></div>
                        <div class="info-item"><label>Rol</label><span>${this.roleLabel(user.role)}</span></div>
                    </div>
                </div>
            </div>`;
        document.getElementById('btn-edit-profile').addEventListener('click', () => this.showProfileEditModal(user));
    },

    // =================== MEDICAL RECORD PAGE (PATIENT) ===================
    async medicalRecordPage() {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="card"><div class="card-header"><h2><i class="fa-solid fa-notes-medical"></i> Tıbbi Kaydım</h2><button class="btn btn-primary btn-sm" id="btn-edit-med">Düzenle</button></div><div class="card-body" id="med-body"><p style="color:var(--text-secondary)">Yükleniyor...</p></div></div>';

        try {
            const rec = await Api.getMyMedicalRecord().catch(() => null);
            const body = document.getElementById('med-body');
            if (rec) {
                body.innerHTML = `<div class="info-grid">
                    <div class="info-item"><label>Kan Grubu</label><span>${rec.bloodGroup || '—'}</span></div>
                    <div class="info-item"><label>Alerjiler</label><span>${rec.allergies || '—'}</span></div>
                    <div class="info-item"><label>Geçmiş Hastalıklar</label><span>${rec.pastDiseases || '—'}</span></div>
                    <div class="info-item"><label>Boy (cm)</label><span>${rec.height || '—'}</span></div>
                    <div class="info-item"><label>Kilo (kg)</label><span>${rec.weight || '—'}</span></div>
                    <div class="info-item"><label>Son Güncelleme</label><span>${rec.updatedAt ? new Date(rec.updatedAt).toLocaleDateString('tr-TR') : '—'}</span></div>
                </div>`;
            } else {
                body.innerHTML = '<div class="empty-state"><i class="fa-solid fa-notes-medical"></i><h3>Henüz tıbbi kayıt yok</h3><p>Tıbbi bilgilerinizi eklemek için "Düzenle" butonuna tıklayın.</p></div>';
            }
            document.getElementById('btn-edit-med').addEventListener('click', () => this.showMedicalRecordModal(rec));
        } catch (err) { App.toast(err.message, 'error'); }
    },

    // =================== PRESCRIPTIONS PAGE (DOCTOR) ===================
    async doctorPrescriptionsPage(user) {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="card"><div class="card-header"><h2><i class="fa-solid fa-prescription"></i> Reçete Yaz</h2></div><div class="card-body" id="presc-form-area"></div></div>';

        try {
            const appts = await Api.getDoctorAppointments(user.id).catch(() => []);
            const completable = appts.filter(a => a.status === 'APPROVED' || a.status === 'COMPLETED');
            const area = document.getElementById('presc-form-area');

            if (completable.length === 0) {
                area.innerHTML = '<div class="empty-state"><i class="fa-solid fa-prescription"></i><h3>Onaylanmış randevu yok</h3><p>Reçete yazmak için önce bir randevuyu onaylamalısınız.</p></div>';
                return;
            }

            area.innerHTML = `<form id="presc-form">
                <div class="form-group"><label>Randevu</label><div class="form-input-wrapper"><i class="fa-solid fa-calendar"></i>
                    <select id="presc-appt" class="form-select">${completable.map(a => `<option value="${a.id}">${a.patientName} — ${new Date(a.appointmentDate).toLocaleDateString('tr-TR')}</option>`).join('')}</select>
                </div></div>
                <div class="form-group"><label>İlaçlar</label><div class="form-input-wrapper"><i class="fa-solid fa-pills"></i><input type="text" id="presc-med" class="form-input" placeholder="Ör: Parol 500mg, Augmentin 1g" required></div></div>
                <div class="form-group"><label>Kullanım Talimatı</label><div class="form-input-wrapper"><i class="fa-solid fa-file-medical"></i><input type="text" id="presc-inst" class="form-input" placeholder="Ör: Günde 3x1, yemekten sonra" required></div></div>
                <button type="submit" class="btn btn-success"><i class="fa-solid fa-check"></i> Reçete Oluştur</button>
            </form>`;

            document.getElementById('presc-form').addEventListener('submit', async (e) => {
                e.preventDefault();
                try {
                    await Api.createPrescription({
                        appointmentId: parseInt(document.getElementById('presc-appt').value),
                        medicines: document.getElementById('presc-med').value,
                        instructions: document.getElementById('presc-inst').value
                    });
                    App.toast('Reçete başarıyla oluşturuldu!', 'success');
                    document.getElementById('presc-med').value = '';
                    document.getElementById('presc-inst').value = '';
                } catch (err) { App.toast(err.message, 'error'); }
            });
        } catch (err) { App.toast(err.message, 'error'); }
    },

    // =================== HELPERS ===================
    renderAppointmentTable(appts, targetId, viewType) {
        const el = document.getElementById(targetId);
        if (!appts.length) { el.innerHTML = '<div class="empty-state"><i class="fa-solid fa-calendar-xmark"></i><h3>Randevu bulunamadı</h3></div>'; return; }

        const rows = appts.map(a => {
            const date = new Date(a.appointmentDate);
            const dateStr = date.toLocaleDateString('tr-TR') + ' ' + date.toLocaleTimeString('tr-TR', {hour:'2-digit',minute:'2-digit'});
            let actions = '';
            let patientCol = a.doctorName; // default for patient view

            if (viewType === 'doctor') {
                patientCol = `<a href="javascript:void(0)" class="patient-link" data-id="${a.patientId}" data-name="${a.patientName}" style="color:var(--primary);text-decoration:underline;">${a.patientName}</a>`;
                if (a.status === 'PENDING') actions = `<button class="btn btn-success btn-sm btn-appt-approve" data-id="${a.id}"><i class="fa-solid fa-check"></i></button><button class="btn btn-danger btn-sm btn-appt-cancel" data-id="${a.id}"><i class="fa-solid fa-xmark"></i></button>`;
                if (a.status === 'APPROVED') actions = `<button class="btn btn-info btn-sm btn-appt-complete" data-id="${a.id}"><i class="fa-solid fa-check-double"></i> Tamamla</button>`;
            } else {
                if (a.status === 'PENDING') actions = `<button class="btn btn-danger btn-sm btn-appt-cancel" data-id="${a.id}"><i class="fa-solid fa-xmark"></i> İptal</button>`;
            }
            return `<tr><td>${a.id}</td><td>${patientCol}</td><td>${dateStr}</td><td><span class="badge badge-${a.status.toLowerCase()}">${this.statusLabel(a.status)}</span></td><td class="actions">${actions}</td></tr>`;
        }).join('');

        el.innerHTML = `<table class="data-table"><thead><tr><th>#</th><th>${viewType === 'doctor' ? 'Hasta' : 'Doktor'}</th><th>Tarih</th><th>Durum</th><th>İşlem</th></tr></thead><tbody>${rows}</tbody></table>`;

        el.querySelectorAll('.btn-appt-approve').forEach(b => b.addEventListener('click', async () => { try { await Api.approveAppointment(b.dataset.id); App.toast('Randevu onaylandı!', 'success'); App.navigate(App.currentPage); } catch(e) { App.toast(e.message, 'error'); }}));
        el.querySelectorAll('.btn-appt-cancel').forEach(b => b.addEventListener('click', async () => { try { await Api.cancelAppointment(b.dataset.id); App.toast('Randevu iptal edildi', 'info'); App.navigate(App.currentPage); } catch(e) { App.toast(e.message, 'error'); }}));
        el.querySelectorAll('.btn-appt-complete').forEach(b => b.addEventListener('click', async () => { try { await Api.completeAppointment(b.dataset.id); App.toast('Randevu tamamlandı!', 'success'); App.navigate(App.currentPage); } catch(e) { App.toast(e.message, 'error'); }}));
        el.querySelectorAll('.patient-link').forEach(b => b.addEventListener('click', async () => { this.showPatientMedicalRecordModal(b.dataset.id, b.dataset.name); }));
    },

    renderPrescriptionList(prescs, targetId) {
        const el = document.getElementById(targetId);
        if (!prescs.length) { el.innerHTML = '<div class="empty-state"><i class="fa-solid fa-prescription-bottle"></i><h3>Henüz reçete yok</h3></div>'; return; }
        el.innerHTML = prescs.map(p => `<div style="background:var(--bg-dark);border-radius:var(--radius-sm);padding:1rem;margin-bottom:0.75rem;border-left:3px solid var(--success)">
            <div style="display:flex;justify-content:space-between;margin-bottom:0.5rem"><strong>${p.doctorName}</strong><span style="color:var(--text-muted);font-size:0.8rem">${p.createdAt ? new Date(p.createdAt).toLocaleDateString('tr-TR') : ''}</span></div>
            <p style="font-size:0.9rem"><i class="fa-solid fa-pills" style="color:var(--primary-light);margin-right:0.5rem"></i>${p.medicines}</p>
            <p style="font-size:0.85rem;color:var(--text-secondary);margin-top:0.25rem"><i class="fa-solid fa-info-circle" style="margin-right:0.5rem"></i>${p.instructions}</p>
        </div>`).join('');
    },

    renderUsersTable(users, targetId) {
        const el = document.getElementById(targetId);
        if (!users.length) { el.innerHTML = '<div class="empty-state"><i class="fa-solid fa-users"></i><h3>Kullanıcı bulunamadı</h3></div>'; return; }
        const rows = users.map(u => `<tr><td>${u.id}</td><td>${u.fullName}</td><td>${u.email}</td><td>${u.phone || '—'}</td><td><span class="badge badge-${u.role.toLowerCase()}">${this.roleLabel(u.role)}</span></td><td><button class="btn btn-danger btn-sm btn-icon btn-del-user" data-id="${u.id}" title="Sil"><i class="fa-solid fa-trash"></i></button></td></tr>`).join('');
        el.innerHTML = `<table class="data-table"><thead><tr><th>#</th><th>Ad</th><th>E-posta</th><th>Telefon</th><th>Rol</th><th>İşlem</th></tr></thead><tbody>${rows}</tbody></table>`;
        el.querySelectorAll('.btn-del-user').forEach(b => b.addEventListener('click', async () => { if (!confirm('Bu kullanıcıyı silmek istediğinize emin misiniz?')) return; try { await Api.deleteUser(b.dataset.id); App.toast('Kullanıcı silindi', 'info'); App.navigate(App.currentPage); } catch(e) { App.toast(e.message, 'error'); }}));
    },

    // =================== MODALS ===================
    async showNewAppointmentModal() {
        try {
            const doctors = await Api.getDoctors();
            if (!doctors.length) { App.toast('Sistemde kayıtlı doktor bulunamadı', 'error'); return; }
            const body = `<form id="appt-modal-form">
                <div class="form-group"><label>Doktor</label><div class="form-input-wrapper"><i class="fa-solid fa-user-doctor"></i>
                    <select id="appt-doctor" class="form-select">${doctors.map(d => `<option value="${d.id}">Dr. ${d.fullName}</option>`).join('')}</select>
                </div></div>
                <div class="form-group"><label>Tarih ve Saat</label><div class="form-input-wrapper"><i class="fa-solid fa-calendar"></i>
                    <input type="datetime-local" id="appt-date" class="form-input" required>
                </div></div>
                <div class="form-group"><label>Notlar (Opsiyonel)</label><div class="form-input-wrapper"><i class="fa-solid fa-pen"></i>
                    <input type="text" id="appt-notes" class="form-input" placeholder="Randevu nedeni...">
                </div></div>
            </form>`;
            App.showModal('Yeni Randevu', body, `<button class="btn btn-outline" id="modal-cancel-btn">İptal</button><button class="btn btn-primary" id="modal-save-btn"><i class="fa-solid fa-check"></i> Oluştur</button>`);
            document.getElementById('modal-cancel-btn').addEventListener('click', () => App.closeModal());
            document.getElementById('modal-save-btn').addEventListener('click', async () => {
                const doctorId = parseInt(document.getElementById('appt-doctor').value);
                const dateVal = document.getElementById('appt-date').value;
                const notes = document.getElementById('appt-notes').value;
                if (!dateVal) { App.toast('Tarih seçin', 'error'); return; }
                try {
                    await Api.createAppointment({ doctorId, appointmentDate: dateVal, notes });
                    App.toast('Randevu oluşturuldu!', 'success');
                    App.closeModal();
                    App.navigate(App.currentPage);
                } catch (err) { App.toast(err.message, 'error'); }
            });
        } catch (err) { App.toast(err.message, 'error'); }
    },

    showMedicalRecordModal(rec) {
        const body = `<form id="med-modal-form">
            <div class="form-row">
                <div class="form-group"><label>Kan Grubu</label><div class="form-input-wrapper"><i class="fa-solid fa-droplet"></i><input type="text" id="med-blood" class="form-input" placeholder="Ör: A+" value="${rec?.bloodGroup || ''}"></div></div>
                <div class="form-group"><label>Boy (cm)</label><div class="form-input-wrapper"><i class="fa-solid fa-ruler-vertical"></i><input type="number" id="med-height" class="form-input" placeholder="175" value="${rec?.height || ''}"></div></div>
            </div>
            <div class="form-row">
                <div class="form-group"><label>Kilo (kg)</label><div class="form-input-wrapper"><i class="fa-solid fa-weight-scale"></i><input type="number" id="med-weight" class="form-input" placeholder="70" value="${rec?.weight || ''}"></div></div>
                <div class="form-group"><label>Alerjiler</label><div class="form-input-wrapper"><i class="fa-solid fa-allergies"></i><input type="text" id="med-allergy" class="form-input" placeholder="Ör: Penisilin" value="${rec?.allergies || ''}"></div></div>
            </div>
            <div class="form-group"><label>Geçmiş Hastalıklar</label><div class="form-input-wrapper"><i class="fa-solid fa-file-medical"></i><input type="text" id="med-diseases" class="form-input" placeholder="Ör: Diyabet, Astım" value="${rec?.pastDiseases || ''}"></div></div>
        </form>`;
        App.showModal('Tıbbi Kaydı Düzenle', body, `<button class="btn btn-outline" id="modal-cancel-btn">İptal</button><button class="btn btn-success" id="modal-save-btn"><i class="fa-solid fa-save"></i> Kaydet</button>`);
        document.getElementById('modal-cancel-btn').addEventListener('click', () => App.closeModal());
        document.getElementById('modal-save-btn').addEventListener('click', async () => {
            try {
                await Api.updateMyMedicalRecord({
                    bloodGroup: document.getElementById('med-blood').value,
                    height: parseFloat(document.getElementById('med-height').value) || null,
                    weight: parseFloat(document.getElementById('med-weight').value) || null,
                    allergies: document.getElementById('med-allergy').value,
                    pastDiseases: document.getElementById('med-diseases').value
                });
                App.toast('Tıbbi kayıt güncellendi!', 'success');
                App.closeModal();
                App.navigate(App.currentPage);
            } catch (err) { App.toast(err.message, 'error'); }
        });
    },

    // =================== LABEL HELPERS ===================
    statusLabel(s) { return {PENDING:'Bekliyor',APPROVED:'Onaylandı',COMPLETED:'Tamamlandı',CANCELLED:'İptal'}[s] || s; },
    roleLabel(r) { return {PATIENT:'Hasta',DOCTOR:'Doktor',ADMIN:'Yönetici'}[r] || r; },

    // =================== NEW MODALS ===================
    async showProfileEditModal(user) {
        const body = `<form id="profile-modal-form">
            <div class="form-group"><label>Ad Soyad</label><div class="form-input-wrapper"><i class="fa-solid fa-user"></i><input type="text" id="prof-name" class="form-input" value="${user.fullName}" required></div></div>
            <div class="form-group"><label>Telefon</label><div class="form-input-wrapper"><i class="fa-solid fa-phone"></i><input type="tel" id="prof-phone" class="form-input" value="${user.phone || ''}"></div></div>
        </form>`;
        App.showModal('Profili Düzenle', body, `<button class="btn btn-outline" id="modal-cancel-btn">İptal</button><button class="btn btn-success" id="modal-save-btn"><i class="fa-solid fa-save"></i> Kaydet</button>`);
        document.getElementById('modal-cancel-btn').addEventListener('click', () => App.closeModal());
        document.getElementById('modal-save-btn').addEventListener('click', async () => {
            const fullName = document.getElementById('prof-name').value;
            const phone = document.getElementById('prof-phone').value;
            if (!fullName) return App.toast('Ad Soyad gerekli', 'error');
            try {
                // Keep same email and role to not break anything
                await Api.updateUser(user.id, { fullName, phone, email: user.email, role: user.role });
                App.toast('Profil güncellendi', 'success');
                App.closeModal();
                // Update global user
                App.user.fullName = fullName;
                App.user.phone = phone;
                localStorage.setItem('ht_user', JSON.stringify(App.user));
                App.setupSidebar(); // refresh name
                App.navigate(App.currentPage);
            } catch (err) { App.toast(err.message, 'error'); }
        });
    },

    async showPatientMedicalRecordModal(patientId, patientName) {
        try {
            const rec = await Api.getPatientMedicalRecord(patientId).catch(() => null);
            let body = '';
            if (rec) {
                body = `<div class="info-grid">
                    <div class="info-item"><label>Kan Grubu</label><span>${rec.bloodGroup || '—'}</span></div>
                    <div class="info-item"><label>Alerjiler</label><span>${rec.allergies || '—'}</span></div>
                    <div class="info-item"><label>Geçmiş Hastalıklar</label><span>${rec.pastDiseases || '—'}</span></div>
                    <div class="info-item"><label>Boy (cm)</label><span>${rec.height || '—'}</span></div>
                    <div class="info-item"><label>Kilo (kg)</label><span>${rec.weight || '—'}</span></div>
                    <div class="info-item"><label>Son Güncelleme</label><span>${rec.updatedAt ? new Date(rec.updatedAt).toLocaleDateString('tr-TR') : '—'}</span></div>
                </div>`;
            } else {
                body = '<div class="empty-state"><i class="fa-solid fa-notes-medical"></i><p>Bu hasta henüz tıbbi kayıt eklememiş.</p></div>';
            }
            App.showModal(`${patientName} - Tıbbi Kayıt`, body, `<button class="btn btn-primary" id="modal-cancel-btn">Kapat</button>`);
            document.getElementById('modal-cancel-btn').addEventListener('click', () => App.closeModal());
        } catch (err) { App.toast(err.message, 'error'); }
    }
};
