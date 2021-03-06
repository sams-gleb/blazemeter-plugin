/**
 Copyright 2016 BlazeMeter Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package hudson.plugins.blazemeter;

import com.cloudbees.plugins.credentials.BaseCredentials;
import com.cloudbees.plugins.credentials.CredentialsDescriptor;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import hudson.Extension;
import hudson.plugins.blazemeter.utils.Constants;
import hudson.plugins.blazemeter.utils.JobUtility;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import java.io.IOException;

public class BlazemeterCredentialImpl extends BaseCredentials implements StandardCredentials {
    private static final long serialVersionUID = 1L;

    private String apiKey =null;
    private String description=null;

    @DataBoundConstructor
    public BlazemeterCredentialImpl(String apiKey,String description) {
        super(CredentialsScope.GLOBAL);
        this.apiKey=apiKey;
        this.description=description;
    }

    public String getId() {
        return StringUtils.left(apiKey,4) + Constants.THREE_DOTS + StringUtils.right(apiKey, 4);
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getDescription() {
        return description;
    }


    @Extension
    public static class DescriptorImpl extends CredentialsDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.BlazemeterCredential_DisplayName();
        }

        @Override
        public ListBoxModel doFillScopeItems() {
            ListBoxModel m = new ListBoxModel();
            m.add(CredentialsScope.GLOBAL.getDisplayName(), CredentialsScope.GLOBAL.toString());
            return m;
        }

        // Used by global.jelly to authenticate User key
        public FormValidation doTestConnection(@QueryParameter("apiKey") final String userKey) throws MessagingException, IOException, JSONException, ServletException {
            BlazeMeterPerformanceBuilderDescriptor descriptor=BlazeMeterPerformanceBuilderDescriptor.getDescriptor();
            return  JobUtility.validateUserKey(userKey,descriptor.getBlazeMeterURL());
        }

    }
}