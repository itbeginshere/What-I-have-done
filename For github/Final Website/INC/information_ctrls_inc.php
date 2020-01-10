<?php  

require 'database_helper_inc.php';

if(isset($_POST['btn_pay_remove'])){
	$info_username = $_POST['info_user'];

	if(empty($info_username)){
		header("location: ../PHP/profile_control_information.php?error=emptyName");
		exit();	
	} else {
		$sql = "SELECT * FROM users_tbl WHERE u_u_name=?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/profile_control_information.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$info_username);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){

				$sql = "DELETE FROM users_tbl WHERE u_u_name=?";
				$sql_pay = "DELETE FROM payment_tbl WHERE pay_u_u_name=?";
				$stmt = mysqli_stmt_init($conn);
				$stmt_pay = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt,$sql) || !mysqli_stmt_prepare($stmt_pay,$sql_pay)){
					header("location: ../PHP/profile_control_information.php?error=sqlErrors");
					exit();	
				} else {
					mysqli_stmt_bind_param($stmt,"s",$row['u_u_name']);
					mysqli_stmt_bind_param($stmt_pay,"s",$row['u_u_name']);
					mysqli_stmt_execute($stmt);
					mysqli_stmt_execute($stmt_pay);
					header("location: ../PHP/profile_control_information.php?success=Removed");
					require '../INC/logout_inc.php';
					exit(); 
				}


			} else {
				header("location: ../PHP/profile_control_information.php?error=ghostName");
				exit();	
			}
		}

	}
	mysqli_stmt_close($stmt_pay);
	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}


if(isset($_POST['btn_pay_update'])){
	
	$info_username = $_POST['info_user'];
	$info_first =  $_POST['info_f_name'];
	$info_last =  $_POST['info_l_name'];
	$info_email =  $_POST['info_email'];

	if(empty($info_username) ||
		empty($info_first) || 
		empty($info_last) ||
		empty($info_email)){

		header("location: ../PHP/profile_control_information.php?error=emptyFields");
		exit();	
	} elseif (preg_match("/[0-9]/", $info_first) || preg_match("/[\W]+/", $info_first)){
		header("location: ../PHP/profile_control_information.php?error=digitOrSpecialF");
		exit();
	} else if (preg_match("/[0-9]/", $info_last) || preg_match("/[\W]+/", $info_last)){
		header("location: ../PHP/profile_control_information.php?error=digitOrSpecialL");
		exit();
	} elseif (!filter_var($info_email,FILTER_VALIDATE_EMAIL)){
		header("location: ../PHP/profile_control_information.php?error=invalidEmail");
		exit();
	} else {
		$sql = "SELECT * FROM users_tbl WHERE u_u_name=?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/profile_control_information.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$info_username);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){

				$sql = "UPDATE users_tbl SET u_f_name=?, u_l_name=?, u_email=? WHERE u_u_name=?";

				$stmt = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt,$sql)){
					header("location: ../PHP/profile_control_information.php?error=sqlErrors");
					exit();	
				} else {
					mysqli_stmt_bind_param($stmt,"ssss", $info_first, $info_last, $info_email, $info_username);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/profile_control_information.php?success=Updated");
					exit(); 
				}

			} else {
				header("location: ../PHP/profile_control_information.php?error=ghostCode");
				exit();	
			}
		}

	}	
	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}
