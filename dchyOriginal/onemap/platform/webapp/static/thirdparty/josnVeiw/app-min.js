function QueryParams() {
}
function Notify() {
    this.dom = {};
    var e = this;
    jsoneditor.util.addEventListener(document, "keydown", function (t) {
        e.onKeyDown(t)
    })
}
function Splitter(e) {
    if (!e || !e.container)throw new Error("params.container undefined in Splitter constructor");
    var t = this;
    jsoneditor.util.addEventListener(e.container, "mousedown", function (e) {
        t.onMouseDown(e)
    }), this.container = e.container, this.snap = Number(e.snap) || 200, this.width = void 0, this.value = void 0, this.onChange = e.change ? e.change : function () {
    }, this.params = {}
}
QueryParams.prototype.getQuery = function () {
    for (var e = window.location.search.substring(1), t = e.split("&"), o = {}, i = 0, r = t.length; r > i; i++) {
        var n = t[i].split("=");
        if (2 == n.length) {
            var a = decodeURIComponent(n[0]), l = decodeURIComponent(n[1]);
            o[a] = l
        }
    }
    return o
}, QueryParams.prototype.setQuery = function (e) {
    var t = "";
    for (var o in e)if (e.hasOwnProperty(o)) {
        var i = e[o];
        void 0 != i && (t.length && (t += "&"), t += encodeURIComponent(o), t += "=", t += encodeURIComponent(e[o]))
    }
    window.location.search = t.length ? "#" + t : ""
}, QueryParams.prototype.getValue = function (e) {
    var t = this.getQuery();
    return t[e]
}, QueryParams.prototype.setValue = function (e, t) {
    var o = this.getQuery();
    o[e] = t, this.setQuery(o)
};
var ajax = function () {
    function e(e, t, o, i, r) {
        try {
            var n = new XMLHttpRequest;
            if (n.onreadystatechange = function () {
                    4 == n.readyState && r(n.responseText, n.status)
                }, n.open(e, t, !0), i)for (var a in i)i.hasOwnProperty(a) && n.setRequestHeader(a, i[a]);
            n.send(o)
        } catch (l) {
            r(l, 0)
        }
    }

    function t(t, o, i) {
        e("GET", t, null, o, i)
    }

    function o(t, o, i, r) {
        e("POST", t, o, i, r)
    }

    return {fetch: e, get: t, post: o}
}(), FileRetriever = function (e) {
    e = e || {}, this.options = {
        maxSize: void 0 != e.maxSize ? e.maxSize : 1048576,
        html5: void 0 != e.html5 ? e.html5 : !0
    }, this.timeout = Number(e.timeout) || 3e4, this.headers = {Accept: "application/json"}, this.scriptUrl = e.scriptUrl || "fileretriever.php", this.notify = e.notify || void 0, this.defaultFilename = "document.json", this.dom = {}
};
FileRetriever.prototype._hide = function (e) {
    e.style.visibility = "hidden", e.style.position = "absolute", e.style.left = "-1000px", e.style.top = "-1000px", e.style.width = "0", e.style.height = "0"
}, FileRetriever.prototype.remove = function () {
    var e = this.dom;
    for (var t in e)if (e.hasOwnProperty(t)) {
        var o = e[t];
        o.parentNode && o.parentNode.removeChild(o)
    }
    this.dom = {}
}, FileRetriever.prototype._getFilename = function (e) {
    return e ? e.replace(/^.*[\\\/]/, "") : ""
}, FileRetriever.prototype.setUrl = function (e) {
    this.url = e
}, FileRetriever.prototype.getFilename = function () {
    return this.defaultFilename
}, FileRetriever.prototype.getUrl = function () {
    return this.url
}, FileRetriever.prototype.loadUrl = function (e, t) {
    this.setUrl(e);
    var o = void 0;
    this.notify && (o = this.notify.showNotification("loading url..."));
    var i = this, r = function (e, r) {
        t && (t(e, r), t = void 0), i.notify && o && (i.notify.removeMessage(o), o = void 0)
    }, n = this.scriptUrl;
    ajax.get(e, i.headers, function (t, o) {
        if (200 == o)r(null, t); else {
            var a, l = n + "?url=" + encodeURIComponent(e);
            ajax.get(l, i.headers, function (t, o) {
                200 == o ? r(null, t) : 404 == o ? (console.log('Error: url "' + e + '" not found', o, t), a = new Error('Error: url "' + e + '" not found'), r(a, null)) : (console.log('Error: failed to load url "' + e + '"', o, t), a = new Error('Error: failed to load url "' + e + '"'), r(a, null))
            })
        }
    }), setTimeout(function () {
        r(new Error("Error loading url (time out)"))
    }, this.timeout)
}, FileRetriever.prototype.loadFile = function (e) {
    var t = void 0, o = this, i = function () {
        o.notify && !t && (t = o.notify.showNotification("loading file...")), setTimeout(function () {
            r(new Error("Error loading url (time out)"))
        }, o.timeout)
    }, r = function (i, r) {
        e && (e(i, r), e = void 0), o.notify && t && (o.notify.removeMessage(t), t = void 0)
    }, n = o.options.html5 && window.File && window.FileReader;
    if (n)this.prompt({
        title: "Open file",
        titleSubmit: "Open",
        description: "Select a file on your computer.",
        inputType: "file",
        inputName: "file",
        callback: function (e, t) {
            if (e) {
                if (n) {
                    var o = t.files[0], a = new FileReader;
                    a.onload = function (e) {
                        var t = e.target.result;
                        r(null, t)
                    }, a.readAsText(o)
                }
                i()
            }
        }
    }); else {
        var a = "fileretriever-upload-" + Math.round(1e15 * Math.random()), l = document.createElement("iframe");
        l.name = a, o._hide(l), l.onload = function () {
            var e = l.contentWindow.document.body.innerHTML;
            if (e) {
                var t = o.scriptUrl + "?id=" + e + "&filename=" + o.getFilename();
                ajax.get(t, o.headers, function (e, t) {
                    if (200 == t)r(null, e); else {
                        var i = new Error("Error loading file " + o.getFilename());
                        r(i, null)
                    }
                    l.parentNode === document.body && document.body.removeChild(l)
                })
            }
        }, document.body.appendChild(l), this.prompt({
            title: "Open file",
            titleSubmit: "Open",
            description: "Select a file on your computer.",
            inputType: "file",
            inputName: "file",
            formAction: this.scriptUrl,
            formMethod: "POST",
            formTarget: a,
            callback: function (e) {
                e && i()
            }
        })
    }
}, FileRetriever.prototype.loadUrlDialog = function (e) {
    var t = this;
    this.prompt({
        title: "Open url",
        titleSubmit: "Open",
        description: "Enter a public url. Urls which need authentication or are located on an intranet cannot be loaded.",
        inputType: "text",
        inputName: "url",
        inputDefault: this.getUrl(),
        callback: function (o) {
            o ? t.loadUrl(o, e) : e()
        }
    })
}, FileRetriever.prototype.prompt = function (e) {
    var t = function () {
        h.parentNode && h.parentNode.removeChild(h), r.parentNode && r.parentNode.removeChild(r), jsoneditor.util.removeEventListener(document, "keydown", i)
    }, o = function () {
        t(), e.callback && e.callback(null)
    }, i = jsoneditor.util.addEventListener(document, "keydown", function (e) {
        var t = e.which;
        27 == t && (o(), e.preventDefault(), e.stopPropagation())
    }), r = document.createElement("div");
    r.className = "fileretriever-overlay", document.body.appendChild(r);
    var n = document.createElement("form");
    n.className = "fileretriever-form", n.target = e.formTarget || "", n.action = e.formAction || "", n.method = e.formMethod || "POST", n.enctype = "multipart/form-data", n.encoding = "multipart/form-data", n.onsubmit = function () {
        return s.value ? (setTimeout(function () {
            t()
        }, 0), e.callback && e.callback(s.value, s), void 0 != e.formAction && void 0 != e.formMethod) : (alert("Enter a " + e.inputName + " first..."), !1)
    };
    var a = document.createElement("div");
    if (a.className = "fileretriever-title", a.appendChild(document.createTextNode(e.title || "Dialog")), n.appendChild(a), e.description) {
        var l = document.createElement("div");
        l.className = "fileretriever-description", l.appendChild(document.createTextNode(e.description)), n.appendChild(l)
    }
    var s = document.createElement("input");
    s.className = "fileretriever-field", s.type = e.inputType || "text", s.name = e.inputName || "text", s.value = e.inputDefault || "";
    var d = document.createElement("div");
    d.className = "fileretriever-contents", d.appendChild(s), n.appendChild(d);
    var p = document.createElement("input");
    p.className = "fileretriever-cancel", p.type = "button", p.value = e.titleCancel || "Cancel", p.onclick = o;
    var c = document.createElement("input");
    c.className = "fileretriever-submit", c.type = "submit", c.value = e.titleSubmit || "Ok";
    var u = document.createElement("div");
    u.className = "fileretriever-buttons", u.appendChild(p), u.appendChild(c), n.appendChild(u);
    var m = document.createElement("div");
    m.className = "fileretriever-border", m.appendChild(n);
    var h = document.createElement("div");
    h.className = "fileretriever-background", h.appendChild(m), h.onclick = function (e) {
        var t = e.target;
        t == h && o()
    }, document.body.appendChild(h), s.focus(), s.select()
}, FileRetriever.prototype.saveFile = function (e, t) {
    var o = void 0;
    this.notify && (o = this.notify.showNotification("saving file..."));
    var i = this, r = function (e) {
        t && (t(e), t = void 0), i.notify && o && (i.notify.removeMessage(o), o = void 0)
    }, n = document.createElement("a");
    this.options.html5 && void 0 != n.download && !util.isFirefox() ? (n.style.display = "none", n.href = "data:application/json;charset=utf-8," + encodeURIComponent(e), n.download = this.getFilename(), document.body.appendChild(n), n.click(), document.body.removeChild(n), r()) : e.length < this.options.maxSize ? ajax.post(i.scriptUrl, e, i.headers, function (e, t) {
        if (200 == t) {
            var o = document.createElement("iframe");
            o.src = i.scriptUrl + "?id=" + e + "&filename=" + i.getFilename(), i._hide(o), document.body.appendChild(o), r()
        } else r(new Error("Error saving file"))
    }) : r(new Error("Maximum allowed file size exceeded (" + this.options.maxSize + " bytes)")), setTimeout(function () {
        r(new Error("Error saving file (time out)"))
    }, this.timeout)
}, Notify.prototype.showNotification = function (e) {
    return this.showMessage({type: "notification", message: e, closeButton: !1})
}, Notify.prototype.showError = function (e) {
    return this.showMessage({type: "error", message: e.message ? "Error: " + e.message : e.toString(), closeButton: !0})
}, Notify.prototype.showMessage = function (e) {
    var t = this.dom.frame;
    if (!t) {
        var o = 500, i = 5, r = document.body.offsetWidth || window.innerWidth;
        t = document.createElement("div"), t.style.position = "absolute", t.style.left = (r - o) / 2 + "px", t.style.width = o + "px", t.style.top = i + "px", t.style.zIndex = "999", document.body.appendChild(t), this.dom.frame = t
    }
    var n = e.type || "notification", a = e.closeButton !== !1, l = document.createElement("div");
    l.className = n, l.type = n, l.closeable = a, l.style.position = "relative", t.appendChild(l);
    var s = document.createElement("table");
    s.style.width = "100%", l.appendChild(s);
    var d = document.createElement("tbody");
    s.appendChild(d);
    var p = document.createElement("tr");
    d.appendChild(p);
    var c = document.createElement("td");
    if (c.innerHTML = e.message || "", p.appendChild(c), a) {
        var u = document.createElement("td");
        u.style.textAlign = "right", u.style.verticalAlign = "top", p.appendChild(u);
        var m = document.createElement("button");
        m.innerHTML = "&times;", m.title = "Close message (ESC)", u.appendChild(m);
        var h = this;
        m.onclick = function () {
            h.removeMessage(l)
        }
    }
    return l
}, Notify.prototype.removeMessage = function (e) {
    var t = this.dom.frame;
    if (!e && t) {
        for (var o = t.firstChild; o && !o.closeable;)o = o.nextSibling;
        o && o.closeable && (e = o)
    }
    e && e.parentNode == t && e.parentNode.removeChild(e), t && 0 == t.childNodes.length && (t.parentNode.removeChild(t), delete this.dom.frame)
}, Notify.prototype.onKeyDown = function (e) {
    var t = e.which;
    27 == t && (this.removeMessage(), e.preventDefault(), e.stopPropagation())
}, Splitter.prototype.onMouseDown = function (e) {
    var t = this, o = e.which ? 1 == e.which : 1 == e.button;
    o && (jsoneditor.util.addClassName(this.container, "active"), this.params.mousedown || (this.params.mousedown = !0, this.params.mousemove = jsoneditor.util.addEventListener(document, "mousemove", function (e) {
        t.onMouseMove(e)
    }), this.params.mouseup = jsoneditor.util.addEventListener(document, "mouseup", function (e) {
        t.onMouseUp(e)
    }), this.params.screenX = e.screenX, this.params.changed = !1, this.params.value = this.getValue()), e.preventDefault(), e.stopPropagation())
}, Splitter.prototype.onMouseMove = function (e) {
    if (void 0 != this.width) {
        var t = e.screenX - this.params.screenX, o = this.params.value + t / this.width;
        o = this.setValue(o), o != this.params.value && (this.params.changed = !0), this.onChange(o)
    }
    e.preventDefault(), e.stopPropagation()
}, Splitter.prototype.onMouseUp = function (e) {
    if (jsoneditor.util.removeClassName(this.container, "active"), this.params.mousedown) {
        jsoneditor.util.removeEventListener(document, "mousemove", this.params.mousemove), jsoneditor.util.removeEventListener(document, "mouseup", this.params.mouseup), this.params.mousemove = void 0, this.params.mouseup = void 0, this.params.mousedown = !1;
        var t = this.getValue();
        this.params.changed || (0 == t && (t = this.setValue(.2), this.onChange(t)), 1 == t && (t = this.setValue(.8), this.onChange(t)))
    }
    e.preventDefault(), e.stopPropagation()
}, Splitter.prototype.setWidth = function (e) {
    this.width = e
}, Splitter.prototype.setValue = function (e) {
    e = Number(e), void 0 != this.width && this.width > this.snap && (e < this.snap / this.width && (e = 0), e > (this.width - this.snap) / this.width && (e = 1)), this.value = e;
    try {
        localStorage.splitterValue = e
    } catch (t) {
        console && console.log && console.log(t)
    }
    return e
}, Splitter.prototype.getValue = function () {
    var e = this.value;
    if (void 0 == e)try {
        void 0 != localStorage.splitterValue && (e = Number(localStorage.splitterValue), e = this.setValue(e))
    } catch (t) {
        console.log(t)
    }
    return void 0 == e && (e = this.setValue(.5)), e
};
var treeEditor = null, codeEditor = null, app = {};
app.CodeToTree = function () {
    try {
        treeEditor.set(codeEditor.get())
    } catch (e) {
        app.notify.showError(app.formatError(e))
    }
}, app.treeToCode = function () {
    try {
        codeEditor.set(treeEditor.get())
    } catch (e) {
        app.notify.showError(app.formatError(e))
    }
}, app.load = function (e) {
    try {
        app.notify = new Notify, app.retriever = new FileRetriever({
            scriptUrl: "fileretriever.php",
            notify: app.notify
        });

        if (window.QueryParams) {
            var t = new QueryParams, o = t.getValue("url");
            o && (e = {}, app.openUrl(o))
        }
        app.lastChanged = void 0;
        var i = document.getElementById("codeEditor");
        codeEditor = new jsoneditor.JSONEditor(i, {
            mode: "code", change: function () {
                app.lastChanged = codeEditor
            }, error: function (e) {
                app.notify.showError(app.formatError(e))
            }
        }), codeEditor.set(e), i = document.getElementById("treeEditor"), treeEditor = new jsoneditor.JSONEditor(i, {
            mode: "tree", change: function () {
                app.lastChanged = treeEditor
            }, error: function (e) {
                app.notify.showError(app.formatError(e))
            }
        }), treeEditor.set(e), app.splitter = new Splitter({
            container: document.getElementById("drag"), change: function () {
                app.resize()
            }
        });
        var r = document.getElementById("toTree");
        r.onclick = function () {
            this.focus(), app.CodeToTree()
        };
        var n = document.getElementById("toCode");
        n.onclick = function () {
            this.focus(), app.treeToCode()
        }, jsoneditor.util.addEventListener(window, "resize", app.resize);
        var a = document.getElementById("clear");
        a.onclick = app.clearFile;
        var l = document.getElementById("menuOpenFile");
        l.onclick = function (e) {
            app.openFile(), e.stopPropagation(), e.preventDefault()
        };
        var s = document.getElementById("menuOpenUrl");
        s.onclick = function (e) {
            app.openUrl(), e.stopPropagation(), e.preventDefault()
        };
        var d = document.getElementById("save");
        d.onclick = app.saveFile, codeEditor.focus(), document.body.spellcheck = !1
    } catch (p) {
        try {
            app.notify.showError(p)
        } catch (c) {
            console && console.log && console.log(p), alert(p)
        }
    }
}, app.openCallback = function (e, t) {
    if (e)app.notify.showError(e); else if (null != t) {
        codeEditor.setText(t);
        try {
            var o = jsoneditor.util.parse(t);
            treeEditor.set(o)
        } catch (e) {
            treeEditor.set({}), app.notify.showError(app.formatError(e))
        }
    }
}, app.openFile = function () {
    app.retriever.loadFile(app.openCallback)
}, app.openUrl = function (e) {
    e ? app.retriever.loadUrl(e, app.openCallback) : app.retriever.loadUrlDialog(app.openCallback)
}, app.saveFile = function () {
    app.lastChanged == treeEditor && app.treeToCode(), app.lastChanged = void 0;
    var e = codeEditor.getText();
    app.retriever.saveFile(e, function (e) {
        e && app.notify.showError(e)
    })
}, app.formatError = function (e) {
    var t = '<pre class="error">' + e.toString() + "</pre>";
    return "undefined" != typeof jsonlint && (t += '<a class="error" href="http://zaach.github.com/jsonlint/" target="_blank">validated by jsonlint</a>'), t
}, app.clearFile = function () {
    var e = {};
    codeEditor.set(e), treeEditor.set(e)
}, app.resize = function () {
    var e = document.getElementById("menu"),
        t = document.getElementById("treeEditor"),
        o = document.getElementById("codeEditor"),
        i = document.getElementById("splitter"),
        r = document.getElementById("buttons"),
        n = document.getElementById("drag"),
        a = document.getElementById("ad"),
        l = 15,
        s = window.innerWidth || document.body.offsetWidth || document.documentElement.offsetWidth, d = a ? a.clientWidth : 0;

    if (d && (s -= d + l), app.splitter) {
        //app.splitter.setWidth(s);
        var p = app.splitter.getValue(), c = p > 0, u = 1 > p, m = c && u;
        r.style.display = m ? "" : "none";
        var h, f = i.clientWidth;
        if (c)
            if (u) {
                h = s * p - f / 2;
                var v = 8 == jsoneditor.util.getInternetExplorerVersion();
                n.innerHTML = v ? "|" : "&#8942;", n.title = "Drag left or right to change the width of the panels"
            } else
                h = s * p - f, n.innerHTML = "&lsaquo;",
                    n.title = "Drag left to show the tree editor";
        else
            h = 0, n.innerHTML = "&rsaquo;",
                n.title = "Drag right to show the code editor";
        //o.style.display = 0 == p ? "none" : "",
        //    o.style.width = Math.max(Math.round(h), 0) + "px",
        //    codeEditor.resize(),
            n.style.height = i.clientHeight - r.clientHeight - 2 * l - (m ? l : 0) + "px",
            n.style.lineHeight = n.style.height,
            t.style.display = 1 == p ? "none" : "",
            t.style.left = Math.round(h + f) + "px", t.style.width = Math.max(Math.round(s - h - f - 2), 0) + "px"
    }
    e && (e.style.right = d ? l + (d + l) + "px" : l + "px")
},app.getResult= function(){
    return treeEditor.get();
},app.setValue=function(data){
    codeEditor.set(data);
    treeEditor.set(data);
};