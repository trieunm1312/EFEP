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

    // Inject serviceList as a JavaScript array
    const serviceList = /*[[${msg.serviceList}]]*/ [];
    console.log('Service List:', serviceList);  // Debugging: Check if this prints correct data



    document.addEventListener("DOMContentLoaded", function() {
    const addServiceButton = document.querySelector('.business-plan__card-add-service');
    const servicesContainer = document.querySelector('.business-plan__card-services-container');

    let serviceIndex = 1;  // Start with index 1 since the first select is already displayed

    // Debug: Log serviceList data
    console.log('Service List at Runtime:', serviceList);

    addServiceButton.addEventListener('click', function(event) {
    event.preventDefault();

    // Create a new div for the service select
    const newServiceDiv = document.createElement('div');
    newServiceDiv.classList.add('business-plan__card-service');

    // Create the select tag with a unique name based on the service index
    const newSelect = document.createElement('select');
    newSelect.name = `businessServiceList[${serviceIndex}].id`;  // Use unique name for each select

    // Check if serviceList is populated with options
    if (serviceList.length > 0) {
    // Populate the select options using the serviceList array
    serviceList.forEach(service => {
    const option = document.createElement('option');
    option.value = service.id;  // Set option value
    option.text = service.name; // Set option text
    newSelect.appendChild(option);  // Append option to the select
});
} else {
    console.error('No services available in serviceList');
}

    // Append the select to the new div
    newServiceDiv.innerHTML = `Service: `;
    newServiceDiv.appendChild(newSelect);

    // Append the new div to the services container
    servicesContainer.appendChild(newServiceDiv);

    // Increment the index for the next service
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

