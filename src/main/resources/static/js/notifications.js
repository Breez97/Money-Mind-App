$(document).ready(function() {
    $(document).ready(function() {
        $('#enableTelegram').on('click', function() {
            var button = $(this);
            var notificationId = button.data('id');
            var isEnabled = button.hasClass('notification-enabled');

            $.ajax({
                url: '/notification-enable-update',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    telegramEnabled: !isEnabled
                }),
                success: function(response) {
                    if (response.success) {
                        button.toggleClass('notification-enabled notification-disabled');
                        button.text(isEnabled ? 'Disabled' : 'Enabled');
                    } else {
                        alert('Error updating notification.');
                    }
                }
            });
        });
    });
});