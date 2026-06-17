window.onload = function(){
    const slides = document.querySelectorAll(".slide");
    let actual = 0;
    setInterval(function(){
        slides[actual].classList.remove("activo");
        actual++;
        if(actual >= slides.length){
            actual = 0;
        }
        slides[actual].classList.add("activo");
    }, 3000);
}
const slides = document.querySelectorAll(".slide");
if (slides.length > 0) {
    let actual = 0;
    setInterval(() => {
        slides[actual].classList.remove("activo");
        actual++;
        if (actual >= slides.length) {
            actual = 0;
        }
        slides[actual].classList.add("activo");
    }, 3000);
}
