<?php 
	if (isset($_SESSION['userName'])) {
						if($_SESSION['userName'] == 'Admin'){
						echo '<h2 class="crtls-heading">Controls</h2>
								<p class="crtls-para">The button below will bring you to the controls page. This can only be accessed by you, the admin.</p>
								<input type="submit" name="btn_crtls_page" value="controls">
								';
						} else {
							echo '';
						}		
					} else {
						echo '';
					}