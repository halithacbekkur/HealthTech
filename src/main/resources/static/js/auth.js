/* ============================================
   HealthTech Auth Module v2
   Login/Register form yönetimi + validasyon
   ============================================ */

const Auth = {

    renderLogin() {
        document.getElementById('auth-subtitle').textContent = 'Hesabınıza giriş yapın';
        document.getElementById('auth-form-container').innerHTML = `
            <form id="login-form" novalidate autocomplete="on">
                <div class="form-group">
                    <label for="login-email">E-posta</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-envelope"></i>
                        <input type="email" id="login-email" class="form-input"
                               placeholder="ornek@email.com" autocomplete="email" required>
                    </div>
                    <span class="field-error" id="err-login-email"></span>
                </div>
                <div class="form-group">
                    <label for="login-password">Şifre</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-lock"></i>
                        <input type="password" id="login-password" class="form-input"
                               placeholder="••••••••" autocomplete="current-password" required>
                        <button type="button" class="pwd-toggle" id="toggle-login-pwd" tabindex="-1">
                            <i class="fa-solid fa-eye"></i>
                        </button>
                    </div>
                    <span class="field-error" id="err-login-pwd"></span>
                </div>
                <button type="submit" class="btn btn-primary btn-block" id="login-btn">
                    <i class="fa-solid fa-right-to-bracket"></i> Giriş Yap
                </button>
                <p style="text-align:center;margin-top:1.25rem;color:var(--text-secondary);font-size:0.9rem;">
                    Hesabınız yok mu? <button type="button" class="text-btn" id="goto-register">Kayıt Ol</button>
                </p>
            </form>
        `;

        // Şifre göster/gizle
        document.getElementById('toggle-login-pwd').addEventListener('click', () => {
            const inp = document.getElementById('login-password');
            const ico = document.querySelector('#toggle-login-pwd i');
            inp.type = inp.type === 'password' ? 'text' : 'password';
            ico.className = inp.type === 'password' ? 'fa-solid fa-eye' : 'fa-solid fa-eye-slash';
        });

        document.getElementById('login-form').addEventListener('submit', e => this.handleLogin(e));
        document.getElementById('goto-register').addEventListener('click', () => this.renderRegister());

        // Enter tuşu
        document.getElementById('login-password').addEventListener('keydown', e => {
            if (e.key === 'Enter') document.getElementById('login-form').requestSubmit();
        });
    },

    renderRegister() {
        document.getElementById('auth-subtitle').textContent = 'Yeni hesap oluşturun';
        document.getElementById('auth-form-container').innerHTML = `
            <form id="register-form" novalidate autocomplete="off">
                <div class="form-row">
                    <div class="form-group">
                        <label for="reg-name">Ad Soyad</label>
                        <div class="form-input-wrapper">
                            <i class="fa-solid fa-user"></i>
                            <input type="text" id="reg-name" class="form-input" placeholder="Ad Soyad" required>
                        </div>
                        <span class="field-error" id="err-reg-name"></span>
                    </div>
                    <div class="form-group">
                        <label for="reg-phone">Telefon</label>
                        <div class="form-input-wrapper">
                            <i class="fa-solid fa-phone"></i>
                            <input type="tel" id="reg-phone" class="form-input" placeholder="05XX XXX XX XX">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="reg-email">E-posta</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-envelope"></i>
                        <input type="email" id="reg-email" class="form-input" placeholder="ornek@email.com" required>
                    </div>
                    <span class="field-error" id="err-reg-email"></span>
                </div>
                <div class="form-group">
                    <label for="reg-password">Şifre <small style="color:var(--text-muted)">(min. 8 karakter)</small></label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-lock"></i>
                        <input type="password" id="reg-password" class="form-input" placeholder="••••••••" required>
                        <button type="button" class="pwd-toggle" id="toggle-reg-pwd" tabindex="-1">
                            <i class="fa-solid fa-eye"></i>
                        </button>
                    </div>
                    <span class="field-error" id="err-reg-pwd"></span>
                </div>
                <div class="form-group">
                    <label for="reg-role">Rol</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-user-tag"></i>
                        <select id="reg-role" class="form-select">
                            <option value="PATIENT">🧑‍⚕️ Hasta</option>
                            <option value="DOCTOR">👨‍⚕️ Doktor</option>
                        </select>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary btn-block" id="register-btn">
                    <i class="fa-solid fa-user-plus"></i> Kayıt Ol
                </button>
                <p style="text-align:center;margin-top:1.25rem;color:var(--text-secondary);font-size:0.9rem;">
                    Hesabınız var mı? <button type="button" class="text-btn" id="goto-login">Giriş Yap</button>
                </p>
            </form>
        `;

        document.getElementById('toggle-reg-pwd').addEventListener('click', () => {
            const inp = document.getElementById('reg-password');
            const ico = document.querySelector('#toggle-reg-pwd i');
            inp.type = inp.type === 'password' ? 'text' : 'password';
            ico.className = inp.type === 'password' ? 'fa-solid fa-eye' : 'fa-solid fa-eye-slash';
        });

        document.getElementById('register-form').addEventListener('submit', e => this.handleRegister(e));
        document.getElementById('goto-login').addEventListener('click', () => this.renderLogin());
    },

    // ===== Validasyon =====
    clearErrors() {
        document.querySelectorAll('.field-error').forEach(el => el.textContent = '');
        document.querySelectorAll('.form-input.invalid, .form-select.invalid').forEach(el => el.classList.remove('invalid'));
    },
    showError(fieldId, errId, msg) {
        const field = document.getElementById(fieldId);
        const errEl = document.getElementById(errId);
        if (field) field.classList.add('invalid');
        if (errEl) errEl.textContent = msg;
    },

    async handleLogin(e) {
        e.preventDefault();
        this.clearErrors();

        const email = document.getElementById('login-email').value.trim();
        const password = document.getElementById('login-password').value;

        let valid = true;
        if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            this.showError('login-email', 'err-login-email', 'Geçerli bir e-posta girin');
            valid = false;
        }
        if (!password || password.length < 4) {
            this.showError('login-password', 'err-login-pwd', 'Şifre boş olamaz');
            valid = false;
        }
        if (!valid) return;

        const btn = document.getElementById('login-btn');
        btn.disabled = true;
        btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Giriş yapılıyor...';

        try {
            const data = await Api.login(email, password);
            Api.setToken(data.token);
            App.toast('Hoş geldiniz! 👋', 'success');
            await App.init();
        } catch (err) {
            App.toast(err.message, 'error');
            document.getElementById('login-password').value = '';
        } finally {
            btn.disabled = false;
            btn.innerHTML = '<i class="fa-solid fa-right-to-bracket"></i> Giriş Yap';
        }
    },

    async handleRegister(e) {
        e.preventDefault();
        this.clearErrors();

        const fullName = document.getElementById('reg-name').value.trim();
        const email = document.getElementById('reg-email').value.trim();
        const phone = document.getElementById('reg-phone').value.trim();
        const password = document.getElementById('reg-password').value;
        const role = document.getElementById('reg-role').value;

        let valid = true;
        if (!fullName || fullName.length < 3) {
            this.showError('reg-name', 'err-reg-name', 'Ad Soyad en az 3 karakter olmalı');
            valid = false;
        }
        if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            this.showError('reg-email', 'err-reg-email', 'Geçerli bir e-posta girin');
            valid = false;
        }
        if (!password || password.length < 8) {
            this.showError('reg-password', 'err-reg-pwd', 'Şifre en az 8 karakter olmalı');
            valid = false;
        }
        if (!valid) return;

        const btn = document.getElementById('register-btn');
        btn.disabled = true;
        btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Kayıt oluşturuluyor...';

        try {
            await Api.register({ fullName, email, phone, password, role });
            App.toast('Kayıt başarılı! Şimdi giriş yapabilirsiniz. ✅', 'success');
            this.renderLogin();
            setTimeout(() => {
                const emailField = document.getElementById('login-email');
                if (emailField) emailField.value = email;
            }, 100);
        } catch (err) {
            App.toast(err.message, 'error');
        } finally {
            btn.disabled = false;
            btn.innerHTML = '<i class="fa-solid fa-user-plus"></i> Kayıt Ol';
        }
    }
};
