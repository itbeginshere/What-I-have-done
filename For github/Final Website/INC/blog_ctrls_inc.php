<?php  

require 'database_helper_inc.php';

if (isset($_POST['btn_blog_add'])){
	//$blg_code = $_POST['blog_code'];
	$blg_title = $_POST['blog_title'];
	$blg_author = $_POST['blog_author'];
	$blg_doc = $_POST['blog_doc'];
	$blg_stamp = date("Y-m-d H:i:s");

	if(empty($blg_title) ||
		empty($blg_author) || 
		empty($blg_doc) ||
		empty($blg_stamp)) {

		header("location: ../PHP/admin_control_blog.php?error=emptyFields");
		exit();	
	} elseif (preg_match("/[0-9]/", $blg_author)){
		header("location: ../PHP/admin_control_blog.php?error=numeric");
		exit();
	} else {

		$sql = "INSERT INTO articles_tbl(a_title,a_doc_location,a_author,a_stamp) VALUES(?,?,?,?)";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt, $sql)){
			header("location: ../PHP/admin_control_blog.php?error=sqlErrors");
			exit();
		} else {
			$sql_checkUser = "SELECT * FROM articles_tbl WHERE a_title=?";
			$stmt_checkUser = mysqli_stmt_init($conn);

			if(!mysqli_stmt_prepare($stmt_checkUser, $sql_checkUser)){
				header("location: ../PHP/admin_control_blog.php?error=sqlErrors");
				exit();
			} else {
				mysqli_stmt_bind_param($stmt_checkUser, "s", $blg_title);
				mysqli_stmt_execute($stmt_checkUser);

				$result = mysqli_stmt_get_result($stmt_checkUser);

				if($row = mysqli_fetch_assoc($result)){
					header("location: ../PHP/admin_control_blog.php?error=Exists");
					exit(); 
				} else {
					mysqli_stmt_bind_param($stmt,"ssss",$blg_title,$blg_doc,$blg_author,$blg_stamp);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/admin_control_blog.php?success=Added");
					exit(); 
				}
			}

		
		}

	}

	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}

if(isset($_POST['btn_blog_remove'])){
	$blg_code = $_POST['blog_code'];

	if(empty($blg_code)){
		header("location: ../PHP/admin_control_blog.php?error=emptyCode");
		exit();	
	} else {
		$sql = "SELECT * FROM articles_tbl WHERE a_id=?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/admin_control_blog.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$blg_code);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){

				$sql = "DELETE FROM articles_tbl WHERE a_id=?";

				$stmt = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt,$sql)){
					header("location: ../PHP/admin_control_blog.php?error=sqlErrors");
					exit();	
				} else {
					mysqli_stmt_bind_param($stmt,"s",$row['a_id']);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/admin_control_blog.php?success=Removed");
					exit(); 
				}


			} else {
				header("location: ../PHP/admin_control_blog.php?error=ghostCode");
				exit();	
			}
		}

	}
	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}


if(isset($_POST['btn_blog_update'])){
	
	$blg_code = $_POST['blog_code'];
	$blg_title = $_POST['blog_title'];
	$blg_author = $_POST['blog_author'];
	$blg_doc = $_POST['blog_doc'];
	$blg_stamp = date("Y-m-d H:i:s");

	if(empty($blg_title) ||
		empty($blg_author) || 
		empty($blg_doc) ||
		empty($blg_stamp)) {

		header("location: ../PHP/admin_control_blog.php?error=emptyFields");
		exit();	
	} elseif (preg_match("/[0-9]/", $blg_author)){
		header("location: ../PHP/admin_control_blog.php?error=numeric");
		exit();
	} else {
		$sql = "SELECT * FROM articles_tbl WHERE a_id=?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/admin_control_blog.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$blg_code);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){

				$sql = "UPDATE articles_tbl SET a_title=?, a_doc_location=?, a_author=?, a_stamp=? WHERE a_id=?";

				$stmt = mysqli_stmt_init($conn);

				if(!mysqli_stmt_prepare($stmt,$sql)){
					header("location: ../PHP/admin_control_blog.php?error=sqlErrors");
					exit();	
				} else {
					mysqli_stmt_bind_param($stmt,"sssss",$blg_title,$blg_doc,$blg_author,$blg_stamp,$blg_code);
					mysqli_stmt_execute($stmt);
					header("location: ../PHP/admin_control_blog.php?success=Updated");
					exit(); 
				}

			} else {
				header("location: ../PHP/admin_control_blog.php?error=ghostCode");
				exit();	
			}
		}

	}	
	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}

if(isset($_POST['btn_blog_get'])){
	
	$blg_code = $_POST['blog_code'];
	$blg_title ="";
	$blg_author = "";
	$blg_doc = "";
	$blg_stamp = "";

	if(empty($blg_code)){
		header("location: ../PHP/admin_control_blog.php?error=emptyCode");
		exit();	
	} else {
		$sql = "SELECT * FROM articles_tbl WHERE a_id=?";
		$stmt = mysqli_stmt_init($conn);
		if(!mysqli_stmt_prepare($stmt,$sql)){
			header("location: ../PHP/admin_control_blog.php?error=sqlErrors");
			exit();	
		} else {
			mysqli_stmt_bind_param($stmt,"s",$blg_code);
			mysqli_stmt_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){
					$blg_title = str_replace(" ", "_", $row['a_title']);
					$blg_author = str_replace(" ", "_", $row['a_author']);
					$blg_doc = str_replace(" ", "_", $row['a_doc_location']);
					$blg_stamp = str_replace(" ", "_", $row['a_stamp']);

					header("location: ../PHP/admin_control_blog.php?dataFetched&Code=".$blg_code."&Title=".$blg_title."&Author=".$blg_author."&DocPath=".$blg_doc."&Stamp=".$bog_stamp);
					exit();
			} else {
				header("location: ../PHP/admin_control_blog.php?error=ghostCode");
				exit();	
			}
		}

	}
	mysqli_stmt_close($stmt);
	mysqli_close($conn);
}
//Session varaibles
//unset specifc on the home button

	//btn_blog_get/add/update/remove



//click button sends values back in the header rip values from the header