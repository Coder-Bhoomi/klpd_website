// Function to change the main image
function changeImage(thumbnail) {
    document.getElementById("mainImage").src = thumbnail.src;
}

// Function to increase/decrease quantity
function changeQuantity(amount) {
    const quantityInput = document.getElementById("quantityInput");
    let quantity = parseInt(quantityInput.value);
    quantity = isNaN(quantity) ? 1 : quantity;
    quantity = Math.max(1, quantity + amount); // Prevent quantity from going below 1
    quantityInput.value = quantity;
}
