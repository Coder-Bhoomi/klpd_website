// Sample product data
const products = [
    { id: 1, name: "Product A", price: 20.00, image: "path_to_product_image" },
    { id: 2, name: "Product B", price: 15.00, image: "path_to_product_image" },
    { id: 3, name: "Product C", price: 25.00, image: "path_to_product_image" }
];

// Function to calculate total price
function calculateTotal() {
    let total = 0;
    document.querySelectorAll('.cart-item').forEach(item => {
        const price = parseFloat(item.dataset.price);
        const quantity = parseInt(item.querySelector('.quantity-input').value);
        total += price * quantity;
    });
    document.getElementById('total-price').textContent = `$${total.toFixed(2)}`;
}

// Function to update quantity
function updateQuantity(event) {
    const quantityInput = event.target;
    if (quantityInput.value < 1) {
        quantityInput.value = 1; // Prevent negative quantity
    }
    calculateTotal();
}

// Function to remove item
function removeItem(event) {
    const itemRow = event.target.closest('.cart-item');
    itemRow.remove();
    calculateTotal();
}

// Function to render cart items
function renderCart() {
    const cartBody = document.getElementById('cart-body');
    products.forEach(product => {
        const tr = document.createElement('tr');
        tr.classList.add('cart-item');
        tr.dataset.price = product.price;
        tr.innerHTML = `
            <td>
                <div class="product-info">
                    <img src="${product.image}" alt="${product.name}" class="product-image">
                    <span>${product.name}</span>
                </div>
            </td>
            <td>$${product.price.toFixed(2)}</td>
            <td>
                <input type="number" value="1" min="1" class="quantity-input">
            </td>
            <td>$${product.price.toFixed(2)}</td>
            <td><button class="remove-btn">Remove</button></td>
        `;
        cartBody.appendChild(tr);
    });

    // Attach event listeners to quantity inputs and remove buttons
    document.querySelectorAll('.quantity-input').forEach(input => {
        input.addEventListener('input', updateQuantity);
    });
    document.querySelectorAll('.remove-btn').forEach(button => {
        button.addEventListener('click', removeItem);
    });

    calculateTotal();
}

// Initial render
renderCart();
