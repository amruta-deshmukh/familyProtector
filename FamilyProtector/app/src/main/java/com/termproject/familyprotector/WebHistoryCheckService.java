package com.termproject.familyprotector;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Browser;
import android.util.Base64;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class WebHistoryCheckService extends IntentService {


    public WebHistoryCheckService() {
        super("WebHistoryCheckService");
    }

    UserLocalStore userLocalStore;
    private boolean gambling = false, hacking = false, social = false, chat = false, mediaSharing = false,
            adult = false, abortion = false, drugs = false, alcohol = false, weapons = false,
            proxy = false, illegal = false, newsMedia = false, shopping = false, games = false, virtual = false;
    private enum CategoryEnum {
        gambling, hacking, socialnetworking, chatandmessaging, mediasharing, adult, abortion,
        drugs, alcoholandtobacco, weapons, proxyandfilteravoidance, illegalcontent, newsandmedia, shopping,
        games, virtualreality, uncategorized, searchenginesandportals, streamingmedia, entertainment,
        vehicles, informationtech, sports, economyandfinance, jobrelated, messageboardsandforums,
        blogsandpersonal, health, personals, religion, travel, education, business, advertising, humor,
        foodandrecipes, realestate, translators, parked;
    }
    private boolean done = false;
    User user;
    String childName,userName;
    ArrayList<BookMark> bookMarks;
    HttpsURLConnection urlConnection = null;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("Handle Intent", "inside handle intent");
        Context context = getApplicationContext();
        userLocalStore = new UserLocalStore(context);
        user = userLocalStore.getLoggedInUser();
        childName = userLocalStore.getChildForThisPhone();
        userName = user.getUsername();
        getCategoryValuesFromParse();
        bookMarks = new ArrayList<>();
        while (!done){

        }
        Log.v("done with parse","for boolean values");
        done = false;
        getBrowserHistory();
        while (!done){

        }
        Log.v("done", "website history check done");
    }

    private void getCategoryValuesFromParse() {
        ParseQuery<ParseObject> queryClass = ParseQuery.getQuery("ChildRuleWebsite");
        queryClass.whereEqualTo("userName", userName);
        queryClass.whereEqualTo("childName", childName);
        queryClass.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    if (parseObject != null) {

                        if (parseObject.getString("gambling").equals("Yes")) {
                            gambling = true;
                            Log.v("status", "gambling:" + gambling);
                        }
                        if (parseObject.getString("hacking").equals("Yes")) {
                            hacking = true;
                            Log.v("status", "hacking:" + hacking);
                        }
                        if (parseObject.getString("social").equals("Yes")) {
                            social = true;
                            Log.v("status", "social:" + social);
                        }
                        if (parseObject.getString("chat").equals("Yes")) {
                            chat = true;
                            Log.v("status", "chat:" + chat);
                        }
                        if (parseObject.getString("mediaSharing").equals("Yes")) {
                            mediaSharing = true;
                            Log.v("status", "mediaSharing:" + mediaSharing);
                        }
                        if (parseObject.getString("adult").equals("Yes")) {
                            adult = true;
                            Log.v("status", "adult:" + adult);
                        }
                        if (parseObject.getString("abortion").equals("Yes")) {
                            abortion = true;
                            Log.v("status", "abortion:" + abortion);
                        }
                        if (parseObject.getString("drugs").equals("Yes")) {
                            drugs = true;
                            Log.v("status", "drugs:" + drugs);
                        }
                        if (parseObject.getString("alcohol").equals("Yes")) {
                            alcohol = true;
                            Log.v("status", "alcohol:" + alcohol);
                        }
                        if (parseObject.getString("weapons").equals("Yes")) {
                            weapons = true;
                            Log.v("status", "weapons:" + weapons);
                        }
                        if (parseObject.getString("proxy").equals("Yes")) {
                            proxy = true;
                            Log.v("status", "proxy:" + proxy);
                        }
                        if (parseObject.getString("illegal").equals("Yes")) {
                            illegal = true;
                            Log.v("status", "illegal:" + illegal);
                        }
                        if (parseObject.getString("newsMedia").equals("Yes")) {
                            newsMedia = true;
                            Log.v("status", "newsMedia:" + newsMedia);
                        }
                        if (parseObject.getString("shopping").equals("Yes")) {
                            shopping = true;
                            Log.v("status", "shopping:" + shopping);
                        }
                        if (parseObject.getString("games").equals("Yes")) {
                            games = true;
                            Log.v("status", "games:" + games);
                        }
                        if (parseObject.getString("virtualReality").equals("Yes")) {
                            virtual = true;
                            Log.v("status", "virtual:" + virtual);
                        }
                    }
                }
                done = true;
            }
        });
    }

    private void getBrowserHistory(){

        String[] proj = new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL,
                Browser.BookmarkColumns.DATE};
        Calendar ci = Calendar.getInstance();
        long endTime = ci.getTimeInMillis();
        ci.add(Calendar.MINUTE, -10);
        long startTime = ci.getTimeInMillis();
        String[] time = new String[] {String.valueOf(startTime),String.valueOf(endTime)};
        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0" + " AND "+ Browser.BookmarkColumns.DATE
                + " BETWEEN ? AND ?"; // 0 = history, 1 = bookmark
        Cursor mCur = getContentResolver().query(Browser.BOOKMARKS_URI, proj, sel, time, null);
        mCur.moveToFirst();
        @SuppressWarnings("unused")
        String title = "";
        @SuppressWarnings("unused")
        String url = "";
        @SuppressWarnings("unused")
        long date;
        if (mCur.moveToFirst() && mCur.getCount() > 0) {
            boolean cont = true;
            while (mCur.isAfterLast() == false && cont) {
                title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
                url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
                Log.v("url",url);
                date = mCur.getLong(mCur.getColumnIndex(Browser.BookmarkColumns.DATE));
                String bookmarkDateTime[] = getDate(date, "dd/MM/yyyy::::hh:mm:ss a").split("::::");
                String encodedURL = encodingStrToBase64(url);
                String[] categoryStrArray = getUrlCategoryFromApi(encodedURL);
//                String[] categoryStrArray = new String[] {"health","personals"};
                BookMark bookMark = new BookMark(title,url,bookmarkDateTime[0],bookmarkDateTime[1],categoryStrArray);
                bookMarks.add(bookMark);
                Log.v("added website: ", url);
                mCur.moveToNext();
            }
        }

        if(bookMarks.size()>0){
            Log.v("checking", "website flagged or not");
            checkIfwebsiteCategoryIsFlagged();
        }
        else{
            Log.v("else","no bookmark found");
            done = true;
        }
    }

    //converting date millis to string
    private String getDate(long milliSeconds,String dateFormat){

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

    }

    //encoding the string to base64
    private String encodingStrToBase64(String abc) {
        byte[] strBytes = abc.getBytes(StandardCharsets.UTF_8);
        String encoded = Base64.encodeToString(strBytes, Base64.DEFAULT);
        encoded = encoded.replaceAll("(\\r|\\n)", "");

        Log.v("encoded url", encoded + "-a");
        return encoded;
    }

    private String[] getUrlCategoryFromApi(String encodedUrl){

        String encodedURL = encodedUrl;
        Log.v("encodedUrl",encodedURL);
        String authCredentials = FamilyProtectorConstants.WEB_SHRINKER_ENCODED_KEY;
        String output = null;
        StringBuffer buffer = new StringBuffer();
        String[] categoryStrArray = null;

        try{
            final String WEBSITE_CATEGORY_API_BASE_URL = "https://api.webshrinker.com/categories/v2/";
            final String WEBSITE_CATEGORY_API_WITH_URL = WEBSITE_CATEGORY_API_BASE_URL+encodedURL;
            Log.v("final url", WEBSITE_CATEGORY_API_WITH_URL);
            final String HEADER_PARAM = "authorization";
            final String HEADER_VALUE = " Basic "+authCredentials;
            Log.v("header value", HEADER_VALUE);

            Uri builtUri = Uri.parse(WEBSITE_CATEGORY_API_WITH_URL).buildUpon()
                    .build();
            URL url = new URL(builtUri.toString());


            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            urlConnection = (HttpsURLConnection) url.openConnection();

            SSLContext context = SSLContext.getInstance("TLS");
            TrustManager tm[] = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
            context.init(null, tm, null);
            SSLSocketFactory preferredCipherSuiteSSLSocketFactory = new PreferredCipherSuiteSSLSocketFactory(context.getSocketFactory());
            urlConnection.setSSLSocketFactory(preferredCipherSuiteSSLSocketFactory);


            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty(HEADER_PARAM, HEADER_VALUE);
            urlConnection.setHostnameVerifier(hostnameVerifier);
            urlConnection.connect();
            Log.v("connected", builtUri.toString());

            InputStream inputStream = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


            while ((output = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(output + "\n");
            }

            JSONObject jsonObject = new JSONObject(buffer.toString());
            JSONArray dataArr = jsonObject.getJSONArray("data");
            JSONArray categoryArr = dataArr.getJSONObject(0).getJSONArray("categories");

            Log.v("category", categoryArr.length()+"");
            categoryStrArray = new String[categoryArr.length()];
            for(int i = 0;i<categoryArr.length();i++){
                categoryStrArray[i]= categoryArr.get(i).toString();
                Log.v("category",categoryArr.get(i).toString());
            }



        }catch (Exception e){
            Log.e("API error",e.toString());
            e.printStackTrace();
        }


        return categoryStrArray;
    }

    private void checkIfwebsiteCategoryIsFlagged(){
        boolean addedToParse = false;

        for(int i=0; i<bookMarks.size();i++){
            boolean flagged = false;
            StringBuffer categoriesStr = new StringBuffer();
            BookMark bookMark = bookMarks.get(i);
            for(int j=0;j<bookMark.bookmarkCategories.length;j++){
                String category = bookMark.bookmarkCategories[j];
                CategoryEnum categoryEnum = null;
                try{
                    categoryEnum = CategoryEnum.valueOf(category);
                }
                catch (Exception e){
                    Log.v("lookuperror",e.toString());
                    e.printStackTrace();
                    categoryEnum = CategoryEnum.valueOf("health");
                }
                switch (categoryEnum){
                    case gambling:
                        if(gambling) {
                            flagged = true;
                            categoriesStr.append("gambling, ");
                        }
                        break;
                    case hacking:
                        if(hacking) {
                            flagged = true;
                            categoriesStr.append("hacking, ");

                        }
                        break;
                    case socialnetworking:
                        if(social) {
                            flagged = true;
                            categoriesStr.append("social networking, ");
                        }
                        break;
                    case chatandmessaging:
                        if(chat) {
                            flagged = true;
                            categoriesStr.append("chat & messaging, ");
                        }
                        break;
                    case mediasharing:
                        if(mediaSharing) {
                            flagged = true;
                            categoriesStr.append("media sharing, ");
                        }
                        break;
                    case adult:
                        if (adult) {
                            flagged = true;
                            categoriesStr.append("adult, ");
                        }
                        break;
                    case abortion:
                        if(abortion) {
                            flagged = true;
                            categoriesStr.append("abortion, ");
                        }
                        break;
                    case drugs:
                        if(drugs) {
                            flagged = true;
                            categoriesStr.append("drugs, ");
                        }
                        break;
                    case alcoholandtobacco:
                        if(alcohol) {
                            flagged = true;
                            categoriesStr.append("alcohol & tobacco, ");
                        }
                        break;
                    case weapons:
                        if(weapons) {
                            flagged = true;
                            categoriesStr.append("weapons, ");
                        }
                        break;
                    case proxyandfilteravoidance:
                        if(proxy) {
                            flagged = true;
                            categoriesStr.append("proxy & filter avoidance, ");
                        }
                        break;
                    case illegalcontent:
                        if(illegal) {
                            flagged = true;
                            categoriesStr.append("illegal content, ");
                        }
                        break;
                    case newsandmedia:
                        if(newsMedia) {
                            flagged = true;
                            categoriesStr.append("news and media, ");
                        }
                        break;
                    case shopping:
                        if(shopping) {
                            flagged = true;
                            categoriesStr.append("shopping, ");
                        }
                        break;
                    case games:
                        if(games) {
                            flagged = true;
                            categoriesStr.append("games, ");
                        }
                        break;
                    case virtualreality:
                        if(virtual) {
                            flagged = true;
                            categoriesStr.append("virtual reality, ");
                        }
                        break;
                    default:
                        break;
                }
            }
            if(flagged){
                Log.v("website flagged", "true");
                categoriesStr.setLength(categoriesStr.length() - 2);
                addWebsiteAlertToParse(bookMark, categoriesStr.toString());
                addedToParse = true;
            }
        }
        if(addedToParse){
            Log.v("added to parse","sending push notification");
            sendParsePushNotification();

        }
        else{
            Log.v("done","no website added to parse");
            done = true;
        }

    }


    private void addWebsiteAlertToParse(BookMark bookMark, String categoriesStr){

        ParseObject childWebsiteAlerts = new ParseObject("ChildWebsiteAlerts");
        childWebsiteAlerts.put("userName", userName);
        childWebsiteAlerts.put("childName", childName);
        childWebsiteAlerts.put("urlName",bookMark.bookmarkUrl);
        childWebsiteAlerts.put("categoriesList",categoriesStr);
        childWebsiteAlerts.put("visitedDate",bookMark.bookmarkDate);
        childWebsiteAlerts.put("visitedTime",bookMark.bookmarkTime);
        childWebsiteAlerts.put("websiteTitle",bookMark.bookmarkTitle);
        childWebsiteAlerts.saveInBackground();
        Log.v("website added to parse",bookMark.bookmarkUrl);


    }

    private void sendParsePushNotification(){
        // Find devices associated with these users
        ParseQuery pushWebQuery = ParseInstallation.getQuery();
        pushWebQuery.whereEqualTo("email", "parent:" + userName);

        // Send push notification to query
        ParsePush pushWeb = new ParsePush();
        pushWeb.setQuery(pushWebQuery); // Set our Installation query

        pushWeb.setMessage("Web History alert for: " + childName);
        pushWeb.sendInBackground();

        done = true;
    }


}
