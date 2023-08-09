$(function () {
    layui.use(['table', 'layer', 'jquery', 'laydate', 'form', 'element'], function () {
        var table = layui.table,
            laydate = layui.laydate,
            form = layui.form,
            $ = layui.jquery;
        birthdaySelect(laydate);
        initUserInfo();
    })
})

function initUserInfo() {
    FetchUtil.get(ctx + "/user/info").then(res => {
        if (res?.data) {
            let data = res.data;
            Object.keys(data).forEach(k => $(`.${k}`)?.text(data[k] ?? "-"));
            $(".avatar").attr("src", data.avatarPath ?? "");
        }
    })
}


function birthdaySelect(laydate) {
    laydate.render({
        elem: '#authTimeStart'
        , theme: '#18b9dc'
        , isInitValue: true
        , trigger: 'click'
        , value: start_date
        , done: function (value, date, endDate) {
            var authTimeEnd = $('#authTimeEnd').val();
            if (value !== "" && authTimeEnd !== "" && value > authTimeEnd) {
                parent.layer.alert("授权时间起不能大于授权时间止");
                // return;
            }
            // if (beyondDays(value, authTimeEnd, 365)) {
            //     parent.layer.alert("授权时间起止最大跨度为1年");
            // }
        }
    });
}


function changeAvatar(e) {
    $(".avatar").attr("src", "/file/view/" + e);
}


