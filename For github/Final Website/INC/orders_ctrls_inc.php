<?php	

	require '../INC/database_helper_inc.php';

	$yesterday_date = date('d/m/Y', strtotime("-1 days")).' 21:00:00';
	$today_date = date('d/m/Y').' 21:00:00';

	$sql = "SELECT t.t_u_name, s.s_p_name, s.s_sold, a.adr_bill_1, a.adr_bill_2, a.adr_poastel_code, a.adr_city, a.adr_province FROM sold_items_tbl s, trans_tbl t, addresses_tbl a WHERE t.t_id = s.t_id AND t.t_u_name = a.adr_u_u_name AND s_date BETWEEN ? AND ?";
	$stmt = mysqli_stmt_init($conn);

	if(!mysqli_stmt_prepare($stmt, $sql)){
		header('location: ../PHP/admin_control_orders.php?error=SQLError');
		exit();
	} else {
		mysqli_stmt_bind_param($stmt,"ss",$yesterday_date, $today_date);
		mysqli_stmt_execute($stmt);

		$result = mysqli_stmt_get_result($stmt);

		while ($row = mysqli_fetch_assoc($result)){
			$username = $row['t_u_name'];
			$product = $row['s_p_name'];
			$qaunitity = $row['s_sold'];
			$billing_1 = $row['adr_bill_1'];
			$billing_2 = $row['adr_bill_2'];
			$poastel = $row['adr_poastel_code'];
			$city = $row['adr_city'];
			$province = $row['adr_province'];


			echo 
					'	<tr>
						<td>'.$username.'</td>
						<td>'.$product.'</td>
						<td>'.$qaunitity.'</td>
						<td>'.$billing_1.'</td>
						<td>'.$billing_2.'</td>
						<td>'.$poastel.'</td>
						<td>'.$city.'</td>
						<td>'.$province.'</td>
					</tr>';

		} 
	}

	mysqli_stmt_close($stmt);
	mysqli_close($conn);
