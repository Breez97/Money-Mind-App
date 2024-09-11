$(document).ready(function() {
    $('#editInfo').on('click', function() {
        $('#editInfo').hide();
        $('#saveInfo').show();
        $('#name').removeAttr('disabled');
        $('#username').removeAttr('disabled');
        let newFields = `
            <div class="form-field">
                <label for="newPassword">New password:</label>
                <input type="password" id="newPassword" name="newPassword" placeholder="Enter new password, if you want to change it">
            </div>
            <div class="form-field">
                <label for="password">Current password:</label>
                <input type="password" id="password" name="password" placeholder="Enter your current password">
            </div>
        `;
        if ($('#newPassword').length === 0 && $('#currentPassword').length === 0) {
            $('.form-actions').before(newFields);
        }
    });

    $('#saveInfo').on('click', function() {
        $.ajax({
            url: '/update-user',
            type: 'POST',
            data: {
                name: $('#name').val(),
                username: $('#username').val(),
                newPassword: $('#newPassword').val(),
                password: $('#password').val()
            },
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

    $('#logoutButton').on('click', function() {
        var form = $('<form action="/logout" method="post"></form>');
        form.appendTo('body').submit().remove();
    });
});