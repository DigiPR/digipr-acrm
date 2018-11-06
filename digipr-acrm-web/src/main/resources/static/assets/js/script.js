$('#confirm-modal').on('show.bs.modal', function(e) {
    $(this).find('#deleteData').attr('data-id', $(e.relatedTarget).data('id'));
});