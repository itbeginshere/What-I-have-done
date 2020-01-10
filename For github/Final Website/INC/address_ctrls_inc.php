<?php  

require 'database_helper_inc.php';

if (isset($_POST['btn_adr_add'])){
	$adr_username = $_POST['adr_user'];
	$adr_bill_1 = $_POST['adr_b_1'];
	$adr_bill_2 = $_POST['adr_b_2'];
	$adr_poastel = $_POST['adr_post'];
	$adr_city = $_POST['adr_city'];
	$adr_province = $_POST['adr_pro'];


	if(empty($adr_bill_1) || 
		empty($adr_bill_2) ||
		empty($adr_poastel)  || 
		empty($adr_city) ||
		empty($adr_province)){

		header("location: ../PHP/profile_control_address.php?error=emptyFields");
		exit();	
	}  elseif (preg_match("/[a-zA-Z]/", $adr_poastel) || preg_match("/[\W]+/", $adr_poastel)){
		header("location: ../PHP/profile_control_address.php?error=onlyDigits");
		exit();
	} elseif (preg_match("/[0-9]/", $adr_city)){
		header("location: ../PHP/profile_control_address.php?error=onlyAplhabet1");
		exit();
	} elseif (preg_match("/[0-9]/", $adr_province)){
		header("location: ../PHP/profile_control_address.php?error=onlyAplhabet2");
		exit();
	} else {

		$sql = "INSERT INTO addresses_tbl (adr_u_u_name, adr_bill_1, adr_bill_2, adr_poastel_code, adr_city, adr_province) VALUES (?,?,?,?,?,?);";

		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt, $sql)){
			header("location: ../PHP/profile_control_address.php?error=sqlErrors");
			exit();
		} else {

			$sql_checkUser = "SELECT * FROM addresses_tbl WHERE adr_u_u_name=?";
			$stmt_checkUser = mysqli_stmt_init($conn);

			if(!mysqli_stmt_prepare($stmt_checkUser, $sql_checkUser)){
				header("location: ../PHP/profile_control_address.php?error=sqlErrors");
				exit();
			} else {
				mysqli_stmt_bind_param($stmt_checkUser, "s", $adr_username);
				mysqli_stmt_execute($stmt_checkUser);

				$result = mysqli_stmt_get_result($stmt_checkUser);

				if($row = mysqli_fetch_assoc($result)){
					header("location: ../PHP/profile_control_address.php?error=Exists");
					exit(); 
				} else {
					mysqli_stmt_bind_param($stmt,"ssssss", $adr_username, $adr_bill_1, $adr_bill_2, $adr_poastel, $adr_city, $adr_province);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/profile_control_address.php?success=Added");
					exit(); 
				}
			}

		
		}

	}

	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}

if(isset($_POST['btn_adr_remove'])){
	$adr_username = $_POST['adr_user'];

	if(empty($adr_username)){
		header("location: ../PHP/profile_control_address.php?error=emptyName");
		exit();	
	} else {
		$sql = "SELECT * FROM addresses_tbl WHERE adr_u_u_name=?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/profile_control_address.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$adr_username);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){

				$sql = "DELETE FROM addresses_tbl WHERE  adr_u_u_name=?";

				$stmt = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt,$sql)){
					header("location: ../PHP/profile_control_address.php?error=sqlErrors");
					exit();	
				} else {
					mysqli_stmt_bind_param($stmt,"s",$row['adr_u_u_name']);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/profile_control_address.php?success=Removed");
					exit(); 
				}


			} else {
				header("location: ../PHP/profile_control_address.php?error=ghostName");
				exit();	
			}
		}

	}
	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}


if(isset($_POST['btn_adr_update'])){
	
	$adr_username = $_POST['adr_user'];
	$adr_bill_1 = $_POST['adr_b_1'];
	$adr_bill_2 = $_POST['adr_b_2'];
	$adr_poastel = $_POST['adr_post'];
	$adr_city = $_POST['adr_city'];
	$adr_province = $_POST['adr_pro'];

	if(empty($adr_bill_1) || 
		empty($adr_bill_2) ||
		empty($adr_poastel)  || 
		empty($adr_city) ||
		empty($adr_province)){

		header("location: ../PHP/profile_control_address.php?error=emptyFields");
		exit();	
	}  elseif (preg_match("/[a-zA-Z]/", $adr_poastel) || preg_match("/[\W]+/", $adr_poastel)){
		header("location: ../PHP/profile_control_address.php?error=onlyDigits");
		exit();
	} elseif (preg_match("/[0-9]/", $adr_city)){
		header("location: ../PHP/profile_control_address.php?error=onlyAplhabet1");
		exit();
	} elseif (preg_match("/[0-9]/", $adr_province)){
		header("location: ../PHP/profile_control_address.php?error=onlyAplhabet2");
		exit();
	} else {
		$sql = "SELECT * FROM addresses_tbl WHERE adr_u_u_name=?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/profile_control_address.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$adr_username);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){

				$sql = "UPDATE addresses_tbl SET adr_bill_1 = ?, adr_bill_2 = ?, adr_poastel_code = ?, adr_city = ?, adr_province = ? WHERE adr_u_u_name = ?";

				$stmt = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt,$sql)){
					header("location: ../PHP/profile_control_address.php?error=sqlErrors");
					exit();	
				} else {
					mysqli_stmt_bind_param($stmt,"ssssss",$adr_bill_1, $adr_bill_2, $adr_poastel, $adr_city, $adr_province, $adr_username);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/profile_control_address.php?success=Updated");
					exit(); 
				}

			} else {
				header("location: ../PHP/profile_control_address.php?error=ghostCode");
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