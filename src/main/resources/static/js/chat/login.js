$(function () {
    let $loginBtn = $(".loginBtn");
    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            login();
        }
    });
    $loginBtn.click(login)
});


function refreshCaptcha(e) {
    $(e).attr("src", "/captcha?v=" + Math.random());
}

function login() {
    let account = $(".account").val(),
        password = $(".password").val(),
        captcha = $(".captcha").val();
    if (!checkEmpty(account, "用户名")
        || !checkEmpty(password, "密码")
        || !checkEmpty(captcha, "验证码")) {
        return;
    }
    $.ajax({
        type: "POST",
        async: true,
        data: {account: account, password: $.base64.encode(password), captcha: captcha},
        url: ctx + "/login",
        success: function (res) {
            if (res.code !== 200) {
                layer.alert(res.msg);
                $(".captchaImg").click();
                return;
            }
            window.location.href = ctx + "/index";
        },
        error: function (res) {
            $(".captchaImg").click();
            layer.alert(res.responseJSON.message);
        }
    })
}

function checkName() {
    let name = $('.account').val();
    if (name == null || name.length === 0) {
        layer.alert("用户名不能为空");
        return false;
    }
    return true;
}

function checkPassword() {
    let password = $('.password').val();
    if (password == null || password === "") {
        layer.alert("密码不能为空");
        return false;
    }
    return true;
}

