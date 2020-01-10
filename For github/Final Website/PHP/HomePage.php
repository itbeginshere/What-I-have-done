
	<?php
	require "header_default.php";
	?>

	<main>
	
	<section class="home-row-1">
		<div class="skewed-1"></div>
		<div class="row-1-content">
			<h1>Welcome to Dalindo's Dram Den</h1>

			<p>
				Let us be the first to welcome you to only place that can source any whisky you have on your mind. Here you will be able to request and find whiskies from common to rare, just at the click of a button. Enjoy your stay at the Dram Den.
			</p>

			<div class="quick-links">
				<ul>
					<li><a href="AboutMe.php">Learn who I am</a></li>	
					<li><a href="StorePage.php">Enter the Dram Den</a></li>
				</ul>	

			</div>

		</div>       
	</section>

	<section class="home-row-2">
		<div class="skewed-2"></div>
		<div class="row-2-content">
		<div class="carousel-container">
		<div class="carousel">
			<?php  
				require '../INC/carousel_imports_inc.php';
			?>
		</div>
		</div>
		<div class="carousel-description">
			<h2>Newly added stock</h2>
			<p>
				Here are all the new whiskies we have sourced from far and wide for you. We more than often get 'hidden gems' that you cannot find anywhere, but it is all about looking in the right places. Every week we make sure to have new stock, be sure to visit the website daily if you are not already signed up to the newsletter. Who knows? We might have that one whiskey you have been searching for your entire life.
			</p>

		</div>

		</div>



	</section>

	<section class="home-row-3">
		<div class="skewed-3"></div>
		<div class="row-3-content">
			
			<div class="store-div">

				<h3 class="row-3-heading row-3-text">browse the store</h3>

				<p  class="row-3-paragraph row-3-text">Take a look at our collection of whiskies. We stock any thing from common to rare...If you can think of it, we can and will stock it.</p>
				<div>
				<img  class="row-3-image"src="../IMGs/Web_Logo.png"> </div>
				<div class="row-3-link">
				<a href="StorePage.php">Enter the dram den</a>
				</div>
				

		
			</div>

			<div class="blog-div">
				
				<h3 class="row-3-heading row-3-text">read up on my thoughts</h3>

				<p  class="row-3-paragraph row-3-text">Every now and then i will open a new bottle of whisky to try it out and i will review it myself. Come and see what i have to say about your favourite whisky</p>

				<div>
				<img  class="row-3-image"src="../IMGs/Blog_Icon.png"> 
				</div>

				<div class="row-3-link">	
				<a href="BlogPage.php">Enter my mind</a>
				</div>
				
				
			
			</div>

		
		</div>
		 
	</section>

	<section class="home-row-4">
		<div class="skewed-4"></div>
	
		<div class="row-4-content">
			<div class="row-4-section">
				<form class="form-subscribe" action="../INC/newsletter_form_inc.php" method="post">
						<label for="home-4-fName">First Name</label>
						<input type="text" id="home-4-fName" placeholder="Enter your name..."  class="text-input contact-input" name="news_f_name">
						<label for="home-4-email">Email Address</label>
						<input type="email"  id="home-4-email" placeholder="Enter your email..."  class="text-input contact-input" name="news_mail">
						<?php 

						if(isset($_GET['error'])){
							if($_GET['error'] == 'news_emptyFields'){
								echo '<p class="error-para">You have left some fields empty!!!</p>';
							} elseif($_GET['error'] == 'news_digitInc'){
								echo '<p class="error-para">Your first name cannot contain numbers!!!</p>';
							} elseif($_GET['error'] == 'news_specailInc'){
								echo '<p class="error-para">Your first name cannot contain special characters!!!</p>';
							} elseif($_GET['error'] == 'news_invalidEmail'){
								echo '<p class="error-para">Invalid Email!!<br>Example: jowSmith@gmail.com</p>';
							} elseif($_GET['error'] == 'news_emailAlreadyInUse'){
								echo '<p class="error-para">Email is already in use</p>';
							}
						} elseif (isset($_GET['subscribed'])){
							echo '<p class="success-para">You have been subscribed to the newsletter</p>';
						} else {
							
						}
							
						?>

						<input type="submit" class="btn btn-big" name="btn_news_sign_up" value="subscribe">
						
						
				</form>			
    		
   		 	</div>
		
			<div class="row-4-section">
				<h1>be one of the first to know</h1>
				
				<p>
					Subscibe to the Dram Den newsletter to be kept in the loop for all the new and rare whiskies that will be uploaded to the site. Stay one step ahead of others, by knwoing when the new stock will be uploaded for purchase.
				</p>

				<h1>stay ahead of the game<br><span>subscribe</span> today!</h1>

			</div>

		</div>
	</section>


	<script type="text/javascript" src="../JS/carousel.js"></script>
	</main>



	<?php 

		require "footer_default.php";

	?>

