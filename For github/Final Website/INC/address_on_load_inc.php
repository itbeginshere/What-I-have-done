	<?php 
	

		require '../INC/database_helper_inc.php';
							
					$adr_username = $_SESSION['userName'];
					$adr_bill_1 = "";
					$adr_bill_2 = "";
					$adr_poastel = "";
					$adr_city = "";
					$adr_province = "";

					if(empty($adr_username)){
						header("location: ../PHP/profile_control_address.php?error=websiteError");
						exit();	
					} else {
						$sql = "SELECT * FROM addresses_tbl WHERE adr_u_u_name=?";
						$stmt = mysqli_stmt_init($conn);
						if(!mysqli_stmt_prepare($stmt,$sql)){
							header("location: ../PHP/profile_control_address.php?error=sqlErrors");
							exit();	
						} else {
							mysqli_stmt_bind_param($stmt,"s",$adr_username);
							mysqli_stmt_execute($stmt);

							$result = mysqli_stmt_get_result($stmt);

							if($row = mysqli_fetch_assoc($result)){

									$adr_bill_1 = str_replace(" ", "_", $row['adr_bill_1']);
									$adr_bill_2 = str_replace(" ", "_", $row['adr_bill_2']);
									$adr_poastel = str_replace(" ", "_", $row['adr_poastel_code']);
									$adr_city = str_replace(" ", "_", $row['adr_city']);
									$adr_province = str_replace(" ", "_", $row['adr_province']);
								
									echo '<div class="text-box-strip">
											<input type="text" name="adr_b_1" placeholder="BILLING_ADDRESS_1" value='.$adr_bill_1.'>
											<input type="text" name="adr_b_2" placeholder="BILLING_ADDRESS_2" value='.$adr_bill_2.'>
											<input type="text" name="adr_post" placeholder="POASTEL_CODE" value='.$adr_poastel.'>
											<input type="text" name="adr_city" placeholder="CITY" value='.$adr_city.'>
											<input type="text" name="adr_pro" placeholder="PROVINCE" value='.$adr_province.'>
											<input type="text" class="hide" name="adr_user" value='.$adr_username.'>
										</div>
										<div class="button-strip">
											<input type="submit" name="btn_adr_add" value="add">
											<input type="submit" name="btn_adr_update" value="update">
											<input type="submit" name="btn_adr_remove" value="remove">
										</div>';
							} else {
								echo '<div class="text-box-strip">
											<input type="text" name="adr_b_1" placeholder="BILLING_ADDRESS_1" value='.$adr_bill_1.'>
											<input type="text" name="adr_b_2" placeholder="BILLING_ADDRESS_2" value='.$adr_bill_2.'>
											<input type="text" name="adr_post" placeholder="POASTEL_CODE" value='.$adr_poastel.'>
											<input type="text" name="adr_city" placeholder="CITY" value='.$adr_city.'>
											<input type="text" name="adr_pro" placeholder="PROVINCE" value='.$adr_province.'>
											<input type="text" class="hide" name="adr_user" value='.$adr_username.'>
										</div>
										<div class="button-strip">
											<input type="submit" name="btn_adr_add" value="add">
											<input type="submit" name="btn_adr_update" value="update">
											<input type="submit" name="btn_adr_remove" value="remove">
										</div>';
									}	
								
								}
							}

						mysqli_stmt_close($stmt);
						mysqli_close($conn);