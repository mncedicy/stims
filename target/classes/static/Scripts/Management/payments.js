$(document).ready(function () {


    var selected;
    var invoices = [];

    function getInvoicesLight() {
        if (validateFields1('validSearch'))
            return;



        $('#btnExport').addClass('disabled');
        console.log(range);

        var details = {
            client_id: client_id,
            date_from: getFromDate(range.selectedDates[0]),
            date_to: getFromDate(range.selectedDates.length > 1 ? range.selectedDates[1] : range.selectedDates[0])
        }

        console.log(details);

        showLoader();
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/report/getInvoicesLight",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            invoices = data;
            loadReport(data);

            hideLoader();
        }).fail(function (err) {
            console.log(err);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }


    function loadReport(data) {
        var str = ""; invoices
        var txtInvoices = data.length, txtInfringements = 0, txtTotalFine = 0, txtCard = 0, txtCash = 0, txtTransfer = 0;


        for (var ii = 0; ii < data.length; ii++) {
            txtInfringements += data[ii].invoice_records;
            txtTotalFine += data[ii].invoice_amount;

            str += '<tr><td>' + (ii + 1) + '</td>';
            str += '<td>' + data[ii].invoice_number + '</td>';
            str += '<td>' + data[ii].invoice_created_by_name + '</td>';
            str += '<td>' + data[ii].invoice_name_to + '</td>';
            str += '<td>' + data[ii].invoice_payment_type + '</td>';
            str += '<td>' + data[ii].invoice_records + '</td>';
            str += '<td>R' + parseFloat(data[ii].invoice_amount).toFixed(2) + '</td>';
            str += '<td>' + getDatee(data[ii].invoice_payment_date) + '</td></tr>';
            $('#btnExport').removeClass('disabled');
        }
        $('#listReport').html(str);

        var types = Object.groupBy(data, invoice => invoice.invoice_payment_type);
        var typesArr = Object.entries(types).map(sum);
        var dates = Object.groupBy(data, invoice => invoice.invoice_payment_date);
        var datesArr = Object.entries(dates).map(sum);

        const uniqueDates = [...new Set(data.map(item => item.invoice_payment_date))];
        const uniqueTypes = [...new Set(data.map(item => item.invoice_payment_type))];

        txtCard = types.Card ? sum2(types.Card) : 0;
        txtCash = types.Cash ? sum2(types.Cash) : 0;
        txtTransfer = types.Transfer ? sum2(types.Transfer) : 0;



        loadChartOfficerPie(typesArr, uniqueTypes)

        loadChartOfficer(datesArr, uniqueDates);


        $('.txtInvoices').html(txtInvoices);
        $('.txtInfringements').html(txtInfringements);
        $('.txtTotalFine').html('R' + parseFloat(txtTotalFine).toFixed(2));
        $('.txtCard').html('R' + parseFloat(txtCard).toFixed(2));
        $('.txtCash').html('R' + parseFloat(txtCash).toFixed(2));
        $('.txtTransfer').html('R' + parseFloat(txtTransfer).toFixed(2));


    }

    function myFunction(value, index, array) {
        value[1] = value[1].length;
        return value[1];
    }



    function sum(value, index, array) {
        const values = value[1].map(item => item.invoice_amount);
        return values.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
    }

    function sum2(value) {
        const values = value.map(item => item.invoice_amount);
        return values.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
    }

    var chartOfficer;
    function loadChartOfficer(series, days) {
        var options = {
            series: [{
                name: 'Paid',
                data: series
            }],
            chart: {
                type: 'bar',
                height: '100%',
            },
            plotOptions: {
                bar: {
                    horizontal: false,
                    columnWidth: '55%',
                    borderRadius: 5,
                    borderRadiusApplication: 'end'
                },
            },
            dataLabels: {
                enabled: false
            },
            stroke: {
                show: true,
                width: 2,
                colors: ['transparent']
            },
            xaxis: {
                categories: days,
            },
            yaxis: {
                title: {
                    text: 'Amount Paid'
                }
            },
            fill: {
                opacity: 1
            },
            tooltip: {
                y: {
                    formatter: function (val) {
                        return "R" + parseFloat(val).toFixed(2) + " "
                    }
                }
            },
            title: {
                text: 'Daily Collection',
                floating: false,
                offsetY: 0,
                align: 'center',
                style: {
                    color: '#444'
                }
            }
        };


        chartOfficer = new ApexCharts(document.querySelector("#chartOfficer"), options);
        chartOfficer.render();

    }



    var chartOfficerPie;
    function loadChartOfficerPie(officersCountData, officers) {
        var options = {
            series: officersCountData,
            chart: {
                width: '100%',
                height: '100%',
                type: 'donut',
            },
            labels: officers,
            dataLabels: {
                formatter(val, opts) {
                    const name = opts.w.globals.labels[opts.seriesIndex]
                    return [name, val.toFixed(1) + "%"]
                },
            },
            plotOptions: {
                pie: {
                    startAngle: -90,
                    endAngle: 270
                }
            },
            fill: {
                type: 'gradient',
            },
            legend: {
                show: false
            },
            title: {
                text: 'Payment Type',
                floating: false,
                offsetY: 0,
                align: 'center',
                style: {
                    color: '#444'
                }
            },
            tooltip: {

                formatter: function (val) {
                    return "R" + parseFloat(val).toFixed(2) + " "
                }

            },
            responsive: [{
                breakpoint: 480,
                options: {
                    chart: {
                        width: 200
                    },
                    legend: {
                        position: 'bottom'
                    }
                }
            }]
        };



        if (!chartOfficerPie) {
            chartOfficerPie = new ApexCharts(document.querySelector("#chartOfficerPie"), options);
            chartOfficerPie.render();
        }
        else {
            chartOfficerPie.updateOptions({
                series: officersCountData,
                labels: officers
            });
        }
    }



    function printNoticeReport() {

        var url = $('#cboDocumentType').val() == 'PDF' ?
            "/report/printPaymentReport?grouping=" + $('#cboGrouping').val() : "/report/printPaymentExcel?author=" + person_full_name;


        showLoader();
        fetch(httpsapi + url,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(invoices),
            }

        )
            .then(response => {
                hideLoader();
                if (!response.ok) {
                    showMessage('danger', 'Error', response.status, 5000);
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                console.log(response);
                return response.arrayBuffer();
            })
            .then(arrayBuffer => {
                var link = document.createElement('a');
                var blob = $('#cboDocumentType').val() == 'PDF' ?
                    new Blob([arrayBuffer], { type: "application/pdf" }) :
                    new Blob([arrayBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;' });
                link.href = window.URL.createObjectURL(blob);
                link.download = 'report.xls';

                if ($('#cboDocumentType').val() == 'PDF')
                    window.open(link.href, "_blank");
                else {
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                }


                $('#printReportModal').modal('hide');
            })
            .catch(error => {
                console.error("Fetch error:", error);
                hideLoader();
                showMessage('danger', 'Error', error, 5000);
            });

    }


    $('#btnExport').click(function () {
        $('.requiredReport').val('').removeClass('is-invalid').removeClass('is-valid');
        $('#cboGrouping').val('');
        $('#printReportModal').modal('show');
    });


    $('#btnSubmitReport').click(function () {
        if (validateFields1('requiredReport'))
            return;

        printNoticeReport();
    });

    $('#cboDocumentType').change(function () {
        if ($(this).val() == 'Excel')
            $('.divGrouping').hide('fast');
        else
            $('.divGrouping').show('fast');

    });







    getInvoicesLight();
    $('#btnSearch').click(function () {
        getInvoicesLight();
    });


});