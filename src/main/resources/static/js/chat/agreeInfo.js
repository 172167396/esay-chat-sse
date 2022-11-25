$(function () {
    let $select = $(".group-select"), groupList = FetchUtil.get(ctx + "/user/group-list");
    return groupList.then(data => {
        if (data?.data) {
            $.each(data.data, function (i, e) {
                const $option = $(`<option value="${e.id}">${e.name}</option>`);
                $select.append($option);
            })
        }
    });
})