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
                Api.getMyAppointments().catch(() => []),
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
            const appts = await Api.getMyDoctorAppointments().catch(() => []);
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

    // =================== PROFILE PAGE (Faz 1 — Genişletilmiş) ===================
    async profilePage(user) {
        const content = document.getElementById('page-content');
        const genderLabel = {MALE:'Erkek',FEMALE:'Kadın',OTHER:'Diğer'}[user.gender] || '—';
        const statusLabel = {ACTIVE:'Aktif',FROZEN:'Dondurulmuş',DELETED:'Silinmiş',PENDING_VERIFY:'Doğrulama Bekliyor'}[user.accountStatus] || '—';
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
                        <div class="info-item"><label>TC Kimlik</label><span>${user.tcKimlik || '—'}</span></div>
                        <div class="info-item"><label>Telefon</label><span>${user.phone || '—'}</span></div>
                        <div class="info-item"><label>Doğum Tarihi</label><span>${user.birthDate || '—'}</span></div>
                        <div class="info-item"><label>Cinsiyet</label><span>${genderLabel}</span></div>
                        <div class="info-item"><label>Kan Grubu</label><span>${user.bloodGroup || '—'}</span></div>
                        <div class="info-item"><label>Hesap Durumu</label><span>${statusLabel}</span></div>
                        <div class="info-item"><label>E-posta Doğrulanmış</label><span>${user.emailVerified ? '✅ Evet' : '❌ Hayır'}</span></div>
                        <div class="info-item"><label>Kronik Hastalıklar</label><span>${user.chronicDiseases || '—'}</span></div>
                    </div>
                </div>
            </div>`;
        document.getElementById('btn-edit-profile').addEventListener('click', () => this.showProfileEditModal(user));
    },

    // =================== ADRES SAYFASI ===================
    async addressPage() {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="card"><div class="card-header"><h2><i class="fa-solid fa-location-dot"></i> Adresim</h2></div><div class="card-body" id="addr-body"><p style="color:var(--text-secondary)">Yükleniyor...</p></div></div>';
        try {
            const addr = await Api.getAddress().catch(() => null);
            const body = document.getElementById('addr-body');
            body.innerHTML = `<form id="addr-form">
                <div class="form-row"><div class="form-group"><label>İl</label><div class="form-input-wrapper"><i class="fa-solid fa-city"></i><input type="text" id="addr-city" class="form-input" placeholder="İstanbul" value="${addr?.city||''}"></div></div>
                <div class="form-group"><label>İlçe</label><div class="form-input-wrapper"><i class="fa-solid fa-map-pin"></i><input type="text" id="addr-district" class="form-input" placeholder="Kadıköy" value="${addr?.district||''}"></div></div></div>
                <div class="form-row"><div class="form-group"><label>Mahalle</label><div class="form-input-wrapper"><i class="fa-solid fa-street-view"></i><input type="text" id="addr-neighborhood" class="form-input" placeholder="Caferağa Mah." value="${addr?.neighborhood||''}"></div></div>
                <div class="form-group"><label>Posta Kodu</label><div class="form-input-wrapper"><i class="fa-solid fa-hashtag"></i><input type="text" id="addr-postal" class="form-input" placeholder="34710" value="${addr?.postalCode||''}"></div></div></div>
                <div class="form-group"><label>Tam Adres</label><div class="form-input-wrapper"><i class="fa-solid fa-map"></i><input type="text" id="addr-full" class="form-input" placeholder="Açık adres" value="${addr?.fullAddress||''}"></div></div>
                <button type="submit" class="btn btn-success"><i class="fa-solid fa-save"></i> Kaydet</button>
            </form>`;
            document.getElementById('addr-form').addEventListener('submit', async(e)=>{e.preventDefault();try{await Api.saveAddress({city:document.getElementById('addr-city').value,district:document.getElementById('addr-district').value,neighborhood:document.getElementById('addr-neighborhood').value,postalCode:document.getElementById('addr-postal').value,fullAddress:document.getElementById('addr-full').value});App.toast('Adres kaydedildi!','success');}catch(err){App.toast(err.message,'error');}});
        } catch(err) { App.toast(err.message,'error'); }
    },

    // =================== ACİL DURUM KİŞİLERİ ===================
    async emergencyContactsPage() {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="card"><div class="card-header" style="justify-content:space-between"><h2><i class="fa-solid fa-phone-volume"></i> Acil Durum Kişilerim</h2><button class="btn btn-primary btn-sm" id="btn-add-ec"><i class="fa-solid fa-plus"></i> Ekle</button></div><div class="card-body" id="ec-body"><p style="color:var(--text-secondary)">Yükleniyor...</p></div></div>';
        try {
            const contacts = await Api.getEmergencyContacts().catch(() => []);
            const body = document.getElementById('ec-body');
            if (!contacts.length) { body.innerHTML = '<div class="empty-state"><i class="fa-solid fa-phone-volume"></i><h3>Henüz acil durum kişisi eklenmemiş</h3><p>Acil bir durumda aranacak kişileri ekleyin.</p></div>'; }
            else { body.innerHTML = contacts.map(c=>`<div style="background:var(--bg-dark);border-radius:var(--radius-sm);padding:1rem;margin-bottom:0.75rem;display:flex;justify-content:space-between;align-items:center;border-left:3px solid var(--danger)"><div><strong>${c.fullName}</strong><br><span style="color:var(--text-secondary);font-size:0.85rem">${c.phone} · ${c.relationship}</span></div><button class="btn btn-danger btn-sm btn-icon btn-del-ec" data-id="${c.id}" title="Sil"><i class="fa-solid fa-trash"></i></button></div>`).join(''); }
            body.querySelectorAll('.btn-del-ec').forEach(b=>b.addEventListener('click',async()=>{if(!confirm('Bu kişiyi silmek istediğinize emin misiniz?'))return;try{await Api.deleteEmergencyContact(b.dataset.id);App.toast('Kişi silindi','info');App.navigate('emergency');}catch(e){App.toast(e.message,'error');}}));
            document.getElementById('btn-add-ec').addEventListener('click', ()=>{
                App.showModal('Acil Durum Kişisi Ekle',`<form id="ec-form"><div class="form-group"><label>Ad Soyad</label><div class="form-input-wrapper"><i class="fa-solid fa-user"></i><input type="text" id="ec-name" class="form-input" placeholder="Ad Soyad" required></div></div><div class="form-row"><div class="form-group"><label>Telefon</label><div class="form-input-wrapper"><i class="fa-solid fa-phone"></i><input type="tel" id="ec-phone" class="form-input" placeholder="05XX XXX XX XX" required></div></div><div class="form-group"><label>Yakınlık</label><div class="form-input-wrapper"><i class="fa-solid fa-heart"></i><input type="text" id="ec-rel" class="form-input" placeholder="Anne, Baba, Eş..." required></div></div></div></form>`,
                `<button class="btn btn-outline" id="modal-cancel-btn">İptal</button><button class="btn btn-success" id="modal-save-btn"><i class="fa-solid fa-save"></i> Kaydet</button>`);
                document.getElementById('modal-cancel-btn').addEventListener('click',()=>App.closeModal());
                document.getElementById('modal-save-btn').addEventListener('click',async()=>{try{await Api.addEmergencyContact({fullName:document.getElementById('ec-name').value,phone:document.getElementById('ec-phone').value,relationship:document.getElementById('ec-rel').value});App.toast('Kişi eklendi!','success');App.closeModal();App.navigate('emergency');}catch(e){App.toast(e.message,'error');}});
            });
        } catch(err) { App.toast(err.message,'error'); }
    },

    // =================== SİGORTA SAYFASI ===================
    async insurancePage() {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="card"><div class="card-header"><h2><i class="fa-solid fa-shield-halved"></i> Sigorta Bilgilerim</h2></div><div class="card-body" id="ins-body"><p style="color:var(--text-secondary)">Yükleniyor...</p></div></div>';
        try {
            const ins = await Api.getInsurance().catch(() => null);
            document.getElementById('ins-body').innerHTML = `<form id="ins-form">
                <div class="form-row"><div class="form-group"><label>Sigorta Türü</label><div class="form-input-wrapper"><i class="fa-solid fa-shield"></i><select id="ins-type" class="form-select"><option value="">Seçiniz</option><option value="SGK" ${ins?.insuranceType==='SGK'?'selected':''}>SGK</option><option value="OZEL" ${ins?.insuranceType==='OZEL'?'selected':''}>Özel Sigorta</option></select></div></div>
                <div class="form-group"><label>Şirket</label><div class="form-input-wrapper"><i class="fa-solid fa-building"></i><input type="text" id="ins-company" class="form-input" placeholder="Sigorta Şirketi" value="${ins?.insuranceCompany||''}"></div></div></div>
                <div class="form-row"><div class="form-group"><label>Poliçe No</label><div class="form-input-wrapper"><i class="fa-solid fa-barcode"></i><input type="text" id="ins-policy" class="form-input" placeholder="Poliçe Numarası" value="${ins?.policyNumber||''}"></div></div>
                <div class="form-group"><label>SGK No</label><div class="form-input-wrapper"><i class="fa-solid fa-id-card"></i><input type="text" id="ins-sgk" class="form-input" placeholder="SGK Numarası" value="${ins?.sgkNo||''}"></div></div></div>
                <div class="form-group"><label>Notlar</label><div class="form-input-wrapper"><i class="fa-solid fa-sticky-note"></i><input type="text" id="ins-notes" class="form-input" placeholder="Ek bilgiler" value="${ins?.notes||''}"></div></div>
                <button type="submit" class="btn btn-success"><i class="fa-solid fa-save"></i> Kaydet</button>
            </form>`;
            document.getElementById('ins-form').addEventListener('submit',async(e)=>{e.preventDefault();try{await Api.saveInsurance({insuranceType:document.getElementById('ins-type').value,insuranceCompany:document.getElementById('ins-company').value,policyNumber:document.getElementById('ins-policy').value,sgkNo:document.getElementById('ins-sgk').value,notes:document.getElementById('ins-notes').value});App.toast('Sigorta bilgileri kaydedildi!','success');}catch(err){App.toast(err.message,'error');}});
        } catch(err) { App.toast(err.message,'error'); }
    },

    // =================== AYARLAR SAYFASI ===================
    async settingsPage(user) {
        const content = document.getElementById('page-content');
        content.innerHTML = `
            <div class="card"><div class="card-header"><h2><i class="fa-solid fa-key"></i> Şifre Değiştir</h2></div><div class="card-body">
                <form id="pwd-form">
                    <div class="form-group"><label>Mevcut Şifre</label><div class="form-input-wrapper"><i class="fa-solid fa-lock"></i><input type="password" id="set-cur-pwd" class="form-input" placeholder="Mevcut şifreniz" required></div></div>
                    <div class="form-group"><label>Yeni Şifre (min. 8 karakter)</label><div class="form-input-wrapper"><i class="fa-solid fa-lock"></i><input type="password" id="set-new-pwd" class="form-input" placeholder="Yeni şifre" required></div></div>
                    <button type="submit" class="btn btn-primary"><i class="fa-solid fa-check"></i> Şifreyi Değiştir</button>
                </form>
            </div></div>
            <div class="card" style="border-color:var(--danger)"><div class="card-header"><h2><i class="fa-solid fa-triangle-exclamation"></i> Tehlikeli İşlemler</h2></div><div class="card-body" style="display:flex;gap:1rem;flex-wrap:wrap;">
                <button class="btn btn-warning" id="btn-freeze"><i class="fa-solid fa-snowflake"></i> Hesabı Dondur</button>
                <button class="btn btn-danger" id="btn-delete-acc"><i class="fa-solid fa-trash"></i> Hesabı Sil</button>
            </div></div>`;
        document.getElementById('pwd-form').addEventListener('submit',async(e)=>{e.preventDefault();const cur=document.getElementById('set-cur-pwd').value;const nw=document.getElementById('set-new-pwd').value;if(nw.length<8){App.toast('Yeni şifre en az 8 karakter olmalı','error');return;}try{await Api.changePassword(cur,nw);App.toast('Şifre değiştirildi!','success');document.getElementById('set-cur-pwd').value='';document.getElementById('set-new-pwd').value='';}catch(err){App.toast(err.message,'error');}});
        document.getElementById('btn-freeze').addEventListener('click',async()=>{if(!confirm('Hesabınızı dondurmak istediğinize emin misiniz?'))return;try{await Api.freezeAccount();App.toast('Hesap donduruldu','info');Api.clearToken();App.showAuth();}catch(e){App.toast(e.message,'error');}});
        document.getElementById('btn-delete-acc').addEventListener('click',async()=>{if(!confirm('DİKKAT: Hesabınız kalıcı olarak silinecek. Emin misiniz?'))return;try{await Api.deleteAccount();App.toast('Hesap silindi','info');Api.clearToken();App.showAuth();}catch(e){App.toast(e.message,'error');}});
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
                    <div class="info-item"><label>Kronik Hastalıklar</label><span>${rec.chronicDiseases || '—'}</span></div>
                    <div class="info-item"><label>Ameliyat Geçmişi</label><span>${rec.surgeryHistory || '—'}</span></div>
                    <div class="info-item"><label>Aile Hastalık Geçmişi</label><span>${rec.familyHistory || '—'}</span></div>
                    <div class="info-item"><label>Kullandığı İlaçlar</label><span>${rec.currentMedications || '—'}</span></div>
                    <div class="info-item"><label>Engellilik</label><span>${rec.disabilities || '—'}</span></div>
                    <div class="info-item"><label>Sigara</label><span>${rec.smoker === true ? '✅ Evet' : rec.smoker === false ? '❌ Hayır' : '—'}</span></div>
                    <div class="info-item"><label>Alkol</label><span>${rec.alcoholUse === true ? '✅ Evet' : rec.alcoholUse === false ? '❌ Hayır' : '—'}</span></div>
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
            const appts = await Api.getMyDoctorAppointments().catch(() => []);
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
                <div class="form-group"><label>Tanı / Teşhis</label><div class="form-input-wrapper"><i class="fa-solid fa-stethoscope"></i>
                    <input type="text" id="presc-diag" class="form-input" placeholder="Ör: Üst solunum yolu enfeksiyonu">
                </div></div>
                <div class="form-group"><label>İlaçlar *</label><div class="form-input-wrapper"><i class="fa-solid fa-pills"></i>
                    <input type="text" id="presc-med" class="form-input" placeholder="Ör: Parol 500mg, Augmentin 1g" required>
                </div></div>
                <div class="form-group"><label>Kullanım Talimatı *</label><div class="form-input-wrapper"><i class="fa-solid fa-file-medical"></i>
                    <input type="text" id="presc-inst" class="form-input" placeholder="Ör: Günde 3x1, yemekten sonra" required>
                </div></div>
                <div class="form-row">
                    <div class="form-group"><label>Dozaj</label><div class="form-input-wrapper"><i class="fa-solid fa-eyedropper"></i>
                        <input type="text" id="presc-dosage" class="form-input" placeholder="Ör: 500mg, 1g">
                    </div></div>
                    <div class="form-group"><label>Kullanım Sıklığı</label><div class="form-input-wrapper"><i class="fa-solid fa-clock"></i>
                        <input type="text" id="presc-freq" class="form-input" placeholder="Ör: 3x1, 2x1">
                    </div></div>
                    <div class="form-group"><label>Süre (gün)</label><div class="form-input-wrapper"><i class="fa-solid fa-hourglass-half"></i>
                        <input type="number" id="presc-duration" class="form-input" placeholder="Ör: 7">
                    </div></div>
                </div>
                <div class="form-group"><label>Uyarılar / Yan Etkiler</label><div class="form-input-wrapper"><i class="fa-solid fa-triangle-exclamation"></i>
                    <input type="text" id="presc-warn" class="form-input" placeholder="Ör: Araç kullanmayın, alkol almayın">
                </div></div>
                <button type="submit" class="btn btn-success"><i class="fa-solid fa-check"></i> Reçete Oluştur</button>
            </form>`;

            document.getElementById('presc-form').addEventListener('submit', async (e) => {
                e.preventDefault();
                try {
                    await Api.createPrescription({
                        appointmentId: parseInt(document.getElementById('presc-appt').value),
                        medicines: document.getElementById('presc-med').value,
                        instructions: document.getElementById('presc-inst').value,
                        diagnosis: document.getElementById('presc-diag').value || null,
                        dosages: document.getElementById('presc-dosage').value || null,
                        frequencies: document.getElementById('presc-freq').value || null,
                        durationDays: parseInt(document.getElementById('presc-duration').value) || null,
                        warnings: document.getElementById('presc-warn').value || null
                    });
                    App.toast('Reçete başarıyla oluşturuldu!', 'success');
                    document.getElementById('presc-med').value = '';
                    document.getElementById('presc-inst').value = '';
                    document.getElementById('presc-diag').value = '';
                    document.getElementById('presc-dosage').value = '';
                    document.getElementById('presc-freq').value = '';
                    document.getElementById('presc-duration').value = '';
                    document.getElementById('presc-warn').value = '';
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
        const statusLabel = {ACTIVE:'Aktif',USED:'Kullanıldı',EXPIRED:'Süresi Doldu',CANCELLED:'İptal'};
        const statusColor = {ACTIVE:'var(--success)',USED:'var(--info)',EXPIRED:'var(--warning)',CANCELLED:'var(--danger)'};
        el.innerHTML = prescs.map(p => {
            const sc = statusColor[p.status] || 'var(--success)';
            return `<div style="background:var(--bg-dark);border-radius:var(--radius-sm);padding:1rem;margin-bottom:0.75rem;border-left:3px solid ${sc}">
            <div style="display:flex;justify-content:space-between;margin-bottom:0.5rem">
                <div style="display:flex;align-items:center;gap:0.5rem">
                    <strong>${p.doctorName}</strong>
                    <span style="background:${sc}22;color:${sc};padding:0.1rem 0.4rem;border-radius:999px;font-size:0.65rem;font-weight:600">${statusLabel[p.status] || 'Aktif'}</span>
                </div>
                <span style="color:var(--text-muted);font-size:0.8rem">${p.createdAt ? new Date(p.createdAt).toLocaleDateString('tr-TR') : ''}</span>
            </div>
            ${p.diagnosis ? `<p style="font-size:0.85rem;color:var(--accent-light);margin-bottom:0.25rem"><i class="fa-solid fa-stethoscope"></i> Tanı: ${p.diagnosis}</p>` : ''}
            <p style="font-size:0.9rem"><i class="fa-solid fa-pills" style="color:var(--primary-light);margin-right:0.5rem"></i>${p.medicines}</p>
            <p style="font-size:0.85rem;color:var(--text-secondary);margin-top:0.25rem"><i class="fa-solid fa-info-circle" style="margin-right:0.5rem"></i>${p.instructions}</p>
            ${p.dosages ? `<p style="font-size:0.8rem;color:var(--text-muted);margin-top:0.25rem">Dozaj: ${p.dosages}</p>` : ''}
            ${p.frequencies ? `<p style="font-size:0.8rem;color:var(--text-muted)">Sıklık: ${p.frequencies}</p>` : ''}
            ${p.durationDays ? `<p style="font-size:0.8rem;color:var(--text-muted)">Süre: ${p.durationDays} gün</p>` : ''}
            ${p.warnings ? `<p style="font-size:0.8rem;color:var(--danger);margin-top:0.25rem"><i class="fa-solid fa-triangle-exclamation"></i> ${p.warnings}</p>` : ''}
        </div>`;
        }).join('');
    },

    renderUsersTable(users, targetId) {
        const el = document.getElementById(targetId);
        if (!users.length) { el.innerHTML = '<div class="empty-state"><i class="fa-solid fa-users"></i><h3>Kullanıcı bulunamadı</h3></div>'; return; }
        const rows = users.map(u => `<tr><td>${u.id}</td><td>${u.fullName}</td><td>${u.email}</td><td>${u.phone || '—'}</td><td><span class="badge badge-${u.role.toLowerCase()}">${this.roleLabel(u.role)}</span></td><td><button class="btn btn-danger btn-sm btn-icon btn-del-user" data-id="${u.id}" title="Sil"><i class="fa-solid fa-trash"></i></button></td></tr>`).join('');
        el.innerHTML = `<table class="data-table"><thead><tr><th>#</th><th>Ad</th><th>E-posta</th><th>Telefon</th><th>Rol</th><th>İşlem</th></tr></thead><tbody>${rows}</tbody></table>`;
        el.querySelectorAll('.btn-del-user').forEach(b => b.addEventListener('click', async () => { if (!confirm('Bu kullanıcıyı silmek istediğinize emin misiniz?')) return; try { await Api.deleteUser(b.dataset.id); App.toast('Kullanıcı silindi', 'info'); App.navigate(App.currentPage); } catch(e) { App.toast(e.message, 'error'); }}));
    },

    // =================== YENİ RANDEVU MODAL — Profesyonel Saat Seçim Sistemi ===================
    async showNewAppointmentModal() {
        try {
            // Doktor listesini ve profil bilgilerini paralel çek
            const [doctors, profiles] = await Promise.all([
                Api.getDoctors(),
                Api.searchDoctors2(null).catch(() => [])
            ]);
            if (!doctors.length) { App.toast('Sistemde kayıtlı doktor bulunamadı', 'error'); return; }

            // Profil bilgilerini userId ile eşle
            const profileMap = {};
            profiles.forEach(p => { profileMap[p.userId] = p; });

            // Doktor listesine uzmanlık bilgisi ekle
            const enrichedDoctors = doctors.map(d => ({
                ...d,
                specialization: profileMap[d.id]?.specialization || null,
                title: profileMap[d.id]?.title || null,
                hospital: profileMap[d.id]?.hospital || null
            }));

            const today = new Date();
            const todayStr = today.toISOString().split('T')[0];

            // Son seçilen doktoru hatırla
            const lastDoctorId = localStorage.getItem('ht_last_doctor') || enrichedDoctors[0].id;

            // Mevcut uzmanlıkları bul (filtre için)
            const availableSpecs = [...new Set(enrichedDoctors.map(d => d.specialization).filter(Boolean))].sort();

            const buildDoctorOptions = (docs, selectedId) => {
                return docs.map(d => {
                    const label = `${d.title || 'Dr.'} ${d.fullName}${d.specialization ? ' — ' + d.specialization : ''}${d.hospital ? ' (' + d.hospital + ')' : ''}`;
                    return `<option value="${d.id}" ${d.id == selectedId ? 'selected' : ''}>${label}</option>`;
                }).join('');
            };

            const body = `<form id="appt-modal-form" class="appt-wizard">
                <!-- ADIM 1: Uzmanlık ve Doktor Seçimi -->
                <div class="form-group">
                    <label><i class="fa-solid fa-stethoscope" style="color:var(--accent)"></i> Uzmanlık Alanı Filtresi</label>
                    <div class="form-input-wrapper"><i class="fa-solid fa-filter"></i>
                        <select id="appt-spec-filter" class="form-select">
                            <option value="">Tüm Uzmanlıklar</option>
                            ${availableSpecs.map(s => `<option value="${s}">${s}</option>`).join('')}
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label><i class="fa-solid fa-user-doctor" style="color:var(--primary-light)"></i> Doktor Seçin</label>
                    <div class="form-input-wrapper"><i class="fa-solid fa-user-doctor"></i>
                        <select id="appt-doctor" class="form-select">
                            ${buildDoctorOptions(enrichedDoctors, lastDoctorId)}
                        </select>
                    </div>
                    <div id="appt-doctor-info" style="margin-top:0.5rem"></div>
                    <button type="button" class="btn btn-accent btn-sm" id="btn-nearest-slot" style="margin-top:0.5rem;width:100%">
                        <i class="fa-solid fa-bolt"></i> En Yakın Müsait Randevuyu Öner
                    </button>
                    <div id="nearest-slot-result" style="display:none;margin-top:0.5rem"></div>
                </div>

                <!-- ADIM 2: Haftalık Takvim -->
                <div class="form-group">
                    <label><i class="fa-solid fa-calendar-week" style="color:var(--success)"></i> Hafta Seçin</label>
                    <div style="display:flex;align-items:center;gap:0.5rem;margin-bottom:0.75rem">
                        <button type="button" class="btn btn-outline btn-sm btn-icon" id="week-prev" title="Önceki Hafta"><i class="fa-solid fa-chevron-left"></i></button>
                        <span id="week-label" style="flex:1;text-align:center;font-weight:600;color:var(--text-primary)"></span>
                        <button type="button" class="btn btn-outline btn-sm btn-icon" id="week-next" title="Sonraki Hafta"><i class="fa-solid fa-chevron-right"></i></button>
                    </div>
                    <div class="week-calendar" id="week-calendar">
                        <!-- 7 günlük takvim kartları -->
                    </div>
                </div>

                <!-- ADIM 3: Saat Seçimi -->
                <div class="form-group" id="slot-area" style="display:none">
                    <label><i class="fa-solid fa-clock" style="color:var(--accent)"></i> Müsait Saatler — <span id="selected-date-label" style="color:var(--primary-light)"></span></label>
                    <!-- Filtreler -->
                    <div class="slot-filters" id="slot-filters" style="display:flex;gap:0.5rem;margin-bottom:0.75rem;flex-wrap:wrap">
                        <button type="button" class="slot-filter-btn active" data-filter="all"><i class="fa-solid fa-list"></i> Tümü</button>
                        <button type="button" class="slot-filter-btn" data-filter="morning"><i class="fa-solid fa-sun"></i> Sabah</button>
                        <button type="button" class="slot-filter-btn" data-filter="afternoon"><i class="fa-solid fa-cloud-sun"></i> Öğle</button>
                        <button type="button" class="slot-filter-btn" data-filter="evening"><i class="fa-solid fa-moon"></i> Akşam</button>
                    </div>
                    <div class="slot-grid" id="slot-grid"></div>
                </div>

                <input type="hidden" id="appt-selected-slot" value="">
                <input type="hidden" id="appt-selected-date" value="">

                <!-- ADIM 4: Notlar -->
                <div class="form-group"><label>Randevu Notu (Opsiyonel)</label>
                    <div class="form-input-wrapper"><i class="fa-solid fa-pen"></i>
                        <input type="text" id="appt-notes" class="form-input" placeholder="Şikayetinizi kısaca yazın...">
                    </div>
                </div>
            </form>`;

            App.showModal('Yeni Randevu Al', body,
                `<button class="btn btn-outline" id="modal-cancel-btn">İptal</button>
                 <button class="btn btn-primary" id="modal-save-btn" disabled>
                     <i class="fa-solid fa-calendar-check"></i> Randevu Oluştur
                 </button>`);

            // ====== State ======
            let currentWeekStart = this._getWeekStart(today);
            let selectedDate = null;
            let allSlots = [];

            // ====== Uzmanlık filtresi → doktor listesini güncelle ======
            const specFilter = document.getElementById('appt-spec-filter');
            const doctorSelect = document.getElementById('appt-doctor');

            const updateDoctorInfo = () => {
                const selectedId = doctorSelect.value;
                const doc = enrichedDoctors.find(d => d.id == selectedId);
                const infoEl = document.getElementById('appt-doctor-info');
                if (doc && (doc.specialization || doc.hospital)) {
                    infoEl.innerHTML = `<div style="background:var(--bg-dark);border-radius:var(--radius-sm);padding:0.6rem 0.85rem;border-left:3px solid var(--accent);font-size:0.85rem">
                        ${doc.specialization ? `<span style="color:var(--accent-light)"><i class="fa-solid fa-stethoscope" style="margin-right:0.3rem"></i>${doc.specialization}</span>` : ''}
                        ${doc.hospital ? `<span style="color:var(--text-secondary);margin-left:0.75rem"><i class="fa-solid fa-hospital" style="margin-right:0.3rem"></i>${doc.hospital}</span>` : ''}
                    </div>`;
                } else {
                    infoEl.innerHTML = '';
                }
            };

            specFilter.addEventListener('change', () => {
                const spec = specFilter.value;
                const filtered = spec ? enrichedDoctors.filter(d => d.specialization === spec) : enrichedDoctors;
                doctorSelect.innerHTML = buildDoctorOptions(filtered, filtered[0]?.id);
                updateDoctorInfo();
                loadWeek();
            });

            doctorSelect.addEventListener('change', () => {
                updateDoctorInfo();
                loadWeek();
            });

            updateDoctorInfo();

            // ====== Hafta Takvimi Yükle ======
            const loadWeek = async () => {
                const doctorId = document.getElementById('appt-doctor').value;
                const weekCal = document.getElementById('week-calendar');
                const weekLabel = document.getElementById('week-label');

                const endDate = new Date(currentWeekStart);
                endDate.setDate(endDate.getDate() + 6);
                weekLabel.textContent = this._formatDateShort(currentWeekStart) + ' — ' + this._formatDateShort(endDate);

                weekCal.innerHTML = '<div style="text-align:center;padding:1rem;color:var(--text-muted)"><i class="fa-solid fa-spinner fa-spin"></i></div>';

                try {
                    const startStr = currentWeekStart.toISOString().split('T')[0];
                    const days = await Api.getWeekAvailability(doctorId, startStr);
                    weekCal.innerHTML = days.map(d => {
                        const dateObj = new Date(d.date + 'T00:00:00');
                        const dayNum = dateObj.getDate();
                        const dayName = d.dayLabel.substring(0, 3);
                        const isPast = new Date(d.date) < new Date(todayStr);
                        const isSelected = selectedDate === d.date;

                        let statusClass = 'day-closed';
                        let statusIcon = 'fa-xmark';
                        let statusText = 'Kapalı';

                        if (!isPast && d.available) {
                            if (d.status === 'AVAILABLE') { statusClass = 'day-available'; statusIcon = 'fa-check'; statusText = d.availableSlots + ' boş'; }
                            else if (d.status === 'ALMOST_FULL') { statusClass = 'day-almost-full'; statusIcon = 'fa-exclamation'; statusText = d.availableSlots + ' kaldı'; }
                            else if (d.status === 'FULL') { statusClass = 'day-full'; statusIcon = 'fa-ban'; statusText = 'Dolu'; }
                        } else if (!isPast && d.status === 'DAY_OFF') {
                            statusClass = 'day-dayoff'; statusIcon = 'fa-umbrella-beach'; statusText = 'İzinli';
                        } else if (isPast) {
                            statusClass = 'day-past'; statusText = ''; statusIcon = '';
                        }

                        const clickable = !isPast && d.available && d.status !== 'FULL' && d.status !== 'DAY_OFF';
                        return `<div class="day-card ${statusClass} ${isSelected ? 'day-selected' : ''} ${clickable ? 'day-clickable' : ''}"
                                     data-date="${d.date}" ${clickable ? '' : 'data-disabled="true"'}>
                            <span class="day-name">${dayName}</span>
                            <span class="day-num">${dayNum}</span>
                            ${statusIcon ? `<span class="day-status"><i class="fa-solid ${statusIcon}"></i> ${statusText}</span>` : '<span class="day-status">&nbsp;</span>'}
                        </div>`;
                    }).join('');

                    // Gün tıklama
                    weekCal.querySelectorAll('.day-clickable').forEach(card => {
                        card.addEventListener('click', () => {
                            weekCal.querySelectorAll('.day-card').forEach(c => c.classList.remove('day-selected'));
                            card.classList.add('day-selected');
                            selectedDate = card.dataset.date;
                            document.getElementById('appt-selected-date').value = selectedDate;
                            loadSlots(selectedDate);
                        });
                    });
                } catch(e) {
                    weekCal.innerHTML = `<p style="color:var(--warning);font-size:0.85rem;padding:0.5rem"><i class="fa-solid fa-info-circle"></i> Doktor takvimi henüz oluşturulmamış. Manuel tarih girin:</p>
                        <div class="form-input-wrapper"><i class="fa-solid fa-calendar"></i>
                        <input type="date" id="appt-manual-date" class="form-input" min="${todayStr}"></div>`;
                    const manualInput = document.getElementById('appt-manual-date');
                    if (manualInput) {
                        manualInput.addEventListener('change', () => {
                            selectedDate = manualInput.value;
                            document.getElementById('appt-selected-date').value = selectedDate;
                            loadSlots(selectedDate);
                        });
                    }
                }
            };

            // ====== Slot Yükleme ======
            const loadSlots = async (date) => {
                const doctorId = document.getElementById('appt-doctor').value;
                const slotArea = document.getElementById('slot-area');
                const slotGrid = document.getElementById('slot-grid');
                const dateLabel = document.getElementById('selected-date-label');

                const dateObj = new Date(date + 'T00:00:00');
                dateLabel.textContent = dateObj.toLocaleDateString('tr-TR', { day: 'numeric', month: 'long', year: 'numeric', weekday: 'long' });

                slotArea.style.display = 'block';
                slotGrid.innerHTML = '<div style="text-align:center;padding:1rem;color:var(--text-muted)"><i class="fa-solid fa-spinner fa-spin"></i> Saatler yükleniyor...</div>';

                // Saat seçimini sıfırla
                document.getElementById('appt-selected-slot').value = '';
                document.getElementById('modal-save-btn').disabled = true;

                try {
                    const slots = await Api.getAvailableSlots(doctorId, date);
                    allSlots = slots || [];

                    if (!allSlots.length) {
                        slotGrid.innerHTML = `<div style="text-align:center;padding:1.5rem;color:var(--warning)">
                            <i class="fa-solid fa-calendar-xmark" style="font-size:2rem;margin-bottom:0.5rem;display:block;opacity:0.5"></i>
                            <p style="font-weight:600">Bu tarihte müsait randevu bulunmamaktadır.</p>
                            <p style="font-size:0.8rem;color:var(--text-muted);margin-top:0.25rem">Lütfen farklı bir gün seçin veya "En Yakın Randevu" butonunu deneyin.</p>
                        </div>`;
                        return;
                    }
                    renderSlotButtons(allSlots, 'all');
                } catch(e) {
                    slotGrid.innerHTML = `<p style="color:var(--danger);padding:0.5rem"><i class="fa-solid fa-circle-xmark"></i> ${e.message}</p>`;
                }
            };

            // ====== Slot Butonları Render ======
            const renderSlotButtons = (slots, filter) => {
                const slotGrid = document.getElementById('slot-grid');
                let filtered = slots;

                if (filter === 'morning') filtered = slots.filter(s => { const h = parseInt(s.dateTime.substring(11,13)); return h < 12; });
                else if (filter === 'afternoon') filtered = slots.filter(s => { const h = parseInt(s.dateTime.substring(11,13)); return h >= 12 && h < 17; });
                else if (filter === 'evening') filtered = slots.filter(s => { const h = parseInt(s.dateTime.substring(11,13)); return h >= 17; });

                if (!filtered.length) {
                    slotGrid.innerHTML = `<p style="color:var(--text-muted);text-align:center;padding:1rem">
                        <i class="fa-solid fa-filter"></i> Bu zaman diliminde müsait saat yok.</p>`;
                    return;
                }

                slotGrid.innerHTML = filtered.map(s => {
                    const time = s.dateTime.substring(11, 16);
                    const hour = parseInt(time.substring(0, 2));
                    let period = 'morning';
                    let periodIcon = 'fa-sun';
                    if (hour >= 12 && hour < 17) { period = 'afternoon'; periodIcon = 'fa-cloud-sun'; }
                    else if (hour >= 17) { period = 'evening'; periodIcon = 'fa-moon'; }

                    return `<button type="button" class="slot-btn slot-${period}" data-time="${s.dateTime.substring(11, 19)}" data-full="${s.dateTime}">
                        <i class="fa-solid ${periodIcon} slot-period-icon"></i>
                        <span class="slot-time">${time}</span>
                        <span class="slot-duration">${s.durationMinutes} dk</span>
                    </button>`;
                }).join('');

                // Slot tıklama
                slotGrid.querySelectorAll('.slot-btn').forEach(btn => {
                    btn.addEventListener('click', async () => {
                        slotGrid.querySelectorAll('.slot-btn').forEach(b => b.classList.remove('selected'));
                        btn.classList.add('selected');
                        document.getElementById('appt-selected-slot').value = btn.dataset.time;
                        document.getElementById('modal-save-btn').disabled = false;
                    });
                });
            };

            // ====== Filtre butonları ======
            document.getElementById('slot-filters').querySelectorAll('.slot-filter-btn').forEach(btn => {
                btn.addEventListener('click', () => {
                    document.querySelectorAll('.slot-filter-btn').forEach(b => b.classList.remove('active'));
                    btn.classList.add('active');
                    if (allSlots.length) renderSlotButtons(allSlots, btn.dataset.filter);
                });
            });

            // ====== En Yakın Randevu ======
            document.getElementById('btn-nearest-slot').addEventListener('click', async () => {
                const doctorId = document.getElementById('appt-doctor').value;
                const resultDiv = document.getElementById('nearest-slot-result');
                resultDiv.style.display = 'block';
                resultDiv.innerHTML = '<p style="color:var(--text-muted);font-size:0.85rem"><i class="fa-solid fa-spinner fa-spin"></i> Aranıyor...</p>';
                try {
                    const nearest = await Api.getNearestSlot(doctorId);
                    if (nearest.found) {
                        const dateObj = new Date(nearest.date + 'T00:00:00');
                        const dateStr = dateObj.toLocaleDateString('tr-TR', { day: 'numeric', month: 'long', weekday: 'long' });
                        resultDiv.innerHTML = `<div style="background:var(--success)11;border:1px solid var(--success)44;border-radius:var(--radius-sm);padding:0.75rem">
                            <p style="font-size:0.9rem;font-weight:600;color:var(--success)"><i class="fa-solid fa-sparkles"></i> En Yakın Randevu Bulundu!</p>
                            <p style="font-size:0.85rem;margin-top:0.25rem;color:var(--text-primary)">📅 ${dateStr} — 🕐 ${nearest.time.substring(0,5)}</p>
                            <p style="font-size:0.75rem;color:var(--text-muted);margin-top:0.25rem">${nearest.totalSlotsOnDay} adet müsait saat mevcut</p>
                            <button type="button" class="btn btn-success btn-sm" id="btn-use-nearest" style="margin-top:0.5rem;width:100%">
                                <i class="fa-solid fa-check"></i> Bu Tarihi Seç
                            </button>
                        </div>`;
                        document.getElementById('btn-use-nearest').addEventListener('click', () => {
                            // Haftayı bu tarihe götür
                            currentWeekStart = this._getWeekStart(dateObj);
                            selectedDate = nearest.date;
                            document.getElementById('appt-selected-date').value = selectedDate;
                            loadWeek().then(() => loadSlots(selectedDate));
                            resultDiv.style.display = 'none';
                        });
                    } else {
                        resultDiv.innerHTML = `<p style="color:var(--warning);font-size:0.85rem;padding:0.5rem;background:var(--warning)11;border-radius:var(--radius-sm)">
                            <i class="fa-solid fa-info-circle"></i> ${nearest.message}</p>`;
                    }
                } catch(e) {
                    resultDiv.innerHTML = `<p style="color:var(--danger);font-size:0.85rem">${e.message}</p>`;
                }
            });

            // ====== Hafta navigasyon ======
            document.getElementById('week-prev').addEventListener('click', () => {
                const newStart = new Date(currentWeekStart);
                newStart.setDate(newStart.getDate() - 7);
                if (newStart >= this._getWeekStart(today)) {
                    currentWeekStart = newStart;
                    selectedDate = null;
                    document.getElementById('slot-area').style.display = 'none';
                    loadWeek();
                }
            });
            document.getElementById('week-next').addEventListener('click', () => {
                const newStart = new Date(currentWeekStart);
                newStart.setDate(newStart.getDate() + 7);
                // Max 4 hafta ileri
                const maxDate = new Date(today);
                maxDate.setDate(maxDate.getDate() + 28);
                if (newStart <= maxDate) {
                    currentWeekStart = newStart;
                    selectedDate = null;
                    document.getElementById('slot-area').style.display = 'none';
                    loadWeek();
                }
            });

            // ====== Doktor değişince ======
            document.getElementById('appt-doctor').addEventListener('change', () => {
                localStorage.setItem('ht_last_doctor', document.getElementById('appt-doctor').value);
                selectedDate = null;
                document.getElementById('slot-area').style.display = 'none';
                document.getElementById('nearest-slot-result').style.display = 'none';
                document.getElementById('appt-selected-slot').value = '';
                document.getElementById('modal-save-btn').disabled = true;
                loadWeek();
            });

            // ====== Modal butonları ======
            document.getElementById('modal-cancel-btn').addEventListener('click', () => App.closeModal());
            document.getElementById('modal-save-btn').addEventListener('click', async () => {
                const doctorId = parseInt(document.getElementById('appt-doctor').value);
                const dateVal = document.getElementById('appt-selected-date').value;
                const slotVal = document.getElementById('appt-selected-slot').value;
                const notes = document.getElementById('appt-notes').value;

                if (!dateVal) { App.toast('Lütfen bir tarih seçin', 'error'); return; }
                if (!slotVal) { App.toast('Lütfen bir saat seçin', 'error'); return; }

                const appointmentDate = dateVal + 'T' + slotVal;
                const saveBtn = document.getElementById('modal-save-btn');
                saveBtn.disabled = true;
                saveBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Kontrol ediliyor...';

                try {
                    await Api.createAppointment({ doctorId, appointmentDate, notes });
                    App.toast('Randevu başarıyla oluşturuldu! 🎉', 'success');
                    localStorage.setItem('ht_last_doctor', doctorId);
                    App.closeModal();
                    App.navigate(App.currentPage);
                } catch (err) {
                    saveBtn.disabled = false;
                    saveBtn.innerHTML = '<i class="fa-solid fa-calendar-check"></i> Randevu Oluştur';

                    if (err.message.includes('başka bir randevusu var') || err.message.includes('çakışma')) {
                        App.toast('Bu saat az önce alındı! Lütfen başka bir saat seçin.', 'error');
                        // Slotları yenile
                        if (selectedDate) loadSlots(selectedDate);
                    } else {
                        App.toast(err.message, 'error');
                    }
                }
            });

            // ====== İlk yükleme ======
            loadWeek();
        } catch (err) { App.toast(err.message, 'error'); }
    },

    // Hafta başlangıcını hesapla (Pazartesi)
    _getWeekStart(date) {
        const d = new Date(date);
        const day = d.getDay();
        const diff = day === 0 ? -6 : 1 - day; // Pazartesi'ye git
        d.setDate(d.getDate() + diff);
        d.setHours(0, 0, 0, 0);
        return d;
    },

    _formatDateShort(date) {
        return date.toLocaleDateString('tr-TR', { day: 'numeric', month: 'short' });
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
            <div class="form-row">
                <div class="form-group"><label>Kronik Hastalıklar</label><div class="form-input-wrapper"><i class="fa-solid fa-heart-pulse"></i><input type="text" id="med-chronic" class="form-input" placeholder="Ör: Hipertansiyon" value="${rec?.chronicDiseases || ''}"></div></div>
                <div class="form-group"><label>Ameliyat Geçmişi</label><div class="form-input-wrapper"><i class="fa-solid fa-scissors"></i><input type="text" id="med-surgery" class="form-input" placeholder="Ör: Apandisit (2020)" value="${rec?.surgeryHistory || ''}"></div></div>
            </div>
            <div class="form-row">
                <div class="form-group"><label>Aile Hastalık Geçmişi</label><div class="form-input-wrapper"><i class="fa-solid fa-people-roof"></i><input type="text" id="med-family" class="form-input" placeholder="Ör: Diyabet (anne)" value="${rec?.familyHistory || ''}"></div></div>
                <div class="form-group"><label>Kullandığı İlaçlar</label><div class="form-input-wrapper"><i class="fa-solid fa-pills"></i><input type="text" id="med-meds" class="form-input" placeholder="Ör: Metformin 500mg" value="${rec?.currentMedications || ''}"></div></div>
            </div>
            <div class="form-row">
                <div class="form-group"><label>Engellilik / Özel Durum</label><div class="form-input-wrapper"><i class="fa-solid fa-wheelchair"></i><input type="text" id="med-disability" class="form-input" placeholder="Varsa belirtin" value="${rec?.disabilities || ''}"></div></div>
            </div>
            <div class="form-row">
                <div class="form-group"><label>Sigara Kullanımı</label><div class="form-input-wrapper"><i class="fa-solid fa-smoking"></i><select id="med-smoker" class="form-select">
                    <option value="">Belirtilmemiş</option>
                    <option value="true" ${rec?.smoker === true ? 'selected' : ''}>Evet</option>
                    <option value="false" ${rec?.smoker === false ? 'selected' : ''}>Hayır</option>
                </select></div></div>
                <div class="form-group"><label>Alkol Kullanımı</label><div class="form-input-wrapper"><i class="fa-solid fa-wine-glass"></i><select id="med-alcohol" class="form-select">
                    <option value="">Belirtilmemiş</option>
                    <option value="true" ${rec?.alcoholUse === true ? 'selected' : ''}>Evet</option>
                    <option value="false" ${rec?.alcoholUse === false ? 'selected' : ''}>Hayır</option>
                </select></div></div>
            </div>
        </form>`;
        App.showModal('Tıbbi Kaydı Düzenle', body, `<button class="btn btn-outline" id="modal-cancel-btn">İptal</button><button class="btn btn-success" id="modal-save-btn"><i class="fa-solid fa-save"></i> Kaydet</button>`);
        document.getElementById('modal-cancel-btn').addEventListener('click', () => App.closeModal());
        document.getElementById('modal-save-btn').addEventListener('click', async () => {
            try {
                const smokerVal = document.getElementById('med-smoker').value;
                const alcoholVal = document.getElementById('med-alcohol').value;
                await Api.updateMyMedicalRecord({
                    bloodGroup: document.getElementById('med-blood').value,
                    height: parseFloat(document.getElementById('med-height').value) || null,
                    weight: parseFloat(document.getElementById('med-weight').value) || null,
                    allergies: document.getElementById('med-allergy').value,
                    pastDiseases: document.getElementById('med-diseases').value,
                    chronicDiseases: document.getElementById('med-chronic').value || null,
                    surgeryHistory: document.getElementById('med-surgery').value || null,
                    familyHistory: document.getElementById('med-family').value || null,
                    currentMedications: document.getElementById('med-meds').value || null,
                    disabilities: document.getElementById('med-disability').value || null,
                    smoker: smokerVal === '' ? null : smokerVal === 'true',
                    alcoholUse: alcoholVal === '' ? null : alcoholVal === 'true'
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
            <div class="form-row">
                <div class="form-group"><label>Ad Soyad</label><div class="form-input-wrapper"><i class="fa-solid fa-user"></i><input type="text" id="prof-name" class="form-input" value="${user.fullName}" required></div></div>
                <div class="form-group"><label>TC Kimlik</label><div class="form-input-wrapper"><i class="fa-solid fa-id-card"></i><input type="text" id="prof-tc" class="form-input" value="${user.tcKimlik||''}" maxlength="11"></div></div>
            </div>
            <div class="form-row">
                <div class="form-group"><label>Telefon</label><div class="form-input-wrapper"><i class="fa-solid fa-phone"></i><input type="tel" id="prof-phone" class="form-input" value="${user.phone || ''}"></div></div>
                <div class="form-group"><label>Doğum Tarihi</label><div class="form-input-wrapper"><i class="fa-solid fa-cake-candles"></i><input type="date" id="prof-birth" class="form-input" value="${user.birthDate||''}"></div></div>
            </div>
            <div class="form-row">
                <div class="form-group"><label>Cinsiyet</label><div class="form-input-wrapper"><i class="fa-solid fa-venus-mars"></i><select id="prof-gender" class="form-select"><option value="">Seçiniz</option><option value="MALE" ${user.gender==='MALE'?'selected':''}>Erkek</option><option value="FEMALE" ${user.gender==='FEMALE'?'selected':''}>Kadın</option><option value="OTHER" ${user.gender==='OTHER'?'selected':''}>Diğer</option></select></div></div>
                <div class="form-group"><label>Kan Grubu</label><div class="form-input-wrapper"><i class="fa-solid fa-droplet"></i><input type="text" id="prof-blood" class="form-input" placeholder="A+, B-, O+ vb." value="${user.bloodGroup||''}"></div></div>
            </div>
            <div class="form-group"><label>Kronik Hastalıklar</label><div class="form-input-wrapper"><i class="fa-solid fa-heart-pulse"></i><input type="text" id="prof-chronic" class="form-input" placeholder="Diyabet, Astım..." value="${user.chronicDiseases||''}"></div></div>
            <div class="form-group"><label>Engellilik / Özel Durum</label><div class="form-input-wrapper"><i class="fa-solid fa-wheelchair"></i><input type="text" id="prof-disability" class="form-input" placeholder="Varsa belirtin" value="${user.disabilities||''}"></div></div>
        </form>`;
        App.showModal('Profili Düzenle', body, `<button class="btn btn-outline" id="modal-cancel-btn">İptal</button><button class="btn btn-success" id="modal-save-btn"><i class="fa-solid fa-save"></i> Kaydet</button>`);
        document.getElementById('modal-cancel-btn').addEventListener('click', () => App.closeModal());
        document.getElementById('modal-save-btn').addEventListener('click', async () => {
            const fullName = document.getElementById('prof-name').value;
            if (!fullName) return App.toast('Ad Soyad gerekli', 'error');
            try {
                const updated = await Api.updateProfile({
                    fullName,
                    phone: document.getElementById('prof-phone').value,
                    tcKimlik: document.getElementById('prof-tc').value || null,
                    birthDate: document.getElementById('prof-birth').value || null,
                    gender: document.getElementById('prof-gender').value || null,
                    bloodGroup: document.getElementById('prof-blood').value || null,
                    chronicDiseases: document.getElementById('prof-chronic').value || null,
                    disabilities: document.getElementById('prof-disability').value || null
                });
                App.toast('Profil güncellendi', 'success');
                App.closeModal();
                App.user = updated;
                localStorage.setItem('ht_user', JSON.stringify(updated));
                App.setupSidebar();
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
                    <div class="info-item"><label>Kronik Hastalıklar</label><span>${rec.chronicDiseases || '—'}</span></div>
                    <div class="info-item"><label>Ameliyat Geçmişi</label><span>${rec.surgeryHistory || '—'}</span></div>
                    <div class="info-item"><label>Aile Hastalık Geçmişi</label><span>${rec.familyHistory || '—'}</span></div>
                    <div class="info-item"><label>Kullandığı İlaçlar</label><span>${rec.currentMedications || '—'}</span></div>
                    <div class="info-item"><label>Engellilik</label><span>${rec.disabilities || '—'}</span></div>
                    <div class="info-item"><label>Sigara</label><span>${rec.smoker === true ? '✅ Evet' : rec.smoker === false ? '❌ Hayır' : '—'}</span></div>
                    <div class="info-item"><label>Alkol</label><span>${rec.alcoholUse === true ? '✅ Evet' : rec.alcoholUse === false ? '❌ Hayır' : '—'}</span></div>
                    <div class="info-item"><label>Son Güncelleme</label><span>${rec.updatedAt ? new Date(rec.updatedAt).toLocaleDateString('tr-TR') : '—'}</span></div>
                </div>`;

                // Lab sonuçlarını da göster
                const labs = await Api.getPatientLabResults(patientId).catch(() => []);
                if (labs.length) {
                    const statusColor = {NORMAL:'var(--success)',ABNORMAL:'var(--warning)',CRITICAL:'var(--danger)',PENDING:'var(--info)'};
                    body += `<div style="margin-top:1rem"><label style="font-size:0.75rem;color:var(--text-muted);text-transform:uppercase">Lab Sonuçları (${labs.length})</label>` +
                        labs.slice(0, 5).map(l => {
                            const sc = statusColor[l.status] || 'var(--text-muted)';
                            return `<div style="padding:0.5rem;background:var(--bg-dark);border-radius:var(--radius-sm);margin-top:0.5rem;font-size:0.85rem;border-left:3px solid ${sc}">
                                <strong>${l.testName}</strong> ${l.resultValue ? '— ' + l.resultValue + ' ' + (l.unit || '') : ''} <span style="color:${sc};font-size:0.75rem">${l.status || ''}</span>
                            </div>`;
                        }).join('') + '</div>';
                }
            } else {
                body = '<div class="empty-state"><i class="fa-solid fa-notes-medical"></i><p>Bu hasta henüz tıbbi kayıt eklememiş.</p></div>';
            }
            App.showModal(`${patientName} - Tıbbi Kayıt`, body, `<button class="btn btn-primary" id="modal-cancel-btn">Kapat</button>`);
            document.getElementById('modal-cancel-btn').addEventListener('click', () => App.closeModal());
        } catch (err) { App.toast(err.message, 'error'); }
    },

    // =================== FAZ 2: DOKTOR ARAMA SAYFASI ===================
    _specializations: [
        'Acil Tıp', 'Aile Hekimliği', 'Beyin ve Sinir Cerrahisi', 'Çocuk Sağlığı ve Hastalıkları',
        'Cildiye (Dermatoloji)', 'Dahiliye (İç Hastalıkları)', 'Diş Hekimliği', 'Endokrinoloji',
        'Enfeksiyon Hastalıkları', 'Fizik Tedavi ve Rehabilitasyon', 'Gastroenteroloji',
        'Genel Cerrahi', 'Göğüs Hastalıkları', 'Göz Hastalıkları', 'Kadın Hastalıkları ve Doğum',
        'Kalp ve Damar Cerrahisi', 'Kardiyoloji', 'Kulak Burun Boğaz (KBB)', 'Nefroloji',
        'Nöroloji', 'Onkoloji', 'Ortopedi ve Travmatoloji', 'Patoloji', 'Plastik Cerrahi',
        'Psikiyatri', 'Radyoloji', 'Romatoloji', 'Üroloji', 'Anestezi ve Reanimasyon',
        'Beslenme ve Diyetetik', 'Fizyoterapi', 'Klinik Psikoloji', 'Diğer'
    ],

    _getSpecOptions(selected) {
        return '<option value="">Tümü</option>' + this._specializations.map(s =>
            `<option value="${s}" ${s === selected ? 'selected' : ''}>${s}</option>`
        ).join('');
    },

    async doctorSearchPage() {
        const content = document.getElementById('page-content');
        content.innerHTML = `<div class="card"><div class="card-header" style="justify-content:space-between;flex-wrap:wrap;gap:0.75rem">
            <h2><i class="fa-solid fa-user-doctor"></i> Doktor Ara</h2>
            <div style="display:flex;gap:0.5rem;align-items:center;flex-wrap:wrap">
                <div class="form-input-wrapper" style="margin:0"><i class="fa-solid fa-stethoscope"></i>
                    <select id="doc-spec-filter" class="form-select" style="width:220px;padding-top:0.5rem;padding-bottom:0.5rem">
                        ${this._getSpecOptions(null)}
                    </select>
                </div>
                <div class="form-input-wrapper" style="margin:0"><i class="fa-solid fa-search"></i><input type="text" id="doc-search-input" class="form-input" placeholder="İsim ara..." style="width:160px;padding-top:0.5rem;padding-bottom:0.5rem"></div>
                <button class="btn btn-primary btn-sm" id="doc-search-btn"><i class="fa-solid fa-search"></i></button>
            </div></div><div class="card-body" id="doc-search-results"><p style="color:var(--text-secondary)">Yükleniyor...</p></div></div>`;

        let allDoctors = [];
        const loadDoctors = async (spec) => {
            const el = document.getElementById('doc-search-results');
            try {
                allDoctors = await Api.searchDoctors2(spec || null).catch(()=>[]);
                renderDoctors(allDoctors);
            } catch(e){el.innerHTML='<p style="color:var(--danger)">'+e.message+'</p>';}
        };

        const renderDoctors = (docs) => {
            const el = document.getElementById('doc-search-results');
            const nameFilter = (document.getElementById('doc-search-input')?.value || '').toLowerCase().trim();
            let filtered = docs;
            if (nameFilter) filtered = docs.filter(d => (d.fullName||'').toLowerCase().includes(nameFilter) || (d.specialization||'').toLowerCase().includes(nameFilter));
            if(!filtered.length){el.innerHTML='<div class="empty-state"><i class="fa-solid fa-user-doctor"></i><h3>Doktor bulunamadı</h3></div>';return;}
            el.innerHTML = '<div class="doctor-grid">'+filtered.map(d=>`<div class="doctor-card" data-id="${d.profileId}">
                <div class="doctor-card-top"><div class="profile-avatar" style="width:56px;height:56px;font-size:1.5rem"><i class="fa-solid fa-user-doctor"></i></div>
                <div><h3>${d.title||''} ${d.fullName}</h3><p style="color:var(--accent-light);font-size:0.85rem"><i class="fa-solid fa-stethoscope" style="margin-right:0.25rem"></i>${d.specialization||'Belirtilmemiş'}</p></div></div>
                <div class="doctor-card-info"><span><i class="fa-solid fa-hospital"></i> ${d.hospital||'—'}</span><span><i class="fa-solid fa-clock"></i> ${d.experienceYears||0} yıl deneyim</span>
                <span><i class="fa-solid fa-star" style="color:var(--warning)"></i> ${d.averageRating?.toFixed(1)||'0.0'} (${d.totalReviews} yorum)</span>
                <span><i class="fa-solid fa-language"></i> ${d.languages||'Türkçe'}</span></div>
                <div style="display:flex;gap:0.5rem;margin-top:0.75rem"><button class="btn btn-primary btn-sm btn-doc-detail" data-id="${d.profileId}" style="flex:1"><i class="fa-solid fa-eye"></i> Detay</button></div>
            </div>`).join('')+'</div>';
            el.querySelectorAll('.btn-doc-detail').forEach(b=>b.addEventListener('click',()=>this.showDoctorDetailModal(b.dataset.id)));
        };

        loadDoctors(null);
        document.getElementById('doc-spec-filter').addEventListener('change', (e) => loadDoctors(e.target.value));
        document.getElementById('doc-search-btn').addEventListener('click', () => renderDoctors(allDoctors));
        document.getElementById('doc-search-input').addEventListener('keydown', e => { if(e.key==='Enter') renderDoctors(allDoctors); });
    },

    async showDoctorDetailModal(profileId) {
        try {
            const d = await Api.getDoctorDetail(profileId);
            const reviews = await Api.getDoctorReviews(profileId).catch(()=>[]);
            const stars = (n)=>'★'.repeat(Math.round(n))+'☆'.repeat(5-Math.round(n));
            let body = `<div style="text-align:center;margin-bottom:1rem"><div class="profile-avatar" style="width:64px;height:64px;font-size:1.75rem;margin:0 auto 0.75rem"><i class="fa-solid fa-user-doctor"></i></div>
                <h2>${d.title||''} ${d.fullName}</h2><p style="color:var(--accent-light)">${d.specialization||'—'}</p>
                <p style="color:var(--warning);font-size:1.1rem;margin-top:0.25rem">${stars(d.averageRating)} ${d.averageRating?.toFixed(1)}</p></div>
                <div class="info-grid"><div class="info-item"><label>Hastane</label><span>${d.hospital||'—'}</span></div>
                <div class="info-item"><label>Bölüm</label><span>${d.department||'—'}</span></div>
                <div class="info-item"><label>Deneyim</label><span>${d.experienceYears||0} yıl</span></div>
                <div class="info-item"><label>Diller</label><span>${d.languages||'Türkçe'}</span></div>
                <div class="info-item"><label>Ücret</label><span>${d.consultationFee||'—'}</span></div>
                <div class="info-item"><label>Eğitim</label><span>${d.education||'—'}</span></div></div>`;
            if(d.biography) body += `<div style="margin-top:1rem;padding:1rem;background:var(--bg-dark);border-radius:var(--radius-sm)"><label style="font-size:0.75rem;color:var(--text-muted);text-transform:uppercase">Biyografi</label><p style="margin-top:0.25rem;font-size:0.9rem">${d.biography}</p></div>`;
            if(d.certificates?.length) body += `<div style="margin-top:1rem"><label style="font-size:0.75rem;color:var(--text-muted);text-transform:uppercase">Sertifikalar</label>${d.certificates.map(c=>`<div style="padding:0.5rem;background:var(--bg-dark);border-radius:var(--radius-sm);margin-top:0.5rem;font-size:0.85rem"><strong>${c.name}</strong> — ${c.institution} (${c.year})</div>`).join('')}</div>`;
            if(reviews.length) body += `<div style="margin-top:1rem"><label style="font-size:0.75rem;color:var(--text-muted);text-transform:uppercase">Yorumlar (${reviews.length})</label>${reviews.slice(0,5).map(r=>`<div style="padding:0.75rem;background:var(--bg-dark);border-radius:var(--radius-sm);margin-top:0.5rem"><div style="display:flex;justify-content:space-between"><strong>${r.patientName}</strong><span style="color:var(--warning)">${stars(r.rating)}</span></div><p style="font-size:0.85rem;color:var(--text-secondary);margin-top:0.25rem">${r.comment||''}</p></div>`).join('')}</div>`;
            App.showModal('Doktor Detayı', body, `<button class="btn btn-outline" id="modal-cancel-btn">Kapat</button><button class="btn btn-success" id="modal-review-btn"><i class="fa-solid fa-star"></i> Değerlendir</button>`);
            document.getElementById('modal-cancel-btn').addEventListener('click',()=>App.closeModal());
            document.getElementById('modal-review-btn').addEventListener('click',()=>{App.closeModal();this.showDoctorReviewModal(profileId, (d.title||'')+' '+d.fullName);});
        } catch(e) { App.toast(e.message,'error'); }
    },

    // =================== FAZ 2: DOKTOR PROFİL DÜZENLEME ===================
    async doctorProfileEditPage() {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="card"><div class="card-header"><h2><i class="fa-solid fa-user-doctor"></i> Doktor Profilim</h2></div><div class="card-body" id="dp-body"><p style="color:var(--text-secondary)">Yükleniyor...</p></div></div><div class="card"><div class="card-header" style="justify-content:space-between"><h2><i class="fa-solid fa-certificate"></i> Sertifikalarım</h2><button class="btn btn-primary btn-sm" id="btn-add-cert"><i class="fa-solid fa-plus"></i> Ekle</button></div><div class="card-body" id="cert-body"></div></div>';
        try {
            const dp = await Api.getMyDoctorProfile().catch(()=>null);
            const body = document.getElementById('dp-body');
            const statusBadge = dp?.approvalStatus === 'APPROVED' ? '<span class="badge badge-completed">Onaylı</span>' : dp?.approvalStatus === 'REJECTED' ? '<span class="badge badge-cancelled">Reddedildi</span>' : '<span class="badge badge-pending">Onay Bekliyor</span>';
            body.innerHTML = `<div style="margin-bottom:1rem">${dp ? 'Durum: '+statusBadge : '<span style="color:var(--text-muted)">Henüz profil oluşturulmamış</span>'}</div>
            ${dp?.rejectionReason ? `<div style="background:rgba(239,68,68,0.1);border:1px solid var(--danger);border-radius:var(--radius-sm);padding:0.75rem;margin-bottom:1rem;font-size:0.9rem"><i class="fa-solid fa-triangle-exclamation" style="color:var(--danger)"></i> Red nedeni: ${dp.rejectionReason}</div>` : ''}
            <form id="dp-form">
                <div class="form-row"><div class="form-group"><label>Unvan</label><div class="form-input-wrapper"><i class="fa-solid fa-graduation-cap"></i><select id="dp-title" class="form-select"><option value="">Seçiniz</option><option value="Uzm. Dr." ${dp?.title==='Uzm. Dr.'?'selected':''}>Uzm. Dr.</option><option value="Dr." ${dp?.title==='Dr.'?'selected':''}>Dr.</option><option value="Doç. Dr." ${dp?.title==='Doç. Dr.'?'selected':''}>Doç. Dr.</option><option value="Prof. Dr." ${dp?.title==='Prof. Dr.'?'selected':''}>Prof. Dr.</option></select></div></div>
                <div class="form-group"><label>Uzmanlık Alanı *</label><div class="form-input-wrapper"><i class="fa-solid fa-stethoscope"></i>
                    <select id="dp-spec" class="form-select">
                        <option value="">Uzmanlık Seçiniz</option>
                        ${this._specializations.map(s => `<option value="${s}" ${dp?.specialization===s?'selected':''}>${s}</option>`).join('')}
                    </select>
                </div></div></div>
                <div class="form-row"><div class="form-group"><label>Hastane / Klinik</label><div class="form-input-wrapper"><i class="fa-solid fa-hospital"></i><input type="text" id="dp-hospital" class="form-input" value="${dp?.hospital||''}"></div></div>
                <div class="form-group"><label>Bölüm</label><div class="form-input-wrapper"><i class="fa-solid fa-building"></i><input type="text" id="dp-dept" class="form-input" value="${dp?.department||''}"></div></div></div>
                <div class="form-row"><div class="form-group"><label>Deneyim (Yıl)</label><div class="form-input-wrapper"><i class="fa-solid fa-clock"></i><input type="number" id="dp-exp" class="form-input" value="${dp?.experienceYears||''}"></div></div>
                <div class="form-group"><label>Muayene Ücreti</label><div class="form-input-wrapper"><i class="fa-solid fa-turkish-lira-sign"></i><input type="text" id="dp-fee" class="form-input" placeholder="500 TL" value="${dp?.consultationFee||''}"></div></div></div>
                <div class="form-row"><div class="form-group"><label>Diller</label><div class="form-input-wrapper"><i class="fa-solid fa-language"></i><input type="text" id="dp-lang" class="form-input" placeholder="Türkçe, İngilizce" value="${dp?.languages||''}"></div></div>
                <div class="form-group"><label>Eğitim</label><div class="form-input-wrapper"><i class="fa-solid fa-school"></i><input type="text" id="dp-edu" class="form-input" value="${dp?.education||''}"></div></div></div>
                <div class="form-group"><label>Biyografi</label><div class="form-input-wrapper"><i class="fa-solid fa-pen"></i><input type="text" id="dp-bio" class="form-input" placeholder="Kendinizi tanıtın..." value="${dp?.biography||''}"></div></div>
                <button type="submit" class="btn btn-success"><i class="fa-solid fa-save"></i> Kaydet</button>
            </form>`;
            document.getElementById('dp-form').addEventListener('submit',async(e)=>{e.preventDefault();try{await Api.saveDoctorProfile({title:document.getElementById('dp-title').value,specialization:document.getElementById('dp-spec').value,hospital:document.getElementById('dp-hospital').value,department:document.getElementById('dp-dept').value,experienceYears:parseInt(document.getElementById('dp-exp').value)||null,consultationFee:document.getElementById('dp-fee').value,languages:document.getElementById('dp-lang').value,education:document.getElementById('dp-edu').value,biography:document.getElementById('dp-bio').value});App.toast('Profil kaydedildi!','success');App.navigate('doctor-profile');}catch(err){App.toast(err.message,'error');}});
            // Sertifikalar
            const certs = dp?.certificates || [];
            const certBody = document.getElementById('cert-body');
            if(!certs.length) certBody.innerHTML='<div class="empty-state"><i class="fa-solid fa-certificate"></i><h3>Henüz sertifika eklenmemiş</h3></div>';
            else certBody.innerHTML=certs.map(c=>`<div style="background:var(--bg-dark);border-radius:var(--radius-sm);padding:0.75rem;margin-bottom:0.5rem;display:flex;justify-content:space-between;align-items:center"><div><strong>${c.name}</strong><br><span style="color:var(--text-secondary);font-size:0.85rem">${c.institution} · ${c.year}</span></div><button class="btn btn-danger btn-sm btn-icon btn-del-cert" data-id="${c.id}"><i class="fa-solid fa-trash"></i></button></div>`).join('');
            certBody.querySelectorAll('.btn-del-cert').forEach(b=>b.addEventListener('click',async()=>{try{await Api.deleteDoctorCertificate(b.dataset.id);App.toast('Sertifika silindi','info');App.navigate('doctor-profile');}catch(e){App.toast(e.message,'error');}}));
            document.getElementById('btn-add-cert').addEventListener('click',()=>{
                App.showModal('Sertifika Ekle',`<div class="form-group"><label>Sertifika Adı</label><div class="form-input-wrapper"><i class="fa-solid fa-certificate"></i><input type="text" id="cert-name" class="form-input" required></div></div><div class="form-row"><div class="form-group"><label>Kurum</label><div class="form-input-wrapper"><i class="fa-solid fa-building"></i><input type="text" id="cert-inst" class="form-input"></div></div><div class="form-group"><label>Yıl</label><div class="form-input-wrapper"><i class="fa-solid fa-calendar"></i><input type="text" id="cert-year" class="form-input" placeholder="2024"></div></div></div>`,
                `<button class="btn btn-outline" id="modal-cancel-btn">İptal</button><button class="btn btn-success" id="modal-save-btn"><i class="fa-solid fa-save"></i> Ekle</button>`);
                document.getElementById('modal-cancel-btn').addEventListener('click',()=>App.closeModal());
                document.getElementById('modal-save-btn').addEventListener('click',async()=>{try{await Api.addDoctorCertificate({name:document.getElementById('cert-name').value,institution:document.getElementById('cert-inst').value,year:document.getElementById('cert-year').value});App.toast('Sertifika eklendi!','success');App.closeModal();App.navigate('doctor-profile');}catch(e){App.toast(e.message,'error');}});
            });
        } catch(err){App.toast(err.message,'error');}
    },

    // =================== FAZ 2: ADMİN DOKTOR ONAY PANELİ ===================
    async doctorApprovalsPage() {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="card"><div class="card-header"><h2><i class="fa-solid fa-user-check"></i> Bekleyen Doktor Başvuruları</h2></div><div class="card-body" id="da-body"><p style="color:var(--text-secondary)">Yükleniyor...</p></div></div>';
        try {
            const pending = await Api.getPendingDoctors().catch(()=>[]);
            const body = document.getElementById('da-body');
            if(!pending.length){body.innerHTML='<div class="empty-state"><i class="fa-solid fa-check-double"></i><h3>Bekleyen başvuru yok</h3><p>Tüm doktor başvuruları işlenmiş.</p></div>';return;}
            body.innerHTML = pending.map(d=>`<div style="background:var(--bg-dark);border-radius:var(--radius-md);padding:1.25rem;margin-bottom:1rem;border-left:3px solid var(--warning)">
                <div style="display:flex;justify-content:space-between;align-items:flex-start;flex-wrap:wrap;gap:1rem">
                    <div><h3>${d.title||''} ${d.fullName}</h3><p style="color:var(--accent-light);font-size:0.9rem">${d.specialization||'—'} · ${d.hospital||'—'}</p>
                    <p style="color:var(--text-muted);font-size:0.85rem;margin-top:0.25rem">${d.email} · ${d.experienceYears||0} yıl deneyim</p>
                    ${d.education?`<p style="font-size:0.85rem;margin-top:0.25rem"><i class="fa-solid fa-school" style="color:var(--text-muted)"></i> ${d.education}</p>`:''}
                    ${d.biography?`<p style="font-size:0.85rem;color:var(--text-secondary);margin-top:0.5rem">${d.biography.substring(0,150)}${d.biography.length>150?'...':''}</p>`:''}</div>
                    <div style="display:flex;gap:0.5rem"><button class="btn btn-success btn-sm btn-approve" data-id="${d.profileId}"><i class="fa-solid fa-check"></i> Onayla</button><button class="btn btn-danger btn-sm btn-reject" data-id="${d.profileId}"><i class="fa-solid fa-xmark"></i> Reddet</button></div>
                </div></div>`).join('');
            body.querySelectorAll('.btn-approve').forEach(b=>b.addEventListener('click',async()=>{if(!confirm('Bu doktoru onaylamak istediğinize emin misiniz?'))return;try{await Api.approveDoctorProfile(b.dataset.id);App.toast('Doktor onaylandı! ✅','success');App.navigate('doctor-approvals');}catch(e){App.toast(e.message,'error');}}));
            body.querySelectorAll('.btn-reject').forEach(b=>b.addEventListener('click',async()=>{const reason=prompt('Red nedeni:');if(!reason)return;try{await Api.rejectDoctorProfile(b.dataset.id,reason);App.toast('Doktor reddedildi','info');App.navigate('doctor-approvals');}catch(e){App.toast(e.message,'error');}}));
        } catch(err){App.toast(err.message,'error');}
    },

    // =================== FAZ 3: DOKTOR TAKVİM YÖNETİMİ ===================
    async doctorSchedulePage() {
        const content = document.getElementById('page-content');
        const dayNames = {MONDAY:'Pazartesi',TUESDAY:'Salı',WEDNESDAY:'Çarşamba',THURSDAY:'Perşembe',FRIDAY:'Cuma',SATURDAY:'Cumartesi',SUNDAY:'Pazar'};
        const days = ['MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY'];
        const today = new Date().toISOString().split('T')[0];

        content.innerHTML = `
            <div class="card">
                <div class="card-header"><h2><i class="fa-solid fa-calendar-days"></i> Haftalık Çalışma Takvimim</h2></div>
                <div class="card-body" id="sched-body"><p style="color:var(--text-secondary)">Yükleniyor...</p></div>
            </div>
            <div class="card" style="margin-top:1rem">
                <div class="card-header"><h2><i class="fa-solid fa-calendar-xmark" style="color:var(--warning)"></i> İzin / Tatil Günlerim</h2></div>
                <div class="card-body">
                    <p style="color:var(--text-secondary);font-size:0.9rem;margin-bottom:1rem">Belirli günlerde çalışmayacaksanız buradan izin ekleyin. İzin eklediğiniz günlerde hastalar randevu alamaz.</p>
                    <div style="display:flex;gap:0.75rem;align-items:flex-end;flex-wrap:wrap;margin-bottom:1.25rem">
                        <div class="form-group" style="margin:0;flex:1;min-width:150px">
                            <label style="font-size:0.8rem">Tarih</label>
                            <div class="form-input-wrapper"><i class="fa-solid fa-calendar"></i>
                                <input type="date" id="dayoff-date" class="form-input" min="${today}">
                            </div>
                        </div>
                        <div class="form-group" style="margin:0;flex:2;min-width:200px">
                            <label style="font-size:0.8rem">Neden (Opsiyonel)</label>
                            <div class="form-input-wrapper"><i class="fa-solid fa-pen"></i>
                                <input type="text" id="dayoff-reason" class="form-input" placeholder="Yıllık izin, konferans, kişisel...">
                            </div>
                        </div>
                        <button class="btn btn-warning btn-sm" id="btn-add-dayoff" style="height:42px">
                            <i class="fa-solid fa-plus"></i> İzin Ekle
                        </button>
                    </div>
                    <div id="dayoff-list"><p style="color:var(--text-muted);font-size:0.85rem"><i class="fa-solid fa-spinner fa-spin"></i> Yükleniyor...</p></div>
                </div>
            </div>`;

        // ====== Haftalık Şablon ======
        try {
            const schedules = await Api.getDoctorSchedule(App.user.id).catch(()=>[]);
            const schedMap = {};
            schedules.forEach(s => schedMap[s.dayOfWeek] = s);

            const body = document.getElementById('sched-body');
            body.innerHTML = '<p style="color:var(--text-secondary);font-size:0.9rem;margin-bottom:1rem">Her gün için çalışma saatlerinizi belirleyin. Kaydettikten sonra hastalar müsait saatlerinizi görebilecektir.</p>' +
                days.map(day => {
                    const s = schedMap[day];
                    return `<div class="schedule-row" style="background:var(--bg-dark);border-radius:var(--radius-sm);padding:1rem;margin-bottom:0.5rem;display:flex;align-items:center;gap:1rem;flex-wrap:wrap">
                        <label style="width:100px;font-weight:600;color:${s?.available?'var(--success)':'var(--text-muted)'}">${dayNames[day]}</label>
                        <label class="toggle-label" style="display:flex;align-items:center;gap:0.5rem;cursor:pointer"><input type="checkbox" id="sched-avail-${day}" ${s?.available!==false?'checked':''}><span style="font-size:0.85rem">Aktif</span></label>
                        <div style="display:flex;align-items:center;gap:0.5rem;flex:1;min-width:250px">
                            <input type="time" id="sched-start-${day}" class="form-input" style="padding:0.4rem 0.6rem;width:auto" value="${s?.startTime||'09:00'}">
                            <span>—</span>
                            <input type="time" id="sched-end-${day}" class="form-input" style="padding:0.4rem 0.6rem;width:auto" value="${s?.endTime||'17:00'}">
                            <select id="sched-slot-${day}" class="form-select" style="width:auto;padding:0.4rem 0.6rem">
                                <option value="15" ${s?.slotDurationMinutes===15?'selected':''}>15dk</option>
                                <option value="20" ${s?.slotDurationMinutes===20?'selected':''}>20dk</option>
                                <option value="30" ${!s||s.slotDurationMinutes===30?'selected':''}>30dk</option>
                                <option value="45" ${s?.slotDurationMinutes===45?'selected':''}>45dk</option>
                                <option value="60" ${s?.slotDurationMinutes===60?'selected':''}>60dk</option>
                            </select>
                            <button class="btn btn-success btn-sm btn-save-sched" data-day="${day}"><i class="fa-solid fa-save"></i></button>
                        </div>
                    </div>`;
                }).join('');

            body.querySelectorAll('.btn-save-sched').forEach(btn => {
                btn.addEventListener('click', async () => {
                    const day = btn.dataset.day;
                    try {
                        await Api.saveSchedule({
                            dayOfWeek: day,
                            startTime: document.getElementById('sched-start-'+day).value + ':00',
                            endTime: document.getElementById('sched-end-'+day).value + ':00',
                            slotDurationMinutes: parseInt(document.getElementById('sched-slot-'+day).value),
                            available: document.getElementById('sched-avail-'+day).checked
                        });
                        App.toast(dayNames[day]+' kaydedildi!', 'success');
                    } catch(e) { App.toast(e.message, 'error'); }
                });
            });
        } catch(err) { App.toast(err.message, 'error'); }

        // ====== İzin Günleri ======
        const loadDayOffs = async () => {
            const list = document.getElementById('dayoff-list');
            try {
                const dayOffs = await Api.getDayOffs();
                if (!dayOffs.length) {
                    list.innerHTML = '<div style="text-align:center;padding:1.5rem;color:var(--text-muted)"><i class="fa-solid fa-umbrella-beach" style="font-size:1.5rem;opacity:0.3;display:block;margin-bottom:0.5rem"></i><p>Henüz izin günü eklenmemiş.</p></div>';
                    return;
                }
                list.innerHTML = dayOffs.map(d => `
                    <div style="display:flex;align-items:center;gap:1rem;padding:0.75rem;background:var(--bg-dark);border-radius:var(--radius-sm);margin-bottom:0.4rem;border-left:3px solid var(--warning)">
                        <div style="flex:1">
                            <span style="font-weight:600;color:var(--text-primary)">${new Date(d.date+'T00:00:00').toLocaleDateString('tr-TR', {day:'numeric',month:'long',year:'numeric',weekday:'long'})}</span>
                            ${d.reason ? `<span style="color:var(--text-muted);font-size:0.8rem;margin-left:0.5rem">— ${d.reason}</span>` : ''}
                        </div>
                        <button class="btn btn-danger btn-sm btn-del-dayoff" data-date="${d.date}" title="İzni Kaldır"><i class="fa-solid fa-trash"></i></button>
                    </div>
                `).join('');

                list.querySelectorAll('.btn-del-dayoff').forEach(btn => {
                    btn.addEventListener('click', async () => {
                        if (!confirm('Bu izin gününü kaldırmak istediğinize emin misiniz?')) return;
                        try {
                            await Api.removeDayOff(btn.dataset.date);
                            App.toast('İzin kaldırıldı', 'success');
                            loadDayOffs();
                        } catch(e) { App.toast(e.message, 'error'); }
                    });
                });
            } catch(e) { list.innerHTML = `<p style="color:var(--danger)">${e.message}</p>`; }
        };

        document.getElementById('btn-add-dayoff').addEventListener('click', async () => {
            const dateVal = document.getElementById('dayoff-date').value;
            const reason = document.getElementById('dayoff-reason').value;
            if (!dateVal) { App.toast('Lütfen bir tarih seçin', 'error'); return; }
            try {
                const result = await Api.addDayOff({ date: dateVal, reason });
                App.toast(result.message, 'success');
                document.getElementById('dayoff-date').value = '';
                document.getElementById('dayoff-reason').value = '';
                loadDayOffs();
            } catch(e) { App.toast(e.message, 'error'); }
        });

        loadDayOffs();
    },

    // =================== FAZ 4: BİLDİRİMLER SAYFASI ===================
    async notificationsPage() {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="card"><div class="card-header" style="justify-content:space-between"><h2><i class="fa-solid fa-bell"></i> Bildirimler</h2><button class="btn btn-outline btn-sm" id="btn-mark-all"><i class="fa-solid fa-check-double"></i> Tümünü Okundu İşaretle</button></div><div class="card-body" id="notif-body"><p style="color:var(--text-secondary)">Yükleniyor...</p></div></div>';
        try {
            const notifs = await Api.getNotifications().catch(()=>[]);
            const body = document.getElementById('notif-body');

            if (!notifs.length) {
                body.innerHTML = '<div class="empty-state"><i class="fa-solid fa-bell-slash"></i><h3>Bildirim yok</h3><p>Henüz bildiriminiz bulunmamaktadır.</p></div>';
                return;
            }

            const timeAgo = (dateStr) => {
                const diff = Date.now() - new Date(dateStr).getTime();
                const mins = Math.floor(diff / 60000);
                if (mins < 1) return 'Az önce';
                if (mins < 60) return mins + ' dk önce';
                const hours = Math.floor(mins / 60);
                if (hours < 24) return hours + ' saat önce';
                const days = Math.floor(hours / 24);
                if (days < 7) return days + ' gün önce';
                return new Date(dateStr).toLocaleDateString('tr-TR');
            };

            body.innerHTML = notifs.map(n => `<div class="notif-item ${n.read ? '' : 'unread'}" data-id="${n.id}" data-action="${n.actionUrl||''}">
                <div class="notif-icon"><i class="fa-solid ${n.icon || 'fa-bell'}"></i></div>
                <div class="notif-body"><h4>${n.title}</h4><p>${n.message}</p></div>
                <div class="notif-time">${timeAgo(n.createdAt)}</div>
            </div>`).join('');

            body.querySelectorAll('.notif-item').forEach(item => {
                item.addEventListener('click', async () => {
                    const id = item.dataset.id;
                    const action = item.dataset.action;
                    try {
                        await Api.markNotificationRead(id);
                        item.classList.remove('unread');
                        App.updateNotifBadge();
                        if (action && action !== 'null') {
                            // Video görüşme bildirimlerini doğru sayfaya yönlendir
                            if (action.startsWith('/video-call/')) {
                                const roomId = action.replace('/video-call/', '');
                                App.navigate('video-calls');
                                // Kısa gecikme ile otomatik odaya katıl
                                setTimeout(async () => {
                                    try {
                                        await Api.joinVideoCall(roomId);
                                        Pages.openVideoCall(roomId);
                                    } catch(e) { App.toast('Görüşme bulunamadı veya sona ermiş', 'error'); }
                                }, 500);
                            } else {
                                App.navigate(action);
                            }
                        }
                    } catch(e) { /* silent */ }
                });
            });

            document.getElementById('btn-mark-all').addEventListener('click', async () => {
                try {
                    await Api.markAllNotificationsRead();
                    body.querySelectorAll('.notif-item.unread').forEach(i => i.classList.remove('unread'));
                    App.updateNotifBadge();
                    App.toast('Tüm bildirimler okundu işaretlendi', 'info');
                } catch(e) { App.toast(e.message, 'error'); }
            });
        } catch(err) { App.toast(err.message, 'error'); }
    },

    // =================== FAZ 5: MESAJLAŞMA SAYFASI ===================
    async messagesPage() {
        const content = document.getElementById('page-content');
        content.innerHTML = `<div class="chat-layout">
            <div class="chat-sidebar" id="chat-sidebar">
            <div class="chat-sidebar-header"><h3><i class="fa-solid fa-comment-dots"></i> Konuşmalar</h3><button class="btn btn-primary btn-sm" id="btn-new-msg" style="margin-left:auto"><i class="fa-solid fa-plus"></i></button></div>
                <div class="chat-list" id="chat-list"><p style="color:var(--text-secondary);padding:1rem">Yükleniyor...</p></div>
            </div>
            <div class="chat-main" id="chat-main">
                <div class="chat-empty-state"><i class="fa-solid fa-comments" style="font-size:3rem;color:var(--text-muted);margin-bottom:1rem"></i><h3 style="color:var(--text-muted)">Bir konuşma seçin</h3><p style="color:var(--text-secondary)">Sol taraftan bir kişi seçerek mesajlaşmaya başlayın.</p></div>
            </div>
        </div>`;

        try {
            const convs = await Api.getConversations().catch(()=>[]);
            const chatList = document.getElementById('chat-list');

            if (!convs.length) {
                chatList.innerHTML = '<div style="padding:1.5rem;text-align:center;color:var(--text-muted)"><i class="fa-solid fa-comment-slash" style="font-size:2rem;margin-bottom:0.5rem;display:block"></i><p>Henüz konuşma yok</p></div>';
                return;
            }

            chatList.innerHTML = convs.map(c => `<div class="chat-contact ${c.unreadCount > 0 ? 'unread' : ''}" data-id="${c.partnerId}">
                <div class="chat-contact-avatar"><i class="fa-solid ${c.partnerRole==='DOCTOR'?'fa-user-doctor':'fa-user'}"></i></div>
                <div class="chat-contact-info">
                    <div class="chat-contact-name">${c.partnerName}${c.unreadCount > 0 ? `<span class="chat-unread-badge">${c.unreadCount}</span>` : ''}</div>
                    <div class="chat-contact-last">${c.lastMessage}</div>
                </div>
                <div class="chat-contact-time">${c.lastMessageTime}</div>
            </div>`).join('');

            chatList.querySelectorAll('.chat-contact').forEach(el => {
                el.addEventListener('click', () => {
                    chatList.querySelectorAll('.chat-contact').forEach(c => c.classList.remove('active'));
                    el.classList.add('active');
                    el.classList.remove('unread');
                    const badge = el.querySelector('.chat-unread-badge');
                    if (badge) badge.remove();
                    this.openChat(parseInt(el.dataset.id), el.querySelector('.chat-contact-name').textContent.trim());
                });
            });
        } catch(err) { App.toast(err.message, 'error'); }
        document.getElementById('btn-new-msg').addEventListener('click', () => this.showNewMessageModal());
    },

    async openChat(partnerId, partnerName) {
        const chatMain = document.getElementById('chat-main');
        chatMain.innerHTML = `<div class="chat-header"><h3><i class="fa-solid fa-user-doctor"></i> ${partnerName}</h3></div>
            <div class="chat-messages" id="chat-messages"><p style="color:var(--text-secondary);text-align:center;padding:2rem">Yükleniyor...</p></div>
            <div class="chat-input-bar">
                <input type="text" id="chat-input" class="form-input" placeholder="Mesajınızı yazın..." style="flex:1">
                <button class="btn btn-primary" id="chat-send-btn"><i class="fa-solid fa-paper-plane"></i></button>
            </div>`;

        const loadMessages = async () => {
            try {
                const msgs = await Api.getConversation(partnerId);
                const container = document.getElementById('chat-messages');
                if (!msgs.length) {
                    container.innerHTML = '<p style="color:var(--text-muted);text-align:center;padding:2rem">Henüz mesaj yok. İlk mesajı gönderin!</p>';
                } else {
                    container.innerHTML = msgs.map(m => `<div class="chat-bubble ${m.mine ? 'mine' : 'theirs'}">
                        <div class="chat-bubble-content">${m.content}</div>
                        <div class="chat-bubble-time">${new Date(m.createdAt).toLocaleTimeString('tr-TR', {hour:'2-digit',minute:'2-digit'})}</div>
                    </div>`).join('');
                    container.scrollTop = container.scrollHeight;
                }
                App.updateNotifBadge();
            } catch(e) { App.toast(e.message, 'error'); }
        };

        await loadMessages();

        const sendMsg = async () => {
            const input = document.getElementById('chat-input');
            const text = input.value.trim();
            if (!text) return;
            input.value = '';
            try {
                await Api.sendMessage(partnerId, text);
                await loadMessages();
            } catch(e) { App.toast(e.message, 'error'); }
        };

        document.getElementById('chat-send-btn').addEventListener('click', sendMsg);
        document.getElementById('chat-input').addEventListener('keydown', e => { if (e.key === 'Enter') sendMsg(); });

        // 5sn'de bir yeni mesajları kontrol et
        this._chatInterval = setInterval(loadMessages, 5000);
        // Sayfadan çıkınca temizle
        const observer = new MutationObserver(() => {
            if (!document.getElementById('chat-messages')) {
                clearInterval(this._chatInterval);
                observer.disconnect();
            }
        });
        observer.observe(document.getElementById('page-content'), { childList: true });
    },

    // =================== FAZ 9: VIDEO GÖRÜŞMELER ===================
    async videoCallsPage(user) {
        const content = document.getElementById('page-content');
        const isDoctor = user.role === 'DOCTOR';
        const isPatient = user.role === 'PATIENT';

        content.innerHTML = `
            ${isDoctor ? `<div class="card" style="border-color:var(--primary)">
                <div class="card-header"><h2><i class="fa-solid fa-video"></i> Yeni Video Görüşme Başlat</h2></div>
                <div class="card-body" id="vc-create-area"><p style="color:var(--text-secondary)">Yükleniyor...</p></div>
            </div>` : ''}
            <div class="card">
                <div class="card-header"><h2><i class="fa-solid fa-phone-volume" style="color:var(--success)"></i> Aktif Görüşmeler</h2></div>
                <div class="card-body" id="vc-active"><p style="color:var(--text-secondary)">Yükleniyor...</p></div>
            </div>
            <div class="card">
                <div class="card-header"><h2><i class="fa-solid fa-clock-rotate-left"></i> Görüşme Geçmişi</h2></div>
                <div class="card-body" id="vc-history"><p style="color:var(--text-secondary)">Yükleniyor...</p></div>
            </div>`;

        try {
            // Doktor: Hasta seçip görüşme başlatma formu
            if (isDoctor) {
                const appts = await Api.getMyDoctorAppointments().catch(() => []);
                const activeAppts = appts.filter(a => a.status === 'APPROVED');
                const area = document.getElementById('vc-create-area');
                if (!activeAppts.length) {
                    area.innerHTML = '<div class="empty-state"><i class="fa-solid fa-calendar-xmark"></i><h3>Onaylanmış randevu yok</h3><p>Video görüşme başlatmak için önce bir randevuyu onaylamalısınız.</p></div>';
                } else {
                    area.innerHTML = `<form id="vc-create-form" style="display:flex;gap:1rem;align-items:flex-end;flex-wrap:wrap">
                        <div class="form-group" style="flex:1;min-width:200px"><label>Hasta (Randevu)</label><div class="form-input-wrapper"><i class="fa-solid fa-user"></i>
                            <select id="vc-appt" class="form-select">${activeAppts.map(a => `<option value="${a.id}">${a.patientName} — ${new Date(a.appointmentDate).toLocaleDateString('tr-TR')}</option>`).join('')}</select>
                        </div></div>
                        <button type="submit" class="btn btn-primary"><i class="fa-solid fa-video"></i> Görüşme Başlat</button>
                    </form>`;
                    document.getElementById('vc-create-form').addEventListener('submit', async (e) => {
                        e.preventDefault();
                        try {
                            const call = await Api.createVideoCall({ appointmentId: parseInt(document.getElementById('vc-appt').value) });
                            App.toast('Video görüşme oluşturuldu! Oda: ' + call.roomId, 'success');
                            App.navigate('video-calls');
                        } catch (err) { App.toast(err.message, 'error'); }
                    });
                }
            }

            // Aktif görüşmeler
            const activeCalls = await Api.getActiveCalls().catch(() => []);
            const activeEl = document.getElementById('vc-active');
            if (!activeCalls.length) {
                activeEl.innerHTML = '<div class="empty-state"><i class="fa-solid fa-phone-slash"></i><h3>Aktif görüşme yok</h3></div>';
            } else {
                activeEl.innerHTML = activeCalls.map(c => {
                    const statusColor = c.status==='ACTIVE' ? 'var(--success)' : 'var(--warning)';
                    const statusText = c.status==='WAITING' ? 'Bekleniyor' : 'Aktif';
                    // Hasta hem WAITING hem ACTIVE'de katılabilmeli, Doktor da aynı şekilde
                    const showJoin = (c.status==='WAITING' || c.status==='ACTIVE');
                    const showEnd = (c.status==='ACTIVE');
                    return `<div style="background:var(--bg-dark);border-radius:var(--radius-md);padding:1.25rem;margin-bottom:1rem;border-left:3px solid ${statusColor}">
                    <div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:1rem">
                        <div>
                            <h3>${c.status==='ACTIVE'?'<span class="vc-active-pulse"></span>':''}<i class="fa-solid fa-video" style="color:${statusColor}"></i> ${isDoctor ? c.patientName : 'Dr. ' + c.doctorName}</h3>
                            <p style="color:var(--text-muted);font-size:0.85rem">Oda: ${c.roomId} · Durum: <span class="badge badge-${c.status.toLowerCase()}">${statusText}</span></p>
                        </div>
                        <div style="display:flex;gap:0.5rem">
                            ${showJoin ? `<button class="btn btn-success btn-sm btn-vc-join" data-room="${c.roomId}"><i class="fa-solid fa-video"></i> Görüşmeye Katıl</button>` : ''}
                            ${showEnd ? `<button class="btn btn-danger btn-sm btn-vc-end" data-room="${c.roomId}"><i class="fa-solid fa-phone-slash"></i> Sonlandır</button>` : ''}
                        </div>
                    </div>
                </div>`;
                }).join('');
                activeEl.querySelectorAll('.btn-vc-join').forEach(b => b.addEventListener('click', async () => {
                    try {
                        await Api.joinVideoCall(b.dataset.room);
                        this.openVideoCall(b.dataset.room);
                    } catch(e) { App.toast(e.message, 'error'); }
                }));
                activeEl.querySelectorAll('.btn-vc-end').forEach(b => b.addEventListener('click', async () => {
                    if(!confirm('Görüşmeyi sonlandırmak istediğinize emin misiniz?')) return;
                    try { await Api.endVideoCall(b.dataset.room); App.toast('Görüşme sonlandırıldı', 'info'); App.navigate('video-calls'); } catch(e) { App.toast(e.message, 'error'); }
                }));
            }

            // Geçmiş görüşmeler
            const allCalls = await Api.getMyCalls().catch(() => []);
            const pastCalls = allCalls.filter(c => c.status === 'COMPLETED' || c.status === 'MISSED' || c.status === 'CANCELLED');
            const historyEl = document.getElementById('vc-history');
            if (!pastCalls.length) {
                historyEl.innerHTML = '<div class="empty-state"><i class="fa-solid fa-clock-rotate-left"></i><h3>Geçmiş görüşme yok</h3></div>';
            } else {
                historyEl.innerHTML = pastCalls.map(c => {
                    const dur = c.durationSeconds ? Math.floor(c.durationSeconds/60) + 'dk ' + (c.durationSeconds%60) + 'sn' : '—';
                    const statusIcon = c.status === 'COMPLETED' ? 'fa-check-circle' : c.status === 'MISSED' ? 'fa-phone-slash' : 'fa-xmark-circle';
                    const statusColor = c.status === 'COMPLETED' ? 'var(--success)' : 'var(--danger)';
                    return `<div style="background:var(--bg-dark);border-radius:var(--radius-sm);padding:1rem;margin-bottom:0.5rem;display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:0.75rem">
                        <div>
                            <strong><i class="fa-solid ${statusIcon}" style="color:${statusColor}"></i> ${isDoctor ? c.patientName : 'Dr. ' + c.doctorName}</strong>
                            <p style="font-size:0.85rem;color:var(--text-muted)">${c.createdAt ? new Date(c.createdAt).toLocaleDateString('tr-TR') + ' ' + new Date(c.createdAt).toLocaleTimeString('tr-TR',{hour:'2-digit',minute:'2-digit'}) : '—'}</p>
                        </div>
                        <div style="display:flex;align-items:center;gap:0.75rem">
                            <div style="text-align:right">
                                <span style="font-size:0.85rem;color:var(--text-secondary)">Süre: ${dur}</span>
                                ${c.notes ? `<p style="font-size:0.8rem;color:var(--accent-light);margin-top:0.25rem"><i class="fa-solid fa-sticky-note"></i> ${c.notes.substring(0,60)}${c.notes.length>60?'...':''}</p>` : ''}
                            </div>
                            ${isDoctor && c.status === 'COMPLETED' ? `<button class="btn btn-outline btn-sm btn-vc-notes" data-id="${c.id}" data-notes="${(c.notes||'').replace(/"/g,'&quot;')}" title="Not Ekle/Düzenle"><i class="fa-solid fa-pen"></i></button>` : ''}
                        </div>
                    </div>`;
                }).join('');

                // Not ekleme butonları
                historyEl.querySelectorAll('.btn-vc-notes').forEach(b => b.addEventListener('click', () => {
                    const callId = b.dataset.id;
                    const existing = b.dataset.notes;
                    App.showModal('Görüşme Notu', `<div class="form-group"><label>Görüşme notu / özeti</label><div class="form-input-wrapper"><i class="fa-solid fa-sticky-note"></i><input type="text" id="vc-note-input" class="form-input" placeholder="Görüşme notlarını yazın..." value="${existing}"></div></div>`,
                        `<button class="btn btn-outline" id="modal-cancel-btn">İptal</button><button class="btn btn-success" id="modal-save-btn"><i class="fa-solid fa-save"></i> Kaydet</button>`);
                    document.getElementById('modal-cancel-btn').addEventListener('click', () => App.closeModal());
                    document.getElementById('modal-save-btn').addEventListener('click', async () => {
                        try {
                            await Api.saveCallNotes(callId, document.getElementById('vc-note-input').value);
                            App.toast('Not kaydedildi!', 'success');
                            App.closeModal();
                            App.navigate('video-calls');
                        } catch(e) { App.toast(e.message, 'error'); }
                    });
                }));
            }
        } catch(err) { App.toast(err.message, 'error'); }
    },

    // =================== VIDEO GÖRÜŞME — Kamera Önizleme + Jitsi Yeni Sekme (Faz 9 v2) ===================
    openVideoCall(roomId) {
        const jitsiDomain = 'meet.jit.si';
        const userName = App.user?.fullName || 'Kullanıcı';
        const jitsiUrl = `https://${jitsiDomain}/${roomId}#userInfo.displayName=%22${encodeURIComponent(userName)}%22&config.prejoinPageEnabled=false&config.startWithAudioMuted=false&config.startWithVideoMuted=false`;

        App.showModal('🎥 Video Görüşme', `
            <div class="vc-call-container">
                <div class="vc-camera-preview">
                    <video id="vc-local-video" autoplay playsinline muted></video>
                    <div class="vc-camera-overlay" id="vc-camera-overlay">
                        <i class="fa-solid fa-video-slash"></i>
                        <p>Kamera yükleniyor...</p>
                    </div>
                </div>
                <div class="vc-call-info">
                    <div class="vc-call-status">
                        <span class="vc-active-pulse"></span>
                        <span style="font-weight:600;color:var(--success)">Görüşme Aktif</span>
                    </div>
                    <p style="color:var(--text-secondary);font-size:0.85rem;margin-top:0.5rem">
                        <i class="fa-solid fa-door-open" style="color:var(--primary-light)"></i> Oda: <strong>${roomId}</strong>
                    </p>
                    <p style="color:var(--text-muted);font-size:0.8rem;margin-top:0.25rem">
                        <i class="fa-solid fa-user"></i> ${userName}
                    </p>
                </div>
                <div class="vc-call-actions">
                    <button class="btn btn-primary" id="vc-open-jitsi" style="width:100%">
                        <i class="fa-solid fa-up-right-from-square"></i> Jitsi Meet'te Görüşmeyi Aç
                    </button>
                    <div style="display:flex;gap:0.5rem;margin-top:0.5rem">
                        <button class="btn btn-outline" id="vc-toggle-cam" style="flex:1">
                            <i class="fa-solid fa-video"></i> Kamera
                        </button>
                        <button class="btn btn-outline" id="vc-toggle-mic" style="flex:1">
                            <i class="fa-solid fa-microphone"></i> Mikrofon
                        </button>
                    </div>
                </div>
                <p style="text-align:center;color:var(--text-muted);font-size:0.75rem;margin-top:0.75rem">
                    <i class="fa-solid fa-info-circle"></i> Görüşme Jitsi Meet üzerinden yeni sekmede açılır.
                    Kameranız bu ekranda önizleme olarak gösterilir.
                </p>
            </div>`,
            `<button class="btn btn-danger" id="vc-end-call"><i class="fa-solid fa-phone-slash"></i> Görüşmeyi Sonlandır</button>`);

        // Kamera başlat
        let localStream = null;
        let camOn = true;
        let micOn = true;

        const startCamera = async () => {
            try {
                localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
                const videoEl = document.getElementById('vc-local-video');
                if (videoEl) {
                    videoEl.srcObject = localStream;
                    const overlay = document.getElementById('vc-camera-overlay');
                    if (overlay) overlay.style.display = 'none';
                }
            } catch(err) {
                console.warn('Kamera erişim hatası:', err);
                const overlay = document.getElementById('vc-camera-overlay');
                if (overlay) {
                    overlay.innerHTML = '<i class="fa-solid fa-video-slash"></i><p>Kamera erişimi reddedildi</p><p style="font-size:0.75rem;color:var(--text-muted)">Tarayıcı ayarlarından kamera iznini kontrol edin</p>';
                }
            }
        };
        startCamera();

        // Jitsi'yi yeni sekmede aç
        document.getElementById('vc-open-jitsi').addEventListener('click', () => {
            window.open(jitsiUrl, '_blank', 'noopener,noreferrer');
            App.toast('Jitsi Meet yeni sekmede açıldı', 'success');
        });

        // Kamera toggle
        document.getElementById('vc-toggle-cam').addEventListener('click', () => {
            if (!localStream) return;
            camOn = !camOn;
            localStream.getVideoTracks().forEach(t => t.enabled = camOn);
            const btn = document.getElementById('vc-toggle-cam');
            btn.innerHTML = camOn ? '<i class="fa-solid fa-video"></i> Kamera' : '<i class="fa-solid fa-video-slash"></i> Kamera Kapalı';
            btn.className = camOn ? 'btn btn-outline' : 'btn btn-danger';
            btn.style.flex = '1';
        });

        // Mikrofon toggle
        document.getElementById('vc-toggle-mic').addEventListener('click', () => {
            if (!localStream) return;
            micOn = !micOn;
            localStream.getAudioTracks().forEach(t => t.enabled = micOn);
            const btn = document.getElementById('vc-toggle-mic');
            btn.innerHTML = micOn ? '<i class="fa-solid fa-microphone"></i> Mikrofon' : '<i class="fa-solid fa-microphone-slash"></i> Sessiz';
            btn.className = micOn ? 'btn btn-outline' : 'btn btn-warning';
            btn.style.flex = '1';
        });

        // Görüşmeyi sonlandır
        document.getElementById('vc-end-call').addEventListener('click', async () => {
            if (!confirm('Görüşmeyi sonlandırmak istediğinize emin misiniz?')) return;
            // Kamerayı kapat
            if (localStream) {
                localStream.getTracks().forEach(t => t.stop());
                localStream = null;
            }
            try { await Api.endVideoCall(roomId); } catch(e) { /* silent */ }
            App.closeModal();
            App.toast('Görüşme sonlandırıldı', 'info');
            App.navigate('video-calls');
        });

        // Modal kapatılırken kamerayı da kapat
        const origClose = App.closeModal.bind(App);
        App.closeModal = () => {
            if (localStream) {
                localStream.getTracks().forEach(t => t.stop());
                localStream = null;
            }
            origClose();
            App.closeModal = origClose; // restore original
        };
    },

    // Eski fonksiyon uyumluluğu
    openJitsiRoom(roomId) {
        this.openVideoCall(roomId);
    },

    // =================== FAZ 10: ACİL DURUM SAYFASI ===================
    async emergencySOSPage(user) {
        const content = document.getElementById('page-content');
        const isPatient = user.role === 'PATIENT';
        const isDoctor = user.role === 'DOCTOR';
        const isAdmin = user.role === 'ADMIN';

        content.innerHTML = `
            ${isPatient ? `<div class="card" style="border-color:var(--danger)">
                <div class="card-header"><h2><i class="fa-solid fa-triangle-exclamation" style="color:var(--danger)"></i> 🚨 SOS — Acil Yardım Talebi</h2></div>
                <div class="card-body">
                    <form id="sos-form">
                        <div class="form-row">
                            <div class="form-group"><label>Aciliyet Seviyesi</label><div class="form-input-wrapper"><i class="fa-solid fa-gauge-high"></i>
                                <select id="sos-level" class="form-select">
                                    <option value="LOW">Düşük — Danışma</option>
                                    <option value="MEDIUM" selected>Orta — Acil Yardım</option>
                                    <option value="HIGH">Yüksek — Ciddi Durum</option>
                                    <option value="CRITICAL">Kritik — Hayati Tehlike!</option>
                                </select>
                            </div></div>
                            <div class="form-group"><label>Konum / Adres</label><div style="display:flex;gap:0.5rem"><div class="form-input-wrapper" style="flex:1"><i class="fa-solid fa-location-dot"></i>
                                <input type="text" id="sos-location" class="form-input" placeholder="Bulunduğunuz adres">
                            </div><button type="button" class="btn btn-info btn-sm" id="btn-gps" title="GPS ile konumumu al" style="white-space:nowrap"><i class="fa-solid fa-location-crosshairs"></i> GPS</button></div></div>
                        </div>
                        <div class="form-group"><label>Belirtiler</label><div class="form-input-wrapper"><i class="fa-solid fa-heart-pulse"></i>
                            <input type="text" id="sos-symptoms" class="form-input" placeholder="Göğüs ağrısı, nefes darlığı, baş dönmesi...">
                        </div></div>
                        <div class="form-group"><label>Açıklama</label><div class="form-input-wrapper"><i class="fa-solid fa-pen"></i>
                            <input type="text" id="sos-desc" class="form-input" placeholder="Durumunuzu detaylı açıklayın...">
                        </div></div>
                        <button type="submit" class="btn btn-danger" style="width:100%;font-size:1.1rem;padding:0.9rem"><i class="fa-solid fa-phone-volume"></i> ACİL YARDIM TALEP ET</button>
                    </form>
                </div>
            </div>` : ''}

            ${isDoctor || isAdmin ? `<div class="card" style="border-color:var(--warning)">
                <div class="card-header" style="justify-content:space-between"><h2><i class="fa-solid fa-triangle-exclamation" style="color:var(--warning)"></i> Bekleyen Acil Talepler</h2>
                <span class="badge badge-pending" id="sos-pending-badge" style="font-size:0.9rem">—</span></div>
                <div class="card-body" id="sos-pending"><p style="color:var(--text-secondary)">Yükleniyor...</p></div>
            </div>` : ''}

            ${isDoctor ? `<div class="card">
                <div class="card-header"><h2><i class="fa-solid fa-user-doctor"></i> Üstlendiğim Talepler</h2></div>
                <div class="card-body" id="sos-assigned"><p style="color:var(--text-secondary)">Yükleniyor...</p></div>
            </div>` : ''}

            ${isPatient ? `<div class="card">
                <div class="card-header"><h2><i class="fa-solid fa-clock-rotate-left"></i> Acil Talep Geçmişim</h2></div>
                <div class="card-body" id="sos-history"><p style="color:var(--text-secondary)">Yükleniyor...</p></div>
            </div>` : ''}

            ${isAdmin ? `<div class="card">
                <div class="card-header"><h2><i class="fa-solid fa-list"></i> Tüm Talepler</h2></div>
                <div class="card-body" id="sos-all"><p style="color:var(--text-secondary)">Yükleniyor...</p></div>
            </div>` : ''}`;

        const levelLabel = {LOW:'Düşük',MEDIUM:'Orta',HIGH:'Yüksek',CRITICAL:'Kritik'};
        const levelColor = {LOW:'var(--info)',MEDIUM:'var(--warning)',HIGH:'var(--danger)',CRITICAL:'#dc2626'};
        const statusLabel = {PENDING:'Beklemede',RESPONDING:'Yanıtlanıyor',RESOLVED:'Çözüldü',CANCELLED:'İptal'};

        const renderEmergencyCard = (e, showActions) => {
            const borderColor = levelColor[e.level] || 'var(--border)';
            return `<div style="background:var(--bg-dark);border-radius:var(--radius-md);padding:1.25rem;margin-bottom:1rem;border-left:4px solid ${borderColor}">
                <div style="display:flex;justify-content:space-between;align-items:flex-start;flex-wrap:wrap;gap:1rem">
                    <div>
                        <div style="display:flex;align-items:center;gap:0.75rem;margin-bottom:0.5rem">
                            <span style="background:${borderColor};color:white;padding:0.2rem 0.6rem;border-radius:999px;font-size:0.75rem;font-weight:700">${levelLabel[e.level]||e.level}</span>
                            <span class="badge badge-${e.status.toLowerCase()}">${statusLabel[e.status]||e.status}</span>
                            <span style="color:var(--text-muted);font-size:0.8rem">#${e.id}</span>
                        </div>
                        <h3>${e.patientName}</h3>
                        ${e.symptoms ? `<p style="color:var(--text-secondary);font-size:0.9rem;margin-top:0.25rem"><i class="fa-solid fa-heart-pulse" style="color:var(--danger)"></i> ${e.symptoms}</p>` : ''}
                        ${e.description ? `<p style="font-size:0.85rem;color:var(--text-muted);margin-top:0.25rem">${e.description}</p>` : ''}
                        ${e.location ? `<p style="font-size:0.85rem;margin-top:0.25rem"><i class="fa-solid fa-location-dot" style="color:var(--primary-light)"></i> ${e.location}</p>` : ''}
                        ${e.assignedDoctorName ? `<p style="font-size:0.85rem;margin-top:0.5rem;color:var(--success)"><i class="fa-solid fa-user-doctor"></i> Dr. ${e.assignedDoctorName}</p>` : ''}
                        ${e.responseNotes ? `<p style="font-size:0.85rem;margin-top:0.25rem;color:var(--accent-light)"><i class="fa-solid fa-sticky-note"></i> ${e.responseNotes}</p>` : ''}
                        <p style="font-size:0.75rem;color:var(--text-muted);margin-top:0.5rem">${e.createdAt ? new Date(e.createdAt).toLocaleDateString('tr-TR') + ' ' + new Date(e.createdAt).toLocaleTimeString('tr-TR',{hour:'2-digit',minute:'2-digit'}) : ''}</p>
                    </div>
                    <div style="display:flex;gap:0.5rem;flex-wrap:wrap">
                        ${showActions && e.status === 'PENDING' && isDoctor ? `<button class="btn btn-success btn-sm btn-sos-respond" data-id="${e.id}"><i class="fa-solid fa-hand-holding-medical"></i> Üstlen</button>` : ''}
                        ${showActions && e.status === 'RESPONDING' && isDoctor ? `<button class="btn btn-primary btn-sm btn-sos-resolve" data-id="${e.id}"><i class="fa-solid fa-check-circle"></i> Çöz</button>` : ''}
                        ${e.status === 'PENDING' && isPatient ? `<button class="btn btn-outline btn-sm btn-sos-cancel" data-id="${e.id}"><i class="fa-solid fa-xmark"></i> İptal</button>` : ''}
                    </div>
                </div>
            </div>`;
        };

        const bindSosActions = (container) => {
            container.querySelectorAll('.btn-sos-respond').forEach(b => b.addEventListener('click', async () => {
                if(!confirm('Bu acil talebi üstlenmek istediğinize emin misiniz?')) return;
                try { await Api.respondEmergency(b.dataset.id); App.toast('Talep üstlenildi!', 'success'); App.navigate('emergency-sos'); } catch(e) { App.toast(e.message, 'error'); }
            }));
            container.querySelectorAll('.btn-sos-resolve').forEach(b => b.addEventListener('click', async () => {
                const notes = prompt('Çözüm notları:');
                if(notes === null) return;
                try { await Api.resolveEmergency(b.dataset.id, notes); App.toast('Acil durum çözüldü!', 'success'); App.navigate('emergency-sos'); } catch(e) { App.toast(e.message, 'error'); }
            }));
            container.querySelectorAll('.btn-sos-cancel').forEach(b => b.addEventListener('click', async () => {
                if(!confirm('Acil talebinizi iptal etmek istediğinize emin misiniz?')) return;
                try { await Api.cancelEmergency(b.dataset.id); App.toast('Talep iptal edildi', 'info'); App.navigate('emergency-sos'); } catch(e) { App.toast(e.message, 'error'); }
            }));
        };

        try {
            // Hasta: SOS form
            if (isPatient) {
                document.getElementById('sos-form').addEventListener('submit', async (e) => {
                    e.preventDefault();
                    if(!confirm('ACİL YARDIM TALEBİ GÖNDERİLECEK. Emin misiniz?')) return;
                    try {
                        await Api.createEmergency({
                            level: document.getElementById('sos-level').value,
                            location: document.getElementById('sos-location').value,
                            symptoms: document.getElementById('sos-symptoms').value,
                            description: document.getElementById('sos-desc').value
                        });
                        App.toast('🚨 Acil yardım talebiniz gönderildi! Doktorlar bilgilendirildi.', 'success');
                        App.navigate('emergency-sos');
                    } catch(err) { App.toast(err.message, 'error'); }
                });

                // GPS Konum Butonu
                const gpsBtn = document.getElementById('btn-gps');
                if (gpsBtn) {
                    gpsBtn.addEventListener('click', () => {
                        if (!navigator.geolocation) { App.toast('Tarayıcınız GPS desteklemiyor', 'error'); return; }
                        gpsBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i>';
                        gpsBtn.disabled = true;
                        navigator.geolocation.getCurrentPosition(
                            (pos) => {
                                const lat = pos.coords.latitude.toFixed(6);
                                const lng = pos.coords.longitude.toFixed(6);
                                document.getElementById('sos-location').value = `GPS: ${lat}, ${lng}`;
                                gpsBtn.innerHTML = '<i class="fa-solid fa-check"></i> Alındı';
                                gpsBtn.disabled = false;
                                App.toast('Konum alındı ✅', 'success');
                            },
                            (err) => {
                                App.toast('Konum alınamadı: ' + err.message, 'error');
                                gpsBtn.innerHTML = '<i class="fa-solid fa-location-crosshairs"></i> GPS';
                                gpsBtn.disabled = false;
                            },
                            { enableHighAccuracy: true, timeout: 10000 }
                        );
                    });
                }
                // Geçmiş
                const myEmergencies = await Api.getMyEmergencies().catch(() => []);
                const historyEl = document.getElementById('sos-history');
                if (!myEmergencies.length) {
                    historyEl.innerHTML = '<div class="empty-state"><i class="fa-solid fa-check-circle"></i><h3>Acil talep geçmişi boş</h3></div>';
                } else {
                    historyEl.innerHTML = myEmergencies.map(e => renderEmergencyCard(e, false)).join('');
                    bindSosActions(historyEl);
                }
            }

            // Doktor/Admin: Bekleyen talepler
            if (isDoctor || isAdmin) {
                const pending = await Api.getPendingEmergencies().catch(() => []);
                const pendingEl = document.getElementById('sos-pending');
                document.getElementById('sos-pending-badge').textContent = pending.length + ' talep';
                if (!pending.length) {
                    pendingEl.innerHTML = '<div class="empty-state"><i class="fa-solid fa-check-double"></i><h3>Bekleyen acil talep yok</h3><p>Tüm talepler yanıtlanmış.</p></div>';
                } else {
                    pendingEl.innerHTML = pending.map(e => renderEmergencyCard(e, true)).join('');
                    bindSosActions(pendingEl);
                }
            }

            // Doktor: Üstlendiğim talepler
            if (isDoctor) {
                const assigned = await Api.getAssignedEmergencies().catch(() => []);
                const assignedEl = document.getElementById('sos-assigned');
                if (!assigned.length) {
                    assignedEl.innerHTML = '<div class="empty-state"><i class="fa-solid fa-clipboard-check"></i><h3>Üstlendiğiniz talep yok</h3></div>';
                } else {
                    assignedEl.innerHTML = assigned.map(e => renderEmergencyCard(e, true)).join('');
                    bindSosActions(assignedEl);
                }
            }

            // Admin: Tüm talepler
            if (isAdmin) {
                const allReqs = await Api.getAllEmergencies().catch(() => []);
                const allEl = document.getElementById('sos-all');
                if (!allReqs.length) {
                    allEl.innerHTML = '<div class="empty-state"><i class="fa-solid fa-list"></i><h3>Hiç acil talep yok</h3></div>';
                } else {
                    allEl.innerHTML = allReqs.map(e => renderEmergencyCard(e, false)).join('');
                }
            }
        } catch(err) { App.toast(err.message, 'error'); }
    },

    // =================== FAZ 11: ADMİN PANEL — SİSTEM RAPORU ===================
    async adminPanelPage() {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="empty-state"><i class="fa-solid fa-spinner fa-spin"></i><h3>Rapor yükleniyor...</h3></div>';

        try {
            const r = await Api.getAdminReport();

            // İstatistik kartları
            const stats = [
                { icon: 'fa-users', label: 'Toplam Kullanıcı', value: r.totalUsers, color: 'var(--primary)' },
                { icon: 'fa-user-injured', label: 'Hasta', value: r.totalPatients, color: 'var(--info)' },
                { icon: 'fa-user-doctor', label: 'Doktor', value: r.totalDoctors, color: 'var(--success)' },
                { icon: 'fa-user-shield', label: 'Admin', value: r.totalAdmins, color: 'var(--warning)' },
                { icon: 'fa-calendar-check', label: 'Toplam Randevu', value: r.totalAppointments, color: 'var(--accent)' },
                { icon: 'fa-clock', label: 'Bekleyen Randevu', value: r.pendingAppointments, color: 'var(--warning)' },
                { icon: 'fa-check-double', label: 'Tamamlanan', value: r.completedAppointments, color: 'var(--success)' },
                { icon: 'fa-ban', label: 'İptal', value: r.cancelledAppointments, color: 'var(--danger)' },
                { icon: 'fa-prescription', label: 'Reçete', value: r.totalPrescriptions, color: 'var(--primary-light)' },
                { icon: 'fa-notes-medical', label: 'Tıbbi Kayıt', value: r.totalMedicalRecords, color: 'var(--accent-light)' },
                { icon: 'fa-flask', label: 'Lab Sonucu', value: r.totalLabResults || 0, color: '#8b5cf6' },
                { icon: 'fa-comment-dots', label: 'Mesaj', value: r.totalMessages || 0, color: '#06b6d4' },
                { icon: 'fa-video', label: 'Video Görüşme', value: r.totalVideoCalls || 0, color: '#f59e0b' },
                { icon: 'fa-phone-volume', label: 'Aktif Görüşme', value: r.activeVideoCalls || 0, color: 'var(--success)' },
                { icon: 'fa-triangle-exclamation', label: 'Acil Talep', value: r.totalEmergencyRequests || 0, color: 'var(--danger)' },
                { icon: 'fa-hourglass-half', label: 'Bekleyen Acil', value: r.pendingEmergencyRequests || 0, color: '#dc2626' },
                { icon: 'fa-user-clock', label: 'Doktor Onay Bekl.', value: r.pendingDoctorApprovals || 0, color: 'var(--warning)' },
                { icon: 'fa-clipboard-list', label: 'İşlem Logu', value: r.totalAuditLogs || 0, color: '#64748b' },
            ];

            // Trend bar chart renderer
            const renderBarChart = (data, title) => {
                if (!data || !data.length) return '<p style="color:var(--text-muted)">Veri yok</p>';
                const maxVal = Math.max(...data.map(d => d.count), 1);
                return `<h3 style="margin-bottom:1rem;font-size:0.95rem;color:var(--text-secondary)">${title}</h3>
                <div style="display:flex;align-items:flex-end;gap:0.5rem;height:120px;padding-top:10px">
                    ${data.map(d => `<div style="flex:1;display:flex;flex-direction:column;align-items:center;gap:0.3rem">
                        <span style="font-size:0.7rem;color:var(--text-muted)">${d.count}</span>
                        <div style="width:100%;background:linear-gradient(180deg,var(--primary),var(--primary-light));border-radius:4px 4px 0 0;height:${Math.max((d.count/maxVal)*100, 4)}%;min-height:4px;transition:height 0.5s"></div>
                        <span style="font-size:0.65rem;color:var(--text-muted)">${d.label}</span>
                    </div>`).join('')}
                </div>`;
            };

            content.innerHTML = `
                <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(180px,1fr));gap:1rem;margin-bottom:1.5rem">
                    ${stats.map(s => `<div style="background:var(--bg-card);border:1px solid var(--border);border-radius:var(--radius-md);padding:1.25rem;display:flex;align-items:center;gap:1rem;transition:transform 0.2s,box-shadow 0.2s" onmouseover="this.style.transform='translateY(-2px)';this.style.boxShadow='0 4px 12px rgba(0,0,0,0.3)'" onmouseout="this.style.transform='';this.style.boxShadow=''">
                        <div style="width:42px;height:42px;border-radius:var(--radius-sm);background:${s.color}22;display:flex;align-items:center;justify-content:center">
                            <i class="fa-solid ${s.icon}" style="color:${s.color};font-size:1.1rem"></i>
                        </div>
                        <div>
                            <div style="font-size:1.4rem;font-weight:700;color:var(--text-primary)">${s.value}</div>
                            <div style="font-size:0.75rem;color:var(--text-muted)">${s.label}</div>
                        </div>
                    </div>`).join('')}
                </div>

                <div style="display:grid;grid-template-columns:1fr 1fr;gap:1.5rem">
                    <div class="card"><div class="card-header"><h2><i class="fa-solid fa-chart-bar" style="color:var(--primary)"></i> Randevu Trendi (7 Gün)</h2></div>
                        <div class="card-body">${renderBarChart(r.appointmentTrend, '')}</div>
                    </div>
                    <div class="card"><div class="card-header"><h2><i class="fa-solid fa-user-plus" style="color:var(--success)"></i> Kayıt Trendi (7 Gün)</h2></div>
                        <div class="card-body">${renderBarChart(r.registrationTrend, '')}</div>
                    </div>
                </div>

                <div class="card" style="margin-top:1.5rem">
                    <div class="card-header"><h2><i class="fa-solid fa-bullhorn" style="color:var(--warning)"></i> Toplu Bildirim Gönder</h2></div>
                    <div class="card-body">
                        <form id="broadcast-form" style="display:flex;gap:1rem;align-items:flex-end;flex-wrap:wrap">
                            <div class="form-group" style="flex:2;min-width:250px"><label>Mesaj</label><div class="form-input-wrapper"><i class="fa-solid fa-pen"></i>
                                <input type="text" id="broadcast-msg" class="form-input" placeholder="Tüm kullanıcılara gönderilecek mesaj..." required>
                            </div></div>
                            <div class="form-group" style="flex:1;min-width:150px"><label>Hedef</label><div class="form-input-wrapper"><i class="fa-solid fa-users"></i>
                                <select id="broadcast-role" class="form-select">
                                    <option value="ALL">Herkes</option>
                                    <option value="PATIENT">Hastalar</option>
                                    <option value="DOCTOR">Doktorlar</option>
                                    <option value="ADMIN">Adminler</option>
                                </select>
                            </div></div>
                            <button type="submit" class="btn btn-warning"><i class="fa-solid fa-paper-plane"></i> Gönder</button>
                        </form>
                    </div>
                </div>`;

            document.getElementById('broadcast-form').addEventListener('submit', async (e) => {
                e.preventDefault();
                const msg = document.getElementById('broadcast-msg').value;
                const role = document.getElementById('broadcast-role').value;
                if (!msg.trim()) return;
                try {
                    const res = await Api.broadcastNotification(msg, role);
                    App.toast(res.message || 'Bildirim gönderildi!', 'success');
                    document.getElementById('broadcast-msg').value = '';
                } catch(err) { App.toast(err.message, 'error'); }
            });
        } catch(err) {
            content.innerHTML = '<div class="empty-state"><i class="fa-solid fa-circle-xmark" style="color:var(--danger)"></i><h3>Rapor yüklenemedi</h3><p>' + err.message + '</p></div>';
        }
    },

    // =================== FAZ 11: KULLANICI YÖNETİMİ ===================
    async adminUsersPage() {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="empty-state"><i class="fa-solid fa-spinner fa-spin"></i><h3>Yükleniyor...</h3></div>';

        try {
            let users = await Api.getAdminUsers();
            let filterRole = 'ALL';

            const renderUsers = (list) => {
                const statusBadge = (s) => {
                    const colors = { ACTIVE: 'var(--success)', FROZEN: 'var(--warning)', DELETED: 'var(--danger)', PENDING_VERIFY: 'var(--info)' };
                    const labels = { ACTIVE: 'Aktif', FROZEN: 'Dondurulmuş', DELETED: 'Silindi', PENDING_VERIFY: 'Doğrulama Bekliyor' };
                    return `<span style="background:${colors[s]||'var(--border)'}22;color:${colors[s]||'var(--text-muted)'};padding:0.15rem 0.5rem;border-radius:999px;font-size:0.7rem;font-weight:600">${labels[s]||s}</span>`;
                };
                const roleBadge = (r) => {
                    const colors = { PATIENT: 'var(--info)', DOCTOR: 'var(--success)', ADMIN: 'var(--warning)' };
                    const labels = { PATIENT: 'Hasta', DOCTOR: 'Doktor', ADMIN: 'Admin' };
                    return `<span style="background:${colors[r]||'var(--border)'}22;color:${colors[r]||'var(--text-muted)'};padding:0.15rem 0.5rem;border-radius:999px;font-size:0.7rem;font-weight:600">${labels[r]||r}</span>`;
                };

                return `<div style="overflow-x:auto">
                    <table style="width:100%;border-collapse:collapse;font-size:0.85rem">
                        <thead><tr style="border-bottom:2px solid var(--border)">
                            <th style="padding:0.75rem;text-align:left;color:var(--text-muted)">ID</th>
                            <th style="padding:0.75rem;text-align:left;color:var(--text-muted)">Ad Soyad</th>
                            <th style="padding:0.75rem;text-align:left;color:var(--text-muted)">E-posta</th>
                            <th style="padding:0.75rem;text-align:left;color:var(--text-muted)">Rol</th>
                            <th style="padding:0.75rem;text-align:left;color:var(--text-muted)">Durum</th>
                            <th style="padding:0.75rem;text-align:left;color:var(--text-muted)">Kayıt</th>
                            <th style="padding:0.75rem;text-align:center;color:var(--text-muted)">İşlem</th>
                        </tr></thead>
                        <tbody>${list.map(u => `<tr style="border-bottom:1px solid var(--border);transition:background 0.2s" onmouseover="this.style.background='var(--bg-dark)'" onmouseout="this.style.background=''">
                            <td style="padding:0.75rem;color:var(--text-muted)">#${u.id}</td>
                            <td style="padding:0.75rem;font-weight:600">${u.fullName}</td>
                            <td style="padding:0.75rem;color:var(--text-secondary)">${u.email}</td>
                            <td style="padding:0.75rem">${roleBadge(u.role)}</td>
                            <td style="padding:0.75rem">${statusBadge(u.accountStatus)}</td>
                            <td style="padding:0.75rem;color:var(--text-muted);font-size:0.8rem">${u.createdAt ? new Date(u.createdAt).toLocaleDateString('tr-TR') : '—'}</td>
                            <td style="padding:0.75rem;text-align:center">
                                <div style="display:flex;gap:0.3rem;justify-content:center">
                                    <select class="user-role-select" data-id="${u.id}" style="background:var(--bg-dark);color:var(--text-primary);border:1px solid var(--border);border-radius:var(--radius-sm);padding:0.25rem 0.4rem;font-size:0.75rem;cursor:pointer">
                                        <option value="PATIENT" ${u.role==='PATIENT'?'selected':''}>Hasta</option>
                                        <option value="DOCTOR" ${u.role==='DOCTOR'?'selected':''}>Doktor</option>
                                        <option value="ADMIN" ${u.role==='ADMIN'?'selected':''}>Admin</option>
                                    </select>
                                    <select class="user-status-select" data-id="${u.id}" style="background:var(--bg-dark);color:var(--text-primary);border:1px solid var(--border);border-radius:var(--radius-sm);padding:0.25rem 0.4rem;font-size:0.75rem;cursor:pointer">
                                        <option value="ACTIVE" ${u.accountStatus==='ACTIVE'?'selected':''}>Aktif</option>
                                        <option value="FROZEN" ${u.accountStatus==='FROZEN'?'selected':''}>Dondur</option>
                                        <option value="DELETED" ${u.accountStatus==='DELETED'?'selected':''}>Sil</option>
                                    </select>
                                    <button class="btn btn-danger btn-sm btn-delete-user" data-id="${u.id}" data-name="${u.fullName}" style="padding:0.25rem 0.5rem"><i class="fa-solid fa-trash-can"></i></button>
                                </div>
                            </td>
                        </tr>`).join('')}</tbody>
                    </table>
                </div>`;
            };

            const render = () => {
                const filtered = filterRole === 'ALL' ? users : users.filter(u => u.role === filterRole);

                content.innerHTML = `
                    <div class="card">
                        <div class="card-header" style="justify-content:space-between;flex-wrap:wrap;gap:1rem">
                            <h2><i class="fa-solid fa-users-gear" style="color:var(--primary)"></i> Kullanıcılar (${filtered.length})</h2>
                            <div style="display:flex;gap:0.5rem">
                                ${['ALL','PATIENT','DOCTOR','ADMIN'].map(r => `<button class="btn ${filterRole===r?'btn-primary':'btn-outline'} btn-sm btn-role-filter" data-role="${r}">${r==='ALL'?'Hepsi':r==='PATIENT'?'Hasta':r==='DOCTOR'?'Doktor':'Admin'}</button>`).join('')}
                            </div>
                        </div>
                        <div class="card-body">
                            ${filtered.length ? renderUsers(filtered) : '<div class="empty-state"><i class="fa-solid fa-users-slash"></i><h3>Kullanıcı bulunamadı</h3></div>'}
                        </div>
                    </div>`;

                // Filter buttons
                content.querySelectorAll('.btn-role-filter').forEach(b => b.addEventListener('click', () => {
                    filterRole = b.dataset.role;
                    render();
                }));

                // Role change
                content.querySelectorAll('.user-role-select').forEach(sel => sel.addEventListener('change', async () => {
                    try {
                        await Api.changeUserRole(sel.dataset.id, sel.value);
                        App.toast('Rol güncellendi', 'success');
                        users = await Api.getAdminUsers();
                        render();
                    } catch(e) { App.toast(e.message, 'error'); }
                }));

                // Status change
                content.querySelectorAll('.user-status-select').forEach(sel => sel.addEventListener('change', async () => {
                    try {
                        await Api.changeUserStatus(sel.dataset.id, sel.value);
                        App.toast('Durum güncellendi', 'success');
                        users = await Api.getAdminUsers();
                        render();
                    } catch(e) { App.toast(e.message, 'error'); }
                }));

                // Delete
                content.querySelectorAll('.btn-delete-user').forEach(b => b.addEventListener('click', async () => {
                    if (!confirm(b.dataset.name + ' kullanıcısını silmek istediğinize emin misiniz? Bu işlem geri alınamaz!')) return;
                    try {
                        await Api.deleteAdminUser(b.dataset.id);
                        App.toast('Kullanıcı silindi', 'info');
                        users = await Api.getAdminUsers();
                        render();
                    } catch(e) { App.toast(e.message, 'error'); }
                }));
            };

            render();
        } catch(err) { content.innerHTML = '<div class="empty-state"><i class="fa-solid fa-circle-xmark" style="color:var(--danger)"></i><h3>Yüklenemedi</h3><p>' + err.message + '</p></div>'; }
    },

    // =================== FAZ 11: İŞLEM LOGLARI (AUDİT) ===================
    async auditLogsPage() {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="empty-state"><i class="fa-solid fa-spinner fa-spin"></i><h3>Yükleniyor...</h3></div>';

        try {
            const logs = await Api.getAuditLogs();

            const actionIcons = {
                'LOGIN': 'fa-right-to-bracket', 'LOGOUT': 'fa-right-from-bracket',
                'VIEW_RECORD': 'fa-eye', 'UPDATE_PROFILE': 'fa-pen',
                'DELETE_USER': 'fa-trash', 'CREATE_APPOINTMENT': 'fa-calendar-plus',
                'APPROVE_DOCTOR': 'fa-user-check', 'REJECT_DOCTOR': 'fa-user-xmark',
            };
            const actionColors = {
                'LOGIN': 'var(--success)', 'LOGOUT': 'var(--info)',
                'DELETE_USER': 'var(--danger)', 'APPROVE_DOCTOR': 'var(--success)',
                'REJECT_DOCTOR': 'var(--danger)',
            };

            content.innerHTML = `
                <div class="card">
                    <div class="card-header" style="justify-content:space-between;flex-wrap:wrap;gap:1rem">
                        <h2><i class="fa-solid fa-clipboard-list" style="color:var(--accent)"></i> Son İşlem Logları (${logs.length})</h2>
                        <div class="form-group" style="margin:0;min-width:220px"><div class="form-input-wrapper"><i class="fa-solid fa-search"></i>
                            <input type="text" id="audit-search" class="form-input" placeholder="Kullanıcı e-posta ile filtrele...">
                        </div></div>
                    </div>
                    <div class="card-body" id="audit-list">
                        ${!logs.length ? '<div class="empty-state"><i class="fa-solid fa-clipboard-check"></i><h3>Henüz işlem logu yok</h3></div>' :
                        `<div style="overflow-x:auto"><table style="width:100%;border-collapse:collapse;font-size:0.85rem">
                            <thead><tr style="border-bottom:2px solid var(--border)">
                                <th style="padding:0.6rem;text-align:left;color:var(--text-muted)">Zaman</th>
                                <th style="padding:0.6rem;text-align:left;color:var(--text-muted)">İşlem</th>
                                <th style="padding:0.6rem;text-align:left;color:var(--text-muted)">Tür/ID</th>
                                <th style="padding:0.6rem;text-align:left;color:var(--text-muted)">Yapan</th>
                                <th style="padding:0.6rem;text-align:left;color:var(--text-muted)">Detay</th>
                            </tr></thead>
                            <tbody>${logs.map(l => {
                                const icon = actionIcons[l.action] || 'fa-circle-info';
                                const color = actionColors[l.action] || 'var(--text-secondary)';
                                return `<tr class="audit-row" data-email="${(l.performedBy||'').toLowerCase()}" style="border-bottom:1px solid var(--border)">
                                    <td style="padding:0.6rem;color:var(--text-muted);font-size:0.8rem;white-space:nowrap">${l.createdAt ? new Date(l.createdAt).toLocaleDateString('tr-TR') + ' ' + new Date(l.createdAt).toLocaleTimeString('tr-TR',{hour:'2-digit',minute:'2-digit',second:'2-digit'}) : '—'}</td>
                                    <td style="padding:0.6rem"><span style="color:${color}"><i class="fa-solid ${icon}"></i> ${l.action}</span></td>
                                    <td style="padding:0.6rem;color:var(--text-secondary)">${l.entityType || '—'} ${l.entityId ? '#'+l.entityId : ''}</td>
                                    <td style="padding:0.6rem;font-weight:500">${l.performedBy || '—'}</td>
                                    <td style="padding:0.6rem;color:var(--text-muted);font-size:0.8rem;max-width:250px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">${l.details || '—'}</td>
                                </tr>`;
                            }).join('')}</tbody>
                        </table></div>`}
                    </div>
                </div>`;

            // Search filter
            const searchInput = document.getElementById('audit-search');
            if (searchInput) {
                searchInput.addEventListener('input', () => {
                    const q = searchInput.value.toLowerCase();
                    document.querySelectorAll('.audit-row').forEach(row => {
                        row.style.display = row.dataset.email.includes(q) ? '' : 'none';
                    });
                });
            }
        } catch(err) {
            content.innerHTML = '<div class="empty-state"><i class="fa-solid fa-circle-xmark" style="color:var(--danger)"></i><h3>Loglar yüklenemedi</h3><p>' + err.message + '</p></div>';
        }
    },

    // =================== FAZ 4: LAB SONUÇLARI SAYFASI ===================
    async labResultsPage(user) {
        const content = document.getElementById('page-content');
        const isPatient = user.role === 'PATIENT';
        const isDoctor = user.role === 'DOCTOR';

        content.innerHTML = `
            ${isDoctor ? `<div class="card" style="border-color:var(--accent)">
                <div class="card-header"><h2><i class="fa-solid fa-flask" style="color:var(--accent)"></i> Hasta İçin Lab Sonucu Ekle</h2></div>
                <div class="card-body" id="lab-add-area"><p style="color:var(--text-secondary)">Yükleniyor...</p></div>
            </div>` : ''}
            <div class="card">
                <div class="card-header"><h2><i class="fa-solid fa-vials"></i> ${isPatient ? 'Lab Sonuçlarım' : 'Son Eklenen Sonuçlar'}</h2></div>
                <div class="card-body" id="lab-list"><p style="color:var(--text-secondary)">Yükleniyor...</p></div>
            </div>`;

        const statusLabel = {NORMAL:'Normal',ABNORMAL:'Anormal',CRITICAL:'Kritik',PENDING:'Bekliyor'};
        const statusColor = {NORMAL:'var(--success)',ABNORMAL:'var(--warning)',CRITICAL:'var(--danger)',PENDING:'var(--info)'};

        try {
            // Doktor: Hasta seçip lab sonucu ekleme
            if (isDoctor) {
                const appts = await Api.getMyDoctorAppointments().catch(() => []);
                const patients = appts.filter(a => a.status === 'APPROVED' || a.status === 'COMPLETED');
                const area = document.getElementById('lab-add-area');
                if (!patients.length) {
                    area.innerHTML = '<div class="empty-state"><i class="fa-solid fa-calendar-xmark"></i><h3>Randevusu olan hasta yok</h3><p>Lab sonucu eklemek için önce bir randevuyu onaylamalısınız.</p></div>';
                } else {
                    // Benzersiz hastaları al
                    const uniquePatients = [];
                    const seen = new Set();
                    patients.forEach(a => { if (!seen.has(a.patientId)) { seen.add(a.patientId); uniquePatients.push({id: a.patientId, name: a.patientName}); }});

                    area.innerHTML = `<form id="lab-form">
                        <div class="form-row">
                            <div class="form-group"><label>Hasta</label><div class="form-input-wrapper"><i class="fa-solid fa-user"></i>
                                <select id="lab-patient" class="form-select">${uniquePatients.map(p => `<option value="${p.id}">${p.name}</option>`).join('')}</select>
                            </div></div>
                            <div class="form-group"><label>Test Adı *</label><div class="form-input-wrapper"><i class="fa-solid fa-flask"></i>
                                <input type="text" id="lab-name" class="form-input" placeholder="Ör: Tam Kan Sayımı" required>
                            </div></div>
                        </div>
                        <div class="form-row">
                            <div class="form-group"><label>Kategori</label><div class="form-input-wrapper"><i class="fa-solid fa-tags"></i>
                                <select id="lab-category" class="form-select">
                                    <option value="KAN">Kan Tahlili</option>
                                    <option value="IDRAR">İdrar Tahlili</option>
                                    <option value="GORUNTULEME">Görüntüleme</option>
                                    <option value="BIYOKIMYA">Biyokimya</option>
                                    <option value="HORMON">Hormon</option>
                                    <option value="DIGER">Diğer</option>
                                </select>
                            </div></div>
                            <div class="form-group"><label>Sonuç Durumu</label><div class="form-input-wrapper"><i class="fa-solid fa-flag"></i>
                                <select id="lab-status" class="form-select">
                                    <option value="NORMAL">Normal</option>
                                    <option value="ABNORMAL">Anormal</option>
                                    <option value="CRITICAL">Kritik</option>
                                    <option value="PENDING">Bekliyor</option>
                                </select>
                            </div></div>
                        </div>
                        <div class="form-row">
                            <div class="form-group"><label>Sonuç Değeri</label><div class="form-input-wrapper"><i class="fa-solid fa-chart-simple"></i>
                                <input type="text" id="lab-value" class="form-input" placeholder="Ör: 12.5">
                            </div></div>
                            <div class="form-group"><label>Birim</label><div class="form-input-wrapper"><i class="fa-solid fa-ruler"></i>
                                <input type="text" id="lab-unit" class="form-input" placeholder="Ör: g/dL">
                            </div></div>
                            <div class="form-group"><label>Referans Aralığı</label><div class="form-input-wrapper"><i class="fa-solid fa-arrows-left-right"></i>
                                <input type="text" id="lab-ref" class="form-input" placeholder="Ör: 12-16 g/dL">
                            </div></div>
                        </div>
                        <div class="form-row">
                            <div class="form-group"><label>Test Tarihi</label><div class="form-input-wrapper"><i class="fa-solid fa-calendar"></i>
                                <input type="date" id="lab-date" class="form-input">
                            </div></div>
                            <div class="form-group"><label>Laboratuvar</label><div class="form-input-wrapper"><i class="fa-solid fa-hospital"></i>
                                <input type="text" id="lab-laboratory" class="form-input" placeholder="Lab adı">
                            </div></div>
                        </div>
                        <div class="form-group"><label>Sonuç Detayı / Açıklama</label><div class="form-input-wrapper"><i class="fa-solid fa-pen"></i>
                            <input type="text" id="lab-results" class="form-input" placeholder="Detaylı sonuç açıklaması">
                        </div></div>
                        <div class="form-group"><label>Notlar</label><div class="form-input-wrapper"><i class="fa-solid fa-sticky-note"></i>
                            <input type="text" id="lab-notes" class="form-input" placeholder="Doktor notu">
                        </div></div>
                        <button type="submit" class="btn btn-success"><i class="fa-solid fa-plus"></i> Lab Sonucu Ekle</button>
                    </form>`;

                    document.getElementById('lab-form').addEventListener('submit', async (e) => {
                        e.preventDefault();
                        const patientId = parseInt(document.getElementById('lab-patient').value);
                        try {
                            await Api.addLabResult(patientId, {
                                testName: document.getElementById('lab-name').value,
                                testCategory: document.getElementById('lab-category').value,
                                status: document.getElementById('lab-status').value,
                                resultValue: document.getElementById('lab-value').value,
                                unit: document.getElementById('lab-unit').value,
                                referenceRange: document.getElementById('lab-ref').value,
                                testDate: document.getElementById('lab-date').value || null,
                                laboratory: document.getElementById('lab-laboratory').value,
                                results: document.getElementById('lab-results').value,
                                notes: document.getElementById('lab-notes').value
                            });
                            App.toast('Lab sonucu eklendi!', 'success');
                            App.navigate('lab-results');
                        } catch(err) { App.toast(err.message, 'error'); }
                    });
                }
            }

            // Liste: Hasta kendi sonuçlarını, doktor son eklediği hasta sonuçlarını görür
            const labs = isPatient
                ? await Api.getMyLabResults().catch(() => [])
                : []; // Doktor için tüm liste yok ama ekledikten sonra sayfayı yeniler

            const listEl = document.getElementById('lab-list');
            if (!labs.length && isPatient) {
                listEl.innerHTML = '<div class="empty-state"><i class="fa-solid fa-flask"></i><h3>Henüz lab sonucu yok</h3><p>Doktorunuz tahlil sonuçlarınızı buraya ekleyecektir.</p></div>';
            } else if (labs.length) {
                listEl.innerHTML = labs.map(l => {
                    const sc = statusColor[l.status] || 'var(--text-muted)';
                    return `<div style="background:var(--bg-dark);border-radius:var(--radius-md);padding:1.25rem;margin-bottom:1rem;border-left:4px solid ${sc}">
                        <div style="display:flex;justify-content:space-between;align-items:flex-start;flex-wrap:wrap;gap:0.75rem">
                            <div>
                                <div style="display:flex;align-items:center;gap:0.5rem;margin-bottom:0.5rem">
                                    <h3 style="font-size:1rem">${l.testName}</h3>
                                    <span style="background:${sc}22;color:${sc};padding:0.15rem 0.5rem;border-radius:999px;font-size:0.7rem;font-weight:600">${statusLabel[l.status] || l.status}</span>
                                    ${l.testCategory ? `<span style="background:var(--border);padding:0.15rem 0.5rem;border-radius:999px;font-size:0.7rem;color:var(--text-muted)">${l.testCategory}</span>` : ''}
                                </div>
                                ${l.resultValue ? `<p style="font-size:0.95rem"><strong>${l.resultValue}</strong> ${l.unit || ''} ${l.referenceRange ? `<span style="color:var(--text-muted);font-size:0.8rem">(Ref: ${l.referenceRange})</span>` : ''}</p>` : ''}
                                ${l.results ? `<p style="font-size:0.85rem;color:var(--text-secondary);margin-top:0.25rem">${l.results}</p>` : ''}
                                ${l.notes ? `<p style="font-size:0.85rem;color:var(--accent-light);margin-top:0.25rem"><i class="fa-solid fa-sticky-note"></i> ${l.notes}</p>` : ''}
                            </div>
                            <div style="text-align:right;min-width:120px">
                                ${l.doctorName ? `<p style="font-size:0.85rem;color:var(--success)"><i class="fa-solid fa-user-doctor"></i> ${l.doctorName}</p>` : ''}
                                ${l.laboratory ? `<p style="font-size:0.8rem;color:var(--text-muted)"><i class="fa-solid fa-hospital"></i> ${l.laboratory}</p>` : ''}
                                <p style="font-size:0.75rem;color:var(--text-muted);margin-top:0.25rem">${l.testDate ? new Date(l.testDate).toLocaleDateString('tr-TR') : (l.createdAt ? new Date(l.createdAt).toLocaleDateString('tr-TR') : '—')}</p>
                            </div>
                        </div>
                    </div>`;
                }).join('');
            } else if (isDoctor) {
                listEl.innerHTML = '<div class="empty-state"><i class="fa-solid fa-flask"></i><h3>Lab sonuçları yukarıdan eklenebilir</h3><p>Hastayı seçerek tahlil sonuçlarını girin.</p></div>';
            }
        } catch(err) { App.toast(err.message, 'error'); }
    },

    // =================== FAZ 5: DOKTOR REÇETE GEÇMİŞİ ===================
    async doctorPrescriptionsHistoryPage(user) {
        const content = document.getElementById('page-content');
        content.innerHTML = '<div class="empty-state"><i class="fa-solid fa-spinner fa-spin"></i><h3>Yükleniyor...</h3></div>';

        const statusLabel = {ACTIVE:'Aktif',USED:'Kullanıldı',EXPIRED:'Süresi Doldu',CANCELLED:'İptal'};
        const statusColor = {ACTIVE:'var(--success)',USED:'var(--info)',EXPIRED:'var(--warning)',CANCELLED:'var(--danger)'};

        try {
            const prescs = await Api.getDoctorPrescriptions().catch(() => []);

            if (!prescs.length) {
                content.innerHTML = '<div class="card"><div class="card-header"><h2><i class="fa-solid fa-file-prescription"></i> Yazdığım Reçeteler</h2></div><div class="card-body"><div class="empty-state"><i class="fa-solid fa-prescription-bottle"></i><h3>Henüz reçete yazmadınız</h3><p>Randevular sayfasından reçete yazabilirsiniz.</p></div></div></div>';
                return;
            }

            const stats = {
                total: prescs.length,
                active: prescs.filter(p => p.status === 'ACTIVE').length,
                used: prescs.filter(p => p.status === 'USED').length,
                expired: prescs.filter(p => p.status === 'EXPIRED').length
            };

            content.innerHTML = `
                <div class="stats-grid" style="margin-bottom:1.5rem">
                    <div class="stat-card primary"><div class="stat-icon primary"><i class="fa-solid fa-prescription"></i></div><div class="stat-info"><h3>${stats.total}</h3><p>Toplam Reçete</p></div></div>
                    <div class="stat-card success"><div class="stat-icon success"><i class="fa-solid fa-check-circle"></i></div><div class="stat-info"><h3>${stats.active}</h3><p>Aktif</p></div></div>
                    <div class="stat-card info"><div class="stat-icon info"><i class="fa-solid fa-pills"></i></div><div class="stat-info"><h3>${stats.used}</h3><p>Kullanıldı</p></div></div>
                    <div class="stat-card warning"><div class="stat-icon warning"><i class="fa-solid fa-clock"></i></div><div class="stat-info"><h3>${stats.expired}</h3><p>Süresi Doldu</p></div></div>
                </div>
                <div class="card">
                    <div class="card-header" style="justify-content:space-between">
                        <h2><i class="fa-solid fa-file-prescription" style="color:var(--primary)"></i> Yazdığım Reçeteler</h2>
                        <select id="presc-filter" class="form-select" style="width:auto;padding:0.25rem 2rem 0.25rem 0.75rem">
                            <option value="ALL">Tümü</option>
                            <option value="ACTIVE">Aktif</option>
                            <option value="USED">Kullanıldı</option>
                            <option value="EXPIRED">Süresi Doldu</option>
                            <option value="CANCELLED">İptal</option>
                        </select>
                    </div>
                    <div class="card-body" id="presc-history-list"></div>
                </div>`;

            const renderList = (list) => {
                const el = document.getElementById('presc-history-list');
                if (!list.length) {
                    el.innerHTML = '<div class="empty-state"><i class="fa-solid fa-filter"></i><h3>Bu filtreye uygun reçete yok</h3></div>';
                    return;
                }
                el.innerHTML = list.map(p => {
                    const sc = statusColor[p.status] || 'var(--text-muted)';
                    return `<div style="background:var(--bg-dark);border-radius:var(--radius-md);padding:1.25rem;margin-bottom:1rem;border-left:4px solid ${sc}">
                        <div style="display:flex;justify-content:space-between;align-items:flex-start;flex-wrap:wrap;gap:1rem">
                            <div style="flex:1">
                                <div style="display:flex;align-items:center;gap:0.5rem;margin-bottom:0.5rem">
                                    <strong>${p.patientName}</strong>
                                    <span style="background:${sc}22;color:${sc};padding:0.15rem 0.5rem;border-radius:999px;font-size:0.7rem;font-weight:600">${statusLabel[p.status] || p.status}</span>
                                </div>
                                ${p.diagnosis ? `<p style="font-size:0.9rem;color:var(--accent-light);margin-bottom:0.25rem"><i class="fa-solid fa-stethoscope"></i> Tanı: ${p.diagnosis}</p>` : ''}
                                <p style="font-size:0.9rem"><i class="fa-solid fa-pills" style="color:var(--primary-light)"></i> ${p.medicines}</p>
                                <p style="font-size:0.85rem;color:var(--text-secondary);margin-top:0.25rem"><i class="fa-solid fa-info-circle"></i> ${p.instructions}</p>
                                ${p.dosages ? `<p style="font-size:0.8rem;color:var(--text-muted);margin-top:0.25rem">Dozaj: ${p.dosages}</p>` : ''}
                                ${p.frequencies ? `<p style="font-size:0.8rem;color:var(--text-muted)">Sıklık: ${p.frequencies}</p>` : ''}
                                ${p.durationDays ? `<p style="font-size:0.8rem;color:var(--text-muted)">Süre: ${p.durationDays} gün</p>` : ''}
                                ${p.warnings ? `<p style="font-size:0.8rem;color:var(--danger);margin-top:0.25rem"><i class="fa-solid fa-triangle-exclamation"></i> ${p.warnings}</p>` : ''}
                            </div>
                            <div style="display:flex;flex-direction:column;gap:0.5rem;align-items:flex-end">
                                <span style="font-size:0.75rem;color:var(--text-muted)">${p.createdAt ? new Date(p.createdAt).toLocaleDateString('tr-TR') : '—'}</span>
                                <select class="presc-status-change" data-id="${p.id}" style="background:var(--bg-card);color:var(--text-primary);border:1px solid var(--border);border-radius:var(--radius-sm);padding:0.25rem 0.4rem;font-size:0.75rem;cursor:pointer">
                                    <option value="ACTIVE" ${p.status==='ACTIVE'?'selected':''}>Aktif</option>
                                    <option value="USED" ${p.status==='USED'?'selected':''}>Kullanıldı</option>
                                    <option value="EXPIRED" ${p.status==='EXPIRED'?'selected':''}>Süresi Doldu</option>
                                    <option value="CANCELLED" ${p.status==='CANCELLED'?'selected':''}>İptal</option>
                                </select>
                            </div>
                        </div>
                    </div>`;
                }).join('');

                el.querySelectorAll('.presc-status-change').forEach(sel => {
                    sel.addEventListener('change', async () => {
                        try {
                            await Api.updatePrescriptionStatus(sel.dataset.id, sel.value);
                            App.toast('Reçete durumu güncellendi', 'success');
                        } catch(e) { App.toast(e.message, 'error'); }
                    });
                });
            };

            renderList(prescs);

            document.getElementById('presc-filter').addEventListener('change', (e) => {
                const val = e.target.value;
                renderList(val === 'ALL' ? prescs : prescs.filter(p => p.status === val));
            });
        } catch(err) { App.toast(err.message, 'error'); }
    },

    // =================== MESAJLAR — YENİ KONUŞMA BAŞLATMA (Enhancement) ===================
    async showNewMessageModal() {
        try {
            let users;
            if (App.user.role === 'DOCTOR') {
                // Doktor: Tüm kullanıcıları getir, hastalarını göster
                const allUsers = await Api.getAllUsers().catch(() => []);
                users = allUsers.filter(u => u.role === 'PATIENT');
            } else {
                // Hasta: Doktorları göster
                users = await Api.getDoctors().catch(() => []);
            }
            if (!users.length) { App.toast('Mesaj gönderilecek kullanıcı bulunamadı', 'error'); return; }
            const labelPrefix = App.user.role === 'DOCTOR' ? '' : 'Dr. ';
            const body = `<form id="new-msg-form">
                <div class="form-group"><label>Alıcı</label><div class="form-input-wrapper"><i class="fa-solid fa-user"></i>
                    <select id="msg-receiver" class="form-select">${users.map(u => `<option value="${u.id}">${labelPrefix}${u.fullName}</option>`).join('')}</select>
                </div></div>
                <div class="form-group"><label>Mesaj</label><div class="form-input-wrapper"><i class="fa-solid fa-pen"></i>
                    <input type="text" id="msg-content" class="form-input" placeholder="Mesajınızı yazın..." required>
                </div></div>
            </form>`;
            App.showModal('Yeni Mesaj Gönder', body, `<button class="btn btn-outline" id="modal-cancel-btn">İptal</button><button class="btn btn-primary" id="modal-save-btn"><i class="fa-solid fa-paper-plane"></i> Gönder</button>`);
            document.getElementById('modal-cancel-btn').addEventListener('click', () => App.closeModal());
            document.getElementById('modal-save-btn').addEventListener('click', async () => {
                const receiverId = parseInt(document.getElementById('msg-receiver').value);
                const content = document.getElementById('msg-content').value.trim();
                if (!content) { App.toast('Mesaj boş olamaz', 'error'); return; }
                try {
                    await Api.sendMessage(receiverId, content);
                    App.toast('Mesaj gönderildi!', 'success');
                    App.closeModal();
                    App.navigate('messages');
                } catch(e) { App.toast(e.message, 'error'); }
            });
        } catch(e) { App.toast(e.message, 'error'); }
    },

    // =================== DOKTOR DEĞERLENDİRME (Hasta tarafından) ===================
    async showDoctorReviewModal(doctorProfileId, doctorName) {
        const body = `<form id="review-form">
            <div style="text-align:center;margin-bottom:1rem">
                <div class="profile-avatar" style="width:56px;height:56px;font-size:1.5rem;margin:0 auto 0.75rem"><i class="fa-solid fa-user-doctor"></i></div>
                <h3>${doctorName}</h3>
                <p style="color:var(--text-secondary);font-size:0.9rem">Bu doktoru değerlendirin</p>
            </div>
            <div class="form-group" style="text-align:center">
                <label>Puanınız</label>
                <div id="star-rating" style="font-size:2rem;cursor:pointer;display:flex;justify-content:center;gap:0.25rem;margin-top:0.5rem">
                    ${[1,2,3,4,5].map(i => `<span class="star-btn" data-val="${i}" style="color:var(--text-muted);transition:color 0.2s">★</span>`).join('')}
                </div>
                <input type="hidden" id="review-rating" value="5">
            </div>
            <div class="form-group"><label>Yorumunuz</label><div class="form-input-wrapper"><i class="fa-solid fa-pen"></i>
                <input type="text" id="review-comment" class="form-input" placeholder="Deneyiminizi paylaşın...">
            </div></div>
        </form>`;
        App.showModal('Doktor Değerlendirmesi', body, `<button class="btn btn-outline" id="modal-cancel-btn">İptal</button><button class="btn btn-success" id="modal-save-btn"><i class="fa-solid fa-star"></i> Değerlendir</button>`);

        // Star rating interaktif
        let selectedRating = 5;
        document.querySelectorAll('.star-btn').forEach(star => {
            star.addEventListener('mouseover', () => {
                const val = parseInt(star.dataset.val);
                document.querySelectorAll('.star-btn').forEach(s => {
                    s.style.color = parseInt(s.dataset.val) <= val ? 'var(--warning)' : 'var(--text-muted)';
                });
            });
            star.addEventListener('click', () => {
                selectedRating = parseInt(star.dataset.val);
                document.getElementById('review-rating').value = selectedRating;
                document.querySelectorAll('.star-btn').forEach(s => {
                    s.style.color = parseInt(s.dataset.val) <= selectedRating ? 'var(--warning)' : 'var(--text-muted)';
                });
            });
        });
        // Default 5 yıldız göster
        document.querySelectorAll('.star-btn').forEach(s => { s.style.color = 'var(--warning)'; });
        document.getElementById('star-rating').addEventListener('mouseleave', () => {
            document.querySelectorAll('.star-btn').forEach(s => {
                s.style.color = parseInt(s.dataset.val) <= selectedRating ? 'var(--warning)' : 'var(--text-muted)';
            });
        });

        document.getElementById('modal-cancel-btn').addEventListener('click', () => App.closeModal());
        document.getElementById('modal-save-btn').addEventListener('click', async () => {
            try {
                await Api.addDoctorReview({
                    doctorProfileId: parseInt(doctorProfileId),
                    rating: selectedRating,
                    comment: document.getElementById('review-comment').value
                });
                App.toast('Değerlendirmeniz kaydedildi! ⭐', 'success');
                App.closeModal();
            } catch(e) { App.toast(e.message, 'error'); }
        });
    }
};
