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

        // Kích hoạt nút "Submit"
        document.querySelector(".submit").disabled = false;
    })
})

function handleSubmit(event) {
    event.preventDefault(); // Ngăn trang reload
    document.getElementById("feedback-form").classList.add("hide");
    document.getElementById("submit-section").classList.remove("hide");
}

// document.getElementById("feedback-form").addEventListener("submit", handleSubmit);
//
// function handleSubmit(event) {
//     event.preventDefault(); // Ngăn trang reload
//
//     // Ẩn form và hiển thị phần xác nhận sau khi gửi
//     document.getElementById("feedback-form").classList.add("hide");
//     document.getElementById("submit-section").classList.remove("hide");
//
//     // Gửi form
//     document.getElementById("feedback-form").submit();
// }
