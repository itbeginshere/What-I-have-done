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
							if(isset($_SESSION['userName']) == 'Admin'){
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
						<form action="../INC/address_ctrls_inc.php" method="post">
							
						<?php 
					
								require '../INC/address_on_load_inc.php';

								
							if(isset($_GET['error'])){
								 
								if($_GET['error'] == 'emptyFields'){
									echo '<p class="error-para">You have left empty fields!!!</p>';
								} elseif ($_GET['error'] == 'onlyAplhabet1') {
									echo '<p class="error-para">The city name can only be letters!!</p>';
								} elseif ($_GET['error'] == 'onlyAplhabet2') {
									echo '<p class="error-para">The province name can only be letters!!</p>';
								} elseif ($_GET['error'] == 'onlyDigits') {
									echo '<p class="error-para">The poastel code can only be numeric!!</p>';
								} elseif ($_GET['error'] == 'length1') {
									echo '<p class="error-para">The card code must be three characters long!!</p>';
								} elseif ($_GET['error'] == 'length2') {
									echo '<p class="error-para">The card number must be seventeen characters long!!</p>';
								} elseif ($_GET['error'] == 'Month') {
									echo '<p class="error-para">The expiry month value must bebetween 1 and 12, inclusive!!</p>';
								} elseif ($_GET['error'] == 'Year') {
									echo '<p class="error-para">The expiry year value must be greater than 2000!!</p>';
								} elseif ($_GET['error'] == 'sqlErrors') {
									echo '<p class="error-para">SQL query error encountered!!</p>';
								} elseif ($_GET['error'] == 'emptyName') {
									echo '<p class="error-para">SQL query error encountered!!</p>';
								}  elseif ($_GET['error'] == 'Exists') {
									echo '<p class="error-para">You already have a payment method!!</p>';
								} 

							}

							if(isset($_GET['success'])){
								if($_GET['success'] == 'Added'){
									echo '<p class="notif-para">Entry has been added!!</p>';
								} elseif($_GET['success'] == 'Removed'){
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

