<?php 


	require '../INC/database_helper_inc.php';



	$sql = 'SELECT * FROM products_tbl';
	$stmt = mysqli_stmt_init($conn);


	if(!mysqli_stmt_prepare($stmt, $sql)){
		echo '<p class="error-para">Something went wrong with the database connection</p>';
		exit();
	} else {
		mysqli_stmt_execute($stmt);

		$result = mysqli_stmt_get_result($stmt);

		while($row = mysqli_fetch_assoc($result)){
			$str_code = $row['p_id'];
			$str_name = $row['p_name'];
			$str_from = $row['p_from'];
			$str_price = $row['p_price'];
			$str_quan = $row['p_quantity'];
			$str_back = $row['p_backordered'];
			$str_state_f = $row['p_featured'];
			$str_state_t = $row['p_trending'];
			$str_state_s = $row['p_sale'];


			echo '	<tr>
						<td>'.$str_code.'</td>
						<td>'.$str_name.'</td>
						<td>'.$str_from.'</td>
						<td>'.$str_price.'</td>
						<td>'.$str_state_f.'</td>
						<td>'.$str_state_t.'</td>
						<td>'.$str_state_s.'</td>
						<td>'.$str_quan.'</td>
						<td>'.$str_back.'</td>
					</tr>';




		}

		mysqli_stmt_close($stmt);
		mysqli_close($conn);
	}

