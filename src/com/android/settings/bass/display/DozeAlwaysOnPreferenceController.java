/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.bass.display;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;

import com.android.settings.core.PreferenceController;

import static android.provider.Settings.Secure.DOZE_ALWAYS_ON;

public class DozeAlwaysOnPreferenceController extends PreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_DOZE_ALWAYS_ON = "doze_always_on";

    public DozeAlwaysOnPreferenceController(Context context) {
        super(context);
    }

    @Override
    public boolean isAvailable() {
        return mContext.getResources().getBoolean(com.android.internal.R.bool.config_dozeAlwaysOn);
    }

    @Override
    public String getPreferenceKey() {
        return KEY_DOZE_ALWAYS_ON;
    }

    @Override
    public void updateState(Preference preference) {
        int value = Settings.Secure.getInt(mContext.getContentResolver(), DOZE_ALWAYS_ON, 0);
        ((SwitchPreference) preference).setChecked(value != 0);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean enabled = (boolean) newValue;
        Settings.Secure.putInt(mContext.getContentResolver(),
                Settings.Secure.DOZE_ALWAYS_ON, enabled ? 1 : 0);
        return true;
    }
}
