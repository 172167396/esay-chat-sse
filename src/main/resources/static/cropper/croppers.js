/*!
 * Cropper v3.0.0
 */

layui.config({
    base: '/cropper/' //layui自定义layui组件目录
}).define(['jquery', 'layer', 'cropper'], function (exports) {
    let $ = layui.jquery
        , layer = layui.layer;
    let obj = {
        render: function (e) {
            $(".showImgEdit").removeClass("hide")
            let self = this,
                elem = e.elem,
                saveW = e.saveW,
                saveH = e.saveH,
                mark = e.mark,
                area = e.area,
                url = e.url,
                done = e.done,
                imgChanged = false;

            let content = $('.showImgEdit'),
                image = $(".showImgEdit .readyimg img"),
                preview = '.showImgEdit .img-preview',
                file = $(".showImgEdit input[name='file']"),
                options = {aspectRatio: mark, preview: preview, viewMode: 1};

            file.change(function () {
                imgChanged = true;
                let r = new FileReader(),
                    f = this.files[0];
                r.readAsDataURL(f);
                r.onload = function (e) {
                    image.cropper('destroy').attr('src', this.result).cropper(options);
                };
            });

            $(".layui-btn").on('click', function () {
                let event = $(this).attr("cropper-event");
                switch (event) {
                    case "confirmSave": {
                        if (!imgChanged) {
                            closeCurrFrame();
                            return;
                        }
                        let canvas = image.cropper("getCroppedCanvas", {
                            width: saveW,
                            height: saveH
                        });
                        if (!canvas) {
                            top.layer.alert("请选择图片");
                            return;
                        }
                        canvas.toBlob(function (blob) {
                            let formData = new FormData();
                            formData.append('file', blob, 'avatar.jpg');
                            $.ajax({
                                method: "post",
                                url: url, //用于文件上传的服务器端请求地址
                                data: formData,
                                processData: false,
                                contentType: false,
                                success: function (result) {
                                    if (result.code === 200) {
                                        top.layer.msg(result.msg, {icon: 1});
                                        return done(result.data);
                                    } else {
                                        top.layer.alert(result.msg, {icon: 2});
                                    }
                                }
                            });
                        });
                        break;
                    }
                    case "moveTo":
                    case "rotate":
                    case "reset":
                    case "zoom": {
                        let option = $(this).data('option');
                        image.cropper(event, option ?? {});
                        break;
                    }
                    case "cancel": {
                        closeCurrFrame();
                        break;
                    }
                }
            });
        }
    };
    exports('croppers', obj);
});