'use strict';

/**
 * @description 碎屏过渡效果1
 * @author zetVv
 * @time 2020-10-01
 */
var cut_break = {
	min: 0, // x轴最小切块数量最小值,越大块越小,默认3
	max: 0, // y轴最小切块数量最大值,越大块越小,默认3
	element: '', // 容器,默认 cut-break
	codeNode: '', // node节点
	callBack: function callBack() {},
	// 初始化
	init: function init(options) {
		this.min = options && option.min !== undefined ? option.min : 3;
		this.max = options && option.max !== undefined ? option.max : 3;
		this.element = options && option.element !== undefined ? options.element : 'cut-break';
		var div = this._getElement();
		if (div) {
			var X = this._random(this.min, this.max);
			var Y = this._random(this.min, this.max);
			var codeNode = document.createElement('div');
			codeNode.style.zIndex = 99;
			codeNode.style.width = '100%';
			codeNode.style.height = '101%';
			var html = '';
			for (var i = 0; i < Y; i++) {
				for (var j = 0; j < X; j++) {
					html += '<div style="width: ' + 100 / X + '%; height: ' + 101 / Y + '%; float: left; background: #000000;"></div>';
				}
			};
			codeNode.innerHTML = html;
			div.appendChild(codeNode);
			this.codeNode = codeNode;
		}
		return this;
	},

	// 获取元素
	_getElement: function _getElement() {
		return document.querySelector('.' + this.element);
	},

	// 获取两个数之间随机数
	_random: function _random(min, max) {
		return parseInt(Math.random() * (max - min)) + ++min;
	},

	// 显示遮罩
	show: function show(callBack) {
		var _this = this;

		this.callBack = callBack || function () {};
		setTimeout(function () {
			_this._getElement().classList.add('cut-break-show');
			_this.break();
		}, 0);
	},

	// 打碎动画
	break: function _break() {
		var _this2 = this;

		var codeNode = this.codeNode.children;
		var tranX = this._random(-10, 10);
		var rotateX = this._random(-10, 10);
		setTimeout(function () {
			for (var i = 0; i < codeNode.length; i++) {
				codeNode[i].style.transition = Math.random() * 2.5 + 0.5 + 's all ease';
				codeNode[i].style.transform = 'perspective(800px) translateX(' + tranX + 'px) translateY(' + tranX + 'px) rotateX(' + rotateX + 'deg) rotateY(' + rotateX + 'deg) scale(' + Math.random() * 2 + ',' + Math.random() * 2 + ')';
				codeNode[i].style.opacity = 0;
			}
			_this2.callBack();
		}, 2000);
		setTimeout(function () {
			_this2._getElement().classList.remove('cut-break-show');
			_this2._getElement().removeChild(_this2.codeNode);
		}, 4000);
	}
};
