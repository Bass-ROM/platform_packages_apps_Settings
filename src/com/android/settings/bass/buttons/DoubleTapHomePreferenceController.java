/*
 * Copyright (C) 2017 CypherOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.android.settings.bass.buttons;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.UserHandle;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.core.PreferenceController;

import static android.provider.Settings.System.KEY_HOME_DOUBLE_TAP_ACTION;

public class DoubleTapHomePreferenceController extends PreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "DoubleTapHomePref";
  
    private final String mDoubleTapHomeKey;
  
    private ListPreference mDoubleTapHome;

    public DoubleTapHomePreferenceController(Context context, String key) {
        super(context);
        mDoubleTapHomeKey = key;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getPreferenceKey() {
        return mDoubleTapHomeKey;
    }

    @Override
    public void updateState(Preference preference) {
        final ListPreference mDoubleTapHome = (ListPreference) preference;
        final Resources res = mContext.getResources();
        if (mDoubleTapHome != null) {
            int defaultDoubleTapOnHomeKeyBehavior = res.getInteger(
                    com.android.internal.R.integer.config_doubleTapOnHomeKeyBehavior);
            int doubleTapOnHomeKeyBehavior = Settings.System.getIntForUser(mContext.getContentResolver(),
                    Settings.System.KEY_HOME_DOUBLE_TAP_ACTION,
                    defaultDoubleTapOnHomeKeyBehavior,
                    UserHandle.USER_CURRENT);
            String homeKey = String.valueOf(doubleTapOnHomeKeyBehavior);
            mDoubleTapHome.setValue(homeKey);
            updateDoubleTapHomeSummary(mDoubleTapHome, homeKey);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        try {
            String homeKey = (String) newValue;
            Settings.System.putIntForUser(mContext.getContentResolver(), Settings.System.KEY_HOME_DOUBLE_TAP_ACTION,
                    Integer.parseInt(homeKey), UserHandle.USER_CURRENT);
            updateDoubleTapHomeSummary((ListPreference) preference, homeKey);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Could not persist screenshot mode setting", e);
        }
        return true;
    }

    private void updateDoubleTapHomeSummary(Preference mDoubleTapHome, String homeKey) {
        if (homeKey != null) {
            String[] values = mContext.getResources().getStringArray(R.array
                    .action_values);
            final int summaryArrayResId = R.array.action_entries;
            String[] summaries = mContext.getResources().getStringArray(summaryArrayResId);
            for (int i = 0; i < values.length; i++) {
                if (homeKey.equals(values[i])) {
                    if (i < summaries.length) {
                        mDoubleTapHome.setSummary(summaries[i]);
                        return;
                    }
                }
            }
        }

        mDoubleTapHome.setSummary("");
        Log.e(TAG, "Invalid double tap value: " + homeKey);
    }
}
