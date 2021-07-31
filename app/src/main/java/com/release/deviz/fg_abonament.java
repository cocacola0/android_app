package com.release.deviz;

import static com.android.billingclient.api.BillingClient.SkuType.SUBS;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class fg_abonament extends Fragment implements PurchasesUpdatedListener
{
    public static final String PREF_FILE= "MyPref";
    private static ArrayList<String> subcribeItemIDs = new ArrayList<String>() {{
        add("subscriptie_o_luna");
        add("subscriptie_un_an");
    }};
    private static ArrayList<String> subscribeItemDisplay = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    ListView listView;

    private static BillingClient billingClient;

    LinearLayout l_trial, l_luna, l_an;
    Button btn_plateste;
    TextView txt_data_final;
    LinearLayout last_selected;
    String selected;
    RelativeLayout frame;


    public fg_abonament() {
        // Required empty public constructor
    }

    public fg_abonament newInstance() {
        return new fg_abonament();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View r_view = inflater.inflate(R.layout.fg_abonament, container, false);

        setup_prices(r_view);

        billingClient = BillingClient.newBuilder(getContext()).enablePendingPurchases().setListener(this).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK)
                {
                    Purchase.PurchasesResult queryPurchase = billingClient.queryPurchases(SUBS);
                    List<Purchase> queryPurchases = queryPurchase.getPurchasesList();
                    if(queryPurchases!=null && queryPurchases.size()>0){
                        handlePurchases(queryPurchases);
                    }

                    ArrayList<Integer> purchaseFound =new ArrayList<Integer> ();
                    if(queryPurchases!=null && queryPurchases.size()>0){
                        for(Purchase p:queryPurchases){
                            int index=subcribeItemIDs.indexOf(p.getSku());
                            if(index>-1)
                            {
                                purchaseFound.add(index);
                                if(p.getPurchaseState() == Purchase.PurchaseState.PURCHASED)
                                {
                                    saveSubscribeItemValueToPref(subcribeItemIDs.get(index),true);
                                }
                                else{
                                    saveSubscribeItemValueToPref(subcribeItemIDs.get(index),false);
                                }
                            }
                        }
                        for(int i=0;i < subcribeItemIDs.size(); i++){
                            if(purchaseFound.indexOf(i)==-1){
                                saveSubscribeItemValueToPref(subcribeItemIDs.get(i),false);
                            }
                        }
                    }
                    else{
                        for( String purchaseItem: subcribeItemIDs ){
                            saveSubscribeItemValueToPref(purchaseItem,false);
                        }
                    }

                }

            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });

        return r_view;
    }

    void setup_prices(View r_view)
    {
        frame = r_view.findViewById(R.id.fg_ab_frame);

        l_trial = r_view.findViewById(R.id.fg_abonament_ll_trial);
        l_luna = r_view.findViewById(R.id.fg_abonament_ll_1_luna);
        l_an = r_view.findViewById(R.id.fg_abonament_ll_1_an);

        btn_plateste = r_view.findViewById(R.id.fg_ab_cumpara);
        txt_data_final = r_view.findViewById(R.id.fg_ab_txt_data_final);

        txt_data_final.setText(getDateFromPref());

        if(getSubscribeItemValueFromPref("trial")) {
            frame.removeView(l_trial);
            l_trial.setVisibility(View.GONE);
        }
        else
            l_trial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(last_selected != null)
                        last_selected.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    l_trial.setBackgroundColor(Color.parseColor("#ADD8E6"));
                    last_selected = l_trial;
                    selected = "trial";
                }
            });

        l_luna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(last_selected != null)
                    last_selected.setBackgroundColor(Color.parseColor("#FFFFFF"));

                l_luna.setBackgroundColor(Color.parseColor("#ADD8E6"));
                last_selected = l_luna;
                selected = "luna";
            }
        });

        l_an.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(last_selected != null)
                    last_selected.setBackgroundColor(Color.parseColor("#FFFFFF"));

                l_an.setBackgroundColor(Color.parseColor("#ADD8E6"));
                last_selected = l_an;
                selected = "an";
            }
        });

        btn_plateste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(last_selected == l_trial)
                    buy_trial();
                else
                    if(last_selected == l_luna)
                        on_click(0);
                    else
                        on_click(1);
            }
        });

    }

    private void buy_trial()
    {
        saveSubscribeItemValueToPref("trial", true);
        saveDateToPref("luna");
        ((MainActivity) getActivity()).start_fg("acasa");
    }

    private void on_click(int position)
    {
        if(getSubscribeItemValueFromPref(subcribeItemIDs.get(position))){
            Toast.makeText(getContext(),subcribeItemIDs.get(position)+" is Already Subscribed",Toast.LENGTH_SHORT).show();
            return;
        }
        if (billingClient.isReady()) {
            initiatePurchase(subcribeItemIDs.get(position));
        }
        else{
            billingClient = BillingClient.newBuilder(getContext()).enablePendingPurchases().setListener(fg_abonament.this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase(subcribeItemIDs.get(position));
                    } else {
                        Toast.makeText(getContext(),"Error "+billingResult.getDebugMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onBillingServiceDisconnected() {
                }
            });
        }
    }

    private void notifyList(){
        subscribeItemDisplay.clear();
        for(String p:subcribeItemIDs){
            subscribeItemDisplay.add("Subscribe Status of "+p+" = "+getSubscribeItemValueFromPref(p));
        }
        arrayAdapter.notifyDataSetChanged();
    }

    private SharedPreferences getPreferenceObject() {
        return getContext().getSharedPreferences(PREF_FILE, 0);
    }

    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = getContext().getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }

    private boolean getSubscribeItemValueFromPref(String PURCHASE_KEY){
        return getPreferenceObject().getBoolean(PURCHASE_KEY,false);
    }

    private void saveSubscribeItemValueToPref(String PURCHASE_KEY,boolean value){
        getPreferenceEditObject().putBoolean(PURCHASE_KEY,value).commit();
    }

    private void saveDateToPref(String date_to_add)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        String cur_date = getDateFromPref();
        String[] cur_date_ = cur_date.split("/");

        int m = Integer.parseInt(cur_date_[0]);
        int d = Integer.parseInt(cur_date_[1]);
        int y = Integer.parseInt(cur_date_[2]);

        if(date_to_add.equals("luna"))
        {
            m++;

            if(m>12) {
                m = m - 12;
                y++;
            }
        }
        else
            if(date_to_add.equals("an"))
                y++;

        String new_date = String.valueOf(m) + "/" + String.valueOf(d) + "/" + String.valueOf(y);


        getPreferenceEditObject().putString("sub_date",new_date).commit();

        txt_data_final.setText(new_date);
    }

    private String getDateFromPref()
    {
        String pattern = "MM/dd/yyyy";

        String cur_date = new SimpleDateFormat(pattern).format(new Date());

        return getPreferenceObject().getString("sub_date",cur_date);
    }

    private void initiatePurchase(final String PRODUCT_ID) {
        Log.d("ABONAMENTE", "Arrived in initiatePurchase");
        List<String> skuList = new ArrayList<>();
        skuList.add(PRODUCT_ID);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(SUBS);

        BillingResult billingResult = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);

        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(@NonNull BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(0))
                                            .build();
                                    billingClient.launchBillingFlow(requireActivity(), flowParams);
                                } else {
                                    Toast.makeText(getContext(), "Subscribe Item " + PRODUCT_ID + " not Found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(),
                                        " Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getContext(),
                    "Sorry Subscription not Supported. Please Update Play Store", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        //if item newly purchased
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases);
        }
        //if item already purchased then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Purchase.PurchasesResult queryAlreadyPurchasesResult = billingClient.queryPurchases(SUBS);
            List<Purchase> alreadyPurchases = queryAlreadyPurchasesResult.getPurchasesList();
            if(alreadyPurchases!=null){
                handlePurchases(alreadyPurchases);
            }
        }
        //if purchase cancelled
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(getContext(),"Purchase Canceled",Toast.LENGTH_SHORT).show();
        }
        // Handle any other error msgs
        else {
            Toast.makeText(getContext(),"Error "+billingResult.getDebugMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    void handlePurchases(List<Purchase>  purchases) {
        Log.d("ABONAMENTE", "handlePurchases!");

        int i = 0;

        for(Purchase purchase:purchases)
        {
            final int index=subcribeItemIDs.indexOf(purchase.getSku());
            
            if(index>-1) {
                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED)
                {
                    if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                        Toast.makeText(getContext(), "Error : Invalid Purchase", Toast.LENGTH_SHORT).show();
                        continue;
                    }
                    if (!purchase.isAcknowledged()) {
                        AcknowledgePurchaseParams acknowledgePurchaseParams =
                                AcknowledgePurchaseParams.newBuilder()
                                        .setPurchaseToken(purchase.getPurchaseToken())
                                        .build();

                        billingClient.acknowledgePurchase(acknowledgePurchaseParams,
                                new AcknowledgePurchaseResponseListener() {
                                    @Override
                                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                                        if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                                            //if purchase is acknowledged
                                            //then saved value in preference
                                            saveSubscribeItemValueToPref(subcribeItemIDs.get(index),true);
                                            Toast.makeText(getContext(), subcribeItemIDs.get(index)+" Item Subscribed", Toast.LENGTH_SHORT).show();

                                            if(index == 0)
                                                saveDateToPref("luna");
                                            else
                                                saveDateToPref("an");

                                            ((MainActivity) getActivity()).start_fg("acasa");
                                        }
                                    }
                                });

                    }
                    else {
                        if(!getSubscribeItemValueFromPref(subcribeItemIDs.get(index))){
                            saveSubscribeItemValueToPref(subcribeItemIDs.get(index),true);
                            Toast.makeText(getContext(), subcribeItemIDs.get(index)+" Item Subscribed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //if purchase is pending
                else if(  purchase.getPurchaseState() == Purchase.PurchaseState.PENDING)
                {
                    Toast.makeText(getContext(),
                            subcribeItemIDs.get(index)+" Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
                }
                //if purchase is refunded or unknown
                else if( purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE)
                {
                    //mark purchase false in case of UNSPECIFIED_STATE
                    saveSubscribeItemValueToPref(subcribeItemIDs.get(index),false);
                    Toast.makeText(getContext(), subcribeItemIDs.get(index)+" Purchase Status Unknown", Toast.LENGTH_SHORT).show();
                }
            }

            i++;
        }
    }

    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            Log.d("ABONAMENTE","Verify Valid signature");
            String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlgjSWkLGW/RfAHpJzECRql/1Ds+mEO7+IwtVsP6ddxTcYBMFYKYmd51dSI2yfiav2gBEw07lYMn9tBUts/18GDE7Ddbay5zOScrY+aflr8QUL/1tWoFzEF4s8vkixqLqVjcdJvQb2FCwqL2MZRTYJ8Cd8LdR52TU8pbp5UvsHIE6kS6Nd3/pbckecJj9Hli8uSWv0EivrwzVQScISLiTrdabSEahrJyIG4Zx2nZzwDTmN3EpuFWTW9jMg29ZgzzOGR+7vXx3aZZJ5oIXh7/+GEFg14A+QWP6p66Rdj1hCSl6gqJgH1f7gh+i0ZmL+vp3jDTSokJmnN8wXwFIyfR5GwIDAQAB";
            return com.release.deviz.Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(billingClient!=null){
            billingClient.endConnection();
        }
    }
}