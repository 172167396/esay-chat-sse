$(function () {
    init().then(() => {
        buttonClick()
    });
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
                    <p title="${e.remark}">${e.remark}</p>
                    ${(function () {
                return e.state === 0 ? `<button>回复</button>` : '';
            })()}
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

function buttonClick() {
    let $btn = $(".applyBtn button");
    $btn.click(function () {
        let $this = $(this),
            id = $this.closest(".applyLine").data("id"),
            isAgree = $this.hasClass("agree");
        isAgree ? showAgree(id) : showIgnore(id);
    })
}

function showAgree(id) {
    top.layer.open({
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
        yes: function (index, layero) {
            let body = top.layer.getChildFrame("body", index);
            let $select = body.find(".group-select"),
                $inputName = body.find(".input-nickName"),
                $applyBtn = $(document).find(".applyBtn"),
                reqObj = {id: id, groupId: $select.val(), remarkName: $inputName.val()};
            agree(reqObj).then(() => {
                $applyBtn.empty();
                $applyBtn.append(`<span class="handledApply">已同意</span>`);
                top.layer.close(index);
            });
        },
        btn2: function (index) {
            layer.close(index);
        }
    });
}

function showIgnore(id) {
    top.layer.confirm('确定忽略该请求?', {icon: 3, title: '提示', btn: ['确定', '取消']}, function (index, layro) {
        //do something
        alert("click yes");
        ignore(id).then(() => {
            let $applyBtn = $(document).find(".applyBtn")
            $applyBtn.empty();
            $applyBtn.append(`<span class="handledApply">已忽略</span>`);
            top.layer.close(index)
        });

    }, function (index) {
        layer.close(index);
    });
}

function agree(obj) {
    return FetchUtil.post(ctx + "/friend/agree", obj);
}

function ignore(id) {
    return FetchUtil.post(ctx + `/friend/ignore?id=${id}`);
}