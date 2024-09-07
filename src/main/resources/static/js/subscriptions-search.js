$(document).ready(function() {
    let originalContainer = $('.subscription-container').clone();
    let subscriptionCards = $('.subscription-card');
    let noSubscriptionsMessage = $('<div class="no-subscriptions">No subscriptions</div>');

    $('#search-input').on('input', function() {
        let query = $(this).val().toLowerCase();
        subscriptionCards.each(function() {
            let subscriptionTitle = $(this).find('.subscription-text:first').text().toLowerCase();

            if (subscriptionTitle.includes(query)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });

        let visibleSubscriptions = subscriptionCards.filter(':visible');
        if (visibleSubscriptions.length === 0) {
            if ($('.no-subscriptions').length === 0) {
                $('.subscription-container').append(noSubscriptionsMessage);
            }
        } else {
            $('.no-subscriptions').remove();
        }
    });
});