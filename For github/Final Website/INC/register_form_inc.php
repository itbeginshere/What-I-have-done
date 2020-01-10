<?php
//Checks to see if the submit button was POSTED to this file  
if(isset($_POST['btn_sign_up'])){

	//Imports the database helper file into this file
	require 'database_helper_inc.php';	

	//Vairable assignment and declarations 
	$sign_firstName = $_POST['frm_sign_first_name'];
	$sign_lastName = $_POST['frm_sign_last_name'];
	$sign_userName = $_POST['frm_sign_user_name'];
	$sign_email = $_POST['frm_sign_email'];
	$sign_pass = $_POST['frm_sign_password'];
	$sign_passConfirm = $_POST['frm_sign_password_confirm'];

	//Checks to see if there are any empty fields
	if(empty($sign_firstName) ||
		empty($sign_lastName) ||
		empty($sign_userName) ||
		empty($sign_email) ||
		empty($sign_pass) ||
		empty($sign_passConfirm)		
	){
		//changes the path of the header
		header("location: ../PHP/register_form.php?error=emptyFields");
		exit(); 
	
	} elseif (preg_match("/[0-9]/", $sign_firstName) || preg_match("/[\W]+/", $sign_firstName)){
		header("location: ../PHP/register_form.php?error=digitOrSpecialF");
		exit();

	} else if (preg_match("/[0-9]/", $sign_lastName) || preg_match("/[\W]+/", $sign_lastName)){
		header("location: ../PHP/register_form.php?error=digitOrSpecialL");
		exit();
	} elseif (!filter_var($sign_email,FILTER_VALIDATE_EMAIL)){
		header("location: ../PHP/register_form.php?error=invalidEmail");
		exit();
	} elseif (!preg_match("/^[a-zA-Z0-9]*$/", $sign_userName)){
		header("location: ../PHP/register_form.php?error=invalidUserName");
		exit();
		//checks to see if the password and confirm password fields match
	} else if(strlen($sign_pass) < 8){
		header("location: ../PHP/register_form.php?error=invalidPassword");
		exit();
	} else if ($sign_pass !== $sign_passConfirm){
		header("location: ../PHP/register_form.php?error=passwordMismatch");
		exit();
	} else {
		//SQL query in a string
		$sql = "SELECT u_id FROM users_tbl WHERE u_u_name=?";
		//initialises a statment that is used for mysqli_stmt_prepare
		$stmt = mysqli_stmt_init($conn);
		//Tests a query against a statmement
		if(!mysqli_stmt_prepare($stmt, $sql)){
			header("location: ../PHP/register_form.php?error=sqlErrors&UserName=".$sign_userName);
			exit();
		} else {
			//Binds the appropraite varaibles to the placeholder ares within the sql query
			mysqli_stmt_bind_param($stmt,"s",$sign_userName);  //S string,i integer, b blob, d double 
			//executes the prepared statment
			mysqli_stmt_execute($stmt);	
			//Generates a result set from the prepared statmeent
			mysqli_stmt_store_result($stmt); //for fetching
			//determines how many rows there are in the resultset
			$resultCheck = mysqli_stmt_num_rows($stmt);

			if($resultCheck > 0) {
					header("location: ../PHP/register_form.php?error=userNameALreadyExists&UserName=".$sign_userName);
					exit();
			} else {
					$sql = "INSERT INTO users_tbl(u_f_name,u_l_name,u_u_name,u_email,u_pass) VALUES(?,?,?,?,?)";

					$stmt = mysqli_stmt_init($conn);
					if(!mysqli_stmt_prepare($stmt, $sql)){
						header("location: ../PHP/register_form.php?error=sqlErrors&UserName=".$sign_userName);
						exit();	
					} else {

						//Encrypts the password via calculated hash
						$hashedPass = password_hash($sign_pass, PASSWORD_DEFAULT);

						mysqli_stmt_bind_param($stmt,"sssss",$sign_firstName,$sign_lastName,$sign_userName,$sign_email,$hashedPass);
						mysqli_stmt_execute($stmt);
						header("location: ../PHP/register_form.php?signUp=success");
						exit();
					}
			}

		}


	}
	//closes the statment
	mysqli_stmt_close($stmt);
	//closes the connection
	mysqli_close($conn);
} 

else {
	header("location: ../PHP/register_form.php");
	exit();
}

