var selectedNotification;
var notifications= Array();
var notificationList = Array();
$(document).ready(function () {



    init();
    function init() {
        if (user_password_type == 'Temporary') {
            $(".requiredPassword").val('');
            $("#txtCurrentPassword").removeClass('requiredPassword');
             $(".requiredPassword").addClass('is-invalid').removeClass('is-valid').val('');
            $(".changeElem").hide();
            $("#btnLogout").show();
            $("#passwordModal").modal('show');
        }
    }


    $(".btnChangePassword").click(function () {
        $("#txtCurrentPassword").addClass('requiredPassword');
         $(".requiredPassword").addClass('is-invalid').removeClass('is-valid').val('');
        $(".requiredPassword").val('');
        $(".changeElem").show();
        $("#btnLogout").hide();
        $("#passwordModal").modal('show');
    });

    $("#btnSubmitPassword").click(function () {
        if (validateFieldsLeg('currentPassword', 8))
            return;
        if (!isValidPassword())
              return;
        if ($("#txtNewPassword").val() != $("#txtConfirmPassword").val()) {
            showMessage('danger', 'Error', 'New password and confirm password does not match', 10000);
            $(".newPassword").addClass('is-invalid').removeClass('is-valid').val('');
            $("#txtNewPassword").focus();
            return;
        }
//        if (!isValidPassword($("#txtNewPassword").val())){
//            showMessage('danger', 'Error', 'New password must contain at least one number and one uppercase and lowercase letter, and between 8 and 20 characters', 10000);
//            $(".newPassword").val('');
//            $("#txtNewPassword").focus();
//            return;
//        }

        changePassword();
    });

//    function isValidPassword(password) {
//      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
//      return passwordRegex.test(password);
//    }


        function isValidPassword() {
           var validd = true;
                var lowerCaseLetters = /[a-z]/g;
                if(myInput.value.match(lowerCaseLetters)) {
                  letter.classList.remove("invalid");
                  letter.classList.add("valid");
                } else {
                  letter.classList.remove("valid");
                  letter.classList.add("invalid");
                  validd=false;
                }

                // Validate capital letters
                var upperCaseLetters = /[A-Z]/g;
                if(myInput.value.match(upperCaseLetters)) {
                  capital.classList.remove("invalid");
                  capital.classList.add("valid");
                } else {
                  capital.classList.remove("valid");
                  capital.classList.add("invalid");
                   validd=false;
                }

                // Validate numbers
                var numbers = /[0-9]/g;
                if(myInput.value.match(numbers)) {
                  number.classList.remove("invalid");
                  number.classList.add("valid");
                } else {
                  number.classList.remove("valid");
                  number.classList.add("invalid");
                   validd=false;
                }

                      // Validate specials
                      var specials = /\W|_/g;
                      if(myInput.value.match(specials)) {
                        special.classList.remove("invalid");
                        special.classList.add("valid");
                      } else {
                        special.classList.remove("valid");
                        special.classList.add("invalid");
                         validd=false;
                      }

                // Validate length
                if(myInput.value.length >= 8 && myInput.value.length <= 20) {
                  length.classList.remove("invalid");
                  length.classList.add("valid");
                } else {
                  length.classList.remove("valid");
                  length.classList.add("invalid");
                   validd=false;
                }


                if(validd){
                  myInput.classList.remove("is-invalid");
                  myInput.classList.add("is-valid");
                } else {
                  myInput.classList.remove("is-valid");
                  myInput.classList.add("is-invalid");
                }
          return validd;
        }

var myInputConfirm = document.getElementById("txtConfirmPassword");
var myInputCurrent = document.getElementById("txtCurrentPassword");
var myInput = document.getElementById("txtNewPassword");
var letter = document.getElementById("letter");
var capital = document.getElementById("capital");
var number = document.getElementById("number");
var length = document.getElementById("length");
var special = document.getElementById("special");

    myInput.onkeyup = function() {
      isValidPassword();
    };

  myInput.change = function() {
      isValidPassword();
    };

    myInputCurrent.onkeyup = function() {
                if(myInputCurrent.value.length >= 8 && myInput.value.length <= 20) {
                        myInputCurrent.classList.remove("is-invalid");
                        myInputCurrent.classList.add("is-valid");
                      } else {
                        myInputCurrent.classList.remove("is-valid");
                        myInputCurrent.classList.add("is-invalid");
                      }
    };

        myInputConfirm.onkeyup = function() {
                    if(myInputConfirm.value == myInput.value) {
                            myInputConfirm.classList.remove("is-invalid");
                            myInputConfirm.classList.add("is-valid");
                          } else {
                            myInputConfirm.classList.remove("is-valid");
                            myInputConfirm.classList.add("is-invalid");
                          }
        };

    function changePassword() {
        showLoader();
        var details = {
            user_id: user_id,
            current_password: $("#txtCurrentPassword").val(),
            new_password: $("#txtNewPassword").val()
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/home/changePassword",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
          //   console.log(data);
            if (data.status == 'Success') {
                $('#passwordModal').modal('hide');
                showMessage('success', 'success', data.message, 3000);
            }
            else {
                showMessage('danger', 'Error', data.message, 10000);
                $(".requiredPassword").addClass('is-invalid').removeClass('is-valid').val('');
                $("#txtCurrentPassword").focus();
            }

            hideLoader();
        }).fail(function (err) {
            console.log(err.statusText);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }


  //  getNotificationCount();
    function getNotificationCount() {
        var details = {
            person_id: person_id
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/report/getNotificationCount",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            data = JSON.parse(data);
            //console.log(data);
            if (data.Notifications.unread > 0)
                $(".badgeNotification").html(data.Notifications.unread).show();
            else
                $(".badgeNotification").html('0').hide();
            getNotifications();
        }).fail(function (err) {
            console.log(err.statusText);

        });
    }


    function loadOfficerCount(data){
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
    function toPercent(total, value) {
        if (total>0)
            return (parseFloat(100 * value / total).toFixed(2)) + '%';
        else
            return '0%';
   }

var itemData, uniqueData;

   getNotificationItemReceiverUnread();
       function getNotificationItemReceiverUnread() {
           var details = {
               person_id: person_id
           }
           $.ajax({
               type: "GET", //GET, POST, PUT
               url: httpsapi + "/report/getNotificationItemReceiverUnread",  //the url to call
               contentType: "application/json",
               data: jQuery.param(details)
           }).done(function (data) {
                var group = Object.groupBy(data, item => item.notification_item_reference);
               itemData = Object.entries(group).map(countFunct);
               uniqueData = [...new Set(data.map(item => item.notification_item_reference))];
              //  console.log(data);
               //   console.log(itemData,uniqueData);
               if (data.length > 0)
                   $(".badgeNotification").html(data.length).show();
               else
                   $(".badgeNotification").html('0').hide();
               getNotifications();
           }).fail(function (err) {
               console.log(err.statusText);

           });
       }


   function countFunct(value, index, array) {
        value[1] = value[1].length;
        return value;
    }

 
        function getNotifications() {
            var details = {
                person_id: person_id
            }
            $.ajax({
                type: "GET", //GET, POST, PUT
                url: httpsapi + "/report/getNotifications",  //the url to call
                contentType: "application/json",
                data: jQuery.param(details)
            }).done(function (data) {
              //   console.log(data);
                notifications = data;
                var str = "";
                for (var ii = 0; ii < data.length; ii++) {
                    var index = uniqueData.indexOf(data[ii].notification_subject);
                    var cls = index>=0?"readCls":"";
                    var count = index>=0? itemData[index][1]:0;
                    str += '<a id="' + ii + '" class="list-group-item '+cls+' list-group-item-action viewNotification" ';
                    str += 'href="#" data-bs-toggle="offcanvas" data-bs-target="#offcanvas_pc_layout">';
                    str += '<div class="d-flex"><div class="flex-shrink-0">';
                    str += '<div class="user-avtar bg-light-secondary">';
                    str += '<i class="ti ti-message"></i></div></div>';
                    str += '<div class="flex-grow-1 ms-1 txtDiv">';
                    str += '<span class="float-end text-muted">' + getDatee(data[ii].notification_last_update) +'</span>';
                    str += '<p class="text-body mb-1"><span>' + data[ii].notification_subject+'</span></p>';
                    str += '<span class="text-muted">' + data[ii].notification_message;
                    str += '<span class="float-end msgBadge"><span class="badge bg-primary badgeCount">'+count+'</span></span></span>';
                    str += '</div></div></a>';
                }
                $('.listNotification').html(str);

                $('.viewNotification').click(function () {
                    selectedNotification = data[parseInt(this.id)];
                    $('.txtChatReference').html(selectedNotification.notification_subject);
                    $('.txtChatSender').html(selectedNotification.notification_sender_id == person_id ? selectedNotification.notification_receiver_name : selectedNotification.notification_sender_name);
                    getNotificationItemsByNoticeId(selectedNotification.notification_notice_id);
                });

            }).fail(function (err) {
                console.log(err.statusText);

            });
        }

    $('.btnMessege').click(function () {
        selectedNotification = notifications[parseInt($(this).attr('notification-index'))];
        $('.txtChatReference').html(selectedNotification.notification_subject);
        $('.txtChatSender').html(selectedNotification.notification_sender_id == person_id ? selectedNotification.notification_receiver_name : selectedNotification.notification_sender_name);
        getNotificationItemsByNoticeId(selectedNotification.notification_notice_id);
    });



  function getNotificationItemsByNoticeIdRead(notice_id) {
        var details = {
            notice_id: notice_id,
            receiver_id: person_id
        }

        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/report/getNotificationItemsByNoticeIdRead",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
         //  console.log(data);
        }).fail(function (err) {
            console.log(err.statusText);

        });
    }





    function getNotificationItemsByNoticeId(notice_id) {
        var details = {
            notice_id: notice_id,
            receiver_id: person_id
        }

        var str = '<div class="col-md-12 text-center mt-4"><div class="spinner-grow text-primary" role="status"><span class="sr-only">Loading...</span></div><div class="spinner-grow text-primary" role="status"><span class="sr-only">Loading...</span></div><div class="spinner-grow text-primary" role="status"><span class="sr-only">Loading...</span></div></div>';
        $('.listNotificationItems').html(str);

        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/report/getNotificationItemsByNoticeId",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
       //    console.log(data);
            notificationList = data;
            loadMessages();
            getNotificationItemsByNoticeIdRead(notice_id);

            setTimeout(function() {
              getNotificationItemReceiverUnread();
            },300);





           // getBookCountByOfficerId();
        }).fail(function (err) {
            console.log(err.statusText);

        });
    }


    function loadMessages() {
       // console.log(notificationList);
        var str = '';
        for (var ii = 0; ii < notificationList.length; ii++) {
            var type = notificationList[ii].notification_item_sender_id == person_id ? 'me' : 'you'
            var name = notificationList[ii].notification_item_sender_id == person_id ? 'me' : notificationList[ii].notification_item_sender_name;
              var cls = notificationList[ii].notification_item_status == 'Unread' &&
               notificationList[ii].notification_item_receiver_id == person_id ? "readCls":"";
            str += '<a href="#" class="list-group-item '+cls+' mt-3 chat-item chat-' + type + ' list-group-item-action p-2">';
            str += '<div class="d-flex align-items-center"><div class="flex-grow-1 mx-2">';
            str += '<h6 class="mb-0">' + name + '<span class="float-end text-sm text-muted f-w-400 txtDiv">' + getDatee(notificationList[ii].notification_item_sent_date) + '</span></h6>';
            str += '<span class="text-sm text-muted">' + notificationList[ii].notification_item_message + '<span class="float-end msgBadge">';
            str += '<span class="bg-primary chat-badge-status"></span ></span></div></div></a>';
        }
        $('.listNotificationItems').html(str);
        $('#txtMessageSend').val('');
        $('#txtMessageSend')[0].focus();
        $(".offcanvas-body").animate({
            scrollTop: $('.offcanvas-body')[0].scrollHeight - $('.offcanvas-body')[0].clientHeight
        }, 1000);
    }



    $("#btnMessageSend").click(function () {
        if ($("#btnMessageSend").hasClass('disabled') || validateFields1('txtMessageSend'))
            return;
        sendMessage();
    });

    $('#txtMessageSend').on('keyup', function (evt) {
        if (evt.which == 13) {
                $("#btnMessageSend").trigger('click');
        }
    });

    function sendMessage() {
        var item = {};
        item.notification_item_message = $('#txtMessageSend').val();
        item.notification_item_notification_id = selectedNotification.notification_id;
        item.notification_item_notice_id = selectedNotification.notification_notice_id;
        item.notification_item_client_id = selectedNotification.notification_client_id;
        item.notification_item_reference = selectedNotification.notification_reference;
        item.notification_item_receiver_name = selectedNotification.notification_receiver_id == person_id ?
            selectedNotification.notification_sender_name : selectedNotification.notification_receiver_name;
        item.notification_item_receiver_id = selectedNotification.notification_receiver_id == person_id ?
            selectedNotification.notification_sender_id : selectedNotification.notification_receiver_id;
        item.notification_item_sender_id = person_id;
        item.notification_item_sender_name = person_full_name;
        item.notification_item_subject = selectedNotification.notification_reference;
        item.notification_item_type = "infringement";
        item.notification_item_status = "Unread";
        
        $("#btnMessageSend").html('<i class="fa fa-spinner fa-spin"></i>');
        $('#txtMessageSend').removeClass('is-valid');
        $("#btnMessageSend").addClass('disabled');

        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + "/report/sendMessage",  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(item),
            success: function (data, text) {
                $("#btnMessageSend").removeClass('disabled');
              //  console.log(data);
                if (data.status == 'Success') {
                    notificationList.push(data.data);
                    loadMessages();
                }
                else {
                    showMessage('danger', 'Error', data.message, 5000);
                }
                $("#btnMessageSend").html('<i class="feather icon-send"></i>');
            },
            error: function (request, status, error) {
                $("#btnMessageSend").removeClass('disabled');
                console.log(request, status, error);
                showMessage('danger', 'Error', error, 5000);
                $("#btnMessageSend").html('<i class="feather icon-send"></i>');
            }

        });


    }




    $(".cardItem").click(function () {
        if (!$(this).hasClass('active')) {
            $(".cardItem").removeClass('active');
            $(this).addClass('active');
        }
    });




});