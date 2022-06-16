let $register = $(".register");
$(function () {
    let $account = $(".account");
    $register.click(register);
    $account.focus().blur(function () {
        let account = $account.val();
        if (account) {
            let accountPass = patternCheck(account);
            if (!accountPass) {
                layer.alert("用户名格式为数字或字母");
                return;
            }
        }
        let lastRepeatAccount = $register.data("lastAccount");
        if (account && (lastRepeatAccount !== account)) {
            checkRepeat(account);
        }
    })
});

function patternCheck(account) {
    let pattern = /^[a-zA-Z0-9_-]{4,16}$/;
    return pattern.test(account);
}

function checkRepeat(val) {
    $register.data("lastAccount", val);
    $.get(ctx + "/user/checkRepeat?account=" + val, function (res) {
        if (res.code !== 200) {
            layer.alert(res.msg);
        }
    })
}

function register() {
    let $account = $(".account"),
        $nickName = $(".nickName"),
        $password = $(".password"),
        account = $account.val(),
        nickName = $nickName.val(),
        password = $password.val();
    $register.data("lastAccount", account);
    if (!account) {
        layer.alert("请输入账号");
        return;
    }
    if (!nickName) {
        layer.alert("请输入昵称");
        return;
    }
    if (!password) {
        layer.alert("请输入密码");
        return;
    }
    $.ajax({
        type: "POST",
        url: ctx + "/user/register",
        data: {account: account, nickName: nickName, password: $.base64.encode(password)},
        async: true,
        success: function (res) {
            if (res.code !== 200) {
                layer.alert(res.msg);
                return;
            }
            layer.msg("注册成功");
            setTimeout(function () {
                window.location.href = ctx + "/login";
            }, 1000)
        },
        error: function (res) {
            layer.alert(res.responseJSON.message);
        }
    })

}
