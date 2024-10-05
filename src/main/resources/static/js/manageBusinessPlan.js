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
    console.log(addServiceButton);
    const servicesContainer = document.querySelector('.business-plan__card-services-container');

    if (addServiceButton) {
        addServiceButton.addEventListener('click', function(event) {
            event.preventDefault();
            console.log('Button clicked!'); // Check if the click event is firing

            const newServiceDiv = document.createElement('div');
            newServiceDiv.classList.add('business-plan__card-service');

            const selectElement = document.createElement('select');
            const selectName = `businessServiceList[${servicesContainer.children.length}].id`;
            selectElement.name = selectName;

            serviceList.forEach(service => {
                const option = document.createElement('option');
                option.value = service.id;
                option.textContent = service.name;
                selectElement.appendChild(option);
            });

            newServiceDiv.innerHTML = 'Service: ';
            newServiceDiv.appendChild(selectElement);

            servicesContainer.appendChild(newServiceDiv);
        });
    }
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

