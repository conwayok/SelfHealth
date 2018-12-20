package com.eebrian123tw.kable2580.selfhealth.googleFit;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class GoogleFitRecording {
    public static Task<Void> fitRecordingSubscribe(Activity activity, DataType dataType) {
        return Fitness.getRecordingClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .subscribe(dataType);
    }

    public static Task<Void> fitRecordingUnsubscribe(Activity activity, DataType dataType) {
        return Fitness.getRecordingClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .unsubscribe(dataType);
    }

    public static Task<List<Subscription>> fitRecordingListSubscribe(Activity activity, DataType dataType) {
        return Fitness.getRecordingClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .listSubscriptions();
    }
}
