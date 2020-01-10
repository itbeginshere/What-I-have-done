<?php 
	require 'header_default.php';

?>
<main>
	
	<div class="blog-container">
		<div class="article-conent">
			<div class="articles-container">
			<?php		
				require '../INC/article_card_imports_inc.php';
			?>
			</div>
		</div>	
		<div class="blog-controls">
			<form class="modify-blog-form" action="../PHP/admin_control_blog.php" method="post">	
				<?php 
					require '../INC/admin_ctrls_inc.php';
				?>
			</form>	
		</div>
	</div>	
</main>
<?php
	require 'footer_default.php';
?>

