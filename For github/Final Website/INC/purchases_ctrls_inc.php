<?php 

	require '../INC/database_helper_inc.php';

	$total_amount = 0;

	$sql_1 = "SELECT t.t_id, t.t_date, t.t_amount, s.s_p_name, s.s_sold FROM trans_tbl t, sold_items_tbl s WHERE t.t_id = s.t_id AND t.t_u_name = ?;";

	$stmt_1 = mysqli_stmt_init($conn);

	if(!mysqli_stmt_prepare($stmt_1, $sql_1)){
		echo '<p class="error-para">Something went wrong with the database connection</p>';
		exit();
	} else {
		mysqli_stmt_bind_param($stmt_1, "s", $_SESSION['userName']);
		mysqli_stmt_execute($stmt_1);

		$result = mysqli_stmt_get_result($stmt_1);

		while ($row = mysqli_fetch_assoc($result)){

			$product_name = $row['s_p_name'];
			$trans_id = $row['t_id'];
			$trans_date = $row['t_date'];
			$trans_quan = $row['s_sold'];

			$sql_2 = "SELECT p_price FROM products_tbl WHERE p_name=?";
			$stmt_2 = mysqli_stmt_init($conn);

			if(!mysqli_stmt_prepare($stmt_2, $sql_2)){
				echo '<p class="error-para">Something went wrong with the database connection</p>';
				exit();
			} else{
				mysqli_stmt_bind_param($stmt_2, "s", $product_name);
				mysqli_stmt_execute($stmt_2);				

				$result_price = mysqli_stmt_get_result($stmt_2);

				if($row2 = mysqli_fetch_assoc($result_price)){

					$price = $row2['p_price'];
					
					$placeholder = floatval((str_replace("R", "", $price))) * $trans_quan;

					$total_amount = $total_amount + $placeholder;


					echo 
					'	<tr>
						<td>'.$trans_id.'</td>
						<td>'.$product_name.'</td>
						<td>'.$trans_date.'</td>
						<td>'.$trans_quan.'</td>
						<td>'.$price.'</td>
					</tr>';


				} else {
					echo '<p class="error-para">Something went wrong with the database connection</p>';
					exit();
				}


			}
		}

		echo '
			
			<div class="centered">
			 <label>Total amount spent R'.$total_amount.'</label>	
			</div>';
	}

	mysqli_stmt_close($stmt_1);
	mysqli_close($conn);