package zhangjingfeng.com.zproject.fragment;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.support.annotation.Nullable;


import java.util.Set;

import zhangjingfeng.com.zproject.R;

/**
 * Created by Administrator on 2017/12/8.
 */

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private EditTextPreference et_pre;
    private ListPreference list_pre;
    private MultiSelectListPreference msl_pre;
    private RingtonePreference rt_pre;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        initPreferences();
    }

    private void initPreferences() {
        et_pre= (EditTextPreference) findPreference("et_pre");
        list_pre=(ListPreference)findPreference("list_pre");
        msl_pre=(MultiSelectListPreference)findPreference("msl_pre");
        rt_pre=(RingtonePreference)findPreference("rt_pre");
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences=getPreferenceScreen().getSharedPreferences();
        //显示的值
        et_pre.setSummary(sharedPreferences.getString("et_pre","无"));
        list_pre.setSummary(sharedPreferences.getString("list_pre","无"));
        rt_pre.setSummary(getRingtoneName(Uri.parse(sharedPreferences.getString("rt_pre","无"))));
        msl_pre.setSummary(getMultiSelectString(sharedPreferences,"msl_pre"));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    //获取多选字符串
    private String getMultiSelectString(SharedPreferences sharedPreferences,String key) {
        Set<String> set=sharedPreferences.getStringSet(key,null);
        StringBuilder stringBuilder=new StringBuilder();
        if(set!=null){
            for (String str:set){
                stringBuilder.append(str+" ");
            }
        }else
            return "";
        return stringBuilder.toString();
    }

    // 获取提示音名称
    public String getRingtoneName(Uri uri) {
        Ringtone r = RingtoneManager.getRingtone(getActivity(), uri);
        return r.getTitle(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("et_pre")){
            et_pre.setSummary(sharedPreferences.getString("et_pre","无"));
        }else if (s.equals("list_pre")){
            list_pre.setSummary(sharedPreferences.getString("list_pre","无"));
        }else if (s.equals("rt_pre")){
            rt_pre.setSummary(sharedPreferences.getString("rt_pre","无"));
        }else if (s.equals("msl_pre")){
            msl_pre.setSummary(getMultiSelectString(sharedPreferences,"msl_pre"));
        }
    }
}
