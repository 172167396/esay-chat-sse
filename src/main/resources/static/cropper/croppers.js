/*!
 * Cropper v3.0.0
 */

layui.config({
    base: '/cropper/' //layui自定义layui组件目录
}).define(['jquery', 'layer', 'cropper'], function (exports) {
    let $ = layui.jquery
        , layer = layui.layer;
    let html = "<div class=\"layui-fluid showImgEdit\">\n" +
        "    <div class=\"layui-form-item\">\n" +
        "        <div class=\"layui-input-inline layui-btn-container\" style=\"margin: 0 auto;\n" +
        "    text-align: center;\">\n" +
        "            <label for=\"cropper_avatarImgUpload\" class=\"layui-btn layui-btn-primary\">\n" +
        "                <i class=\"layui-icon\">&#xe67c;</i>选择图片\n" +
        "            </label>\n" +
        "            <input class=\"layui-upload-file\" id=\"cropper_avatarImgUpload\" type=\"file\" value=\"选择图片\" name=\"file\">\n" +
        "        </div>\n" +
        "        <div class=\"layui-form-mid layui-word-aux notice\">头像的尺寸限定150x150px,大小在50kb以内</div>\n" +
        "    </div>\n" +
        "    <div class=\"layui-row layui-col-space15\">\n" +
        "        <div class=\"layui-col-xs9\">\n" +
        "            <div class=\"readyimg\" style=\"height:300px;width:300px;background-color: rgb(247, 247, 247);\">\n" +
        "                <img src=\"#src\" >\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <div class=\"layui-col-xs3\">\n" +
        "            <div class=\"img-preview\">\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "    <div class=\"layui-row layui-col-space15 cropper-btn-div\">\n" +
        "        <div class=\"layui-col-xs9 cropper-func\">\n" +
        "            <div class=\"layui-row\">\n" +
        "                <div class=\"layui-col-xs6\">\n" +
        "                    <button type=\"button\" class=\"layui-btn lbtn layui-icon rotate-left\" cropper-event=\"rotate\" data-option=\"-15\" title=\"Rotate -90 degrees\"></button>\n" +
        "                    <button type=\"button\" class=\"layui-btn lbtn layui-icon rotate-right\" cropper-event=\"rotate\" data-option=\"15\" title=\"Rotate 90 degrees\"></button>\n" +
        // "                </div>\n" +
        // "                <div class=\"layui-col-xs5\" style=\"text-align: right;\">\n" +
        "                    <button type=\"button\" class=\"layui-btn lbtn move-to\" title=\"移动\" cropper-event=\"moveTo\" data-option=\"0\"></button>\n" +
        "                    <button type=\"button\" class=\"layui-btn lbtn enlarge\" title=\"放大图片\" cropper-event=\"zoom\" data-option=\"0.1\"></button>\n" +
        "                    <button type=\"button\" class=\"layui-btn lbtn shrink\" title=\"缩小图片\" cropper-event=\"zoom\" data-option=\"-0.1\"></button>\n" +
        "                    <button type=\"button\" class=\"layui-btn lbtn layui-icon reset\" cropper-event=\"reset\" title=\"重置图片\"></button>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <div class=\"bottomBtn\">\n" +
        "            <button class=\"layui-btn\" cropper-event=\"confirmSave\" type=\"button\">保存</button>\n" +
        "            <button class=\"layui-btn layui-btn-primary\" cropper-event=\"cancel\" type=\"button\">取消</button>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "\n" +
        "</div>";
    let obj = {
        render: function (e) {
            $('body').append(html.replace("#src", e.initSrc));
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
                            formData.append('file', blob, 'head.jpg');
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