//close toast
function closeToast(element) {
    const toast = element.closest('.toast');
    if (toast) {
        toast.style.display = 'none';
    }
}

//Password constraint
document.addEventListener('DOMContentLoaded', function() {
    const passwordInput = document.getElementById('password');

    passwordInput.addEventListener('input', function() {
        const password = passwordInput.value;

        // Check for uppercase letter
        const hasUppercase = /[A-Z]/.test(password);
        toggleConstraintColor('uppercase', hasUppercase);

        // Check for lowercase letter
        const hasLowercase = /[a-z]/.test(password);
        toggleConstraintColor('lowercase', hasLowercase);

        // Check for special character
        const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
        toggleConstraintColor('special', hasSpecialChar);

        // Check for number
        const hasNumber = /\d/.test(password);
        toggleConstraintColor('number', hasNumber);

        // Check for length
        const isLongEnough = password.length >= 8;
        toggleConstraintColor('length', isLongEnough);
    });

    function toggleConstraintColor(id, isValid) {
        const element = document.getElementById(id);
        if (isValid) {
            element.style.color = '#00ff00'; // Turn light green if valid
        } else {
            element.style.color = '#d9534f'; // Turn red if invalid
        }
    }
});


//Truncate user name
document.addEventListener("DOMContentLoaded", function() {
    const userNameElement = document.getElementById("userName");
    let fullName = userNameElement.textContent.trim();

    // Split the name by spaces and get the first word
    let firstName = fullName.split(" ")[0];

    // If the first name is longer than 5 characters, truncate it
    if (firstName.length > 5) {
        firstName = firstName.substring(0, 5) + "...";
    }

    // Update the span's text content with the truncated name
    userNameElement.textContent = firstName;
});


document.querySelectorAll('.header__item-megamenu-item__category').forEach(item => {
    item.addEventListener('mouseenter', () => {
        item.querySelector('.category__block').style.display = 'block';
    });
    item.addEventListener('mouseleave', () => {
        item.querySelector('.category__block').style.display = 'none';
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const searchIcon = document.querySelector('.header__item-icon-search');
    const searchBox = document.querySelector('.header__item-search-box');
    const userIcon = document.querySelector('.header__item-icon-user');
    const accountBox = document.querySelector('.header__item-account');
    const wishlistIcon = document.querySelector('.header__cart-icon');  // Wishlist icon
    const wishlistBox = document.querySelector('.header__cart-list');   // Wishlist box

    // Function to hide all boxes
    function hideAllBoxes() {
        searchBox.style.display = 'none';
        accountBox.style.display = 'none';
        wishlistBox.style.display = 'none';
    }

    // Toggle search box visibility on click
    searchIcon.addEventListener('click', (e) => {
        e.stopPropagation();

        // Hide account and wishlist boxes if they are open
        hideAllBoxes();

        // Toggle between 'none' and 'flex' display values for the search box
        if (searchBox.style.display === 'flex') {
            searchBox.style.display = 'none';
        } else {
            searchBox.style.display = 'flex';
        }
    });

    // Toggle account box visibility on click
    userIcon.addEventListener('click', (e) => {
        e.stopPropagation();

        // Hide search and wishlist boxes if they are open
        hideAllBoxes();

        // Toggle between 'none' and 'block' display values for the account box
        if (accountBox.style.display === 'block') {
            accountBox.style.display = 'none';
        } else {
            accountBox.style.display = 'block';
        }
    });

    // Toggle wishlist box visibility on click
    wishlistIcon.addEventListener('click', (e) => {
        e.stopPropagation();

        // Hide search and account boxes if they are open
        hideAllBoxes();

        // Toggle between 'none' and 'block' display values for the wishlist box
        if (wishlistBox.style.display === 'block') {
            wishlistBox.style.display = 'none';
        } else {
            wishlistBox.style.display = 'block';
        }
    });

    // Hide all boxes when clicking outside
    document.addEventListener('click', () => {
        hideAllBoxes();
    });

    // Prevent hiding when interacting with the search box
    searchBox.addEventListener('click', (e) => {
        e.stopPropagation();
    });

    // Prevent hiding when interacting with the account box
    accountBox.addEventListener('click', (e) => {
        e.stopPropagation();
    });

    // Prevent hiding when interacting with the wishlist box
    wishlistBox.addEventListener('click', (e) => {
        e.stopPropagation();
    });
});




// View Order Summary
var countdownElement = document.getElementById("countdown");
var countdown = 5;
var redirectUrl = "/";


var role = /*[[${session.acc.getRole()}]]*/ 'buyer';
if (role === 'seller') {
    redirectUrl = "/seller/view/plan";
}

var interval = setInterval(function() {
    countdown--;
    countdownElement.textContent = countdown;

    if (countdown <= 0) {
        clearInterval(interval);
        window.location.href = redirectUrl;
    }
}, 1000);