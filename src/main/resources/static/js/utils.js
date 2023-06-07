var iconsConfig = [{
    name: "贴吧表情",
    path: "/emoji/dist/img/tieba/",
    maxNum: 50,
    file: ".jpg",
    placeholder: ":{alias}:",
}, {
    name: "秋秋表情",
    path: "/emoji/dist/img/qq/",
    maxNum: 91,
    excludeNums: [41, 45, 54],
    file: ".gif",
    placeholder: "#qq_{alias}#"
}], barAbbr = {
    1: "呵呵",
    2: "哈哈",
    3: "吐舌",
    4: "啊",
    5: "酷",
    6: "怒",
    7: "开心",
    8: "汗",
    9: "泪",
    10: "黑线",
    11: "鄙视",
    12: "不高兴",
    13: "真棒",
    14: "钱",
    15: "疑问",
    16: "阴脸",
    17: "吐",
    18: "咦",
    19: "委屈",
    20: "花心",
    21: "呼~",
    22: "笑脸",
    23: "冷",
    24: "太开心",
    25: "滑稽",
    26: "勉强",
    27: "狂汗",
    28: "乖",
    29: "睡觉",
    30: "惊哭",
    31: "生气",
    32: "惊讶",
    33: "喷",
    34: "爱心",
    35: "心碎",
    36: "玫瑰",
    37: "礼物",
    38: "彩虹",
    39: "星星月亮",
    40: "太阳",
    41: "钱币",
    42: "灯泡",
    43: "茶杯",
    44: "蛋糕",
    45: "音乐",
    46: "haha",
    47: "胜利",
    48: "大拇指",
    49: "弱",
    50: "OK"
}, qiuqiuAbbr = {1: "表情"};

function uuid() {
    var len = 32;//32长度
    var radix = 16;//16进制
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var uuid = [], i;
    radix = radix || chars.length;
    if (len) {
        for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix];
    } else {
        var r;
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
        uuid[14] = '4';
        for (i = 0; i < 36; i++) {
            if (!uuid[i]) {
                r = 0 | Math.random() * 16;
                uuid[i] = chars[(i === 19) ? (r & 0x3) | 0x8 : r];
            }
        }
    }
    return uuid.join('');
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

function imgUrlToBase64(url, callback, outputFormat) {
    let canvas = document.createElement('CANVAS'),
        ctx = canvas.getContext('2d'),
        img = new Image();
    img.crossOrigin = 'Anonymous'
    img.onload = function () {
        canvas.height = img.height
        canvas.width = img.width
        ctx.drawImage(img, 0, 0)
        let dataURL = canvas.toDataURL(outputFormat || 'image/png')
        //回调函数传递转换完成的base64编码
        callback.call(this, dataURL)
        canvas = null;
    }
    img.src = url;
}

function closeCurrFrame() {
    let index = parent.layer.getFrameIndex(window.name);
    top.layer.close(index);
}

function simplifyEmoji(emoji) {
    if (emoji?.startsWith(":")) {
        return "[" + barAbbr[emoji?.split(":")[1]] + "]";
    }
    if (emoji?.startsWith("#")) {
        //todo
        return "[" + qiuqiuAbbr[1] + "]";
    }
    return "[表情]";
}

function simplifyEmojiFromHtml(html) {
    let p1 = new RegExp('<img class="emoji_icon" src="/emoji/dist/img/tieba/(\\d+?).jpg">', 'gm'),
        p2 = new RegExp('<img class="emoji_icon" src="/emoji/dist/img/qq/(\\d+?).gif">', 'gm');
    return html?.replace(p1, function ($0, $1) {
        return "[" + barAbbr[$1] + "]";
    }).replace(p2, function ($0, $1) {
        return "[" + qiuqiuAbbr[1] + "]";
    });

}