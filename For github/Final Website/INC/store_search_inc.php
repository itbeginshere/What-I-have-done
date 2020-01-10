<?php 

	require 'database_helper_inc.php';

	if(isset($_POST['btn_srch_submit'])){
		
		

		$str_from = $_POST['fil_from'];
		$str_begin_price = $_POST['fil_low_price'];
		$str_end_price = $_POST['fil_high_price'];
		$str_state = $_POST['state'];
		$str_ord = $_POST['order'];
		$str_srch_txt = $_POST['srch_txt'];

		$state_f = 0;
		$state_t = 0;
		$state_s = 0;

		if ($str_state == "Featured"){
			$state_f = 1;
			$state_t = 0;
			$state_s = 0;
		} elseif ($str_state == "Trending"){
			$state_f = 0;
			$state_t = 1;
			$state_s = 0;
		} elseif ($str_state == "Sale"){
			$state_f = 0;
			$state_t = 0;
			$state_s = 1;
		}

	
		if (empty($str_begin_price)){
			$str_begin_price = 0;
		} if (empty($str_end_price)){
			$str_end_price = 99999;
		} if (empty($str_srch_txt)) {
			$str_srch_txt = "Every";
		}
		
		if($str_ord == 'A-Z'){

			if ($str_from == 'Any' &&
				$str_state == 'Any' &&
				$str_srch_txt == "Every"){ 
					$sql = 'SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_price BETWEEN ? AND ? ORDER BY p_name ASC';
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"dd", $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}

			} elseif ($str_from != 'Any' &&
				$str_state == 'Any' &&
				$str_srch_txt == "Every"){
					$sql = 'SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_from =? AND p_price BETWEEN ? AND ? ORDER BY p_name ASC';
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"sdd", $str_from, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}
			} elseif ($str_from != 'Any' &&
				$str_state == 'Any' &&
				$str_srch_txt != "Every"){
					
					$sql = "SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_name LIKE concat('%',?,'%') AND p_from =? AND p_price BETWEEN ? AND ? ORDER BY p_name ASC";
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"ssdd",$str_srch_txt, $str_from, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}
			} elseif ($str_from == 'Any' &&
				$str_state == 'Any' &&
				$str_srch_txt != "Every"){
					
					$sql = "SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_name LIKE concat('%',?,'%') AND p_price BETWEEN ? AND ? ORDER BY p_name ASC";
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"sdd", $str_srch_txt, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}
			} elseif ($str_from == 'Any' &&
				$str_state != 'Any' &&
				$str_srch_txt == "Every"){
					$sql = 'SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_featured =? AND p_trending =? AND p_sale =? AND p_price BETWEEN ? AND ? ORDER BY p_name ASC';
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"ddddd", $state_f, $state_t, $state_s, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}
			} elseif ($str_from != 'Any' &&
				$str_state != 'Any' &&
				$str_srch_txt == "Every"){
					$sql = 'SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_from =? AND p_featured =? AND p_trending =? AND p_sale =? AND p_price BETWEEN ? AND ? ORDER BY p_name ASC';
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"sddddd", $str_from, $state_f, $state_t, $state_s, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);

					}
			} elseif ($str_from == 'Any' &&
				$str_state != 'Any' &&
				$str_srch_txt != "Every"){
					$sql = "SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_featured =? AND p_trending =? AND p_sale =? AND p_name LIKE concat('%',?,'%') AND p_price BETWEEN ? AND ? ORDER BY p_name ASC";
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"dddsdd", $state_f, $state_t, $state_s, $str_srch_txt, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}
			} elseif ($str_from != 'Any' &&
				$str_state != 'Any' &&
				$str_srch_txt != "Every"){
					$sql = "SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_name LIKE concat('%',?,'%') AND p_from =? AND p_featured =? AND p_trending =? AND p_sale =? AND p_price BETWEEN ? AND ? ORDER BY p_name ASC";
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"ssddddd", $str_srch_txt, $str_from, $state_f, $state_t, $state_s, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);

					}

			}



		} else {

			if ($str_from == 'Any' &&
				$str_state == 'Any' &&
				$str_srch_txt == "Every"){ 
					$sql = 'SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_price BETWEEN ? AND ? ORDER BY p_name DESC';
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"dd", $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}

			} elseif ($str_from != 'Any' &&
				$str_state == 'Any' &&
				$str_srch_txt == "Every"){
					$sql = 'SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_from =? AND p_price BETWEEN ? AND ? ORDER BY p_name DESC';
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"sdd", $str_from, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}
			} elseif ($str_from != 'Any' &&
				$str_state == 'Any' &&
				$str_srch_txt != "Every"){
					
					$sql = "SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_name LIKE concat('%',?,'%') AND p_from =? AND p_price BETWEEN ? AND ? ORDER BY p_name DESC";
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"ssdd",$str_srch_txt, $str_from, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}
			} elseif ($str_from == 'Any' &&
				$str_state == 'Any' &&
				$str_srch_txt != "Every"){
					$sql = "SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_name LIKE concat('%',?,'%') AND p_price BETWEEN ? AND ? ORDER BY p_name DESC";
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"sdd", $str_srch_txt, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}
			} elseif ($str_from == 'Any' &&
				$str_state != 'Any' &&
				$str_srch_txt == "Every"){
					$sql = 'SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_featured =? AND p_trending =? AND p_sale =? AND p_price BETWEEN ? AND ? ORDER BY p_name DESC';
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"ddddd", $state_f, $state_t, $state_s, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}
			} elseif ($str_from != 'Any' &&
				$str_state != 'Any' &&
				$str_srch_txt == "Every"){
					$sql = 'SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_from =? AND p_featured =? AND p_trending =? AND p_sale =? AND p_price BETWEEN ? AND ? ORDER BY p_name DESC';
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"sddddd", $str_from, $state_f, $state_t, $state_s, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);

					}
			} elseif ($str_from == 'Any' &&
				$str_state != 'Any' &&
				$str_srch_txt != "Every"){
					$sql = "SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_featured =? AND p_trending =? AND p_sale =? AND p_name LIKE concat('%',?,'%') AND p_price BETWEEN ? AND ? ORDER BY p_name DESC";
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"dddsdd", $state_f, $state_t, $state_s, $str_srch_txt, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);
					}
			} elseif ($str_from != 'Any' &&
				$str_state != 'Any' &&
				$str_srch_txt != "Every"){
					$sql = "SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl WHERE p_name LIKE concat('%',?,'%') AND p_from =? AND p_featured =? AND p_trending =? AND p_sale =? AND p_price BETWEEN ? AND ? ORDER BY p_name DESC";
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt,$sql)){
						header('location ../PHP/StorePage.php?error=sqlErrors');
						exit();
					} else {
						mysqli_stmt_bind_param($stmt,"ssddddd", $str_srch_txt, $str_from, $state_f, $state_t, $state_s, $str_begin_price, $str_end_price);
						mysqli_stmt_execute($stmt);

					}

			}
		}
		// where we change 
					$result = mysqli_stmt_get_result($stmt);
							while ($row  = mysqli_fetch_assoc($result)){
							
							$pro_name = $row['p_name'];
							$pro_from = $row['p_from'];
							$pro_price = $row['p_price'];
							$pro_img = $row['p_img_location'];
							$pro_f = $row['p_featured'];
							$pro_t = $row['p_trending'];
							$pro_s = $row['p_sale'];
							$pro_tag = "";


							if($pro_f == 1){
								$pro_tag = "Featured";
							} elseif($pro_t == 1){
								$pro_tag = "Trending";
							} elseif($pro_s == 1){
								$pro_tag = "Sale";
							}

							if ($pro_tag != ""){
							echo '
 								<div class="item-card">
									<div class="tag">'.$pro_tag.'</div>
									<div class="card-top-section">
										<img class="image-container"alt="product_item" src="'.$pro_img.'">
									</div>
									<form class="card-bottom-form">
										<div class="item-name">'.$pro_name.'</div>
										<div class="item-description">'.$pro_from.'</div>
										<div class="item-price"> R'.$pro_price.'</div>
										<div class="cart-button">
											<input type="button" name="btn_add_cart" value="Add to cart +">	
										</div>
										
									</form>
								</div> ';
							} else {
								echo '
 								<div class="item-card">
									<div class="card-top-section">
										<img class="image-container"alt="product_item" src="'.$pro_img.'">
									</div>
									<form class="card-bottom-form">
										<div class="item-name">'.$pro_name.'</div>
										<div class="item-description">'.$pro_from.'</div>
										<div class="item-price"> R'.$pro_price.'</div>
										<div class="cart-button">
											<input type="button" name="btn_add_cart" value="Add to cart +">	
										</div>
										
									</form>
								</div> ';
							}

						}	


	mysqli_stmt_close($stmt);
	mysqli_close($conn);
	} else {
					$pro_name = "";
					$pro_from = "";
					$pro_price = 0.00;
					$pro_img = "";

					$sql = "SELECT p_id, p_name, p_img_location, p_price, p_from, p_featured, p_trending, p_sale FROM products_tbl";
					$stmt = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt, $sql)){
					} else {
						
						mysqli_stmt_execute($stmt);
						
						$result = mysqli_stmt_get_result($stmt);
							while ($row  = mysqli_fetch_assoc($result)){
							
							$pro_name = $row['p_name'];
							$pro_from = $row['p_from'];
							$pro_price = $row['p_price'];
							$pro_img = $row['p_img_location'];
							$pro_f = $row['p_featured'];
							$pro_t = $row['p_trending'];
							$pro_s = $row['p_sale'];
							$pro_tag = "";


							if($pro_f == 1){
								$pro_tag = "Featured";
							} elseif($pro_t == 1){
								$pro_tag = "Trending";
							} elseif($pro_s == 1){
								$pro_tag = "Sale";
							}

							if ($pro_tag != ""){
							echo '
 								<div class="item-card">
									<div class="tag">'.$pro_tag.'</div>
									<div class="card-top-section">
										<img class="image-container"alt="product_item" src="'.$pro_img.'">
									</div>
									<form class="card-bottom-form">
										<div class="item-name">'.$pro_name.'</div>
										<div class="item-description">'.$pro_from.'</div>
										<div class="item-price"> R'.$pro_price.'</div>
										<div class="cart-button">
											<input type="button" name="btn_add_cart" value="Add to cart +">	
										</div>
										
									</form>
								</div> ';
							} else {
								echo '
 								<div class="item-card">
									<div class="card-top-section">
										<img class="image-container"alt="product_item" src="'.$pro_img.'">
									</div>
									<form class="card-bottom-form">
										<div class="item-name">'.$pro_name.'</div>
										<div class="item-description">'.$pro_from.'</div>
										<div class="item-price"> R'.$pro_price.'</div>
										<div class="cart-button">
											<input type="button" name="btn_add_cart" value="Add to cart +">	
										</div>
										
									</form>
								</div> ';
							}
						}					
					}
						mysqli_stmt_close($stmt);
	mysqli_close($conn);
	}
