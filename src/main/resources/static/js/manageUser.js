const createNewBtn = document.getElementById('createNewBtn');
const secondContainer = document.getElementById('secondContainer');
const closeBtn = document.getElementById('closeBtn');

// Display slide in the form
createNewBtn.addEventListener('click', () => {
    secondContainer.classList.add('active');
});

// Close the form
closeBtn.addEventListener('click', () => {
    secondContainer.classList.add('closing');  // Add 'closing' class to trigger slide out

    // Wait for the transition to complete (0.4s for slide-out)
    setTimeout(() => {
        secondContainer.classList.remove('active', 'closing');  // Remove both classes after animation ends
    }, 400);
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