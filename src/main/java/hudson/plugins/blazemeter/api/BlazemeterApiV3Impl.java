package hudson.plugins.blazemeter.api;

import hudson.plugins.blazemeter.api.urlmanager.BmUrlManager;
import hudson.plugins.blazemeter.api.urlmanager.BmUrlManagerV3Impl;
import hudson.plugins.blazemeter.api.urlmanager.URLFactory;
import hudson.plugins.blazemeter.entities.TestInfo;
import hudson.plugins.blazemeter.entities.TestStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * User: Vitali
 * Date: 4/2/12
 * Time: 14:05
 * <p/>
 * Updated
 * User: Doron
 * Date: 8/7/12
 * <p/>
 * Updated (proxy)
 * User: Marcel
 * Date: 9/23/13
 */

public class BlazemeterApiV3Impl implements BlazemeterApi {

    private final String apiKey;
    BmUrlManager urlManager;
    private BZMHTTPClient bzmhc = null;

    BlazemeterApiV3Impl(String apiKey) {
        this.apiKey = apiKey;
        urlManager = URLFactory.getURLFactory().
                getURLManager(URLFactory.ApiVersion.v3, "https://a.blazemeter.com");
        try {
            bzmhc = BZMHTTPClient.getInstance();
            bzmhc.configureProxy();
        } catch (Exception ex) {
            logger.format("error Instantiating HTTPClient. Exception received: %s", ex);
        }
    }


    /**
     * @param testId - test id
     * @param file   - jmx file
     *               //     * @return test id
     *               //     * @throws java.io.IOException
     *               //     * @throws org.json.JSONException
     */
    public synchronized void uploadJmx(String testId, File file) {

        if (!validate(apiKey, testId)) return;

        String url = this.urlManager.scriptUpload(APP_KEY, apiKey, testId, file.getName());
        JSONObject json = this.bzmhc.getJsonForFileUpload(url, file);
        try {
            if (!json.get("response_code").equals(200)) {
                logger.println("Could not upload file " + file.getName() + " " + json.get("error").toString());
            }
        } catch (JSONException e) {
            logger.println("Could not upload file " + file.getName() + " " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param testId - test id
     * @param file   - the file (Java class) you like to upload
     * @return test id
     * //     * @throws java.io.IOException
     * //     * @throws org.json.JSONException
     */

    public synchronized JSONObject uploadBinaryFile(String testId, File file) {

        if (!validate(apiKey, testId)) return null;

        String url = this.urlManager.fileUpload(APP_KEY, apiKey, testId, file.getName());

        return this.bzmhc.getJsonForFileUpload(url, file);
    }


    public TestInfo getTestRunStatus(String testId) {
        TestInfo ti = new TestInfo();

        if (!validate(apiKey, testId)) {
            ti.setStatus(TestStatus.NotFound);
            return ti;
        }

        try {
            String url = this.urlManager.testStatus(APP_KEY, apiKey, testId);
            JSONObject jo = this.bzmhc.getJson(url, null, BZMHTTPClient.Method.GET);
            JSONObject result = (JSONObject) jo.get("result");
            if (result.get("dataUrl") == null) {
                ti.setStatus(TestStatus.NotFound);
            } else {
                ti.setId(result.getString("testId"));
                ti.setName(result.getString("name"));
                if (!result.getString("status").equals("ENDED")) {
                    ti.setStatus(TestStatus.Running);
                } else {
                    ti.setStatus(TestStatus.NotRunning);
                }
            }
        } catch (Exception e) {
            logger.println("error getting status " + e);
            ti.setStatus(TestStatus.Error);
        }
        return ti;
    }

    public synchronized JSONObject startTest(String testId) {

        if (!validate(apiKey, testId)) return null;

        String url = this.urlManager.testStart(APP_KEY, apiKey, testId);
        return this.bzmhc.getJson(url, null, BZMHTTPClient.Method.POST);
    }

    public int getTestCount() throws JSONException, IOException, ServletException {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.println("getTests apiKey is empty");
            return 0;
        }

        String url = this.urlManager.getTests(APP_KEY, apiKey);

        try {
            JSONObject jo = this.bzmhc.getJson(url, null, BZMHTTPClient.Method.GET);
            if (jo == null) {
                return -1;
            } else {
                JSONArray result = (JSONArray) jo.get("result");
                if (result.length() == 0) {
                    return 0;
                } else {
                    return result.length();
                }
            }
        } catch (JSONException e) {
            logger.println("Error getting response from server: ");
            e.printStackTrace();
            return -1;
        }
    }


    private boolean validate(String apiKey, String testId) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.println("startTest apiKey is empty");
            return false;
        }

        if (testId == null || testId.trim().isEmpty()) {
            logger.println("testId is empty");
            return false;
        }
        return true;
    }

    /**
     * @param testId - test id
     *               //     * @throws IOException
     *               //     * @throws ClientProtocolException
     */
    public JSONObject stopTest(String testId) {
        if (!validate(apiKey, testId)) return null;

        String url = this.urlManager.testStop(APP_KEY, apiKey, testId);
        return this.bzmhc.getJson(url, null, BZMHTTPClient.Method.GET);
    }

    /**
     * @param reportId - report Id same as Session Id, can be obtained from start stop status.
     *                 //     * @throws IOException
     *                 //     * @throws ClientProtocolException
     */
    public JSONObject testReport(String reportId) {
        if (!validate(apiKey, reportId)) return null;

        String url = this.urlManager.testReport(APP_KEY, apiKey, reportId);
        JSONObject summary = null;
        try {
            summary = (JSONObject) this.bzmhc.getJson(url, null, BZMHTTPClient.Method.GET).getJSONObject("result")
                    .getJSONArray("summary")
                    .get(0);
        } catch (JSONException je) {
            logger.println("Error while parsing summary for V3 API:" + je);
        }
        return summary;
    }

    public HashMap<String, String> getTestList() throws IOException, MessagingException {

        LinkedHashMap<String, String> testListOrdered = null;

        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.println("getTests apiKey is empty");
        } else {
            String url = this.urlManager.getTests(APP_KEY, apiKey);
            logger.println(url);
            JSONObject jo = this.bzmhc.getJson(url, null, BZMHTTPClient.Method.GET);
            try {
                JSONArray result = (JSONArray) jo.get("result");
                if (result != null && result.length() > 0) {
                    testListOrdered = new LinkedHashMap<String, String>(result.length());
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject en = null;
                        try {
                            en = result.getJSONObject(i);
                        } catch (JSONException e) {
                            logger.println("Error with the JSON while populating test list, " + e);
                        }
                        String id;
                        String name;
                        try {
                            if (en != null) {
                                id = en.getString("id");
                                name = en.getString("name").replaceAll("&", "&amp;");
                                testListOrdered.put(name, id);

                            }
                        } catch (JSONException ie) {
                            logger.println("Error with the JSON while populating test list, " + ie);
                        }
                    }
                }
            } catch (Exception e) {
                logger.println("Error while populating test list, " + e);
            }
        }

        return testListOrdered;
    }


    @Override
    public JSONObject getUser() {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.println("User apiKey is empty");
            return null;
        }
        String url = this.urlManager.getUser(APP_KEY, apiKey);
        JSONObject jo = this.bzmhc.getJson(url, null, BZMHTTPClient.Method.GET);
        return jo;
    }

    public JSONObject getTestInfo(String testId){
        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.println("User apiKey is empty");
            return null;
        }
        String url = ((BmUrlManagerV3Impl)this.urlManager).getTestInfo(APP_KEY, apiKey,testId);
        JSONObject jo = this.bzmhc.getJson(url, null, BZMHTTPClient.Method.GET);
        return jo;
    }
}
