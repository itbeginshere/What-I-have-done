<?php  

require 'database_helper_inc.php';

if (isset($_POST['btn_pay_add'])){
	$pay_username = $_POST['pay_user'];
	$pay_name = $_POST['pay_name'];
	$pay_code = $_POST['pay_crd_code'];
	$pay_no = $_POST['pay_crd_no'];
	$pay_exp_y = $_POST['pay_exp_y'];
	$pay_exp_m = $_POST['pay_exp_m'];


	if(empty($pay_name) || 
		empty($pay_code) ||
		empty($pay_no)  || 
		empty($pay_exp_y) ||
		empty($pay_exp_m)){

		header("location: ../PHP/profile_control_payments.php?error=emptyFields");
		exit();	
	} elseif (preg_match("/[0-9]/", $pay_name) || preg_match("/[\W]+/", $pay_name)){
		header("location: ../PHP/profile_control_payments.php?error=onlyAplhabet");
		exit();
	} elseif (preg_match("/[a-zA-Z]/", $pay_code) || preg_match("/[\W]+/", $pay_code)){
		header("location: ../PHP/profile_control_payments.php?error=onlyDigits1");
		exit();
	} elseif (preg_match("/[a-zA-Z]/", $pay_no) || preg_match("/[\W]+/", $pay_no)){
		header("location: ../PHP/profile_control_payments.php?error=onlyDigits2");
		exit();
	} elseif (preg_match("/[a-zA-Z]/", $pay_exp_y) || preg_match("/[\W]+/", $pay_exp_y)){
		header("location: ../PHP/profile_control_payments.php?error=onlyDigits3");
		exit();
	} elseif (preg_match("/[a-zA-Z]/", $pay_exp_m) || preg_match("/[\W]+/", $pay_exp_m)){
		header("location: ../PHP/profile_control_payments.php?error=onlyDigits4");
		exit();
	} elseif (strlen($pay_code) != 3){
		header("location: ../PHP/profile_control_payments.php?error=length1");
		exit();
	} elseif (strlen($pay_no) != 17){
		header("location: ../PHP/profile_control_payments.php?error=length2");
		exit();
	}elseif (floatval($pay_exp_m) < 1 || floatval($pay_exp_m) > 12){
		header("location: ../PHP/profile_control_payments.php?error=Month");
		exit();
	}elseif (floatval($pay_exp_y) < 2000){
		header("location: ../PHP/profile_control_payments.php?error=Year");
		exit();
	}else {

		$sql = "INSERT INTO payment_tbl (pay_u_u_name, pay_card_name, pay_card_no, pay_card_code, pay_exp_year, pay_exp_month) VALUES (?,?,?,?,?,?);";

		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt, $sql)){
			header("location: ../PHP/profile_control_payments.php?error=sqlErrors");
			exit();
		} else {

			$sql_checkUser = "SELECT * FROM payment_tbl WHERE pay_u_u_name=?";
			$stmt_checkUser = mysqli_stmt_init($conn);

			if(!mysqli_stmt_prepare($stmt_checkUser, $sql_checkUser)){
				header("location: ../PHP/profile_control_payments.php?error=sqlErrors");
				exit();
			} else {
				mysqli_stmt_bind_param($stmt_checkUser, "s", $pay_username);
				mysqli_stmt_execute($stmt_checkUser);

				$result = mysqli_stmt_get_result($stmt_checkUser);

				if($row = mysqli_fetch_assoc($result)){
					header("location: ../PHP/profile_control_payments.php?error=Exists");
					exit(); 
				} else {
					mysqli_stmt_bind_param($stmt,"ssssss", $pay_username, $pay_name, $pay_no, $pay_code, $pay_exp_y, $pay_exp_m);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/profile_control_payments.php?success=Added");
					exit(); 
				}
			}

		
		}

	}

	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}

if(isset($_POST['btn_pay_remove'])){
	$pay_username = $_POST['pay_user'];

	if(empty($pay_username)){
		header("location: ../PHP/profile_control_payments.php?error=emptyName");
		exit();	
	} else {
		$sql = "SELECT * FROM payment_tbl WHERE pay_u_u_name=?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/profile_control_payments.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$pay_username);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){

				$sql = "DELETE FROM payment_tbl WHERE  pay_u_u_name=?";

				$stmt = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt,$sql)){
					header("location: ../PHP/profile_control_payments.php?error=sqlErrors");
					exit();	
				} else {
					mysqli_stmt_bind_param($stmt,"s",$row['pay_u_u_name']);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/profile_control_payments.php?success=Removed");
					exit(); 
				}


			} else {
				header("location: ../PHP/profile_control_payments.php?error=ghostName");
				exit();	
			}
		}

	}
	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}


if(isset($_POST['btn_pay_update'])){
	
	$pay_username = $_POST['pay_user'];
	$pay_name = $_POST['pay_name'];
	$pay_code = $_POST['pay_crd_code'];
	$pay_no = $_POST['pay_crd_no'];
	$pay_exp_y = $_POST['pay_exp_y'];
	$pay_exp_m = $_POST['pay_exp_m'];
	

	if(empty($pay_username) ||
		empty($pay_name) || 
		empty($pay_code) ||
		empty($pay_no)  || 
		empty($pay_exp_y) ||
		empty($pay_exp_m)){

		header("location: ../PHP/profile_control_payments.php?error=emptyFields");
		exit();	
	} elseif (preg_match("/[0-9]/", $pay_name) || preg_match("/[\W]+/", $pay_name)){
		header("location: ../PHP/profile_control_payments.php?error=onlyAplhabet");
		exit();
	} elseif (preg_match("/[a-zA-Z]/", $pay_code) || preg_match("/[\W]+/", $pay_code)){
		header("location: ../PHP/profile_control_payments.php?error=onlyDigits1");
		exit();
	} elseif (preg_match("/[a-zA-Z]/", $pay_no) || preg_match("/[\W]+/", $pay_no)){
		header("location: ../PHP/profile_control_payments.php?error=onlyDigits2");
		exit();
	} elseif (preg_match("/[a-zA-Z]/", $pay_exp_y) || preg_match("/[\W]+/", $pay_exp_y)){
		header("location: ../PHP/profile_control_payments.php?error=onlyDigits3");
		exit();
	} elseif (preg_match("/[a-zA-Z]/", $pay_exp_m) || preg_match("/[\W]+/", $pay_exp_m)){
		header("location: ../PHP/profile_control_payments.php?error=onlyDigits4");
		exit();
	} elseif (strlen($pay_code) != 3){
		header("location: ../PHP/profile_control_payments.php?error=length1");
		exit();
	} elseif (strlen($pay_no) != 17){
		header("location: ../PHP/profile_control_payments.php?error=length2");
		exit();
	}elseif (floatval($pay_exp_m) < 1 || floatval($pay_exp_m) > 12){
		header("location: ../PHP/profile_control_payments.php?error=Month");
		exit();
	}elseif (floatval($pay_exp_y) < 2000){
		header("location: ../PHP/profile_control_payments.php?error=Year");
		exit();
	}else {
		$sql = "SELECT * FROM payment_tbl WHERE pay_u_u_name=?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/profile_control_payments.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$pay_username);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){

				$sql = "UPDATE payment_tbl SET pay_card_name=?, pay_card_no=?, pay_card_code=?, pay_exp_year=?, pay_exp_month=? WHERE pay_u_u_name=?";

				$stmt = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt,$sql)){
					header("location: ../PHP/profile_control_payments.php?error=sqlErrors");
					exit();	
				} else {
					mysqli_stmt_bind_param($stmt,"ssssss",$pay_name, $pay_no, $pay_code, $pay_exp_y, $pay_exp_m, $pay_username);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/profile_control_payments.php?success=Updated");
					exit(); 
				}

			} else {
				header("location: ../PHP/profile_control_payments.php?error=ghostCode");
				exit();	
			}
		}

	}	
	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}

/*if(isset($_POST['btn_pay_get'])){
	

}
*/