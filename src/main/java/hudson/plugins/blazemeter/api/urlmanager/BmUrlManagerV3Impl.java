package hudson.plugins.blazemeter.api.urlmanager;

import hudson.plugins.blazemeter.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by dzmitrykashlach on 10/11/14.
 */
public class BmUrlManagerV3Impl implements BmUrlManager {

    private String serverUrl = "";
    BmUrlManagerV3Impl(String blazeMeterUrl) {
        this.serverUrl = blazeMeterUrl;
    }

    @Override
    public String getServerUrl() {
        return serverUrl;
    }

    @Override
    public String testMasterStatus(String appKey, String userKey, String masterId) {
        String testStatus=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
            masterId = URLEncoder.encode(masterId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        testStatus= serverUrl +"/api/latest/masters/"+masterId+"/status?events=false&api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;
         return testStatus;
    }

    @Override
    public String getTests(String appKey, String userKey) {
        String getTests=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getTests= serverUrl +"/api/web/tests?api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return getTests;
    }

    @Override
    public String scriptUpload(String appKey, String userKey, String testId, String fileName) {
    return Constants.NOT_IMPLEMENTED;
    }

    @Override
    public String fileUpload(String appKey, String userKey, String testId, String fileName) {
        return Constants.NOT_IMPLEMENTED;
    }

    @Override
    public String testStart(String appKey, String userKey, String testId) {
        String testStart=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
            testId = URLEncoder.encode(testId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        testStart= serverUrl +"/api/latest/tests/"
                +testId+"/start?api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return testStart;
    }

    @Override
    public String collectionStart(String appKey, String userKey, String collectionId) {
        String testStart=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
            collectionId = URLEncoder.encode(collectionId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        testStart= serverUrl +"/api/latest/collections/"
                +collectionId+"/start?api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return testStart;
    }

    @Override
    public String testStop(String appKey, String userKey, String masterId) {
         String testStop=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
            masterId = URLEncoder.encode(masterId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        testStop= serverUrl +"/api/latest/masters/"
                +masterId+"/stop?api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return testStop;
    }

    @Override
    public String testTerminate(String appKey, String userKey, String masterId) {
        String testTerminate=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
            masterId = URLEncoder.encode(masterId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        testTerminate= serverUrl +"/api/latest/masters/"
                +masterId+"/terminate?api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return testTerminate;
    }

    @Override
    public String testReport(String appKey, String userKey, String masterId) {
        String testAggregateReport=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
            masterId = URLEncoder.encode(masterId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        testAggregateReport= serverUrl +"/api/latest/masters/"
                +masterId+"/reports/main/summary?api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return testAggregateReport;
    }

    @Override
    public String getUser(String appKey, String userKey) {
        String getUser=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getUser= serverUrl +"/api/latest/user?api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return getUser;
    }

    @Override
    public String getTestConfig(String appKey, String userKey, String testId){
        String getTestInfo=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getTestInfo= serverUrl +"/api/latest/tests/"+testId+"?api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return getTestInfo;
    }

    @Override
    public String createTest(String appKey, String userKey) {
        String createTest=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        createTest= serverUrl +"/api/latest/tests/custom?custom_test_type=yahoo&api_key="
                +userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return createTest;
    }

    @Override
    public String getCIStatus(String appKey, String userKey, String masterId){
        String getTresholds=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getTresholds= serverUrl +"/api/latest/masters/"+masterId+"/ci-status?api_key="
                +userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return getTresholds;

    }

    @Override
    public void setServerUrl(String serverUrl) {
        this.serverUrl =serverUrl;
    }

    @Override
    public String retrieveJUNITXML(String appKey, String userKey, String masterId) {
        String retrieveJUNITXML=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        retrieveJUNITXML= serverUrl +"/api/latest/masters/"+masterId+
                "/reports/thresholds?format=junit&api_key="
                +userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return retrieveJUNITXML;
    }



    @Override
    public String postJsonConfig(String appKey, String userKey, String testId) {
        String getTestInfo=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getTestInfo= serverUrl +"/api/latest/tests/"+testId+"/custom?custom_test_type=yahoo&api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return getTestInfo;
    }

    @Override
    public String retrieveJTLZIP(String appKey, String userKey, String sessionId) {
        String retrieveJTLZIP=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        retrieveJTLZIP= serverUrl +"/api/latest/sessions/"+sessionId+
                "/reports/logs?api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return retrieveJTLZIP;
    }

    @Override
    public String generatePublicToken(String appKey, String userKey, String masterId) {
        String generatePublicToken=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        generatePublicToken= serverUrl +"/api/latest/masters/"+masterId+
                "/publicToken?api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return generatePublicToken;
    }


    @Override
    public String listOfSessionIds(String appKey, String userKey, String masterId) {
        String listOfSessionIds=null;
        try {
            appKey = URLEncoder.encode(appKey, "UTF-8");
            userKey = URLEncoder.encode(userKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        listOfSessionIds= serverUrl +"/api/latest/masters/"+masterId+
                "/sessions?api_key="+userKey+"&app_key="+appKey+ CLIENT_IDENTIFICATION;

        return listOfSessionIds;
    }
}

