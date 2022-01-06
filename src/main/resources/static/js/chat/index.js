let host = location.host;
$(function () {

    let url = `http://${host}/connect/` + userId;
    let source = new EventSource(url);
    source.onopen = function () {
        console.log(userId + "加入");
    };

    source.addEventListener("message", function (e) {
        let msgJson = JSON.parse(e.data);
        let messageEntity = msgJson.message;
        let users = msgJson.onlineUsers;
        let $people = $(".people");
        if (messageEntity.messageType === 1) {
            renderUsers(users, $people);
        } else if (messageEntity.messageType === 2) {
            renderMsg(messageEntity);
        } else if (messageEntity.messageType === 3) {
            removeUser(messageEntity.createUserId);
        }
        choosePerson();
    });

    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            sendMsg();
        }
    });
    $(".send").click(sendMsg);

    let _beforeUnload_time = 0, _gap_time = 0;
    //是否是火狐浏览器
    // let is_fireFox = navigator.userAgent.indexOf("Firefox") > -1;
    window.onunload = function () {
        _gap_time = new Date().getTime() - _beforeUnload_time;
        if (_gap_time <= 5) {
            $.get("/close/" + userId);
        }
        // else {
        //     //刷新
        // }
    }

    window.onbeforeunload = function () {
        _beforeUnload_time = new Date().getTime();
    };
})


function removeUser(id) {
    $(`li[data-id='${id}']`).remove();
    $(".sendTo").text("");
    $(`#${id}`).remove();
}

function getDate(dt) {
    let year = dt.getFullYear();
    let month = dt.getMonth() + 1;
    let day = dt.getDate();
    let hour = dt.getHours();
    let minute = dt.getMinutes();
    let second = dt.getSeconds();
    //星期
    // let arr = ["天", "一", "二", "三", "四", "五", "六"];
    // let week = dt.getDay();//4


    // month = month < 10 ? "0" + month : month;
    // day = day < 10 ? "0" + day : day;
    // hour = hour < 10 ? "0" + hour : hour;
    // minut = minut < 10 ? "0" + minut : minut;
    // second = second < 10 ? "0" + second : second;

    //定义一个补位的函数
    function buWei(i) {
        i = i < 10 ? "0" + i : i;
        return i;
    }

    return year + "-" + buWei(month) + "-" + buWei(day) + " " + buWei(hour) + ":" + buWei(minute) + ":" + buWei(second);
}


function choosePerson() {
    let $chat = $(".chat");
    $(".person").bind("click", function () {
        if ($chat.length > 0) {
            $(".chat").each(function (index, ele) {
                $(ele).removeClass("active");
                $(ele).removeClass("active-chat");
            });
        }
        let $this = $(this);
        let $unRead = $this.children(".notRead");
        let id = $this.attr("data-id");
        $(".sendTo").text($this.attr("data-name"));
        $unRead.text("");
        $unRead.removeClass("unRead");
        let chatPage = $(`#${id}`);
        if (chatPage.length > 0) {
            chatPage.addClass("active active-chat");
        } else {
            let $content = "";
            // find record
            $.get("/findRecord?userId=" + userId + "&choseId=" + id, function (res) {
                console.log(res);
                console.log(userId);
                $.each(res, function (index, e) {
                    if (index === 0) {
                        $content += `<div class="conversation-start"><span>${e.sendTime}</span></div>`;
                    }
                    $content += `<div class="bubble ${e.createUserId === userId ? 'me' : 'you'}">${e.content}</div>`;
                })
                $(".write").before(`<div class="chat active active-chat" id="${id}" data-id="${id}">${$content}</div>`);
            })
        }
    });
}

function sendMsg() {
    let $active = $(".active-chat");
    if ($active.length === 0) {
        alert("请选择需要发送的对象!");
        return;
    }
    let sendTo = $active.attr("data-id");
    let $message = $(".message");
    let message = $message.val();
    if (message == null || message.length === 0) {
        alert("请输入您要发送的消息!");
        return;
    }
    $.get("/pushTo?message=" + message + "&id=" + sendTo + "&sender=" + userId);
    let $sendTime = $active.children(".conversation-start");
    if ($sendTime.length === 0) {
        $active.append(`<div class="conversation-start">
                    <span>${getDate(new Date())}</span>
                </div>`);
    }
    $active.append(`<div class="bubble me">${message}</div>`);
    $message.val("");
}

function renderMsg(messageEntity) {
    let msg = messageEntity.content;
    let createUser = messageEntity.createUserId;
    let $user = $(`li[data-id='${createUser}']`);
    let sendTime = messageEntity.sendTime;
    let chatPage = $(`#${createUser}`);
    if (chatPage.length > 0) {
        let startTime = chatPage.children(".conversation-start");
        if (startTime.length === 0) {
            chatPage.append(`<div class="conversation-start">
                    <span>${sendTime}</span>
                </div>`);
        }
        chatPage.append(`<div class="bubble ${createUser === userId ? 'me' : 'you'}">
                    ${msg}
                </div>`)
    } else {
        $(".write").before(`<div class="chat" id="${createUser}" data-id="${createUser}">
                <div class="conversation-start">
                    <span>${sendTime}</span>
                </div>
                <div class="bubble ${createUser === userId ? 'me' : 'you'}">
                    ${msg}
                </div>
            </div>`);
    }
    let $unRead = $user.children(".notRead");
    let $prevMsg = $user.children(".preview");
    $unRead.addClass("unRead");
    $prevMsg.text(msg);
    let unReadCount = $unRead.text();
    if (unReadCount === "") {
        $unRead.text("1");
        return;
    }
    let c, count = (c = parseInt(unReadCount) + 1) > 99 ? 99 : c;
    if (count > 99) {
        $unRead.text(count);
        return;
    }
    $unRead.text(c);
}

function renderUsers(users, userTab) {
    let others = [...users].filter(u => u.id !== userId);
    userTab.empty();
    $.each(others, function (index, ele) {
        let userInfo = `<li data-id="${ele.id}" data-name="${ele.userName}" class="person" data-chat="person${index}">
                    <img src="img/${ele.avatar}" alt="" />
                    <span class="name">${ele.userName}</span>
                    <span class="time">${ele.loginTime}</span>
                    <span class="notRead"></span>
                    <span class="preview">${ele.lastPush ?? ""}</span>
                </li>`;
        userTab.append(userInfo);
    })
}





