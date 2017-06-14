package com.jsz.peini.model.seller;

import java.util.List;

/**
 * Created by th on 2016/12/15.
 */

public class SellerCodesBySellerCodesBean {
    /**
     * resultCode : 1
     * resultDesc : 成功
     * sellerCodes : [{"subData":[{"name":"阿萨德01","id":59}],"name":"全部","id":50,"imgSrc":"/upload/sellerTypeIcon/2017/3/2/a50f7b2c2ffb4fa8aeb6c9d592ebd7e1.png"},{"subData":[{"name":"面包甜品","id":22},{"name":"其他（北京菜/沪菜）","id":23},{"name":"小吃快餐","id":10},{"name":"自助餐","id":11},{"name":"火锅","id":12},{"name":"烧烤","id":13},{"name":"西餐","id":14},{"name":"冀菜","id":15},{"name":"日韩菜","id":16},{"name":"海鲜粤菜","id":17},{"name":"川湘菜","id":18},{"name":"东北菜","id":19},{"name":"清真菜","id":20},{"name":"特色餐饮","id":21}],"name":"美食","id":1,"imgSrc":"/upload/sellerTypeIcon/2017/3/2/f5588385e8114734bb7515653081262c.png"},{"subData":[{"name":"Ktv","id":24}],"name":"唱歌","id":2,"imgSrc":"/upload/sellerTypeIcon/2017/3/2/ff1a9a78cd8f4bd6a14520d3f6f9ba45.png"},{"subData":[{"name":"电影院","id":25}],"name":"电影","id":3,"imgSrc":"/upload/sellerTypeIcon/2017/3/2/e964ac87702c4af4a944f546f3ba74ee.png"},{"subData":[{"name":"台球","id":26},{"name":"健身房","id":27},{"name":"瑜伽教室","id":28},{"name":"体育场馆","id":29}],"name":"运动健身","id":4,"imgSrc":"/upload/sellerTypeIcon/2017/3/2/422f0a51e42540cfb75e421db8775c52.png"},{"subData":[{"name":"咖啡","id":30},{"name":"酒吧","id":31},{"name":"茶楼","id":32},{"name":"密室逃脱","id":33},{"name":"洗浴汗蒸","id":34},{"name":"温泉","id":35},{"name":"中医养生","id":36},{"name":"足疗按摩","id":37}],"name":"休闲娱乐","id":5,"imgSrc":"/upload/sellerTypeIcon/2017/3/2/17c02fbfd922478c943bf3bcf0d5f201.png"},{"subData":[{"name":"酒店","id":38}],"name":"酒店","id":6,"imgSrc":"/upload/sellerTypeIcon/2017/3/2/b47975b8987044539b6a80d3b182aa88.png"},{"subData":[{"name":"美容美体","id":39},{"name":"美发","id":40}],"name":"丽人","id":7,"imgSrc":"/upload/sellerTypeIcon/2017/3/2/ff3033cbba214e5fac25d0d01eb21030.png"},{"subData":[{"name":"汽车维修","id":41},{"name":"衣物清洗","id":42},{"name":"珠宝","id":43}],"name":"生活服务","id":8,"imgSrc":"/upload/sellerTypeIcon/2017/3/2/f24d1628076c4a76aa48c85d4c410ddd.png"},{"subData":[{"name":"网吧","id":44},{"name":"连锁超市","id":45}],"name":"其他","id":9,"imgSrc":"/upload/sellerTypeIcon/2017/3/2/9ede09cd8bf540dc8d0c81b92d8f81db.png"}]
     */

    private int resultCode;
    private String resultDesc;
    private List<SellerCodesBean> sellerCodes;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public List<SellerCodesBean> getSellerCodes() {
        return sellerCodes;
    }

    public void setSellerCodes(List<SellerCodesBean> sellerCodes) {
        this.sellerCodes = sellerCodes;
    }

    public static class SellerCodesBean {
        /**
         * subData : [{"name":"阿萨德01","id":59}]
         * name : 全部
         * id : 50
         * imgSrc : /upload/sellerTypeIcon/2017/3/2/a50f7b2c2ffb4fa8aeb6c9d592ebd7e1.png
         */

        private String name;
        private String id;
        private String imgSrc;
        private List<SubDataBean> subData;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        public List<SubDataBean> getSubData() {
            return subData;
        }

        public void setSubData(List<SubDataBean> subData) {
            this.subData = subData;
        }

        public static class SubDataBean {
            /**
             * name : 阿萨德01
             * id : 59
             */

            private String name;
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }

//    /**
//     * resultCode : 1
//     * resultDesc : 成功
//     * sellerCodes : [{"id":1,"codesName":"美食"},{"id":2,"codesName":"唱歌"},{"id":3,"codesName":"电影"},{"id":4,"codesName":"运动健身"},{"id":5,"codesName":"休闲娱乐"},{"id":6,"codesName":"酒店"},{"id":7,"codesName":"丽人"},{"id":8,"codesName":"生活服务"},{"id":10,"codesName":"小吃快餐"},{"id":11,"codesName":"自助餐"},{"id":12,"codesName":"火锅"},{"id":13,"codesName":"烧烤"},{"id":14,"codesName":"西餐"},{"id":15,"codesName":"冀菜"},{"id":16,"codesName":"日韩菜"},{"id":17,"codesName":"海鲜粤菜"},{"id":18,"codesName":"川湘菜"},{"id":19,"codesName":"东北菜"},{"id":20,"codesName":"清真菜"},{"id":21,"codesName":"特色餐饮"},{"id":22,"codesName":"面包甜品"},{"id":23,"codesName":"其他（北京菜/沪菜）"},{"id":24,"codesName":"Ktv"},{"id":25,"codesName":"电影院"},{"id":26,"codesName":"台球"},{"id":27,"codesName":"健身房"},{"id":28,"codesName":"瑜伽教室"},{"id":29,"codesName":"体育场馆"},{"id":30,"codesName":"咖啡"},{"id":31,"codesName":"酒吧"},{"id":32,"codesName":"茶楼"},{"id":33,"codesName":"密室逃脱"},{"id":34,"codesName":"洗浴汗蒸"},{"id":35,"codesName":"温泉"},{"id":36,"codesName":"中医养生"},{"id":37,"codesName":"足疗按摩"},{"id":38,"codesName":"酒店"},{"id":39,"codesName":"美容美体"},{"id":40,"codesName":"美发"},{"id":41,"codesName":"汽车维修"},{"id":42,"codesName":"衣物清洗"},{"id":43,"codesName":"珠宝"},{"id":9,"codesName":"其他"},{"id":44,"codesName":"网吧"},{"id":45,"codesName":"连锁超市"}]
//     */
//
//    private int resultCode;
//    private String resultDesc;
//    private List<SellerCodesBean> sellerCodes;
//
//    public int getResultCode() {
//        return resultCode;
//    }
//
//    public void setResultCode(int resultCode) {
//        this.resultCode = resultCode;
//    }
//
//    public String getResultDesc() {
//        return resultDesc;
//    }
//
//    public void setResultDesc(String resultDesc) {
//        this.resultDesc = resultDesc;
//    }
//
//    public List<SellerCodesBean> getSellerCodes() {
//        return sellerCodes;
//    }
//
//    public void setSellerCodes(List<SellerCodesBean> sellerCodes) {
//        this.sellerCodes = sellerCodes;
//    }
//
//    public static class SellerCodesBean {
//        /**
//         * id : 1
//         * codesName : 美食
//         */
//
//        private int id;
//        private String codesName;
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getCodesName() {
//            return codesName;
//        }
//
//        public void setCodesName(String codesName) {
//            this.codesName = codesName;
//        }
//    }

}
