$(document).ready(function() {
    let originalContainers = $('.transactions-container').clone();
    let transactionBoxes = $('.transaction-card');
    let noTransactionsMessage = $('<div class="no-transactions">No transactions</div>');

    $('#search-input').on('input', function() {
        let query = $(this).val().toLowerCase();
        transactionBoxes.each(function() {
            let transactionTitle = $(this).find('.transaction-title').text().toLowerCase();
            let transactionId = $(this).find('input[type="hidden"]').val();
            let transactionDate = $(this).find('.transaction-detail:last-child div:last-child').text();

            if (transactionTitle.includes(query) || transactionId.includes(query) || transactionDate.includes(query)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });

        let visibleTransactions = transactionBoxes.filter(':visible');
        if (visibleTransactions.length === 0) {
            if ($('.no-transactions').length === 0) {
                $('.transactions-container').append(noTransactionsMessage);
            }
        } else {
            $('.no-transactions').remove();
        }
    });
});