/* ============================================
   HealthTech App v4 - Ana Uygulama Modülü
   Router, navigation ve global utilities
   Faz 12: Tema desteği, slot seçimi, GPS, Jitsi
   ============================================ */

const App = {
    user: null,
    currentPage: 'dashboard',

    async init() {
        // Token var mı kontrol et
        if (!Api.token) { this.showAuth(); return; }

        try {
            // Kullanıcı bilgilerini yeni /profile/me endpoint'inden al
            this.user = await Api.getFullProfile();
            localStorage.setItem('ht_user', JSON.stringify(this.user));
            this.showApp();
            this.setupSidebar();
            this.setupTopbar();
            this.navigate('dashboard');
        } catch (err) {
            // Token geçersiz
            Api.clearToken();
            this.showAuth();
        }
    },

    // ======= AUTH / APP TOGGLE =======
    showAuth() {
        document.getElementById('loading-screen').style.display = 'none';
        document.getElementById('app').style.display = 'none';
        document.getElementById('auth-container').style.display = 'flex';
        Auth.renderLogin();
    },

    showApp() {
        document.getElementById('loading-screen').style.display = 'none';
        document.getElementById('auth-container').style.display = 'none';
        document.getElementById('app').style.display = 'flex';
    },

    // ======= SIDEBAR SETUP =======
    setupSidebar() {
        // User info
        document.getElementById('user-name').textContent = this.user.fullName;
        document.getElementById('user-role-badge').textContent = Pages.roleLabel(this.user.role);

        // Navigation menu based on role
        const menu = document.getElementById('nav-menu');
        let items = [];

        if (this.user.role === 'PATIENT') {
            items = [
                { id: 'dashboard', icon: 'fa-house', label: 'Dashboard' },
                { id: 'doctor-search', icon: 'fa-search', label: 'Doktor Ara' },
                { id: 'medical-record', icon: 'fa-notes-medical', label: 'Tıbbi Kaydım' },
                { id: 'lab-results', icon: 'fa-flask', label: 'Lab Sonuçları' },
                { id: 'video-calls', icon: 'fa-video', label: 'Video Görüşmeler' },
                { id: 'emergency-sos', icon: 'fa-triangle-exclamation', label: '🚨 Acil Durum' },
                { id: 'notifications', icon: 'fa-bell', label: 'Bildirimler' },
                { id: 'messages', icon: 'fa-comment-dots', label: 'Mesajlar' },
                { id: 'profile', icon: 'fa-user', label: 'Profilim' },
                { id: 'address', icon: 'fa-location-dot', label: 'Adresim' },
                { id: 'emergency', icon: 'fa-phone-volume', label: 'Acil Kişi' },
                { id: 'insurance', icon: 'fa-shield-halved', label: 'Sigorta' },
                { id: 'settings', icon: 'fa-gear', label: 'Ayarlar' },
            ];
        } else if (this.user.role === 'DOCTOR') {
            items = [
                { id: 'dashboard', icon: 'fa-house', label: 'Dashboard' },
                { id: 'doctor-profile', icon: 'fa-user-doctor', label: 'Doktor Profilim' },
                { id: 'doctor-schedule', icon: 'fa-calendar-days', label: 'Çalışma Takvimim' },
                { id: 'prescriptions', icon: 'fa-prescription', label: 'Reçete Yaz' },
                { id: 'doctor-prescriptions', icon: 'fa-file-prescription', label: 'Reçete Geçmişim' },
                { id: 'video-calls', icon: 'fa-video', label: 'Video Görüşmeler' },
                { id: 'emergency-sos', icon: 'fa-triangle-exclamation', label: '🚨 Acil Talepler' },
                { id: 'notifications', icon: 'fa-bell', label: 'Bildirimler' },
                { id: 'messages', icon: 'fa-comment-dots', label: 'Mesajlar' },
                { id: 'profile', icon: 'fa-user', label: 'Profilim' },
                { id: 'settings', icon: 'fa-gear', label: 'Ayarlar' },
            ];
        } else if (this.user.role === 'ADMIN') {
            items = [
                { id: 'dashboard', icon: 'fa-chart-line', label: 'Dashboard' },
                { id: 'admin-panel', icon: 'fa-shield-halved', label: 'Admin Panel' },
                { id: 'admin-users', icon: 'fa-users-gear', label: 'Kullanıcı Yönetimi' },
                { id: 'doctor-approvals', icon: 'fa-user-check', label: 'Doktor Onayları' },
                { id: 'audit-logs', icon: 'fa-clipboard-list', label: 'İşlem Logları' },
                { id: 'emergency-sos', icon: 'fa-triangle-exclamation', label: '🚨 Acil Talepler' },
                { id: 'video-calls', icon: 'fa-video', label: 'Video Görüşmeler' },
                { id: 'notifications', icon: 'fa-bell', label: 'Bildirimler' },
                { id: 'messages', icon: 'fa-comment-dots', label: 'Mesajlar' },
                { id: 'profile', icon: 'fa-user', label: 'Profilim' },
                { id: 'settings', icon: 'fa-gear', label: 'Ayarlar' },
            ];
        }

        menu.innerHTML = items.map(i =>
            `<li><button class="nav-link" data-page="${i.id}"><i class="fa-solid ${i.icon}"></i><span>${i.label}</span></button></li>`
        ).join('');

        menu.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', () => {
                this.navigate(link.dataset.page);
                // Mobile: close sidebar
                document.getElementById('sidebar').classList.remove('open');
                const overlay = document.getElementById('sidebar-overlay');
                if (overlay) overlay.classList.remove('show');
            });
        });

        // Sidebar toggle (collapse)
        document.getElementById('sidebar-toggle').addEventListener('click', () => {
            document.getElementById('sidebar').classList.toggle('collapsed');
        });

        // Logout
        document.getElementById('btn-logout').addEventListener('click', () => {
            Api.clearToken();
            this.user = null;
            this.showAuth();
            this.toast('Çıkış yapıldı', 'info');
        });

        // Mobile menu
        const overlay = document.getElementById('sidebar-overlay');
        document.getElementById('mobile-menu-btn').addEventListener('click', () => {
            document.getElementById('sidebar').classList.toggle('open');
            if (overlay) overlay.classList.toggle('show');
        });
        if (overlay) {
            overlay.addEventListener('click', () => {
                document.getElementById('sidebar').classList.remove('open');
                overlay.classList.remove('show');
            });
        }
    },

    // ======= TOPBAR =======
    setupTopbar() {
        const updateTime = () => {
            const now = new Date();
            document.getElementById('topbar-time').textContent = now.toLocaleTimeString('tr-TR', { hour: '2-digit', minute: '2-digit' }) + ' · ' + now.toLocaleDateString('tr-TR', { day: 'numeric', month: 'short' });
        };
        updateTime();
        setInterval(updateTime, 30000);

        // Bildirim çanı tıklama
        const bell = document.getElementById('notif-bell');
        if (bell) bell.addEventListener('click', () => this.navigate('notifications'));

        // Bildirim sayısı polling (30sn)
        this.updateNotifBadge();
        setInterval(() => this.updateNotifBadge(), 30000);

        // ======= TEMA TOGGLE (Faz 12) =======
        this.initTheme();
        const themeBtn = document.getElementById('theme-toggle');
        if (themeBtn) {
            themeBtn.addEventListener('click', () => this.toggleTheme());
        }
    },

    // ======= THEME MANAGEMENT (Faz 12) =======
    initTheme() {
        const saved = localStorage.getItem('ht_theme') || 'dark';
        document.documentElement.setAttribute('data-theme', saved);
        this.updateThemeIcon(saved);
    },

    toggleTheme() {
        const current = document.documentElement.getAttribute('data-theme') || 'dark';
        const next = current === 'dark' ? 'light' : 'dark';
        document.documentElement.setAttribute('data-theme', next);
        localStorage.setItem('ht_theme', next);
        this.updateThemeIcon(next);
    },

    updateThemeIcon(theme) {
        const btn = document.getElementById('theme-toggle');
        if (!btn) return;
        const icon = btn.querySelector('i');
        if (icon) {
            icon.className = theme === 'dark' ? 'fa-solid fa-moon' : 'fa-solid fa-sun';
        }
    },

    async updateNotifBadge() {
        try {
            const data = await Api.getUnreadCount();
            const badge = document.getElementById('notif-badge');
            if (badge) {
                if (data.count > 0) { badge.textContent = data.count > 9 ? '9+' : data.count; badge.style.display = 'flex'; }
                else { badge.style.display = 'none'; }
            }
        } catch(e) { /* silent */ }
    },

    // ======= NAVIGATION / ROUTER =======
    async navigate(page) {
        // Video görüşme bildirim linki yakalama (/video-call/roomId)
        if (page && page.startsWith('/video-call/')) {
            const roomId = page.replace('/video-call/', '');
            this.currentPage = 'video-calls';
            // Önce video-calls sayfasını yükle, sonra otomatik katıl
            await this.navigate('video-calls');
            setTimeout(async () => {
                try {
                    await Api.joinVideoCall(roomId);
                    Pages.openVideoCall(roomId);
                } catch(e) { this.toast('Görüşme bulunamadı veya sona ermiş', 'error'); }
            }, 500);
            return;
        }
        this.currentPage = page;

        // Update active nav
        document.querySelectorAll('.nav-link[data-page]').forEach(l => l.classList.remove('active'));
        const activeLink = document.querySelector(`.nav-link[data-page="${page}"]`);
        if (activeLink) activeLink.classList.add('active');

        // Page title
        const titles = {
            'dashboard': 'Dashboard',
            'doctor-search': 'Doktor Ara',
            'doctor-profile': 'Doktor Profilim',
            'doctor-schedule': 'Çalışma Takvimim',
            'doctor-approvals': 'Doktor Onayları',
            'notifications': 'Bildirimler',
            'messages': 'Mesajlar',
            'medical-record': 'Tıbbi Kaydım',
            'lab-results': 'Lab Sonuçları',
            'prescriptions': 'Reçete Yaz',
            'doctor-prescriptions': 'Yazdığım Reçeteler',
            'profile': 'Profilim',
            'address': 'Adresim',
            'emergency': 'Acil Durum Kişilerim',
            'insurance': 'Sigorta Bilgilerim',
            'settings': 'Ayarlar',
            'video-calls': 'Video Görüşmeler',
            'emergency-sos': 'Acil Durum Talebi',
            'admin-panel': 'Admin Panel — Sistem Raporu',
            'admin-users': 'Kullanıcı Yönetimi',
            'audit-logs': 'İşlem Logları',
        };
        document.getElementById('page-title').textContent = titles[page] || 'Dashboard';

        // Reset content with fade
        const content = document.getElementById('page-content');
        content.style.opacity = '0';
        content.style.transform = 'translateY(10px)';

        setTimeout(async () => {
            // Render page
            switch (page) {
                case 'dashboard':
                    if (this.user.role === 'PATIENT') await Pages.patientDashboard(this.user);
                    else if (this.user.role === 'DOCTOR') await Pages.doctorDashboard(this.user);
                    else await Pages.adminDashboard();
                    break;
                case 'medical-record':
                    await Pages.medicalRecordPage();
                    break;
                case 'prescriptions':
                    await Pages.doctorPrescriptionsPage(this.user);
                    break;
                case 'profile':
                    await Pages.profilePage(this.user);
                    break;
                case 'address':
                    await Pages.addressPage();
                    break;
                case 'emergency':
                    await Pages.emergencyContactsPage();
                    break;
                case 'insurance':
                    await Pages.insurancePage();
                    break;
                case 'settings':
                    await Pages.settingsPage(this.user);
                    break;
                case 'doctor-search':
                    await Pages.doctorSearchPage();
                    break;
                case 'doctor-profile':
                    await Pages.doctorProfileEditPage();
                    break;
                case 'doctor-schedule':
                    await Pages.doctorSchedulePage();
                    break;
                case 'doctor-approvals':
                    await Pages.doctorApprovalsPage();
                    break;
                case 'notifications':
                    await Pages.notificationsPage();
                    break;
                case 'messages':
                    await Pages.messagesPage();
                    break;
                case 'video-calls':
                    await Pages.videoCallsPage(this.user);
                    break;
                case 'emergency-sos':
                    await Pages.emergencySOSPage(this.user);
                    break;
                case 'lab-results':
                    await Pages.labResultsPage(this.user);
                    break;
                case 'doctor-prescriptions':
                    await Pages.doctorPrescriptionsHistoryPage(this.user);
                    break;
                case 'admin-panel':
                    await Pages.adminPanelPage();
                    break;
                case 'admin-users':
                    await Pages.adminUsersPage();
                    break;
                case 'audit-logs':
                    await Pages.auditLogsPage();
                    break;
            }
            content.style.opacity = '1';
            content.style.transform = 'translateY(0)';
        }, 150);
    },

    // ======= TOAST =======
    toast(msg, type = 'info') {
        const container = document.getElementById('toast-container');
        const icons = { success: 'fa-circle-check', error: 'fa-circle-xmark', info: 'fa-circle-info' };
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        toast.innerHTML = `<i class="fa-solid ${icons[type] || icons.info}"></i><span>${msg}</span>`;
        container.appendChild(toast);
        setTimeout(() => { toast.style.opacity = '0'; setTimeout(() => toast.remove(), 300); }, 3500);
    },

    // ======= MODAL =======
    showModal(title, bodyHtml, footerHtml) {
        document.getElementById('modal-title').textContent = title;
        document.getElementById('modal-body').innerHTML = bodyHtml;
        document.getElementById('modal-footer').innerHTML = footerHtml || '';
        document.getElementById('modal-overlay').style.display = 'flex';
        document.getElementById('modal-close').onclick = () => this.closeModal();
        document.getElementById('modal-overlay').onclick = (e) => { if (e.target.id === 'modal-overlay') this.closeModal(); };
    },
    closeModal() { document.getElementById('modal-overlay').style.display = 'none'; },
};

// ======= APP START =======
document.addEventListener('DOMContentLoaded', () => {
    setTimeout(() => App.init(), 600);
});
