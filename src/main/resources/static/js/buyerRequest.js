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


//pass value from quill.js to rejectionReason
document.querySelectorAll('.reason__background').forEach((form, index) => {
    form.addEventListener('submit', (event) => {
        event.preventDefault();
        const quillContent = editors[index].quill.root.innerHTML;
        const rejectionReasonInput = form.querySelector('input[name="rejectionReason"]');
        rejectionReasonInput.value = quillContent;
        form.submit();
    });
});