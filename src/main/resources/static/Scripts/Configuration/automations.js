$(document).ready(function () {

    checkPriv(14);
   
    var selectedRule;
    var users = [];
    var userIds = [];
    var usersSelected = [];
    var rules = [];
    var rule = {};
    var rule_receivers = [];


    if (myPrivs.indexOf(54) < 0)
        $('#btnAddNew').addClass('hidden');
    var showEdit = myPrivs.indexOf(55) < 0 ? 'hidden' : '';
    var showDelete = myPrivs.indexOf(56) < 0 ? 'hidden' : '';
    var showReactivate = myPrivs.indexOf(57) < 0 ? 'hidden' : '';
    var showDeactivate = myPrivs.indexOf(57) < 0 ? 'hidden' : '';




    getAutomations();
    function getAutomations() {
        showLoader();

        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/configuration/getAutomations?client_id=" + client_id,  //the url to call
            contentType: "application/json"
        }).done(function (data) {
            rules = data;
            console.log(data);

           var str = '';
            for (i = 0; i < data.length; i++) {
                var status = getStatus(data[i].rule.rule_status);
                var deleted = status.status == "Deleted" ? 'hidden="hidden"' : '';
                var undeleted = status.status != "Deleted" ? 'hidden="hidden"' : '';
                var days = data[i].rule.rule_trigger == "Email" ? data[i].rule.rule_doc_type : data[i].rule.rule_days_repeats;

                str += '<tr><td>' + (i + 1) + '</td><td>' + data[i].rule.rule_name + '</td>';
                str += '<td class="text-capitalize">' + data[i].rule.rule_trigger + '</td>';
                str += '<td class="text-capitalize">' + data[i].rule.rule_type + '</td>';
                str += '<td class="text-capitalize">' + data[i].rule.rule_when + '</td>';
                str += '<td class="text-capitalize">' + days + '</td>';
                str += '<td>' + status.span + '</td>';
                str += '<td class="text-center clsAction"><a  ' + deleted + ' class="clsEdit ' + showEdit +'" id="c' + i + '" href="#!"><i class="icon feather icon-edit tblIcon text-primary"></i>';
                str += '</a><a href="#!" ' + deleted + ' class="clsDelete ' + showDelete +'" id="d' + i + '"><i class="feather icon-trash-2 tblIcon text-danger"></i></a>';
                str += '<a href="#!" ' + undeleted + '  class="clsActivate ' + showReactivate +'" id="a' + i + '"><i class="feather icon-rotate-ccw tblIcon text-success"></i></a></td></tr>';
           }
            $('#tblRule').html(str);

            if (showEdit && showDelete && showReactivate)
                $('.clsAction').hide();

            $('.clsDelete').click(function () {
                selectedRule = parseInt(this.id.substr(1));
                showConfirm('danger', 'Confirmation', 'Are you sure you want to delete this rule?');
            });

            $('.clsActivate').click(function () {
                selectedRule = parseInt(this.id.substr(1));
                showConfirm('success', 'Confirmation', 'Are you sure you want to re-activate this rule?');
            });

            $('.clsEdit').click(function () {
                selectedRule = parseInt(this.id.substr(1));
                $('#addRuleModal').modal('show');
                $('.clsRule').val('');
                $('.Trigger').hide();
                $('.Type').hide();
                 $('.cboWhen').hide();
                $('.clsRuleChange').removeClass("clsRule");
                 $('.clsRuleChangeType').removeClass("clsRule");
                rule = rules[selectedRule].rule;
                rule_receivers = rules[selectedRule].rule_receivers;
                choices.removeActiveItems();
                loadAll();
            });



            hideLoader();
        }).fail(function (err) {
            console.log(err);
        });
    }


    function loadAll() {

                $('#txtName').val(rule.rule_name);
                $("#cboTrigger").val(rule.rule_trigger).trigger("change");
                $('#cboWhen').val(rule.rule_when);
                $('#txtDaysRepeat').val(rule.rule_days_repeats);
                $('#cboType').val(rule.rule_type).trigger("change");
                $('#cboRange').val(rule.rule_range);
                $('#cboDocument').val(rule.rule_doc_type);
                $('#cboGrouping').val(rule.rule_grouping);
                $('#cboStart').val(rule.rule_start_date);
                $('#txtMessage').val(rule.rule_message);
                $('#cboWeekDays').val(rule.rule_week_days);
                setReceivers();

    }



    usersLightActive();
    function usersLightActive() {
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/home/usersLightActive?client_id=" + client_id,  //the url to call
            contentType: "application/json"
        }).done(function (data) {
            users = data;
            console.log(data);
        }).fail(function (err) {
            console.log(err);
        });
    }




    $('#btnAddNew').click(function () {
        $('#addRuleModal').modal('show');
        $('.clsRule').val('');
        $('.Trigger').hide();
        $('.Type').hide();
         $('.cboWhen').hide();
        $('.clsRuleChange').removeClass("clsRule");
         $('.clsRuleChangeType').removeClass("clsRule");
        rule = {};
        rule_receivers = [];
        choices.removeActiveItems();
    });


    $('#cboTrigger').change(function () {
        $('.Trigger').hide();
        $('.clsCleanTrigger').val("");
        $('.clsRuleChange').removeClass("clsRule");
        $('.'+$(this).val()).show('fast');
        $('.'+$(this).val()+' .clsRuleChange').addClass("clsRule");
    });

   $('#cboType').change(function () {
        $('.Type').hide();
        $('.'+$(this).val()).show('fast');
    });

   $('#cboWhen').change(function () {
        $('.cboWhen').hide();
        $('.cboWhen #cboWeekDays').removeClass("clsRule");
        $('.'+$(this).val()).show('fast');
        $('.'+$(this).val()+' #cboWeekDays').val('').addClass("clsRule");
    });


    $('.variables .dropdown-item').click(function () {
        insertAtCursor('txtMessage', $(this).attr("data-val"));
    });



function insertAtCursor(textareaId, textToInsert) {
    const textarea = document.getElementById(textareaId); // Get the native DOM element
    const startPos = textarea.selectionStart;
    const endPos = textarea.selectionEnd;
    const originalValue = textarea.value;

    // Construct the new value by inserting the textToInsert at the cursor position
    textarea.value = originalValue.substring(0, startPos) +
                     textToInsert +
                     originalValue.substring(endPos);

    // Set the cursor position to the end of the inserted text
    textarea.selectionStart = startPos + textToInsert.length;
    textarea.selectionEnd = startPos + textToInsert.length;

    // Focus the textarea to ensure the cursor is visible
    textarea.focus();
}





    $('#btnSubmitConfirm').click(function () {
        $('#confirmModal').modal('hide');
        rules[selectedRule].rule.rule_status = rules[selectedRule].rule.rule_status == "Deleted" ? "Active" : "Deleted";
        saveRule(rules[selectedRule]);
    });


   function setReceivers() {
        var group = Array();
        usersSelected = Array();
        userIds = rule_receivers.map(item => item.rule_receiver_ref_id);
        for (var i = 0; i < users.length; i++) {
            var index = group.indexOf(users[i].user_role_id);
            if (index < 0) {
                group.push(users[i].user_role_id);
                usersSelected.push({
                    label: users[i].user_role_name,
                    choices: [{
                        value: i,
                        label: users[i].user_person_name,
                        selected: userIds.indexOf(users[i].user_person_id)>=0
                    }]

                });
            }
            else {
                usersSelected[index].choices.push(
                    {
                        value: i,
                       label: users[i].user_person_name,
                        selected: userIds.indexOf(users[i].user_person_id)>=0

                    }
                );
            }

        }

        choices.setChoices(usersSelected,'value','label',true);
        console.log(choices.getValue());
         choices.passedElement.element.blur();

        choices.passedElement.element.addEventListener('change', function (event) {
            const selectedValues = choices.getValue(true); // Returns array of selected objects
             for (var i = 0; i < selectedValues.length; i++) {
                     console.log(selectedValues[0]);
             }
            if (selectedValues.length == 0) {
                $('.choices__inner').removeClass('is-valid').addClass('is-invalid');
            }
            else {
                $('.choices__inner').removeClass('is-invalid').addClass('is-valid');
            }
             console.log(choices);
            console.log(selectedValues);
        });

        choices.passedElement.element.dispatchEvent(new Event('change'));



    }











    $('#btnSubmitRule').click(function () {

            if (validateFields1('clsRule')) {
                return;
            }

            var values = choices.getValue(true)
            if ($("#cboTrigger").val()=='Email' && values.length==0) {
               return;
            }


            rule.rule_status = "Active";
            rule.rule_name = $('#txtName').val();
            rule.rule_trigger = $("#cboTrigger").val();
            rule.rule_when = $('#cboWhen').val();
            rule.rule_days_repeats = $('#txtDaysRepeat').val();
            rule.rule_type = $('#cboType').val();
            rule.rule_range = $('#cboRange').val();
            rule.rule_doc_type = $('#cboDocument').val();
            rule.rule_grouping = $('#cboGrouping').val();
            rule.rule_start_date = $('#cboStart').val()?$('#cboStart').val():rule.rule_start_date;
            rule.rule_start_date = !rule.rule_start_date?getNowDate():rule.rule_start_date;
            rule.rule_message = $('#txtMessage').val();
            rule.rule_week_days = $('#cboWeekDays').val();
            rule.rule_range_days = $('#cboRange option:selected').attr('data-val');
            rule.rule_client_id = client_id;
            rule.rule_created_by = person_id;
            rule.rule_created_by_name = person_full_name;

            rule_receivers= [];
            for(var i=0;i<values.length;i++){
            var choice= values[i];
                rule_receivers.push({
                    rule_receiver_rule_id : rule.rule_id,
                    rule_receiver_name:rule.rule_name,
                    rule_receiver_type:rule.rule_trigger,
                    rule_receiver_ref_id:users[choice].user_person_id,
                    rule_receiver_ref_name:users[choice].user_person_name,
                    rule_receiver_distination:users[choice].user_username,
                    rule_receiver_client_id: client_id,
                    rule_receiver_created_by: person_id,
                    rule_receiver_created_by_name: person_full_name
                });
            }

               var details = {
                    rule: rule,
                    rule_receivers: rule_receivers
                };

                saveRule(details);

    });


    function saveRule(details) {

console.log(details);




        showLoader();

        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + "/configuration/saveRule",  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(details),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    getAutomations();
                    $('#addRuleModal').modal('hide');
                    showMessage('success', 'success', data.message, 3000);
                }
                else {
                    $('#step1').trigger('click');
                    showMessage('danger', 'Error', data.message, 5000);
                }
            },
            error: function (request, status, error) {
                console.log(request, status, error);
                hideLoader();
                showMessage('danger', 'Error', error, 5000);
            }

        });


    }























});