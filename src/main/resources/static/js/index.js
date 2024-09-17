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
