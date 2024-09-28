// Get elements for the grid and list icons
const gridIcon = document.querySelector('.grid-icon');
const listIcon = document.querySelector('.list-icon');

// Add click event listener to the grid icon
gridIcon.addEventListener('click', function() {
    // Ensure grid icon is active
    gridIcon.classList.add('active');
    listIcon.classList.remove('active');
    
    // Ensure only the grid is visible
    document.querySelector('.flower__grid').classList.add('active-view');
    document.querySelector('.flower-list').classList.remove('active-view');
});

// Add click event listener to the list icon
listIcon.addEventListener('click', function() {
    // Ensure list icon is active
    listIcon.classList.add('active');
    gridIcon.classList.remove('active');
    
    // Ensure only the list is visible
    document.querySelector('.flower-list').classList.add('active-view');
    document.querySelector('.flower__grid').classList.remove('active-view');
});

//Submit delete
function submitForm() {
    document.getElementById("flowerForm").submit();
}
