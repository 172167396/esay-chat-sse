$(function () {
    let $searchBtn = $(".searchBtn");
    layui.use("form", function () {
        let form = layui.form;
        form.render();
    })
    $searchBtn.click(function () {
        let content = $(".searchInput").val(),
            $searchResult = $("#searchResult");
        if (!content) {
            layer.msg("请输入账号或昵称后再点击查找");
            return;
        }
        searchUser(content).catch(e => {
            console.log(e);
            layer.alert(e);
            return null;
        }).then(renderSearchedUsers);


    })
})

function bindApplyFunc() {
    $(".apply").click(function () {
        let myName = $(".username").val(),
            id = $(this).data("id");
        layer.open({
            type: 2,
            offset: 'auto',
            title: `${myName}-添加好友`,
            maxmin: false,
            id: 'layerDemo' + uuid(), //防止重复弹出,
            content: ctx + "/user/apply/" + id,
            btn: ['下一步', '关闭'],
            btnAlign: 'r', //按钮居右
            shade: 0, //不显示遮罩
            // title: false,
            area: ['500px', '420px'],
            fixed: false,
            btn2: function (index) {
                layer.alert("关闭");
            }
        }, function (index, layero) {
            layer.alert("下一步");
        });
    })
}

function renderSearchedUsers(json) {
    if (!json) {
        return;
    }
    let $searchResult = $("#searchResult");
    $.each(json, function (i, e) {
        $searchResult.empty();
        $searchResult.append(`<div class="user inline-block">
                <div class="avatarDiv inline-block">
                    <img class="userAvatar" src="${e.avatarUrl}"/>
                </div>
                <div class="userInfo inline-block">
                    <p class="names text-ell">${e.name}(${e.account})</p>
                    <i class="gender boy inline-block"></i>
                    <button class="apply" data-id="${e.id}">加好友</button>
                </div>
                </div>`);
    })
}

function searchUser(content) {
    return fetch(ctx + "/user/search/" + encodeURIComponent(content))
        .then(r => r.json())
        .then(r => r.data);
}