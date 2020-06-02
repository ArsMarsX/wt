package com.revolve44.windturbine1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.revolve44.windturbine1.ui.dashboard.DashboardFragment;
import com.revolve44.windturbine1.ui.home.HomeFragment;
//import com.revolve44.windturbine1.ui.notifications.NotificationsFragment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //Example
    //api.openweathermap.org/data/2.5/weather?q=moscow&appid=1b87fee17221ed7893aa488cff08bfa2
    // http://api.openweathermap.org/data/2.5/find?lat=55.5&lon=37.5&cnt=10&appid=ac79fea59e9d15377b787a610a29b784

    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String CITY = "Moscow";

    public static String AppId = "4e78fa71de97f97aa376e42f2f1c99cf";
    public static String MC = "&units=metric&appid=";

    public TextView temperatureText;
    public TextView windText;

    public static String lat;
    public static String lon;
    public static String metric = "metric";


    //Variables
    public float NominalPower;//????????????????????????????????
    public float CurrentPower;
    public int CurrentPowerInt;
    public float cloud;
    public float windF;
    public int windI;
    public float temp;

    public String desription;
    public float pressure;
    public float humidity;
    public boolean tempScale;

    public long unixSunrise;
    public long unixSunset;

    public String city;
    public String country;

    public boolean isDataAvailable = false;
    public LinkedHashMap<Long, Float> dataMap = new LinkedHashMap<>();
    public LinkedHashMap<String, Float> corvette = new LinkedHashMap<>();

    public String sunset;
    public String sunrise;
    public String hournowStr;

    public TextView mainoutput;
    public TextView saymayname;

    public long TimeHashMap;
    public float CurrentPowerHashMap;

    Boolean HotCheck= false;

    int SunPeriod = 0;

    public String solarhoursString;

    public long GMT = 0;
    public long UTCtime;
    public long UnixCurrentTime;

    //public String jsonString;

    LinkedList<String> Legendzero = new LinkedList<>();
    LinkedList<Integer> Valuezero = new LinkedList<>();

    String json;
    String json2;

    int z = 0; // for retrofit -
    int w =0; // for timemanipulations
    int p = 0; // for retrofit
    int s = 0;


    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new DashboardFragment();
    //final Fragment fragment3 = new NotificationsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
////        // Passing each menu ID as a set of Ids because each
////        // menu should be considered as top level destinations.
        Log.d("Lifecycle Launch ->>>"," activity ");


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).addToBackStack(null).commit();
        }
       // fm.beginTransaction().replace(R.id.fragment_container, fragment3, "3").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment1, "1").commit();
        runforecast();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void runforecast(){
        //////////////////////////////////////////////
        //         Click from Fragment              //
        //////////////////////////////////////////////
        Log.d("Lifecycle -> method "," runforecast ");
        LoadData();
        getCurrentData();

        TimeManipulations();
        SaveData();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void getCurrentData() {
        Log.d("Lifecycle -> method "," getCurrentdata ");

//        // Before all, we load coordinations and nominal power of station
        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave", MODE_PRIVATE);
        lat = sharedPreferences.getString("lati",lat);
        lon = sharedPreferences.getString("long",lon);

        lat = String.valueOf(sharedPreferences.getFloat("latitudeF",7f));
        lon = String.valueOf(sharedPreferences.getFloat("longitudeF",7f));

        lat = "54.43";
        lon = "60.45";
        NominalPower = 1000;

        Log.d("Lifecycle -> method "," latitude " + lat + lon);
//        NominalPower = sharedPreferences.getFloat("Nominal_Power", (float) NominalPower);
        //city = sharedPreferences.getString("MyCity",city);
        //NominalPower = 1000;
        //CITY = "Mexico";
        //NominalPower = 29000;
        OkHttpClient.Builder  okhttpClientBuilder = new OkHttpClient.Builder();//for create a LOGs
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); //for create a LOGs
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); //for create a LOGs
        okhttpClientBuilder.addInterceptor(logging); //for create a LOGs

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build()) //for create a LOGs
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        //Call<WeatherResponse> call = service.getCurrentWeatherData(CITY, metric, AppId);
        Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, metric, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    isDataAvailable = true;
//                    Log.d("RETROFITTTTTTTT    ", weatherResponse+" ");

                    //main variables
                    cloud = weatherResponse.clouds.all;
                    temp = weatherResponse.main.temp;
                    windF = weatherResponse.wind.speed;
                    country = weatherResponse.sys.country;
                    unixSunrise = weatherResponse.  sys.sunrise;
                    unixSunset = weatherResponse.sys.sunset; // time of sunrise and sunset
                    city = weatherResponse.name; // i added two variable and this work, before i dont see this in Toast.
                    pressure = weatherResponse.main.pressure;
                    humidity = weatherResponse.main.humidity;
                    GMT = weatherResponse.timezone;
                    UnixCurrentTime = (long) weatherResponse.dt;
                    Log.d("From retrofit         ",  "Temp and press " + temp+ " "+ pressure);

//                    unixSunrise = weatherResponse.sunrise;
//                    unixSunset = weatherResponse.sunset; // time of sunrise and sunset
//                    try {
//                        desription = weatherResponse.weather.description;
//                    }catch (Exception e){
//                        Log.d("################DEPLOY", "descript dont work");
//                    }

                }
            }


            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "Check Internet connection. Fail in Response"+t.getMessage();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Call<WeatherForecastResponse> forecastCall = service.getDailyData(lat, lon, metric, AppId);
        forecastCall.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherForecastResponse> forecastCall, @NonNull Response<WeatherForecastResponse> response) {
                if (response.code() == 200) {
                    WeatherForecastResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    ArrayList<WeatherResponse> list = weatherResponse.list;

                    if (dataMap.size() == 0){

                        Log.d("z -1 ", z+" ");

                        for(WeatherResponse wr: list){
                            // .put (Key, Clouds.all)

                            if (z<=20){
                                CurrentPowerHashMap = NominalPower - NominalPower * (wr.clouds.all / 100)*0.8f; // чисто разницу лучше не оставлять, а домножанать на 0.8 например чтобы при макс. облач. не было нуля

                                TimeHashMap = (long) wr.dt * 1000;
                                //if (unixSunrise )

                                dataMap.put(TimeHashMap, CurrentPowerHashMap);
                                Log.d("Datamap 1->", TimeHashMap+" "+ CurrentPowerHashMap);
                                z++;
                            }
//                            try {
//                            }catch (Exception e){
//                                Log.d("my Error", "in retrofit array");
//                            }
                            //list.dt -- is Time of data forecasted, unix, UTC
                            //Toast.makeText(getApplicationContext(), (CharSequence) dataMap, Toast.LENGTH_LONG).show();

                        }
                        Log.d("Datamap 1>>>>>", ""+dataMap);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherForecastResponse> forecastCall, @NonNull Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "CHECK INTERNET CONNECTION"+t.getMessage();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        windI = Math.round(windF);
        if (temp>30){
            HotCheck = true;
        }


        //SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();

//        editor.putString("MyCity",city);
//        editor.apply();
          Log.d("From retrofit         ",  "Sunrise and sunset " + unixSunrise+ " "+ unixSunset);
//        Log.d("From retrofit          ", GMT +"GMT now ");
//        Log.d("From retrofit          ", UnixCurrentTime +"current time ");
        if (cloud >-1 ){
            CurrentPower = NominalPower - NominalPower*(windF/100)*0.8f;
        }else{
            CurrentPower = 404;
        }
        CurrentPowerInt = Math.round(CurrentPower);
        Log.d("From retrofit          ", "Current power >"+ CurrentPowerInt);
        //TimeManipulations();
        Log.d("z -2 ", z+" ");

    }

    public void TimeManipulations(){
        Log.d("Lifecycle -> method "," timemanipulations ");
        Log.d("Timemaipulation method "," UTC -> "+ UnixCurrentTime+ " unixSunrise "+ unixSunrise+ " unixSet "+ unixSunset + "GMT "+ GMT);
        if (UnixCurrentTime>1 & unixSunrise>1) {
            ////////////////////////////////////////////////////
            //     Time zone & unix sunrise/sunset            //
            //      Here we define human time                 //
            ////////////////////////////////////////////////////
            long timestamp = UnixCurrentTime + GMT;
            long timestamp2 = unixSunrise + GMT;
            long timestamp3 = unixSunset + GMT;



            //UTCtime = System.currentTimeMillis(); // Here i have been problem coz i multipled on 1000L UTC time:(
            String zeroPlace1 = "";
            String zeroPlace2 = "";
            String zeroPlace3 = "";
            String zeroPlace4 = "";

            /////////////////////////////
            /////////////////////////////
            long day = timestamp / 86400;
            //
            long hourinSec = (timestamp - day * 86400); //hours in sec
            long hour = hourinSec / 3600; // hr
            //
            long minutesinSec = hourinSec - hour * 3600; // minutes in sec
            long minutes = minutesinSec / 60;
            //
            hournowStr = String.valueOf(hour);
            /////////////////////////////////
            /////////////////////////////////
            long day2 = timestamp2 / 86400;

            //
            long hourinSec2 = (timestamp2 - day2 * 86400); //hours in sec
            long hour2 = hourinSec2 / 3600; // hr
            if (hour2 < 10) {
                zeroPlace1 = "0";
            }
            //
            long minutesinSec2 = hourinSec2 - hour2 * 3600; // minutes in sec
            long minutes2 = minutesinSec2 / 60;
            if (minutes2 < 10) {
                zeroPlace2 = "0";
            }
            //
            sunrise = zeroPlace1 + hour2 + ":" + zeroPlace2 + minutes2;
            /////////////////////////////////////
            ////////////////////////////////////
            long day3 = timestamp3 / 86400;

            //
            long hourinSec3 = (timestamp3 - day3 * 86400); //hours in sec
            long hour3 = hourinSec3 / 3600; // hr
            if (hour3 < 10) {
                zeroPlace3 = "0";
            }
            //
            long minutesinSec3 = hourinSec3 - hour3 * 3600; // minutes in sec
            long minutes3 = minutesinSec3 / 60;
            if (minutes3 < 10) {
                zeroPlace4 = "0";
            }
            //
            sunset = zeroPlace3 + hour3 + ":" + zeroPlace4 + minutes3;

            Log.d("TIMEST >", sunrise + "and sunset " + sunset);
            Log.d("TIMEST >", timestamp + "and  " + timestamp2);

//////////////////////////////////////////
            int hournow = (int) hour;
            int hourRise = (int) hour2;
            int hourSet = (int) hour3;

            int sector = (hourSet - hourRise) / 5;

            //set Sun Position
            if (hournow > hourSet) {
                SunPeriod = 0;
                //Toast.makeText(this, "NIGHT", Toast.LENGTH_SHORT).show();
            } else if (hournow > hourSet - sector) {
                //Toast.makeText(this, "sunset", Toast.LENGTH_SHORT).show();
                SunPeriod = 5;
                //0.6
                CurrentPower = (float) (CurrentPower);

            } else if (hournow > hourSet - 2 * sector) {
                //Toast.makeText(this, "135", Toast.LENGTH_SHORT).show();
                SunPeriod = 4;
                //0.8
                CurrentPower = (float) (CurrentPower);

            } else if (hournow > hourSet - 3 * sector) {
                //Toast.makeText(this, "90", Toast.LENGTH_SHORT).show();
                SunPeriod = 3;
                //1

            } else if (hournow > hourSet - 4 * sector) {
                //Toast.makeText(this, "45", Toast.LENGTH_SHORT).show();
                SunPeriod = 2;
                //0.8
                CurrentPower = (float) (CurrentPower);

            } else if (hournow > hourRise) {
                //Toast.makeText(this, "sunrise", Toast.LENGTH_SHORT).show();
                SunPeriod = 1;
                //0.6
                CurrentPower = (float) (CurrentPower);

            } else {
                SunPeriod = 0;
//            Toast.makeText(this, "NIGHT", Toast.LENGTH_SHORT).show();
            }


//            if (SunPeriod == 0) {
//                CurrentPowerInt = 0;
//            }

            Log.d("Timemanipulations END> ", "Sunperiod ->" + SunPeriod + " hournow " + hournow);

            Log.d("Datamap 2>>>>>", "" + dataMap);


            ////////////////////////////////////////////////////
            solarhoursString = String.valueOf(hourSet - hourRise);
            Log.d("@@@@@@@@@@@@", "0 Solarhour:  "+solarhoursString);

            //Log.d("##########", " "+sunrise+" "+sunset + " unix -> " +unixSunrise + " GMT is ->" + GMT +" TZ is -> "+ finalblank);
            String zeroPlace5 = "";
            String zeroPlace6 = "";
            //set GMT in human view
            int timeGMT = (int) (GMT/ 3600);
            TimeZone tz = TimeZone.getTimeZone("GMT"+timeGMT);

            int a = 0;
            for (Map.Entry<Long, Float> entry : dataMap.entrySet()) {
                long key = ((entry.getKey()) / 1000L) + GMT;
                float value = (entry.getValue());
                zeroPlace5 = "";
                zeroPlace6 = "";

                // Set current GMT
                /* debug: is it local time? */
                Log.d("Time zone: ", tz.getDisplayName());
                /* date formatter in local timezone */
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM");
                sdf.setTimeZone(tz);
                /* print your timestamp and double check it's the date you expect */
                String date = sdf.format(new Date(key * 1000));

                //String date = DateFormat.format("dd-MMMM", key*1000L).toString();


                long day4 = key / 86400;

                //
                long hourinSec4 = (key - day4 * 86400); //hours in sec
                long hour4 = hourinSec4 / 3600; // hr
                if (hour4 < 10) {
                    zeroPlace5 = "0";
                }
                //
                long minutesinSec4 = hourinSec4 - hour4 * 3600; // minutes in sec
                long minutes4 = minutesinSec4 / 60;
                if (minutes4 < 10) {
                    zeroPlace6 = "0";
                }
                //-2:-59 > 21:00
                String ModernTime = zeroPlace5 + hour4 + ":" + zeroPlace6 + minutes4;

                //Log.d("Hashmap test loop -> ", "key : "+ key + " value : "+ value);
                int transitTime = (int) hour4;
                Log.d("Hashmap test loop -> ", "transittime : " + transitTime + " hoursunset : " + hourSet);
                Log.d("Loopz ", ModernTime+ " and value " + value);
                Legendzero.add(ModernTime+" "+date);
                Valuezero.add((int) (value));
//                if (transitTime > hourSet) {
//                    //0
//                    //corvette.put(ModernTime, value*0f);
//                    Legendzero.add(ModernTime+" "+date);
//                    Valuezero.add((int) (value*0f));
//                } else if (transitTime > hourSet - sector) {
//                    //0.6
//                    //corvette.put(ModernTime, value*0.6f);
//                    Legendzero.add(ModernTime+" "+date);
//                    Valuezero.add((int) (value*0.6f));
//                } else if (transitTime > hourSet - 2 * sector) {
//                    //0.8
//                    //corvette.put(ModernTime, value*0.8f);
//                    Legendzero.add(ModernTime+" "+date);
//                    Valuezero.add((int) (value*0.8f));
//                } else if (transitTime > hourSet - 3 * sector) {
//                    //1
//                    //corvette.put(ModernTime, value);
//                    Legendzero.add(ModernTime+" "+date);
//                    Valuezero.add((int) value);
//                } else if (transitTime > hourSet - 4 * sector) {
//                    //0.8
//                    //corvette.put(ModernTime, value*0.8f);
//                    Legendzero.add(ModernTime+" "+date);
//                    Valuezero.add((int) (value*0.8f));
//                } else if (transitTime > hourSet - 5 * sector) {
//                    //0.6
//                    //corvette.put(ModernTime, value*0.6f);
//                    Legendzero.add(ModernTime+" "+date);
//                    Valuezero.add((int) (value*0.6f));
//                } else {
//                    //0
//                    //corvette.put(ModernTime, value*0f);
//                    Legendzero.add(ModernTime+" "+date);
//                    Valuezero.add((int) (value*0f));
//                }
                a++;
                Log.d(" loop ", a + " times ");
                Log.d("Loop after of this", ModernTime+ " and value " + value);
            }
            w++;
        }
        Log.d("Value and legend 3>>>>>", ""+Legendzero+ "< ][ >"+Valuezero);
        //////////////////////////////////////////////////////////////////////////
        //                    convert to gson and save                          //
        //////////////////////////////////////////////////////////////////////////
        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (Legendzero.size()>1 & s<1){
            Gson gson = new Gson();
            json = gson.toJson(Legendzero);
            json2 = gson.toJson(Valuezero);

            editor.putString("legend", json);
            editor.putString("value", json2);
            editor.apply();
            Log.d("Datamap 2,5>>>>>", ""+json);
            s++;

//            jsonString = new Gson().toJson(corvette);
//            editor.putString("map", jsonString); // this hashmap
//            editor.apply(); // added apply and this works!
//            Log.d("Hash map corvette -> ", " "+ corvette + "json -> "+ jsonString);
//            Log.d("json 4>>>>>", ""+jsonString);
        }
    }

    public void SaveData(){
        Log.d("Lifecycle -> method "," savedata ");
        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("CurPow",CurrentPowerInt);
        editor.putLong("GMT",GMT);
        editor.putFloat("temp",temp);
        editor.putFloat("pressure",pressure);
        editor.putFloat("humidity",humidity);
        editor.putString("sunrise",sunrise);
        editor.putString("sunset",sunset);

        editor.putLong("unixrise",unixSunrise);
        editor.putLong("unixset",unixSunset);
        editor.putLong("unix",UnixCurrentTime);

        editor.putString("solarhours",solarhoursString);
        editor.putBoolean("tempScale",tempScale);
        Log.d("@@@@@@@@@@@@", "1 Solarhour:  "+solarhoursString);

        editor.putString("MyCity",city);
        editor.apply();
        Log.d("@@@@@@@@@@@@", "GMT:  "+GMT);
        Log.d("@@@@@@@@@@@@", "temp:  "+temp);
        //Log.d("@@@@@@@@@@@@", "JSON:  "+jsonString);
        Log.d("@@@@@@@@@@@@", "CITY:  "+city);

    }
    public void LoadData(){
        Log.d("Lifecycle -> method "," Loaddata ");

        // Before all, we load coordinations and nominal power of station
        SharedPreferences sharedPreferences = getSharedPreferences("MasterSave",MODE_PRIVATE);

        //Check Savings
        Map<String, ?> allPrefs = sharedPreferences.getAll(); //your sharedPreference
        Set<String> set = allPrefs.keySet();
        for(String s : set){
            Log.d("Shared Pref", s + "<" + allPrefs.get(s).getClass().getSimpleName() +"> =  "
                    + allPrefs.get(s).toString());
        }

        lat = sharedPreferences.getString("lati",lat);
        lon = sharedPreferences.getString("long",lon);
        city = sharedPreferences.getString("MyCity",city);
        NominalPower = sharedPreferences.getFloat("Nominal_Power",(float)NominalPower);

        sunrise = sharedPreferences.getString("sunrise",sunrise);
        sunset = sharedPreferences.getString("sunset",sunset);

        Log.d("************", "LAT LOT:  "+lat+ " and " + lon);
        Log.d("************", "GMT:  "+GMT);
        Log.d("************", "temp:  "+temp);
        //Log.d("************", "JSON:  "+jsonString);
        Log.d("************", "CITY:  "+city);



    }

    //switcher of fragmnets, he help for switching without loss filled form in fragments
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.navigation_dashboard:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
            }
            return false;
        }
    };
}
