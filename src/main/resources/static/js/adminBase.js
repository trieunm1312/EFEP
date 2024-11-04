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




