layui.define(function(exports) {
	var dom = $(document), win = $(window);
	var MOD_NAME = 'form', ELEM = '.layui-form', THIS = 'layui-this', SHOW = 'layui-show', HIDE = 'layui-hide', DISABLED = 'layui-disabled';
	
	//查找指定的元素在数组中的位置
    Array.prototype.indexOfElem = function(val){
        for (var i = 0; i < this.length; i++) {
            if (this[i] == val) return i;
        }
        return -1;
    }
	//得到元素的索引
    Array.prototype.removeElem = function(val) {
        var index = this.indexOfElem(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };

	var hidePlaceholder = function(title,select){
        //判断是否全部删除，如果全部删除则展示提示信息
        if(title.find(".multiSelect a").length != 0){
            title.children("input.layui-input").attr("placeholder","");
        }else{
            title.children("input.layui-input").attr("placeholder",select.find("option:eq(0)").text());
        }
    }
	var checkbox=function(elem){//复选框/开关
        var CLASS = {
            checkbox: ['layui-form-checkbox', 'layui-form-checked', 'checkbox']
            ,_switch: ['layui-form-switch', 'layui-form-onswitch', 'switch']
        }
            ,checks = elem.find('input[type=checkbox]')

            ,events = function(reElem, RE_CLASS){
            	var check = $(this);
            //勾选
            reElem.on('click', function(){
                var filter = check.attr('lay-filter') //获取过滤器
                    ,text = (check.attr('lay-text')||'').split('|');

                if(check[0].disabled) return;

                check[0].checked ? (
                    check[0].checked = false
                        ,reElem.removeClass(RE_CLASS[1]).find('em').text(text[1])
                ) : (
                    check[0].checked = true
                        ,reElem.addClass(RE_CLASS[1]).find('em').text(text[0])
                );

                layui.event.call(check[0], MOD_NAME, RE_CLASS[2]+'('+ filter +')', {
                    elem: check[0]
                    ,value: check[0].value
                    ,othis: reElem
                });
            });
        }

        checks.each(function(index, check){
            var othis = $(this), skin = othis.attr('lay-skin')
                ,text = (othis.attr('lay-text')||'').split('|'), disabled = this.disabled;
            if(skin === 'switch') skin = '_'+skin;
            var RE_CLASS = CLASS[skin] || CLASS.checkbox;

            if(typeof othis.attr('lay-ignore') === 'string') return othis.show();

            //替代元素
            var hasRender = othis.next('.' + RE_CLASS[0]);
            var reElem = $(['<div class="layui-unselect '+ RE_CLASS[0] + (
                check.checked ? (' '+RE_CLASS[1]) : '') + (disabled ? ' layui-checkbox-disbaled '+DISABLED : '') +'" lay-skin="'+ (skin||'') +'">'
                ,{
                    _switch: '<em>'+ ((check.checked ? text[0] : text[1])||'') +'</em><i></i>'
                }[skin] || ((check.title.replace(/\s/g, '') ? ('<span>'+ check.title +'</span>') : '') +'<i class="layui-icon">'+ (skin ? '&#xe605;' : '&#xe618;') +'</i>')
                ,'</div>'].join(''));

            hasRender[0] && hasRender.remove(); //如果已经渲染，则Rerender
            othis.after(reElem);
            events.call(this, reElem, RE_CLASS);
        });
    }
    
	var mod = function(elem){
        var TIPS = '请选择', CLASS = 'layui-form-select', TITLE = 'layui-select-title'
            ,NONE = 'layui-select-none', initValue = '', thatInput

            ,selects = $(elem), hide = function(e, clear){
            if(!$(e.target).parent().hasClass(TITLE) || clear){
                $('.'+CLASS).removeClass(CLASS+'ed ' + CLASS+'up');
                thatInput && initValue && thatInput.val(initValue);
            }
            thatInput = null;
        }

        ,events = function(reElem, disabled, isSearch){
            var select = $(this)
                ,title = reElem.find('.' + TITLE)
                ,input = title.find('input')
                ,multiSelect = title.find(".multiSelect")
                ,dl = reElem.find('dl')
                ,dds = dl.children('dd')
            hidePlaceholder(title,select);
            if(disabled) return;

            //展开下拉
            var showDown = function(){
                var top = reElem.offset().top + reElem.outerHeight() + 5 - win.scrollTop()
                    ,dlHeight = dl.outerHeight();
                $("body").find("."+CLASS).removeClass(CLASS+'ed');
                reElem.addClass(CLASS+'ed');
                dds.removeClass(HIDE);

                //上下定位识别
                if(top + dlHeight > win.height() && top >= dlHeight){
                    reElem.addClass(CLASS + 'up');
                }
            }, hideDown = function(choose){
                reElem.removeClass(CLASS+'ed ' + CLASS+'up');
                input.blur();

                if(choose) return;

                notOption(input.val(), function(none){
                    if(none){
                        initValue = dl.find('.'+THIS).html();
                        input && input.val(initValue);
                    }
                });
            };

            //点击标题区域
            title.on('click', function(e){
                if(typeof select.attr('multiple') && typeof select.attr('multiple') === 'string') {
                    if(!reElem.hasClass(CLASS + 'ed')){
                        showDown();
                    }
                }else{
                    reElem.hasClass(CLASS + 'ed') ? (
                        hideDown()
                    ) : (
                        hide(e, true),
                            showDown()
                    );
                    dl.find('.' + NONE).remove();
                }
                e.stopPropagation();
                //选择完毕关闭下拉
                $(document).not(title.find(".layui-anim")).off('click', hide).on('click', hide);
            });

            //点击箭头获取焦点
            title.find('.layui-edge').on('click', function(){
                input.focus();
            });

            //键盘事件
            input.on('keyup', function(e){
                var keyCode = e.keyCode;
                //Tab键
                if(keyCode === 9){
                    showDown();
                }
            }).on('keydown', function(e){
                var keyCode = e.keyCode;
                //Tab键
                if(keyCode === 9){
                    hideDown();
                } else if(keyCode === 13){ //回车键
                    e.preventDefault();
                }
            });

            //检测值是否不属于select项
            var notOption = function(value, callback, origin){
                var num = 0;
                layui.each(dds, function(){
                    var othis = $(this)
                        ,text = othis.text()
                        ,not = text.indexOf(value) === -1;
                    if(value === '' || (origin === 'blur') ? value !== text : not) num++;
                    origin === 'keyup' && othis[not ? 'addClass' : 'removeClass'](HIDE);
                });
                var none = num === dds.length;
                return callback(none), none;
            };

            //搜索匹配
            var search = function(e){
                var value = this.value, keyCode = e.keyCode;

                if(keyCode === 9 || keyCode === 13
                    || keyCode === 37 || keyCode === 38
                    || keyCode === 39 || keyCode === 40
                ){
                    return false;
                }

                notOption(value, function(none){
                    if(none){
                        dl.find('.'+NONE)[0] || dl.append('<p class="'+ NONE +'">无匹配项</p>');
                    } else {
                        dl.find('.'+NONE).remove();
                    }
                }, 'keyup');

                if(value === ''){
                    dl.find('.'+NONE).remove();
                }
            };
            if(isSearch){
                input.on('keyup', search).on('blur', function(e){
                    thatInput = input;
                    initValue = dl.find('.'+THIS).html();
                    setTimeout(function(){
                        notOption(input.val(), function(none){
                            if(none && !initValue){
                                input.val('');
                            }
                        }, 'blur');
                    }, 200);
                });
            }

            //多选删除
            title.delegate(".multiSelect a i","click",function(e){
                var valueStr = select.val() || [];
                var _this = $(this);
                e.stopPropagation();
                title.find("dd").each(function(){
                    if($(this).find("span").text() == _this.siblings("span").text()){
                        var othis = $(this);
                        $(this).find("input").prop('checked',false);
                        $(this).find(".layui-form-checkbox").removeClass("layui-form-checked");
                        valueStr.removeElem($(this).attr("lay-value"));
                        select.val(valueStr);
                        layui.event.call(this, MOD_NAME, 'select('+ select.attr('lay-filter') +')', {
                            elem: select[0]
                            ,value: valueStr
                            ,othis: reElem
                        });
                    }
                })
                $(this).parent("a").remove();
                hidePlaceholder(title,select);
            })

            //选择
            dds.on('click', function(event){
                var othis = $(this), value = othis.attr('lay-value'),valueStr = select.val() || [];
                var filter = select.attr('lay-filter'); //获取过滤器
                if(typeof select.attr('multiple') && typeof select.attr('multiple') === 'string'){
                    if(othis.hasClass(DISABLED)) return false;
                    if(othis.find("input[type='checkbox']").is(':checked')){
                        multiSelect.html(multiSelect.html() + "<a href='javascript:;'><span>"+othis.find("span").text()+"</span><i></i></a>");
                        valueStr.push(value);
                    }else{
                        multiSelect.find("a").each(function(){
                            if($(this).find("span").text() == othis.find("span").text()){
                                $(this).remove();
                                valueStr.removeElem(value);
                            }
                        })
                    }
                    select.val(valueStr).removeClass('layui-form-danger');
                    layui.event.call(this, MOD_NAME, 'select('+ filter +')', {
                        elem: select[0]
                        ,valueStr: valueStr
                        ,othis: reElem
                    });
                    hidePlaceholder(title,select);
                }else{
                    if(othis.hasClass(DISABLED)) return false;
                    if(othis.hasClass('layui-select-tips')){
                        input.val('');
                    } else {
                        input.val(othis.text());
                        othis.addClass(THIS);
                    }
                    othis.siblings().removeClass(THIS);
                    select.val(value).removeClass('layui-form-danger');
                    layui.event.call(this, MOD_NAME, 'select('+ filter +')', {
                        elem: select[0]
                        ,value: value
                        ,othis: reElem
                    });
                    hideDown(true);
                    return false;
                    reElem.find('dl>dt').on('click', function(e){
                        return false;
                    });
                }
            });
        }

        selects.each(function(index, select){
            var othis = $(this)
                ,hasRender = othis.next('.'+CLASS)
                ,disabled = this.disabled
                ,selected = $(select.options[select.selectedIndex]) //获取当前选中项
                ,optionsFirst = select.options[0];
            if(typeof othis.attr('multiple') && typeof othis.attr('multiple') === 'string'){
                var value = $(select).val()
            }else{
                var value = select.value
            }

            if(typeof othis.attr('lay-ignore') === 'string') return othis.show();

            var isSearch = typeof othis.attr('lay-search') === 'string'
                ,isMultiple = typeof othis.attr('multiple') === 'string'
                ,placeholder = optionsFirst ? (
                optionsFirst.value ? TIPS : (optionsFirst.innerHTML || TIPS)
            ) : TIPS;
                
            //替代元素
            if(typeof othis.attr('multiple') && typeof othis.attr('multiple') === 'string') {
                var reElem = $(['<div class="' + (isMultiple ? '' : 'layui-unselect ') + CLASS + (disabled ? ' layui-select-disabled' : '') + '">'
                    , '<div class="' + TITLE + '"><input type="text" class="layui-input" placeholder="' + placeholder + '"><div class="layui-input multiSelect" >' + function(){
                        var aLists = [];
                        if(value != null && value != undefined && value.length != 0){
                            for(var aList = 0;aList<value.length;aList++){
                                if(value[aList]){
                                    aLists.push("<a href='javascript:;'><span>"+select[value[aList]].text+"</span><i></i></a>")
                                }
                            }
                        }
                        return aLists.join('');
                    }(othis.find('*')) + '<i class="layui-edge"></i></div>'
                    , '<dl class="layui-anim layui-anim-upbit' + (othis.find('optgroup')[0] ? ' layui-select-group' : '') + '">' + function (options) {
                        var arr = [];
                        layui.each(options, function (index, item) {
                            if (index === 0 && !item.value) {
                                arr.push('<dd lay-value="" class="layui-select-tips">' + (item.innerHTML || TIPS) + '</dd>');
                            }else {
                                if(value != null && value != undefined && value.length != 0) {
                                    for (var checkedVal = 0; checkedVal < value.length; checkedVal++) {
                                        if (value[checkedVal] == item.value) {
                                            arr.push('<dd lay-value="' + item.value + '">' + '<input type="checkbox" ' + (item.disabled ? "disabled" : "") + ' checked lay-filter="searchChecked" title="' + item.innerHTML + '" lay-skin="primary"></dd>');
                                            return false;
                                        }
                                    }
                                }
                                arr.push('<dd lay-value="' + item.value + '">' + '<input type="checkbox" ' + (item.disabled ? "disabled" : "") + ' lay-filter="searchChecked" title="' + item.innerHTML + '" lay-skin="primary"></dd>');
                            }
                        });
                        arr.length === 0 && arr.push('<dd lay-value="" class="' + DISABLED + '">没有选项</dd>');
                        return arr.join('');
                    }(othis.find('*')) + '</dl>'
                    , '</div>'].join(''));

                hasRender[0] && hasRender.remove(); //如果已经渲染，则Rerender
                checkbox(reElem);
                othis.after(reElem);
                events.call(this, reElem, disabled, isMultiple);
            }else{
                var reElem = $(['<div class="' + (isSearch ? '' : 'layui-unselect ') + CLASS + (disabled ? ' layui-select-disabled' : '') + '">'
                    , '<div class="' + TITLE + '"><input type="text" placeholder="' + placeholder + '" value="' + (value ? selected.html() : '') + '" ' + (isSearch ? '' : 'readonly') + ' class="layui-input' + (isSearch ? '' : ' layui-unselect') + (disabled ? (' ' + DISABLED) : '') + '">'
                    , '<i class="layui-edge"></i></div>'
                    , '<dl class="layui-anim layui-anim-upbit' + (othis.find('optgroup')[0] ? ' layui-select-group' : '') + '">' + function (options) {
                        var arr = [];
                        layui.each(options, function (index, item) {
                            if (index === 0 && !item.value) {
                                arr.push('<dd lay-value="" class="layui-select-tips">' + (item.innerHTML || TIPS) + '</dd>');
                            } else if (item.tagName.toLowerCase() === 'optgroup') {
                                arr.push('<dt>' + item.label + '</dt>');
                            } else {
                                arr.push('<dd lay-value="' + item.value + '" class="' + (value === item.value ? THIS : '') + (item.disabled ? (' ' + DISABLED) : '') + '">' + item.innerHTML + '</dd>');
                            }
                        });
                        arr.length === 0 && arr.push('<dd lay-value="" class="' + DISABLED + '">没有选项</dd>');
                        return arr.join('');
                    }(othis.find('*')) + '</dl>'
                    , '</div>'].join(''));

                hasRender[0] && hasRender.remove(); //如果已经渲染，则Rerender
                checkbox(reElem);
                othis.after(reElem);
                events.call(this, reElem, disabled, isSearch);
            }
        });
    }
	exports('ysMultiSelect', mod);
	layui.link(layui.cache.base + 'css/ysMultiSelect.css', null, 'ysMultiSelect');
});