const allStar = document.querySelectorAll('.rating .star');
const ratingValue = document.querySelector('.rating input');
const getValueRating = document.getElementById('rate')

allStar.forEach((item, idx) => {
    item.addEventListener('click', function () {
        let click = 0
        ratingValue.value = idx + 1
        getValueRating.value = ratingValue.value
        console.log(getValueRating.value)

        allStar.forEach(i => {
            i.classList.replace('fa-solid', 'fa-regular')
            i.classList.remove('active')
        })
        for (let i = 0; i < allStar.length; i++) {
            if (i <= idx) {
                allStar[i].classList.replace('fa-regular', 'fa-solid')
                allStar[i].classList.add('active')
            } else {
                allStar[i].style.setProperty('--i', click)
                click++
            }
        }

        document.querySelector(".submit").disabled = false;
    })
})

function submitFeedback() {
    // Lấy dữ liệu từ form
    const form = document.getElementById("feedback-form");
    const formData = new FormData(form);

    // Gửi dữ liệu qua `fetch` mà không tải lại trang
    fetch(form.action, {
        method: "POST",
        body: formData
    })
// Ẩn form và hiển thị phần xác nhận ngay sau khi gọi `fetch`
    document.getElementById("feedback-form").classList.add("hide");
    document.getElementById("submit-section").classList.remove("hide");

}
