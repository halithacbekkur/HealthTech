/* ============================================
   HealthTech Auth Module v3
   Login/Register form yönetimi + gelişmiş validasyon
   Faz 1: TC Kimlik, doğum tarihi, cinsiyet eklendi
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
                <div style="display:flex;justify-content:space-between;margin-top:1.25rem;font-size:0.9rem;">
                    <button type="button" class="text-btn" id="goto-forgot">Şifremi Unuttum</button>
                    <span style="color:var(--text-secondary)">
                        Hesabınız yok mu? <button type="button" class="text-btn" id="goto-register">Kayıt Ol</button>
                    </span>
                </div>
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
        document.getElementById('goto-forgot').addEventListener('click', () => this.renderForgotPassword());

        // Enter tuşu
        document.getElementById('login-password').addEventListener('keydown', e => {
            if (e.key === 'Enter') document.getElementById('login-form').requestSubmit();
        });
    },

    renderRegister() {
        document.getElementById('auth-subtitle').textContent = 'Yeni hesap oluşturun';
        document.getElementById('auth-form-container').innerHTML = `
            <form id="register-form" novalidate autocomplete="off">
                <!-- Adım 1: Temel Bilgiler -->
                <div id="reg-step-1">
                    <div class="step-indicator"><span class="step active">1</span><span class="step-line"></span><span class="step">2</span></div>
                    <h3 style="margin-bottom:1rem;font-size:0.95rem;color:var(--text-secondary)">Temel Bilgiler</h3>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="reg-name">Ad Soyad *</label>
                            <div class="form-input-wrapper">
                                <i class="fa-solid fa-user"></i>
                                <input type="text" id="reg-name" class="form-input" placeholder="Ad Soyad" required>
                            </div>
                            <span class="field-error" id="err-reg-name"></span>
                        </div>
                        <div class="form-group">
                            <label for="reg-tc">TC Kimlik No</label>
                            <div class="form-input-wrapper">
                                <i class="fa-solid fa-id-card"></i>
                                <input type="text" id="reg-tc" class="form-input" placeholder="11 haneli" maxlength="11">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="reg-email">E-posta *</label>
                        <div class="form-input-wrapper">
                            <i class="fa-solid fa-envelope"></i>
                            <input type="email" id="reg-email" class="form-input" placeholder="ornek@email.com" required>
                        </div>
                        <span class="field-error" id="err-reg-email"></span>
                    </div>
                    <div class="form-group">
                        <label for="reg-password">Şifre * <small style="color:var(--text-muted)">(min. 8 karakter)</small></label>
                        <div class="form-input-wrapper">
                            <i class="fa-solid fa-lock"></i>
                            <input type="password" id="reg-password" class="form-input" placeholder="••••••••" required>
                            <button type="button" class="pwd-toggle" id="toggle-reg-pwd" tabindex="-1">
                                <i class="fa-solid fa-eye"></i>
                            </button>
                        </div>
                        <span class="field-error" id="err-reg-pwd"></span>
                    </div>
                    <button type="button" class="btn btn-primary btn-block" id="reg-next-btn">
                        Devam <i class="fa-solid fa-arrow-right"></i>
                    </button>
                </div>

                <!-- Adım 2: Detaylı Bilgiler -->
                <div id="reg-step-2" style="display:none;">
                    <div class="step-indicator"><span class="step completed"><i class="fa-solid fa-check"></i></span><span class="step-line active"></span><span class="step active">2</span></div>
                    <h3 style="margin-bottom:1rem;font-size:0.95rem;color:var(--text-secondary)">Kişisel Bilgiler</h3>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="reg-phone">Telefon</label>
                            <div class="form-input-wrapper">
                                <i class="fa-solid fa-phone"></i>
                                <input type="tel" id="reg-phone" class="form-input" placeholder="05XX XXX XX XX">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="reg-birthdate">Doğum Tarihi</label>
                            <div class="form-input-wrapper">
                                <i class="fa-solid fa-cake-candles"></i>
                                <input type="date" id="reg-birthdate" class="form-input">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="reg-gender">Cinsiyet</label>
                            <div class="form-input-wrapper">
                                <i class="fa-solid fa-venus-mars"></i>
                                <select id="reg-gender" class="form-select">
                                    <option value="">Seçiniz</option>
                                    <option value="MALE">Erkek</option>
                                    <option value="FEMALE">Kadın</option>
                                    <option value="OTHER">Diğer</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="reg-role">Rol *</label>
                            <div class="form-input-wrapper">
                                <i class="fa-solid fa-user-tag"></i>
                                <select id="reg-role" class="form-select">
                                    <option value="PATIENT">🧑‍⚕️ Hasta</option>
                                    <option value="DOCTOR">👨‍⚕️ Doktor</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div style="display:flex;gap:0.75rem;">
                        <button type="button" class="btn btn-outline" id="reg-back-btn" style="flex:1">
                            <i class="fa-solid fa-arrow-left"></i> Geri
                        </button>
                        <button type="submit" class="btn btn-primary" id="register-btn" style="flex:2">
                            <i class="fa-solid fa-user-plus"></i> Kayıt Ol
                        </button>
                    </div>
                </div>

                <p style="text-align:center;margin-top:1.25rem;color:var(--text-secondary);font-size:0.9rem;">
                    Hesabınız var mı? <button type="button" class="text-btn" id="goto-login">Giriş Yap</button>
                </p>
            </form>
        `;

        // Şifre göster/gizle
        document.getElementById('toggle-reg-pwd').addEventListener('click', () => {
            const inp = document.getElementById('reg-password');
            const ico = document.querySelector('#toggle-reg-pwd i');
            inp.type = inp.type === 'password' ? 'text' : 'password';
            ico.className = inp.type === 'password' ? 'fa-solid fa-eye' : 'fa-solid fa-eye-slash';
        });

        // Adım geçişleri
        document.getElementById('reg-next-btn').addEventListener('click', () => {
            this.clearErrors();
            const name = document.getElementById('reg-name').value.trim();
            const email = document.getElementById('reg-email').value.trim();
            const password = document.getElementById('reg-password').value;

            let valid = true;
            if (!name || name.length < 3) { this.showError('reg-name', 'err-reg-name', 'Ad Soyad en az 3 karakter'); valid = false; }
            if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) { this.showError('reg-email', 'err-reg-email', 'Geçerli bir e-posta girin'); valid = false; }
            if (!password || password.length < 8) { this.showError('reg-password', 'err-reg-pwd', 'Şifre en az 8 karakter'); valid = false; }
            if (!valid) return;

            document.getElementById('reg-step-1').style.display = 'none';
            document.getElementById('reg-step-2').style.display = 'block';
        });

        document.getElementById('reg-back-btn').addEventListener('click', () => {
            document.getElementById('reg-step-2').style.display = 'none';
            document.getElementById('reg-step-1').style.display = 'block';
        });

        document.getElementById('register-form').addEventListener('submit', e => this.handleRegister(e));
        document.getElementById('goto-login').addEventListener('click', () => this.renderLogin());
    },

    renderForgotPassword() {
        document.getElementById('auth-subtitle').textContent = 'Şifrenizi sıfırlayın';
        document.getElementById('auth-form-container').innerHTML = `
            <form id="forgot-form" novalidate>
                <p style="color:var(--text-secondary);font-size:0.9rem;margin-bottom:1.25rem;">
                    E-posta adresinizi girin, size şifre sıfırlama linki göndereceğiz.
                </p>
                <div class="form-group">
                    <label for="forgot-email">E-posta</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-envelope"></i>
                        <input type="email" id="forgot-email" class="form-input" placeholder="ornek@email.com" required>
                    </div>
                    <span class="field-error" id="err-forgot-email"></span>
                </div>
                <button type="submit" class="btn btn-primary btn-block" id="forgot-btn">
                    <i class="fa-solid fa-paper-plane"></i> Gönder
                </button>
                <p style="text-align:center;margin-top:1.25rem;color:var(--text-secondary);font-size:0.9rem;">
                    <button type="button" class="text-btn" id="goto-login-from-forgot">← Giriş sayfasına dön</button>
                </p>
            </form>
        `;

        document.getElementById('forgot-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            this.clearErrors();
            const email = document.getElementById('forgot-email').value.trim();
            if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                this.showError('forgot-email', 'err-forgot-email', 'Geçerli bir e-posta girin');
                return;
            }
            const btn = document.getElementById('forgot-btn');
            btn.disabled = true;
            btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Gönderiliyor...';
            try {
                await Api.forgotPassword(email);
                App.toast('Şifre sıfırlama linki e-postanıza gönderildi', 'success');
                this.renderLogin();
            } catch (err) {
                App.toast(err.message, 'error');
            } finally {
                btn.disabled = false;
                btn.innerHTML = '<i class="fa-solid fa-paper-plane"></i> Gönder';
            }
        });

        document.getElementById('goto-login-from-forgot').addEventListener('click', () => this.renderLogin());
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
            // Kullanıcı bilgilerini login response'dan al
            if (data.user) {
                localStorage.setItem('ht_user', JSON.stringify(data.user));
            }
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
        const tcKimlik = document.getElementById('reg-tc').value.trim() || null;
        const birthDate = document.getElementById('reg-birthdate').value || null;
        const genderVal = document.getElementById('reg-gender').value || null;

        const btn = document.getElementById('register-btn');
        btn.disabled = true;
        btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Kayıt oluşturuluyor...';

        try {
            const payload = { fullName, email, phone, password, role };
            if (tcKimlik) payload.tcKimlik = tcKimlik;
            if (birthDate) payload.birthDate = birthDate;
            if (genderVal) payload.gender = genderVal;

            await Api.register(payload);
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
