package com.hsy.student_app.bean;

import java.util.List;

public class Comment {

    String name;
    List<String> jobs;
    String imgUrl;
    String data;
    String time;
    String content;
    int star;
    boolean starIsClick;

    public boolean isStarIsClick() {
        return starIsClick;
    }

    public void setStarIsClick(boolean starIsClick) {
        this.starIsClick = starIsClick;
    }

    List<String> imageViewList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getJobs() {
        return jobs;
    }

    public void setJobs(List<String> jobs) {
        this.jobs = jobs;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<String> getImageViewList() {
        return imageViewList;
    }

    public void setImageViewList(List<String> imageViewList) {
        this.imageViewList = imageViewList;
    }
}
