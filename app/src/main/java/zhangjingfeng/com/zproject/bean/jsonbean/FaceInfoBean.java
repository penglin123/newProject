package zhangjingfeng.com.zproject.bean.jsonbean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/2.
 */

public class FaceInfoBean implements Serializable {

    /**
     * errnum : -13
     * errmsg : 验证失败，请重试或者使用用户名和密码登录
     */

    private int errnum;
    private String errmsg;

    public int getErrnum() {
        return errnum;
    }

    public void setErrnum(int errnum) {
        this.errnum = errnum;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
