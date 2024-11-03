//Create new service item when click Create New
document.addEventListener("DOMContentLoaded", function() {
    const createBtn = document.querySelector('.manage-flower__create-btn');
    const newFlowerRow = document.querySelector('.table__body-item-new');

    createBtn.addEventListener('click', function() {
        if (newFlowerRow.style.display === 'none' || newFlowerRow.style.display === '') {
            newFlowerRow.style.display = 'table-row';
        } else {
            newFlowerRow.style.display = 'none';
        }
    });
});

// Click edit to edit the business service content
function editRow(button) {
    var row = button.closest('tr');

    var img = row.querySelector('#flower-update');
    var imgInput = row.querySelector('.file-input');

    var nameText = row.querySelector('.name-text');
    var nameInput = row.querySelector('.name-input');

    var descText = row.querySelector('.desc-text');
    var descInput = row.querySelector('.desc-input');

    var amountText = row.querySelector('.amount-text');
    var amountInput = row.querySelector('.amount-input');

    var qntText = row.querySelector('.qnt-text');
    var qntInput = row.querySelector('.qnt-input');

    var priceText = row.querySelector('.price-text');
    var priceInput = row.querySelector('.price-input');
    var statusText = row.querySelector('.status-text');
    var soldText = row.querySelector('.sold-text');

    var editBtn = row.querySelector('.edit-btn');
    var pencilIcon = row.querySelector('.modify-icon');
    var saveBtn = row.querySelector('.save-btn');
    var toggleBtn = row.querySelector('.toggle-btn');

    // img.style.display = 'none';
    imgInput.style.display = 'inline-block';

    nameText.style.display = 'none';
    nameInput.style.display = 'inline-block';

    descText.style.display = 'none';
    descInput.style.display = 'inline-block';

    amountText.style.display = 'none';
    amountInput.style.display = 'inline-block';

    qntText.style.display = 'none';
    qntInput.style.display = 'inline-block';

    priceText.style.display = 'none';
    priceInput.style.display = 'inline-block';

    editBtn.style.display = 'none';
    pencilIcon.style.display = 'none';
    toggleBtn.style.display = 'none';

    saveBtn.style.display = 'inline-block';
}

// Open and close popup functions
function openPopup() {
    document.getElementById("editPopup").style.display = "flex";
}

function closePopup() {
    document.getElementById("editPopup").style.display = "none";
}

// Show image preview on file upload
// document.getElementById("imageUpload").addEventListener("change", function(event) {
//     const files = event.target.files; // Lấy danh sách các tệp đã chọn
//     const imagePreviewContainer = document.getElementById("imagePreviewContainer");
    
//     // Đặt lại hình ảnh đã hiển thị trước đó
//     imagePreviewContainer.querySelectorAll("img").forEach(img => {
//         img.style.display = "none"; // Ẩn tất cả ảnh trước đó
//     });

//     // Hiển thị hình ảnh đã tải lên
//     Array.from(files).forEach((file, index) => {
//         if (index < 3) { // Chỉ hiển thị tối đa 3 hình ảnh
//             const reader = new FileReader();
//             reader.onload = function(e) {
//                 const previewImage = document.getElementById(`previewImage${index + 1}`);
//                 previewImage.src = e.target.result;
//                 previewImage.style.display = "block"; // Hiển thị ảnh
//             };
//             reader.readAsDataURL(file);
//         }
//     });
// });

// // Function to remove an image and allow new upload
// function removeImage(imageIndex) {
//     const previewImage = document.getElementById(`previewImage${imageIndex}`);
//     previewImage.style.display = "none"; // Ẩn hình ảnh đã xóa
//     previewImage.src = "#"; // Đặt lại đường dẫn hình ảnh

//     // Tạo một input mới cho hình ảnh nếu có không gian
//     const imageUpload = document.getElementById("imageUpload");
//     if (imageUpload.files.length < 3) {
//         imageUpload.click(); // Mở hộp thoại chọn file để tải lên hình ảnh mới
//     }
// }

let selectedSlot = null;

// Chọn ô để hiển thị ảnh
function selectSlot(slotIndex) {
    selectedSlot = slotIndex; // Gán slot hiện tại
    document.getElementById("imageUpload").click(); // Mở hộp thoại chọn file
}

// Tải ảnh lên cho slot đã chọn
function uploadImage() {
    const fileInput = document.getElementById("imageUpload");
    const file = fileInput.files[0]; // Chỉ lấy một ảnh

    if (file && selectedSlot) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const previewImage = document.getElementById(`previewImage${selectedSlot}`);
            previewImage.src = e.target.result;
            previewImage.style.display = "block"; // Hiển thị ảnh
        };
        reader.readAsDataURL(file);
    }
}

// Xóa ảnh ở slot được chọn
function removeImage(imageIndex, event) {
    event.stopPropagation(); // Ngăn sự kiện click trên ô khung hình vuông
    const previewImage = document.getElementById(`previewImage${imageIndex}`);
    previewImage.src = "#"; // Đặt lại đường dẫn ảnh
    previewImage.style.display = "none"; // Ẩn ảnh

    // Reset file input để có thể chọn lại
    const fileInput = document.getElementById("imageUpload");
    fileInput.value = ""; 
}


// Placeholder function for save button
function saveData() {
    closePopup();
}
