if(document.readyState == 'loading'){
	document.addEventListener('DOMContentLoaded', ready());
} else {
	ready();
}


function ready(){
	var removeCartItemButtons = document.getElementsByClassName('btn-remove'); //store all the elements with the calss btn-remove

	//adding event listners to each button stored in the removeCartItemBUttons
	for (var k = 0; k < removeCartItemButtons.length ; k++) {
		var button = removeCartItemButtons[k];
		//event listener to remove the cart item when the button is clicked
		button.addEventListener('click', removeCartItem);
	}

	var quantityInputs = document.getElementsByClassName('cart-quantity-input');

	for(var j = 0; j < quantityInputs.length; j++){
		var input = quantityInputs[j];

		input.addEventListener('change', quantityChanged);
	}


	var addToCartButtons = document.getElementsByClassName('cart-button');

	for(var a = 0; a < addToCartButtons.length; a++){
		var addButton = addToCartButtons[a];

		addButton.addEventListener('click', addToCartClicked);
	}

	var purcahseButton = document.getElementsByClassName('btn-purchase')[0];
	purcahseButton.addEventListener('click', purchasedClicked);
}


function purchasedClicked(event){
	
	var while_counter = 0;
	var itemsToString = "";
	var cartItems = document.getElementsByClassName('cart-items')[0];
	var value = document.getElementsByClassName('cart-total-price')[0].innerText;

	if(cartItems.getElementsByClassName('cart-row').length == 0){
		alert('Your cart is empty!!!');
		return;
	}

	while (cartItems.hasChildNodes()){

		if(while_counter != 0){
			var itemQuantity = cartItems.getElementsByClassName('cart-quantity-input')[0].value;
			var itemName = cartItems.getElementsByClassName('cart-item-title')[0].innerText;

			itemsToString = itemsToString + (itemName + "_" + itemQuantity + "#");
			
		}

		cartItems.removeChild(cartItems.firstChild);

		while_counter++;
	}

	var cartForm = document.getElementsByClassName('cart-store-form')[0];
	var newDiv = document.createElement('div');
	newDiv.classList.add('hidden-div');

	var newDivContents = '<input type="text" class="hidden" name="hidden_text_items" value="'+itemsToString+'"><input type="text" class="hidden" name="hidden_text_total" value="'+value+'">';
	newDiv.innerHTML = newDivContents;
	cartForm.append(newDiv);

	updateCartTotal();
	alert('Thank you for your purcahse!');
}

function removeCartItem(event){
	var buttonClicked = event.target;
		buttonClicked.parentElement.parentElement.remove();
		updateCartTotal();
}

function quantityChanged(event){
	var input = event.target;

	if(isNaN(input.value) || input.value <= 0){
		input.value = 1;
	}
	updateCartTotal();
}


function addToCartClicked(event){
	var button = event.target;
	var shopItem = button.parentElement.parentElement;
	var nameElement = shopItem.getElementsByClassName('item-name')[0].innerText;
	var priceElement = shopItem.getElementsByClassName('item-price')[0].innerText;
	
	addItemToCart(nameElement,priceElement);
	updateCartTotal();
	
}


function addItemToCart(name, price){
	var name = name.toUpperCase();
	var cartRow = document.createElement('div');
	cartRow.classList.add('cart-row');
	var cartItems = document.getElementsByClassName('cart-items')[0];
	var cartItemsNames = cartItems.getElementsByClassName('cart-item-title');
	for(var b = 0; b < cartItemsNames.length; b++){
		if (cartItemsNames[b].innerText == name){
			alert("You already have that item in your cart!!!");
			return;
		}
	}

	var cartRowContents = '<div class="cart-item cart-column"><span class="cart-item-title">'+ name +'</span></div><span class="cart-price cart-column">'+price+'</span><div class="cart-quantity cart-column"><input type="number" class="cart-quantity-input" value="1"><button class="btn btn-remove" type="button">remove</button></div>';
	cartRow.innerHTML = cartRowContents;
	cartItems.append(cartRow);
	cartRow.getElementsByClassName('btn-remove')[0].addEventListener('click', removeCartItem);
	cartRow.getElementsByClassName('cart-quantity-input')[0].addEventListener('change',quantityChanged);

}

function updateCartTotal(){
	var carItemContainer = document.getElementsByClassName('cart-items')[0];
	var cartRows = carItemContainer.getElementsByClassName('cart-row');
	var total = 0;

	for(var l = 0; l < cartRows.length; l++){
		var cartRow = cartRows[l];
		var priceElement = cartRow.getElementsByClassName('cart-price')[0];
		var quantityElement = cartRow.getElementsByClassName('cart-quantity-input')[0];

		var price = parseFloat(priceElement.innerText.replace('R',''));
		var qauntity = quantityElement.value;

		total = total + (price * qauntity);
		

	}	
	total = Math.round(total*100) / 100;
	document.getElementsByClassName('cart-total-price')[0].innerText ='R' + total;
}

function createHiddenInputDif(cName, value){

	var inputName = cName;
	var inputValue = value;
	var cartForm = document.getElementsByClassName('cart-store-form')[0];
	var newInput = document.createElement('input');
	newInput.setAttribute('type','text');
	newInput.setAttribute('name', inputName);
	newInput.setAttribute('value', inputValue);
	newInput.classList.add('hidden');

	cartForm.append(newInput);
	console.log(newInput);
}
