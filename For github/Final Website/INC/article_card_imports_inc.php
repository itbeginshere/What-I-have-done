<?php  
			require '../INC/database_helper_inc.php';

			$sql = "SELECT * FROM articles_tbl";
			$stmt = mysqli_stmt_init($conn);

			if(!mysqli_stmt_prepare($stmt, $sql)){
				header("location: ../PHP/BlogPage.php?error=sqlErrors");
				exit();
			} else {
				mysqli_execute($stmt);
				$result = mysqli_stmt_get_result($stmt);

				while($row = mysqli_fetch_assoc($result)){
					
					$art_num = $row['a_id'];
					$art_title = $row['a_title'];
					$art_author = $row['a_author'];
					$art_stamp = $row['a_stamp'];
					$art_file = $row['a_doc_location'];
					$art_brief = "";
					
 
					$char_count = 0;
	
					$myfile = Fopen($art_file, "r") or die("unable to open the file.");
						while ($char_count < 282) {
							$art_brief = $art_brief.fgetc($myfile);
							$char_count++;
						};
			
					fclose($myfile);
					$char_count = 0;


					echo '
						<div class="article-card">
							<form class="article-form" action="../PHP/ArticlePage.php" method="post">
								<h1 class="article-title">'.$art_title.'</h1>
								<div class="article-log">
									<span class="article-author">'.$art_author.'</span>
									<span class="article-stamp">'.$art_stamp.'</span>
								</div>
								<div class="article-brief">
									<p class="first-para"> '.$art_brief.'...</p>
								</div>

								<input type="text" class="hidden" name="art_num" value="'.$art_num.'">
					
								<input type="submit" name="btn_read_more" value="read more...">
								
							</form>
						</div> ';


				}
			}

			mysqli_stmt_close($stmt);
			mysqli_close($conn);
