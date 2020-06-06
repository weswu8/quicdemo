package com.weswu.cloudcdn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import org.chromium.net.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuicGrabber {
    private static String TAG = "QuicGrabber";
    private static CronetEngine cronetEngine;
    private Context context;
    private static Object sLock = new Object();


    public QuicGrabber(Context context) {
        this.context = context;
        getCronetEngine(this.context);
    }

    private static synchronized CronetEngine getCronetEngine(Context context) {
        if (cronetEngine == null) {
            CronetEngine.Builder myBuilder = new CronetEngine.Builder(context);
            cronetEngine = myBuilder
                    .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_DISABLED, 100 * 1024)
                    .addQuicHint("cdn.obwiz.com", 443, 443)
                    .enableQuic(true)
                    //.enableHttp2(true)
                    .setUserAgent(from(context))
                    .build();
            //CHECK USER AGENT
            String params =myBuilder.getDefaultUserAgent();
            Log.i(TAG, from(context));

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
        private static final String TAG = "QuicUrlRequestCallback";
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

            // Now we have response headers!
            int httpStatusCode = info.getHttpStatusCode();
            if (httpStatusCode == 200) {
                // Success! Let's tell Cronet to read the response body.
                request.read(ByteBuffer.allocateDirect(102400));
            } else if (httpStatusCode == 503) {
                // Do something. Note that 4XX and 5XX are not considered
                // errors from Cronet's perspective since the response is
                // successfully read.
            }
        }

        @Override
        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
            Log.i(TAG, "onReadCompleted method called.");
            // You should keep reading the request until there's no more data.
            byteBuffer.flip();
            try {
                receiveChannel.write(byteBuffer);
            } catch (IOException e) {
                android.util.Log.i(TAG, "IOException during ByteBuffer read. Details: ", e);
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
            Stats.calcLatency(stop - start, 0, totalBytes);

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


    public static String from(Context context) {
        StringBuilder builder = new StringBuilder();
        // Our package name and version.
        builder.append(context.getPackageName());
        builder.append('/');
        builder.append("1.0");
        // The platform version.
        builder.append(" (Linux; U; Android ");
        builder.append(Build.VERSION.RELEASE);
        builder.append("; ");
        builder.append(Locale.getDefault().toString());
        String model = Build.MODEL;
        if (model.length() > 0) {
            builder.append("; ");
            builder.append(model);
        }
        String id = Build.ID;
        if (id.length() > 0) {
            builder.append("; Build/");
            builder.append(id);
        }
        builder.append("; ");
        // This is the API version not the implementation version.
        builder.append("Cronet/80.0.3987.149");
        builder.append(')');
        return builder.toString();
    }
}
