// delete pop up
// Open and close popup functions
function openDeletePopup(id) {
    document.getElementById('confirm-Delete-' + id).style.display = "flex";
}

function closeDeletePopup(id) {
    document.getElementById('confirm-Delete-' + id).style.display = "none";
}

// create pop up
// Open and close popup functions
function openCreatePopup() {
    document.getElementById("createPopup").style.display = "flex";
}

function closeCreatePopup() {
    document.getElementById("createPopup").style.display = "none";
}

// Open and close popup functions
function openPopup(id) {
    document.getElementById('editPopup-' + id).style.display = "flex";
}

function closePopup(id) {
    document.getElementById('editPopup-' + id).style.display = "none";
}

// confirm-date pop up
// Open and close popup functions
function openConfirmDatePopup() {
    document.getElementById("confirm-date").style.display = "flex";
}

function closeConfirmDatePopup() {
    document.getElementById("confirm-date").style.display = "none";
}

const today = new Date();
const localDate = new Date(today.getTime() - today.getTimezoneOffset() * 60000)
    .toISOString()
    .split('T')[0];
document.getElementById('expiredDate').setAttribute('min', localDate);

