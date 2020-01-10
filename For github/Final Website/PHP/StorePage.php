<?php 
	require 'header_default.php';
?>
<main>
<div class="store">
	<div class="store-top">
		<div class="side-filters">
			<form class="filters-form" name="same-form" action="../PHP/StorePage.php" method="post">
				
				<div class="fil-from-strip">
					<select name="fil_from" id="fil_from">
						<option>Any</option>
						<option>American</option>
						<option>Irish</option>
						<option>Scotland</option>
						<option>South African</option>
						<option>France</option>
						<option>Japanese</option>
					</select>
				</div>

				<div class="fil-range-strip">
					<span class="range-txt-begin">from:</span>
					<input type="text" name="fil_low_price" placeholder="START_RANGE">
					<span class="range-txt-mid">to:</span>
					<input type="text" name="fil_high_price" placeholder="END_RANGE">
				</div>

				<div class="fil-checked-strip">
					<span class="checked-txt-begin">States:</span>
					<label><input type="radio" name="state" value="Any" checked>Any</label>
					<label><input type="radio" name="state" value="Featured">Featured</label>
					<label><input type="radio" name="state" value="Trending">Trending</label>
					<label><input type="radio" name="state" value="Sale">Sale</label>
					<span class="checked-txt-mid">Order:</span>
					<label><input type="radio" name="order" value="A-Z" checked>A-Z</label>
					<label><input type="radio" name="order" value="Z-A">Z-A</label>

				</div>
				<div class="fil-submit-strip">
					<input type="text" name="srch_txt" placeholder="Search...">
					<input type="submit" name="btn_srch_submit" value="search">
				</div>
			</form>
		</div>

		<div class="shop-container">
			<div class="shop-products">
				<div class="card-container">
					
					<?php 
			
						require '../INC/store_search_inc.php';
					
					 ?>
					
				</div>
			</div>			
		</div>

	</div>




	<div class="store-bottom">
		<div class="admin-form">
			<form class="modify-stock-form" action="../PHP/admin_control_store.php" method="post">
				<?php 
					require '../INC/admin_ctrls_inc.php';
				?>
			</form>	
		</div>
		<div class="cart-container">
			<form class="cart-store-form"  action="../INC/purchase_update_inc.php" method="post">
			<h3 class="form-header">cart</h3>
			<div class="cart-row">
				<span class="cart-item cart-header cart-column">item</span>
				<span class="cart-price cart-header cart-column">price</span>
				<span class="cart-quantity cart-header cart-column">quantity</span>
			</div>	
			<div class="cart-items">
					
			</div>
			<div class="cart-total">
				<strong class="cart-total-title">Total</strong>
				<span class="cart-total-price">R0.00</span>
			</div>

			<?php 

			if(isset($_SESSION['userName']) != "" && isset($_SESSION['pay']) != "" /*&& isset($_SESSION['address']) != ""*/){
					echo '<button class="btn btn-purchase" type="submit" name="btn_purchase">purchase</button>';
					echo '<input type="text" class="hidden" name="hidden_text_username" value='.$_SESSION['userName'].'>';
						
			} else {
					echo '<p class="notif-para">You need to be logged in to purchase the items</p>';	
					echo '<p class="notif-para">You need to have a payment method to purchase the items. Please add one by clicking on the PROFILE section and navigate to the PAYMENT section</p>';
					echo '<p class="notif-para">You need to have a address to purchase the items. Please add one by clicking on the PROFILE section and navigate to the ADDRESS section</p>';		
			}
			?>
			

			</form>			
		</div>
	</div>
</div>



	
</main>

<?php
	require 'footer_default.php';
?>