function uuid() {
    let s = [];
    let hexDigits = "0123456789abcdef";
    for (let i = 0; i < 32; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23];
    return s.join("");
}

//默认改变height
$.extend({
    changeFrameSize: function (layerIndex, options, callback) {
        let op = options;
        if (typeof options == 'string') {
            op = {height: options}
        }
        parent.layer.style(layerIndex, op);
        callback && callback();
    }
})

function errorHandler(e) {
    console.log(e);
    layer.alert(e.responseText);
}

function onEnter(callback) {
    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            callback && callback();
        }
    });
}

function fetcher(url, options) {
    let op = options ?? {};
    return fetch(url, op)
        .then(r => r.json())
        .catch(e => {
            console.log(e);
        });
}

