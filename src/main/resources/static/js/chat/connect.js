!(function (e) {
    let socket = e.socket, host = location.host,
        socketUrl = `ws://${host}/connect`;
    if (socket) {
        return;
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
            console.log("websocket发生了错误");
        }
    }
    e.socket = socket;
})(window);

