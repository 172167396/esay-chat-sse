$(function () {
    init().then(() => agreeClick());
})

async function init() {
    let resJson = await FetchUtil.get(ctx + "/friend/received"), $applies = $(".applies");
    if (resJson?.data) {
        $.each(resJson.data, (i, e) => {
            const $applyLine = $(`<div class="applyLine">
            <p class="applyDate">${e.createTime}</p>
            <div class="mainInfo">
                <div class="avatarDiv">
                    <img src="${e.avatar ?? ''}">
                </div>
                <div class="userInfo">
                    <p>${e.userName}</p>
                    <p>来源：${e.source}</p><br>
                    <p>${e.remark}</p>
                    <button>回复</button>
                </div>
                    ${(function () {
                if (e.state === 0) {
                    return `<div class="applyBtn">
                        <button class="agree">同意</button>
                        <button class="ignore">忽略</button>
                    </div>`;
                } else {
                    return `<span class="handledApply">${e.state === 1 ? '已同意' : '已拒绝'}</span>`;
                }
            })()}
                </div>
            </div>`);
            $applyLine.data("id", e.id);
            $applies.append($applyLine);
        })
    }
}

function agreeClick(e) {

    let $agree = $(".agree");
    $agree.click(function (e) {
        let id = $(e.target).closest(".applyLine").data("id"),
            remarkName = "";

        let index = top.layer.open({
            type: 2,
            offset: '160px',
            skin: 'my-skin',
            title: `添加`,
            maxmin: false,
            id: 'layerDemo' + uuid(), //防止重复弹出,
            content: ctx + "/friend/applyInfo/" + id,
            btn: ['确定'],
            btnAlign: 'r', //按钮居右
            shade: 0, //不显示遮罩
            // title: false,
            area: ['260px', '180px'],
            fixed: false,
            yes: function (index, layero) {//layer.msg('yes');    //点击确定回调
                let body = top.layer.getChildFrame("body", index);
                let $select = body.find(".group-select"),
                    $inputName = body.find(".input-nickName");
                agree({id: id, groupId: $select.val(), remarkName: $inputName.val()}).then(() => {
                    top.layer.close(index);
                });
            },
            btn2: function (index) {
                layer.close(index);//layer.closeAll();
            }
        });
    })
}

function agree(obj) {
    return FetchUtil.post(ctx + "/friend/agree", obj);
}