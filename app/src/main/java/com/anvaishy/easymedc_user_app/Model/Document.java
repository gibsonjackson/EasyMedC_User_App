package com.anvaishy.easymedc_user_app.Model;

public class Document {
    String docname;
    String docurl;
    public Document() {}//Left blank for firebase to fill wrapper code
    public String getDocname() {
        return docname;
    }
    public void setDocname(String docname) {
        this.docname = docname;
    }
    public String getDocurl() {
        return docurl;
    }
    public void setDocurl(String docurl) {
        this.docurl = docurl;
    }
}
