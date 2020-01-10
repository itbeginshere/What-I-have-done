<?php  

require 'database_helper_inc.php';

if (isset($_POST['btn_store_add'])){
	//$str_code = $_POST['sck_code'];
	$str_name = $_POST['sck_name'];
	$str_from = $_POST['sck_from'];
	$str_price = $_POST['sck_price'];
	$str_img = $_POST['sck_img'];
	$str_state = $_POST['sck_state'];
	$str_quan = $_POST['sck_quan'];
	$str_back = $_POST['sck_back'];

	$str_state_f = 0;
	$str_state_t = 0;
	$str_state_s = 0;

	if(empty($str_name) || 
		empty($str_from) || 
		empty($str_price) ||
		empty($str_img)	||
		empty($str_state) ||
		empty($str_quan)){

		header("location: ../PHP/admin_control_store.php?error=emptyFields");
		exit();	
	} elseif (!preg_match("/[0-9]/", $str_price)){
		header("location: ../PHP/admin_control_store.php?error=nonNumeric1");
		exit();
	} elseif (!preg_match("/[0-9]/", $str_quan)){
		header("location: ../PHP/admin_control_store.php?error=nonNumeric2");
		exit();
	} elseif (!preg_match("/[0-9]/", $str_back)){
		header("location: ../PHP/admin_control_store.php?error=nonNumeric3");
		exit();
	}  elseif (floatval($str_price) < 0){
		header("location: ../PHP/admin_control_store.php?error=negativeValue1");
		exit();
	} elseif (floatval($str_quan) < 0){
		header("location: ../PHP/admin_control_store.php?error=negativeValue2");
		exit();
	}elseif (floatval($str_back) < 0){
		header("location: ../PHP/admin_control_store.php?error=negativeValue3");
		exit();
	}else {

		if($str_state == "featured"){

			$str_state_f = 1;
			$str_state_t = 0;
			$str_state_s = 0;

		} elseif ($str_state == "trending"){

			$str_state_f = 0;
			$str_state_t = 1;
			$str_state_s = 0;

		} elseif ($str_state == "sale"){

			$str_state_f = 0;
			$str_state_t = 0;
			$str_state_s = 1;

		} elseif ($str_state == "none"){
			$str_state_f = 0;
			$str_state_t = 0;
			$str_state_s = 0;
		}

		$sql = "INSERT INTO products_tbl(p_name,p_from,p_img_location,p_price,p_featured,p_trending,p_sale,p_quantity,p_backordered) VALUES(?,?,?,?,?,?,?,?,?)";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt, $sql)){
			header("location: ../PHP/admin_control_store.php?error=sqlErrors");
			exit();
		} else {
			$sql_checkUser = "SELECT * FROM products_tbl WHERE p_name=?";
			$stmt_checkUser = mysqli_stmt_init($conn);

			if(!mysqli_stmt_prepare($stmt_checkUser, $sql_checkUser)){
				header("location: ../PHP/admin_control_store.php?error=sqlErrors");
				exit();
			} else {
				mysqli_stmt_bind_param($stmt_checkUser, "s", $str_name);
				mysqli_stmt_execute($stmt_checkUser);

				$result = mysqli_stmt_get_result($stmt_checkUser);

				if($row = mysqli_fetch_assoc($result)){
					header("location: ../PHP/admin_control_store.php?error=Exists");
					exit(); 
				} else {
					mysqli_stmt_bind_param($stmt,"sssdiiidd",$str_name,$str_from,$str_img,$str_price,$str_state_f,$str_state_t,$str_state_s,$str_quan,$str_back);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/admin_control_store.php?success=Added");
					exit(); 
				}
			}
			
			
			}
		}

	mysqli_stmt_close($stmt);
	mysqli_close($conn);
	}




if(isset($_POST['btn_store_remove'])){
	$str_code = $_POST['sck_code'];

	if(empty($str_code)){
		header("location: ../PHP/admin_control_store.php?error=emptyCode");
		exit();	
	} else {
		$sql = "SELECT * FROM products_tbl WHERE p_id=?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/admin_control_store.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$str_code);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){

				$sql = "DELETE FROM products_tbl WHERE p_id=?";

				$stmt = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt,$sql)){
					header("location: ../PHP/admin_control_store.php?error=sqlErrors");
					exit();	
				} else {
					mysqli_stmt_bind_param($stmt,"s",$row['p_id']);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/admin_control_store.php?success=Removed");
					exit(); 
				}


			} else {
				header("location: ../PHP/admin_control_store.php?error=ghostCode");
				exit();	
			}
		}



	}
	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}


if(isset($_POST['btn_store_update'])){
	
	$str_code = $_POST['sck_code'];
	$str_name = str_replace('_', ' ', $_POST['sck_name']);
	$str_from = $_POST['sck_from'];
	$str_price = $_POST['sck_price'];
	$str_img = $_POST['sck_img'];
	$str_quan = $_POST['sck_quan'];
	$str_back = $_POST['sck_back'];
	$str_state = $_POST['sck_state'];


	$str_state_f = 0;
	$str_state_t = 0;
	$str_state_s = 0;

	if(empty($str_name) ||
		empty($str_from) || 
		empty($str_price) ||
		empty($str_img)	||
		empty($str_state) ||
		empty($str_quan)){

		header("location: ../PHP/admin_control_store.php?error=emptyFields");
		exit();	
	} elseif (!preg_match("/[0-9]/", $str_price)){
		header("location: ../PHP/admin_control_store.php?error=nonNumeric");
		exit();
	}  elseif (floatval($str_price) < 0){
		header("location: ../PHP/admin_control_store.php?error=negativeValue");
		exit();
	} elseif (!preg_match("/[0-9]/", $str_price)){
		header("location: ../PHP/admin_control_store.php?error=nonNumeric1");
		exit();
	} elseif (!preg_match("/[0-9]/", $str_quan)){
		header("location: ../PHP/admin_control_store.php?error=nonNumeric2");
		exit();
	} elseif (!preg_match("/[0-9]/", $str_back)){
		header("location: ../PHP/admin_control_store.php?error=nonNumeric3");
		exit();
	}  elseif (floatval($str_price) < 0){
		header("location: ../PHP/admin_control_store.php?error=negativeValue1");
		exit();
	} elseif (floatval($str_quan) < 0){
		header("location: ../PHP/admin_control_store.php?error=negativeValue2");
		exit();
	}	elseif (floatval($str_back) < 0){
		header("location: ../PHP/admin_control_store.php?error=negativeValue3");
		exit();
	} else {
		$sql = "SELECT * FROM products_tbl WHERE p_id=?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/admin_control_store.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$str_code);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){

				if($str_state == "featured"){

					$str_state_f = 1;
					$str_state_t = 0;
					$str_state_s = 0;

				} elseif ($str_state == "trending"){

					$str_state_f = 0;
					$str_state_t = 1;
					$str_state_s = 0;

				} elseif ($str_state == "sale"){

					$str_state_f = 0;	
					$str_state_t = 0;
					$str_state_s = 1;

				} elseif ($str_state == "none"){
					$str_state_f = 0;
					$str_state_t = 0;
					$str_state_s = 0;
				}


				$sql = "UPDATE products_tbl SET p_name=?, p_from=?,p_img_location=?,p_price=?,p_featured=?,p_trending=?,p_sale=?,p_quantity=?,p_backordered=? WHERE p_id=?";

				$stmt = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt,$sql)){
					header("location: ../PHP/admin_control_store.php?error=sqlErrors");
					exit();	
				} else {
					mysqli_stmt_bind_param($stmt,"sssdiiidds",$str_name,$str_from,$str_img,$str_price, $str_state_f, $str_state_t, $str_state_s,$str_quan,$str_back,$str_code);
					mysqli_stmt_execute($stmt);


					if($str_quan > $str_back){
						$new_quan = $str_quan - $str_back;
						$new_back = 0;

						$sql_back = "UPDATE products_tbl SET p_quantity=?, p_backordered=? WHERE p_id=?";
						$stmt_back = mysqli_stmt_init($conn);

						if(!mysqli_stmt_prepare($stmt_back, $sql_back)){
							header("location: ../PHP/admin_control_store.php?error=sqlErrors");
							exit();	
						} else {
							mysqli_stmt_bind_param($stmt_back,"dds",$new_quan,$new_back,$str_code);
							mysqli_stmt_execute($stmt_back);
							header("location: ../PHP/admin_control_store.php?success=Updated");
							exit();	 
						}

					} else {
					header("location: ../PHP/admin_control_store.php?success=Updated");
					exit(); 
					}
				}

			} else {
				header("location: ../PHP/admin_control_store.php?error=ghostCode");
				exit();	
			}
		}

	}	
	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}

if(isset($_POST['btn_store_get'])){

	$str_code = $_POST['sck_code'];
	$str_name = "";
	$str_from = "";
	$str_price = "";
	$str_img = "";
	$str_quan = "";
	$str_back = "";
	$str_state_f = 0;
	$str_state_t = 0;
	$str_state_s = 0;

	if(empty($str_code)){
		header("location: ../PHP/admin_control_store.php?error=emptyCode");
		exit();	
	} else {
		$sql = "SELECT * FROM products_tbl WHERE p_id=?";
		$stmt = mysqli_stmt_init($conn);
		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/admin_control_store.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$str_code);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){
					$str_name = str_replace(" ", "_", $row['p_name']);
					$str_from = str_replace(" ", "_", $row['p_from']);
					$str_img = str_replace(" ", "_", $row['p_img_location']);
					$str_price = $row['p_price'];
					$str_quan = $row['p_quantity'];
					$str_back = $row['p_backordered'];
					$str_state_f = $row['p_featured'];
					$str_state_t = $row['p_trending'];
					$str_state_s = $row['p_sale'];

					header("location: ../PHP/admin_control_store.php?dataFetched&Code=".$str_code."&Name=".$str_name."&From=".$str_from."&ImagePath=".$str_img."&Price=".$str_price."&Featured=".$str_state_f."&Trending=".$str_state_t."&Sale=".$str_state_s."&Quan=".$str_quan."&Back=".$str_back);
					exit();
			} else {
				header("location: ../PHP/admin_control_store.php?error=ghostCode");
				exit();	
			}
		}

	}
	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}
