package zhangjingfeng.com.zproject.bean.jsonbean;

/**
 * Created by Administrator on 2018/2/6.
 */

public class UpdateAppInfoBean {

    /**
     * data : {"appname":"hoolay.apk","serverVersion":"1.0.2","serverFlag":"1","lastForce":"1","updateurl":"http://releases.b0.upaiyun.com/hoolay.apk","upgradeinfo":"V1.0.2版本更新，你想不想要试一下哈！！！"}
     * error_code : 200
     * error_msg : 蛋疼的认识
     */

    private DataBean data;
    private String error_code;
    private String error_msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public static class DataBean {
        /**
         * appname : hoolay.apk
         * serverVersion : 1.0.2
         * serverFlag : 1
         * lastForce : 1
         * updateurl : http://releases.b0.upaiyun.com/hoolay.apk
         * upgradeinfo : V1.0.2版本更新，你想不想要试一下哈！！！
         */

        private String appname;
        private String serverVersion;
        private String serverFlag;
        private String lastForce;
        private String updateurl;
        private String upgradeinfo;

        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getServerVersion() {
            return serverVersion;
        }

        public void setServerVersion(String serverVersion) {
            this.serverVersion = serverVersion;
        }

        public String getServerFlag() {
            return serverFlag;
        }

        public void setServerFlag(String serverFlag) {
            this.serverFlag = serverFlag;
        }

        public String getLastForce() {
            return lastForce;
        }

        public void setLastForce(String lastForce) {
            this.lastForce = lastForce;
        }

        public String getUpdateurl() {
            return updateurl;
        }

        public void setUpdateurl(String updateurl) {
            this.updateurl = updateurl;
        }

        public String getUpgradeinfo() {
            return upgradeinfo;
        }

        public void setUpgradeinfo(String upgradeinfo) {
            this.upgradeinfo = upgradeinfo;
        }
    }
}
