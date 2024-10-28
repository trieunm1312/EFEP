// Click show the create block
const createBtn = document.querySelector('.header__create-btn');
const createBlock = document.querySelector('.header__create-block');


createBtn.addEventListener('click', function(event) {
    event.stopPropagation(); 
    createBlock.style.display = (createBlock.style.display === 'block') ? 'none' : 'block';
});


document.addEventListener('click', function(event) {
    if (!createBtn.contains(event.target)) {
        createBlock.style.display = 'none';  
    }
});

//Click show the account block
document.getElementById("headerAccount").addEventListener("click", function() {
    var dropdown = document.getElementById("accountDropdown");
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
});

//Close dropdown when clicking outside
document.addEventListener("click", function(event) {
    var headerAccount = document.getElementById("headerAccount");
    var dropdown = document.getElementById("accountDropdown");

    if (!headerAccount.contains(event.target)) {
        dropdown.style.display = "none";
    }
});

//Open account box to logout
document.addEventListener("DOMContentLoaded", function () {
    const headerAccount = document.querySelector(".header__account");
    const accountBlock = document.querySelector(".header__account-block");

    // Toggle display when clicking on headerAccount
    headerAccount.addEventListener("click", function (event) {
        event.stopPropagation(); // Prevent click event from propagating to document
        accountBlock.style.display = accountBlock.style.display === "block" ? "none" : "block";
    });

    // Close the accountBlock when clicking outside
    document.addEventListener("click", function (event) {
        if (!headerAccount.contains(event.target)) {
            accountBlock.style.display = "none";
        }
    });
});