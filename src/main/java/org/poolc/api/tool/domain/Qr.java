package org.poolc.api.tool.domain;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Qr {
    private final int WIDTH = 400;
    private final int HEIGHT = 400;
    private final String FORMAT = "PNG";
    private final String str;

    public Qr(String str) {
        this.str = str;
    }

    public byte[] createQrImage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            BitMatrix m = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
            MatrixToImageWriter.writeToStream(m, FORMAT, out);
        } catch (WriterException | IOException e) {
            throw new IllegalArgumentException(e);
        }

        return out.toByteArray();
    }
}
