// Create New Business Plan
document.addEventListener("DOMContentLoaded", function() {
    const createNewButton = document.querySelector('.business-plan__create-btn');
    const newPlanCard = document.querySelector('.business-plan__card-new');

    if (createNewButton && newPlanCard) {
        createNewButton.addEventListener('click', function() {
            if (newPlanCard.style.display === "none" || newPlanCard.style.display === "") {
                newPlanCard.style.display = "block";
            } else {
                newPlanCard.style.display = "none";
            }
        });
    } else {
        console.error('Create button or new plan card not found.');
    }
});


// Add more service

document.addEventListener("DOMContentLoaded", function() {
    const addServiceButton = document.querySelector('.business-plan__card-add-service');
    const servicesContainer = document.querySelector('.business-plan__card-services-container');
    let serviceIndex = 1;

    addServiceButton.addEventListener('click', function(event) {
        event.preventDefault();

        const newServiceDiv = document.createElement('div');
        newServiceDiv.classList.add('business-plan__card-service');

        newServiceDiv.innerHTML = `
            Service:
            <select name="serviceList[${serviceIndex}].id">
                <option value="2">Post 2 posts per day</option>
                <option value="4">Post 4 posts per day</option>
                <option value="6">Post 8 posts per day</option>
                <option value="8">Post 16 posts per day</option>
                <option value="10">Post 32 posts per day</option>
            </select>
        `;

        servicesContainer.appendChild(newServiceDiv);
        serviceIndex++;
    });
});



//Click edit to edit content
document.querySelectorAll('.business-plan__card-edit').forEach(editBtn => {
    editBtn.addEventListener('click', function (e) {
        e.preventDefault();

        const card = this.closest('.business-plan__card');
        const form = card.querySelector('.business-plan__card-form');

        form.querySelectorAll('h4').forEach(h4 => {
            const input = h4.querySelector('input');
            if (input) {
                h4.style.display = 'block'; 
            }
        });

        form.querySelectorAll('h4:not(:has(input))').forEach(h4 => {
            h4.style.display = 'none';  
        });

        form.querySelectorAll('input, textarea, select').forEach(input => {
            input.style.display = 'inline'; 
        });

        form.querySelectorAll('span').forEach(span => {
            span.style.display = 'none';  
        });

        form.querySelector('.business-plan__card-new-btn').style.display = 'block';

        card.querySelector('.business-plan__card-modify').style.display = 'none';
    });
});

