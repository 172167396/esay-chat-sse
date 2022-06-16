$(function () {
    let $loginBtn = $(".loginBtn");
    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            login();
        }
    });
    $loginBtn.click(login)
});

function login() {
    let account = $(".account").val();
    let password = $(".password").val();
    if (!checkName() || !checkPassword()) {
        return;
    }
    $.ajax({
        type: "POST",
        async: true,
        data: {account: account, password: $.base64.encode(password)},
        url: "/login",
        success: function (res) {
            if (res.code !== 200) {
                layer.alert(res.msg);
                return;
            }
            window.location.href = ctx + "index";
        },
        error: function (res) {
            layer.alert(res.responseJSON.message);
        }
    })
}

function checkName() {
    let name = $('.account').val();
    if (name == null || name.length === 0) {
        alert("用户名不能为空");
        return false;
    }
    return true;
}

function checkPassword() {
    let password = $('.password').val();
    if (password == null || password === "") {
        alert("密码不能为空");
        return false;
    }
    return true;
}

