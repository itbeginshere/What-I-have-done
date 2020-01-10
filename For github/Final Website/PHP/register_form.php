<?php 
session_start();
?>
<!DOCTYPE html>
<html>
<head>
	<title>Regitser Form</title>

</head>
<body>
<main>

<div class="content-container">
	
		<form class="login-form" action="../INC/login_form_inc.php" method="post">
			<h1>Returning? Do so here!</h1>

			<?php 

				if(isset($_GET['error'])){
					if($_GET['error'] == 'invalidUserNameOrPassword'){
						echo '<p class="error-para">Invald username and/or password!!!</p>';	
					}					
				}
			?>

			<div>
				<input type="text" name="frm_log_user_name" placeholder="Enter your user name...">
			</div>

			<div>
				<input type="password" name="frm_log_password" placeholder="Enter your password...">	
			</div>

			<div class="submit-button">
				<input type="submit" name="btn_log_in" value="sign in!">				
			</div>

			<?php 

				if(isset($_GET['signUp'])){
			 		if($_GET['signUp'] == 'success'){
					echo '<p class="notif-para">Your account was created successfully!<br>Sign In with your details above</p>';			
					}		
				} 
			?>
		</form>

		<form class="register-form" action="../INC/register_form_inc.php" method="post">
			<h1>New? Sign up here!</h1>
		
			<?php 
				if(isset($_GET['error'])){
					if($_GET['error'] == 'emptyFields'){
						echo '<p class="error-para">You have left text fields empty!!!</p>'; 
					} elseif ($_GET['error'] == 'digitOrSpecialF'){
						echo '<p class="error-para">First name cannot have digits or special characters!!!</p>'; 
					}  elseif ($_GET['error'] == 'digitOrSpecialL'){
						echo '<p class="error-para">Last name cannot have digits or special characters!!!</p>'; 
					}  elseif ($_GET['error'] == 'invalidEmail'){
						echo '<p class="error-para">Invalid email!!! <br> Example: joeSmith@gmail.com</p>'; 
					} elseif ($_GET['error'] == 'invalidUserName'){
						echo '<p class="error-para">Invalid username!!! <br> Must conatin one upper case letter, one lower case letter and one number</p>'; 
					} elseif ($_GET['error'] == 'invalidPassword'){
						echo '<p class="error-para">Invalid password!!! <br> Must be longer than eight characters</p>'; 
					} elseif ($_GET['error'] == 'passwordMismatch'){
						echo '<p class="error-para">Password and confirm password do not match!!!</p>'; 
					} elseif ($_GET['error'] == 'userNameALreadyExists'){
						echo '<p class="error-para">User name already exists!!!</p>'; 
					} 
				}

			?>

			<div>
				<input type="text" name="frm_sign_first_name" placeholder="Enter your first name...">
			</div>
			
			<div>
				<input type="text" name="frm_sign_last_name" placeholder="Enter your last name...">
			</div>
			
			<div>
				<input type="text" name="frm_sign_user_name" placeholder="Enter your user name...">
			</div>
			
			<div>
				<input type="email"  name="frm_sign_email" placeholder="Enter your email...">
			</div>
			
			<div>
				<input type="password" name="frm_sign_password" placeholder="Enter your password...">
			</div>
			
			<div>
				<input type="password" name="frm_sign_password_confirm" placeholder="Re-Enter your password...">
			</div>

			<div class="submit-button">
				<input type="submit" name="btn_sign_up" value="sign up!">
			</div>
		</form>
	</div>

	
</main>

	<div class="home-button">
		<input type="button" class="btn_sign_up" onclick="location.href='HomePage.php';" value="home">		
	</div>

</body>

	<link rel="stylesheet" type="text/css" href="../CSS/register_page.css">
</html>