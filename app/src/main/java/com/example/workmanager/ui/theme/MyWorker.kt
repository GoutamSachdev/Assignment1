package com.example.workmanager.ui.theme

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParameters: WorkerParameters)
    :Worker(context,workerParameters) {
    interface Callback {
        fun onWorkCompleted(message: String)
    }

    override fun doWork(): Result {
        var isImageOpenLoaded=false
        val successMessage = "Login successful"
        val inputData = inputData

        val email = inputData.getString("email")
        val password = inputData.getString("password")
        Log.d("WorkerManger","value   $email $password")


        if(email.equals("goutam@gmail.com") && password.equals( "123")) {

            // Notify UI using callback
            (applicationContext as? Callback)?.onWorkCompleted(successMessage)
            return Result.success()
        }
        else
            return Result.retry()


    }

}