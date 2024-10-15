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
