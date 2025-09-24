$(document).ready(function () {
   

    var selected;
    var infringements = [];
    var ReportType;
    var AccessStatus;

    function getNoticesLight() {
        if (validateFields1('validSearch'))
            return;



        $('#btnExport').addClass('disabled');
        ReportType = $('#cboReportType').val();
        AccessStatus = $('#cboAccessStatus').val();
        console.log(range);

        var details = {
            client_id: client_id,
            type_name: ReportType,
            access_status: AccessStatus,
            date_from: getFromDate(range.selectedDates[0]),
            date_to: getFromDate(range.selectedDates.length > 1 ? range.selectedDates[1] : range.selectedDates[0])
        }

        console.log(details);

        showLoader();
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/report/getNoticesLight",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            infringements = data;
            loadReport(data);

            hideLoader();
        }).fail(function (err) {
            console.log(err);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }


    function loadReport(data) {
        var str = "";
        var txtInfringements = data.length, txtTotalFine = 0, txtNAG = 0, txtPaid = 0, txtPaidFine = 0;
        var txtAvgHeadCount = 0, txtAvgHeadCountPercent =0, txtHighestCount = 0, txtHighestPercent = 0, txtLowestCount = 0, txtLowestPercent = 0;
        var officers = Array(), officersCount = Array(), officersCountData = Array(), charges = Array(),chargesShort = Array(), chargesCount = Array(), days = Array(), daysVals = Array();
        var  txtAvgHeadCountAboveBelow, txtHighestName, txtLowestName;

        for (var ii = 0; ii < data.length; ii++) {
            var status = getStatus(data[ii].infringement_notice_status);
            var fine = data[ii].infringement_notice_final_amount;
            txtTotalFine += fine;
            txtNAG += data[ii].infringement_notice_fine_amount == 'NAG' ? 1 : 0;
            txtPaidFine += status.status == 'Paid' ? fine : 0;
            txtPaid += status.status == 'Paid' ? 1 : 0;

            if (days.indexOf(data[ii].infringement_notice_offence_date) < 0) {
                days.push(data[ii].infringement_notice_offence_date);
                daysVals.push(0);
            }

            str += '<tr><td>' + (ii + 1) + '</td>';
            str += '<td>' + data[ii].infringement_notice_reference + '</td>';
            str += '<td>' + data[ii].infringement_notice_registration + '</td>';
            str += '<td>' + data[ii].infringement_notice_name + '</td>';
            str += '<td>' + data[ii].infringement_notice_officer_name + '</td>';
            str += '<td>' + data[ii].infringement_notice_charge_code + '</td>';
            str += '<td>' + getDatee(data[ii].infringement_notice_offence_date) + '</td>';
            str += '<td>' + status.span + '</td></tr>';
            $('#btnExport').removeClass('disabled');
        }
        $('#listReport').html(str);

        for (var d = 0; d < data.length; d++) {

            var dayIndex = days.indexOf(data[d].infringement_notice_offence_date);
            var index = officers.indexOf(data[d].infringement_notice_officer_name);
            if (index < 0) {
                officers.push(data[d].infringement_notice_officer_name);
                officersCount.push({
                    name: data[d].infringement_notice_officer_name,
                    data: JSON.parse(JSON.stringify(daysVals))
                });
                index = officers.length - 1;
            }
            officersCount[index].data[dayIndex] += 1;

            var chargeIndex = charges.indexOf(data[d].infringement_notice_charge_code);
            if (chargeIndex < 0) {
                charges.push(data[d].infringement_notice_charge_code);
                chargesCount.push(1);
                chargesShort.push({ short: data[d].infringement_notice_charge_short_description, data: [1] });
            }
            else {
                chargesCount[chargeIndex]++;
                chargesShort[chargeIndex].data[0]++;
            }
        }
        txtAvgHeadCount = data.length / officers.length;
        txtAvgHeadCountPercent = toPercent(target, txtAvgHeadCount);

        for (var s = 0; s < officersCount.length; s++) {
            var sum = officersCount[s].data.reduce((a, b) => a + b, 0);

            txtHighestName = txtHighestCount < sum ? officersCount[s].name : txtHighestName;
            txtHighestCount = txtHighestCount < sum ? sum : txtHighestCount;
            txtHighestPercent = toPercent(target, txtHighestCount);

            txtLowestName = txtLowestCount==0 || txtLowestCount > sum ? officersCount[s].name : txtLowestName;
            txtLowestCount = txtLowestCount == 0 || txtLowestCount > sum ? sum : txtLowestCount;
            txtLowestPercent = toPercent(target, txtLowestCount);
            console.log(txtLowestName, officersCount[s].name);

            officersCountData.push(sum);
        }

        txtAvgHeadCountAboveBelow = txtAvgHeadCount < target ? 'Below Target' : 'Above Target';
        if (txtAvgHeadCount < target)
            $('.txtAvgHeadCountPercent').removeClass('text-success').addClass('text-danger');
        else
            $('.txtAvgHeadCountPercent').removeClass('text-danger').addClass('text-success');
        

      

        console.log(officersCount, days);
        loadChartOfficer(officersCount, days);
        loadChartCharge(chargesCount, charges, chargesShort);

        loadChartOfficerPie(officersCountData, officers);

        $('.txtInfringements').html(txtInfringements);
        $('.txtTotalFine').html('R' + parseFloat(txtTotalFine).toFixed(2));
        $('.txtNAG').html(txtNAG);
        $('.txtPaid').html(txtPaid);
        $('.txtPaidFine').html('R' + parseFloat(txtPaidFine).toFixed(2));

        $('.txtOfficerTarget').html(target);
        $('.txtAvgHeadCount').html(txtAvgHeadCount);
        $('.txtAvgHeadCountPercent').html(txtAvgHeadCountPercent);
        $('.txtHighestCount').html(txtHighestCount);
        $('.txtHighestPercent').html(txtHighestPercent);
        $('.txtLowestCount').html(txtLowestCount);
        $('.txtLowestPercent').html(txtLowestPercent);
        $('.txtAvgHeadCountAboveBelow').html(txtAvgHeadCountAboveBelow);
        $('.txtHighestName').html(txtHighestName);
        $('.txtLowestName').html(txtLowestName);

        $('.viewInfringment').click(function () {
            selected = parseInt(this.id);
            //  getNoticesPrint();
        });
    }

    function loadChartOfficer(series,days) {
        var options = {
            series: series,
            chart: {
                type: 'bar',
                height: 350
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
                    text: 'infringments'
                }
            },
            fill: {
                opacity: 1
            },
            tooltip: {
                y: {
                    formatter: function (val) {
                        return "" + val + " infringments"
                    }
                }
            },
            title: {
                text: 'Traffic Officers',
                floating: false,
                offsetY: 0,
                align: 'center',
                style: {
                    color: '#444'
                }
            }
        };

        var chart = new ApexCharts(document.querySelector("#chartOfficer"), options);
        chart.render();
    }


    function loadChartCharge(series, charges, chargesShort) {
        var options = {
            series: [{
                data: series
            }],
            chart: {
                height: 350,
                type: 'bar',
            },
            plotOptions: {
                bar: {
                    borderRadius: 10,
                    dataLabels: {
                        position: 'top', // top, center, bottom
                    },
                }
            },
            dataLabels: {
                enabled: true,
                formatter: function (val) {
                    return val;
                },
                offsetY: -20,
                style: {
                    fontSize: '12px',
                    colors: ["#304758"]
                }
            },
            tooltip: {
                y: {
                    formatter(val, opts) {
                        const name = opts.w.globals.labels[opts.dataPointIndex];
                        console.log(opts, name);
                        return chargesShort[opts.dataPointIndex].short + " " + val;
                    },
                }
            },
            xaxis: {
                categories: charges,
                position: 'top',
                axisBorder: {
                    show: false
                },
                axisTicks: {
                    show: false
                },
                crosshairs: {
                    fill: {
                        type: 'gradient',
                        gradient: {
                            colorFrom: '#D8E3F0',
                            colorTo: '#BED1E6',
                            stops: [0, 100],
                            opacityFrom: 0.4,
                            opacityTo: 0.5,
                        }
                    }
                },
                tooltip: {
                    enabled: true,
                }
            },
            yaxis: {
                axisBorder: {
                    show: false
                },
                axisTicks: {
                    show: false,
                },
                labels: {
                    show: false,
                    formatter: function (val) {
                        return val;
                    }
                }

            },
            title: {
                text: 'Charge Codes',
                floating: true,
                offsetY: 330,
                align: 'center',
                style: {
                    color: '#444'
                }
            },
            grid: {
                padding: {
                    top: 20, // Adjust the top margin here
                    right: 20,
                    bottom: 20,
                    left: 20
                }
            }

        };

        var chart = new ApexCharts(document.querySelector("#chartCharge"), options);
        chart.render();
    }



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
                    return [name, val.toFixed(1)+"%"]
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
                enabled: false
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

        var chart = new ApexCharts(document.querySelector("#chartOfficerPie"), options);
        chart.render();
    }





    function printNoticeReport() {

        var url = $('#cboDocumentType').val() == 'PDF' ?
            "/report/printNoticeReport?grouping=" + $('#cboGrouping').val() : "/report/printNoticeExcel?author=" + person_full_name;


        showLoader();
        fetch(httpsapi + url,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(infringements),
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

                if($('#cboDocumentType').val() == 'PDF')
                    window.open(link.href, "_blank");
                else
                {
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

    

    getNoticesLight();
    $('#btnSearch').click(function () {
        getNoticesLight();
    });





});