package com.hsy.student_app.bean;


import com.nanchen.wavesidebar.FirstLetterUtil;

import java.util.List;

/**
 * 总的实体类
 */

public class ContactModel {
    private String index;
    private int id;
    private String name;
    private String phone;
    private List<String> jobs;
    private String imgUrl;

    private List<Comment> list;

    public ContactModel() {
    }

    public ContactModel(int id, String name, String phone, List<String> jobs, String imgUrl, List<Comment> list) {
        this.index = FirstLetterUtil.getFirstLetter(name);
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.jobs = jobs;
        this.imgUrl = imgUrl;
        this.list = list;
    }

    public List<Comment> getList() {
        return list;
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
