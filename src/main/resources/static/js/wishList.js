function deleteItem(form) {
    // Lấy dữ liệu từ form
    const wishlistItemId = form.querySelector('input[name="wishlistItemId"]').value;
    const accountId = form.querySelector('input[name="accountId"]').value;

    // Gửi yêu cầu DELETE bằng fetch
    fetch('/buyer/wishlist-item', {
        method: 'POST', // Dùng POST vì bạn đang giả lập PUT hoặc DELETE
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            '_method': 'delete',  // Chỉ định phương thức DELETE thông qua _method
            'accountId': accountId,
            'wishlistItemId': wishlistItemId
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Xóa phần tử trong DOM sau khi xóa thành công
                form.closest('tr').remove(); // Xóa dòng chứa mục vừa bị xóa
                updateTotalPrice(); // Cập nhật lại tổng giỏ hàng nếu cần
            } else {
                console.error('Xóa không thành công');
            }
        })
        .catch(error => console.error('Lỗi:', error));
}

function updateTotalPrice() {
    let total = 0;
    const priceElements = document.querySelectorAll('.table-item-price');
    priceElements.forEach(priceElement => {
        const price = parseFloat(priceElement.textContent.replace('$', '').trim());
        if (!isNaN(price)) {
            total += price;
        }
    });

    const totalPriceElement = document.querySelector('.total__wishlist-detail-price');
    totalPriceElement.textContent = '$ ' + total.toFixed(2); // Cập nhật tổng giá trị mới
}
