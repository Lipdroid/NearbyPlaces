package com.lipu.findnearbyplacesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsValues {

	private SharedPreferences mPrefs;

	public PrefsValues(Context context) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public SharedPreferences getPrefs() {
		return mPrefs;
	}

	public String getUserlat() {
		return mPrefs.getString("Userlat", "");
	}

	public void setUserlat(String firstname) {
		mPrefs.edit().putString("Userlat", firstname).commit();
	}

	public String getUserlng() {
		return mPrefs.getString("Userlng", "");
	}

	public void setUserlng(String lastname) {
		mPrefs.edit().putString("Userlng", lastname).commit();
	}

	public String getDeslat() {

		return mPrefs.getString("Deslat", "");
	}

	public void setDeslat(String email) {
		mPrefs.edit().putString("Deslat", email).commit();
	}

	public String getDeslng() {
		return mPrefs.getString("Deslng", "");
	}

	public void setDeslng(String dob) {
		mPrefs.edit().putString("Deslng", dob).commit();
	}

	public String getName() {
		return mPrefs.getString("Name", "");
	}

	public void setName(String name) {
		mPrefs.edit().putString("Name", name).commit();
	}

	public String getaddress() {
		return mPrefs.getString("address", "");
	}

	public void setaddress(String address) {
		mPrefs.edit().putString("address", address).commit();
	}

}
