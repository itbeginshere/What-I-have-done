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
						<form action="" method="">
							<table>
							<tr>
								<th>trans_num</th>
								<th>item_name</th>
								<th>datetime_bought</th>
								<th>amt_bought</th>
								<th>item_price</th>
							</tr>

							<?php 
						
								require '../INC/purchases_ctrls_inc.php';

							?>

						
						</table>

					

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

