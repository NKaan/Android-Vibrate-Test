package com.example.numank.qavibratetest;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AnaSayfa extends AppCompatActivity implements View.OnClickListener  {

    private int second = 0;
    private int minute = 0;
    private int hour = 0;
    Integer count =1;

    public MyTask myTask;

    TextView gecen_sure,kalan_adim_sayisi,dongu_saat_txt,not_txt;
    RadioButton saat_bazinda,dongusel,saat_ayari,dakika_ayari;
    ConstraintLayout ana_Sayfa;
    RadioGroup saat_dakika_group;
    Button baslat;
    EditText test_dongu_sayisi,test_titreme_suresi,iki_titreme_arasi_beklenen_sure;
    ProgressBar kalan_miktar_progress;
    int adim_sayisi,titreme_suresi,titreme_arasi_beklenen_sure;
    boolean test_aktif = false;
    int saniye;
    int eksi_sure;
    int yapilacak_saniye;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    CountDownTimer yourCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        bilesenleri_yukle();

    }



    private void bilesenleri_yukle(){

        ana_Sayfa = (ConstraintLayout)findViewById(R.id.anaSayfa_id);
        saat_ayari = (RadioButton)findViewById(R.id.saat_ayari);
        dakika_ayari = (RadioButton)findViewById(R.id.dakika_ayari);
        saat_ayari.setOnClickListener(this);
        dakika_ayari.setOnClickListener(this);
        saat_dakika_group = (RadioGroup)findViewById(R.id.radio_group_id1);

        gecen_sure = (TextView)findViewById(R.id.gecen_sure_txt);
        kalan_adim_sayisi = (TextView)findViewById(R.id.kalan_adim_sayisi_txt);
        baslat = (Button)findViewById(R.id.baslat);
        iki_titreme_arasi_beklenen_sure = (EditText)findViewById(R.id.iki_titresim_arasi_gecen_sure_edit);
        test_dongu_sayisi = (EditText)findViewById(R.id.dongu_sayisi_edit);
        dongu_saat_txt = (TextView)findViewById(R.id.dongu_saat_txt);
        test_titreme_suresi = (EditText)findViewById(R.id.titreme_suresi_edit);
        kalan_miktar_progress = (ProgressBar)findViewById(R.id.kalan_adim_sayisi_progress);
        not_txt = (TextView)findViewById(R.id.not);
        baslat.setOnClickListener(this);
        saat_bazinda = (RadioButton) findViewById(R.id.saat_bazinda);
        saat_bazinda.setOnClickListener(this);
        dongusel = (RadioButton) findViewById(R.id.dongu_bazinda);
        dongusel.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        if (view == baslat) {

            if(!test_aktif){

                dongusel.setEnabled(false);
                saat_bazinda.setEnabled(false);
                Sure_Baslat();
                test_aktif = true;
                baslat.setText("Bitir");
                myTask = new MyTask();
                myTask.execute();

                Log.d("QaGeneral", "view : baslat " + baslat.getId());



            }else if (test_aktif){

                dongusel.setEnabled(true);
                saat_bazinda.setEnabled(true);
                baslat.setText("Başlat");
                kalan_miktar_progress.setProgress(0);
                test_aktif = false;
                myTask.onCancelled();
                count = 1;


            }


        }else if(view == saat_bazinda){

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ana_Sayfa.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.arka_plan_zaman) );
            } else {
                ana_Sayfa.setBackground(ContextCompat.getDrawable(this, R.drawable.arka_plan_zaman));
            }
            kalan_adim_sayisi.setText("Kalan Saniye :");
            saat_dakika_group.setVisibility(View.VISIBLE);
            dongu_saat_txt.setText("Çalışma Süresi (Dk)");
            test_dongu_sayisi.setText("10");
            Log.d("QaGeneral", "view : saat_bazinda " + saat_bazinda.getId());

        }else if(view == dongusel){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ana_Sayfa.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.arka_plan_dongusel) );
            } else {
                ana_Sayfa.setBackground(ContextCompat.getDrawable(this, R.drawable.arka_plan_dongusel));
            }
            kalan_adim_sayisi.setText("Kalan Döngü Sayısı :");
            saat_dakika_group.setVisibility(View.GONE);
            test_dongu_sayisi.setText("50");
            dongu_saat_txt.setText("Döngü Sayısını Giriniz");
            Log.d("QaGeneral", "view : dongusel " + dongusel.getId());

        }else if(view == saat_ayari){
            test_dongu_sayisi.setText("2");
            dongu_saat_txt.setText("Çalışma Süresi (Saat)");
            Log.d("QaGeneral", "view : dongusel " + saat_ayari.getId());

        }else if(view == dakika_ayari){
            test_dongu_sayisi.setText("10");
            dongu_saat_txt.setText("Çalışma Süresi (Dk)");
            Log.d("QaGeneral", "view : dongusel " + dakika_ayari.getId());

        }

    }

  public class MyTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {

               for (; count <= adim_sayisi; count++) {
                   try {

                       Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                           v.vibrate(VibrationEffect.createOneShot(titreme_suresi, VibrationEffect.DEFAULT_AMPLITUDE));

                       } else {

                           v.vibrate(titreme_suresi);
                       }

                       Thread.sleep(titreme_suresi);

                       Thread.sleep(titreme_arasi_beklenen_sure);

                       if(saat_bazinda.isChecked()){

                           count = 0;
                           if((saniye - eksi_sure) > yapilacak_saniye){

                               testi_bitiridli();
                               return "Test Tamamlandı";
                           }
                           publishProgress(second);

                       }else{
                           publishProgress(count);

                       }


                       if(!test_aktif){
                           testi_bitiridli();
                           return "Test Durduruldu";
                       }

                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }

               }
            return "Test Tamamlandı";
        }
        @Override
        protected void onPostExecute(String result) {
            testi_bitiridli();

        }
        @Override
        protected void onPreExecute() {

            titreme_arasi_beklenen_sure = Integer.parseInt(iki_titreme_arasi_beklenen_sure.getText().toString());
            adim_sayisi = Integer.parseInt(test_dongu_sayisi.getText().toString());
            titreme_suresi = Integer.parseInt(test_titreme_suresi.getText().toString());


            if(dongusel.isChecked()){

                kalan_miktar_progress.setMax(adim_sayisi + 1);

            }else{

                if(saat_ayari.isChecked()){
                    yapilacak_saniye = ((adim_sayisi * 60) * 60);
                }else{
                    yapilacak_saniye = (adim_sayisi * 60);
                }

                kalan_miktar_progress.setMax(yapilacak_saniye + 1);
            }


        }
        @Override
        protected void onProgressUpdate(Integer... values) {

            if(dongusel.isChecked()){
                kalan_adim_sayisi.setText("Kalan Adım Sayısı : " + (adim_sayisi - Integer.valueOf(values[0])));
                kalan_miktar_progress.setProgress(values[0]);
            }else{

                kalan_adim_sayisi.setText("Kalan Saniye : " + ((yapilacak_saniye) - Integer.valueOf(values[0])));
                kalan_miktar_progress.setProgress(values[0]);

            }


        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), "Test Durduruldu", Toast.LENGTH_SHORT).show();
            timer.cancel();
            super.onCancelled();
        }

    }
    Timer timer;
    private void Sure_Baslat(){

        final Handler handler = new Handler();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String stopwatch = "";
                        if(hour > 0){
                            stopwatch =  hour + " Saat " + minute + " Dakika " + second + " Saniye";
                        }else if (minute > 0){
                            stopwatch = minute + " Dakika " + second + " Saniye";
                        }else if (second > 0){
                            stopwatch = second + " Saniye";
                        }

                        gecen_sure.setText("Geçen Süre : " + stopwatch);

                        if(second>0 && second%59==0){
                            minute++;
                        }
                        if(minute>1 &&minute%59==0){
                            hour++;
                            minute=0;
                        }
                        if(hour>23){
                            hour=0;
                        }
                        if(second>59){
                            second=0;
                        }
                        second++;
                        saniye++;
                    }
                });
            }
        };

        timer = new Timer();

        timer.schedule(timerTask,0,1000);

    }

    private void testi_bitiridli(){
        count = 1;
        kalan_miktar_progress.setProgress(0);
        minute = 0;
        hour = 0;
        second = 0;
        baslat.setText("Başlat");
        yapilacak_saniye = 0;
        eksi_sure = 0;
        saniye = 0;
        timer.cancel();
        test_aktif = false;
        dongusel.setEnabled(true);
        saat_bazinda.setEnabled(true);
    }


}
