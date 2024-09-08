$(document).ready(function() {
    $('.add-subscription-button').on('click', function() {
       $('#addModal').show();
    });

    $('.close').on('click', function() {
        $('#addModal').hide();
//        $('#editModal').hide();
//        $('#deleteModal').hide();
//        $('#addErrorMessages').html('');
//        $('#editErrorMessages').html('');
    });
});