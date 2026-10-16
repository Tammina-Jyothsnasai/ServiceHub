/* ServiceHub Authentication & Session Management */

const AuthService = {
    getUser() {
        const userStr = localStorage.getItem('servicehub_user');
        return userStr ? JSON.parse(userStr) : null;
    },

    setUser(user) {
        localStorage.setItem('servicehub_user', JSON.stringify(user));
    },

    clearSession() {
        localStorage.removeItem('servicehub_user');
    },

    isLoggedIn() {
        return this.getUser() !== null;
    },

    isAdmin() {
        const user = this.getUser();
        return user && user.role === 'ADMIN';
    },

    requireAuth(requiredRole = null) {
        const user = this.getUser();
        if (!user) {
            window.location.href = 'login.html';
            return false;
        }
        if (requiredRole && user.role !== requiredRole) {
            if (user.role === 'ADMIN') {
                window.location.href = 'admin-dashboard.html';
            } else {
                window.location.href = 'dashboard.html';
            }
            return false;
        }
        return true;
    },

    logout() {
        this.clearSession();
        showToast('Logged out successfully', 'info');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 600);
    }
};

// UI Top Header / User Info Setup
function renderHeaderUserInfo() {
    const user = AuthService.getUser();
    const nameEl = document.getElementById('headerUserName');
    const roleEl = document.getElementById('headerUserRole');

    if (nameEl && user) nameEl.textContent = user.name;
    if (roleEl && user) roleEl.textContent = user.role;

    // Fetch unread notifications count if user logged in
    if (user && document.getElementById('notifBadge')) {
        ApiClient.get(`/notifications/user/${user.id}/unread-count`)
            .then(res => {
                const badge = document.getElementById('notifBadge');
                if (badge && res.unreadCount > 0) {
                    badge.textContent = res.unreadCount;
                    badge.classList.remove('d-none');
                }
            })
            .catch(err => console.log('Notification fetch silent error', err));
    }
}

document.addEventListener('DOMContentLoaded', () => {
    renderHeaderUserInfo();
});
