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
						<form action="../INC/store_ctrls_inc.php" method="post">
							<?php 

							if(isset($_POST['btn_store_crtls'])){
								
								$str_code = "";
								$str_name = "";
								$str_from = "";
								$str_img = "";
								$str_price = "";
								$str_quan = "";
								$str_back = "";
								$str_state_f = 0;
								$str_state_t = 0;
								$str_state_s = 0;
								$select_american = "selected";
								$select_irish = "";
								$select_scot = "";
								$select_south = "";
								$select_france = "";
								$select_japanese = "";
								$checked_f = "";
								$checked_t = "";
								$checked_s = "";
								$checked_n = "checked";

								echo '<div class="text-box-strip">
								<input type="text" name="sck_code" placeholder="STOCK_CODE" value='.$str_code.'>
								<input type="text" name="sck_name" placeholder="STOCK_NAME" value='.$str_name.'>
								<select name="sck_from" id="sck_from">
									<option '.$select_american.'>American</option>
									<option '.$select_irish.'>Irish</option>
									<option '.$select_scot.'>Scotland</option>
									<option '.$select_south.'>South African</option>
									<option '.$select_france.'>France</option>
									<option '.$select_japanese.'>Japanese</option>
								</select>
								<input type="text" name="sck_price" placeholder="STOCK_PRICE" value='.$str_price.'>
								<input type="text" name="sck_img" placeholder="STOCK_IMG" value='.$str_img.'>
								<input type="text" name="sck_quan" placeholder="STOCK_QUANTITY" value='.$str_quan.'>
								<input type="text" name="sck_back" placeholder="STOCK_BACKORDER" value='.$str_back.'>
							</div>

							<div class="state-strip">
								<label><input type="radio" name="sck_state" id"sck_state_f" value="featured" '.$checked_f.'>featured</label>
								<label><input type="radio" name="sck_state" id"sck_state_t" value="trending" '.$checked_t.'>trending</label>
								<label><input type="radio" name="sck_state" id"sck_state_s" value="sale" '.$checked_s.'>sale</label> 
								<label><input type="radio" name="sck_state" id"sck_state_n" value="none" '.$checked_n.'>none</label> 
							</div>

							<div class="button-strip">
								<input type="submit" name="btn_store_get" value="get">
								<input type="submit" name="btn_store_add" value="add">
								<input type="submit" name="btn_store_update" value="update">
								<input type="submit" name="btn_store_remove" value="remove">
								
							</div>';
								
								} else {
								
								$str_code = "";
								$str_name = "";
								$str_from = "";
								$str_img = "";
								$str_price = "";
								$str_quan = "";
								$str_back = "";
								$str_state_f = 0;
								$str_state_t = 0;
								$str_state_s = 0;
								$select_american = "selected";
								$select_irish = "";
								$select_scot = "";
								$select_south = "";
								$select_france = "";
								$select_japanese = "";
								$checked_f = "";
								$checked_t = "";
								$checked_s = "";
								$checked_n = "checked";


							if(isset($_GET['dataFetched'])){
								$str_code = $_GET['Code'];
								$str_name = $_GET['Name'];
								$str_from = $_GET['From'];
								$str_img = $_GET['ImagePath'];
								$str_price = $_GET['Price'];
								$str_quan = $_GET['Quan'];
								$str_back = $_GET['Back'];
								$str_state_f = $_GET['Featured'];
								$str_state_t = $_GET['Trending'];
								$str_state_s = $_GET['Sale'];

								if($str_from == "American"){
									$select_american = "selected";
								} elseif ($str_from == "Irish"){
									$select_irish = "selected";
								} elseif($str_from == "Scotland"){
									$select_scot = "selected";
								} elseif($str_from == "South_African"){
									$select_south = "selected";
								} elseif($str_from == "France"){
									$select_france = "selected";
								} elseif($str_from == "Japanese"){
									$select_japanese = "selected";
								} else {
									$select_american = "selected";
								}
			

								if($str_state_f == 1){
									$checked_f = "checked";
									$checked_t = "";
									$checked_s = "";
									$checked_n = "";
								} elseif($str_state_t == 1){
									$checked_t = "checked";
									$checked_f = "";
									$checked_s = "";
									$checked_n = "";
								} elseif($str_state_s == 1){
									$checked_s = "checked";
									$checked_f = "";
									$checked_t = "";
									$checked_n = "";
								} else {
									$checked_n = "checked";
									$checked_f = "";
									$checked_t = "";
									$checked_s = "";
								}

							}

							echo '<div class="text-box-strip">
								<input type="text" name="sck_code" placeholder="STOCK_CODE" value='.$str_code.'>
								<input type="text" name="sck_name" placeholder="STOCK_NAME" value='.$str_name.'>
								<select name="sck_from" id="sck_from">
									<option '.$select_american.'>American</option>
									<option '.$select_irish.'>Irish</option>
									<option '.$select_scot.'>Scotland</option>
									<option '.$select_south.'>South African</option>
									<option '.$select_france.'>France</option>
									<option '.$select_japanese.'>Japanese</option>
								</select>
								<input type="text" name="sck_price" placeholder="STOCK_PRICE" value='.$str_price.'>
								<input type="text" name="sck_img" placeholder="STOCK_IMG" value='.$str_img.'>
								<input type="text" name="sck_quan" placeholder="STOCK_QUANTITY" value='.$str_quan.'>
								<input type="text" name="sck_back" placeholder="STOCK_BACKORDER" value='.$str_back.'>
							</div>

							<div class="state-strip">
								<label><input type="radio" name="sck_state" id"sck_state_f" value="featured" '.$checked_f.'>featured</label>
								<label><input type="radio" name="sck_state" id"sck_state_t" value="trending" '.$checked_t.'>trending</label>
								<label><input type="radio" name="sck_state" id"sck_state_s" value="sale" '.$checked_s.'>sale</label> 
								<label><input type="radio" name="sck_state" id"sck_state_n" value="none" '.$checked_n.'>none</label> 
							</div>

							<div class="button-strip">
								<input type="submit" name="btn_store_get" value="get">
								<input type="submit" name="btn_store_add" value="add">
								<input type="submit" name="btn_store_update" value="update">
								<input type="submit" name="btn_store_remove" value="remove">
							</div>
							';



							if(isset($_GET['error'])){

								if($_GET['error'] == 'emptyFields'){
									echo '<p class="error-para">You have left empty fields!!!</p>';
								} elseif ($_GET['error'] == 'nonNumeric1') {
									echo '<p class="error-para">The price can only be numeric!!</p>';
								} elseif ($_GET['error'] == 'nonNumeric2') {
									echo '<p class="error-para">The quantity can only be numeric!!</p>';
								} elseif ($_GET['error'] == 'nonNumeric3') {
									echo '<p class="error-para">The back order can only be numeric!!</p>';
								} elseif ($_GET['error'] == 'negativeValue1') {
									echo '<p class="error-para">The price can not be negative!!</p>';
								} elseif ($_GET['error'] == 'negativeValue2') {
									echo '<p class="error-para">The quantity can not be negative!!</p>';
								} elseif ($_GET['error'] == 'negativeValue3') {
									echo '<p class="error-para">The back order can not be negative!!</p>';
								} elseif ($_GET['error'] == 'sqlErrors') {
									echo '<p class="error-para">SQL query error encountered!!</p>';
								} elseif ($_GET['error'] == 'emptyCode') {
									echo '<p class="error-para">You have left the STOCK_CODE field blank!!</p>';
								}  elseif ($_GET['error'] == 'ghostCode') {
									echo '<p class="error-para">The code which you provided does not exist!!</p>';
								} elseif ($_GET['error'] == 'Exists') {
									echo '<p class="error-para">The name which you provided already exists!!</p>';
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

							if(isset($_GET['dataFetched'])){
									echo '<p class="notif-para">Entry has been fetched!!</p>';
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

