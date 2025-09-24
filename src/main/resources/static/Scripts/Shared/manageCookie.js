
var client_id = parseInt(getCookie('client_id'));
var user_username = getCookie('user_username');
var role_id = parseInt(getCookie('role_id'));
var access_token = getCookie('access_token');
var role_name = getCookie('role_name');
var user_id = parseInt(getCookie('user_id'));
var person_full_name = getCookie('person_full_name');
var person_name = getCookie('person_name');
var person_title = getCookie('person_title');
var person_surname = getCookie('person_surname');
var person_id = parseInt(getCookie('person_id'));
var login_time = getCookie('login_time');
var client_authority_code = getCookie('client_authority_code');
var client_logo = getCookie('client_logo');
var client_name = getCookie('client_name');
var client_status = getCookie('client_status');
var role_landing_page = getCookie('role_landing_page');
var user_password_type = getCookie('user_password_type');
var parameter = getCookie('parameter');
var target = 10;
var contact_cellphone = getCookie('contact_cellphone');
var contact_address = getCookie('contact_address');
var login_date = getCookie('login_date');
var user_status = getCookie('user_status');




var myPrivs = Array();
if (getCookie('myPrivs'))
    myPrivs = JSON.parse(getCookie('myPrivs'));

var myPrivsPath = Array();
if (getCookie('myPrivsPath'))
    myPrivsPath = JSON.parse(getCookie('myPrivsPath'));


function cleanCookies() {
    setCookie('login_time', '', 0);
    setCookie('client_id', '', 0);
    setCookie('user_username', '', 0);
    setCookie('role_id', '', 0);
    setCookie('role_name', '', 0);
    setCookie('access_token', '', 0);
    setCookie('user_id', '', 0);
    setCookie('person_full_name', '', 0);
    setCookie('person_name', '', 0);
    setCookie('person_id', '', 0);
    setCookie('person_surname', '', 0);
    setCookie('client_name', '', 0);
    setCookie('person_title', '', 0);
    setCookie('myPrivs', '', 0);
    setCookie('myPrivsPath', '', 0);
    setCookie('role_landing_page', '', 0);
    setCookie('user_password_type', '', 0);
    setCookie('parameter', '', 0);
    setCookie('client_authority_code', '', 0);
    setCookie('client_logo', '', 0);
}



function deleteAllCookies() {
    document.cookie.split(';').forEach(cookie => {
        const eqPos = cookie.indexOf('=');
        const name = eqPos > -1 ? cookie.substring(0, eqPos) : cookie;
        document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT';
    });
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000) + (2 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}
function setCookieMin(cname, cvalue, exMins) {
    var d = new Date();
    d.setTime(d.getTime() + (exMins * 60 * 1000) + (2 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function checkPriv(priv) {
    if (priv && myPrivs.indexOf(priv) < 0) {
        showMessage('danger', 'access denied', deniedMsg, 3000);
        location.href = role_landing_page;
    }
}


function getCookie(cname) {
    let name = cname + "=";
    let ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function checkCookie(cname) {
    let user = getCookie(cname);
    if (user != "") {
        return true;
    } else {
        return false;
    }
}
