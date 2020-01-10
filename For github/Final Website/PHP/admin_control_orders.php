<?php 
	session_start();
?>

<!DOCTYPE html>
<html>
	<link rel="stylesheet" type="text/css" href="../CSS/admin_control_store.css">
	<head>
		<title>ADMIN CONTROLS</title>
	</head>

	<body>

		<main>
			<div class="center-container">
				
				<div class="features-container">
					<div class="form-controls">
						<form action="admin_control_store.php" method="post">
							<input type="submit" name="btn_store_crtls" value="Store">
						</form>
						<form action="admin_control_blog.php" method="post">		
							<input type="submit" name="btn_blog_crtls" value="Blog">
						</form>
						<form action="admin_control_stock.php" method="post">		
							<input type="submit" name="btn_stock_crtls" value="Stock">
						</form>
						<form action="admin_control_orders.php" method="post">
							<input type="submit" name="btn_ord_crtls" value="Orders">
						</form>
						<form action="profile_control_payments.php" method="post">
							<input type="submit" name="btn_pay_crtls" value="Profile">
						</form>
					
					</div>
				</div>

				<div class="components-container">
					<div class="form-components">
						<table>
							<tr>
								<th>User</th>
								<th>Product</th>
								<th>Qauntity</th>
								<th>Billing 1</th>
								<th>Billing 2</th>
								<th>Poastel Code</th>
								<th>City</th>
								<th>Province</th>
							</tr>

							<?php 

								require '../INC/orders_ctrls_inc.php';

							?>

						
						</table>
					</div>
				</div>


			</div>
		</main>
	<div class="home-button">
		<input type="button" class="btn_home" onclick="location.href='HomePage.php';" value="home">		
	</div>

	</body>

</html>

