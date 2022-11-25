let socket, host = location.host;
function openSocket() {
    let socketUrl = `ws://${host}/connect`;
    if (socket != null) {
        socket.close();
        socket = null;
    }
    socket = new WebSocket(socketUrl);
    //打开事件
    socket.onopen = function () {
        //获得消息事件
        socket.onmessage = function (msg) {
            console.log(msg);
        };
        //发生了错误事件
        socket.onerror = function () {
            alert("websocket发生了错误");
        }
    }
}


function sendMessage() {
    $.ajax({
        url: "http://localhost:8449/push",
        type: "POST",
        data: {
            'message': $("#contentText").val(),
            'toUserId': $("#toUserId").val(),
            'userId': $("#userId").val()
        }
    })
}


function closeSocket() {
    let userId = $("#userId").val()
    $.ajax({
        type: "get",
        url: "http://localhost:8449/close?userId=" + userId,
        success: function (res) {
            alert('111');

        }
    });
    $("#userId").attr("disabled", false);
    $('#contain').html("下线成功！");
}
