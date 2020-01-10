	<?php 
	

		require '../INC/database_helper_inc.php';
							
					$pay_username = $_SESSION['userName'];
					$pay_name = "";
					$pay_code = "";
					$pay_no = "";
					$pay_exp_y = "";
					$pay_exp_m = "";

					if(empty($pay_username)){
						header("location: ../PHP/profile_control_payments.php?error=websiteError");
						exit();	
					} else {
						$sql = "SELECT * FROM payment_tbl WHERE pay_u_u_name=?";
						$stmt = mysqli_stmt_init($conn);
						if(!mysqli_stmt_prepare($stmt,$sql)){
							header("location: ../PHP/profile_control_payments.php?error=sqlErrors1");
							exit();	
						} else {
							mysqli_stmt_bind_param($stmt,"s",$pay_username);
							mysqli_stmt_execute($stmt);

							$result = mysqli_stmt_get_result($stmt);

							if($row = mysqli_fetch_assoc($result)){

									$pay_name = str_replace(" ", "_", $row['pay_card_name']);
									$pay_code = str_replace(" ", "_", $row['pay_card_code']);
									$pay_no = str_replace(" ", "_", $row['pay_card_no']);
									$pay_exp_y = str_replace(" ", "_", $row['pay_exp_year']);
									$pay_exp_m = str_replace(" ", "_", $row['pay_exp_month']);
								
									echo '<div class="text-box-strip">
											<input type="text" name="pay_name" placeholder="PAYMENT_CARD_NAME" value='.$pay_name.'>
											<input type="text" name="pay_crd_code" placeholder="PAYMENT_CARD_CODE" value='.$pay_code.'>
											<input type="text" name="pay_crd_no" placeholder="PAYMENT_CARD_NO" value='.$pay_no.'>
											<input type="text" name="pay_exp_y" placeholder="PAYMENT_EXPIRE_YEAR" value='.$pay_exp_y.'>
											<input type="text" name="pay_exp_m" placeholder="PAYMENT_EXPIRE_MONTH" value='.$pay_exp_m.'>
											<input type="text" class="hide" name="pay_user" value='.$pay_username.'>
										</div>
										<div class="button-strip">
											<input type="submit" name="btn_pay_add" value="add">
											<input type="submit" name="btn_pay_update" value="update">
											<input type="submit" name="btn_pay_remove" value="remove">
										</div>';
							} else {
								echo '<div class="text-box-strip">
											<input type="text" name="pay_name" placeholder="PAYMENT_CARD_NAME" value='.$pay_name.'>
											<input type="text" name="pay_crd_code" placeholder="PAYMENT_CARD_CODE" value='.$pay_code.'>
											<input type="text" name="pay_crd_no" placeholder="PAYMENT_CARD_NO" value="'.$pay_no.'">
											<input type="text" name="pay_exp_y" placeholder="PAYMENT_EXPIRE_YEAR" value='.$pay_exp_y.'>
											<input type="text" name="pay_exp_m" placeholder="PAYMENT_EXPIRE_MONTH" value='.$pay_exp_m.'>
											<input type="text" class="hide" name="pay_user" value='.$pay_username.'>
										</div>
										<div class="button-strip">
											<input type="submit" name="btn_pay_add" value="add">
											<input type="submit" name="btn_pay_update" value="update">
											<input type="submit" name="btn_pay_remove" value="remove">
										</div>';
									}	
								
								}
							}

						mysqli_stmt_close($stmt);
						mysqli_close($conn);