(function (global) {
    let Screenshot = function (options) {
        if (options) {
            this.options = options;
        }
    }

    Screenshot.prototype = {
        init: function () {
            this.registryComboKey();
            this.render();
        },
        render: function () {
            let that = this, $document = $(global.document),
                option = {
                    target: $document.find("body"),
                    canvasOption: {
                        width: $document.width(),
                        height: $document.height(),
                        allowTaint: true,
                        logging: true,
                        profile: true,
                        useCORS: true
                    },
                    callback: function (canvas) {
                        return canvas;
                    }
                };
            let initDoneBtnClick = that.initDoneBtnClick.bind(that);
            //1. require clipboard permission
            this.requirePermission();
            //2. init selection
            this.initSelection();
            //3. get a screenshot of whole page for the cover and monitor the change of selection,
            // then get selection's width and height,do screenshot by specific area again
            let iframeOption = {
                target: that.options.iframe,
                callback: function (canvas) {
                    let $canvas = $(canvas), offset = $("#rightFrame").offset()
                    $canvas.css({
                        "position": "absolute",
                        "left": offset.left,
                        "top": offset.top,
                        "z-index": 8
                    });
                    $canvas.addClass("iframeScreenshot");
                    $("body").append($canvas);
                    return canvas;
                }
            };
            this.screenshot(iframeOption).then(canvas => this.screenshot(option).then(canvas => {
                $(".iframeScreenshot").remove();
                that.renderSelect(that.$selection, canvas, function (options) {
                    that.screenshot({
                        target: $document.find("body"),
                        canvasOption: options,
                        callback: initDoneBtnClick
                    })
                })
            }))
        },
        initSelection: function () {
            let $selection = $(`<div id="selection"></div>`)
            $selection.css({
                "border": "1px solid lightgray",
                "z-index": 99,
                "position": "absolute",
                "background": "transparent",
                "overflow": "hidden"
            });
            $(global.document).find("body").append($selection);
            this.$selection = $selection;
        },
        resetSelection: function () {
            this.$selection.css({
                left: 0,
                top: 0,
                width: 0,
                height: 0
            });
        },
        initShadow: function () {
            let $document = $(global.document), width = $document.width() + "px", height = $document.height() + "px";
            const $shadow = $(`<div class="shadow"></div>`);
            $shadow.css({
                "position": "absolute",
                "width": `${width}`,
                "height": `${height}`,
                "background-color": "#000",
                "opacity": 0.5,
                "left": 0,
                "top": 0
            })
            $document.find("body").append($shadow);
            this.$shadow = $shadow;
        },
        initDoneBtnClick: function (canvas) {
            let that = this;
            //exitClip
            this.$doneButton.find("button:eq(0)").click(function () {
                that.hideDoneBtn();
                that.closeShadow();
            })
            //finishClip
            this.$doneButton.find("button:eq(1)").click(function () {
                that.copyPicture(canvas).then(() => {
                    that.closeShadow();
                });
            })
        },
        copyPicture: async function (canvas) {
            try {
                // const response = await fetch('./logo.png');
                // const blob = await response.blob();
                await canvas.toBlob(blob => {
                    navigator.clipboard.write([
                        new ClipboardItem({
                            [blob.type]: blob
                        })
                    ]);
                })
                console.log('Image copied.');
            } catch (err) {
                console.error(err.name, err.message);
            }
        },
        closeShadow: function () {
            this.$shadow.remove();
            this.resetSelection();
            this.hideDoneBtn();
        },
        renderSelect: function ($selection, canvas, callback) {
            let that = this, start = {}, end = {}, isSelecting = false, $canvas = $(canvas);
            // Listen for selection
            $(global).on('mousedown', function ($event) {
                // Update our state
                isSelecting = true;
                that.resetSelection();
                $selection.removeClass('complete');
                start.x = $event.pageX;
                start.y = $event.pageY;

                // Add selection to screen
                $selection.css({
                    left: start.x,
                    top: start.y
                });
                that.initShadow();
                // coverImg.attr("src", coverUrl);
                $selection.append($canvas);
            })
                // Listen for movement
                .on('mousemove', function ($event) {
                    // Ignore if we're not selecing
                    if (!isSelecting) {
                        return;
                    }
                    // Update our state
                    end.x = $event.pageX;
                    end.y = $event.pageY;

                    // Move & resize selection to reflect mouse position
                    $selection.css({
                        left: start.x < end.x ? start.x : end.x,
                        top: start.y < end.y ? start.y : end.y,
                        width: Math.abs(start.x - end.x),
                        height: Math.abs(start.y - end.y)
                    });
                    let translate = {
                        translateX: start.x > end.x ? end.x : start.x,
                        translateY: start.y > end.y ? end.y : start.y
                    };
                    $canvas.css("transform", `translateX(-${translate.translateX + 1}px) translateY(-${translate.translateY + 2}px)`);
                })
                // listen for end
                .on('mouseup', function ($event) {
                    // Update our state
                    isSelecting = false;
                    $selection.addClass('complete');

                    let width = Math.abs(start.x - (end.x ?? start.x)),
                        height = Math.abs(start.y - (end.y ?? start.y));

                    if (width >= 5 && height >= 5) {
                        //show done btn
                        that.showDoneButton(start, end);
                        callback && callback({
                            x: start.x,
                            y: start.y,
                            width: Math.abs(start.x - end.x),
                            height: Math.abs(start.y - end.y),
                            allowTaint: true,
                            logging: true,
                            profile: true,
                            useCORS: true
                        });
                    } else {
                        that.closeShadow();
                        // resetSelection();
                    }
                    //clear
                    start = end = {};
                    $(global).off("mousedown").off("mousemove").off("mouseup");
                    // $(area).css({
                    //     left: 0,
                    //     top: 0,
                    //     width: 0,
                    //     height: 0
                    // });
                });
        },
        requirePermission: function () {
            const permissionName = "clipboard-write", permissionRead = "clipboard-read";
            navigator.permissions.query({name: permissionName}).then((result) => {
                if (result.state === "granted" || result.state === "prompt") {
                    console.log("Write access granted!");
                }
            });
            navigator.permissions.query({name: permissionRead}).then((result) => {
                if (result.state === "granted" || result.state === "prompt") {
                    console.log("Read access granted!");
                }
            });
        },
        registryComboKey: function () {
            let that = this;
            $(global.document).keydown(function (e) {
                let keyCode = e.code || e.keyCode || e.which || e.charCode, ctrl = e.ctrlKey;
                if (ctrl && keyCode === 65) {
                    that.render();
                }
                e.preventDefault();
                return false;
            })
        },
        screenshot: function screenshot(option) {
            let $body = option.target;
            return html2canvas($body[0], option.canvasOptions).then(canvas => {
                return option.callback && option.callback(canvas);
            });
        },
        showDoneButton: function (start, end) {
            let $doneBtn = $(`<div class="screenshotBtn"><button class="exitClip">退出</button><button class="finishClip">完成</button></div>`),
                endX = end.x ?? 0,
                endY = end.y ?? 0,
                translateX = start.x > end.x ? endX : start.x,
                translateY = start.y > end.y ? start.y : endY,
                width = Math.abs(start.x - endX),
                css = {
                    "transform": `translateX(${translateX}px) translateY(${translateY + 3}px)`,
                    "width": width,
                    "visibility": "visible",
                    "display": "flex",
                    "justify-content": "flex-end",
                    "position": "absolute",
                    "z-index": 99,
                    "left": 0,
                    "top": 0
                }
            $doneBtn.css(css);
            this.$selection.after($doneBtn);
            this.$doneButton = $doneBtn;
        },
        hideDoneBtn: function () {
            this.$doneButton.css("visibility", "hidden");
        }


    }
    global.Screenshot = Screenshot;
}(window));