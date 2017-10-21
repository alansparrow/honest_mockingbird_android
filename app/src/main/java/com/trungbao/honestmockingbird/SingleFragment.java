package com.trungbao.honestmockingbird;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.trungbao.honestmockingbird.helper.NetworkRequester;

/**
 * Created by baotrungtn on 10/22/17.
 */

public class SingleFragment extends Fragment {
    private final String TAG = "SingleFragment";



    private class GetNewUserTokenTask extends AsyncTask<Void,Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return new NetworkRequester().getNewUserToken();
        }

        @Override
        protected void onPostExecute(String userToken) {
            super.onPostExecute(userToken);
            SharedInfo.setUserToken(userToken);
            Log.i(TAG, "GetNewUserTokenTask: get new user token: " + userToken);
        }
    }
}
