/**
 * 序列化表单
 * Created by user on 2016-01-21.
 */
define(function () {
    /***
     *
     * @param form
     * @returns {{}}
     * @private
     */
    var _serializeObject = function (form) {
        var o = {};
        var a = form.serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

    return {
        serializeObject: _serializeObject
    };
});