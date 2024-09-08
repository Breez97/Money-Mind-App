$(document).ready(function() {
    $('.add-transaction-button').on('click', function() {
        FillCategories('add', 'income');
        $('#addModal').show();
    });

    $('#add-type').on('change', function() {
        let type = $(this).val();
        FillCategories('add', type);
    });

    $('.transactions-container').on('click', '.edit-button', function() {
        $('#edit-category').empty();

        $('#edit-id').val($(this).data('id'));
        $('#edit-title').val($(this).data('title'));
        $('#edit-amount').val($(this).data('amount'));
        $('#edit-type').val($(this).data('type'));
        $('#current-category').val($(this).data('category'));
        $('#edit-date').val($(this).data('date'));

        FillCategories('edit', $(this).data('type'));
        $('#editModal').show();
    });

    $('#edit-type').on('change', function() {
        let type = $(this).val();
        FillCategories('edit', type);
    });

    $('.delete-button').on('click', function() {
        const id = $(this).data('id');
        $('#confirmDeleteButton').data('id', id);
        $('#deleteModal').show();
    });

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