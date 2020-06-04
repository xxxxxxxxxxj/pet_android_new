package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.List;

public class ShopListBean implements Serializable{


	/**
	 * code : 0
	 * data : {"regions":[{"city":"北京市","regionMap":[{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305179831747360.jpeg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305215126815293.jpeg","address":"123","shopWxNum":"123","lng":113.927956,"mile":1961689.0353660258,"shopName":"123","dist":"1961.69km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305345352001643.jpg"],"phone":"123","id":20,"openTime":"123","lat":22.490612,"status":1}],"region":"东城区","areacode":110101,"selected":0},{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223985788809634612.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/shopzjdj.jpg?v=343","address":"朝阳区珠江帝景B区北街127号底商","shopWxNum":"test","lng":116.486058,"mile":15695.63079530299,"shopActivePoint":8,"shopName":"珠江帝景店","dist":"15.7km","cityId":1,"sort":3,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/zjdjweikaiye.png?v=1"],"shopActiveTitle":"biaotitest","shopActiveBackup":"https://api.cwjia.cn/static/content/protocol.html","areaId":16,"phone":"400-8700-111","id":12,"tag":"上次预约","openTime":"09:30-20:00","lat":39.895557,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223955510910255188.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/yyc.png","address":"朝阳区小营北路29号院融华世家底商109","shopWxNum":"1232345","lng":116.428095,"mile":4204.025551443674,"shopActivePoint":2,"shopName":"亚运村店","dist":"4.2km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/yyc.png?v=6"],"shopActiveTitle":"1234","shopActiveBackup":"https://www.baidu.com","areaId":12,"phone":"15340171792","id":8,"tag":"距离最近","openTime":"09:30-20:00","lat":40.005274,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223954378666165047.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/wj.png?v=85","address":"朝阳区望京街道湖光中街季景沁园底商22A","shopWxNum":"Zzz","lng":116.46169,"mile":7083.463148186043,"shopActivePoint":8,"shopName":"望京店","dist":"7.08km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/zzz.jpg?v=102"],"shopActiveTitle":"cceshicehis","shopActiveBackup":"http://www.haotang365.com.cn","areaId":10,"phone":"15340137695","id":7,"openTime":"09:30-20:00","lat":40.002329,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/html5/15217072359279741710.png","img":"","address":"朝阳区北沙滩","shopWxNum":"18234566543","lng":116.339533,"mile":13223.149228771481,"shopName":"朝阳区2号店","dist":"13.22km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724077712817790936.jpg"],"phone":"18234566543","id":24,"openTime":"09:30-20:00","lat":39.895557,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223950498302564993.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/shj.png?v=703","address":"朝阳区百子湾南二路A派公寓一号楼底商1-8","shopWxNum":"111111","lng":116.475051,"mile":14501.643221626824,"shopActivePoint":1,"shopName":"双井店","dist":"14.5km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/zzz.jpg?v=102"],"shopActiveTitle":"huodong huodong ,sdkfjskdf,sllsl,sdfk","shopActiveBackup":"https://demo.cwjia.cn","areaId":9,"phone":"15340108680","id":3,"openTime":"09:30-20:00","lat":39.902762,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223991312828874513.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170711190423.png?v=919","address":"朝阳区青年路润枫水尚东区4号楼07号底商","shopWxNum":"pet-house365","lng":116.522325,"mile":14973.87353603541,"shopName":"朝阳大悦城店","dist":"14.97km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170712135711.png?v=236"],"areaId":17,"phone":"010-53686050","id":13,"openTime":"09:30-20:00","lat":39.93238,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/html5/15217072359279741710.png","img":"","address":"朝阳区北沙滩","shopWxNum":"13739248814","lng":113.947523,"mile":1959658.2006746116,"shopName":"后海中心店","dist":"1959.66km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724075199802345126.jpg"],"phone":"13739248814","id":23,"openTime":"09:30-20:00","lat":22.507025,"status":1}],"region":"朝阳区","areacode":110105,"selected":1},{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723192488820450175.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723192525014631917.jpg","address":"12","shopWxNum":"12","lng":113.927956,"mile":1961689.0353660258,"shopName":"12","dist":"1961.69km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723192631269477762.jpg"],"phone":"12","id":19,"openTime":"12","lat":22.490612,"status":1}],"region":"丰台区","areacode":110106,"selected":0},{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223963902914640411.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/qinghe.jpg?v=820","address":"海淀区学府树家园二区1号楼1-12底商","shopWxNum":"111111111","lng":116.339533,"mile":4635.354071503134,"shopName":"清河五彩城店","dist":"4.64km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/qhshg.jpg?v=182"],"areaId":15,"phone":"15340171794","id":11,"openTime":"09:30-20:00","lat":40.03907,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223958500551308210.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/wanliu.jpg","address":"海淀区蓝靛厂时雨园甲2-10号2层","shopWxNum":"pet-house365","lng":116.289718,"mile":8995.401374403022,"shopName":"远大路万柳店","dist":"9km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/zzz.jpg?v=102"],"areaId":14,"phone":"15340171790","id":10,"openTime":"09:30-20:00","lat":39.967588,"status":1}],"region":"海淀区","areacode":110108,"selected":0}],"cityId":1,"allShops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223991854344360214.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191726.jpg?v=778","address":"北沙滩店一号院","shopWxNum":"vvb","lng":116.428095,"mile":4204.025551443674,"shopActivePoint":1,"shopName":"宠物家福田店","dist":"4.2km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191751.png?v=19"],"shopActiveTitle":"测试","shopActiveBackup":"www.baidu.com","areaId":19,"phone":"18519283822","id":14,"openTime":"09:30-20:00","lat":40.005274,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723145532146460582.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723145619766674331.png","address":"南山南","shopWxNum":"34567","lng":116.450229,"mile":10764.529678266701,"shopName":"宠物家南山南","dist":"10.76km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723146075104726462.jpg"],"phone":"324567","id":18,"openTime":"09:30-20:00","lat":39.930461,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223992715282729315.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20171130103745.jpg?v=664","address":"朝阳","shopWxNum":"vv","lng":116.507942,"mile":13667.448477866224,"shopName":"深圳2号店","dist":"13.67km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191751.png?v=19"],"areaId":18,"phone":"15300000000","id":15,"openTime":"09:30-20:00","lat":39.937196,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724080887215458897.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724080951932441260.png","address":"盐田区","shopWxNum":"432","lng":113.927956,"mile":1959874.7814831312,"shopName":"盐田","dist":"1959.87km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724081620964006578.png"],"phone":"543","id":25,"openTime":"09:30-20:00","lat":22.507025,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223993076195830116.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15722514723378512813.jpg","address":"工业九路与花园路交叉口北50米","shopWxNum":"222","lng":113.927956,"mile":1961689.0353660258,"shopName":"深圳3号店","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15722514843603050022.jpg"],"areaId":20,"phone":"111","id":16,"openTime":"09:30-20:00","lat":22.490612,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143083000364729.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143126725105625.jpg","address":"深圳市南山区蛇口望海路南蛇口港西南海玫瑰园一期102B铺","shopWxNum":"pet-house365","lng":113.927956,"mile":1961689.0353660258,"shopName":"南海玫瑰园店","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143561023006827.png"],"phone":"0755-23890663","id":17,"openTime":"10:00-22:00","lat":22.490612,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724060258393384247.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724060330091588703.png","address":"南山北","shopWxNum":"45678","lng":113.927956,"mile":1961689.0353660258,"shopName":"南山区","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724061014763223006.png"],"phone":"56789","id":22,"openTime":"09:30-20:00","lat":22.490612,"status":1}],"selected":1},{"city":"深圳市","regionMap":[{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223992715282729315.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20171130103745.jpg?v=664","address":"朝阳","shopWxNum":"vv","lng":116.507942,"mile":13667.448477866224,"shopName":"深圳2号店","dist":"13.67km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191751.png?v=19"],"areaId":18,"phone":"15300000000","id":15,"openTime":"09:30-20:00","lat":39.937196,"status":1}],"region":"罗湖区","areacode":440303,"selected":0},{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223991854344360214.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191726.jpg?v=778","address":"北沙滩店一号院","shopWxNum":"vvb","lng":116.428095,"mile":4204.025551443674,"shopActivePoint":1,"shopName":"宠物家福田店","dist":"4.2km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191751.png?v=19"],"shopActiveTitle":"测试","shopActiveBackup":"www.baidu.com","areaId":19,"phone":"18519283822","id":14,"openTime":"09:30-20:00","lat":40.005274,"status":1}],"region":"福田区","areacode":440304,"selected":0},{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723145532146460582.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723145619766674331.png","address":"南山南","shopWxNum":"34567","lng":116.450229,"mile":10764.529678266701,"shopName":"宠物家南山南","dist":"10.76km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723146075104726462.jpg"],"phone":"324567","id":18,"openTime":"09:30-20:00","lat":39.930461,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223993076195830116.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15722514723378512813.jpg","address":"工业九路与花园路交叉口北50米","shopWxNum":"222","lng":113.927956,"mile":1961689.0353660258,"shopName":"深圳3号店","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15722514843603050022.jpg"],"areaId":20,"phone":"111","id":16,"openTime":"09:30-20:00","lat":22.490612,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143083000364729.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143126725105625.jpg","address":"深圳市南山区蛇口望海路南蛇口港西南海玫瑰园一期102B铺","shopWxNum":"pet-house365","lng":113.927956,"mile":1961689.0353660258,"shopName":"南海玫瑰园店","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143561023006827.png"],"phone":"0755-23890663","id":17,"openTime":"10:00-22:00","lat":22.490612,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724060258393384247.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724060330091588703.png","address":"南山北","shopWxNum":"45678","lng":113.927956,"mile":1961689.0353660258,"shopName":"南山区","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724061014763223006.png"],"phone":"56789","id":22,"openTime":"09:30-20:00","lat":22.490612,"status":1}],"region":"南山区","areacode":440305,"selected":0},{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724080887215458897.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724080951932441260.png","address":"盐田区","shopWxNum":"432","lng":113.927956,"mile":1959874.7814831312,"shopName":"盐田","dist":"1959.87km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724081620964006578.png"],"phone":"543","id":25,"openTime":"09:30-20:00","lat":22.507025,"status":1}],"region":"盐田区","areacode":440308,"selected":0}],"cityId":2,"allShops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223991854344360214.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191726.jpg?v=778","address":"北沙滩店一号院","shopWxNum":"vvb","lng":116.428095,"mile":4204.025551443674,"shopActivePoint":1,"shopName":"宠物家福田店","dist":"4.2km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191751.png?v=19"],"shopActiveTitle":"测试","shopActiveBackup":"www.baidu.com","areaId":19,"phone":"18519283822","id":14,"openTime":"09:30-20:00","lat":40.005274,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723145532146460582.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723145619766674331.png","address":"南山南","shopWxNum":"34567","lng":116.450229,"mile":10764.529678266701,"shopName":"宠物家南山南","dist":"10.76km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723146075104726462.jpg"],"phone":"324567","id":18,"openTime":"09:30-20:00","lat":39.930461,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223992715282729315.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20171130103745.jpg?v=664","address":"朝阳","shopWxNum":"vv","lng":116.507942,"mile":13667.448477866224,"shopName":"深圳2号店","dist":"13.67km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191751.png?v=19"],"areaId":18,"phone":"15300000000","id":15,"openTime":"09:30-20:00","lat":39.937196,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724080887215458897.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724080951932441260.png","address":"盐田区","shopWxNum":"432","lng":113.927956,"mile":1959874.7814831312,"shopName":"盐田","dist":"1959.87km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724081620964006578.png"],"phone":"543","id":25,"openTime":"09:30-20:00","lat":22.507025,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223993076195830116.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15722514723378512813.jpg","address":"工业九路与花园路交叉口北50米","shopWxNum":"222","lng":113.927956,"mile":1961689.0353660258,"shopName":"深圳3号店","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15722514843603050022.jpg"],"areaId":20,"phone":"111","id":16,"openTime":"09:30-20:00","lat":22.490612,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143083000364729.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143126725105625.jpg","address":"深圳市南山区蛇口望海路南蛇口港西南海玫瑰园一期102B铺","shopWxNum":"pet-house365","lng":113.927956,"mile":1961689.0353660258,"shopName":"南海玫瑰园店","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143561023006827.png"],"phone":"0755-23890663","id":17,"openTime":"10:00-22:00","lat":22.490612,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724060258393384247.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724060330091588703.png","address":"南山北","shopWxNum":"45678","lng":113.927956,"mile":1961689.0353660258,"shopName":"南山区","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724061014763223006.png"],"phone":"56789","id":22,"openTime":"09:30-20:00","lat":22.490612,"status":1}],"selected":0}]}
	 * msg : 操作成功
	 */

	private int code;
	private DataBean data;
	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static class DataBean implements Serializable {
		private List<RegionsBean> regions;

		public List<RegionsBean> getRegions() {
			return regions;
		}

		public void setRegions(List<RegionsBean> regions) {
			this.regions = regions;
		}

		public static class RegionsBean implements Serializable{
			/**
			 * city : 北京市
			 * regionMap : [{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305179831747360.jpeg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305215126815293.jpeg","address":"123","shopWxNum":"123","lng":113.927956,"mile":1961689.0353660258,"shopName":"123","dist":"1961.69km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305345352001643.jpg"],"phone":"123","id":20,"openTime":"123","lat":22.490612,"status":1}],"region":"东城区","areacode":110101,"selected":0},{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223985788809634612.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/shopzjdj.jpg?v=343","address":"朝阳区珠江帝景B区北街127号底商","shopWxNum":"test","lng":116.486058,"mile":15695.63079530299,"shopActivePoint":8,"shopName":"珠江帝景店","dist":"15.7km","cityId":1,"sort":3,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/zjdjweikaiye.png?v=1"],"shopActiveTitle":"biaotitest","shopActiveBackup":"https://api.cwjia.cn/static/content/protocol.html","areaId":16,"phone":"400-8700-111","id":12,"tag":"上次预约","openTime":"09:30-20:00","lat":39.895557,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223955510910255188.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/yyc.png","address":"朝阳区小营北路29号院融华世家底商109","shopWxNum":"1232345","lng":116.428095,"mile":4204.025551443674,"shopActivePoint":2,"shopName":"亚运村店","dist":"4.2km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/yyc.png?v=6"],"shopActiveTitle":"1234","shopActiveBackup":"https://www.baidu.com","areaId":12,"phone":"15340171792","id":8,"tag":"距离最近","openTime":"09:30-20:00","lat":40.005274,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223954378666165047.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/wj.png?v=85","address":"朝阳区望京街道湖光中街季景沁园底商22A","shopWxNum":"Zzz","lng":116.46169,"mile":7083.463148186043,"shopActivePoint":8,"shopName":"望京店","dist":"7.08km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/zzz.jpg?v=102"],"shopActiveTitle":"cceshicehis","shopActiveBackup":"http://www.haotang365.com.cn","areaId":10,"phone":"15340137695","id":7,"openTime":"09:30-20:00","lat":40.002329,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/html5/15217072359279741710.png","img":"","address":"朝阳区北沙滩","shopWxNum":"18234566543","lng":116.339533,"mile":13223.149228771481,"shopName":"朝阳区2号店","dist":"13.22km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724077712817790936.jpg"],"phone":"18234566543","id":24,"openTime":"09:30-20:00","lat":39.895557,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223950498302564993.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/shj.png?v=703","address":"朝阳区百子湾南二路A派公寓一号楼底商1-8","shopWxNum":"111111","lng":116.475051,"mile":14501.643221626824,"shopActivePoint":1,"shopName":"双井店","dist":"14.5km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/zzz.jpg?v=102"],"shopActiveTitle":"huodong huodong ,sdkfjskdf,sllsl,sdfk","shopActiveBackup":"https://demo.cwjia.cn","areaId":9,"phone":"15340108680","id":3,"openTime":"09:30-20:00","lat":39.902762,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223991312828874513.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170711190423.png?v=919","address":"朝阳区青年路润枫水尚东区4号楼07号底商","shopWxNum":"pet-house365","lng":116.522325,"mile":14973.87353603541,"shopName":"朝阳大悦城店","dist":"14.97km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170712135711.png?v=236"],"areaId":17,"phone":"010-53686050","id":13,"openTime":"09:30-20:00","lat":39.93238,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/html5/15217072359279741710.png","img":"","address":"朝阳区北沙滩","shopWxNum":"13739248814","lng":113.947523,"mile":1959658.2006746116,"shopName":"后海中心店","dist":"1959.66km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724075199802345126.jpg"],"phone":"13739248814","id":23,"openTime":"09:30-20:00","lat":22.507025,"status":1}],"region":"朝阳区","areacode":110105,"selected":1},{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723192488820450175.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723192525014631917.jpg","address":"12","shopWxNum":"12","lng":113.927956,"mile":1961689.0353660258,"shopName":"12","dist":"1961.69km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723192631269477762.jpg"],"phone":"12","id":19,"openTime":"12","lat":22.490612,"status":1}],"region":"丰台区","areacode":110106,"selected":0},{"shops":[{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223963902914640411.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/qinghe.jpg?v=820","address":"海淀区学府树家园二区1号楼1-12底商","shopWxNum":"111111111","lng":116.339533,"mile":4635.354071503134,"shopName":"清河五彩城店","dist":"4.64km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/qhshg.jpg?v=182"],"areaId":15,"phone":"15340171794","id":11,"openTime":"09:30-20:00","lat":40.03907,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223958500551308210.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/wanliu.jpg","address":"海淀区蓝靛厂时雨园甲2-10号2层","shopWxNum":"pet-house365","lng":116.289718,"mile":8995.401374403022,"shopName":"远大路万柳店","dist":"9km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/zzz.jpg?v=102"],"areaId":14,"phone":"15340171790","id":10,"openTime":"09:30-20:00","lat":39.967588,"status":1}],"region":"海淀区","areacode":110108,"selected":0}]
			 * cityId : 1
			 * allShops : [{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223991854344360214.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191726.jpg?v=778","address":"北沙滩店一号院","shopWxNum":"vvb","lng":116.428095,"mile":4204.025551443674,"shopActivePoint":1,"shopName":"宠物家福田店","dist":"4.2km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191751.png?v=19"],"shopActiveTitle":"测试","shopActiveBackup":"www.baidu.com","areaId":19,"phone":"18519283822","id":14,"openTime":"09:30-20:00","lat":40.005274,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723145532146460582.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723145619766674331.png","address":"南山南","shopWxNum":"34567","lng":116.450229,"mile":10764.529678266701,"shopName":"宠物家南山南","dist":"10.76km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723146075104726462.jpg"],"phone":"324567","id":18,"openTime":"09:30-20:00","lat":39.930461,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223992715282729315.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20171130103745.jpg?v=664","address":"朝阳","shopWxNum":"vv","lng":116.507942,"mile":13667.448477866224,"shopName":"深圳2号店","dist":"13.67km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191751.png?v=19"],"areaId":18,"phone":"15300000000","id":15,"openTime":"09:30-20:00","lat":39.937196,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724080887215458897.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724080951932441260.png","address":"盐田区","shopWxNum":"432","lng":113.927956,"mile":1959874.7814831312,"shopName":"盐田","dist":"1959.87km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724081620964006578.png"],"phone":"543","id":25,"openTime":"09:30-20:00","lat":22.507025,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223993076195830116.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15722514723378512813.jpg","address":"工业九路与花园路交叉口北50米","shopWxNum":"222","lng":113.927956,"mile":1961689.0353660258,"shopName":"深圳3号店","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15722514843603050022.jpg"],"areaId":20,"phone":"111","id":16,"openTime":"09:30-20:00","lat":22.490612,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143083000364729.jpg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143126725105625.jpg","address":"深圳市南山区蛇口望海路南蛇口港西南海玫瑰园一期102B铺","shopWxNum":"pet-house365","lng":113.927956,"mile":1961689.0353660258,"shopName":"南海玫瑰园店","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723143561023006827.png"],"phone":"0755-23890663","id":17,"openTime":"10:00-22:00","lat":22.490612,"status":1},{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724060258393384247.png","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724060330091588703.png","address":"南山北","shopWxNum":"45678","lng":113.927956,"mile":1961689.0353660258,"shopName":"南山区","dist":"1961.69km","cityId":2,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15724061014763223006.png"],"phone":"56789","id":22,"openTime":"09:30-20:00","lat":22.490612,"status":1}]
			 * selected : 1
			 */

			private String city;
			private int cityId;
			private int selected;
			private List<RegionMapBean> regionMap;
			private List<AllShopsBean> allShops;

			public String getCity() {
				return city;
			}

			public void setCity(String city) {
				this.city = city;
			}

			public int getCityId() {
				return cityId;
			}

			public void setCityId(int cityId) {
				this.cityId = cityId;
			}

			public int getSelected() {
				return selected;
			}

			public void setSelected(int selected) {
				this.selected = selected;
			}

			public List<RegionMapBean> getRegionMap() {
				return regionMap;
			}

			public void setRegionMap(List<RegionMapBean> regionMap) {
				this.regionMap = regionMap;
			}

			public List<AllShopsBean> getAllShops() {
				return allShops;
			}

			public void setAllShops(List<AllShopsBean> allShops) {
				this.allShops = allShops;
			}

			public static class RegionMapBean implements Serializable{
				/**
				 * shops : [{"shopWxImg":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305179831747360.jpeg","img":"http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305215126815293.jpeg","address":"123","shopWxNum":"123","lng":113.927956,"mile":1961689.0353660258,"shopName":"123","dist":"1961.69km","cityId":1,"sort":1,"hotelImg":["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305345352001643.jpg"],"phone":"123","id":20,"openTime":"123","lat":22.490612,"status":1}]
				 * region : 东城区
				 * areacode : 110101
				 * selected : 0
				 */

				private String region;
				private int areacode;
				private int selected;
				private int shopNum;
				private List<ShopsBean> shops;

				public RegionMapBean(String region, int areacode, int selected, int shopNum, List<ShopsBean> shops) {
					this.region = region;
					this.areacode = areacode;
					this.selected = selected;
					this.shopNum = shopNum;
					this.shops = shops;
				}

				public String getRegion() {
					return region;
				}

				public void setRegion(String region) {
					this.region = region;
				}

				public int getShopNum() {
					return shopNum;
				}

				public void setShopNum(int shopNum) {
					this.shopNum = shopNum;
				}

				public int getAreacode() {
					return areacode;
				}

				public void setAreacode(int areacode) {
					this.areacode = areacode;
				}

				public int getSelected() {
					return selected;
				}

				public void setSelected(int selected) {
					this.selected = selected;
				}

				public List<ShopsBean> getShops() {
					return shops;
				}

				public void setShops(List<ShopsBean> shops) {
					this.shops = shops;
				}

				public static class ShopsBean implements Serializable{
					/**
					 * shopWxImg : http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305179831747360.jpeg
					 * img : http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305215126815293.jpeg
					 * address : 123
					 * shopWxNum : 123
					 * lng : 113.927956
					 * mile : 1961689.0353660258
					 * shopName : 123
					 * dist : 1961.69km
					 * cityId : 1
					 * sort : 1
					 * hotelImg : ["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15723305345352001643.jpg"]
					 * phone : 123
					 * id : 20
					 * openTime : 123
					 * lat : 22.490612
					 * status : 1
					 */

					private String shopWxImg;
					private String img;
					private String address;
					private String shopWxNum;
					private String tag;
					private double lng;
					private double mile;
					private String shopName;
					private String dist;
					private int cityId;
					private int sort;
					private String phone;
					private int id;
					private String openTime;
					private String shopActiveTitle;
					private String notOpen;
					private double lat;
					private int status;
					private String region;
					private int classId;
					private int shopActivePoint;
					private String shopActiveBackup;
					private int headerId;
					private List<String> hotelImg;

					public int getHeaderId() {
						return headerId;
					}

					public void setHeaderId(int headerId) {
						this.headerId = headerId;
					}

					public String getShopWxImg() {
						return shopWxImg;
					}

					public void setShopWxImg(String shopWxImg) {
						this.shopWxImg = shopWxImg;
					}

					public String getShopActiveBackup() {
						return shopActiveBackup;
					}

					public void setShopActiveBackup(String shopActiveBackup) {
						this.shopActiveBackup = shopActiveBackup;
					}

					public int getShopActivePoint() {
						return shopActivePoint;
					}

					public void setShopActivePoint(int shopActivePoint) {
						this.shopActivePoint = shopActivePoint;
					}

					public String getShopActiveTitle() {
						return shopActiveTitle;
					}

					public void setShopActiveTitle(String shopActiveTitle) {
						this.shopActiveTitle = shopActiveTitle;
					}

					public String getNotOpen() {
						return notOpen;
					}

					public void setNotOpen(String notOpen) {
						this.notOpen = notOpen;
					}

					public int getClassId() {
						return classId;
					}

					public void setClassId(int classId) {
						this.classId = classId;
					}

					public String getRegion() {
						return region;
					}

					public void setRegion(String region) {
						this.region = region;
					}

					public String getTag() {
						return tag;
					}

					public void setTag(String tag) {
						this.tag = tag;
					}

					public String getImg() {
						return img;
					}

					public void setImg(String img) {
						this.img = img;
					}

					public String getAddress() {
						return address;
					}

					public void setAddress(String address) {
						this.address = address;
					}

					public String getShopWxNum() {
						return shopWxNum;
					}

					public void setShopWxNum(String shopWxNum) {
						this.shopWxNum = shopWxNum;
					}

					public double getLng() {
						return lng;
					}

					public void setLng(double lng) {
						this.lng = lng;
					}

					public double getMile() {
						return mile;
					}

					public void setMile(double mile) {
						this.mile = mile;
					}

					public String getShopName() {
						return shopName;
					}

					public void setShopName(String shopName) {
						this.shopName = shopName;
					}

					public String getDist() {
						return dist;
					}

					public void setDist(String dist) {
						this.dist = dist;
					}

					public int getCityId() {
						return cityId;
					}

					public void setCityId(int cityId) {
						this.cityId = cityId;
					}

					public int getSort() {
						return sort;
					}

					public void setSort(int sort) {
						this.sort = sort;
					}

					public String getPhone() {
						return phone;
					}

					public void setPhone(String phone) {
						this.phone = phone;
					}

					public int getId() {
						return id;
					}

					public void setId(int id) {
						this.id = id;
					}

					public String getOpenTime() {
						return openTime;
					}

					public void setOpenTime(String openTime) {
						this.openTime = openTime;
					}

					public double getLat() {
						return lat;
					}

					public void setLat(double lat) {
						this.lat = lat;
					}

					public int getStatus() {
						return status;
					}

					public void setStatus(int status) {
						this.status = status;
					}

					public List<String> getHotelImg() {
						return hotelImg;
					}

					public void setHotelImg(List<String> hotelImg) {
						this.hotelImg = hotelImg;
					}
				}
			}

			public static class AllShopsBean implements Serializable{
				/**
				 * shopWxImg : http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/15223991854344360214.jpg
				 * img : http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191726.jpg?v=778
				 * address : 北沙滩店一号院
				 * shopWxNum : vvb
				 * lng : 116.428095
				 * mile : 4204.025551443674
				 * shopActivePoint : 1
				 * shopName : 宠物家福田店
				 * dist : 4.2km
				 * cityId : 2
				 * sort : 1
				 * hotelImg : ["http://dev-pet-avatar.oss-cn-beijing.aliyuncs.com/shop/imgs/20170817191751.png?v=19"]
				 * shopActiveTitle : 测试
				 * shopActiveBackup : www.baidu.com
				 * areaId : 19
				 * phone : 18519283822
				 * id : 14
				 * openTime : 09:30-20:00
				 * lat : 40.005274
				 * status : 1
				 */

				private String shopWxImg;
				private String img;
				private String address;
				private String shopWxNum;
				private double lng;
				private double mile;
				private int shopActivePoint;
				private String shopName;
				private String dist;
				private int cityId;
				private int sort;
				private String shopActiveTitle;
				private String shopActiveBackup;
				private int areaId;
				private String phone;
				private int id;
				private String openTime;
				private double lat;
				private int status;
				private List<String> hotelImg;

				public String getShopWxImg() {
					return shopWxImg;
				}

				public void setShopWxImg(String shopWxImg) {
					this.shopWxImg = shopWxImg;
				}

				public String getImg() {
					return img;
				}

				public void setImg(String img) {
					this.img = img;
				}

				public String getAddress() {
					return address;
				}

				public void setAddress(String address) {
					this.address = address;
				}

				public String getShopWxNum() {
					return shopWxNum;
				}

				public void setShopWxNum(String shopWxNum) {
					this.shopWxNum = shopWxNum;
				}

				public double getLng() {
					return lng;
				}

				public void setLng(double lng) {
					this.lng = lng;
				}

				public double getMile() {
					return mile;
				}

				public void setMile(double mile) {
					this.mile = mile;
				}

				public int getShopActivePoint() {
					return shopActivePoint;
				}

				public void setShopActivePoint(int shopActivePoint) {
					this.shopActivePoint = shopActivePoint;
				}

				public String getShopName() {
					return shopName;
				}

				public void setShopName(String shopName) {
					this.shopName = shopName;
				}

				public String getDist() {
					return dist;
				}

				public void setDist(String dist) {
					this.dist = dist;
				}

				public int getCityId() {
					return cityId;
				}

				public void setCityId(int cityId) {
					this.cityId = cityId;
				}

				public int getSort() {
					return sort;
				}

				public void setSort(int sort) {
					this.sort = sort;
				}

				public String getShopActiveTitle() {
					return shopActiveTitle;
				}

				public void setShopActiveTitle(String shopActiveTitle) {
					this.shopActiveTitle = shopActiveTitle;
				}

				public String getShopActiveBackup() {
					return shopActiveBackup;
				}

				public void setShopActiveBackup(String shopActiveBackup) {
					this.shopActiveBackup = shopActiveBackup;
				}

				public int getAreaId() {
					return areaId;
				}

				public void setAreaId(int areaId) {
					this.areaId = areaId;
				}

				public String getPhone() {
					return phone;
				}

				public void setPhone(String phone) {
					this.phone = phone;
				}

				public int getId() {
					return id;
				}

				public void setId(int id) {
					this.id = id;
				}

				public String getOpenTime() {
					return openTime;
				}

				public void setOpenTime(String openTime) {
					this.openTime = openTime;
				}

				public double getLat() {
					return lat;
				}

				public void setLat(double lat) {
					this.lat = lat;
				}

				public int getStatus() {
					return status;
				}

				public void setStatus(int status) {
					this.status = status;
				}

				public List<String> getHotelImg() {
					return hotelImg;
				}

				public void setHotelImg(List<String> hotelImg) {
					this.hotelImg = hotelImg;
				}
			}
		}
	}
}
