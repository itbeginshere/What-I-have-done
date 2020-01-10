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
						<form action="../INC/blog_ctrls_inc.php" method="post">
							<?php 

							if(isset($_POST['btn_blog_crtls'])){
								
							$blg_code = "";
							$blg_title = "";
							$blg_author = "";
							$blg_doc = "";

							echo '<div class="text-box-strip">
								<input type="text" name="blog_code" placeholder="BLOG_CODE" value='.$blg_code.'>
								<input type="text" name="blog_title" placeholder="BLOG_TITLE" value='.$blg_title.'>
								<input type="text" name="blog_author" placeholder="BLOG_AUTHOR" value='.$blg_author.'>
								<input type="text" name="blog_doc" placeholder="BLOG_DOC" value='.$blg_doc.'>
							</div>
							
							<div class="button-strip">
								<input type="submit" name="btn_blog_get" value="get">
								<input type="submit" name="btn_blog_add" value="add">
								<input type="submit" name="btn_blog_update" value="update">
								<input type="submit" name="btn_blog_remove" value="remove">
							</div>';

								//error checking
								
								} else {

							$blg_code = "";
							$blg_title = "";
							$blg_author = "";
							$blg_doc = "";
									

								if(isset($_GET['dataFetched'])){
									$blg_code = $_GET['Code'];
									$blg_title = $_GET['Title'];
									$blg_author = $_GET['Author'];
									$blg_doc = $_GET['DocPath'];
								}

							echo '<div class="text-box-strip">
								<input type="text" name="blog_code" placeholder="BLOG_CODE" value='.$blg_code.'>
								<input type="text" name="blog_title" placeholder="BLOG_TITLE" value='.$blg_title.'>
								<input type="text" name="blog_author" placeholder="BLOG_AUTHOR" value='.$blg_author.'>
								<input type="text" name="blog_doc" placeholder="BLOG_DOC" value='.$blg_doc.'>
							</div>
							
							<div class="button-strip">
								<input type="submit" name="btn_blog_get" value="get">
								<input type="submit" name="btn_blog_add" value="add">
								<input type="submit" name="btn_blog_update" value="update">
								<input type="submit" name="btn_blog_remove" value="remove">
							</div>';

								//error checking

							if(isset($_GET['error'])){
								if($_GET['error'] == 'emptyFields'){
									echo '<p class="error-para">You have left empty fields!!!</p>';
								} elseif ($_GET['error'] == 'numeric') {
									echo '<p class="error-para">Author name can not have numbers!!</p>';
								}  elseif ($_GET['error'] == 'sqlErrors') {
									echo '<p class="error-para">SQL query error encountered!!</p>';
								} elseif ($_GET['error'] == 'emptyCode') {
									echo '<p class="error-para">You have left the BLOG_CODE field blank!!</p>';
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

