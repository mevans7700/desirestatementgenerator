package com.evansappwriter.dsgenerator;

import android.app.Activity;

public class DSActivity extends Activity {
	// This determines the which set of online content 
   public static final int EBOOK_BOOK = 1;
   public static final String DS_PREFERENCES = "DSPrefs";
   public static final String DS_PREFERENCES_STATEMENTID   = "DSID";
   public static final String DS_PREFERENCES_STATEMENTNAME = "DSName";
   public static final String DS_PREFERENCES_STATEMENT     = "DesireStatement";
   public static final String DS_OVERVIEW_VIDEO            = "http://www.vimeo.com/23505626";
   
   // XML Tags
   public static final String DS_XML_TAG_WEBCONTENT = "webcontent";
   public static final String DS_XML_TAG_TITLE = "title";
   public static final String DS_XML_TAG_SITE = "site";
   
   // Server URLs
   /*public static final String DS_SERVER_BASE = "http://droid-messengernetwork.appspot.com/";
   public static final String DS_SERVER_CONTENT = DS_SERVER_BASE + "dswebcontents.jsp" + "?"; */
   
   // 06/21/2012 - 1.2 MEE - switch servers & using cold fussion script now
   public static final String DS_SERVER_BASE = "http://messengerphoneapps.com/phone/";   
   public static final String DS_SERVER_CONTENT = DS_SERVER_BASE + "webcontents.cfm" + "?book=" + EBOOK_BOOK;
   
   public static final String DEBUG_TAG = "DS Generator Log"; 

   
}