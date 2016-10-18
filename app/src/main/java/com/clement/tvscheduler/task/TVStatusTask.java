package com.clement.tvscheduler.task;

import android.util.JsonReader;
import android.util.Log;

import com.clement.tvscheduler.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Clément on 09/07/2016.
 */
public class TVStatusTask extends BaseTask {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    TVStatus tvStatus;
    private String messageRetour;

    public TVStatusTask(MainActivity mainActivity) {

        super(mainActivity);
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            Log.i(MainActivity.TAG, "Execution " + this.getClass());
            InputStream is = getInputStreamConnection("/tvscheduler/tvstatus");
            tvStatus = readJsonStream(is);
            messageRetour = "Succès";
            Log.i(MainActivity.TAG, "Execution après");
            return 0L;
        } catch (Exception e) {
            Log.e(MainActivity.TAG, e.getMessage());
        }
        messageRetour = "Service non disponible";
        return 0L;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        if (tvStatus != null) {
            mainActivity.setTimeRemaining(tvStatus.getRemainingTime());
            if (tvStatus.getStatusRelay()) {
                mainActivity.setRelayStatus("Télé autorisée");
            } else {
                mainActivity.setRelayStatus("Télé interdite");
            }
            if (tvStatus.getNextCreditOn() != null) {
                mainActivity.setNextCredit(tvStatus.getNextCreditOn(), tvStatus.getNextCreditAmount());
            }
            if (tvStatus.getConsumedToday() != null) {
                mainActivity.setConsumedToday(tvStatus.getConsumedToday());
            }
            if (tvStatus.getActiveTV()) {
                mainActivity.setTvStatus("La télé est ON");
            }
            else{
                mainActivity.setTvStatus("La télé est OFF");
            }
        } else {
            mainActivity.showMessage("Impossible de se connecter au serveur");
        }


    }

    /**
     * @param in
     * @return
     * @throws IOException
     */
    public TVStatus readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {

            return readTvStatus(reader);
        } finally {
            reader.close();
        }
    }

    /**
     * @param reader
     * @return
     * @throws IOException
     */
    public List<Double> readDoublesArray(JsonReader reader) throws IOException {
        List<Double> doubles = new ArrayList<Double>();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }

    /**
     * This reads the json.
     *
     * @param reader
     * @return
     * @throws IOException
     */
    public TVStatus readTvStatus(JsonReader reader) throws IOException {
        Log.d(MainActivity.TAG, "Decryptage du TV status");

        TVStatus tvStatus = new TVStatus();

        String remainingTime = null;
        Date nextCredit = null;
        Integer nextAmount = null;
        String minutesToday = "-";
        Boolean relayStatus = false;
        Boolean activeTv = false;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            Log.i(MainActivity.TAG, "Decryptage du TV status entry name " + name);

            if (name.equals("remainingTime")) {
                remainingTime = reader.nextString();
            } else if (name.equals("relayStatus")) {
                relayStatus = reader.nextBoolean();
            } else if (name.equals("dateOfCredit")) {
                String dateInt = reader.nextString();
                Log.i(MainActivity.TAG, "Lecture de la date " + dateInt);
                try {
                    nextCredit = df.parse(dateInt);
                } catch (ParseException e) {
                    Log.i(MainActivity.TAG, e.getMessage(), e);

                }
            } else if (name.equals("minutesToday")) {
                Integer minutesInt = reader.nextInt();
                Log.i(MainActivity.TAG, "Lecture des minutes " + minutesInt);
                minutesToday = "Minutes consommées: " + minutesInt;
            } else if (name.equals("amountOfCreditInMinutes")) {
                nextAmount = reader.nextInt();
            } else if (name.equals("activeStandbyState")) {
                activeTv = (reader.nextInt() != 1);
            } else {
                reader.skipValue();
            }
        }
        tvStatus.setRemainingSecond(remainingTime);
        tvStatus.setStatusRelay(relayStatus);
        tvStatus.setNextCreditOn(nextCredit);
        tvStatus.setNextCreditAmount(nextAmount);
        tvStatus.setConsumedToday(minutesToday);
        tvStatus.setActiveTV(activeTv);

        reader.endObject();
        return tvStatus;
    }


}