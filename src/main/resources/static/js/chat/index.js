let host = location.host;
$(function () {
    // $(document).on("click", "#recent-chat .chat-line", function (e) {
    //     alert(e.target);
    // });
    layui.use(['tree', 'dropdown', 'util'], function () {
        let tree = layui.tree
            , layer = layui.layer
            , util = layui.util,
            dropdown = layui.dropdown;
        initRecentChat().then(data => {
            //绑定聊天列表点击事件
            bindChatClick();
        })
        // initFriendGroups(tree).then(data => {
        //     initDropdown(dropdown);
        //     clickGroup();
        //     return data;
        // }).then(data => {
        //
        //     tree.render({
        //         elem: '#userGroups'
        //         , data: data
        //         , showLine: false  //是否开启连接线
        //     });
        // });
        getFriendsAndRefreshGroup();
        bindAddBtn();
        registerAddBtnClick(layer);
        bindSwitchBtnClick();

    });


    // let url = `http://${host}/connect/`;
    // let source = new EventSource(url);
    // source.onopen = function () {
    //     console.log("connected.....");
    // };

    // source.addEventListener("message", function (e) {
    //     let msgJson = JSON.parse(e.data);
    //     let messageEntity = msgJson.message;
    //     console.log(msgJson);
    //     console.log(messageEntity);
    //
    //     // let users = msgJson.onlineUsers;
    //     // let $people = $(".people");
    //     // if (messageEntity.messageType === 1) {
    //     //     renderUsers(users, $people);
    //     // } else if (messageEntity.messageType === 2) {
    //     //     renderMsg(messageEntity);
    //     // } else if (messageEntity.messageType === 3) {
    //     //     removeUser(messageEntity.createUserId);
    //     // }
    //     // choosePerson();
    // });
    //监听enter
    onEnter(null);
    $(".send").click(null);

    let _beforeUnload_time = 0, _gap_time = 0;
    //是否是火狐浏览器
    // let is_fireFox = navigator.userAgent.indexOf("Firefox") > -1;
    window.onunload = function () {
        _gap_time = new Date().getTime() - _beforeUnload_time;
        if (_gap_time <= 5) {
            $.get("/close");
        }
        // else {
        //     //刷新
        // }
    }

    window.onbeforeunload = function () {
        _beforeUnload_time = new Date().getTime();
    };
})


function initRecentChat() {
    return FetchUtil.get(ctx + "/chat/recent")
        .then(r => {
            if (r.code !== 200) {
                layer.alert(r.msg);
                return null;
            }
            let $recentChat = $("#recent-chat"), $chatTarget = "";
            const $container = $recentChat.children('ul');
            $.each(r.data, function (i, e) {
                const $chatLine = $(`<li class="chat-line">
                                <div class="${e.type}">
                                    <div class="notice-msg relative">
                                        <div class="sys-notice ${e.type === 'NOTICE' || '' ? 'show' : 'displayNone'}">
                                        </div>
                                        <div class="new-apply ${e.type === 'NEW_FRIEND' ? 'show' : 'displayNone'}">
                                        </div>
                                        <div class="recent-chat ${e.type === 'PERSONAL' ? 'show' : 'displayNone'}">
                                            <img class="chat-avatar" src="${e.avatar ?? ''}">
                                        </div>
                                        <p id="nickName">${e.name}</p>
                                        <p id="chatDate">${e.chatDate}</p>
                                        <p id="short-desc">${e.briefMsg}</p>
                                    </div>
                                </div>
                            </li>`);
                $chatLine.data("id", e.targetId);
                $chatLine.data("type", e.type);
                $recentChat.append($chatLine);
            })
        })
}

function bindChatClick() {
    let $recentChatLi = $("#recent-chat").children("li"), $iframe = $("#rightFrame");
    $recentChatLi.each(function (i, e) {
        let $this = $(e);
        $this.click(function () {
            let type = $this.data("type"), id = $this.data("id");
            if (type === 'NEW_FRIEND') id = type;
            $iframe.attr("src", ctx + "/chat/recent/" + id);
        })
    })

}

function getFriendsAndRefreshGroup() {
    let $groupsUl = $("#groups");
    FetchUtil.get(ctx + "/user/friends").then(res => {
        if (res?.data) {
            $.each(res.data, function (i, e) {
                let tmp = `<li class="layui-nav-item">
                            <a class="" href="javascript:;">${e.groupName}
                            <i class="expand layui-icon layui-icon-triangle-r layui-nav-more"></i>
                            </a>
                            <dl class="layui-nav-child">
                            ${e.users.map(item => {
                    return `<dd><img class="avatarInGroup" src="${item.avatar}"><a class="inline-block" href="javascript:;">${item.name}</a></dd>`;
                }).join('')
                }</dl></li>`
                const $li = $(tmp);
                $groupsUl.append($li);
            })
        }
    }).then(() => {
        $groupsUl.append(`<span class="layui-nav-bar" style="top: 55px; height: 0; opacity: 0;"></span>`);
        refreshNav();
    });
}

function refreshNav() {
    layui.use('element', function () {
        let element = layui.element,
            layFilter = $("#groups").attr('lay-filter');
        element.render('nav', layFilter);
    })
}


function showNewApply() {
    let $applies = $(".applies"), $chats = $(".chats");
    $(".new-apply").click(function () {
        $chats.hide();
        $applies.addClass("active");
    })
}

function registerAddBtnClick(layer) {
    $(".addAction").click(function () {
        let $this = $(this),
            action = $this.data("action");
        let index = layer.open({
            type: 2,
            offset: '160px',
            title: ' ',
            maxmin: false,
            id: 'layerDemo' + action, //防止重复弹出,
            content: ctx + "/user/searchPage/" + action,
            // ,btn: '关闭全部'
            // ,btnAlign: 'c' //按钮居中
            shade: 0, //不显示遮罩
            // title: false,
            area: ['600px', '220px'],
            fixed: false
            // ,yes: function(){
            //     layer.closeAll();
            // }
        });
        localStorage.setItem("searchPageIndex", index);
    })
}

function bindAddBtn() {
    let $addBtn = $("#addBtn"),
        $addPanel = $("#addPanel");
    $addBtn.click(function () {
        $("#addPanel").show();
    })
    $addBtn.mouseleave(function () {
        $("#addPanel").hide();
    })
    $addPanel.mouseenter(function () {
        $("#addPanel").show();
    })
    $addPanel.mouseleave(function () {
        $("#addPanel").hide();
    })
}

function clickGroup() {
    let $group = $(".layui-tree-main");
    $group.click(function () {
        $(this).find("i").toggleClass("layui-tree-iconArrow-down");
    })
}

function bindSwitchBtnClick() {
    $(".switch-btn").find("li").click(function () {
        let $this = $(this),
            linkedClass = $this.data("link"),
            $linkClass = $(`.${linkedClass}`);
        $this.addClass("active");
        $this.siblings().removeClass("active");
        $linkClass.addClass("active");
        $linkClass.siblings().removeClass("active");
        //调整搜索框的div大小
        if ($this.hasClass("chat-panel")) {
            $(".top").addClass("search-adjust");
            $(".to-do").addClass("active");
        } else {
            $(".top").removeClass("search-adjust");
            $(".to-do").removeClass("active");
        }
    })
}

//参考json[{
//     title: '江西'
//     ,id: 1
//     ,children: [{
//       title: '南昌'
//       ,id: 1000
//       ,children: [{
//         title: '青山湖区'
//         ,id: 10001
//       },{
//         title: '高新区'
//         ,id: 10002
//       }]
//     },{
//       title: '九江'
//       ,id: 1001
//     },{
//       title: '赣州'
//       ,id: 1002
//     }]
//   },{
//     title: '广西'
//     ,id: 2
//     ,children: [{
//       title: '南宁'
//       ,id: 2000
//     },{
//       title: '桂林'
//       ,id: 2001
//     }]
//   }]
function initFriendGroups(tree) {
    return getGroups().then(data => {
        tree.render({
            elem: '#userGroups'
            , data: data
            , showLine: false  //是否开启连接线
        });
        //不知道为啥arrow没了
        $(".layui-tree").find("i").each(function (i, e) {
            $(e).removeClass("layui-hide");
        });
        return data;
    });
}

function initDropdown(dropdown) {
    //右键菜单
    let inst = dropdown.render({
        elem: '#myGroups' //也可绑定到 document，从而重置整个右键
        , trigger: 'contextmenu' //contextmenu
        , isAllowSpread: false //禁止菜单组展开收缩
        , style: 'width: 200px' //定义宽度，默认自适应
        , id: 'test777' //定义唯一索引
        , data: [{
            title: '修改好友备注'
            , id: 'test'
        }, {
            title: '删除好友'
            , id: 'print'
        }, {type: '-'}, {
            title: '排序显示'
            , id: '#3'
            , child: [{
                title: '按名称排序'
                , id: '#1'
            }]
        }]
        , click: function (obj, othis) {
            if (obj.id === 'test') {
                layer.msg('click');
            } else if (obj.id === 'print') {
                window.print();
            } else if (obj.id === 'reload') {
                location.reload();
            }
        }
    });
}

function getGroups() {
    return fetch(ctx + "/user/groups")
        .then(res => res.json())
        .then(r => {
            if (r.code !== 200) {
                layer.alert();
                return null;
            }
            return r.data;
        })
}

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
        return i < 10 ? "0" + i : i;
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
        $active.append(`<div class="conversation-start"><span>${getDate(new Date())}</span></div>`);
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
            chatPage.append(`<div class="conversation-start"><span>${sendTime}</span></div>`);
        }
        chatPage.append(`<div class="bubble ${createUser === userId ? 'me' : 'you'}">${msg}</div>`)
    } else {
        $(".write").before(`<div class="chat" id="${createUser}" data-id="${createUser}">
                <div class="conversation-start"><span>${sendTime}</span></div>
                <div class="bubble ${createUser === userId ? 'me' : 'you'}">${msg}</div>
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
        let userInfo = `<li data-id="${ele.id}" data-name="${ele.account}" class="person" data-chat="person${index}">
                    <img src="img/${ele.avatar}" alt="" />
                    <span class="name">${ele.account}</span>
                    <span class="time">${ele.loginTime}</span>
                    <span class="notRead"></span>
                    <span class="preview">${ele.lastPush ?? ""}</span>
                </li>`;
        userTab.append(userInfo);
    })
}





