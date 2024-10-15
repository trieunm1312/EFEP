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