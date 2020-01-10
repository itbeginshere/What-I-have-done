
<?php 


	require '../INC/database_helper_inc.php';

	$home_featured = 0;
 	$sql = "SELECT * FROM products_tbl WHERE p_featured = ?";
	$stmt = mysqli_stmt_init($conn);

	if(!mysqli_stmt_prepare($stmt, $sql)){
		header("location: ../PHP/HomePage.php");
		exit();
	} else {
		mysqli_stmt_bind_param($stmt, "d", $home_featured);
		mysqli_execute($stmt);

		$result = mysqli_stmt_get_result($stmt);
		$current_slide = 0;
		while ($row = mysqli_fetch_assoc($result)) {
			$image_path = $row['p_img_location'];

			if($current_slide = 0){
				echo '<li class="carousel-slide current-slide">
  						<img class="carousel-image" src="'.$image_path.'" alt="image_not_found">	
  					</li>';
				$current_slide = 4;
			} else {
				echo '<li class="carousel-slide">
  						<img class="carousel-image" src="'.$image_path.'" alt="image_not_found">	
  					</li>';
			}

		}
	}

mysqli_stmt_close($stmt);
mysqli_close($conn);
