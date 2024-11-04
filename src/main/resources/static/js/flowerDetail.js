//Change main screen image
const thumbnailImages = document.querySelectorAll('.flower-information__img-item');
const mainScreenImage = document.querySelector('.flower-information__img-main-screen img');

function updateMainScreenImage(src, element) {
  mainScreenImage.src = src;
  thumbnailImages.forEach(img => img.classList.remove('pink-border'));
  element.classList.add('pink-border');
}
thumbnailImages.forEach(thumbnail => {
  thumbnail.addEventListener('mouseenter', () => {
    updateMainScreenImage(thumbnail.src, thumbnail);
  });

  thumbnail.addEventListener('click', () => {
    updateMainScreenImage(thumbnail.src, thumbnail);
  });
});


// Quantity button
function increaseValue() {
  var maxQuantity = parseInt(document.getElementById('maxQuantity').innerText, 10);
  var value = parseInt(document.getElementById('number').value, 10);
  value = isNaN(value) ? 0 : value;
  if (value < maxQuantity) {
    value++;
    document.getElementById('number').value = value;
    updateHiddenInput();
  }
  if (value >= maxQuantity) {
    document.getElementById('increase').disabled = true;
  }
}

function decreaseValue() {
  var value = parseInt(document.getElementById('number').value, 10);
  value = isNaN(value) ? 1 : value;
  value < 2 ? value = 2 : '';
  value--;
  document.getElementById('number').value = value;
  updateHiddenInput();
}

//Quantity value ref

  // Hàm để cập nhật giá trị của input hidden
//   function updateHiddenInput() {
//   var quantity = document.getElementById("number").value;
//   document.getElementById("quantity").value = quantity;
// }
//
//   // Gọi hàm khi form submit để đảm bảo hidden input luôn được cập nhật
//   document.getElementById("quantity").addEventListener('change', updateHiddenInput);

// Hàm cập nhật giá trị hidden input
function updateHiddenInput() {
  var numberInputValue = document.getElementById('number').value;
  document.getElementById('quantity').value = numberInputValue;  // Cập nhật giá trị vào hidden input
  document.getElementById('flowerQuantity').value = numberInputValue;
}