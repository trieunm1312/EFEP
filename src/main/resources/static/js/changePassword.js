document.addEventListener('DOMContentLoaded', () => {
    const firstForm = document.querySelector('.first-form');
    const secondForm = document.querySelector('.second-form');
    const overlay = document.querySelector('.overlay');
    const validateEmailButton = document.querySelector('.verfy-password');
    const cancelButton = document.querySelector('.auth-form__switch-btn');
    
    validateEmailButton.addEventListener('click', (e) => {
        e.preventDefault(); 

        firstForm.classList.add('hidden');
        secondForm.classList.remove('hidden');
        overlay.classList.remove('hidden');
    });

    cancelButton.addEventListener('click', (e) => {
        e.preventDefault(); 

        secondForm.classList.add('hidden');
        overlay.classList.add('hidden');
        firstForm.classList.remove('hidden');
    });

    overlay.addEventListener('click', (e) => {
        e.stopPropagation(); 
    });
});
