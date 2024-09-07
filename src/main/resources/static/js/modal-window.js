$(document).ready(function() {
    $('.add-transaction-button').on('click', function() {
        FillCategories('add', 'income');
        $('#addModal').show();
    });

    $('#add-type').on('change', function() {
        let type = $(this).val();
        FillCategories('add', type);
    });

    $('.edit-button').on('click', function() {
        $('#edit-category').empty();

        let button = $(this);
        $('#edit-id').val(button.data('id'));
        $('#edit-title').val(button.data('title'));
        $('#edit-amount').val(button.data('amount'));
        $('#edit-type').val(button.data('type'));
        $('#current-category').val(button.data('category'));
        $('#edit-date').val(button.data('date'));

        FillCategories('edit', button.data('type'));
        $('#editModal').show();
    });

    $('#edit-type').on('change', function() {
        let type = $(this).val();
        FillCategories('edit', type);
    });

    $('.delete-button').on('click', function() {
        const link = `/delete-transaction/${$(this).data('id')}`;
        $('#hrefDelete').attr('href', link);
        $('#deleteModal').show();
    });

    $('.close').on('click', function() {
        $('#addModal').hide();
        $('#editModal').hide();
        $('#deleteModal').hide();
        $('#addErrorMessages').html('');
        $('#editErrorMessages').html('');
    });

    $('#noButton').on('click', function() {
        $('#deleteModal').hide();
    });
});

function FillCategories(operation, type) {
    const expenseCategories = ['Supermarkets', 'Clothes', 'Fast Food', 'Transaction', 'Others'];
    const incomeCategories = ['Transaction', 'ATM', 'Others'];
    const selector = `#${operation}-category`;

    $(selector).empty();
    if (type == 'expense') {
        for (let i = 0; i < expenseCategories.length; i++) {
            if (expenseCategories[i] == 'Fast Food') {
                $(selector).append(`<option value="fast_food">${expenseCategories[i]}</option>`);
            } else {
                $(selector).append(`<option value="${expenseCategories[i].toLowerCase()}">${expenseCategories[i]}</option>`);
            }
        }
    } else {
        for (let i = 0; i < incomeCategories.length; i++) {
            $(selector).append(`<option value="${incomeCategories[i].toLowerCase()}">${incomeCategories[i]}</option>`);
        }
    }
}