package com.aispeech.upgradeaar;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class SpeechTestAudioRecord {


    private static final String TAG = "SpeechTestAudioRecord";
    static final int SAMPLE_RATE_IN_HZ = 44100;
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
    AudioRecord mAudioRecord;
    boolean isGetVoiceRun;
    Object mLock;
    private File wavFile;
    private File pcmFile;

    public SpeechTestAudioRecord() {
        //mLock = new Object();
        this.wavFile = null;
        this.pcmFile = null;
    }


    public SpeechTestAudioRecord(File pcmfile,File wavfile) {
        this.pcmFile = pcmfile;
        this.wavFile = wavfile;
    }

    public void startRecord() {
        if (isGetVoiceRun) {
            Log.e(TAG, "还在录着呢");
            return;
        }



        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
        if (mAudioRecord == null) {
            Log.e("sound", "mAudioRecord初始化失败");
        }



        isGetVoiceRun = true;
         Log.e(TAG, "＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os_pcmfile = null;
                OutputStream os_wavfile = null;
                long totalAudioLen = 0;
                long totalDataLen = totalAudioLen + 36;
                int channels = 1;
                long byteRate = 16 *SAMPLE_RATE_IN_HZ * channels / 8;
                try {
                    os_pcmfile = new BufferedOutputStream(new FileOutputStream(pcmFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    os_wavfile = new BufferedOutputStream(new FileOutputStream(wavFile));
                    byte[] header = new byte[44];
                    try {
                        os_wavfile.write(header);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    try {
//                        WriteWaveFileHeader(os_wavfile,totalAudioLen,totalDataLen,SAMPLE_RATE_IN_HZ,channels,byteRate);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mAudioRecord.startRecording();
                byte[] buffer = new byte[BUFFER_SIZE];
                int countdb=0;
                while (isGetVoiceRun) {
                    //r是实际读取的数据长度，一般而言r会小于buffersize
                    int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
                    if(r>0)
                    {
                        try {
                            os_pcmfile.write(buffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            os_wavfile.write(buffer);
                            totalAudioLen = totalAudioLen + r;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
//                    if(countdb == 10) {
//                        countdb = 0;
//                        long v = 0;
//                        // 将 buffer 内容取出，进行平方和运算
//                        for (int i = 0; i < buffer.length; i++) {
//                            v += buffer[i] * buffer[i];
//                        }
//
//                        // 平方和除以数据总长度，得到音量大小。
//                        double mean = v / (double) r;
//                        double volume = 10 * Math.log10(mean);
//                        int vol = (int) volume;
//                        Log.d(TAG, "分贝值:" + volume);
//                        SpeechTestActivity.instance.handler.obtainMessage(1, vol).sendToTarget();
//                    }
//                    countdb++;
//                    synchronized (mLock) {
//                        try {
//                            mLock.wait(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }

                if(os_pcmfile != null)
                {
                    try {
                        os_pcmfile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(os_wavfile != null)
                {
                    try {
                        os_wavfile.close();
                        RandomAccessFile raf = null;
                        raf = new RandomAccessFile(wavFile, "rw");
                        totalDataLen = totalAudioLen + 36;
                        WriteWaveFileHeader(raf,totalAudioLen,totalDataLen,SAMPLE_RATE_IN_HZ,channels,byteRate);
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "完成:" );

                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
            }
        }).start();
    }


    public void stopRecord(){

        isGetVoiceRun=false;
    }

    private void WriteWaveFileHeader(RandomAccessFile out, long totalAudioLen, long totalDataLen, long longSampleRate,
                                     int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);//数据大小
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';//WAVE
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //FMT Chunk
        header[12] = 'f'; // 'fmt '
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';//过渡字节
        //数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;
        //通道数
        header[22] = (byte) channels;
        header[23] = 0;
        //采样率，每个通道的播放速度
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        header[32] = (byte) (1 * 16 / 8);
        header[33] = 0;
        //每个样本的数据位数
        header[34] = 16;
        header[35] = 0;
        //Data chunk
        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.seek(0);
        out.write(header, 0, 44);
    }
}
