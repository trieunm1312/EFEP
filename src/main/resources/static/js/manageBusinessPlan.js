// Create New Business Plan
document.addEventListener("DOMContentLoaded", function() {
    // Function to get the URL parameter
    function getUrlParameter(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    const newPlan = getUrlParameter('newPlan');
    console.log("newPlan parameter value: ", newPlan);

    const createNewButton = document.querySelector('.business-plan__create-btn');
    const newPlanCard = document.querySelector('.business-plan__card-new');

    //Create new plan auto
    if (newPlan === 'true') {
        console.log("newPlan is true, showing the card");

        if (newPlanCard) {

            newPlanCard.style.display = "block";
        } else {
            console.error('New plan card not found.');
        }


        if (createNewButton) {
            createNewButton.addEventListener('click', function() {
                if (newPlanCard.style.display === "none" || newPlanCard.style.display === "") {
                    newPlanCard.style.display = "block";
                } else {
                    newPlanCard.style.display = "none";
                }
            });
        } else {
            console.error('Create button not found.');
        }
    } else {
        console.log('newPlan parameter is not true or missing.');
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
    const addServiceButtons = document.querySelectorAll('.business-plan__card-more-service');

    addServiceButtons.forEach((addServiceButton) => {
        const servicesContainer = addServiceButton.closest('.business-plan__card').querySelector('.business-plan__card-services-box');

        addServiceButton.addEventListener('click', function(event) {
            event.preventDefault();

            // Create new div for the service
            const newServiceDiv = document.createElement('div');
            newServiceDiv.classList.add('business-plan__card-service');

            // Create a new select element
            const selectElement = document.createElement('select');
            const newIndex = servicesContainer.querySelectorAll('select').length; // Ensure a flat index
            selectElement.name = `businessServiceList[${newIndex}]`; // Use a flat index for the list

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

