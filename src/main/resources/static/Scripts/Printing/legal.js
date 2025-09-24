$(document).ready(function () {
    
    var selected;
    var infringements = [];
    var checkedList = [];
    var letter_status;

    function getletter() {

        letter_status = $('#cboLetterType').val();
        console.log(range);
       
        var details = {
            client_id: client_id,
            search_with: letter_status,
            date_from: getFromDate(range.selectedDates[0]),
            date_to: getFromDate(range.selectedDates.length > 1 ? range.selectedDates[1] : range.selectedDates[0])
        }
      
        console.log(details);
       
        showLoader();
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/management/getletter",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            infringements = data;
            var str = "";
            for (var ii = 0; ii < data.length; ii++) {
                var status = getStatus(data[ii].notice.infringement_notice_status);
                var fine = data[ii].notice.infringement_notice_final_amount;
                var checkBox = data[ii].notice.infringement_notice_access_status == 'Closed' || fine == 0 ? '' :
                    '<div class="form-check"><input checked data-index="' + ii + '" class="form-check-input checkNotice" type="checkbox"></div>';

                str += '<tr><td>' + checkBox + '</td>';
                str += '<td>' + (ii + 1) + '</td>';
                str += '<td>' + data[ii].notice.infringement_notice_reference + '</td>';
                str += '<td>' + data[ii].notice.infringement_notice_registration + '</td>';
                str += '<td>' + data[ii].infringer.infringement_infringer_name + ' ' + data[ii].infringer.infringement_infringer_surname + '</td>';
                str += '<td>' + data[ii].notice.infringement_notice_letter_status + '</td>';
                str += '<td>' + getDatee(data[ii].notice.infringement_notice_letter_status_date) + '</td>';
                str += '<td>' + status.span + '</td>';
                str += '<td class="text-center"><a href="#!" id="' + ii + '" class="viewInfringment"><i class="feather icon-printer tblIcon btn btn-icon btn-outline-primary avtar-xs"></i></a></td></tr>';
                infringements[ii].charge_code.charge_code_description = replaceHolders(infringements[ii].charge_code.charge_code_description, infringements[ii].notice);
            }
            $('#listLetters').html(str);

            $("#checkAllNotice").prop('checked', true).trigger('change');

            $('.linkSearch').click(function () {
                $('#cboSearchWith').val($(this).attr('data-link'));
                $('#txtSearch').val($(this).text());
                $('#btnSearch').trigger('click');
            });

            $(".checkNotice").change(function () {
                $('.checkNotice:checked').map(function (x) {
                    console.log(x, $(this).attr('data-index'));
                });
                if ($('.checkNotice:checked').length > 0) 
                    $('#btnPrintSelected').removeClass('disabled');
                else 
                    $('#btnPrintSelected').addClass('disabled');


                if ($('.checkNotice:checked').length == $('.checkNotice').length)
                    $("#checkAllNotice").prop('checked', true);
                else
                    $("#checkAllNotice").prop('checked', false);
            });

            $('.viewInfringment').click(function () {
                $(".checkNotice").prop('checked', false);
                $('input[data-index="' + this.id + '"]').prop('checked', true);
                selected = parseInt(this.id);
                getletterPrint();
            });
            
            hideLoader();
        }).fail(function (err) {
            console.log(err);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }

    $('#btnSearch').click(function () {
        if (validateFields1('validSearch'))
            return;
        $("#checkAllNotice").prop('checked', false);
        getletter();
    });

    $("#checkAllNotice").change(function () {
        $('.checkNotice').prop('checked', $(this).is(':checked'));
        if ($('.checkNotice:checked').length > 0)
            $('#btnPrintSelected').removeClass('disabled');
        else
            $('#btnPrintSelected').addClass('disabled');
    });

    $('#btnPrintSelected').click(function () {
        getletterPrint();
    });

    function getletterPrint() {

        checkedList = [];
        $('.checkNotice:checked').map(function (x) {
            checkedList.push(infringements[parseInt($(this).attr('data-index'))]);
        });

        console.log(checkedList);

        showLoader();

        fetch(httpsapi + "/management/getletterPrint/" + client_id + "?letter_status=" + letter_status, {
            method: 'POST', // or 'POST', etc.
            body: JSON.stringify(checkedList),
            headers: {
                'Content-Type': 'application/json' // Or other content type
            }
        })
            .then(response => {
                hideLoader();
                if (!response.ok) {
                    showMessage('danger', 'Error', response.status, 5000);
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.arrayBuffer();
            })
            .then(arrayBuffer => {
                var blob = new Blob([arrayBuffer], { type: "application/pdf" });
                var link = window.URL.createObjectURL(blob);
                var newWindow = window.open(link, "_blank");
            })
            .catch(error => {
                console.error("Fetch error:", error);
                hideLoader();
                showMessage('danger', 'Error', error, 5000);
            });

    } 

});