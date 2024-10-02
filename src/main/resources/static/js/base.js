document.addEventListener('DOMContentLoaded', () => {
    const searchIcon = document.querySelector('.header__item-icon-search');
    const searchBox = document.querySelector('.header__item-search-box');
    const userIcon = document.querySelector('.header__item-icon-user');
    const accountBox = document.querySelector('.header__item-account');

    // Toggle search box visibility on click
    searchIcon.addEventListener('click', (e) => {
        e.stopPropagation();

        // Hide the account box if it's open
        if (accountBox.style.display === 'block') {
            accountBox.style.display = 'none';
        }

        // Toggle between 'none' and 'flex' display values for the search box
        if (searchBox.style.display === 'flex') {
            searchBox.style.display = 'none';
        } else {
            searchBox.style.display = 'flex';
        }
    });

    // Toggle account box visibility on click
    userIcon.addEventListener('click', (e) => {
        e.stopPropagation();

        // Hide the search box if it's open
        if (searchBox.style.display === 'flex') {
            searchBox.style.display = 'none';
        }

        // Toggle between 'none' and 'block' display values for the account box
        if (accountBox.style.display === 'block') {
            accountBox.style.display = 'none';
        } else {
            accountBox.style.display = 'block';
        }
    });

    // Hide both boxes when clicking outside
    document.addEventListener('click', () => {
        searchBox.style.display = 'none';
        accountBox.style.display = 'none';
    });

    // Prevent hiding when interacting with the search box
    searchBox.addEventListener('click', (e) => {
        e.stopPropagation();
    });

    // Prevent hiding when interacting with the account box
    accountBox.addEventListener('click', (e) => {
        e.stopPropagation();
    });
});

document.querySelectorAll('.header__item-megamenu-item__category').forEach(item => {
    item.addEventListener('mouseenter', () => {
        item.querySelector('.category__block').style.display = 'block';
    });
    item.addEventListener('mouseleave', () => {
        item.querySelector('.category__block').style.display = 'none';
    });
});
