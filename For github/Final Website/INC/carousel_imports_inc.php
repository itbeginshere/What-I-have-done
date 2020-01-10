<?php 


require '../INC/database_helper_inc.php';

$featrued = 1;
$sql = "SELECT * FROM products_tbl WHERE p_featured = ?";

$stmt = mysqli_stmt_init($conn);


if(!mysqli_stmt_prepare($stmt, $sql)){
	echo 'ERROR FETCHING FROM THE DATABASE';
} else {

	mysqli_stmt_bind_param($stmt, "i", $featrued);
	mysqli_stmt_execute($stmt);


	$result = mysqli_stmt_get_result($stmt);
	$result_length = mysqli_num_rows($result);

	$images = array(); 

	while ($row = mysqli_fetch_assoc($result)) {
		array_push($images, $row['p_img_location']);
	}


}

	echo '
			<button class="carousel-button carousel-button-left is-hidden">
				<img src="../IMGS/arrow_carousel_left.svg">
			</button>
  
  			<div class="carousel-track-container">
  				<ul class="carousel-track">';
  				

  				for ($k = 0; $k < $result_length; $k ++) { 
  					echo '
  					<li class="carousel-slide current-slide">
  						<img class="carousel-image" src='.$images[$k].' alt="image_not_found">	
  					</li>';
  				}



  		echo '</ul>

  			</div>

			<button class="carousel-button carousel-button-right">
				<img src="../IMGS/arrow_carousel_right.svg">
			</button>

			<div class="carousel-nav">';

				

				for ($k = 0; $k < $result_length; $k ++) { 
  					
  					if($k == 0){
  						echo '<button class="carousel-indicator current-indicator"></button>';
  					} else {
						echo '<button class="carousel-indicator "></button>';
  					}

  				}
			echo '</div>

			';