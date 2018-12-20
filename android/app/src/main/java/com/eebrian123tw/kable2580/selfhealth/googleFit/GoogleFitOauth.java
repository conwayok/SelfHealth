package com.eebrian123tw.kable2580.selfhealth.googleFit;
import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.Task;

public class GoogleFitOauth {
    public static final int REQUEST_GOOLE_FIT_OAUTH_REQUEST_CODE = 1001;

    public static FitnessOptions fitnessOptions() {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)

                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)

                .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_READ)
                .build();
        return fitnessOptions;
    }

    public static boolean hasOauth(Activity activity) {
        return GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(activity), fitnessOptions());
    }

    public static void oauthRequest(Activity activity) {
        GoogleSignIn.requestPermissions(
                activity,
                REQUEST_GOOLE_FIT_OAUTH_REQUEST_CODE,
                GoogleSignIn.getLastSignedInAccount(activity),
                fitnessOptions());
    }

    public static void disconnectGoogleFit(Activity activity) {
         Fitness.getConfigClient(activity, GoogleSignIn.getLastSignedInAccount(activity)).disableFit();
    }


}
