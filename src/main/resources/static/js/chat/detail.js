const $container = $(".container"),
    $messageEditor = $("#editor"),
    $sendBtn = $(".sendBtn"),
    socket = window.parent.socket;
$(function () {
    let id = $(".container").data("id"), $chat = $(".chat");
    $(document).on("click", function (e) {
        let $target = $(e.target), $chooseEnter = $(".chooseEnter");
        if ($target.hasClass("sendWays")) {
            $chooseEnter.slideToggle(100);
            return;
        } else if ($target.hasClass("chooseEnter")) {
            return;
        }
        $chooseEnter.hide();
    })
    //查询聊天记录
    getRecord(id).then(() => $chat.get(0).scrollTop = $chat.get(0).scrollHeight);
    $sendBtn.click(sendMsg);
    // onEnter(() => $sendBtn.click());

    initEmoji();
    screenshotBtn();
    onSelectPicture();
})


function getRecord(id) {
    return FetchUtil.get(ctx + "/chat/records/" + id).then(res => {
        if (res?.data) {
            $.each(res.data, (i, e) => {
                const $record = $(`<div class="bubble ${e.whois}">${e.content}</div>`);
                $record.data("createTime", e.createTime);
                $(".chat").append($record);
            })
        }
    })
}

function sendMsg(e) {
    if ($(e.target).hasClass("sendWays")) {
        return;
    }
    if (!$container) {
        return;
    }
    let $chat = $(".chat"),
        $editorClone = $editor.clone(true),
        empty = !$editor.text() && !$editor.find("img").length;
    $editorClone.find("img").each((i, e) => {
        let parsedEmoji;
        const $img = $(e), src = $img.attr("src");
        if (src && src.startsWith("/emoji")) {
            let name = src.split("/")[5], nameArr = name?.split(".") ?? [];
            parsedEmoji = nameArr[1]?.endsWith("jpg") ? `:${nameArr[0]}:` : `#qq_${nameArr[0]}#`;
            $(e).replaceWith(simplifyEmoji(parsedEmoji));
        } else {
            $(e).replaceWith("[图片]");
        }
    })
    if (empty) {
        $editor.empty();
        $editor.focus();
        layer.tips('发送内容不能为空，请重新输入', $sendBtn, {tips: [1, '#3595CC'], time: 700})
        return;
    }
    if (!socket || socket.isClosed) {
        return;
    }
    let receiver = $container.data("id"), msg = $editor.html();
    socket.send(JSON.stringify({content: encodeURIComponent(msg), receiver: receiver, messageType: 1}));
    let $lastRecord = $chat.children(":last-child");
    console.log($lastRecord)
    // if ($sendTime.length === 0) {
    //     $active.append(`<div class="conversation-start"><span>${getDate(new Date())}</span></div>`);
    // }
    $chat.append(`<div class="bubble me">${$editor.html()}</div>`);
    $chat.get(0).scrollTop = $chat.get(0).scrollHeight;
    console.log($editorClone)
    $(`#${receiver}`, parent.document).find(".short-desc").html($editorClone.html());
    $editor.empty();
}

function initEmoji() {
    $messageEditor.emoji({
        button: ".emoji",
        scale: 0.8,
        left: "-55",
        top: "169px",
        showTab: true,
        animation: 'slide',
        icons: iconsConfig
    });
}

function screenshotBtn() {
    $(".screenshot").click(() => {
        window.parent.triggerScreenshot();
    })
}

function onSelectPicture() {
    $("#selectPicture").change(function () {
        let file = $(this).prop("files")[0];
        if (!file) {
            return;
        }
        let ext = getExtension(file.name);
        if (!ext) {
            return;
        }
        if (!isPicture(ext)) {
            return;
        }
        renderPictureMsg(file);
    })

}

function renderPictureMsg(file) {
    // let pictureUrl = getObjectURL(file);
    // let img = `<img alt="" src="${pictureUrl}">`;
    // $(img).width("40px").height("40px").appendTo($editor);
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function (e) {
        let mb = (e.total / 1024) / 1024;
        if (mb >= 2) {
            alert('文件大小大于2M');
            return;
        }
        let img = `<img alt="" src="${e.target.result}">`, $img = $(img);
        // $(img).width("80px").height("80px");
        $img.css({"width": "80px", "height": "80px"});
        $messageEditor.append($img);
    }
}