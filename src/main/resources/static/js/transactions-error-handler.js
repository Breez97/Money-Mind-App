$(document).ready(function() {
    $('#addForm').submit(function(e) {
        e.preventDefault();
        $.ajax({
            url: '/add-transaction',
            type: 'POST',
            data: $(this).serialize(),
            success: function(response) {
                $('#addModal').hide();
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
        $.ajax({
            url: '/update-transaction',
            type: 'POST',
            data: $(this).serialize(),
            success: function(response) {
                $('#editModal').hide();
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
});