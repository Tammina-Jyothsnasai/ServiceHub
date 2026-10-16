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
    const notifBadge = document.getElementById('notifBadge');
    if (user && notifBadge) {
        ApiClient.get(`/notifications/user/${user.id}/unread-count`)
            .then(res => {
                if (res.unreadCount > 0) {
                    notifBadge.textContent = res.unreadCount;
                    notifBadge.classList.remove('d-none');
                } else {
                    notifBadge.classList.add('d-none');
                }
            })
            .catch(err => console.log('Notification fetch silent error', err));

        // Attach click listener to bell parent link
        const bellLink = notifBadge.closest('a');
        if (bellLink) {
            bellLink.href = 'javascript:void(0);';
            bellLink.onclick = (e) => {
                e.preventDefault();
                openNotificationModal();
            };
        }
    }
}

// Interactive In-App Notification Center Modal
async function openNotificationModal() {
    const user = AuthService.getUser();
    if (!user) return;

    let modalEl = document.getElementById('notificationCenterModal');
    if (!modalEl) {
        const modalHtml = `
            <div class="modal fade" id="notificationCenterModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
                    <div class="modal-content rounded-4 border-0 shadow">
                        <div class="modal-header border-bottom pb-3">
                            <div class="d-flex align-items-center gap-2">
                                <i class="bi bi-bell-fill text-primary fs-5"></i>
                                <h5 class="modal-title fw-bold mb-0">Notifications</h5>
                            </div>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body p-0" id="notificationModalBody" style="max-height: 400px;">
                            <div class="p-4 text-center text-muted">
                                <div class="spinner-border spinner-border-sm text-primary me-2"></div> Loading notifications...
                            </div>
                        </div>
                        <div class="modal-footer border-top py-2 px-3 justify-content-between">
                            <span class="small text-muted" id="notifFooterCount">0 notifications</span>
                            <button type="button" class="btn btn-sm btn-outline-secondary rounded-pill" data-bs-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        document.body.insertAdjacentHTML('beforeend', modalHtml);
        modalEl = document.getElementById('notificationCenterModal');
    }

    const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
    modal.show();

    const bodyEl = document.getElementById('notificationModalBody');
    const footerCountEl = document.getElementById('notifFooterCount');

    try {
        const notifications = await ApiClient.get(`/notifications/user/${user.id}`);
        if (!notifications || notifications.length === 0) {
            bodyEl.innerHTML = `
                <div class="text-center py-5 text-muted">
                    <i class="bi bi-bell-slash fs-1 d-block mb-2 text-secondary opacity-50"></i>
                    <h6 class="fw-semibold">No notifications yet</h6>
                    <p class="small mb-0">Updates on your tickets will appear here.</p>
                </div>
            `;
            footerCountEl.textContent = '0 notifications';
            return;
        }

        footerCountEl.textContent = `${notifications.length} notifications`;

        let listHtml = '<div class="list-group list-group-flush">';
        notifications.forEach(n => {
            const isUnread = !n.read;
            const timeAgo = formatDate(n.createdAt);
            const reqLink = n.requestId ? `onclick="window.location.href='request-details.html?id=${n.requestId}'"` : '';
            const cursorStyle = n.requestId ? 'cursor: pointer;' : '';

            listHtml += `
                <div class="list-group-item p-3 ${isUnread ? 'bg-light border-start border-primary border-4' : ''}" style="${cursorStyle}">
                    <div class="d-flex justify-content-between align-items-start gap-2">
                        <div class="flex-grow-1" ${reqLink}>
                            <p class="mb-1 text-dark small ${isUnread ? 'fw-bold' : ''}">${n.message}</p>
                            <small class="text-muted"><i class="bi bi-clock me-1"></i>${timeAgo}</small>
                            ${n.requestId ? '<small class="text-primary ms-2"><i class="bi bi-arrow-right-circle me-1"></i>View Ticket</small>' : ''}
                        </div>
                        ${isUnread ? `
                            <button class="btn btn-sm btn-link text-decoration-none text-primary p-0" title="Mark as read" onclick="markNotifRead(${n.id}, event)">
                                <i class="bi bi-check2-circle fs-5"></i>
                            </button>
                        ` : ''}
                    </div>
                </div>
            `;
        });
        listHtml += '</div>';
        bodyEl.innerHTML = listHtml;

    } catch (err) {
        bodyEl.innerHTML = `<div class="p-3 text-center text-danger small">Failed to load notifications: ${err.message}</div>`;
    }
}

async function markNotifRead(notifId, event) {
    if (event) event.stopPropagation();
    try {
        await ApiClient.put(`/notifications/${notifId}/read`, {});
        renderHeaderUserInfo();
        openNotificationModal(); // reload list
    } catch (e) {
        console.error('Failed to mark read', e);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    renderHeaderUserInfo();
});
