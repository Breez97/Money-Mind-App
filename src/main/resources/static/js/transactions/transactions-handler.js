$(document).ready(function() {
    $('#addForm').submit(function(e) {
        e.preventDefault();
        $.ajax({
            url: '/add-transaction',
            type: 'POST',
            data: $(this).serialize(),
            success: function(response) {
                location.reload();
            },
            error: function(xhr) {
                let errors = xhr.responseJSON;
                let errorMessages = '';
                for (let field in errors) {
                    errorMessages += errors[field] + '<br>';
                }
                $('#addErrorMessages').html(errorMessages);
            }
        });
    });

    $('#editForm').submit(function(e) {
        e.preventDefault();
        let oldAmount = parseFloat($(this).find('[name="amount"]').val());
        let oldType = $(this).find('[name="type"]').val();

        $.ajax({
            url: '/update-transaction',
            type: 'POST',
            data: $(this).serialize(),
            success: function(response) {
                location.reload();
            },
            error: function(xhr) {
                let errors = xhr.responseJSON;
                let errorMessages = '';
                for (let field in errors) {
                    errorMessages += errors[field] + '<br>';
                }
                $('#editErrorMessages').html(errorMessages);
            }
        });
    });

    $(document).on('click', '.delete-button', function() {
        const id = $(this).data('id');
        $('#confirmDeleteButton').data('id', id);
        $('#deleteModal').show();
    });

    $('#confirmDeleteButton').on('click', function() {
        let id = $(this).data('id');
        let type = $(this).data('type');
        let amount = $(this).data('amount');

        $.ajax({
            url: `/delete-transaction/${id}`,
            type: 'GET',
            success: function(response) {
                location.reload();
            },
            error: function(xhr) {
                let errors = xhr.responseJSON;
                let errorMessages = '';
                for (let field in errors) {
                    errorMessages += errors[field] + '<br>';
                }
                $('#deleteErrorMessages').html(errorMessages);
            }
        });
    });
});