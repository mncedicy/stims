$(document).ready(function () {

    var allOfficers = Array();
    var books = Array();
    var Batchs = Array();
    var selectedBook;
    var selectedBatch;
    var status = 'new';


    if (myPrivs.indexOf(17) < 0)
        $('#btnBatch').addClass('hidden');
   


    getUsersByRoleId(2);


    function getUsersByRoleId(role_id) {
        showLoader();

        var details = {
            client_id: client_id,
            role_id: role_id
        }


        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/home/getUsersByRoleId",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details),
        }).done(function (data) {
            console.log(data);
            allOfficers = data;
            var str = "<option value=''>Select...</option>";
            for (var ii = 0; ii < data.length; ii++) {
                str += '<option value="' + data[ii].person.person_id + '">' +
                    (data[ii].person.person_first_name + ' ' + data[ii].person.person_last_name) + '</option>';

            }
            $('#cboOfficer').html(str);

        }).fail(function (err) {
            console.log(err);
            showErrorMessage(err);
        });
    }




    $('#btnSubmitIssue').on('click', function () {
        if (vaidateFields('requiredissue'))
            return;
        showLoader();

        var book = {};
        book.book_issued_by_name = person_full_name;
        book.book_issued_by = person_id;
        book.book_issued_to = parseInt($('#cboOfficer').val());
        book.book_issued_to_name = $("#cboOfficer option:selected").text();
        book.book_issue_date = $('#txtIssueDate').val()+'T00:00:00';
        book.book_status = 'issued';

        console.log(book);


        $.ajax({
            type: "PUT", //GET, POST, PUT
            url: httpsapi + "/capturing/issueBook/" + books[selectedBook].book_id,  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(book),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    books[selectedBook] = data.data;
                    $('#assignBook').modal('hide');
                    loadBooks();
                    showMessage('success', 'success', data.message, 3000);
                    $('#addBatchModal').modal('hide');
                }
                else {
                    showMessage('danger', 'Error', data.message, 5000);
                }
            },
            error: function (request, status, error) {
                console.log(request, status, error);
                hideLoader();
                showMessage('danger', 'Error', request.responseText, 5000);
            }

        });


    });




    $('#btnSubmitHandin').on('click', function () {
        if (vaidateFields('requiredhandin'))
            return;
        showLoader();

        var book = {};
        book.book_handedin_by_name = person_full_name;
        book.book_handedin_by = person_id;
        book.book_handedin_date = $('#txtIssueDate').val() + 'T00:00:00';
        book.book_status = 'handedin';

        console.log(book);


        $.ajax({
            type: "PUT", //GET, POST, PUT
            url: httpsapi + "/capturing/HandinBook/" + books[selectedBook].book_id,  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(book),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    books[selectedBook] = data.data;
                    $('#returnBook').modal('hide');
                    loadBooks();
                    showMessage('success', 'success', data.message, 3000);
                    $('#addBatchModal').modal('hide');
                }
                else {
                    showMessage('danger', 'Error', data.message, 5000);
                }
            },
            error: function (request, status, error) {
                console.log(request, status, error);
                hideLoader();
                showMessage('danger', 'Error', request.responseText, 5000);
            }

        });


    });





    getBookBatches();

    function getBookBatches() {
        showLoader();
        var details = {
            client_id: client_id
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/capturing/getBookBatches",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            Batchs = data;
            var str = "";
            for (var ii = 0; ii < data.length; ii++) {
                str += '<tr><td>' + (ii + 1) + '</td>';
                str += '<td>' + data[ii]['book_batch_type_code'] + ' (' + data[ii]['book_batch_type_name'] + ')</td>';
                str += '<td>' + data[ii]['book_batch_first_number'] + '</td>';
                str += '<td>' + data[ii]['book_batch_last_number'] + '</td>';
                str += '<td>' + removeNull(data[ii]['book_batch_captured_by_name']) + '</td>';
                str += '<td>' + getDatee(data[ii]['book_batch_timestamp']) + '</td>';
                str += '<td>' + data[ii]['book_batch_books_count'] + '</td>';
                str += '<td>' + data[ii]['book_batch_pages_per_book'] + '</td>';
                str += '<td class="text-center"><a href="#!" id="' + ii + '" class="viewBatch"><i class="feather icon-eye tblIcon text-primary"></i></a></td></tr>';

            }
            $('#listBatches').html(str);

            $('.viewBatch').click(function () {
                selectedBatch = parseInt(this.id);
                getBooks(Batchs[selectedBatch]['book_batch_id']);
            });

            hideLoader();
        }).fail(function (err) {
            console.log(err.statusText);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }




    function getBooks(batch_id) {
        
        
        showLoader();
        var details = {
            batch_id: batch_id
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/capturing/getBooks",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            books = data;
            $('#viewBatchModal').modal('show');
            loadBooks();



            $('.btnrdo1').trigger('change');



            hideLoader();
        }).fail(function (err) {
            console.log(err.statusText);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }

    function loadBooks() {
        var statusArr = ['new', 'issued', 'handedin'];
        var statusVallArr = [0, 0, 0];
        var str = "";
        for (var ii = 0; ii < books.length; ii++) {
            var statuss = books[ii]['book_status'];
            var action = statuss == 'new' ? 'Assign' : statuss == 'issued' ? 'Return' : 'View';
            var showAssignReturn = myPrivs.indexOf(18) < 0 && action == 'Assign' ? 'hidden' :
                myPrivs.indexOf(19) < 0 && action == 'Return' ? 'hidden' :'';
 
            str += '<tr><td>' + (ii + 1) + '</td>';
            str += '<td>' + books[ii]['book_type_code'] + ' (' + books[ii]['book_type_name'] + ')</td>';
            str += '<td>' + books[ii]['book_first_number'] + '</td>';
            str += '<td>' + books[ii]['book_last_number'] + '</td>';
            str += '<td>' + books[ii]['book_pages'] + '</td>';
            str += '<td>' + statuss + '</td>';
            str += '<td>' + removeNull(books[ii]['book_issued_to_name']) + '</td>';
            str += '<td>' + getDatee(books[ii]['book_issue_date']) + '</td>';
            str += '<td>' + getDatee(books[ii]['book_handedin_date']) + '</td>';
            str += '<td class="text-center"><a href="#!"  id="' + ii + '" class="assignBook ' + showAssignReturn+'">' + action + '</a></td></tr>';
            statusVallArr[statusArr.indexOf(books[ii]['book_status'])]++;
        }
        $('#listBooks').html(str);

        $('#bgNewBooks').html(statusVallArr[0]);
        $('#bgIssued').html(statusVallArr[1]);
        $('#bgHandedin').html(statusVallArr[2]);


        $('.assignBook').click(function () {
            selectedBook = parseInt(this.id);
            loadBookData($(this).text());
        });
    }


    function loadBookData(text) {
        $('.txtBookType').html(books[selectedBook]['book_type_code'] + ' (' + books[selectedBook]['book_type_name']+')');
        $('.txtBookPages').html(books[selectedBook]['book_pages']);
        $('.txtFirstPage').html(books[selectedBook]['book_first_number']);
        $('.txtLastPage').html(books[selectedBook]['book_last_number']);
        $('.txtIssuedTo').html(books[selectedBook]['book_issued_to_name']);
        $('.txtIssueDate').html(getDatee(books[selectedBook]['book_issue_date']));
        $('.txtHandinDate').html(getDatee(books[selectedBook]['book_handedin_date']));
        $('.txtCompleted').html(books[selectedBook]['book_pages_completed']);
        $('.myDatepicker').val(getDatee(getNow()));
        $('#cboOfficer').val('');

        if (text == 'Assign')
            $('#assignBook').modal('show');
        else if (text == 'Return')
            $('#returnBook').modal('show');
        else
            $('#viewBook').modal('show');
    }



    $('.btn-check').change(function () {
        status = $(this).val();
        console.log(status);
        $("#listBooks tr").each(function () {
            if ($(this).text().search(new RegExp(status, "i")) < 0) {
                $(this).fadeOut();
            } else {
                $(this).show();
            }
        });
    });



    $('#cboBookType').change(function () {
        $('#txtFirstNotice').val('');
        $('#txtFirstNotice').inputmask($(this).val() + '/' + $("#cboBookType option:selected").attr('data-notice') + '/' + client_authority_code);
    });

    $('#btnBatch').click(function () {
        $('#cboBookType').trigger('change');
        $('#addBatchModal').modal('show');
    });


    $('#btnSubmitBatch').on('click', function () {
        if (vaidateFieldsContain('requiredBatch','_'))
            return;
        showLoader();
        var details = {
            book_batch_client_id: client_id,
            book_batch_captured_by: person_id,
            book_batch_captured_by_name: person_full_name,
            book_batch_authority_name: client_name,
            book_batch_authority_code: client_authority_code,
            book_batch_pages_per_book: parseInt($('#txtPages').val()),
            book_batch_books_count: parseInt($('#txtBookCount').val()),
            book_batch_first_number_complete: $('#txtFirstNotice').val(),
            book_batch_type_code: parseInt($('#cboBookType').val()),
            book_batch_type_name: $("#cboBookType option:selected").text(),
            book_batch_first_number: $('#txtFirstNotice').val().split('/')[1]
        };

        console.log($('#txtFirstNotice').val().length, details, httpsapi + "/capturing/addBatch");

        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + "/capturing/saveBatch",  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(details),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    getBookBatches();
                    showMessage('success', 'success', data.message, 3000);
                    $('#addBatchModal').modal('hide');
                }
                else {
                    showMessage('danger', 'Error', data.message, 5000);
                }
            },
            error: function (request, status, error) {
                console.log(request, status, error);
                hideLoader();
                showMessage('danger', 'Error', error, 5000);
            }

        });
















    });








});