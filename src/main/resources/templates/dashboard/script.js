const API_BASE = "http://localhost:8080/api/admin";

// تحميل المستخدمين (وتقسيمهم إلى Mothers و Doctors)
async function loadUsers(search = "") {
  try {
    const res = await fetch(`${API_BASE}/users?search=${search}`);
    const data = await res.json();

    const mothersBody = document.getElementById("mothers-table-body");
    const doctorsBody = document.getElementById("doctors-table-body");
    mothersBody.innerHTML = "";
    doctorsBody.innerHTML = "";

    if (!data.data || data.data.length === 0) {
      mothersBody.innerHTML = '<tr><td colspan="8" style="text-align: center; color: #666;">No mothers found</td></tr>';
      doctorsBody.innerHTML = '<tr><td colspan="12" style="text-align: center; color: #666;">No doctors found</td></tr>';
      return;
    }

    data.data.forEach(user => {
      const tr = document.createElement("tr");

      if (user.role === "MOTHER") {
        tr.innerHTML = `
          <td>${user.userId || '<span class="na-value">N/A</span>'}</td>
          <td>${user.email || '<span class="na-value">N/A</span>'}</td>
          <td>${user.firstName || '<span class="na-value">N/A</span>'}</td>
          <td>${user.lastName || '<span class="na-value">N/A</span>'}</td>
          <td>${user.phoneNumber || '<span class="na-value">N/A</span>'}</td>
          <td>${formatDate(user.dateOfBirth)}</td>
          <td>${user.address || '<span class="na-value">N/A</span>'}</td>
          <td>
            <button class="edit-btn" data-id="${user.userId}" onclick="editUser(${user.userId})">Edit</button>
            <button class="delete-btn" data-id="${user.userId}" onclick="deleteUser(${user.userId})">Delete</button>
          </td>
        `;
        mothersBody.appendChild(tr);
      } else if (user.role === "DOCTOR") {
        tr.innerHTML = `
          <td>${user.userId || '<span class="na-value">N/A</span>'}</td>
          <td>${user.email || '<span class="na-value">N/A</span>'}</td>
          <td>${user.firstName || '<span class="na-value">N/A</span>'}</td>
          <td>${user.lastName || '<span class="na-value">N/A</span>'}</td>
          <td>${user.phoneNumber || '<span class="na-value">N/A</span>'}</td>
          <td>${user.specialization || '<span class="na-value">N/A</span>'}</td>
          <td>${user.doctorLicense || '<span class="na-value">N/A</span>'}</td>
          <td>${formatDate(user.dateOfBirth)}</td>
          <td>${user.address || '<span class="na-value">N/A</span>'}</td>
          <td>${user.academicDegree || '<span class="na-value">N/A</span>'}</td>
          <td>${user.yearsOfExperience ? user.yearsOfExperience + ' years' : '<span class="na-value">N/A</span>'}</td>
          <td>
            <button class="edit-btn" data-id="${user.userId}" onclick="editUser(${user.userId})">Edit</button>
            <button class="delete-btn" data-id="${user.userId}" onclick="deleteUser(${user.userId})">Delete</button>
          </td>
        `;
        doctorsBody.appendChild(tr);
      }
    });
  } catch (error) {
    console.error('Error loading users:', error);
    const mothersBody = document.getElementById("mothers-table-body");
    const doctorsBody = document.getElementById("doctors-table-body");
    mothersBody.innerHTML = '<tr><td colspan="8" style="text-align: center; color: #f44336;">Error loading data</td></tr>';
    doctorsBody.innerHTML = '<tr><td colspan="12" style="text-align: center; color: #f44336;">Error loading data</td></tr>';
  }
}

// تحميل إحصائيات النظام
async function loadSystemStatus() {
  try {
    const res = await fetch(`${API_BASE}/system/status`);
    const data = await res.json();
    
    document.getElementById("total-users").textContent = data.totalUsers || 0;
    document.getElementById("total-mothers").textContent = data.mothers || 0;
    document.getElementById("total-doctors").textContent = data.doctors || 0;
    document.getElementById("server-time").textContent = data.serverTime ? 
      new Date(data.serverTime).toLocaleString() : 'N/A';
  } catch (error) {
    console.error('Error loading system status:', error);
    document.getElementById("total-users").textContent = 'Error';
    document.getElementById("total-mothers").textContent = 'Error';
    document.getElementById("total-doctors").textContent = 'Error';
    document.getElementById("server-time").textContent = 'Error';
  }
}

// حذف المستخدم
async function deleteUser(userId) {
  if (!confirm("Are you sure you want to delete this user?")) return;
  
  try {
    const res = await fetch(`${API_BASE}/users/${userId}`, {
      method: "DELETE",
    });

    if (res.ok) {
      alert("User deleted successfully.");
      loadUsers(); // إعادة التحميل
      loadSystemStatus();
    } else {
      const errorData = await res.json();
      alert("Failed to delete user: " + (errorData.message || 'Unknown error'));
    }
  } catch (error) {
    console.error('Error deleting user:', error);
    alert("Failed to delete user. Please try again.");
  }
}

// تحديث بيانات المستخدم
async function updateUser(userId, updatedData) {
  try {
    const res = await fetch(`${API_BASE}/users/${userId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updatedData),
    });
    const result = await res.json();
    alert(result.message || "User updated successfully.");
    loadUsers(); // Reload the data
  } catch (error) {
    console.error('Error updating user:', error);
    alert("Failed to update user. Please try again.");
  }
}

// Edit user function
function editUser(userId) {
  // For now, we'll show a simple prompt. You can enhance this with a modal later
  const newEmail = prompt("Enter new email:");
  if (newEmail && newEmail.trim()) {
    updateUser(userId, { email: newEmail.trim() });
  }
}

// Helper function to format dates
function formatDate(dateString) {
  if (!dateString) return 'N/A';
  try {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return 'N/A';
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  } catch (error) {
    return 'N/A';
  }
}

// تحميل البيانات عند فتح الصفحة
document.addEventListener("DOMContentLoaded", () => {
  loadSystemStatus();
  loadUsers();

  // البحث التلقائي أثناء الكتابة
  document.getElementById("search-input").addEventListener("input", (e) => {
    loadUsers(e.target.value);
  });

  // البحث عند الضغط على زرار Search
  document.getElementById("search-button").addEventListener("click", () => {
    const query = document.getElementById("search-input").value;
    loadUsers(query);
  });
});
