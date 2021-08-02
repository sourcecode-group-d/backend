let mainNav = document.getElementById('menu');
let navBarToggle = document.getElementById('navbarToggle');

navBarToggle.addEventListener('click', () => {
    mainNav.classList.toggle('active');
});