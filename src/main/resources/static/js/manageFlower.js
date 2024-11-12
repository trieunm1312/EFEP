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
