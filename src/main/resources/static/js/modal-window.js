$(document).ready(function() {
    $('.edit-button').on('click', function() {
        $('#edit-category').empty();

        let button = $(this);
        $('#edit-id').val(button.data('id'));
        $('#edit-title').val(button.data('title'));
        $('#edit-amount').val(button.data('amount'));
        $('#edit-type').val(button.data('type'));
        $('#current-category').val(button.data('category'));
        $('#edit-date').val(button.data('date'));

        let currentCategory = $('#current-category').val();
        FillCategories(button.data('type'));
        $('#editModal').show();
    });

    $('#edit-type').on('change', function() {
        $('#edit-category').empty();
        let type = $(this).val();
        FillCategories(type);
    });

    $('.close').on('click', function() {
        $('#editModal').hide();
    });
});

function FillCategories(type) {
    const expenseCategories = ['Supermarkets', 'Clothes', 'Fast Food', 'Transaction', 'Others'];
    const incomeCategories = ['Transaction', 'ATM', 'Others'];
    if (type == 'expense') {
        for (let i = 0; i < expenseCategories.length; i++) {
            if (expenseCategories[i] == 'Fast Food') {
                $('#edit-category').append(`<option value="fast_food">${expenseCategories[i]}</option>`);
            } else {
                $('#edit-category').append(`<option value="${expenseCategories[i].toLowerCase()}">${expenseCategories[i]}</option>`);
            }
        }
    } else {
        for (let i = 0; i < incomeCategories.length; i++) {
            $('#edit-category').append(`<option value="${incomeCategories[i].toLowerCase()}">${incomeCategories[i]}</option>`);
        }
    }
}