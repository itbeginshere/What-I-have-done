<?php 

if(isset($_POST['btn_log_in'])){

	require 'database_helper_inc.php';

	$log_userName_Email = $_POST['frm_log_user_name'];
	$log_pass = $_POST['frm_log_password'];

	if(empty($log_userName_Email) || 
		empty($log_pass)){

		header("location: ../PHP/register_form.php?error=invalidUserNameOrPassword");
		exit();
	} else {
		$sql = "SELECT * FROM users_tbl WHERE u_u_name=? OR u_email=?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/register_form.php?error=sqlError");
			exit();
		} else {
			mysqli_stmt_bind_param($stmt,"ss",$log_userName_Email,$log_userName_Email);
			mysqli_stmt_execute($stmt);
			//Gets a result set from a prepared statment
			$result = mysqli_stmt_get_result($stmt);
			//fetches a result row as an associative array			
			if($row = mysqli_fetch_assoc($result)){
				//checks to see whether the stored password and inputted password match
				$passCheck = password_verify($log_pass,$row['u_pass']);

				if($passCheck == false){
					header("location: ../PHP/register_form.php?error=invalidUserNameOrPassword");
					exit();
				} else if($passCheck == true) {

					$sql_pay = "SELECT pay_u_u_name FROM payment_tbl WHERE pay_u_u_name=?";
					$sql_address = "SELECT adr_u_u_name FROM addresses_tbl WHERE adr_u_u_name=?";

					$stmt_pay = mysqli_stmt_init($conn);
					$stmt_address = mysqli_stmt_init($conn);

					if(!mysqli_stmt_prepare($stmt_pay,$sql_pay) || !mysqli_stmt_prepare($stmt_address, $sql_address)){
						header("location: ../PHP/register_form.php?error=sqlError");
						exit();
					} else {
						mysqli_stmt_bind_param($stmt_pay,"s",$log_userName_Email);
						mysqli_stmt_bind_param($stmt_address, "s", $log_userName_Email);
						
						mysqli_stmt_execute($stmt_pay);
						mysqli_stmt_execute($stmt_address);
						//Gets a result set from a prepared statment
						$result_pay = mysqli_stmt_get_result($stmt_pay);
						$result_address = mysqli_stmt_get_result($stmt_address);

						if($row_pay = mysqli_fetch_assoc($result_pay)){
							
							if($row_address = mysqli_fetch_assoc($result_address)){
								//creates or continues a session
								session_start();
								//Declaration and assignment of session variables
								$_SESSION['userId'] = $row['u_id'];
								$_SESSION['userName'] = $row['u_u_name'];
								$_SESSION['pay'] = $row['u_u_name'];
								$_SESSION['address'] = $row['u_u_name'];
								//$_SESSION['userEmail'] = $row['user_email'];
								header("location: ../PHP/HomePage.php?login=success");
								exit();
							} else {
								//creates or continues a session
								session_start();
								//Declaration and assignment of session variables
								$_SESSION['userId'] = $row['u_id'];
								$_SESSION['userName'] = $row['u_u_name'];
								$_SESSION['pay'] = $row['u_u_name'];
								//$_SESSION['userEmail'] = $row['user_email'];
								header("location: ../PHP/HomePage.php?login=success");
								exit();
							}	
						} else {
							//creates or continues a session
							session_start();
							//Declaration and assignment of session variables
							$_SESSION['userId'] = $row['u_id'];
							$_SESSION['userName'] = $row['u_u_name'];
							
							//$_SESSION['userEmail'] = $row['user_email'];
							header("location: ../PHP/HomePage.php?login=success");
							exit();
						}

					}


					
				} else {
					header("location: ../PHP/register_form.php?error=invalidUserNameOrPassword");
					exit();
				}

			} else {
				header("location: ../PHP/register_form.php?error=invalidUserNameOrPassword");
				exit();
			}
		}

	}
	mysqli_stmt_close($stmt);
	mysqli_stmt_close($stmt_pay);
	mysqli_close($conn);

}  else { 
	header("location: ../PHP/register_form.php?error=invalidUserNameOrPassword");
	exit();
}
