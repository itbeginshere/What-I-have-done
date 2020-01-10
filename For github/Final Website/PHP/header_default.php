<?php 
	session_start();
 ?>


<!DOCTYPE html>
<html>
	<link rel="stylesheet" type="text/css" href="../CSS/home_page.css"> 
	<link rel="stylesheet" type="text/css" href="../CSS/about_me_page.css">
	<link rel="stylesheet" type="text/css" href="../CSS/blog_page.css">
	<link rel="stylesheet" type="text/css" href="../CSS/store_page.css"> 
	<link rel="stylesheet" type="text/css" href="../CSS/carousel.css">
	<link rel="stylesheet" type="text/css" href="../CSS/header_default.css">
	<link rel="stylesheet" type="text/css" href="../CSS/footer_default.css">
	<link rel="stylesheet" type="text/css" href="../CSS/faq_page.css"> 
	<link rel="stylesheet" type="text/css" href="../CSS/blog_article.css">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width">
		<meta http-equiv="X-UA-Compatible" content="ie=edge">
		<script src="../JS/cart.js" async></script>
		<title>Welcome to the Dram Den!</title>
	</head>
<body>

	<header>
	<div class="container">
		<nav class="navbar">
			<img src="../IMGs/Web_logo.png" class="logo">
			<ul>
				<li><a href="HomePage.php">Home</a></li>
				<li><a href="AboutMe.php">About Me</a></li>
				<li><a href="BlogPage.php">Blog</a></li>
				<li><a href="StorePage.php">Store</a></li>
				<li><a href="FaqPage.php">Faq</a></li>
				<?php 
					if(isset($_SESSION['userId'])){
						echo '<li><a href="profile_control_payments.php">Profile</a></li>';
						echo '<li><a href="../INC/logout_inc.php">Sign Out</a></li>';						
					} else { 
						echo '<li><a href="register_form.php">Sign In/Up</a></li>';
					}
				?>
			</ul>
		</nav>
	</div>
	</header>

