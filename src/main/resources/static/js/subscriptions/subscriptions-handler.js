$(document).ready(function() {
   $('#addForm').submit(function(e) {
      e.preventDefault();
       $.ajax({
           url: '/add-subscription',
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
            url: '/update-subscription',
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
                $('#editErrorMessages').html(errorMessages);
            }
        });
    });

    $('#confirmDeleteButton').on('click', function() {
        let id = $('#deleteModal').find('input[type="hidden"]').val();

        $.ajax({
            url: `/delete-subscription/${id}`,
            type: 'GET',
            success: function(response) {
                $('#deleteModal').hide();
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