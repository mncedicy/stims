
var deniedMsg = 'You do not have the appropriate permissions to access this page.';


$(document).ready(function () {


    $('.person_full_name').html(person_full_name);
    $('.role_name').html(role_name);
    $('.client_logo').attr('src', client_logo);
    $('.client_name').html(client_name);

    $('.user_status').html(user_status);
    $('.user_username').html(user_username);
    $('.contact_cellphone').html(contact_cellphone);
    $('.contact_address').html(contact_address);
    $('.login_date').html(login_date);




    $(".navMenu").click(function () {
        setCookie('parameter', '', 0);
        var priv = parseInt($(this).attr('data-priv'));
        var index = myPrivs.indexOf(priv);
        if (priv && index >= 0) {
            location.href = myPrivsPath[index];
        } else {
            showMessage('danger', 'access denied', deniedMsg, 5000);
        }
    });



    $(".navHome").click(function () {
        setCookie('parameter', '', 0);
        location.href = role_landing_page;
    });

  checkPriv();
    function checkPriv() {
        if (!getCookie('user_username')) {
            location.href = '/';
        }
        else {
            var index = myPrivsPath.indexOf(location.pathname);
            if (index < 0 && location.pathname != role_landing_page) {
                setCookie('parameter', '', 0);
                location.href = role_landing_page;
            }
            else {
                var elem = $("a.navMenu[data-priv='" + myPrivs[index] + "']");
                if (location.pathname == '/Home')
                    elem = $("a.navHome[data-priv='0']");
                elem.parent('li.pc-item').addClass('active');
                elem.parent('li').parent('ul').show();
                elem.parent('li').parent('ul').parent('li.pc-hasmenu').addClass('active').addClass('pc-trigger');
            }
        }
    }





});