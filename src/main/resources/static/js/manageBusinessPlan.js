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

    if (addServiceButton) {
        addServiceButton.addEventListener('click', function(event) {
            event.preventDefault();

            // Create new div for the service
            const newServiceDiv = document.createElement('div');
            newServiceDiv.classList.add('business-plan__card-service');

            // Create a new select element
            const selectElement = document.createElement('select');
            const newIndex = servicesContainer.children.length; // Increment index based on number of existing services
            const selectName = `businessServiceList[${newIndex}].id`;
            selectElement.name = selectName;

            // Add default "Select a service" option
            const defaultOption = document.createElement('option');
            defaultOption.value = "";
            defaultOption.textContent = "Select a service";
            selectElement.appendChild(defaultOption);

            // Populate the select element with service options
            serviceList.forEach(service => {
                const option = document.createElement('option');
                option.value = service.id;
                option.textContent = service.name;
                selectElement.appendChild(option);
            });

            // Append select element to new div and the container
            newServiceDiv.appendChild(selectElement);
            servicesContainer.appendChild(newServiceDiv);
        });
    }
});

// Add more service
document.addEventListener("DOMContentLoaded", function() {
    const addServiceButton = document.querySelector('.business-plan__card-more-service');
    const servicesContainer = document.querySelector('.business-plan__card-services-box');

    if (addServiceButton) {
        addServiceButton.addEventListener('click', function(event) {
            event.preventDefault();

            // Create new div for the service
            const newServiceDiv = document.createElement('div');
            newServiceDiv.classList.add('business-plan__card-service');

            // Create a new select element
            const selectElement = document.createElement('select');
            const newIndex = servicesContainer.children.length; // Update index
            selectElement.name = `businessServiceList[${newIndex}].id`;

            // Add default "Select a service" option
            const defaultOption = document.createElement('option');
            defaultOption.value = "";
            defaultOption.textContent = "Select a service";
            selectElement.appendChild(defaultOption);

            // Populate the select element with service options
            serviceList.forEach(service => {
                const option = document.createElement('option');
                option.value = service.id;
                option.textContent = service.name;
                selectElement.appendChild(option);
            });

            // Append select element to new div and the container
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

        form.querySelectorAll('input, textarea, select, label, button').forEach(input => {
            input.style.display = 'inline';
        });

        form.querySelectorAll('span').forEach(span => {
            span.style.display = 'none';
        });

        form.querySelector('.business-plan__card-new-btn').style.display = 'block';

        card.querySelector('.business-plan__card-modify').style.display = 'none';
    });
});

