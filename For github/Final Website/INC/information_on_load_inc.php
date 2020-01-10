	<?php 
	

		require '../INC/database_helper_inc.php';
							
					$info_username = $_SESSION['userName'];
					$info_first = "";
					$info_last = "";
					$info_email = "";

					if(empty($info_username)){
						header("location: ../PHP/profile_control_information.php?error=websiteError");
						exit();	
					} else {
						$sql = "SELECT * FROM users_tbl WHERE u_u_name=?";
						$stmt = mysqli_stmt_init($conn);
						if(!mysqli_stmt_prepare($stmt,$sql)){
							header("location: ../PHP/profile_control_information.php?error=sqlErrors1");
							exit();	
						} else {
							mysqli_stmt_bind_param($stmt,"s",$info_username);
							mysqli_stmt_execute($stmt);

							$result = mysqli_stmt_get_result($stmt);

							if($row = mysqli_fetch_assoc($result)){

									$info_first = str_replace(" ", "_", $row['u_f_name']);
									$info_last = str_replace(" ", "_", $row['u_l_name']);
									$info_email = str_replace(" ", "_", $row['u_email']);
				
									echo '<div class="text-box-strip">
											<input type="text" name="info_f_name" placeholder="FIRST_NAME" value='.$info_first.'>
											<input type="text" name="info_l_name" placeholder="LAST_NAME" value='.$info_last.'>
											<input type="text" name="info_email" placeholder="EMAIL" value='.$info_email.'>
											<input type="text" name="info_user" placeholder= "USER_NAME" value='.$info_username.'>
										</div>
										<div class="button-strip">
											<input type="submit" name="btn_pay_update" value="update">
											<input type="submit" id="delete-account" name="btn_pay_remove" value="delete account">
										</div>';
							} else {
									echo '<div class="text-box-strip">
											<input type="text" name="info_f_name" placeholder="FIRST_NAME" value='.$info_first.'>
											<input type="text" name="info_l_name" placeholder="LAST_NAME" value='.$info_last.'>
											<input type="text" name="info_email" placeholder="EMAIL" value='.$info_email.'>
											<input type="text" name="info_user" placeholder= "USER_NAME" value='.$info_username.'>
										</div>
										<div class="button-strip">
											<input type="submit" name="btn_pay_update" value="update">
											<input type="submit" id="delete-account" name="btn_pay_remove" value="delete account">
										</div>';
									}	
								
								}
							}

						mysqli_stmt_close($stmt);
						mysqli_close($conn);