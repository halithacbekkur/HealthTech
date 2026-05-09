/* ============================================
   HealthTech App - Ana Uygulama Modülü
   Router, navigation ve global utilities
   ============================================ */

const App = {
    user: null,
    currentPage: 'dashboard',

    async init() {
        // Token var mı kontrol et
        if (!Api.token) { this.showAuth(); return; }

        try {
            this.user = await Api.getMe();
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
                { id: 'medical-record', icon: 'fa-notes-medical', label: 'Tıbbi Kaydım' },
                { id: 'profile', icon: 'fa-user', label: 'Profilim' },
            ];
        } else if (this.user.role === 'DOCTOR') {
            items = [
                { id: 'dashboard', icon: 'fa-house', label: 'Dashboard' },
                { id: 'prescriptions', icon: 'fa-prescription', label: 'Reçete Yaz' },
                { id: 'profile', icon: 'fa-user', label: 'Profilim' },
            ];
        } else if (this.user.role === 'ADMIN') {
            items = [
                { id: 'dashboard', icon: 'fa-chart-line', label: 'Dashboard' },
                { id: 'profile', icon: 'fa-user', label: 'Profilim' },
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
    },

    // ======= NAVIGATION / ROUTER =======
    async navigate(page) {
        this.currentPage = page;

        // Update active nav
        document.querySelectorAll('.nav-link[data-page]').forEach(l => l.classList.remove('active'));
        const activeLink = document.querySelector(`.nav-link[data-page="${page}"]`);
        if (activeLink) activeLink.classList.add('active');

        // Page title
        const titles = { 'dashboard': 'Dashboard', 'medical-record': 'Tıbbi Kaydım', 'prescriptions': 'Reçete Yaz', 'profile': 'Profilim' };
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
