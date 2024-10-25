const swiper = new Swiper('.swiper', {
  loop: true,

  // If we need pagination
  pagination: {
    el: '.swiper-pagination',
  },

  // Navigation arrows
  navigation: {
    nextEl: '.swiper-button-next',
    prevEl: '.swiper-button-prev',
  },

  breakpoints: {
    1024: {
      slidesPerView: 4, // When viewport is 1024px or larger, show 4 items
    },
    768: {
      slidesPerView: 3, // When viewport is 768px or larger, show 3 items
    },
    640: {
      slidesPerView: 2, // When viewport is 640px or larger, show 2 items
    },
    320: {
      slidesPerView: 1, // When viewport is 320px or larger, show 1 item
    }
  }
});

// Quantity button
function increaseValue() {
  var value = parseInt(document.getElementById('number').value, 10);
  value = isNaN(value) ? 0 : value;
  value++;
  document.getElementById('number').value = value;
  updateHiddenInput();
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