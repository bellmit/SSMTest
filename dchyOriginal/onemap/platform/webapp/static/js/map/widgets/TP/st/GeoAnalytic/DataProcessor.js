/**
 * Created by alex on 2017/9/30.
 *  单位 公顷
 */
define(['dojo/_base/lang',
    'dojo/_base/array'], function (lang, arrayUtil) {
    var _data = {};
    var _total = 0;
    var _nydArea, _jsydArea, _wlydArea;
    //一级分类
    var _areaMapFirst = {
        耕地: 0,
        园地: 0,
        林地: 0,
        草地: 0,
        城镇村及工矿用地: 0,
        交通运输用地: 0,
        水域及水利设施用地: 0,
        其他用地: 0
    };
    // 二级分类
    var _areaMapSecond = {
        水田: 0,
        水浇地: 0,
        旱地: 0,
        果园: 0,
        茶园: 0,
        其他园地: 0,
        有林地: 0,
        灌木林地: 0,
        其他林地: 0,
        天然牧草地: 0,
        人工牧草地: 0,
        其他草地: 0,
        城市: 0,
        建制镇: 0,
        村庄: 0,
        采矿用地: 0,
        风景名胜及特殊用地: 0,
        铁路用地: 0,
        公路用地: 0,
        农村道路: 0,
        机场用地: 0,
        港口码头用地: 0,
        管道运输用地: 0,
        河流水面: 0,
        湖泊水面: 0,
        水库水面: 0,
        坑塘水面: 0,
        沿海滩涂: 0,
        内陆滩涂: 0,
        沟渠: 0,
        水工建筑用地: 0,
        冰川及永久积雪: 0,
        设施农用地: 0,
        田坎: 0,
        盐碱地: 0,
        沼泽地: 0,
        沙地: 0,
        裸地: 0
    };
    // 三大类
    var _areaMapThird = {
        农用地: 0,
        建设用地: 0,
        未利用地: 0
    };

    var me = function (data) {
        _data = data;
        _total = 0;
        _nydArea = 0;
        _jsydArea = 0;
        _wlydArea = 0;

        for (var _k in _areaMapFirst) {
            _areaMapFirst[_k] = 0;
        }

        for (var _k in _areaMapSecond) {
            _areaMapSecond[_k] = 0;
        }
    };
    me.prototype = {
        parse: function () {
            console.info('分析结果: %o', _data);
            var ret = {};
            var _second = lang.clone(_areaMapSecond);
            var _first = lang.clone(_areaMapFirst);
            var _third = lang.clone(_areaMapThird);

            for (var k in _data) {
                var area = _data[k];
                if (_second.hasOwnProperty(k)) {
                    _second[k] = area;
                }
                _total += area;

                switch (k) {
                    case '水田':
                    case '水浇地':
                    case '旱地':
                        _first.耕地 += area;
                        break;
                    case '果园':
                    case '茶园':
                    case '其他园地':
                        _first.园地 += area;
                        break;
                    case '有林地':
                    case '灌木林地':
                    case '其他林地':
                        _first.林地 += area;
                        break;
                    case '天然牧草地':
                    case '人工牧草地':
                    case '其他草地':
                        _first.草地 += area;
                        break;
                    case '城市':
                    case '建制镇':
                    case '村庄':
                    case '采矿用地':
                    case '风景名胜及特殊用地':
                        _first.城镇村及工矿用地 += area;
                        break;
                    case '铁路用地':
                    case '公路用地':
                    case '农村道路':
                    case '机场用地':
                    case '港口码头用地':
                    case '管道运输用地':
                        _first.交通运输用地 += area;
                        break;
                    case '河流水面':
                    case '湖泊水面':
                    case '水库水面':
                    case '坑塘水面':
                    case '沿海滩涂':
                    case '内陆滩涂':
                    case '沟渠':
                    case '水工建筑用地':
                    case '冰川及永久积雪':
                        _first.水域及水利设施用地 += area;
                        break;
                    case '设施农用地':
                    case '田坎':
                    case '盐碱地':
                    case '沼泽地':
                    case '沙地':
                    case '裸地':
                        _first.其他用地 += area;
                        break;
                    default:
                        break;
                }
            }
            try {
                for (var _k in _first) {
                    if (_first[_k] > 0) {
                        _areaMapFirst[_k] = (_first[_k] / 10000).toFixed(4);
                    }
                }

                for (var _k in _second) {
                    if (_second[_k] > 0) {
                        _areaMapSecond[_k] = (_second[_k] / 10000).toFixed(4);
                    }
                }
                if (_total > 0) {
                    _total = (_total / 10000).toFixed(4);
                }
            } catch (e) {
                console.warn(e);
            }

            // for (var _k in _areaMapThird) {
            //     if (_areaMapThird[_k] > 0) {
            //         _areaMapThird[_k] = _areaMapThird[_k].toFixed(4);
            //     }
            // }


            return {
                total: _total,
                first: _areaMapFirst,
                second: _areaMapSecond,
                third: _areaMapThird
            };
        }
    };
    return me;
});