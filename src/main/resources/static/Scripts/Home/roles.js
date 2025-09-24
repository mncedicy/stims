$(document).ready(function () {

    checkPriv(13);

    var selectedRole;
    var roles = [];
    var role = {};
    var privileges = [];

    if (myPrivs.indexOf(4) < 0)
        $('#btnAddNew').addClass('hidden');
    var showEdit = myPrivs.indexOf(5) < 0 ? 'hidden' : '';
    var showDelete = myPrivs.indexOf(6) < 0 ? 'hidden' : '';
    var showReactivate = myPrivs.indexOf(45) < 0 ? 'hidden' : '';


    getRoles();
    function getRoles() {
        showLoader();

        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/home/roles?client_id=" + client_id,  //the url to call
            contentType: "application/json"
        }).done(function (data) {
            roles = data;
            console.log(data);
           var str = '';
            for (i = 0; i < data.length; i++) {
                var status = getStatus(data[i].role.role_status);
                var deleted = status.status == "Deleted" ? 'hidden="hidden"' : '';
                var undeleted = status.status != "Deleted" ? 'hidden="hidden"' : '';
                var deletable = status.status == "Deleted" || data[i].role.role_deletable=="No" ? 'hidden="hidden"' : '';
                str += '<tr><td>' + (i + 1) + '</td><td>' + data[i].role.role_name + '</td>';
                str += '<td>' + data[i].role.role_description + '</td>';
                str += '<td>' + $("option[value='" + data[i].role.role_landing_page + "']").text() + '</td>';
                str += '<td>' + status.span + '</td>';
                str += '<td>' + data[i].myPrivileges.length + '</td>';
                str += '<td class="text-center clsAction"><a  ' + deleted + ' class="clsEdit ' + showEdit+'" id="c' + i +'" href="#!"><i class="icon feather icon-edit tblIcon text-primary"></i>';
                str += '</a><a href="#!" ' + deletable + ' class="clsDelete ' + showDelete+'" id="d' + i + '"><i class="feather icon-trash-2 tblIcon text-danger"></i></a>';
                str += '<a href="#!" ' + undeleted + ' class="clsActivate ' + showReactivate +'" id="a' + i +'"><i class="feather icon-rotate-ccw tblIcon text-success"></i></a></td></tr>';
            }
            $('#tblRole').html(str);

            if (showEdit && showDelete && showReactivate)
                $('.clsAction').hide();

            $('.clsDelete').click(function () {
                selectedRole = parseInt(this.id.substr(1));
                showConfirm('danger', 'Confirmation', 'Are you sure you want to delete this role?');
            });

            $('.clsActivate').click(function () {
                selectedRole = parseInt(this.id.substr(1));
                showConfirm('success', 'Confirmation', 'Are you sure you want to re-activate this role?');
            });

            $('.clsEdit').click(function () {
                selectedRole = parseInt(this.id.substr(1));
                $('#addRoleModal').modal('show');
                $('.clsRole').removeClass('is-valid').removeClass('is-invalid');
                $('input:checkbox:checked').prop('checked', false);
                $('#clsRoleCategory').val(roles[selectedRole].role.role_category).change();
                $('#txtTarget').val(roles[selectedRole].role.role_target);
                $('#txtRoleName').val(roles[selectedRole].role.role_name);
                $('#cboRoleLanding').val(roles[selectedRole].role.role_landing_page);
                $('#txtRoleDescription').val(roles[selectedRole].role.role_description);
                $('#step1').trigger('click');
                $('#profile11').trigger('click');
                $.map(roles[selectedRole].myPrivileges, function (val, i) {
                    $('input:checkbox#p' + val.role_privilege_privilege_id).prop('checked', true);
                });
                role = roles[selectedRole].role;
            });



            hideLoader();
        }).fail(function (err) {
            console.log(err);
            showMessage('danger', 'Error', err, 5000);
        });
    }


  
    $('#btnAddNew').click(function () {
        $('#addRoleModal').modal('show');
        $('input:checkbox:checked').prop('checked', false);
        $('.clsRole').val('').removeClass('is-valid').removeClass('is-invalid');
        $('#txtRoleDescription').val('');
        $('#step1').trigger('click');
        $('#profile11').trigger('click');
        $('#txtTarget').removeClass('clsRole');
        $('.divTarget').hide('fast');
        role = {};
    });



    $('#clsRoleCategory').change(function () {
        if ($(this).val() == 'Officer') {
            $('#txtTarget').addClass('clsRole');
            $('.divTarget').show('fast');
        }
        else {
            $('#txtTarget').removeClass('clsRole');
            $('.divTarget').hide('fast');
        }
    });



    getPrivileges();
    function getPrivileges() {
        showLoader();

        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/home/getPrivileges",  //the url to call
            contentType: "application/json"
        }).done(function (data) {
            privileges = data;
            console.log(data);
            $('.list-group').html('');
            for (i = 0; i < data.length; i++) {
                var str = '<label class="list-group-item col-6">' +
                    '<input id="p' + data[i].privilege_id+'" data-index="'+i+'" class="form-check-input me-1" type="checkbox" value="" /> ' +
                    data[i].privilege_name + '</label>';
                $('#' + data[i].privilege_type).find('div.list-group').append(str);
            }

            hideLoader();
        }).fail(function (err) {
            console.log(err);
            showMessage('danger', 'Error', err, 5000);
        });
    }




    $('#btnSubmitConfirm').click(function () {

        $('#confirmModal').modal('hide');
        roles[selectedRole].role.role_status = roles[selectedRole].role.role_status == "Deleted" ? "Active" : "Deleted";
        saveRole(roles[selectedRole]);
    });


    $('#btnSubmitRole').click(function () {

        if ($(this).text() == 'Save Role') {
            if (validateFields1('clsRole')) {
                $('#step1').trigger('click');
                return;
            }
            if ($('input:checkbox:checked').length == 0) {
                showMessage('danger', 'Error', 'At least one privilege is required', 3000);
                return;
            }

            role.role_status = "Active";
            role.role_name = $('#txtRoleName').val();
            role.role_landing_page = $("#cboRoleLanding").val();
            role.role_description = $('#txtRoleDescription').val();
            roles[selectedRole].role.role_category = $('#clsRoleCategory').val();
            roles[selectedRole].role.role_target = $('#clsRoleCategory').val()=='Officer' ? parseInt($('#txtTarget').val()):0;
            role.role_client_id = client_id;
            var role_privileges = [];
            $('input:checkbox:checked').map(function () {
                role_privileges.push({
                    role_privilege_privilege_id: privileges[parseInt($(this).attr('data-index'))].privilege_id,
                    role_privilege_path: privileges[parseInt($(this).attr('data-index'))].privilege_path,
                    role_privilege_client_id: client_id
                });
            }).get();



            var details = {
                role: role,
                myPrivileges: role_privileges
            };

            saveRole(details);
        }
    });


    function saveRole(details) {
        


        console.log(details);
        showLoader();

        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + "/home/saveRole",  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(details),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    getRoles();
                    $('#addRoleModal').modal('hide');
                    showMessage('success', 'success', data.message, 3000);
                    $('#addNoticeModal').modal('hide');
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


    }





















    var navListItems = $('div.setup-panel div a'),
        allWells = $('.setup-content'),
        allNextBtn = $('.nextBtn');

    allWells.hide();

    navListItems.click(function (e) {
        e.preventDefault();
        var $target = $($(this).attr('href')),
            $item = $(this);

        if (!$item.hasClass('disabled')) {
            navListItems.removeClass('btn-primary').addClass('btn-light');
            navListItems.parent("div").find('small').removeClass('text-primary').removeClass('activeTab');
            $item.addClass('btn-primary');
            $item.parent("div").find('small').addClass('text-primary').addClass('activeTab');
            allWells.hide();
            $target.show();
            $target.find('input:eq(0)').focus();
            setTimeout(function () {
                if (parseInt($item.text()) == navListItems.length)
                    allNextBtn.text('Save Role');
                else
                    allNextBtn.text('Next');
            }, 10);




        }
    });

    allNextBtn.click(function () {
        console.log($(this).text());
        if ($(this).text() == 'Next') {
            nextStepWizard = $('div.setup-panel div a.btn-primary').parent().next().children("a");
            nextStepWizard.trigger('click');
        }
    });

    $('div.setup-panel div a.btn-primary').trigger('click');



});