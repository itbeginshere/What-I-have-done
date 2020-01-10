<?php

if(isset($_POST['btn_purchase'])){
	$coun = 0;
	$username = $_POST['hidden_text_username'];

	$item_string = $_POST['hidden_text_items']; 
	
	$total = $_POST['hidden_text_total'];

	$date_dmy = date('d/m/Y H:m:s');
 	$pos = strpos($item_string, '_');

 	require '../INC/database_helper_inc.php';

	$sql_1 = "INSERT INTO trans_tbl(t_u_name, t_date, t_amount) VALUES(?,?,?)";
	$stmt_1 = mysqli_stmt_init($conn);

	if(!mysqli_stmt_prepare($stmt_1, $sql_1)){
		header("location: ../PHP/StorePage.php?error=sqlError3");
		exit();
	} else {
		mysqli_stmt_bind_param($stmt_1,"sss", $username, $date_dmy, $total);
		mysqli_stmt_execute($stmt_1);
	}

	$reuslt_trans_id = mysqli_query($conn, "SELECT * FROM trans_tbl ORDER BY t_id DESC LIMIT 1");
	$row = mysqli_fetch_assoc($reuslt_trans_id);
	$tran_id = $row['t_id'];

 	while ($pos !== false){
		$item_name = "";
		$item_qauntity = "";
		$p_old_quantity = 0;

		if($item_string === ''){
			break;
		}
		
		$pos = strpos($item_string, '_');	
		
		$item_name = substr($item_string, 0, $pos);

		$item_string = substr($item_string, $pos + 1, strlen($item_string));
	
		$pos = strpos($item_string, '#');		
		
		$item_qauntity = substr($item_string, 0, $pos);

		$item_string = substr($item_string, $pos + 1, strlen($item_string));

		
		$sql_2 = "INSERT INTO sold_items_tbl(s_date, s_p_name, s_sold, t_id) VALUES(?,?,?,?)";
		$sql_3 = "SELECT * FROM products_tbl WHERE p_name = ?"; //get quantity 
		$sql_4 = "UPDATE products_tbl SET p_quantity = ? WHERE p_name = ?";
		$sql_5 = "UPDATE products_tbl SET p_quantity = ?, p_backordered = ? WHERE p_name = ?";

		
		$stmt_2 = mysqli_stmt_init($conn);
		$stmt_3 = mysqli_stmt_init($conn);
		$stmt_4 = mysqli_stmt_init($conn);
		$stmt_5 = mysqli_stmt_init($conn);


		if(!mysqli_stmt_prepare($stmt_2, $sql_2) ||
			!mysqli_stmt_prepare($stmt_3, $sql_3) ||
			!mysqli_stmt_prepare($stmt_4, $sql_4) ||
			!mysqli_stmt_prepare($stmt_5, $sql_5)){

			header("location: ../PHP/StorePage.php?error=sqlError1");
			exit();
		 } else {
		 
		  	mysqli_stmt_bind_param($stmt_2,"ssdd", $date_dmy, $item_name, $item_qauntity, $tran_id);
		 	mysqli_stmt_bind_param($stmt_3, "s", $item_name);

		 	mysqli_stmt_execute($stmt_2);
			mysqli_stmt_execute($stmt_3);

			$result = mysqli_stmt_get_result($stmt_3);

			if(!$row = mysqli_fetch_assoc($result)){
				header("location: ../PHP/StorePage.php?error=sqlError2");
				exit();
			} else {
				$p_old_quantity = $row['p_quantity'];
				$p_new_quantity = $p_old_quantity - $item_qauntity;

				if($p_new_quantity >= 0){
					mysqli_stmt_bind_param($stmt_4,"ds", $p_new_quantity, $item_name);
					mysqli_stmt_execute($stmt_4);
					$coun = 1;
				} else {
					//back order
					$p_new_quantity = $p_new_quantity*-1;
					$p_new_back_order = $p_new_quantity;
					$p_new_quantity = 0;

					$coun = 2;
					mysqli_stmt_bind_param($stmt_5,"dds", $p_new_quantity, $p_new_back_order, $item_name);
					mysqli_stmt_execute($stmt_5);

				}

			}


		 }
	}

	

	mysqli_stmt_close($stmt_1);
	mysqli_stmt_close($stmt_2);
	mysqli_stmt_close($stmt_3);
	mysqli_stmt_close($stmt_4);
	mysqli_stmt_close($stmt_5);

	mysqli_close($conn);
	header("location: ../PHP/StorePage.php?success=purchased&COUNTER=".$tran_id);
	exit();
} else {
	header("location: ../PHP/StorePage.php?error=purchaseError");
	exit();
}