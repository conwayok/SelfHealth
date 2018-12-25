package com.eebrian123tw.kable2580.selfhealth.googleFit;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class GoogleFitRecording {
    public static Task<Void> fitRecordingSubscribe(Context context, DataType dataType) {
        return Fitness.getRecordingClient(context, GoogleSignIn.getLastSignedInAccount(context))
                .subscribe(dataType);
    }

    public static Task<Void> fitRecordingUnsubscribe(Context context, DataType dataType) {
        return Fitness.getRecordingClient(context, GoogleSignIn.getLastSignedInAccount( context))
                .unsubscribe(dataType);
    }

    public static Task<List<Subscription>> fitRecordingListSubscribe(Context context, DataType dataType) {
        return Fitness.getRecordingClient(context, GoogleSignIn.getLastSignedInAccount(context))
                .listSubscriptions();
    }
}
