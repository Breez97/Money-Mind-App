$(document).ready(function() {
    $('.add-subscription-button').on('click', function() {
       $('#addModal').show();
    });

    $('.edit-button').on('click', function() {
        let subscription = $(this).closest('.subscription-info');
        let amountText = subscription.find('.subscription-text').eq(1).text().trim();
        let amount = parseFloat(amountText.replace('$', ''));
        if (!isNaN(amount)) amount = amount.toFixed(2);
        else amount = '0.00';

        $('#edit-id').val(subscription.find('input[type="hidden"]').val());
        $('#edit-title').val(subscription.find('.subscription-text').eq(0).text());
        $('#edit-frequency').val(subscription.find('.subscription-text').eq(2).text());
        $('#edit-amount').val(amount);
        $('#edit-date').val(subscription.find('.subscription-text').eq(3).text());

        $('#editModal').show();
    });

    $('.delete-button').on('click', function() {
        let subscription = $(this).closest('.subscription-info');
        let id = subscription.find('input[type="hidden"]').val();

        $('#deleteModal').show();
        $('#deleteModal').find('input[type="hidden"]').val(id);
    })

    $('.close').on('click', function() {
        $('#addModal').hide();
        $('#editModal').hide();
        $('#deleteModal').hide();
        $('#addErrorMessages').html('');
        $('#editErrorMessages').html('');
    });

    $('#cancelDeleteButton').on('click', function() {
        $('#deleteModal').hide();
    });
});