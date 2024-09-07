$(document).ready(function() {
   $('#registerForm').submit(function(e) {
       e.preventDefault();
       $.ajax({
           url: '/register',
           type: 'POST',
           data: $(this).serialize(),
           success: function(response) {
               window.location.href = "login-page";
           },
           error: function(xhr) {
               let errors = xhr.responseJSON;
               let errorMessages = '';
               for (let field in errors) {
                   errorMessages += errors[field] + '<br>';
               }
               $('#registerErrorMessages').html(errorMessages);
           }
       });
   });
});