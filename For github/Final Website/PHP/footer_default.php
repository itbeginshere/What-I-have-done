<footer>
		<div class="footer-content">
			<div class="footer-section">
				<div class="footer-intro">
					<h3>DaLindso's Dram Den</h3>

					<p class="footer-para"> 
						Weldome to the Dram Den! I distribute Japanese, Scottish, South African, American, British, French, Canadian, Irish as well as Indian Whiskies. Any product varying from a Bourbon,
						a single malt, a single barrel, a blended malt, a pure malt to any cask flavoured blends. Whatever you desire i will find it for you. 
						
					</p>

					<p class="footer-para">Learn more about me --<a href="AboutMe.php">here!</a>--</p>

					<p class="footer-para">If you are getting cold feet about my reputable sourcing abilities, please review our --<a href="AboutMe.php">affliations</a>-- .</p>

				</div>
			</div>
			<div class="footer-section">
				<div class="footer-help">
					<h3>Help & FAQs</h3>

					<span>Help</span>

					<p class="footer-para">If you are new to the site and up for self learning, please make use of the avaiable User Guide for this specific website provided in the following link --<a target="_blank" href="../MISC/ITEC301 Deliverable 3 User Manual.pdf">User Guide</a>--. This is not necessary, only for those who wish to learn the in's and out's of what the site has to offer.</p>

					<span>FAQs</span>

					<p class="footer-para"> If you uncover any issues or misunderstandings please vist the --<a href="../PHP/FaqPage.php">FAQs</a>-- page. If you have any specific inquiries or requests, please use the contact me form at the bottom of the website</p>

				</div>
			</div>
			<div class="footer-section">
				<h3>Contact Us</h3>	
					<br>	
					<form class="contact-form" action="../INC/contact_form_inc.php" method="post">
						<input type="email" name="contact_email" placeholder="Enter your email..."  class="text-input contact-input">
						<textarea name="contact_message" class="text-input contact-input" placeholder="Enter your message..."></textarea>
						<input type="submit" class="btn btn-big" name="btn_mess_send" value="send">
						<?php
							$web_page = basename($_SERVER['PHP_SELF']);
							echo '<input type="text" name="web_page" value='.$web_page.'>';	
						?>

						<?php 
							if(isset($_GET['error'])){
								if($_GET['error'] == 'con_emptyFields'){
									echo "<p class='error-para'>You have left fields empty!!!</p>";
								} else if($_GET['error'] == 'con_invalidEmail'){
									echo '<p class="error-para">Invalid Email!!<br>Example: jowSmith@gmail.com</p>';
								}
							} elseif(isset($_GET['sent'])) {
								if($_GET['sent'] == 'success'){
									echo "<p class='nottif-para'>I will get to you as soon as possible!</p>";
								} 
							}

						?>

						
					</form>

			</div>
		</div>
		
		<div class="footer-copyright">
			&copy;2019 DaLindso's Dram Den | Designed by Matthew Birkholtz RWNFVCVW4
		</div>
	</footer>

	</body>
</html>