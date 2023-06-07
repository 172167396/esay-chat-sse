$(function () {
    initUserInfo();
    initCropperPage();
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


function initCropperPage() {
    $(".camera").on('click', function () {
        top.layer.open({
            type: 2,
            shade: 0,
            offset: '110px',
            skin: '',
            maxmin: false,
            fixed: false,
            // btn: ['保存', '取消'],
            // btnAlign: 'c', //按钮居右
            content: ctx + "/file/cropper",
            area: ['400px', '505px']
        });
    });
}


function changeAvatar(e) {
    $(".avatar").attr("src", "/file/view/" + e);
}


