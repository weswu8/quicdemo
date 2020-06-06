package com.weswu.cloudcdn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HttpsGrabber {
    private static String TAG = "HttpsGrabber";
    private static CronetEngine cronetEngine;
    private Context context;


    public HttpsGrabber(Context context) {
        this.context = context;
        getCronetEngine(this.context);
    }

    private static synchronized CronetEngine getCronetEngine(Context context) {
        if (cronetEngine == null) {
            CronetEngine.Builder myBuilder = new CronetEngine.Builder(context);
            cronetEngine = myBuilder
                    .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_DISABLED, 100 * 1024)
                    .enableHttp2(true)
                    .build();
        }
        return cronetEngine;
    }

    public void grabFromUrl(ImageView imageView, String url){
        Executor executor = Executors.newSingleThreadExecutor();
        UrlRequest.Callback callback = new urlRequestCallback(imageView,
                this.context);
        UrlRequest.Builder builder = cronetEngine.newUrlRequestBuilder(
                url, callback, executor);
        // Measure the start time of the request so that
        // we can measure latency of the entire request cycle
        ((urlRequestCallback) callback).start = System.nanoTime();
        // Start the request
        builder.build().start();
    }

    class urlRequestCallback extends UrlRequest.Callback {
        private static final String TAG = "HttpsUrlRequestCallback";
        private ByteArrayOutputStream bytesReceived = new ByteArrayOutputStream();
        private WritableByteChannel receiveChannel = Channels.newChannel(bytesReceived);
        private ImageView imageView;
        public long start;
        private long stop;
        private MainActivity mainActivity;

        urlRequestCallback(ImageView imageView, Context context) {
            this.imageView = imageView;
            this.mainActivity = MainActivity.instance;
        }

        @Override
        public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
            Log.i(TAG, "onRedirectReceived method called.");
            // You should call the request.followRedirect() method to continue
            // processing the request.
            request.followRedirect();
        }

        @Override
        public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onResponseStarted method called.");
            // You should call the request.read() method before the request can be
            // further processed. The following instruction provides a ByteBuffer object
            // with a capacity of 102400 bytes to the read() method.
            request.read(ByteBuffer.allocateDirect(102400));
        }

        @Override
        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
            Log.i(TAG, "onReadCompleted method called.");
            Log.i("HttpsGrabber", " URI: " + request.toString());
            // You should keep reading the request until there's no more data.
            byteBuffer.flip();
            try {
                receiveChannel.write(byteBuffer);
            } catch (IOException e) {
                Log.i(TAG, "IOException during ByteBuffer read. Details: ", e);
            }
            byteBuffer.clear();
            request.read(byteBuffer);
        }

        @Override
        public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            stop = System.nanoTime();
            Log.i(TAG, "onSucceeded method called.");
            // Set the latency
            long totalBytes = info.getReceivedByteCount()/1024;
            Stats.calcLatency(stop - start, 1, totalBytes);

            byte[] byteArray = bytesReceived.toByteArray();
            final Bitmap bimage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            this.mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    imageView.setImageBitmap(bimage);
                }
            });
        }

        @Override
        public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
            Log.i(TAG, "****** onFailed, error is: " + error.getMessage());
        }
    }


}
