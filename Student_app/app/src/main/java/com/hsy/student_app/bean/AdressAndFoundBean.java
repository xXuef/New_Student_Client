package com.hsy.student_app.bean;

import java.util.List;



/**
 *  @Data 2019/4/24 10:38
 *   解析数据用的bean类
 */
public class AdressAndFoundBean {

    private List<AdressAllBean> adressAll;

    public List<AdressAllBean> getAdressAll() {
        return adressAll;
    }

    public void setAdressAll(List<AdressAllBean> adressAll) {
        this.adressAll = adressAll;
    }

    public static class AdressAllBean {
        /**
         * id : 09
         * name : 阮子洋
         * phone : 531231513
         * imgUrl : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg
         * jobs : ["学生"]
         * comment : [{"data":"2019-02-03","time":"12:21","star":3,"content":"内容内容内容内容内容内容内容内容内容内容","imgUrl":["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg"]},{"data":"2018-01-03","time":"11:11","star":1,"content":"内容内容内容内容内容内容内容内容内容内容","imgUrl":["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg"]}]
         */

        private String id;
        private String name;
        private String phone;
        private String imgUrl;
        private List<String> jobs;
        private List<CommentBean> comment;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public List<String> getJobs() {
            return jobs;
        }

        public void setJobs(List<String> jobs) {
            this.jobs = jobs;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
        }

        public static class CommentBean {
            /**
             * data : 2019-02-03
             * time : 12:21
             * star : 3
             * content : 内容内容内容内容内容内容内容内容内容内容
             * imgUrl : ["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555759022420&di=031f8484fad4c070ffcabb04a94582b3&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F20%2F20150820004359_Qzaxt.jpeg"]
             */

            private String data;
            private String time;
            private int star;
            private String content;
            private List<String> imgUrl;
            boolean starIsClick;

            public boolean isStarIsClick() {
                return starIsClick;
            }

            public void setStarIsClick(boolean starIsClick) {
                this.starIsClick = starIsClick;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getStar() {
                return star;
            }

            public void setStar(int star) {
                this.star = star;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public List<String> getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(List<String> imgUrl) {
                this.imgUrl = imgUrl;
            }
        }
    }
}
