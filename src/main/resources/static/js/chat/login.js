$(function () {
    let $loginBtn = $(".loginBtn");
    $('#username').focus().blur(checkName);
    $('#password').blur(checkPassword);
    $(document).keyup(function(event){
        if(event.keyCode ===13){
           login();
        }
    });
    $loginBtn.click(login)
});

function login(){
    let userName = $("#username").val();
    if (userName == null || userName.length === 0) {
        alert("请输入用户名！");
        return;
    }
    $.get("/doLogin?userName=" + userName, function (res, status) {
        if (res.code !== 200) {
            alert(res.msg);
            return;
        }
        window.location.href = "/index?userId=" + res.data;
    });
}

function checkName() {
    var name = $('#username').val();
    let msgInfo = $('#count-msg');
    if (name == null || name.length === 0) {
        //提示错误
        msgInfo.html("用户名不能为空");
        return;
    }
    var reg = /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/;
    if (!reg.test(name)) {
        msgInfo.html("输入3-10个字母或数字或下划线");
        return;
    }
    msgInfo.empty();
}

function checkPassword() {
    let password = $('#password').val();
    let $passwordMsg = $('#password-msg');
    if (password == null || password === "") {
        //提示错误
        $passwordMsg.html("密码不能为空");
        return false;
    }
    var reg = /^\w{3,10}$/;
    if (!reg.test(password)) {
        $passwordMsg.html("输入3-10个字母或数字或下划线");
        return false;
    }
    $passwordMsg.empty();
    return true;
}

