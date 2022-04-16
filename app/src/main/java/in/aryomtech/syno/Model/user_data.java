package in.aryomtech.syno.Model;

public class user_data {

    private String name;
    private String uid;
    private String profile_emoji;
    private String branch;
    private String semester;
    private String roll_no;

    public user_data() {
    }

    public user_data(String name, String uid, String profile_emoji, String branch, String semester, String roll_no) {
        this.name = name;
        this.uid = uid;
        this.profile_emoji = profile_emoji;
        this.branch = branch;
        this.semester = semester;
        this.roll_no = roll_no;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getProfile_emoji() {
        return profile_emoji;
    }

    public String getBranch() {
        return branch;
    }

    public String getSemester() {
        return semester;
    }

    public String getRoll_no() {
        return roll_no;
    }
}
