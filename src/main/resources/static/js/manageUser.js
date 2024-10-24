//Create Seller Account form
document.addEventListener("DOMContentLoaded", function() {
    // Function to get the URL parameter
    function getUrlParameter(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    const newSeller = getUrlParameter('newSeller');
    const createNewBtn = document.getElementById('createNewBtn');
    const secondContainer = document.getElementById('secondContainer');
    const closeBtn = document.getElementById('closeBtn');


    if (newSeller === 'true' && secondContainer) {
        secondContainer.classList.add('active');
    }


    if (createNewBtn) {
        createNewBtn.addEventListener('click', () => {
            secondContainer.classList.add('active');  // Slide in the form
        });
    } else {
        console.error('Create New button not found.');
    }


    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            secondContainer.classList.add('closing');


            setTimeout(() => {
                secondContainer.classList.remove('active', 'closing');
            }, 400);
        });
    } else {
        console.error('Close button not found.');
    }
});


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