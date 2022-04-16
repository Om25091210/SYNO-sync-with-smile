package in.aryomtech.syno.Model;

public class post_data {

    private String image_link;
    private String title;
    private String body;
    private String date;
    private String seen;
    private String category;
    private String city;
    private String pushkey;
    private String uid;
    private String link;

    public post_data(String image_link, String title, String body, String date, String seen, String category, String city, String pushkey, String uid, String link) {
        this.image_link = image_link;
        this.title = title;
        this.body = body;
        this.date = date;
        this.seen = seen;
        this.category = category;
        this.city = city;
        this.pushkey = pushkey;
        this.uid = uid;
        this.link = link;
    }

    public post_data() {
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPushkey(String pushkey) {
        this.pushkey = pushkey;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }

    public String getSeen() {
        return seen;
    }

    public String getCategory() {
        return category;
    }

    public String getCity() {
        return city;
    }

    public String getPushkey() {
        return pushkey;
    }

    public String getUid() {
        return uid;
    }

    public String getLink() {
        return link;
    }
}
