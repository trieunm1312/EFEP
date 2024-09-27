const shopping = document.querySelector(".header__cart-wrap");
const selectShopping = shopping.querySelector(".header__cart-icon");

selectShopping.addEventListener("click", () => {
    shopping.classList.toggle("active");
});

document.addEventListener("click", (e) => {
    if (!shopping.contains(e.target)) {
        shopping.classList.remove("active");
    }
    
});

// document.getElementById("viewWishlistBtn").addEventListener("click", function (event) {
//     event.preventDefault(); // Ngăn không cho trang load lại
    
//     // Gửi yêu cầu GET đến đường dẫn '/buyer/wishlist'
//     fetch("/buyer/wishlist", {
//         method: "GET",
//         headers: {
//             "Content-Type": "application/json"
//         }
//     })
//     .then(response => response.json()) // Giả sử backend trả về JSON
//     .then(data => {
//         // Xử lý dữ liệu wishlist nhận được, bạn có thể hiển thị trong giao diện tùy ý
//         console.log(data);
//         // Ví dụ: hiển thị danh sách wishlist trong một phần tử HTML khác
//         alert("Yêu cầu thành công! Dữ liệu wishlist đã được tải.");
//     })
//     .catch(error => {
//         console.error("Lỗi:", error);
//         alert("Không thể tải wishlist.");
//     });
// });

// Đếm ngược từ 5 giây
var countdownElement = document.getElementById("countdown");
var countdown = 5; // Bắt đầu từ 5 giây

var interval = setInterval(function() {
    countdown--; // Giảm số giây
    countdownElement.textContent = countdown; // Cập nhật hiển thị

    if (countdown <= 0) {
        clearInterval(interval); // Dừng đếm ngược khi đến 0
        //window.location.href = "/home.html"; // Chuyển về trang chủ
    }
}, 1000); // Mỗi giây (1000ms) thực hiện một lần