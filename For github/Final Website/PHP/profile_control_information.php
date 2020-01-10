<?php 
	session_start();
?>

<!DOCTYPE html>
<html>
	<link rel="stylesheet" type="text/css" href="../CSS/profile_page.css">
	<head>
		<title>PROFILE CONTROLS</title>
	</head>

	<body>

		<main>
			<div class="center-container">
				
				<div class="features-container">
					<div class="form-controls">
						<form action="profile_control_payments.php" method="post">
							<input type="submit" name="btn_pay_crtls" value="Payment">
						</form>
						<form action="profile_control_address.php" method="post">
							<input type="submit" name="btn_adr_ctrls" value="Address">
						</form>
						<form action="profile_control_purchases.php" method="post">		
							<input type="submit" name="btn_pur_crtls" value="History">
						</form>
						<form action="profile_control_information.php" method="post">		
							<input type="submit" name="btn_info_crtls" value="Info">
						</form>
						<?php 
							if(isset($_SESSION['userName'])){


								if ($_SESSION['userName'] == 'Admin'){
								echo 
									'
										<form action="admin_control_store.php" method="post">		
											<input type="submit" name="btn_stock_crtls" value="Controls">
										</form>
									'
								;}
							}
						?>
					
					</div>
				</div>

				<div class="components-container">
					<div class="form-components">
						<form action="../INC/information_ctrls_inc.php" method="post"> 
							<?php 
								require '../INC/information_on_load_inc.php';

	
							if(isset($_GET['error'])){
								 
								if($_GET['error'] == 'emptyFields'){
									echo '<p class="error-para">You have left empty fields!!!</p>';
								}  elseif ($_GET['error'] == 'digitOrSpecialF'){
									echo '<p class="error-para">First name cannot have digits or special characters!!!</p>'; 
								}  elseif ($_GET['error'] == 'digitOrSpecialL'){
									echo '<p class="error-para">Last name cannot have digits or special characters!!!</p>'; 
								} elseif ($_GET['error'] == 'invalidEmail'){
									echo '<p class="error-para">Invalid email!!! <br> Example: joeSmith@gmail.com</p>'; 
								} elseif ($_GET['error'] == 'sqlErrors') {
									echo '<p class="error-para">SQL query error encountered!!</p>';
								} elseif ($_GET['error'] == 'emptyName') {
									echo '<p class="error-para">SQL query error encountered!!</p>';
								}  elseif ($_GET['error'] == 'Exists') {
									echo '<p class="error-para">You already have a payment method!!</p>';
								} 

							}

							if(isset($_GET['success'])){
								if($_GET['success'] == 'Removed'){
									echo '<p class="notif-para">Entry has been removed!!</p>';
								} elseif($_GET['success'] == 'Updated'){
									echo '<p class="notif-para">Entry has been updated!!</p>';
								} 
							}

							?>
						</form>
					</div>

				</div>


			</div>
		</main>
	<div class="home-button">
		<input type="button" class="btn_home" onclick="location.href='HomePage.php';" value="home">		
	</div>

	</body>

</html>

