Array.isStrictEmpty = function (arr) {
    if (arr === null) {
        return true;
    }
    if (arr.length === 0) {
        return true;
    }
    return arr && arr.some(e => !e || $.trim(e) === '');
}

Array.prototype.toMap = function (field, mergeHandler) {
    let $this = $(this);
    let newArray = $this && Array.from($this);
    if(newArray == null) return;
    let map = new Map();
    newArray.forEach(o => {
        let key = o[field] || null;
        if (key == null) {
            return;
        }
        let prevObj = map.get(key);
        if (prevObj == null) {
            map.set(key, o);
            return;
        }
        let merged = o;
        if (mergeHandler != null) {
            merged = mergeHandler(prevObj, o);
        }
        map.set(key, merged);
    });
    return map;
};

class FetchApi {

    get(url) {
        return fetch(url, this.getOption("get"))
            .then(res => res.json())
            .then(json => {
                if (json?.code !== 200) {
                    top.layer.alert(json?.msg);
                    return null;
                }
                return json;
            })
            .catch(e => top.layer.alert(e.responseText));
    }

    post(url, data) {
        return fetch(url, this.getOption("post", data))
            .then(res => res.json())
            .then(json => {
                if (json?.code !== 200) {
                    top.layer.alert(json?.msg);
                    return null;
                }
                return json;
            })
            .catch(e => top.layer.alert(e.responseText));
    }

    getOption(method, body) {
        if (method === 'get') {
            return {
                method: 'GET',
                headers: {
                    "X-Requested-With": "XMLHttpRequest",
                    'Accept': 'application/json',
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }
        }
        return {
            method: 'POST',
            headers: {
                "X-Requested-With": "XMLHttpRequest",
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body ?? {})
        }
    }
}

window.FetchUtil = new FetchApi();

function checkEmpty(val, msg) {
    val = $.trim(val);
    if (!val) {
        layer.alert(msg + "不能为空");
        return false;
    }
    return true;
}