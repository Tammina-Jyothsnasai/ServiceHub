/* ServiceHub Centralized API Client & Notification Toast Engine */

const API_BASE_URL = 'http://localhost:8080/api';

class ApiClient {
    static async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;
        const defaultHeaders = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };

        const config = {
            ...options,
            headers: {
                ...defaultHeaders,
                ...options.headers
            }
        };

        try {
            const response = await fetch(url, config);
            const contentType = response.headers.get('content-type');
            let data = null;

            if (contentType && contentType.includes('application/json')) {
                data = await response.json();
            } else {
                data = await response.text();
            }

            if (!response.ok) {
                const errorMessage = (data && data.message) ? data.message : `HTTP error! Status: ${response.status}`;
                throw new Error(errorMessage);
            }

            return data;
        } catch (error) {
            console.error(`API Error [${endpoint}]:`, error);
            throw error;
        }
    }

    static get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    }

    static post(endpoint, body) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(body)
        });
    }

    static put(endpoint, body) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body)
        });
    }

    static delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
}

// Toast Alert Manager
function showToast(message, type = 'success') {
    let container = document.getElementById('toastContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toastContainer';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toastId = 'toast_' + Date.now();
    const bgClass = type === 'success' ? 'bg-success' : (type === 'error' ? 'bg-danger' : 'bg-info');

    const toastHtml = `
        <div id="${toastId}" class="toast align-items-center text-white ${bgClass} border-0 show shadow" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body font-weight-medium">
                    <i class="bi ${type === 'success' ? 'bi-check-circle-fill' : 'bi-exclamation-triangle-fill'} me-2"></i>
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `;

    container.insertAdjacentHTML('beforeend', toastHtml);
    setTimeout(() => {
        const el = document.getElementById(toastId);
        if (el) el.remove();
    }, 4000);
}

// Format Helpers
function formatDate(dateString) {
    if (!dateString) return '-';
    const d = new Date(dateString);
    return d.toLocaleDateString('en-US', {
        month: 'short', day: 'numeric', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });
}

function getStatusBadge(status) {
    const s = (status || 'PENDING').toUpperCase();
    const badges = {
        'PENDING': '<span class="badge-status badge-pending"><i class="bi bi-clock-history"></i> PENDING</span>',
        'ASSIGNED': '<span class="badge-status badge-assigned"><i class="bi bi-person-check-fill"></i> ASSIGNED</span>',
        'IN_PROGRESS': '<span class="badge-status badge-in_progress"><i class="bi bi-gear-wide-connected"></i> IN PROGRESS</span>',
        'RESOLVED': '<span class="badge-status badge-resolved"><i class="bi bi-check-circle-fill"></i> RESOLVED</span>',
        'CLOSED': '<span class="badge-status badge-closed"><i class="bi bi-archive-fill"></i> CLOSED</span>'
    };
    return badges[s] || `<span class="badge bg-secondary">${s}</span>`;
}

function getPriorityBadge(priority) {
    const p = (priority || 'MEDIUM').toUpperCase();
    const badges = {
        'LOW': '<span class="badge-priority badge-low">LOW</span>',
        'MEDIUM': '<span class="badge-priority badge-medium">MEDIUM</span>',
        'HIGH': '<span class="badge-priority badge-high">HIGH</span>',
        'URGENT': '<span class="badge-priority badge-urgent">URGENT</span>'
    };
    return badges[p] || `<span class="badge bg-secondary">${p}</span>`;
}
