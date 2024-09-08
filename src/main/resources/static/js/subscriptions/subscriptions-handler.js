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
});