package com.acomp.khobarapp.model;

import java.util.ArrayList;

public class NewsModel {
    private String title = "";
    private String strDate = "";
    private String content;
    private String code;
    private ArrayList<AttachmentModel> attachmentModels = new ArrayList<AttachmentModel>();

    public ArrayList<AttachmentModel> getAttachmentModels() {
        return attachmentModels;
    }

    public void setAttachmentModels(ArrayList<AttachmentModel> attachmentModels) {
        this.attachmentModels = attachmentModels;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
