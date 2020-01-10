<?php  

if(isset($_POST['btn_news_sign_up'])){

	require 'database_helper_inc.php';

	$news_first_name = $_POST['news_f_name'];
	$news_email = $_POST['news_mail'];

	if(empty($news_first_name) ||
		empty($news_email)){
		header("location: ../PHP/HomePage.php?error=news_emptyFields");
		exit();
	} elseif (preg_match("/[0-9]/", $news_first_name)){
		header("location: ../PHP/HomePage.php?error=news_digitInc");
		exit();
	} elseif (preg_match("/[\W]+/", $news_first_name)){
		header("location: ../PHP/HomePage.php?error=news_specailInc");
		exit();
	} elseif (!filter_var($news_email,FILTER_VALIDATE_EMAIL)){
		header("location: ../PHP/HomePage.php?error=news_invalidEmail");
		exit();
	} else {
		//Query to determine if a email exists
		$sql = "SELECT nl_id FROM newsletter_tbl WHERE nl_email=?";
		//Initailising the statement
		$stmt = mysqli_stmt_init($conn);
		//can this statement run in this database
		if(!mysqli_stmt_prepare($stmt, $sql)){
			header("location: ../PHP/HomePage.php?error=news_sqlError");
			exit();
		} else {
			//bind the variables to the placeholders
			mysqli_stmt_bind_param($stmt,"s",$news_email);
			//execute the statement
			mysqli_stmt_execute($stmt);
			//generates the result set 
			mysqli_stmt_store_result($stmt);
			//determines how many rows there are in the resultset
			$resultsCheck = mysqli_stmt_num_rows($stmt);

			if($resultsCheck > 0){
				header("location: ../PHP/HomePage.php?error=news_emailAlreadyInUse");
				exit();
			} else {
				$sql = "INSERT INTO newsletter_tbl(nl_f_name,nl_email) VALUES (?,?)";

				$stmt = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt, $sql)){
					header("location: ../PHP/HomePage.php?error=news_sqlError");
					exit();
				} else {
					mysqli_stmt_bind_param($stmt,"ss",$news_first_name,$news_email);

					mysqli_stmt_execute($stmt);

					header("location: ../PHP/HomePage.php?subscribed=success");
					exit();
				}
			}

		}

	}
	
} else {
	header("location: ../PHP/HomePage.php");
	exit();
}