package com.hackmython.wya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
//import android.preference.SwitchPreference;

public class Preferences extends PreferenceActivity {
int nextTrigger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPreferenceScreen(createPreferenceHierarchy());
    }

    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
        
      // Security preferences
      PreferenceCategory launchPrefCat = new PreferenceCategory(this);
      launchPrefCat.setTitle("Triggers");
      root.addPreference(launchPrefCat);
      
      // Intent preference
      Intent launchPreferencesIntent = new Intent().setClass(this, WhereYouApp.class);
      launchPreferencesIntent.setAction("CHANGEPASS");
      
      PreferenceScreen intentPref = getPreferenceManager().createPreferenceScreen(this);
      intentPref.setIntent(launchPreferencesIntent);
      intentPref.setTitle("Change Master Password");
      intentPref.setSummary("Anyone with this trigger can locate you");
      launchPrefCat.addPreference(intentPref);
      
      
      // trigger preferences
      PreferenceCategory triggersPrefCat = new PreferenceCategory(this);
      triggersPrefCat.setTitle("Triggers:\nAuto-respond to incoming texts including these phrases");
      root.addPreference(triggersPrefCat);
      
  	SharedPreferences triggersFile = this.getSharedPreferences(
			WhereYouApp.TRIGGERS_KEY, 0);
  	
  	
	  // Edit text preference
  	
//	  EditTextPreference editTextPref = new EditTextPreference(this);
//	  editTextPref.setDialogTitle("Enter a new trigger");
//	  editTextPref.setKey("newtrigger");
//	  editTextPref.setTitle("Add new trigger");
//	  editTextPref.setSummary("");
//	  triggersPrefCat.addPreference(editTextPref);
  	
	int x = 0;
  	for(Object i : triggersFile.getAll().values()){
	    // Checkbox preference
	    CheckBoxPreference checkboxPref = new CheckBoxPreference(this);
	    checkboxPref.setKey(x+"");
	    checkboxPref.setTitle((String)i);
	    checkboxPref.setSummary("Check to enable");
	    triggersPrefCat.addPreference(checkboxPref);
	    x++;
  	}

//        // Inline preferences
//        PreferenceCategory inlinePrefCat = new PreferenceCategory(this);
//        inlinePrefCat.setTitle(R.string.inline_preferences);
//        root.addPreference(inlinePrefCat);
//
//        // Checkbox preference
//        CheckBoxPreference checkboxPref = new CheckBoxPreference(this);
//        checkboxPref.setKey("checkbox_preference");
//        checkboxPref.setTitle(R.string.title_checkbox_preference);
//        checkboxPref.setSummary(R.string.summary_checkbox_preference);
//        inlinePrefCat.addPreference(checkboxPref);
//
//        // Switch preference
//        SwitchPreference switchPref = new SwitchPreference(this);
//        switchPref.setKey("switch_preference");
//        switchPref.setTitle(R.string.title_switch_preference);
//        switchPref.setSummary(R.string.summary_switch_preference);
//        inlinePrefCat.addPreference(switchPref);
//
//        // Dialog based preferences
//        PreferenceCategory dialogBasedPrefCat = new PreferenceCategory(this);
//        dialogBasedPrefCat.setTitle(R.string.dialog_based_preferences);
//        root.addPreference(dialogBasedPrefCat);
//
//        // Edit text preference
//        EditTextPreference editTextPref = new EditTextPreference(this);
//        editTextPref.setDialogTitle(R.string.dialog_title_edittext_preference);
//        editTextPref.setKey("edittext_preference");
//        editTextPref.setTitle(R.string.title_edittext_preference);
//        editTextPref.setSummary(R.string.summary_edittext_preference);
//        dialogBasedPrefCat.addPreference(editTextPref);
//
//        // List preference
//        ListPreference listPref = new ListPreference(this);
//        listPref.setEntries(R.array.entries_list_preference);
//        listPref.setEntryValues(R.array.entryvalues_list_preference);
//        listPref.setDialogTitle(R.string.dialog_title_list_preference);
//        listPref.setKey("list_preference");
//        listPref.setTitle(R.string.title_list_preference);
//        listPref.setSummary(R.string.summary_list_preference);
//        dialogBasedPrefCat.addPreference(listPref);
//
//        // Launch preferences
//        PreferenceCategory launchPrefCat = new PreferenceCategory(this);
//        launchPrefCat.setTitle(R.string.launch_preferences);
//        root.addPreference(launchPrefCat);
//
//        /*
//         * The Preferences screenPref serves as a screen break (similar to page
//         * break in word processing). Like for other preference types, we assign
//         * a key here so that it is able to save and restore its instance state.
//         */
//        // Screen preference
//        PreferenceScreen screenPref = getPreferenceManager().createPreferenceScreen(this);
//        screenPref.setKey("screen_preference");
//        screenPref.setTitle(R.string.title_screen_preference);
//        screenPref.setSummary(R.string.summary_screen_preference);
//        launchPrefCat.addPreference(screenPref);
//
//        /*
//         * You can add more preferences to screenPref that will be shown on the
//         * next screen.
//         */
//
//        // Example of next screen toggle preference
//        CheckBoxPreference nextScreenCheckBoxPref = new CheckBoxPreference(this);
//        nextScreenCheckBoxPref.setKey("next_screen_toggle_preference");
//        nextScreenCheckBoxPref.setTitle(R.string.title_next_screen_toggle_preference);
//        nextScreenCheckBoxPref.setSummary(R.string.summary_next_screen_toggle_preference);
//        screenPref.addPreference(nextScreenCheckBoxPref);
//
//        // Intent preference
//        PreferenceScreen intentPref = getPreferenceManager().createPreferenceScreen(this);
//        intentPref.setIntent(new Intent().setAction(Intent.ACTION_VIEW)
//                .setData(Uri.parse("http://www.android.com")));
//        intentPref.setTitle(R.string.title_intent_preference);
//        intentPref.setSummary(R.string.summary_intent_preference);
//        launchPrefCat.addPreference(intentPref);
//
//        // Preference attributes
//        PreferenceCategory prefAttrsCat = new PreferenceCategory(this);
//        prefAttrsCat.setTitle(R.string.preference_attributes);
//        root.addPreference(prefAttrsCat);
//
//        // Visual parent toggle preference
//        CheckBoxPreference parentCheckBoxPref = new CheckBoxPreference(this);
//        parentCheckBoxPref.setTitle(R.string.title_parent_preference);
//        parentCheckBoxPref.setSummary(R.string.summary_parent_preference);
//        prefAttrsCat.addPreference(parentCheckBoxPref);
//
//        // Visual child toggle preference
//        // See res/values/attrs.xml for the <declare-styleable> that defines
//        // TogglePrefAttrs.
//        TypedArray a = obtainStyledAttributes(R.styleable.TogglePrefAttrs);
//        CheckBoxPreference childCheckBoxPref = new CheckBoxPreference(this);
//        childCheckBoxPref.setTitle(R.string.title_child_preference);
//        childCheckBoxPref.setSummary(R.string.summary_child_preference);
//        childCheckBoxPref.setLayoutResource(
//                a.getResourceId(R.styleable.TogglePrefAttrs_android_preferenceLayoutChild,
//                        0));
//        prefAttrsCat.addPreference(childCheckBoxPref);
//        a.recycle();

        return root;
    }
}