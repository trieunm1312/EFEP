//open reason block
document.querySelectorAll('.reject-btn').forEach((declineBtn, index) => {
    declineBtn.addEventListener('click', () => {
        const reason = document.querySelectorAll('.reason')[index];
        reason.classList.remove('closed');
        reason.classList.add('active');
    });
});

document.querySelectorAll('.close-btn').forEach((closeBtn, index) => {
    closeBtn.addEventListener('click', () => {
        const reason = document.querySelectorAll('.reason')[index];
        reason.classList.remove('active');
        reason.classList.add('closed');
    });
});


// Pass sanitized value from Quill.js to rejectionReason
document.querySelectorAll('.reason__background').forEach((form, index) => {
    form.addEventListener('submit', (event) => {
        event.preventDefault();


        const quillContent = editors[index].quill.root.innerHTML.trim();
        const sanitizedContent = quillContent.replace(/^<p>|<\/p>$/g, '').trim();

        // Set sanitized content to hidden input
        const rejectionReasonInput = form.querySelector('input[name="rejectionReason"]');
        rejectionReasonInput.value = sanitizedContent;

        form.submit();
    });
});