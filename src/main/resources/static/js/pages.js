'use strict';

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

/**
 * @description 页面交互事件
 * @author zetVv
 * @time 2020-10-01
 */
var pageEvent = {
	// 初始化页面样式
	init: function init() {
		var app = document.querySelector('#app');
		if (app) {
			app.style.width = window.innerHeight + 'px';
			app.style.height = window.innerWidth + 'px';
			if (window.orientation !== 90 && window.orientation !== '-90') {
				document.body.style.transform = 'rotate(90deg)';
			}
		}
		return this;
	},

	// 开始
	start: function start() {
		var _this = this;

		var page1 = [{ en: '', cn: '', font: '', name: '' }, { en: 'Miss you.', cn: '我想你', font: '无时无刻的想你', name: 'miss' }, { en: 'Care you.', cn: '在乎你', font: '你就是我的全部', name: 'care' }, { en: 'My. future.', cn: '陪伴你', font: '憧憬美好的未来', name: 'and' }, { en: 'Only you.', cn: '只有你', font: '我的心里只有你', name: 'only' }, { en: 'Protect you.', cn: '保护你', font: '我用行动告诉你', name: 'protect' }, { en: 'Thank you.', cn: '感谢你', font: '约定相守一辈子', name: 'thank' }];
		var html = '';
		page1.map(function (item) {
			html += '\n\t\t\t\t<div class="banner-item">\n\t\t\t\t\t<div class="english-font">' + item.en + '</div>\n\t\t\t\t\t<div class="china-font">\n\t\t\t\t\t\t<span class="' + item.name + '-you">' + item.cn + '</span>\n\t\t\t\t\t\t<span class="color-font">' + item.font + '</span>\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t';
		});

		var scroll_picture_body = document.querySelector('.scroll-picture-body');
		if (scroll_picture_body) {
			scroll_picture_body.innerHTML = html;
			setTimeout(function () {
				var page_end = document.querySelector('.page-end');
				if (page_end) {
					page_end.classList.remove('hide');
				}
				_this._page_1_event(1);
			}, 300);
		}
	},

	// 第一版文字动画
	_font_animate: function _font_animate() {
		var index = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 1;

		var scroll_picture_body = document.querySelector('.scroll-picture-body');
		if (scroll_picture_body) {
			scroll_picture_body.style.marginLeft = parseInt(index) * -60 + 'vh';
		}
	},

	// 第一版图片动画
	_picture_animate: function _picture_animate() {
		var type = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 'more';

		var picture_1_1 = document.querySelector('.picture-1-1');
		if (picture_1_1) {
			type === 'more' ? picture_1_1.classList.add('animate-picture') : picture_1_1.classList.remove('animate-picture');
		}
	},

	// 第一版遮罩层
	_layer_animate: function _layer_animate() {
		var type = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 'show';

		var layer_1_1 = document.querySelector('.layer-1-1');
		if (layer_1_1) {
			type === 'show' ? layer_1_1.classList.remove('hide') : layer_1_1.classList.add('hide');
		}
	},

	// 第一版事件回调
	_page_1_event: function _page_1_event() {
		var _this2 = this;

		var index = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 1;

		if (index === 7) {
			this._page_1_end();
		} else {
			setTimeout(function () {
				_this2._font_animate(index);
			}, 0);
			setTimeout(function () {
				var picture_1_1 = document.querySelector('.picture-1-1');
				if (picture_1_1) {
					_this2._layer_animate('show');
					picture_1_1.setAttribute('src', 'http://qjkqgtu9z.bkt.gdipper.com/picture-1-' + index + '.jpg');
				}
			}, 500);
			cut_break.init().show(function () {
				_this2._picture_animate();
				setTimeout(function () {
					index++;
					_this2._picture_animate('default');
					_this2._page_1_event(index);
				}, 4000);
			});
		}
	},

	// 第一版结束事件
	_page_1_end: function _page_1_end() {
		var _this3 = this;

		var banner_font_1 = document.querySelector('.banner-font-1');
		if (banner_font_1) {
			banner_font_1.classList.add('banner-font-1-small');
			setTimeout(function () {
				var many_happy = document.querySelector('.many-happy');
				if (many_happy) {
					many_happy.classList.add('mang-happy-show');
					setTimeout(function () {
						var page_1 = document.querySelector('.page-1');
						if (page_1) page_1.classList.add('page-1-show');
						setTimeout(function () {
							page_1.classList.add('page-1-end-show');
							setTimeout(function () {
								_this3._page_2_start();
							}, 1500);
						}, 2000);
					}, 3000);
				}
			}, 800);
		}
	},

	// 第二版开始
	_page_2_start: function _page_2_start() {
		var _this4 = this;

		var page_2 = document.querySelector('.page-2');
		if (page_2) page_2.classList.remove('hide');
		var page_end = document.querySelector('.page-end');
		if (page_end) {
			page_end.style.top = '-45%';
		}
		var content_2_1 = document.querySelector('.content-2-1');
		if (content_2_1) {
			content_2_1.classList.remove('hide');
			setTimeout(function () {
				var content_2_title = document.querySelector('.content-2-title');
				if (content_2_title) content_2_title.classList.add('content-2-title-show');
				setTimeout(function () {
					content_2_1.classList.add('content-2-1-show');
					setTimeout(function () {
						_this4._page_2_content_1(2);
					}, 1000);
				}, 1000);
			}, 500);
		}
	},

	// 第二版动画回调函数
	_page_2_content_1: function _page_2_content_1(index) {
		var _this5 = this;

		if (index > 4) {
			var msk_color = document.querySelector('.msk-color');
			if (msk_color) msk_color.classList.add('hide');
			var page_2 = document.querySelector('.page-2');
			if (page_2) {
				page_2.style.top = '-100%';
				setTimeout(function () {
					_this5._page_end(1);
				}, 2000);
			}
		} else {
			var i = index - 1;
			var content_pre = document.querySelector('.content-2-' + i);
			if (content_pre) {
				content_pre.classList.add('hide');
				var content_2 = document.querySelector('.content-2-' + index);
				if (content_2) {
					content_2.classList.remove('hide');
					setTimeout(function () {
						content_2.classList.add('content-2-' + index + '-show');
						setTimeout(function () {
							_this5._page_2_content_1(++index);
						}, index === 3 ? 7800 : 4800);
					}, 50);
				}
			}
		}
	},

	// 最后一版动画
	_page_end: function _page_end(index) {
		var _this6 = this;

		if (index > 3) {
			var year = document.querySelector('.year');
			var author = document.querySelector('.author');
			if (year && author) {
				year.classList.add('year-show');
				author.classList.add('author-show');
				this._end();
			}
		} else {
			var element = document.querySelector('.end-' + index);
			if (element) element.classList.add('end-' + index + '-show');
			setTimeout(function () {
				_this6._page_end(++index);
			}, index === 1 ? 2000 : 500);
		}
	},

	// 结束
	_end: function _end() {
		setTimeout(function () {
			var content_end_body = document.querySelector('.content-end-body');
			if (content_end_body) {
				content_end_body.classList.add('content-end-body-show');
				setTimeout(function () {
					var line_end = document.querySelector('.line-end');
					if (line_end) {
						line_end.classList.add('line-end-show');
						setTimeout(function () {
							var img_end = document.querySelector('.img-end');
							if (img_end) img_end.classList.add('img-end-show');
						}, 500);
					}
				}, 500);
			}
		}, 500);
	}
};

// 函数
var func = {
	t_img: '', // 定时器
	isLoad: true, // 是否加载完
	isImgLoad: function isImgLoad(callback) {
		var _this7 = this;

		var imgs = document.querySelectorAll('img');
		if (imgs.length) {
			for (var i = 0; i < imgs.length; i++) {
				if (_typeof(imgs[i]) === 'object') {
					if (!imgs[i].complete) {
						this.isLoad = false;
					}
				}
			}
		}
		// 为true，加载完毕
		if (this.isLoad) {
			clearTimeout(this.t_img); // 清除定时器
			callback();
		} else {
			this.isLoad = true;
			this.t_img = setTimeout(function () {
				_this7.isImgLoad(callback);
			}, 500);
		}
	},

	// 判断当前访问的端口
	isPc: function isPc() {
		var Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
		var flag = true;
		for (var v = 0; v < Agents.length; v++) {
			if (navigator.userAgent.indexOf(Agents[v]) > 0) {
				flag = false;
				break;
			}
		}
		return flag;
	}
};

window.addEventListener('load', function () {
	if (!func.isPc()) {
		pageEvent.init();
		setTimeout(function () {
			func.isImgLoad(function () {
				var page_start = document.querySelector('.page-start');
				if (page_start) {
					var button = document.querySelector('.button');
					var loading_layer = document.querySelector('.loading-layer');
					if (loading_layer) loading_layer.classList.add('hide');
					if (button) {
						button.classList.remove('hide');
						button.addEventListener('click', function () {
							var mp3 = document.querySelector('#mp3');
							if (mp3) mp3.play();
							page_start.classList.add('page-start-hide');
							pageEvent.start();
						});
					}
				}
			});
		}, 2000);
	} else {
		document.body.innerHTML = '<div style="padding: 200px;"><div><img src="http://qjkqgtu9z.bkt.gdipper.com/code.png" alt=""></div><div>一起守护最好那个ta吧，手机扫码访问哟！</div></div>';
	}
});
