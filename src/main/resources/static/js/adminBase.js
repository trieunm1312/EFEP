// Click sidebar item keep css
const sidebarLinks = document.querySelectorAll('.sidebar__link');

sidebarLinks.forEach(link => {
    link.addEventListener('click', function(event) {
        event.preventDefault();  

        const currentActive = document.querySelector('.sidebar__item.active');
        if (currentActive) {
            currentActive.classList.remove('active');
        }

        this.querySelector('.sidebar__item').classList.add('active');
    });
});


// Click show the create block
const createBtn = document.querySelector('.header__create-btn');
const createBlock = document.querySelector('.header__create-block');


createBtn.addEventListener('click', function(event) {
    event.stopPropagation(); 
    createBlock.style.display = (createBlock.style.display === 'block') ? 'none' : 'block';
});


document.addEventListener('click', function(event) {
    if (!createBtn.contains(event.target)) {
        createBlock.style.display = 'none';  
    }
});