const shopping = document.querySelector(".header__cart-wrap");
const selectShopping = shopping.querySelector(".header__cart-icon");
const icon = shopping.querySelector(".header__cart-icon");


// icon.addEventListener("mouseenter", () => {
//     icon.classList.add("hovered");
// });

// icon.addEventListener("mouseleave", () => {
//     icon.classList.remove("hovered");
// });

selectShopping.addEventListener("click", () => {
    shopping.classList.toggle("active");
});

document.addEventListener("click", (e) => {
    if (!shopping.contains(e.target)) {
        shopping.classList.remove("active");
    }
    
});
