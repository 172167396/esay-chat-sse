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