
//variable decalrations
const track = document.querySelector('.carousel-track');
const slides = Array.from(track.children);
const nextButton = document.querySelector('.carousel-button-right');
const prevButton = document.querySelector('.carousel-button-left');
const dotsNav = document.querySelector('.carousel-nav');
const dots = Array.from(dotsNav.children);

const slideWidth = slides[0].getBoundingClientRect().width;

//arrange the slides next to each other
const setSlidePosition = (slide, index) => {
	slide.style.left = slideWidth * index + 'px';
};

slides.forEach(setSlidePosition);


//move the slide to the appropaite location
const moveToSlide = (track, currentSlide, targetSlide) => {
	track.style.transform = 'translatex(-' + (targetSlide.style.left) + ')';
	currentSlide.classList.remove('current-slide');
	targetSlide.classList.add('current-slide');
};

//shows which nav indicators has been clicked on
const updateDots = (currentDot, targetDot) => {

	 currentDot.classList.remove('current-indicator');
	 targetDot.classList.add('current-indicator');

};

//hide the next/prev buttons when it reaches the start and end
const hideNavButtons = (slides, prevButton, nextButton, targetIndex) => {
	 if(targetIndex === 0) {
		prevButton.classList.add('is-hidden');
	 	nextButton.classList.remove('is-hidden');
	 } else if(targetIndex === slides.length - 1) {
	 	nextButton.classList.add('is-hidden');
	 	prevButton.classList.remove('is-hidden');
	 } else {
	 	nextButton.classList.remove('is-hidden');
	 	prevButton.classList.remove('is-hidden');
	 }
};


//when I click right, move slides to the right
nextButton.addEventListener('click', e => {
	const currentSlide = track.querySelector('.current-slide');
	const nextSlide = currentSlide.nextElementSibling;
	const currentDot = dotsNav.querySelector('.current-indicator');
	const nextDot = currentDot.nextElementSibling;
	const nextIndex = slides.findIndex(slide => slide === nextSlide);

	moveToSlide(track, currentSlide, nextSlide);
	updateDots(currentDot, nextDot);
	hideNavButtons(slides, prevButton, nextButton, nextIndex);
	 
});

//when i click left, move slides to the left 
prevButton.addEventListener('click', e =>{
	const currentSlide = track.querySelector('.current-slide');
	const prevSlide = currentSlide.previousElementSibling;
	const currentDot = dotsNav.querySelector('.current-indicator');
	const prevDot = currentDot.previousElementSibling;
	const prevIndex = slides.findIndex(slide => slide === prevSlide);

	moveToSlide(track, currentSlide, prevSlide);
	updateDots(currentDot, prevDot);
	hideNavButtons(slides, prevButton, nextButton, prevIndex);
	
});

//when i click on the nav indicators the slides must move respectfully 
dotsNav.addEventListener('click', e => {
//what indicator was clickk own 
	 const targetDot = e.target.closest('button');

	 if(!targetDot) return;

	 const currentSlide = track.querySelector('.current-slide');
	 const currentDot = dotsNav.querySelector('.current-indicator');
	 const targetIndex = dots.findIndex(dot => dot === targetDot);
	 const targetSlide = slides[targetIndex];

	 moveToSlide(track, currentSlide, targetSlide);
	 updateDots(currentDot, targetDot);
	 hideNavButtons(slides, prevButton, nextButton, targetIndex);
});