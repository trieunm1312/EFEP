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

// // Click edit to edit the business service content
// function editRow(button) {
//     var row = button.closest('tr');

//     var img = row.querySelector('.img');
//     var imgInput = row.querySelector('.img-input');

//     var nameText = row.querySelector('.name-text');
//     var nameInput = row.querySelector('.name-input');

//     var descText = row.querySelector('.desc-text');
//     var descInput = row.querySelector('.desc-input');

//     var amountText = row.querySelector('.amount-text');
//     var amountInput = row.querySelector('.amount-input');

//     var qntText = row.querySelector('.qnt-text');
//     var qntInput = row.querySelector('.qnt-input');

//     var priceText = row.querySelector('.price-text');
//     var priceInput = row.querySelector('.price-input');
//     var statusText = row.querySelector('.status-text');
//     var soldText = row.querySelector('.sold-text');

//     var editBtn = row.querySelector('.edit-btn');
//     var pencilIcon = row.querySelector('.modify-icon');
//     var saveBtn = row.querySelector('.save-btn');
//     var toggleBtn = row.querySelector('.toggle-btn');

//     img.style.display = 'none';
//     imgInput.style.display = 'inline-block';

//     nameText.style.display = 'none';
//     nameInput.style.display = 'inline-block';

//     descText.style.display = 'none';
//     descInput.style.display = 'inline-block';

//     amountText.style.display = 'none';
//     amountInput.style.display = 'inline-block';

//     qntText.style.display = 'none';
//     qntInput.style.display = 'inline-block';

//     priceText.style.display = 'none';
//     priceInput.style.display = 'inline-block';

//     editBtn.style.display = 'none';
//     pencilIcon.style.display = 'none';
//     toggleBtn.style.display = 'none';

//     saveBtn.style.display = 'inline-block';
// }
function openEditPopup(item) {
    // Populate fields with data from the selected row
    document.getElementById('flowerId').value = item.id;
    document.getElementById('popupName').value = item.name;
    document.getElementById('popupDescription').value = item.description;
    document.getElementById('popupAmount').value = item.flowerAmount;
    document.getElementById('popupQuantity').value = item.quantity;
    document.getElementById('popupPrice').value = item.price;
    document.getElementById('popupImagePreview').src = item.imageList[0].link || '';

    document.getElementById('flowerEditPopup').style.display = 'block';
}

function closePopup() {
    document.getElementById('flowerEditPopup').style.display = 'none';
}

function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function () {
        document.getElementById('popupImagePreview').src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
}

function clearImage() {
    document.getElementById('popupImagePreview').src = '';
    document.getElementById('popupImage').value = '';
}