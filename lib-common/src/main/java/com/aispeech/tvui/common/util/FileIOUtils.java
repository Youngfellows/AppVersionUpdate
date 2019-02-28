package com.aispeech.tvui.common.util;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;



public class FileIOUtils {
    private static int sBufferSize = 8192;

    //不可以实例化该类
    private FileIOUtils() {
        throw new UnsupportedOperationException("can't instantiate class FileIOUtils");
    }

    /**
     * write file from input stream
     * @param filePath (文件路径)
     * @param is        输入流
     * @return
     */
    public static boolean writeFileFromIS(final String filePath, final InputStream is) {
        return writeFileFromIS(FileUtil.getFileByPath(filePath), is, false);
    }

    /**
     * Write file from input stream.
     * @param filePath The path of file.
     * @param is       The input stream.
     * @param append   True to append, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */

    public static boolean writeFileFromIS(final String filePath,final InputStream is,final boolean append) {
        return writeFileFromIS(FileUtil.getFileByPath(filePath), is, append);
    }

    /**
     * Write file from input stream.
     * @param file The file.
     * @param is   The input stream.
     * @return {@code true}: success<br>{@code false}: fail
     */

    public static boolean writeFileFromIS(final File file, final InputStream is) {
        return writeFileFromIS(file, is, false);
    }

    /**
     * Write file from input stream.
     * @param file   The file.
     * @param is     The input stream.
     * @param append True to append, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */

    public static boolean writeFileFromIS(final File file, final InputStream is, final boolean append) {
        if (!FileUtil.createOrExistsFile(file) || is == null){
            return false;
        }
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[sBufferSize];
            int len;
            while ((len = is.read(data, 0, sBufferSize)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Write file from bytes by stream.
     * @param filePath
     * @param bytes
     * @return
     */
    public static boolean writeFileFromBytesByStream(final String filePath, final byte[] bytes) {
        return writeFileFromBytesByStream(FileUtil.getFileByPath(filePath), bytes, false);
    }



    /**
     * Write file from bytes by stream.
     *
     * @param filePath The path of file.
     * @param bytes    The bytes.
     * @param append   True to append, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */

    public static boolean writeFileFromBytesByStream(final String filePath, final byte[] bytes, final boolean append) {
        return writeFileFromBytesByStream(FileUtil.getFileByPath(filePath), bytes, append);
    }



    /**
     * Write file from bytes by stream.
     *
     * @param file  The file.
     * @param bytes The bytes.
     * @return {@code true}: success<br>{@code false}: fail
     */

    public static boolean writeFileFromBytesByStream(final File file, final byte[] bytes) {
        return writeFileFromBytesByStream(file, bytes, false);
    }



    /**
     * Write file from bytes by stream.
     * @param file   The file.
     * @param bytes  The bytes.
     * @param append True to append, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */

    public static boolean writeFileFromBytesByStream(final File file, final byte[] bytes, final boolean append) {
        if (bytes == null || !FileUtil.createOrExistsFile(file)){
            return false;
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file, append));
            bos.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Write file from bytes by channel.
     * @param filePath The path of file.
     * @param bytes    The bytes.
     * @param isForce  是否写入文件
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean writeFileFromBytesByChannel(final String filePath, final byte[] bytes, final boolean isForce) {
        return writeFileFromBytesByChannel(FileUtil.getFileByPath(filePath), bytes, false, isForce);
    }

    /**
     * Write file from bytes by channel.
     * @param filePath The path of file.
     * @param bytes    The bytes.
     * @param append   True to append, false otherwise.
     * @param isForce  True to force write file, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */

    public static boolean writeFileFromBytesByChannel(final String filePath, final byte[] bytes, final boolean append, final boolean isForce) {
        return writeFileFromBytesByChannel(FileUtil.getFileByPath(filePath), bytes, append, isForce);
    }

    /**
     * Write file from bytes by channel.
     * @param file    The file.
     * @param bytes   The bytes.
     * @param isForce True to force write file, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean writeFileFromBytesByChannel(final File file, final byte[] bytes, final boolean isForce) {
        return writeFileFromBytesByChannel(file, bytes, false, isForce);
    }



    /**
     * Write file from bytes by channel.
     * @param file    The file.
     * @param bytes   The bytes.
     * @param append  True to append, false otherwise.
     * @param isForce True to force write file, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean writeFileFromBytesByChannel(final File file, final byte[] bytes, final boolean append, final boolean isForce) {
        if (bytes == null){
            return false;
        }
        FileChannel fc = null;
        try {
            fc = new FileOutputStream(file, append).getChannel();
            fc.position(fc.size());
            fc.write(ByteBuffer.wrap(bytes));
            if (isForce){
                fc.force(true);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 输入流转String字符串
     * @param is
     * @return
     */
    public static String inputStream2String(InputStream is){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    private static byte[] is2Bytes(final InputStream is) {
        if (is == null){
            return null;
        }
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            byte[] b = new byte[sBufferSize];
            int len;
            while ((len = is.read(b, 0, sBufferSize)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
