document.addEventListener('DOMContentLoaded', () => {
    const firstForm = document.querySelector('.first-form');
    const secondForm = document.querySelector('.second-form');
    const overlay = document.querySelector('.overlay');
    const validateEmailButton = document.querySelector('.validate-email');
    const cancelButton = document.querySelector('.auth-form__switch-btn');

    // Handle form transition to show the second form
    validateEmailButton.addEventListener('click', (e) => {
        e.preventDefault(); // Prevent actual form submission for now

        // Hide the first form and show the second form and overlay
        firstForm.classList.add('hidden');
        secondForm.classList.remove('hidden');
        overlay.classList.remove('hidden');
    });

    // Handle cancel button to hide the second form and show the first form again
    cancelButton.addEventListener('click', (e) => {
        e.preventDefault(); // Prevent default link behavior

        // Hide the second form and overlay, show the first form
        secondForm.classList.add('hidden');
        overlay.classList.add('hidden');
        firstForm.classList.remove('hidden');
    });

    // Prevent the second form and overlay from closing when clicking outside (on overlay)
    overlay.addEventListener('click', (e) => {
        e.stopPropagation(); // Ensure clicking the overlay doesn't hide the second form
    });
});
