<?php 

require '../INC/database_helper_inc.php';
	
	if(isset($_POST['btn_read_more'])){

		$art_num = $_POST['art_num'];
		$art_title = "";
		$art_author = "";
		$art_stamp = "";
		$art_full = "";
		$art_path = "";

		$sql = "SELECT * FROM articles_tbl WHERE a_id = ?";
		$stmt = mysqli_stmt_init($conn);

		if(!mysqli_stmt_prepare($stmt, $sql)){
			header("location: ../PHP/ArticlePage.php?error=sqlErrors");
			exit();
		} else {
			mysqli_stmt_bind_param($stmt,"d",$art_num);
			mysqli_execute($stmt);

			$result = mysqli_stmt_get_result($stmt);

			if($row = mysqli_fetch_assoc($result)){
				$art_title = $row['a_title'];
				$art_author = $row['a_author'];
				$art_stamp = $row['a_stamp'];
				$art_path = $row['a_doc_location'];
				$count = 0;

				$art_full = file_get_contents($art_path);
   				$art_full = nl2br($art_full);

				echo ' 
					<div class="article-conatiner">
						<h1 class="article-title">'.$art_title.'</h1>
						<div class="article-log">
							<span class="article-author">'.$art_author.'</span>
							<span class="article-stamp">'.$art_stamp.'</span>
						</div>
						<div class="article-brief">
							<p class="first-para">'.$art_full.'</p>
						</div
					</div> ';



			} else {
				echo "error 404 file not found";
			}
		}


	} else {
		echo "error 404 file not found";

	}