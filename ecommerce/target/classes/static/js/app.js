// ShopZone App JS

// Auto-dismiss alerts
document.addEventListener('DOMContentLoaded', function () {
    const alerts = document.querySelectorAll('.alert.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 4000);
    });
});

// Quantity adjuster
function adjustQty(delta) {
    const input = document.getElementById('quantity');
    if (!input) return;
    const newVal = parseInt(input.value) + delta;
    const max = parseInt(input.max) || 99;
    const min = parseInt(input.min) || 1;
    if (newVal >= min && newVal <= max) input.value = newVal;
}

// Format price
function formatPrice(num) {
    return '$' + parseFloat(num).toFixed(2);
}

console.log('ShopZone loaded!');
