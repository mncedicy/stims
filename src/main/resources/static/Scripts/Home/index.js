$(document).ready(function () {

var books;
var charge_codes;
var histories;
var notices;
var users;

   
    getBookCountAll();
    function getBookCountAll() {

        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/report/getBookCountAll",  //the url to call
            contentType: "application/json",
        }).done(function (data) {
            data = JSON.parse(data);
           // console.log(data);
            loadOfficerCount(data);
        }).fail(function (err) {
            console.log(err.statusText);

        });
    }

        getNoticeAll();
        function getNoticeAll() {

            $.ajax({
                type: "GET", //GET, POST, PUT
                url: httpsapi + "/report/getNoticeAll?client_id=" + client_id,  //the url to call
                contentType: "application/json",
            }).done(function (data) {

                books = data.books;
                charge_codes = data.charge_codes;
                histories = data.histories;
                notices = data.notices;
                users = data.users;

              //  console.log(data);
                loadDashboard();
            }).fail(function (err) {
                console.log(err.statusText);

            });
        }

 function loadDashboard() {

var book_status = Object.groupBy(books, book => book.book_status);
var book_issued_to_name = Object.groupBy(books, book => book.book_issued_to_name);

book_status = Object.entries(book_status).map(myFunction);
//console.log(book_status);
//console.log(book_status,book_issued_to_name);

//console.log(groupedDateMonth(books,'month','book_issue_date','book_status'));
//console.log(grouped(notices,'infringement_notice_type_name','infringement_notice_status'));



const webTrafficData = [
  { date: '2024-01-01', visitors: 1500 },
  { date: '2024-01-02', visitors: 1750 },
  { date: '2024-01-03', visitors: 1600 },
  { date: '2024-01-04', visitors: 1900 },
];

const appUsageData = [
  { timestamp: 1704067200000, activeUsers: 250 }, // January 1, 2024, 00:00:00 UTC
  { timestamp: 1704153600000, activeUsers: 300 }, // January 2, 2024, 00:00:00 UTC
  { timestamp: 1704240000000, activeUsers: 280 }, // January 3, 2024, 00:00:00 UTC
];

const stockPrices = [
  { symbol: 'XYZ', tradeDate: '2024-01-01', priceInfo: { open: 100, high: 105, low: 98, close: 103 } },
  { symbol: 'XYZ', tradeDate: '2024-01-02', priceInfo: { open: 103, high: 108, low: 101, close: 106 } },
];

var options = {
  series: [
  {
      name: 'Mobile App Usage',
      data: appUsageData,
      parsing: {
        x: 'timestamp',
        y: 'activeUsers'
      }
    },
    {
      name: 'XYZ Stock Price',
      data: stockPrices,
      type: 'candlestick',
      parsing: {
        x: 'tradeDate',
        y: ['priceInfo.open', 'priceInfo.high', 'priceInfo.low', 'priceInfo.close']
      }
    }
  ],
  chart: {
    type: 'line', // Default type, overridden for candlestick series
    height: 350
  }
};
//console.log(options);

// Assuming 'chartElement' is a reference to your HTML element where the chart will be rendered
 const chart = new ApexCharts(document.querySelector("#chart1"), options);
 //console.log(chart);
 chart.render();



  var options1 = {
          series: [{
                      data: books,
                      parsing: {
                        x: 'book_issue_date',
                        y: 'book_pages_left'
                      }
                    }],
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
//        xaxis: {
//          categories: ['Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct'],
//        },
        yaxis: {
          title: {
            text: '$ (thousands)'
          }
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return "$ " + val + " thousands"
            }
          }
        }
        };

     //   var chart = new ApexCharts(document.querySelector("#chart1"), options);
     //   chart.render();



 }

function myFunction(value, index, array) {
  value[1]=value[1].length;
  return value;
}














var groupedDateMonth = (data,type,dateCol,categoryCol) => {
    return data.reduce((acc, item) => {
    var dateKey = getDatee(item[dateCol]);
     const date = new Date(dateKey);
    if(type=='month'){
          dateKey = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}`; // YYYY-MM format
     }


      const categoryKey = item[categoryCol];

      if (!acc[dateKey]) {
        acc[dateKey] = {}; // Initialize object for the date if it doesn't exist
      }
      if (!acc[dateKey][categoryKey]) {
        acc[dateKey][categoryKey] = []; // Initialize array for the category within the date
      }
      acc[dateKey][categoryKey].push(item);
      return acc;
    }, {});

  //  console.log(groupedData);
};

var grouped = (data,dateCol,categoryCol) => {
    return data.reduce((acc, item) => {
    var dateKey = item[dateCol];
    const categoryKey = item[categoryCol];

      if (!acc[dateKey]) {
        acc[dateKey] = {}; // Initialize object for the date if it doesn't exist
      }
      if (!acc[dateKey][categoryKey]) {
        acc[dateKey][categoryKey] = []; // Initialize array for the category within the date
      }
      acc[dateKey][categoryKey].push(item);
      return acc;
    }, {});

   // console.log(groupedData);
};


    function loadOfficerCount(data) {
        $(".txtBooks").html(data.Books.all);
        $(".txtHanded").html(data.Books.handed_in);
        $(".txtNotices").html(data.Items.all);
        $(".txtCaptured").html(data.Items.captured);
        $(".txtCapturedAll").html(data.Captured.all);
        $(".txtClosed").html(data.Captured.closed);

        $(".txtHandedPercent").html(toPercent(data.Books.all, data.Books.handed_in));
        $(".txtCapturedPercent").html(toPercent(data.Items.all, data.Items.captured));
        $(".txtClosedPercent").html(toPercent(data.Captured.all, data.Captured.closed));
    }










});