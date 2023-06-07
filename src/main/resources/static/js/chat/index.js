$(function () {
    layui.use(['tree', 'dropdown', 'util'], function () {
        let layer = layui.layer
            , util = layui.util,
            dropdown = layui.dropdown;
        initRecentChat().then(() => {
            //绑定聊天列表点击事件
            bindChatClick();
        })

        //好友列表
        getFriendsAndRefreshGroup();
        //加好友/加群
        bindAddBtn();
        registerAddBtnClick(layer);
        bindSwitchBtnClick();
        //编辑个人信息
        editPersonalInfo();
    });

})


function triggerScreenshot() {
    let screenshot = new Screenshot({iframe: $("#rightFrame").contents().find("html")});
    screenshot.init();
}

function initRecentChat() {
    return FetchUtil.get(ctx + "/chat/recent")
        .then(r => {
            if (r.code !== 200) {
                layer.alert(r.msg);
                return null;
            }
            let $recentChat = $("#recent-chat");
            const $container = $recentChat.children('ul');
            $.each(r.data, function (i, e) {
                const $chatLine = $(`<li class="chat-line" id="${e?.targetId}">
                                <div class="${e?.type}">
                                    <div class="notice-msg relative">
                                        <div class="sys-notice ${e?.type === 'NOTICE' || '' ? 'show' : 'displayNone'}">
                                        </div>
                                        <div class="new-apply ${e?.type === 'NEW_FRIEND' ? 'show' : 'displayNone'}">
                                        </div>
                                        <div class="recent-chat ${e?.type === 'PERSONAL' ? 'show' : 'displayNone'}">
                                            <img class="chat-avatar" src="${e?.avatar ?? ''}">
                                        </div>
                                        <p class="nickName overflowEllips">${e?.name}</p>
                                        <p class="chatDate">${e?.chatDate ?? ""}</p>
                                        <span class="notRead hide"></span>
                                        <p class="short-desc overflowEllips" title="">${e.briefMsg ?? ""}</p>
                                    </div>
                                </div>
                            </li>`);
                $chatLine.data("id", e.targetId);
                $chatLine.data("type", e.type);
                let simplifyMsg = simplifyEmojiFromHtml($chatLine.find(".short-desc").html());
                $chatLine.find(".short-desc").html(simplifyMsg);
                $chatLine.find(".short-desc").attr("title", simplifyMsg);
                $container.append($chatLine);
            })
        })
}

/**
 * 最近聊天点击
 */
function bindChatClick() {
    $(document).on('click', ".chat-line", function () {
        let $this = $(this), $rightIframe = $("#rightFrame");
        let $unReadSpan = $this.find(".notRead"),
            nickName = $this.find(".nickName").text(),
            type = $this.data("type"),
            id = $this.attr("id");
        if (type === 'NEW_FRIEND') id = type;
        $unReadSpan.addClass("hide");
        $unReadSpan.text("");
        $(".chat-line").removeClass("focus");
        $this.addClass("focus");
        let iframeId = $rightIframe.attr("data-id");
        if (iframeId === id) {
            return;
        }
        $rightIframe.attr("data-id", id);
        $rightIframe.attr("src", ctx + "/chat/recent/" + id);
        setTimeout(function () {
            $rightIframe.contents().find(".sendTo").text(nickName)
        }, 500)
    })
}

function getFriendsAndRefreshGroup(groupId) {
    let $groupsUl = $("#groups");
    FetchUtil.get(ctx + `/user/friends?id=${groupId ? groupId : ''}`).then(res => {
        if (res?.data) {
            $.each(res.data, function (i, e) {
                let tmp = `<li class="layui-nav-item" id="${e.id}">
                            <a class="" href="javascript:;">${e.groupName}
                            <i class="expand layui-icon layui-icon-triangle-r layui-nav-more"></i>
                            </a>
                            <dl class="layui-nav-child ${!e.users ? 'emptyDl' : ''}">
                            ${e.users?.length > 0 ? e.users.map(item => {
                    return `<dd class="friend" data-id="${item.id}"><img class="avatarInGroup" src="${item.avatar}"><a class="friendName inline-block" href="javascript:;">${item.name}</a></dd>`;
                }).join('') : ''
                }</dl></li>`;
                const $li = $(tmp);
                if (e.users?.length < 1) {
                    $li.find('.layui-nav-child').addClass("emptyDl");
                }
                if (groupId) {
                    $groupsUl.find(`li#${groupId}`).replaceWith($li);
                } else {
                    $groupsUl.append($li);
                }
            })
        }
    }).then(() => {
        $groupsUl.append(`<span class="layui-nav-bar" style="top: 55px; height: 0; opacity: 0;"></span>`);
        refreshNav();
        bindUserClick();
    });
}

function refreshNav() {
    layui.use('element', function () {
        let element = layui.element,
            layFilter = $("#groups").attr('lay-filter');
        element.render('nav', layFilter);
    })
}

/**
 * 好友列表点击
 */
function bindUserClick() {
    $(document).on('dblclick', "dd.friend", function () {
        //添加到最近聊天里，然后置顶
        let id = $(this).data("id"),
            $chatLine = $(`.chat-line#${id}`),
            msgJson,
            $chatPanel = $(".chat-panel"),
            $rightIframe = $("#rightFrame");
        msgJson = {
            type: "PERSONAL",
            name: $(this).find(".friendName").text(),
            sender: id,
            senderAvatar: $(this).find(".avatarInGroup").attr("src"),
            createTime: dayjs().format("HH:mm"),
            content: "",
        };
        if ($chatLine?.length < 1) {
            //插入一条最近聊天
            insertChatLine(msgJson);
        } else {
            //置顶
            let tmp = $chatLine;
            $chatLine.remove();
            $recentChatUl.prepend(tmp);
        }
        $chatPanel.click();
        let iframeId = $rightIframe.attr("data-id");
        if (iframeId === id) {
            return;
        }
        $rightIframe.attr("data-id", id);
        $rightIframe.attr("src", ctx + "/chat/recent/" + id);
        setTimeout(function () {
            $rightIframe.contents().find(".sendTo").text(msgJson.name)
        }, 500)

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

function editPersonalInfo() {
    let $avatarUrl = $(".avatarUrl");
    $avatarUrl.click(function () {
        top.layer.open({
            type: 2,
            offset: '130px',
            skin: '',
            maxmin: false,
            id: 'layerDemo' + uuid(), //防止重复弹出,
            content: ctx + "/user/infoPage",
            btn: ['编辑资料'],
            btnAlign: 'c', //按钮居右
            shade: 0, //不显示遮罩
            title: " ",
            area: ['350px', '480px'],
            fixed: false,
            yes: function (index, layero) {

            },
            cancel: function (index) {
            }
        });
    })
}







