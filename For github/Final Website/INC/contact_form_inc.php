<?php  

if(isset($_POST['btn_mess_send'])){

	require 'database_helper_inc.php';

	$con_email = $_POST['contact_email'];
	$con_message = $_POST['contact_message'];
	$curr_page = $_POST['web_page'];

	if(empty($con_email) ||
		empty($con_message)){
		header("location: ../PHP/".$curr_page."?error=con_emptyFields");
		exit();
	} elseif (!filter_var($con_email,FILTER_VALIDATE_EMAIL)){
		header("location: ../PHP/".$curr_page."?error=con_invalidEmail");
		exit();
	} else {
		//Query to determine if a email exists
		$sql = "SELECT con_id FROM contact_tbl WHERE con_email=?";
		//Initailising the statement
		$stmt = mysqli_stmt_init($conn);
		//can this statement run in this database
		if(!mysqli_stmt_prepare($stmt, $sql)){
			header("location: ../PHP/".$curr_page."?error=con_sqlError");
			exit();
		} else {
			
				$sql = "INSERT INTO contact_tbl (con_email,con_message) VALUES (?,?)";

				$stmt = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt, $sql)){
					header("location: ../PHP/".$curr_page."?error=con_sqlError");
					exit();
				} else {
					mysqli_stmt_bind_param($stmt,"ss",$con_email,$con_message);

					mysqli_stmt_execute($stmt);

					header("location: ../PHP/".$curr_page."?sent=success");
					exit();
				}
			

		}

	}
	
} else {
	header("location: ../PHP/".$curr_page.".php");
	exit();
}