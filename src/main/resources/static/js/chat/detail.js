const tpl = ``, $chat = $(".chat"),
    $container = $(".container"),
    $message = $(".message"),
    $sendBtn = $(".send"),
    socket = window.parent.socket;
$(function () {
    let id = $(".container").data("id");
    getRecord(id);
    $sendBtn.click(sendMsg);
    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            $sendBtn.click();
        }
    });
})

function getRecord(id) {
    FetchUtil.get(ctx + "/chat/records/" + id).then(res => {
        if (res?.data) {
            $.each(res.data, (i, e) => {
                const $record = $(`<div class="bubble ${e.whois}">${e.content}</div>`);
                $record.data("createTime", e.createTime);
                $chat.append($record);
            })
        }
    })
}

function sendMsg() {
    alert(123)
    if (!$container) {
        return;
    }
    let msg = $message.val();
    if (!msg) {
        layer.tips('发送内容不能为空，请重新输入', $sendBtn, {tips: [1, '#3595CC'], time: 700})
        return;
    }
    if (!socket) {
        return;
    }
    socket.send(JSON.stringify({content: encodeURIComponent(msg), receiver: $container.data("id"), messageType: 1}));
    let $lastRecord = $chat.children(":last-child");

    // if ($sendTime.length === 0) {
    //     $active.append(`<div class="conversation-start"><span>${getDate(new Date())}</span></div>`);
    // }
    $chat.append(`<div class="bubble me">${msg}</div>`);
    $message.val("");
}