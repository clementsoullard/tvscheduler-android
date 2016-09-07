package com.clement.tvscheduler.task;

import android.util.JsonReader;
import android.util.Log;

import com.clement.tvscheduler.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Clément on 09/07/2016.
 */
public class TVStatusTask extends BaseTask {


    TVStatus tvStatus;
    String baseUrl;
    private String messageRetour;

    public TVStatusTask(MainActivity mainActivity, String baseUrl) {

        super(mainActivity);
        this.baseUrl = baseUrl;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            Log.i(MainActivity.TAG, "Execution " + this.getClass());
            InputStream is = getInputStreamConnection(new URL(baseUrl + "/tvscheduler/tvstatus"));
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
        mainActivity.credited(messageRetour);
        if (tvStatus != null) {
            mainActivity.setTimeRemaining(tvStatus.getRemainingTime());
        }
        if (tvStatus.getStatusRelay()) {
            mainActivity.setRelayStatus("Tele autorisée");
        } else {
            mainActivity.setRelayStatus("Tele non autorisée");
        }
        if (tvStatus.getNextCreditOn() != null) {
            mainActivity.setNextCredit(tvStatus.getNextCreditOn(), tvStatus.getNextCreditAmount());
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
        Log.i(MainActivity.TAG, "Decryptage du TV status");

        TVStatus tvStatus = new TVStatus();

        String remainingTime = null;
        Date nextCredit = null;
        Integer nextAmount = null;
        Boolean relayStatus = false;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            Log.i(MainActivity.TAG, "Decryptage du TV status entry name " + name);

            if (name.equals("remainingTime")) {
                remainingTime = reader.nextString();
            } else if (name.equals("relayStatus")) {
                relayStatus = reader.nextBoolean();
            } else if (name.equals("dateOfCredit")) {
                Long dateInt = reader.nextLong();
                Log.i(MainActivity.TAG, "Lecture de la date " + dateInt);
                nextCredit = new Date(dateInt);
            } else if (name.equals("amountOfCreditInMinutes")) {
                nextAmount = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        tvStatus.setRemainingSecond(remainingTime);
        tvStatus.setStatusRelay(relayStatus);
        tvStatus.setNextCreditOn(nextCredit);
        tvStatus.setNextCreditAmount(nextAmount);
        reader.endObject();
        return tvStatus;
    }


}