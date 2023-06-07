$(function () {
    initCropper();
})

function initCropper() {
    layui.config({
        base: '/cropper/'
    }).use(['form', 'croppers'], renderCropper);
}

function renderCropper() {
    let $ = layui.jquery,
        croppers = layui.croppers;

    croppers.render({
        saveW: 300,
        saveH: 300,
        initSrc: initSrc,
        mark: 1,
        area: '500px',
        url: ctx + "/file/uploadAvatar",
        done: function (fileId) {
            $(window.parent.document).contents().find("iframe")[1].contentWindow.changeAvatar(fileId);
            $(window.parent.document).contents().find(".avatarUrl").attr("src", "/file/view/" + fileId);
            closeCurrFrame();
        }
    });
    let image = $(".showImgEdit .readyimg img"),
        preview = '.showImgEdit .img-preview',
        options = {aspectRatio: 1, preview: preview, viewMode: 1};
    image.cropper('destroy').attr('src', initSrc).cropper("");
}



