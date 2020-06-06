package com.weswu.cloudcdn;

public class Stats {
    private static final String TAG = "MainActivity";
    private static long [] latencies = new long[]{0, 0};
    private static long [] totalBytes = new long[]{0, 0};
    private static long [] imgCount = new long[]{0, 0};
    private static Object[] mutex = new Object[]{new Object(), new Object()};

    public static void reset(){
        latencies = new long[]{0, 0};
        totalBytes = new long[]{0, 0};
        imgCount = new long[]{0, 0};
    }

    public static void calcLatency(long cronetLatency, final int type, final long imgBytes) {
        if (type == 0) {
            synchronized (mutex[0]) {
                latencies[0] += cronetLatency;
                totalBytes[0] += imgBytes;
                imgCount[0]++;
            }
        } else {
            synchronized (mutex[1]) {
                latencies[1] += cronetLatency;
                totalBytes[1] += imgBytes;
                imgCount[1]++;
            }
        }
        android.util.Log.i(TAG,
                imgCount[0] + " Cronet Quic Requests Complete, all: " + Images.count());
        android.util.Log.i(TAG,
                imgCount[1] + " Cronet Http2 Requests Complete, all: " + Images.count());
        if (imgCount[0] == Images.count() || imgCount[1] == Images.count()) {
            final long avgLatency;
            if (type == 0) {
                avgLatency = latencies[0] / imgCount[0];
            } else {
                avgLatency = latencies[1] / imgCount[1];
            }
            MainActivity.instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (type == 0) {
                        MainActivity.instance.quicResText.setText(String.format(MainActivity.instance.getResources()
                                .getString(R.string.quic_images_loaded), avgLatency / 1000000, totalBytes[0]));
                    } else {
                        MainActivity.instance.httpsResText.setText(String.format(MainActivity.instance.getResources()
                                .getString(R.string.https_images_loaded), avgLatency / 1000000, totalBytes[1]));
                    }
                }
            });
        }
    }
}
