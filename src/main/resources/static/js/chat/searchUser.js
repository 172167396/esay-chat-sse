$(function () {
    let $searchBtn = $(".searchBtn");
    $searchBtn.click(function () {
        let content = $(".searchInput").val(),
            $searchResult = $("#searchResult");
        if (!content) {
            layer.msg("请输入账号或昵称后再点击查找");
            return;
        }
        searchUser(content).then(renderSearchedUsers)
            .then(bindApplyFunc)
            .catch(errorHandler);
    })
    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            $searchBtn.click();
        }
    });

})

function bindApplyFunc() {
    $(".apply").click(function () {
        let myName = $(".username", parent.document).val(),
            id = $(this).data("id");
        top.layer.open({
            type: 2,
            offset: '160px',
            skin: 'my-skin',
            title: `${myName}-添加好友`,
            maxmin: false,
            id: 'layerDemo' + uuid(), //防止重复弹出,
            content: ctx + "/user/applyPage/" + id,
            btn: ['下一步', '关闭'],
            btnAlign: 'r', //按钮居右
            shade: 0, //不显示遮罩
            // title: false,
            area: ['500px', '370px'],
            fixed: false,
            yes: function (index, layero) {//layer.msg('yes');    //点击确定回调
                let body = top.layer.getChildFrame("body", index);
                let $applyMsg = body.find(".apply-msg"),
                    $chooseGroup = body.find(".choose-group"),
                    $finish = body.find(".finish");
                if ($applyMsg.is(":visible")) {
                    $applyMsg.hide();
                    //query group
                    let $select = $chooseGroup.find(".group-select")
                    renderGroupSelect($select).then(() => $chooseGroup.show())
                }
                if ($chooseGroup.is(":visible")) {
                    $chooseGroup.hide();
                    //
                    sendApply(body, id).then(() => {
                        layero.find(".layui-layer-btn0").hide();
                        layero.find(".layui-layer-btn1").text("完成");
                        $finish.show();
                    })
                }
            },
            btn2: function (index) {
                layer.close(index);//layer.closeAll();
            }
        });
    })
}

function renderSearchedUsers(data) {
    let $searchResult = $("#searchResult");
    $searchResult.empty();
    if (!data || data.length === 0) {
        adjustResultArea();
        //没有找到符合搜索条件的用户
        $searchResult.append("<p style='vertical-align: middle;text-align: center;padding-top: 7em;'>没有找到符合搜索条件的用户</p>");
        return;
    }
    $.each(data, function (i, e) {
        $searchResult.empty();
        $searchResult.append(`<div class="user inline-block">
                <div class="avatarDiv inline-block">
                    <img alt="" class="userAvatar" src="${e.avatarUrl}"/>
                </div>
                <div class="userInfo inline-block">
                    <p class="names text-ell" title="${e.name}(${e.account})">${e.name}(${e.account})</p>
                    <i class="gender boy inline-block"></i>
                    <button class="apply" data-id="${e.id}">加好友</button>
                </div>
                </div>`);
    });
    adjustResultArea();
}

function adjustResultArea() {
    let item = localStorage.getItem("searchPageIndex");
    $.changeFrameSize(item, "400px", () => {
        $("#searchResult").height('16em')
    });
}

function renderGroupSelect($select) {
    return fetcher(ctx + "/user/group-list").then(data => {
        if (data) {
            $.each(data, function (i, e) {
                $select.append(`<option value="${e.id}">${e.name}</option>`)
            })
        }
    });
}


function searchUser(content) {
    return fetch(ctx + "/user/search?content=" + encodeURIComponent(content))
        .then(r => r.json())
        .then(r => r.data);
}

function sendApply(body, id) {
    return fetch(ctx + "/user/apply/" + id, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencode'
        },
        body: {id:id,groupId:body.find(".group-select").val(),nickName:body.find(".input-nickName").val(),}
    });
}


