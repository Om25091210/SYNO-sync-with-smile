package in.aryomtech.syno.data;


public class ModelSmoolider {

    private String image_url;
    private String head_text;
    private String des_text;
    private String pushkey;
    private String deep_link;

    public ModelSmoolider() {
    }

    public String getDeep_link() {
        return deep_link;
    }

    public void setDeep_link(String deep_link) {
        this.deep_link = deep_link;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getHead_text() {
        return head_text;
    }

    public void setHead_text(String head_text) {
        this.head_text = head_text;
    }

    public String getDes_text() {
        return des_text;
    }

    public void setDes_text(String des_text) {
        this.des_text = des_text;
    }

    public String getPushkey() {
        return pushkey;
    }

    public void setPushkey(String pushkey) {
        this.pushkey = pushkey;
    }
}
