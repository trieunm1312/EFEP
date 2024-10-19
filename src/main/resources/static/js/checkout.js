//Checkout
function handleCheckout() {
    const paymentMethod = document.getElementById('paymentMethod').value;
    const checkoutForm = document.getElementById('checkout');

    const codActionUrl = checkoutForm.getAttribute('data-cod-action');
    const vnpayActionUrl = checkoutForm.getAttribute('data-vnpay-action');

    if (paymentMethod === 'COD') {
        checkoutForm.setAttribute('action', codActionUrl);
    } else if (paymentMethod === 'VNPay') {
        checkoutForm.setAttribute('action', vnpayActionUrl);
    }
    return true;
}