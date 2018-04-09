package zhangjingfeng.com.zproject.bean.jsonbean;

/**
 * Created by Administrator on 2018/2/5.
 */

public class LoginInfoBean {

    /**
     * userId : 4
     * userName : 13517916851
     * errnum : 0
     * sessionId : 8766982BEEF99882B9D050B2811C978F
     * uuid : 473d1f924ffd4cfebdc3882a5927890f
     */

    private int userId;
    private String userName;
    private int errnum;
    private String sessionId;
    private String uuid;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getErrnum() {
        return errnum;
    }

    public void setErrnum(int errnum) {
        this.errnum = errnum;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
